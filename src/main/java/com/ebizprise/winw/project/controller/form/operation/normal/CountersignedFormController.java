package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.impl.CountersignedFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.CountersignedFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 會辦單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/countersignedForm")
public class CountersignedFormController extends BaseController implements IBaseForm<CountersignedFormVO> {

    @Autowired
    private CountersignedFormServiceImpl service;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private FormHelper formHelper;
    
    private IBaseCountersignedFormService<CountersignedFormVO> cMailService;
    
    /**
     * 直接結案
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public CountersignedFormVO closeForm (@RequestBody CountersignedFormVO vo) {
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
        
        // 寄信通知下一關的審核群組
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        return vo;
    }
    
    /**
     * 產生會辦單
     */
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build (@RequestBody CountersignedFormVO vo) {
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass() + "_C");
        vo.setFormClass(formClass.name());
        vo.setFormType(formClass.formType());
        vo.setFormWording(getMessage(formClass.wording()));
        
        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return BeanUtil.toJson(vo);
        }
        
        if (service.isFormClosed(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return BeanUtil.toJson(vo);
        }
        
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
    public CountersignedFormVO info (@RequestBody CountersignedFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public CountersignedFormVO approval (@RequestBody CountersignedFormVO vo) throws Exception {
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
        service.lockFormFileStatus(vo.getFormId());
        
        // 寄信通知下一關的審核群組
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/reject")
    public CountersignedFormVO reject (@RequestBody CountersignedFormVO vo) throws Exception {
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
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/deprecated")
    public CountersignedFormVO deprecated (@RequestBody CountersignedFormVO vo) throws Exception {
        if (!service.isAllStretchDied(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.4"));
            return vo;
        }
        
        vo.setFormStatus(FormEnum.DEPRECATED.name());
        vo.setProcessStatus(FormEnum.DEPRECATED.name());
        vo.setVerifyResult(FormEnum.DEPRECATED.name());
        service.verifying(vo);
        
        // 寄信通知經辦人員
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @ModifyRecord
    @PostMapping(path = "/save")
    public CountersignedFormVO save (@RequestBody CountersignedFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin(); 
        boolean isEctExtended = vo.getIsEctExtended();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
		
		if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
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
    public CountersignedFormVO modifyColsByVice(@RequestBody CountersignedFormVO vo) throws Exception {
        boolean isEctExtended = vo.getIsEctExtended();
        
        service.mergeFormInfo(vo);
        service.updateVerifyLog(vo);
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // 新增一筆簽核紀錄(簽核意見=修改原因)
        service.createVerifyCommentByVice(vo);
        // 鎖定該表單所有檔案的狀態，改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知下一關的審核群組
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        if (isEctExtended) {
            service.ectExtended(vo);
        }
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/send")
    public CountersignedFormVO send (@RequestBody CountersignedFormVO vo) throws Exception {
        service.sendToVerification(vo);
        
        // 鎖定該表單所有檔案的狀態,改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        
        // 寄信通知下一關的審核群組
        vo.setVerifyResult(FormEnum.SENT.name());
        cMailService = formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
        cMailService.sendMail(vo);
        
        return vo;
    }
    
    @Override
    @PostMapping(path = "/delete")
    public void delete (@RequestBody CountersignedFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public void program (@RequestBody CountersignedFormVO vo) {
        service.saveProgram(vo);
    }
    
    /**
     * 取得處理方案頁簽的資訊
     * 
     * @param formId
     * @return
     */
    @GetMapping(path = "/program/{formId}")
    public CountersignedFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody CountersignedFormVO vo) {
        return validateColumnData(vo, false);
    }
    
    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody CountersignedFormVO vo, boolean isJustInfo) {
        Date ast = vo.getAst();
        Date act = vo.getAct();
        boolean isSuperVice = vo.getIsSuperVice();
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        boolean isApplyLastLevel = valueOfBoolean(vo.getIsApplyLastLevel());
        boolean isReview = FormEnum.REVIEW.name().equals(vo.getVerifyType());
        boolean isINCCForm = FormEnum.INC_C.name().equals(vo.getFormClass());
        
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
            
            if (isINCCForm && ast != null && ast.after(act)) {
                verifyUtil.append(getMessage("form.common.ast.must.less.than.act"));
            }
            
            return verifyUtil.build();
        }
        
        verifyUtil.string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"))  // 服務類別
                .string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"))     // 系統名稱
                .string(vo.getSummary(), this.getMessage("form.question.form.info.summary"))        // 摘要
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"));       // 內容
        
        if (isJustInfo) {
            return verifyUtil.build();
        }
        
        if ((isApplyLastLevel || (isReview && isVice)) && service.isAlertJobSCTWarning(vo)) {
            verifyUtil.append(getMessage("form.common.approval.warning.11"));
        }

        // 申請最後一關[實際完成時間]必填
        if (valueOfBoolean(vo.getIsApplyLastLevel())) {
            verifyUtil.date(vo.getAct(), this.getMessage("form.question.form.info.act"));
        }
        
        // 事件會辦單:「實際開始時間」須小於「實際完成時間」
        // 檢核時機:(1)申請流程最後一關，經辦送出 (2)副科自身關卡
        if (isINCCForm && (isApplyLastLevel || isVice)) {
            if (ast != null && ast.after(act)) {
                verifyUtil.append(getMessage("form.common.ast.must.less.than.act"));
            }
        }
        
        return verifyUtil.build();
    }
}