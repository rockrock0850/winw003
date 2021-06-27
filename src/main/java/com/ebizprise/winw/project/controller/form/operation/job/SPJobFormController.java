package com.ebizprise.winw.project.controller.form.operation.job;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.ebizprise.winw.project.vo.SpJobFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * SP工作單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/spJobForm")
public class SPJobFormController extends BaseController implements IBaseForm<SpJobFormVO> {
    
    @Value("${sp.workout}")
    private boolean isWorkout;
    
    @Autowired
    private IBaseFormService<SpJobFormVO> service;
    @Autowired
    private ICommonJobFormService commonJobFormService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    /**
     * 產生工作單
     */
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build (@RequestBody SpJobFormVO vo) {
        vo.setFormClass(FormEnum.JOB_SP.name());
        
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

    @CtlLog
    @Override
    @PostMapping(path = "/info")
    public SpJobFormVO info (@RequestBody SpJobFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @CtlLog
    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public SpJobFormVO approval (@RequestBody SpJobFormVO vo) throws Exception {
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

    @CtlLog
    @Override
    @FormLock
    @PostMapping(path = "/reject")
    public SpJobFormVO reject (@RequestBody SpJobFormVO vo) throws Exception {
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
    public SpJobFormVO deprecated (@RequestBody SpJobFormVO vo) throws Exception {
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
    public SpJobFormVO save (@RequestBody SpJobFormVO vo) throws Exception {
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
    public SpJobFormVO modifyColsByVice(@RequestBody SpJobFormVO vo) throws Exception {
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
    public SpJobFormVO send (@RequestBody SpJobFormVO vo) throws Exception {
        vo.setCreateTime(new Date());
        vo.setFormStatus(FormEnum.APPROVING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());
        service.mergeFormInfo(vo);// 更新表單
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
    public void delete (@RequestBody SpJobFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public void program (@RequestBody SpJobFormVO vo) {
        service.saveProgram(vo);
    }
    
    /**
     * 取得處理方案頁簽的資訊
     * 
     * @param formId
     * @return
     */
    @GetMapping(path = "/program/{formId}")
    public SpJobFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody SpJobFormVO vo) {
        return validateColumnData(vo, false);
    }

    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody SpJobFormVO vo, boolean isJustInfo) {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getcCategory(), this.getMessage("form.change.form.info.change.c.category"))              // 組態分類
                .string(vo.getcClass(), this.getMessage("form.change.form.info.change.c.class"))                    // 組態元件類別
                .string(vo.getcComponent(), this.getMessage("form.change.form.info.change.c.class"))                // 組態元件
                .string(vo.getWorking(), this.getMessage("form.job.form.working"))                                  // 工作組別
                .string(vo.getSummary(), this.getMessage("form.question.form.info.summary"))                        // 摘要
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"))                        // 內容
                .string(vo.getEffectScope(), this.getMessage("form.search.column.effectScope"))                     // 影響範圍
                .date(vo.getEot(), this.getMessage("form.job.form.eot"))                                            // 預計開始時間
                .date(vo.getMect(), this.getMessage("form.job.form.ect"));                                          // 預計完成時間
        
        // IsProduction和IsTest,兩者不能全部都是N或都是空值
        if (StringConstant.SHORT_NO.equalsIgnoreCase(vo.getIsProduction())
                && StringConstant.SHORT_NO.equalsIgnoreCase(vo.getIsTest())) {
            verifyUtil.append(this.getMessage("form.job.validate.system.type.not.selected"));
        }
        
        if (isJustInfo) {
            return verifyUtil.build();
        }
        
        // 請選擇並且儲存作業關卡人員
        if (commonJobFormService.hasWorkLevel(vo.getDetailId(), vo.getVerifyType())) {
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
    public SpJobFormVO closeForm (@RequestBody SpJobFormVO vo) {
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
    public boolean hasStretchs (@RequestBody SpJobFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }

}
