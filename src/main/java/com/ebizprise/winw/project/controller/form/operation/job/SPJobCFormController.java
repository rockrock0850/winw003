package com.ebizprise.winw.project.controller.form.operation.job;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.ICommonJobFormService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.SpJobCFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * SP工作會辦單 新增/修改/審核/退回等作業
 * 
 * @author emily.lai
 */
@RestController
@RequestMapping("/spJobCForm")
public class SPJobCFormController extends BaseController implements IBaseForm<SpJobCFormVO> {

    @Value("${sp.workout}")
    private boolean isWorkout;
    
    @Autowired
    private IBaseFormService<SpJobCFormVO> service;
    @Autowired
    private ICommonJobFormService commonJobFormService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    /**
     * 產生會辦單
     */
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build (@RequestBody SpJobCFormVO vo) {
        vo.setFormClass(FormEnum.JOB_SP_C.name());
        
        if (isSPDivision(vo)) {
            vo.setValidateLogicError("「系統科工作單」不可新增「系統科會辦單」, 請重新選擇科別。");
            return BeanUtil.toJson(vo);
        }
        
        if (!isWorkout) {
            vo.setValidateLogicError(this.getMessage("form.common.is.work.out"));
            return BeanUtil.toJson(vo);
        }
        
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
    public SpJobCFormVO info(@RequestBody SpJobCFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public SpJobCFormVO approval(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public SpJobCFormVO reject(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public SpJobCFormVO deprecated(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public SpJobCFormVO save(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public SpJobCFormVO modifyColsByVice(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public SpJobCFormVO send(@RequestBody SpJobCFormVO vo) throws Exception {
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
    public void delete(@RequestBody SpJobCFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData(@RequestBody SpJobCFormVO vo) {
        return validateColumnData(vo, false);
    }
    
    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody SpJobCFormVO vo, boolean isJustInfo) {
        String status = vo.getFormStatus();
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getSummary(), this.getMessage("form.question.form.info.summary"))        // 摘要
                .string(vo.getPurpose(), this.getMessage("form.search.column.job.ap.purpose"))      // 作業目的
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"));       // 內容
        
        if (FormEnum.PROPOSING.name().equals(status)) {
           return verifyUtil.build(); 
        }
        
        // IsProduction和IsTest,兩者不能全部都是N或都是空值
        if (StringConstant.SHORT_NO.equalsIgnoreCase(vo.getIsProduction())
                && StringConstant.SHORT_NO.equalsIgnoreCase(vo.getIsTest())) {
            verifyUtil.append(this.getMessage("form.job.validate.system.type.not.selected"));
        }
        
        if (isJustInfo) {
            return verifyUtil.build();
        }
        
        // 請選擇並且儲存作業關卡人員
        if (!StringUtils.isBlank(vo.getFormId()) &&
                FormEnum.ASSIGNING.name().equals(status)) {
            service.getFormInfo(vo);
            verifyUtil.list(
                    commonJobFormService.getJobWorkItems(vo, true),
                    this.getMessage("form.job.checkPerson.isNull"));
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
    public SpJobCFormVO closeForm (@RequestBody SpJobCFormVO vo) {
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
     * 串會副科長
     * 
     * @param vo
     * @throws Exception 
     */
    @CtlLog
    @PostMapping(path = "/sendSplitProcess")
    public void sendSplitProcess (@RequestBody SpJobCFormVO vo) throws Exception {
    	service.sendSplitProcess(vo);
    	
		// 鎖定該表單所有檔案的狀態,改為不能刪除
		service.lockFormFileStatus(vo.getFormId());
		
		// 寄信通知下一關的審核群組
		vo.setVerifyResult(FormEnum.AGREED.name());
		service.notifyProcessMail(vo);
    }
    
    private boolean isSPDivision (SpJobCFormVO vo) {
        return (vo.getDivisionCreated().indexOf("SP") != -1);
    }

}
