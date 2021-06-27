package com.ebizprise.winw.project.controller.form.operation.job;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.ApJobCFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * AP工作會辦單 新增/修改/審核/退回等作業
 * 
 * @author emily.lai
 */
@RestController
@RequestMapping("/apJobCForm")
public class APJobCFormController extends BaseController implements IBaseForm<ApJobCFormVO> {
    
    @Autowired
    private IBaseFormService<ApJobCFormVO> service;
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    /**
     * 產生會辦單
     */
    @CtlLog
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build(@RequestBody ApJobCFormVO vo) {
        vo.setFormClass(FormEnum.JOB_AP_C.name());
        vo.setFormType(FormEnum.JOB_AP_C.formType());
        vo.setFormWording(getMessage(FormEnum.JOB_AP_C.wording()));
        
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
    public ApJobCFormVO info(@RequestBody ApJobCFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public ApJobCFormVO approval(@RequestBody ApJobCFormVO vo) throws Exception {
        
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        //等待全部延伸單結案
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
    public ApJobCFormVO reject(@RequestBody ApJobCFormVO vo) throws Exception {
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
    public ApJobCFormVO deprecated(@RequestBody ApJobCFormVO vo) throws Exception {
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
    public ApJobCFormVO save(@RequestBody ApJobCFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        
        if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
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
    public ApJobCFormVO modifyColsByVice(@RequestBody ApJobCFormVO vo) throws Exception {
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
    public ApJobCFormVO send(@RequestBody ApJobCFormVO vo) throws Exception {
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
    public void delete(@RequestBody ApJobCFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData(@RequestBody ApJobCFormVO vo) {
        return validateColumnData(vo, false);
    }

    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody ApJobCFormVO vo, boolean isJustInfo) {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"))  // 服務類別
                .string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"))   // 系統名稱
                .string(vo.getPurpose(), this.getMessage("form.search.column.job.ap.purpose"))    // 作業目的
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"));     // 內容
        
        return verifyUtil.build();
    }
    
    /**
     * 直接結案
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public ApJobCFormVO closeForm (@RequestBody ApJobCFormVO vo) {
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
     * 內會給其他同仁
     * 
     * @param vo
     * @throws Exception 
     */
    @CtlLog
    @PostMapping(path = "/sendInternalProcess")
    public void sendInternalProcess (@RequestBody ApJobCFormVO vo) throws Exception {
		service.doInternalProcess(vo);
		
		// 鎖定該表單所有檔案的狀態,改為不能刪除
		service.lockFormFileStatus(vo.getFormId());
		
		// 寄信通知下一關的審核群組
		vo.setVerifyResult(FormEnum.AGREED.name());
		service.notifyProcessMail(vo);
    }
    
    /**
     * 結束當前內會流程
     * 
     * @param vo
     * @throws Exception 
     */
    @CtlLog
    @PostMapping(path = "/finishedInternalProcess")
    public void finishedInternalProcess (@RequestBody ApJobCFormVO vo) throws Exception {
    	service.finishedInternalProcess(vo);
    }
    
    /**
     * 串會副科長
     * 
     * @param vo
     * @throws Exception 
     */
    @CtlLog
    @PostMapping(path = "/sendSplitProcess")
    public void sendSplitProcess (@RequestBody ApJobCFormVO vo) throws Exception {
    	service.sendSplitProcess(vo);
    	
		// 鎖定該表單所有檔案的狀態,改為不能刪除
		service.lockFormFileStatus(vo.getFormId());
		
		// 寄信通知下一關的審核群組
		vo.setVerifyResult(FormEnum.AGREED.name());
		service.notifyProcessMail(vo);
    }
}
