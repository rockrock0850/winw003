package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.impl.RequirementFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.RequirementFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 需求單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/requirementForm")
public class RequirementFormController extends BaseController implements IBaseForm<RequirementFormVO> {
    
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private RequirementFormServiceImpl service;

    @CtlLog
    @Override
    @PostMapping(path = "/info")
    public RequirementFormVO info (@RequestBody RequirementFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }
    
    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public RequirementFormVO approval (@RequestBody RequirementFormVO vo) throws Exception {
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
    public RequirementFormVO reject (@RequestBody RequirementFormVO vo) throws Exception {
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
        // 鎖定該表單所有檔案的狀態,改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/deprecated")
    public RequirementFormVO deprecated (@RequestBody RequirementFormVO vo) throws Exception {
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
    public RequirementFormVO save (@RequestBody RequirementFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin();
        boolean isEctExtended = vo.getIsEctExtended();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        
        if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return vo;
        }

        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return vo;
        }
        
        if (isEctExtended) {
            service.ectExtended(vo);
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
    public RequirementFormVO modifyColsByVice(@RequestBody RequirementFormVO vo) throws Exception {
        boolean isEctExtended = vo.getIsEctExtended();

        service.mergeFormInfo(vo);
        service.updateVerifyLog(vo);
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // 新增一筆簽核紀錄(簽核意見=修改原因)
        service.createVerifyCommentByVice(vo);
        // 鎖定該表單所有檔案的狀態，改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        
        if (isEctExtended) {
            service.ectExtended(vo);
        }
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/send")
    public RequirementFormVO send (@RequestBody RequirementFormVO vo) throws Exception {
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
    public void delete (@RequestBody RequirementFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public RequirementFormVO program (@RequestBody RequirementFormVO vo) {
        service.saveProgram(vo);
        return vo;
    }
    
    /**
     * 取得處理方案頁簽的資訊
     * 
     * @param formId
     * @return
     */
    @GetMapping(path = "/program/{formId}")
    public RequirementFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody RequirementFormVO vo) {
        return validateColumnData(vo, false);
    }

    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody RequirementFormVO vo, boolean isJustInfo) {
        boolean isSuperVice = vo.getIsSuperVice();
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        boolean isApplyLastLevel = valueOfBoolean(vo.getIsApplyLastLevel());
        boolean isReview = FormEnum.REVIEW.name().equals(vo.getVerifyType());
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        if (isApplyLastLevel && service.isAlertCCTWarning(vo)) {
            DataVerifyUtil verifyUtil = new DataVerifyUtil();
            verifyUtil.append(getMessage("form.common.cct.must.less.than.source.form.ect"));
            return verifyUtil.build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil();
        
        if (isSuperVice) {
            if (isReview && service.isAlertJobSCTWarning(vo)) {
                verifyUtil.append(getMessage("form.common.approval.warning.11"));
            }
            
            if (service.isAlertECTWarning(vo)) {
                verifyUtil.append(getMessage("form.requirement.compare.date.error"));
            }
            
            return verifyUtil.build();
        }
         
        verifyUtil.string(vo.getUnitCategory(), this.getMessage("form.question.question.source.department.type"))  // 提出單位分類
                .string(vo.getUnitCategory(), this.getMessage("form.question.question.source.department.type"))  // 提出單位分類
                .string(vo.getUnitId(), this.getMessage("form.question.question.source.user.department"))        // 提出人員單位
                .string(vo.getUserName(), this.getMessage("form.question.question.source.user.name"))            // 提出人員姓名
                .string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"))                 // 服務類別
                .string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"))                  // 系統名稱
                .string(vo.getEffectScope(), this.getMessage("form.question.form.info.effect.scope"))            // 影響範圍
                .string(vo.getUrgentLevel(), this.getMessage("form.question.form.info.urgent.level"))            // 緊急程度
                .string(vo.getSummary(), this.getMessage("form.question.form.info.summary"))                     // 摘要
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"))                     // 內容
                .string(vo.getcComponent(), this.getMessage("form.question.form.info.question.c.component"))     // 組態元件
                .string(vo.getProcessProgram(), this.getMessage("form.question.program.reprocess.programason"))  // 處理方案
                .date(vo.getEct(), this.getMessage("form.question.form.info.ect"))                               // 預計完成時間
                .date(vo.getEot(), this.getMessage("form.search.column.sr.eot"));                                // 與業務單位確認預計上線時間
        
        if (isJustInfo) {
            return verifyUtil.build();
        }
        
        if (service.isAlertECTWarning(vo)) {
            verifyUtil.append(getMessage("form.requirement.compare.date.error"));
            return verifyUtil.build();
        }
        
        if ((isApplyLastLevel || (isReview && isVice)) && service.isAlertJobSCTWarning(vo)) {
            verifyUtil.append(getMessage("form.common.approval.warning.11"));
        }

        // 郵件地址
        if (StringUtils.isNotBlank(vo.getEmail())) {
            verifyUtil.email(vo.getEmail(), this.getMessage("form.question.form.info.email"));
        }
        
        // 申請最後一關[實際完成時間]必填
        if (valueOfBoolean(vo.getIsApplyLastLevel())) {
            verifyUtil.date(vo.getAct(), this.getMessage("form.question.form.info.act"));
        }

        // 0007089: 【008】ISO_7_變更單擴充附件功能
        // 需求單或問題單在APPLY_4經辦->REVIEW_1副科時，且有衍伸單(變更單=已結案、有新系統=Y)，需求單或問題單需檢核附件
        if (isApplyLastLevel) {
            String fileVerifyResult = service.checkAttachmentExists(vo);

            if (StringUtils.isNotBlank(fileVerifyResult)) {
                verifyUtil.append(fileVerifyResult);
            }
        }
    
        return verifyUtil.build();
    }
    
    /**
     * 直接結案
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public RequirementFormVO closeForm (@RequestBody RequirementFormVO vo) {
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
        service.notifyProcessMail(vo);
        
        return vo;
    }
    
    /**
     * 是否有延伸單
     * @param vo
     */
    @PostMapping(path = "/hasStretchs")
    public boolean hasStretchs (@RequestBody RequirementFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

}
