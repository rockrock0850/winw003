package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.Calendar;
import java.util.List;
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
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.service.IFormSearchService;
import com.ebizprise.winw.project.service.impl.EventFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.EventFormVO;
import com.ebizprise.winw.project.vo.FormSearchVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 事件單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/eventForm")
public class EventFormController extends BaseController implements IBaseForm<EventFormVO> {
    
    @Autowired
    private IFormSearchService formSearchService;
    @Autowired
    private EventFormServiceImpl service;
    @Autowired
    private IFormProcessBaseService processService;
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    @CtlLog
    @PostMapping(path = "/info")
    public EventFormVO info (@RequestBody EventFormVO vo) {
        service.getFormInfo(vo);
        isShowBackToPic(vo);

        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public EventFormVO approval (@RequestBody EventFormVO vo) throws Exception {
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
    public EventFormVO reject (@RequestBody EventFormVO vo) throws Exception {
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
    public EventFormVO deprecated (@RequestBody EventFormVO vo) throws Exception {
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
    public EventFormVO save (@RequestBody EventFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
        
        if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return vo;
        }
        
        String userId, groupSolving, divisionSolving;
        
        if (vo.getIsSelfSolve()) {// 自行處理
            userId = getUserInfo().getUserId();
            groupSolving = getUserInfo().getGroupId();
            divisionSolving = getUserInfo().getDepartmentId() + "-" + getUserInfo().getDivision();
        } else {
            userId = vo.getUserSolving();
            groupSolving = vo.getGroupSolving();
            divisionSolving = vo.getDivisionSolving();
        }
        
        vo.setUserSolving(userId);
        vo.setGroupSolving(groupSolving);
        vo.setDivisionSolving(divisionSolving);

        if (isAdmin) {
        	service.updateVerifyLog(vo);
        }
        
        service.mergeFormInfo(vo);
        service.ectExtended(vo);
        
        return vo;
    }
    
    @Override
    @FormLock
    @PostMapping(path = "/modifyColsByVice")
    public EventFormVO modifyColsByVice(@RequestBody EventFormVO vo) throws Exception {
        service.mergeFormInfo(vo);
        service.updateVerifyLog(vo);
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // 新增一筆簽核紀錄(簽核意見=修改原因)
        service.createVerifyCommentByVice(vo);
        // 鎖定該表單所有檔案的狀態，改為不能刪除
        service.lockFormFileStatus(vo.getFormId());
        // 寄信通知上一關的審核者或處理人員
        service.notifyProcessMail(vo);
        service.ectExtended(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/send")
    public EventFormVO send (@RequestBody EventFormVO vo) throws Exception {
        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return vo;
        }
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
    public void delete (@RequestBody EventFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }
    
    /**
     * 儲存處理方案頁簽的資訊
     * 
     * @param vo
     */
    @PostMapping(path = "/program")
    public EventFormVO program (@RequestBody EventFormVO vo) {
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
    public EventFormVO program (@PathVariable String formId) {
        return service.getProgram(formId);
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody EventFormVO vo) {
        return validateColumnData(vo, false);
    }
    
    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody EventFormVO vo, boolean isJustInfo) {
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
            verifyOnlineFail(verifyUtil, vo);
            
            if (isReview && service.isAlertJobSCTWarning(vo)) {
                verifyUtil.append(getMessage("form.common.approval.warning.11"));
            }
            
            return verifyUtil.build();
        }
        
        // 驗證表單資訊頁簽
        verifyUtil.string(vo.getUnitCategory(), this.getMessage("form.question.question.source.department.type"));  // 提出單位分類
        verifyUtil.string(vo.getUnitId(), this.getMessage("form.question.question.source.user.department"));        // 提出人員單位
        verifyUtil.string(vo.getUserName(), this.getMessage("form.question.question.source.user.name"));            // 提出人員姓名
        verifyUtil.string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"));                 // 服務類別
        verifyUtil.string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"));                  // 系統名稱
        verifyUtil.string(vo.getEffectScope(), this.getMessage("form.question.form.info.effect.scope"));            // 影響範圍
        verifyUtil.string(vo.getUrgentLevel(), this.getMessage("form.question.form.info.urgent.level"));            // 緊急程度
        verifyUtil.string(vo.getSummary(), this.getMessage("form.question.form.info.summary"));                     // 摘要
        verifyUtil.string(vo.getContent(), this.getMessage("form.question.form.info.content"));                     // 內容
        verifyUtil.string(vo.getEventClass(), this.getMessage("form.event.event.class"));                           // 事件主類別
        verifyUtil.string(vo.getIndication(), this.getMessage("form.question.program.indication"));                 // 征兆
        verifyUtil.string(vo.getReason(), this.getMessage("form.question.program.reason"));                         // 原因
        verifyUtil.string(vo.getProcessProgram(), this.getMessage("form.question.program.reprocess.programason"));  // 處理方案
        verifyUtil.date(vo.getAssignTime(), this.getMessage("form.event.assign.time"));                            // 事件發生時間
        
        if (isJustInfo) {
            return verifyUtil.build();
        }
        
        if ((isApplyLastLevel || (isReview && isVice)) && service.isAlertJobSCTWarning(vo)) {
            verifyUtil.append(getMessage("form.common.approval.warning.11"));
        }
        
        // 勾選上線失敗必填檢核
        verifyOnlineFail(verifyUtil, vo);
        
        // 申請最後一關[實際完成時間]必填
        if (valueOfBoolean(vo.getIsApplyLastLevel())) {
            verifyUtil.date(vo.getAct(), this.getMessage("form.search.column.inc.act"));
        }
        
        // 郵件地址
        if (StringUtils.isNotBlank(vo.getEmail())) {
            verifyUtil.email(vo.getEmail(), this.getMessage("form.question.form.info.email"));
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
    public EventFormVO jumpToReviewTargetLevel (@RequestBody EventFormVO vo) {
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
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public EventFormVO closeForm(@RequestBody EventFormVO vo) {
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
     * 退回經辦(本Action只有審核流程才會觸發)
     * @param vo
     * @return EventFormVO
     * @throws Exception
     */
    @PostMapping(path = "/backToPic")
    public EventFormVO backToPic (@RequestBody EventFormVO vo) throws Exception {
        //JumpLevel為0,且為審核流程的話,則可直接退到申請最後一關(申請最後一關必然是經辦)
        vo.setIsNextLevel(false);
        vo.setJumpLevel("0");
        vo.setVerifyResult(FormEnum.DISAGREED.name());
        
        service.verifying(vo);

        return vo;
    }
    
    /**
     * 是否有延伸單
     * @param vo
     */
    @PostMapping(path = "/hasStretchs")
    public boolean hasStretchs (@RequestBody EventFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }

    /**
     * 事件單：提醒使用者，判斷是否為相同的服務異常重複發生
     * @param vo
     * @return
     * @author jacky.fu
     */
    @PostMapping(path = "/hasTheSameServiceException")
    public FormSearchVO getFormFieldsInfo(@RequestBody EventFormVO vo) {
        FormSearchVO result = null;

        //連續2天開立的所有事件單
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        
        FormSearchVO searchVo = new FormSearchVO();
        searchVo.setInfoDateCreateTimeStart(DateUtils.toString(cal.getTime(), DateUtils._PATTERN_YYYYMMDD_SLASH));
        cal.add(Calendar.DATE,2);
        searchVo.setInfoDateCreateTimeEnd(DateUtils.toString(cal.getTime(), DateUtils._PATTERN_YYYYMMDD_SLASH));
        searchVo.setFormClass(vo.getFormClass());
        List<FormSearchVO> listVo = formSearchService.getIncFormsByCondition(searchVo);
        
        for(FormSearchVO voo : listVo) {
            
            //表單狀態「非擬案」「非已作廢」
            //任1張事件單的「事件(當下)排除時間 [ExcludeTime]」有值
            if(!FormEnum.PROPOSING.toString().equals(voo.getaStatus()) &&
                    !FormEnum.DEPRECATED.toString().equals(voo.getaStatus()) &&
                    voo.getExcludeTime() != null) {
                
                //組態元件(三欄)：皆一致
                if(StringUtils.equals(vo.getcCategory(),voo.getcCategory()) &&
                        StringUtils.equals(vo.getcClass(),voo.getcClass()) &&
                        StringUtils.equals(vo.getcComponent(),voo.getcComponent())) {
                }else {
                    continue;
                }
                
                //依據服務請求、服務異常之分級選項，系統自動計算(規則)事件優先處理權重：皆一致
                if(StringUtils.equals(vo.getEffectScope(),voo.getEffectScope()) &&
                        StringUtils.equals(vo.getUrgentLevel(),voo.getUrgentLevel()) &&
                        StringUtils.equals(vo.getEventPriority(),voo.getEventPriority())) {
                }else {
                    continue;
                }
                
                //系統名稱 (與當前事件單「系統名稱」值比對)
                if(StringUtils.equals(vo.getSystem(),voo.getSystem())) {
                }else {
                    continue;
                }
                
                result = voo;
                
            }
            
        }
        return result;
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }
    
    /**
     * 檢查是否允許退回經辦
     * 根據邏輯,只有審核關卡且為科長處理時,才可進行退回經辦
     * @param vo
     */
    private void isShowBackToPic (EventFormVO vo) {
        String loginTitleCode = getCurrentUserTitleCode();
        String targetTitleCode = processService.getUserTitileCode(vo.getGroupId());
        
        if (targetTitleCode.equalsIgnoreCase(loginTitleCode) &&
                FormEnum.REVIEW.name().equals(vo.getVerifyType())) {
            vo.setIsShowBackToPic(UserEnum.SC.name().equals(loginTitleCode));
        }
    }

    /**
     * 勾選上線失敗必填檢核
     * 
     * @param verifyUtil
     * @param vo
     */
    private void verifyOnlineFail(DataVerifyUtil verifyUtil, EventFormVO vo) {
        if (StringConstant.SHORT_YES.equals(vo.getIsOnlineFail())) {
            if (vo.getOnlineTime() == null) {
                verifyUtil.append(this.getMessage("form.event.onlinetime.message"));
            }
            if (StringUtils.isBlank(vo.getOnlineJobFormId())) {
                verifyUtil.append(this.getMessage("form.event.onlinejobformid.message"));
            }
        }
    }

}
