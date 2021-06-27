package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.service.impl.ChangeFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.ChangeFormVO;
import com.ebizprise.winw.project.vo.FormImpactAnalysisVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 變更單 新增/修改/審核/退回等作業
 * 
 * @author AndrewLee
 */
@RestController
@RequestMapping("/changeForm")
public class ChangeFormController extends BaseController implements IBaseForm<ChangeFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(ChangeFormController.class);

    @Autowired
    private ChangeFormServiceImpl service;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private ICommonFormService commonFormService;
    @Autowired
    private ISystemConfigService systemConfigService;
    
    /**
     * 產生變更單
     */
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build (@RequestBody ChangeFormVO vo) {
        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return BeanUtil.toJson(vo);
        }
        
        if (service.isFormClosed(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return BeanUtil.toJson(vo);
        }
        
        vo.setFormClass(FormEnum.CHG.name());
        vo.setFormWording(getMessage(FormEnum.CHG.wording()));
        service.create(vo);
        
        return BeanUtil.jEscape(BeanUtil.toJson(vo));
    }

    @Override
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView initPage () {
        return new ModelAndView(
                DispatcherEnum.FORM_SEARCH.dispatch(), "info", request.getParameter("formPostData"));
    }
    
    @Override
    @CtlLog
    @PostMapping(path = "/info")
    public ChangeFormVO info(@RequestBody ChangeFormVO vo) {
        if(StringUtils.isNotBlank(vo.getSourceId())) {
            service.getFormInfo(vo);
        } else {
            logger.error("錯誤!來源表單ID不存在,無法取得變更單資訊!");
        }
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public ChangeFormVO approval(@RequestBody ChangeFormVO vo) {
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        if (service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        service.prepareVerifying(vo);
        service.verifying(vo);
        // 鎖定該表單所有檔案的狀態,改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知下一關的審核群組
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/reject")
    public ChangeFormVO reject(@RequestBody ChangeFormVO vo) {
        SysUserVO loginUser = getUserInfo();
        
        if (!service.isVerifyAcceptable(vo, loginUser)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }

        boolean isPic = sysUserService.isPic(loginUser.getGroupId());
        
        if (isPic && service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        vo.setVerifyResult(FormEnum.DISAGREED.name());
        service.verifying(vo);

        if (vo.isBackToApplyLevel1()) {
            // 當被退回申請階段第一步驟時，解鎖檔案
            service.unlockFormFileStatus(vo.getFormId());
        } else {
            // 鎖定該表單所有檔案的狀態,改為不能刪除
            service.lockFormFileStatus(vo.getFormId());
        }

        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/deprecated")
    public ChangeFormVO deprecated(@RequestBody ChangeFormVO vo) {
        if (!service.isAllStretchDied(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.4"));
            return vo;
        }
        
        vo.setFormStatus(FormEnum.DEPRECATED.name());
        vo.setProcessStatus(FormEnum.DEPRECATED.name());
        vo.setVerifyResult(FormEnum.DEPRECATED.name());
        service.verifying(vo);
        
        // 寄信通知經辦人員
        service.notifyProcessMail(vo);

        return vo;
    }

    @Override
    @FormLock
    @ModifyRecord
    @PostMapping(path = "/save")
    public ChangeFormVO save(@RequestBody ChangeFormVO vo) {
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        
        if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return vo;
        }

        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return vo;
        }
        if (isAdmin) {
        	service.updateVerifyLog(vo);
        }
        
        service.mergeFormInfo(vo);
        
        return vo;
    }
    
    @Override
    @FormLock
    @PostMapping(path = "/modifyColsByVice")
    public ChangeFormVO modifyColsByVice(@RequestBody ChangeFormVO vo) {
        service.mergeFormInfo(vo);
        service.updateVerifyLog(vo);
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // 新增一筆簽核紀錄(簽核意見=修改原因)
        service.createVerifyCommentByVice(vo);
        // 鎖定該表單所有檔案的狀態，改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/send")
    public ChangeFormVO send(@RequestBody ChangeFormVO vo) {
        vo.setCreateTime(new Date());
        vo.setFormStatus(FormEnum.APPROVING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());
        service.mergeFormInfo(vo);
        service.sendToVerification(vo);
        
        // 鎖定該表單所有檔案的狀態,改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        
        // 寄信通知下一關的審核群組
        vo.setVerifyResult(FormEnum.SENT.name());
        service.notifyProcessMail(vo);
       
        return vo;
    }

    @Override
    @PostMapping(path = "/delete")
    public void delete(@RequestBody ChangeFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody ChangeFormVO vo) {
        return validateColumnData(vo, false);
    }
    
    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (ChangeFormVO vo, boolean isJustInfo) {
        String fileVerifyResult = service.checkAttachmentExists(vo);
        boolean isAdmin = sysUserService.isAdmin();
        List<FormImpactAnalysisVO> impactList = commonFormService.getFormImpactAnalysis(vo.getFormId());
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                    .string(vo.getUnitCategory(), this.getMessage("form.change.change.source.department.type"))      // 提出單位分類
                    .string(vo.getUnitId(), this.getMessage("form.change.change.source.user.department"))            // 提出人員單位
                    .string(vo.getUserName(), this.getMessage("form.change.change.source.user.name"))                // 提出人員姓名
                    .string(vo.getcCategory(), this.getMessage("form.change.form.info.change.c.category"))           // 組態分類
                    .string(vo.getcClass(), this.getMessage("form.change.form.info.change.c.class"))                 // 組態元件類別
                    .string(vo.getcComponent(), this.getMessage("form.change.form.info.change.c.class"))             // 組態元件
                    .string(vo.getSummary(), this.getMessage("form.change.form.info.change.summary"))                // 變更摘要
                    .string(vo.getContent(), this.getMessage("form.change.form.info.change.content"))                // 變更內容
                    .string(vo.getEffectSystem(), this.getMessage("form.change.form.info.change.effect.system"))     // 變更影響系統
                    .string(vo.getChangeType(), this.getMessage("form.change.form.info.change.type"))                // 變更類型
                    .string(vo.getChangeRank(), this.getMessage("form.change.form.info.change.rank"))                // 變更等級
                    .date(vo.getCct(), this.getMessage("form.change.form.info.cct"));                                // 預計變更結束時間
        
        if (isJustInfo) {
            return verifyUtil.build();
        }

        String solution = vo.getImpactForm().getSolution();
        String evaluation = vo.getImpactForm().getEvaluation();
        String totalFraction = vo.getImpactForm().getTotalFraction();
        
        // 是否需要填 衝擊分析
        if (isNeedFraction(vo)) {
            vo.setTotalFraction(totalFraction);
            service.isImpactFinished(vo);
            verifyUtil.number(vo.getTotalFraction(), getMessage("form.title.tabFormImpactAnalysis"));
        }
        
        // 是否需要填 因應措施 和 影響評估
        if (isNeedSolution(solution, evaluation, impactList)) {
            verifyUtil.append(getMessage("form.impactAnalysis.evaluation.or.solution.empty"));
        }
        
        if (StringUtils.isNotBlank(fileVerifyResult)) {
            verifyUtil.append(fileVerifyResult);
        }
        
        return verifyUtil.build();
    }

    /**
     * 審核流程跳到指定關卡
     * @param vo
     * @return ChangeFormVO
     */
    @FormLock
    @PostMapping(path = "/jumpToReviewTargetLevel")
    public ChangeFormVO jumpToReviewTargetLevel (@RequestBody ChangeFormVO vo) {
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        service.jumpToReview(vo);
        service.notifyProcessMail(vo);

        return vo;
    }
    
    /**
     * 直接結案
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public ChangeFormVO closeForm(@RequestBody ChangeFormVO vo, SysUserVO userInfo) {
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        if (service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        service.immediateClose(vo);
        vo.setVerifyResult(FormEnum.CLOSED.name());
        vo.setVerifyLogs(commonFormService.getFormLogs(vo, userInfo));
        service.notifyProcessMail(vo);
        
        return vo;
    }

    private boolean isNeedSolution (String solution, String evaluation, List<FormImpactAnalysisVO> impactList) {
        String targetFraction;
        int compareFraction = 0;
        boolean isNeedSolution = false;
        String fractionStr = systemConfigService.getFraction().getParamValue();
        
        if (StringUtils.isBlank(fractionStr)) {
            fractionStr = "0";
        }
        
        int limitFraction = Integer.valueOf(fractionStr);
        
        for (FormImpactAnalysisVO vo : impactList) {
            if (isFraction(vo)) {
                targetFraction = vo.getTargetFraction();
                
                if (StringUtils.isBlank(targetFraction)) {
                    targetFraction = "0";
                }
                
                compareFraction = Integer.valueOf(targetFraction);
                
                if (StringConstant.SHORT_YES.equals(vo.getIsValidateFraction()) && compareFraction >= limitFraction) {
                    isNeedSolution = StringUtils.isBlank(solution) || StringUtils.isBlank(evaluation);
                    break;
                }
            }
        }
        
        return isNeedSolution;
    }

    private boolean isFraction (FormImpactAnalysisVO vo) {
        boolean isSolution = FormEnum.S.name().equals(vo.getQuestionType());
        boolean isEvaluation = FormEnum.E.name().equals(vo.getQuestionType());
        boolean isTotalFraction = FormEnum.T.name().equals(vo.getQuestionType());
        
        return (!isSolution && !isEvaluation && !isTotalFraction);
    }
    
    private boolean isNeedFraction (ChangeFormVO vo) {
        boolean isMainForms = service.isMainForms(vo.getSourceId());
        boolean isFormCreated = StringUtils.isNotBlank(vo.getFormId());// 未起單的情況
        boolean isNewSystem = StringConstant.SHORT_YES.equals(vo.getIsNewSystem());
        boolean isProposing = FormEnum.PROPOSING.name().equals(vo.getFormStatus());
        boolean isStandard = this.getMessage("form.change.changeType.2").equals(vo.getChangeType());
        
        return !isFormCreated || (isProposing && isMainForms && !isNewSystem && !isStandard);
    }

}
