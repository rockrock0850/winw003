package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInfoIncDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoUserEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyIncEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewIncEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoUserRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyIncRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewIncRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ISysHolidayRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.EventFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 事件單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@Transactional
@Service("eventFormService")
public class EventFormServiceImpl extends BaseFormService<EventFormVO> implements IBaseFormService<EventFormVO> {
    
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoUserRepository formUserRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoIncDetailRepository formDetailRepo;
    @Autowired
    private IFormProcessDetailApplyIncRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewIncRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private ISysHolidayRepository holidayRepo;
    
    private String[] ignores = {"isNextLevel", "isBackToApply", "isVerifyAcceptable"};
    
    @Override
    public void updateVerifyLog (EventFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void ectExtended (EventFormVO vo) throws Exception {
        ectExtendedForStretchs(vo);
    }
    
    /**
     * 需要跳到審核流程哪一關
     * @param vo
     * @author adam.yeh
     */
    @Override
    public void jumpToReview (EventFormVO vo) {
        super.jumpToReview(vo);
    }

    @ModLog
    @Override
    public void getFormInfo (EventFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        EventFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, EventFormVO.class);

        vo.setFormWording(getMessage(FormEnum.INC.wording()));
        
        // 初始化表單基本資料
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, EventFormVO.class);
            formInfo.setUserSolving("");
            formInfo.setGroupSolving("");
            formInfo.setDivisionSolving("");
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setFormClass(FormEnum.INC.name());
            vo.setFormType(FormEnum.INC.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }
        
        BeanUtil.copyProperties(getProgram(vo.getFormId()), vo);
        BeanUtil.copyProperties(formInfo, vo);
        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);

        vo.setFormType(FormEnum.valueOf(vo.getFormClass()).formType());
        vo.setStatusWording(FormEnum.valueOf(vo.getFormStatus()).status());

        if (isNotPrepared(vo)) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(vo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), vo.getGroupName()));
            
            return;
        }
        
        resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());
        EventFormVO levelInfo = jdbcRepository.queryForBean(resource, params, EventFormVO.class);

        // 取得該關卡資訊
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateChangeIssue(isSubCreation(vo.getIsCreateChangeIssue(), levelInfo.getGroupId()));
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));

        // 判斷該登入者是否有具有審核資格
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        // 是否可直接結案
        vo.setIsCloseForm(verifyIsFormClose(vo));
        // 是否有權限開問題單
        vo.setIsAddQuestionIssue(isCreateQuestionIssue(vo));
        //是否為審核者
        vo.setIsApprover(isApprover(vo));
        //是否為申請流程最後一關
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
        vo.setIsEctExtended(StringConstant.SHORT_YES.equals(vo.getIsExtended()));
        
        setLastJobAct(vo);
    }

    @Override
    public void sendToVerification (EventFormVO vo) throws Exception {
        Date today = new Date();
        boolean isAssigning = vo.getIsAssigning();
        vo.setCreateTime(today);
        vo.setFormStatus(decideFormStatus(vo));
        vo.setProcessStatus(FormEnum.APPROVING.name());
        
        if (isUseDetail(vo)) {
            BaseFormVO newerDetail = newerProcessDetail(
                    vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass()));
            BeanUtil.copyProperties(newerDetail, vo, ignores);
        }

        if (isAssigning) {
            FormProcessDetailApplyIncEntity pojo =
                    applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), 2);
            vo.setGroupSolving(pojo.getGroupId());
        }
        
        vo.setEct(findEventEct(today, vo));
        
        // 更新表單
        mergeFormInfo(vo);
        FormEntity pojo = formRepo.findByFormId(vo.getFormId());
        pojo.setProcessName(vo.getProcessName());
        pojo.setCreateTime(today);
        formRepo.save(pojo);
        vo.setCompleteTime(today);
        vo.setUserId(UserInfoUtil.loginUserId());
        vo.setGroupId(fetchLoginUser().getGroupId());
        
        // 初始化申請流程
        if (isEmptyVerifyLog(vo)) {
            if (isAssigning) {
                saveLevel(vo, "經辦處理", FormEnum.APPLY, FormEnum.SENT);
                vo.setUserId(null);
                vo.setVerifyLevel("1");
                vo.setCompleteTime(null);
                vo.setVerifyType(FormEnum.APPLY.name());
                vo.setGroupId(MapUtils.getString(getProcessLevel(
                        vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
                saveLevel(vo, 1, FormEnum.APPLY, FormEnum.PENDING);
                
                return;
            }

            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }

        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyIncEntity> applyList =
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel);
        applyVerification(vo, applyList);
        formDateRepo.updateOectByFormId(vo.getEct(), vo.getFormId());
    }

    @Override
    public void prepareVerifying(EventFormVO vo) {
        prepareVerifyWording(vo);
    }
    
    @Override
    public void verifying (EventFormVO vo) throws Exception {
        updateCurrentLevel(vo);
        setUpFormProcess(vo);
        
        // 表單作廢
        if (FormEnum.DEPRECATED.equals(
                FormEnum.valueOf(vo.getFormStatus()))) {
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
            
            return;
        }

        if (isBackToApplyLevel1(vo)) {
            vo.setUserSolving(null);
            vo.setFormStatus(FormEnum.ASSIGNING.name());
            vo.setIsBackToApplyLevel1(true);
            mergeFormInfo(vo);
        }
        
        if (isBackToApplyProcess(vo)) {
            backToApply(vo);
            vo.setFormStatus(FormEnum.APPROVING.name());
            mergeFormInfo(vo);
        }
        
        List<Integer> limits = calculateLimitNumber(vo);
        
        // 跳關以及新增下關
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                List<FormProcessDetailApplyIncEntity> applyList =
                        applyRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                applyVerification(vo, applyList);
                
                break;

            case REVIEW:
                List<FormProcessDetailReviewIncEntity> reviewList =
                        reviewRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                reviewVerification(vo, reviewList);
                
                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(EventFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // 系統原本更新關卡資料的邏輯
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm (EventFormVO vo, FormVerifyType type) {
        boolean result = false;
        
        if (type == FormVerifyType.STRETCH_ZERO) {
            result = getStretchs(vo.getFormId()) > 0;
        } else if (type == FormVerifyType.STRETCH_FINISHED) {
            result = isStretchFormClosed(vo);
        }
        
        return result;
    }

    @Override
    protected int getStretchs (String formId) {
        List<String> formStatusNoInList = new ArrayList<>();
        formStatusNoInList.add(FormEnum.CLOSED.name());
        formStatusNoInList.add(FormEnum.DEPRECATED.name());
        
        List<String> formClassNoInList = new ArrayList<>();
        formClassNoInList.add(FormEnum.Q.toString());
        
        return formRepo.countBySourceIdAndFormStatusNotInAndFormClassNotIn(formId, formStatusNoInList,formClassNoInList);
    }

    @Override
    public void deleteForm (EventFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo (EventFormVO vo) throws Exception {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// 新增
            vo.setDetailId("");
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.INC));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormInfoUserEntity formUserPojo = new FormInfoUserEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoIncDetailEntity formDetailPojo = new FormInfoIncDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formUserPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formDatePojo.setCreateTime(vo.getAssignTime());

            formRepo.save(formPojo);
            formUserRepo.save(formUserPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
        } else {
            vo.setEct(findEventEct(vo.getCreateTime(), vo));
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_INC.getResource("UPDATE_INC_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
            saveFormAlterResult(formId, false);
        }
    }

    @Override
    public void saveProgram (EventFormVO vo) {
        Date today = new Date();
        FormProgramEntity pojo = new FormProgramEntity();
        BeanUtil.copyProperties(vo, pojo);
        
        if (StringUtils.isBlank(pojo.getCreatedBy())) {
            pojo.setCreatedBy(UserInfoUtil.loginUserId());
            pojo.setCreatedAt(today);
        }
        
        pojo.setUpdatedBy(UserInfoUtil.loginUserId());
        pojo.setUpdatedAt(today);
        programRepo.save(pojo);
        BeanUtil.copyProperties(pojo, vo);
    }
    
    @Override
    public EventFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        EventFormVO vo = new EventFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }

    @Override
    public EventFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), EventFormVO.class);
    }

    @Override
    public boolean isVerifyAcceptable (EventFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed (EventFormVO vo) {
        return isClosed(vo.getFormId());
    }
    
    @Override
    public boolean isNewerDetailExist (EventFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }
    
    @Override
    public EventFormVO getFormDetailInfo(String formId) {
        EventFormVO vo = new EventFormVO();
        FormInfoIncDetailEntity pojo = formDetailRepo.findByFormId(formId);
        BeanUtil.copyProperties(pojo, vo, new String[] {"updatedAt"});
        
        return vo;
    }
    
    @Override
    public void notifyProcessMail (EventFormVO vo) {
        asyncMailLauncher(vo);
    }
    
    /**
     * 副科/副理 表單直接結案
     * 
     * @param vo
     */
    @Override
    public void immediateClose(EventFormVO vo) {
        closeFormForImmdiation(vo);
    }

    @Override
    public void lockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_YES, formId);
    }

    @Override
    public void unlockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_NO, formId);
    };

    @Override
    public boolean isAllStretchDied (EventFormVO vo) {
        return super.isAllStretchDied(vo);
    }
    
    @Override
    public List<String> getFormCountsignList(String formId) {
        FormInfoIncDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
        List<String> rtnLs = new ArrayList<>();
        
        if(!Objects.isNull(detailEntity) && StringUtils.isNotBlank(detailEntity.getCountersigneds())) {
            rtnLs = Arrays.asList(detailEntity.getCountersigneds().split(","));
        }
        return rtnLs;
    }

    @Override
    protected String getReviewLastLevel (String detailId) {
        return String.valueOf(
                reviewRepo.findByDetailId(detailId).size());
    }

    @Override
    protected String getFormApplyGroupInfo(EventFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyIncEntity formProcessDetailApplyIIncEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplyIIncEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyIncEntity formProcessDetailApplyIIncEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplyIIncEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(EventFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewIncEntity formProcessDetailReviewIncEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailReviewIncEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewIncEntity formProcessDetailReviewIncEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewIncEntity.getGroupId();
    }

    @Override
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        if(FormEnum.APPLY.name().equalsIgnoreCase(verifyType)) {
            int count = applyRepo.countByDetailId(detailId);
            return (count == Integer.parseInt(verifyLevel))? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
        }
        
        return StringConstant.SHORT_NO;
    }
    
    /**
     * 找出目標解決時間
     * @param tempTime
     * @param vo
     * @return
     * @throws Exception
     * @author adam.yeh
     */
    private Date findEventEct (Date tempTime, EventFormVO vo) throws Exception {
        String priority = vo.getEventPriority();
        Calendar c = Calendar.getInstance();
        c.setTime(tempTime);
        
        if ("1".equals(priority)) {
            c.add(Calendar.HOUR, 4);
            tempTime = c.getTime();
        } else if ("2".equals(priority)) {
            c.add(Calendar.HOUR, 8);
            tempTime = c.getTime();
        } else if ("3".equals(priority)) {
            tempTime = autoPassHoliday(tempTime, 2);
        } else if ("4".equals(priority)) {
            tempTime = autoPassHoliday(tempTime, 3);
        }
        
        vo.setIsServerSideUpdated(true);
        
        return tempTime;
    }

    /**
     * 自動跳過例假日
     * @param time
     * @param count
     * @return
     * @throws Exception
     * @author adam.yeh
     */
    private Date autoPassHoliday (Date time, int count) throws Exception {
        // 強制把日期的格式改成YYYY/MM/DD
        String temp = DateUtils.toString(time, DateUtils._PATTERN_YYYYMMDD_SLASH);
        time = DateUtils.fromString(temp, DateUtils._PATTERN_YYYYMMDD_SLASH);
        
        time = recursiveHoliday(time, count);
        
        temp = DateUtils.toString(time, DateUtils._PATTERN_YYYYMMDD_SLASH);
        temp += " 17:00:00";

        return DateUtils.fromString(temp, DateUtils.pattern12);
    }

    // 遞迴判斷遇到例假日則往後加1天
    private Date recursiveHoliday (Date tempTime, int count) {
        count += holidayRepo.countByDateAndIsHoliday(tempTime, "Y");
        
        if (count > 0) {
            tempTime = DateUtils.getDateByOffset(tempTime, 1);
            tempTime = recursiveHoliday(tempTime, --count);
        }
        
        return tempTime;
    }

    private boolean isEmptyVerifyLog (EventFormVO vo) {
        return (verifyRepo.countByFormId(vo.getFormId()) <= 0);
    }

    private String decideByAssigning (EventFormVO vo) {
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        return vo.getIsAssigning() ? form.getUserSolving() : null;
    }

    private boolean isNotPrepared (EventFormVO vo) {
        return !(FormEnum.APPROVING.name().equals(vo.getFormStatus()) ||
                FormEnum.ASSIGNING.name().equals(vo.getFormStatus()) ||
                FormEnum.SELFSOLVE.name().equals(vo.getFormStatus()));
    }

    private void setUpFormProcess (EventFormVO vo) {
        boolean isAssigning = vo.getIsAssigning();
        boolean isSelfSolve = vo.getIsSelfSolve();
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        
        if (isAssigning) {
            ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_USER_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", vo.getUserSolving());
            SysUserVO userInfo = jdbcRepository.queryForBean(resource, params, SysUserVO.class);
            vo.setGroupSolving(userInfo.getGroupId());
        }
        
        if (isAssigning || isSelfSolve) {
            vo.setFormStatus(FormEnum.APPROVING.name());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
        }
    }

    // 判斷起單流程
    private String decideFormStatus (EventFormVO vo) {
        FormEnum status = vo.getIsAssigning() ? FormEnum.ASSIGNING : FormEnum.APPROVING;
        return status.name();
    }
    
    /*
     * 1. 新增待申請關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否轉換為審核流程
     */
    private void applyVerification (EventFormVO vo, List<FormProcessDetailApplyIncEntity> jumpList) {
        // 申請流程最後一關已通過並進入審核流程第一階段
        if (CollectionUtils.isEmpty(jumpList)) {
            vo.setUserId(null);
            vo.setVerifyLevel("1");
            vo.setVerifyType(FormEnum.REVIEW.name());
            vo.setGroupId(MapUtils.getString(getProcessLevel(
                    vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
            saveLevel(vo, 1, FormEnum.REVIEW, FormEnum.PENDING);// 新增審核流程第一關
            
            return;
        }
        
        if (!vo.getIsNextLevel()) {
            Collections.reverse(jumpList);
        }
        
        // 新增待申請關卡
        int size = jumpList.size();
        FormEnum verifyResult = null;
        FormProcessDetailApplyIncEntity applyPojo = jumpList.get(size-1);
        
        vo.setCompleteTime(null);
        if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
            verifyResult = applyPojo.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
            vo.setUserId(decideByAssigning(vo));
            vo.setGroupId(applyPojo.getGroupId());
            saveLevel(vo, applyPojo.getProcessOrder(), FormEnum.APPLY, verifyResult);
        } else {
            // 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // 寫入關卡
            verifyResult = FormEnum.PENDING;
            vo.setUserId(null); // 退回群組
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, applyPojo.getProcessOrder(), FormEnum.APPLY, verifyResult);
            vo.setUserId(back.getUserId());// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        }
    }

    /*
     * 1. 新增待審核關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否可結案
     */
    private void reviewVerification (EventFormVO vo, List<FormProcessDetailReviewIncEntity> jumpList) {
        if (CollectionUtils.isEmpty(jumpList)) {
            vo.setVerifyResult(FormEnum.CLOSED.name());
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            closeForm(form);
            
            return;
        }

        if (!vo.getIsNextLevel()) {
            Collections.reverse(jumpList);
        }
        
        // 新增待審核關卡
        int size = jumpList.size();
        FormProcessDetailReviewIncEntity reviewPojo = jumpList.get(size-1);
        if (vo.getIsNextLevel()) {
            vo.setUserId(null);
            vo.setGroupId(reviewPojo.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {
            // 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // 寫入關卡
            vo.setUserId(null); // 退回群組
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            // 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
            vo.setUserId(back.getUserId());
        }
        
    }

    // 判斷是否需要產生流程編號
    private boolean isUseDetail (EventFormVO vo) {
        return (vo.getIsAssigning() || vo.getIsSelfSolve());
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

}
