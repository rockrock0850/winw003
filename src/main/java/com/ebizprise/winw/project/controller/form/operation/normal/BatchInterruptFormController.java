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
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.BatchInterruptFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 批次作業中斷對策表管理 新增/修改/審核/退回等作業
 * 
 * @author Bernard.Yu
 */
@RestController
@RequestMapping("/batchInterruptForm")
public class BatchInterruptFormController extends BaseController implements IBaseForm<BatchInterruptFormVO> {
    
    @Autowired
    private IBaseFormService<BatchInterruptFormVO> service;
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    @CtlLog
    @PostMapping(path = "/info")
    public BatchInterruptFormVO info (@RequestBody BatchInterruptFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public BatchInterruptFormVO approval (@RequestBody BatchInterruptFormVO vo) throws Exception {
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
    public BatchInterruptFormVO reject (@RequestBody BatchInterruptFormVO vo) throws Exception {
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
    public BatchInterruptFormVO deprecated (@RequestBody BatchInterruptFormVO vo) throws Exception {
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
    public BatchInterruptFormVO save (@RequestBody BatchInterruptFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (service.isFormClosed(vo) && !isAdmin) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return vo;
        }

        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return vo;
        }

        service.mergeFormInfo(vo);
        
        return vo;
    }
    
    @Override
    @FormLock
    @PostMapping(path = "/modifyColsByVice")
    public BatchInterruptFormVO modifyColsByVice(@RequestBody BatchInterruptFormVO vo) throws Exception {
        service.mergeFormInfo(vo);
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
    public BatchInterruptFormVO send (@RequestBody BatchInterruptFormVO vo) throws Exception {
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
    public void delete (@RequestBody BatchInterruptFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public BatchInterruptFormVO program (@RequestBody BatchInterruptFormVO vo) {
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
    public BatchInterruptFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody BatchInterruptFormVO vo) {
        return validateColumnData(vo, false);
    }

    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody BatchInterruptFormVO vo, boolean isJustInfo) {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getBatchName(), this.getMessage("form.question.form.info.batch.name"))   // 批次工作名稱
                .string(vo.getSummary(), this.getMessage("form.question.form.info.job.desc"))       // 作業名稱描述
                .date(vo.getEffectDate(), this.getMessage("form.question.form.info.effect.date"));  // 生效日期

        // 郵件地址
        if (StringUtils.isNotBlank(vo.getEmail())) {
            verifyUtil.email(vo.getEmail(), this.getMessage("form.question.form.info.email"));
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
    public BatchInterruptFormVO closeForm (@RequestBody BatchInterruptFormVO vo) {
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
    public boolean hasStretchs (@RequestBody BatchInterruptFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

}
