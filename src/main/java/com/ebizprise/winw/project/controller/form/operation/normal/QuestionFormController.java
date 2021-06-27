package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.Date;
import java.util.HashMap;
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

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.service.impl.QuestionFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.EventFormVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.QuestionFormVO;
import com.ebizprise.winw.project.vo.SysOptionRoleVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 問題單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh,AndrewLee
 */
@RestController
@RequestMapping("/questionForm")
public class QuestionFormController extends BaseController implements IBaseForm<QuestionFormVO>{

    @Autowired
    private QuestionFormServiceImpl service;
    @Autowired
    private IHtmlService htmlservice;
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    @GetMapping(path = "/init/{sourceId}")
    public ModelAndView initPage (QuestionFormVO vo) {
        service.create(vo);
        return new ModelAndView(DispatcherEnum.FORM_SEARCH.dispatch(), "info", vo);
    }
    
    @Override
    @CtlLog
    @PostMapping(path = "/info")
    public QuestionFormVO info(@RequestBody QuestionFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }
    
    /**
     * 轉開並儲存問題單
     * @param vo
     * @return
     * @throws Exception
     * @author jacky.fu
     */
    @PostMapping(path = "/openAndSave")
    public QuestionFormVO openAndSave(@RequestBody QuestionFormVO vo) throws Exception {
        service.create(vo,true);
        QuestionFormVO quesVo = save(vo);
        service.sendMailToDivisionSolving(quesVo);
        return quesVo;
    }
    
    /**
     * 轉開並儲存問題單(副科)
     * @param vo
     * @return
     * @throws Exception
     * @author jacky.fu
     */
    @PostMapping(path = "/openAndSaveVice")
    public QuestionFormVO openAndSaveVice(@RequestBody QuestionFormVO vo) throws Exception {
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        boolean isAprroving = StringUtils.isNotBlank(vo.getFormStatus()) ?
                FormEnum.APPROVING == FormEnum.valueOf(vo.getFormStatus()) : false;
        if(isVice && isAprroving) {
            return openAndSave(vo);
        }else {
            return vo;
        }
    }
    
    /**
     * 轉開並儲存問題單(科長)
     * @param vo
     * @return
     * @throws Exception
     * @author jacky.fu
     */
    @PostMapping(path = "/openAndSaveChief")
    public QuestionFormVO openAndSaveChief(@RequestBody QuestionFormVO vo) throws Exception {
        boolean isChief = sysUserService.isChief(getUserInfo().getGroupId());
        boolean isAprroving = StringUtils.isNotBlank(vo.getFormStatus()) ?
                FormEnum.APPROVING == FormEnum.valueOf(vo.getFormStatus()) : false;
        if(isChief && isAprroving) {
            return openAndSave(vo);
        }else {
            return vo;
        }
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public QuestionFormVO approval(@RequestBody QuestionFormVO vo) {
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
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/reject")
    public QuestionFormVO reject(@RequestBody QuestionFormVO vo) {
        SysUserVO loginUser = getUserInfo();
        boolean isVice = sysUserService.isVice(loginUser.getGroupId());
        boolean isBackToPic = FormEnum.APPROVING.name().equals(vo.getFormStatus());
        
        if (!service.isVerifyAcceptable(vo, loginUser)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }

        boolean isPic = sysUserService.isPic(loginUser.getGroupId());
        
        if (isPic && service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        if (isVice && isBackToPic && FormEnum.REVIEW.name().equals(vo.getVerifyType())) {
            vo.setObservation(null);
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
    public QuestionFormVO deprecated(@RequestBody QuestionFormVO vo) {
        if (!service.isAllStretchDied(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.4"));
            return vo;
        }
        
        vo.setFormStatus(FormEnum.DEPRECATED.name());
        vo.setProcessStatus(FormEnum.DEPRECATED.name());
        vo.setVerifyResult(FormEnum.DEPRECATED.name());
        service.verifying(vo);
        
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @ModifyRecord
    @PostMapping(path = "/save")
    public QuestionFormVO save(@RequestBody QuestionFormVO vo) throws Exception {
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
    public QuestionFormVO modifyColsByVice(@RequestBody QuestionFormVO vo) {
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
    public QuestionFormVO send(@RequestBody QuestionFormVO vo) {
        vo.setReportTime(new Date());
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
    public void delete(@RequestBody QuestionFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public QuestionFormVO program (@RequestBody QuestionFormVO vo) {
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
    public QuestionFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody QuestionFormVO vo) throws Exception {
            return validateColumnData(vo, false);
    }
    
    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody QuestionFormVO vo, boolean isJustInfo) throws Exception {
        Date today = new Date();
        boolean isSave = vo.getIsSave();
        boolean isSuperVice = vo.getIsSuperVice();
        boolean isAdmin = sysUserService.isAdmin();
        boolean isPic = sysUserService.isPic(getUserInfo().getGroupId());
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        boolean isApply = FormEnum.APPLY.name().equals(vo.getVerifyType());
        boolean isApplyLastLevel = valueOfBoolean(vo.getIsApplyLastLevel());
        boolean isReview = FormEnum.REVIEW.name().equals(vo.getVerifyType());
        boolean isApplyLevelOne = isApply && "1".equals(vo.getVerifyLevel());
                
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
            
            return verifyUtil.build();
        }
        
        if (!isSave) {
            verifyUtil.string(vo.getQuestionId(), this.getMessage("form.question.question.source"))                   // 問題來源
                      .string(vo.getUnitCategory(), this.getMessage("form.question.question.source.department.type")) // 提出單位分類
                      .string(vo.getUnitId(), this.getMessage("form.question.question.source.user.department"))       // 提出人員單位
                      .string(vo.getUserName(), this.getMessage("form.question.question.source.user.name"))           // 提出人員姓名
                      .string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"))                // 服務類別
                      .string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"))                 // 系統名稱
                      .string(vo.getcCategory(), this.getMessage("form.question.form.info.question.c.category"))      // 組態分類
                      .string(vo.getcClass(), this.getMessage("form.question.form.info.question.c.class"))            // 組態元件類別
                      .string(vo.getcComponent(), this.getMessage("form.question.form.info.question.c.component"))    // 組態元件
                      .string(vo.getEffectScope(), this.getMessage("form.question.form.info.effect.scope"))           // 影響範圍
                      .string(vo.getUrgentLevel(), this.getMessage("form.question.form.info.urgent.level"))           // 緊急程度
                      .string(vo.getSummary(), this.getMessage("form.question.form.info.question.summary"))           // 問題摘要
                      .string(vo.getContent(), this.getMessage("form.question.form.info.question.content"))           // 問題內容
                      .string(vo.getIndication(), this.getMessage("form.question.program.indication"))                // 征兆
                      .string(vo.getReason(), this.getMessage("form.question.program.reason"))                        // 原因
                      .string(vo.getProcessProgram(), this.getMessage("form.question.program.reprocess.programason")) // 處理方案
                      .date(vo.getEct(), this.getMessage("form.question.form.info.ect"));                             // 預計完成時間
        
            if (isJustInfo) {
                return verifyUtil.build();
            }
        
            if ((isApplyLastLevel || (isReview && isVice)) && service.isAlertJobSCTWarning(vo)) {
                verifyUtil.append(getMessage("form.common.approval.warning.11"));
            }
            
            if (isApply && isPic && !isApplyLevelOne) {
                verifyUtil.date(vo.getAct(), getMessage("form.search.column.q.act"));
                
                if (vo.getAct() != null) {
                    Date act = vo.getAct();
                    Date ect = vo.getEct();
                    
                    if (act.compareTo(today) > 0) {
                        verifyUtil.append(getMessage("form.search.column.q.1"));
                    } else if (act.compareTo(ect) > 0) {
                        verifyUtil.append(getMessage("form.search.column.q.2"));
                    }
                }
            }
            
            // 如果來源是事件單並且問題來源與事件單有關,則檢查影響範圍是否有超過主單的數值
            if (StringUtils.isNotBlank(vo.getSourceId()) && vo.getSourceId().contains("-") && StringUtils.isNotBlank(vo.getEffectScope())) {
                String sourceFormType = vo.getSourceId().split("-")[0];
                if (FormEnum.INC.name().startsWith(sourceFormType)) {
                    HtmlVO htmlVO = new HtmlVO();
                    htmlVO.setValue(vo.getQuestionId());
                    htmlVO.setWording(vo.getQuestionIdWording());
                    SysOptionRoleVO roleVo = htmlservice.getSysOptionRole(htmlVO);
                
                    //如果為Y的話,代表該選項是事件單對應的問題來源,需檢查影響範圍是否有超過主單的數值
                    if (StringConstant.SHORT_YES.equalsIgnoreCase(roleVo.getCondition())) {
                        EventFormVO eventFormVO = service.getSourceEventForm(vo.getSourceId());
                        Integer sourceEffect = Integer.parseInt(eventFormVO.getEffectScope());
                        Integer effectScope = Integer.parseInt(vo.getEffectScope());
                        
                        if(effectScope > sourceEffect) {
                            String msg = getMessage("form.question.sourceEffec.warning") + "\r\n";
                            verifyUtil.append(msg);
                        }
                    }
                }
            }
            
            // 是否加入知識庫 最終版改由第二階段上線 adam.test TODO
//            if (StringConstant.SHORT_YES.equals(vo.getIsSuggestCase())) {
//                verifyUtil
//                    .string(vo.getKnowledge1(), getMessage("form.question.form.info.knowledge1"))
//                    .string(vo.getKnowledge2(), getMessage("form.question.form.info.knowledge2"));
//            }
            
            // 特殊結案狀態
            if (StringConstant.SHORT_YES.equals(vo.getIsSpecial())) {
                verifyUtil.string(
                        vo.getSpecialEndCaseType(), this.getMessage("form.question.form.info.special.end.case.type"));
            }
            
            // 郵件地址
            if(StringUtils.isNotBlank(vo.getEmail())) {
                verifyUtil.email(vo.getEmail(), this.getMessage("form.question.form.info.email"));
            }
            
            // 電話
            if(StringUtils.isNotBlank(vo.getPhone())) {
                verifyUtil
                    .phone(vo.getPhone(), this.getMessage("form.question.form.info.phone"))
                    .length(vo.getPhone(), 30, this.getMessage("form.question.form.info.phone"));
            }
        
            // 0007089: 【008】ISO_7_變更單擴充附件功能
            // 需求單或問題單在APPLY_4經辦->REVIEW_1副科時，且有衍伸單(變更單=已結案、有新系統=Y)，需求單或問題單需檢核附件
            if (isApplyLastLevel) {
                String fileVerifyResult = service.checkAttachmentExists(vo);

                if (StringUtils.isNotBlank(fileVerifyResult)) {
                    verifyUtil.append(fileVerifyResult);
                }
            }
        }
        
        // 檢核問題單觀察期
        Map<String, Object> validateMap = new HashMap<String, Object>();
        validateMap.put("isSave", isSave);
        validateMap.put("isVerifyAcceptable", vo.getIsVerifyAcceptable());
        validateMap.put("isReview", FormEnum.REVIEW.name().equals(vo.getVerifyType()));
        validateMap.put("isSpecial", StringConstant.SHORT_YES.equals(vo.getIsSpecial()));
        validateMap.put("isDeprecated", FormEnum.DEPRECATED.name().equals(vo.getProcessStatus()));
        service.validateObservationLogic(vo, validateMap, verifyUtil);
        
        return verifyUtil.build();
    }
    
    /*
     * 問題單，表頭「儲存」按鈕
     */
    @CtlLog
    @PostMapping(path = "/validateColumnData/forSaveAction")
    public Map<String, Object> validateColumnDataForSave(@RequestBody QuestionFormVO vo) throws Exception {
            return validateColumnData(vo, false);
    }
    
    /*
     * 問題單，表頭「簽核」按鈕，開啟dialog時，按下確定需檢核的條件
     */
    @CtlLog
    @PostMapping(path = "/validateColumnData/forSigningAction")
    public Map<String, Object> validateColumnDataForSigning(@RequestBody QuestionFormVO vo, boolean isSave) throws Exception {
        Date today = new Date();
        Date observation = vo.getObservation();
        DataVerifyUtil verifyUtil = new DataVerifyUtil();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        boolean isSpecial = StringConstant.SHORT_YES.equals(vo.getIsSpecial());
        boolean isApproving = FormEnum.APPROVING.name().equals(vo.getProcessStatus());
        
        if (isVerifyAcceptable && observation != null) {
            int total = (int) DateUtils.getSmartDiff(today, observation, DateUtils.Type.Day);
            
            if (isSave && isSpecial && total < 30) {
                verifyUtil.append(getMessage("form.question.form.info.observation.validate.1"));
            } else if (isSave && total < 6) {
                verifyUtil.append(getMessage("form.question.form.info.observation.validate.2"));
            }
            
            total = (int) DateUtils.getSmartDiff(today, observation, DateUtils.Type.Seconds);
            
            if (isVice && isApproving && total >= 0) {
                verifyUtil.append(getMessage("form.question.form.info.observation.validate.3"));
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
    public QuestionFormVO closeForm(@RequestBody QuestionFormVO vo) {
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
    public boolean hasStretchs (@RequestBody QuestionFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

}
