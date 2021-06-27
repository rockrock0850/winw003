package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;
import com.ebizprise.winw.project.entity.FormJobCountersignedEntity;
import com.ebizprise.winw.project.entity.FormJobDivisionMappingEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobInfoSysDetailEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormJobCheckPersonRepository;
import com.ebizprise.winw.project.repository.IFormJobCountersignedRepository;
import com.ebizprise.winw.project.repository.IFormJobDivisionMappingRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoSysDetailRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.SpJobCFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * SP工作會辦單 新增/修改/審核/退回等作業
 * 
 * @author emily.lai
 */
@Transactional
@Service("spJobCFormService")
public class SpJobCFormServiceImpl extends BaseFormService<SpJobCFormVO> implements IBaseFormService<SpJobCFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(SpJobCFormServiceImpl.class);

    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormJobInfoDateRepository formDateRepo;
    @Autowired
    private IFormJobInfoSysDetailRepository formDetailRepo;
    @Autowired
    private IFormProcessDetailApplyJobRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewJobRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormJobCountersignedRepository jobcRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private IFormJobCheckPersonRepository personRepo;
    @Autowired
    private IFormJobDivisionMappingRepository jobDivisionMappingRepo;
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public void updateVerifyLog (SpJobCFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void create(SpJobCFormVO vo) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        
        vo.setSourceId(vo.getFormId());
        vo.setUserCreated(loginUser.getUserId());
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setFormStatus(FormEnum.PROPOSING.name());
        vo.setProcessStatus(FormEnum.PROPOSING.name());
        vo.setStatusWording(FormEnum.PROPOSING.status());
        vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(loginUser.getGroupName()));
        vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), loginUser.getGroupName()));
        vo.setCreatedAt(today);
        vo.setCreatedBy(UserInfoUtil.loginUserId());
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(UserInfoUtil.loginUserId());
        vo.setSummary(vo.getFormId() + ":" + vo.getPurpose());
        
        // 表單資訊預設值
        // 系統負責人員
        vo.setUserId(vo.getUserCreated());
        LdapUserEntity user = ldapUserRepository.findByUserIdAndIsEnabled(vo.getUserId(),
            StringConstant.SHORT_YES);
        if (ObjectUtils.isNotEmpty(user)) {
            vo.setUserName(user.getName());
        }
        
        FormJobCountersignedEntity jobcPojo = jobcRepo.findByFormIdAndDivision(vo.getSourceId(), "SP");
        if (jobcPojo != null) {
            vo.setMect(jobcPojo.getSct());
            vo.setRemark(jobcPojo.getDescription());
            vo.setIsTest(jobcPojo.getIsTest());
            vo.setIsProduction(jobcPojo.getIsProduction());
        }
        
        vo.setStatus(FormEnum.PROPOSING.status()); // 系統狀態
        
        // 核准狀態=來源工作單「表單狀態」
        ResourceEnum resource =
                ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_SEARCH_INFO_BY_FORM_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getSourceId());
        Map<String, Object> sourceFormInfo = jdbcRepository.queryForMap(resource, params);
        vo.setAstatus(FormEnum.valueOf(MapUtils.getString(sourceFormInfo, "formStatus")).status());

        // 清空內容
        vo.setFormId(null);
        vo.setAssignTime(null);
        vo.setEot(null);
        vo.setAst(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setCreateTime(null);
        vo.setUserSolving(null);
        vo.setGroupSolving(null);
        vo.setVerifyLevel(null);
        vo.setVerifyType(null);
        vo.setIsCreateChangeIssue(null);
        vo.setIsCreateCIssue(null);
        vo.setIsModifyColumnData(null);
        vo.setIsVerifyAcceptable(false);
        vo.setCountersigneds(null);
    }

    @ModLog
    @Override
    public void getFormInfo(SpJobCFormVO vo) {
        vo.setFormWording(getMessage(FormEnum.JOB_SP_C.wording()));
        
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION
            .getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        /*
         * 取得SQL檔的設計有race conditions的缺陷。 
         * 因為裝載SQL的物件是列舉類型, 
         * 所以當兩個使用者剛好同時對ResourceEnum.getResource()的時候, 有機會取到錯誤的SQL檔。
         * FIXME 要找時間要重新審視取得SQL的設計
         */
        int count = 0;
        int buffer = 1000;
        logger.info("工作單表單資訊的代償機制開始。" + resource.file());
        while (!StringUtils.contains(resource.file(), "FIND_FORM_INFO_JOB_BY_CONDITIONS")) {
            count++;
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
            
            if (count > buffer) {
                logger.info("代償機制執行失敗, 替換成工作單專用SQL檔失敗。");
                break;
            }
        }
        logger.info("工作單表單資訊的代償機制結束。" + resource.file());
        
        SpJobCFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, SpJobCFormVO.class);

        if (formInfo == null) {
            return;
        }

        BeanUtil.copyProperties(formInfo, vo);
        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        vo.setFormType(FormEnum.valueOf(vo.getFormClass()).formType());
        vo.setStatusWording(FormEnum.valueOf(vo.getFormStatus()).status());
        
        // 取得人員姓名
        LdapUserEntity user = ldapUserRepository
                .findByUserIdAndIsEnabled(vo.getUserId(), StringConstant.SHORT_YES);
        if (ObjectUtils.isNotEmpty(user)) {
            vo.setUserName(user.getName());
        }

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
        SpJobCFormVO levelInfo = jdbcRepository.queryForBean(resource, params, SpJobCFormVO.class);

        if (isClosed(vo.getFormId())) {
            return;
        }

        // 取得該關卡資訊
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateChangeIssue(isSubCreation(vo.getIsCreateChangeIssue(), levelInfo.getGroupId()));
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        
        String formStatusWording =
                StringConstant.SHORT_YES.equals(vo.getIsWorkLevel()) ?
                levelInfo.getWorkProjectName() : levelInfo.getGroupName();
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(formStatusWording));
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
        vo.setIsCreateSPJobIssue(isCreateJobIssue(vo));
        // 判斷該登入者是否有具有審核資格
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        //是否可直接結案
        vo.setIsCloseForm(verifyIsFormClose(vo));
        //是否為審核者
        vo.setIsApprover(isApprover(vo));
        // 是否為作業關卡的指定人員
        vo.setIsOwner(isOwner(vo));
        // 是否為平行會辦期間
        vo.setIsParallel(isParalleling(vo));
        vo.setParallel(fetchParallel(vo));
        //是否為申請流程最後一關
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
    }

    @Override
    public void sendToVerification(SpJobCFormVO vo) {
        // 更新表單
        Date today = new Date();
        vo.setCreateTime(today);
        vo.setAssignTime(today);
        vo.setFormStatus(FormEnum.ASSIGNING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());
        
        // 取申請第2關的流程群組寫入處理群組
        ResourceEnum resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
        Map<String, Object> params = new HashMap<>();
        params.put("detailId", vo.getDetailId());
        params.put("processOrder", 2);
        Map<String, Object> pojo = jdbcRepository.queryForMap(resource, params);
        vo.setGroupSolving(MapUtils.getString(pojo, "GroupId"));
        
        mergeFormInfo(vo);

        // 初始化申請流程
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(today);
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, "經辦處理", FormEnum.APPLY, FormEnum.SENT);
            
            // 實際審核關卡
            vo.setUserId(null);
            vo.setVerifyLevel("1");
            vo.setCompleteTime(null);
            vo.setVerifyType(FormEnum.APPLY.name());
            vo.setGroupId(MapUtils.getString(getProcessLevel(
                    vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.PENDING);
            
            return;
        }
        
        vo.setCompleteTime(today);
        vo.setUserId(UserInfoUtil.loginUserId());
        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyJobEntity> applyList = applyRepo.findLimitList(vo.getDetailId(),
            verifyLevel, jumpLevel);
        applyVerification(vo, applyList);
    }
    
    @Override
    public void prepareVerifying(SpJobCFormVO vo) {
        prepareVerifyWording(vo);
    }

    @Override
    public void verifying(SpJobCFormVO vo) {
        updateCurrentLevel(vo);
        
        // 指派轉審核中
        if (FormEnum.ASSIGNING.equals(FormEnum.valueOf(vo.getFormStatus()))) {
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            vo.setFormStatus(FormEnum.APPROVING.name());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
        }
        
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
        }
        
        List<Integer> limits = calculateLimitNumber(vo);
        
        // 跳關以及新增下關
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                List<FormProcessDetailApplyJobEntity> applyList =
                        applyRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                applyVerification(vo, applyList);
                
                break;

            case REVIEW:
                List<FormProcessDetailReviewJobEntity> reviewList =
                        reviewRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                reviewVerification(vo, reviewList);
                
                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(SpJobCFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // 系統原本更新關卡資料的邏輯
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm(SpJobCFormVO vo, FormVerifyType type) {
        return isStretchFormClosed(vo);
    }

    @Override
    public void deleteForm(SpJobCFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo(SpJobCFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) { // 新增
            BeanUtil.copyProperties(newerProcessDetail(vo.getDivisionSolving(), FormEnum.JOB_SP_C), vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.JOB_C));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormJobInfoDateEntity formDatePojo = new FormJobInfoDateEntity();
            FormJobInfoSysDetailEntity formDetailPojo = new FormJobInfoSysDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formRepo.save(formPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
        } else {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("UPDATE_JOB_SP_C_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
        }
    }

    @Override
    public boolean isVerifyAcceptable(SpJobCFormVO vo, SysUserVO userInfo) {
        boolean isVerifyAcceptable = false;
        ResourceEnum resource = ResourceEnum.
                SQL_FORM_OPERATION.getResource("FIND_VERIFY_LOGS_BY_CONDITIONS");
        
        Conditions conditions = new Conditions();
        conditions.and().isNull("FVL.Completetime");
        conditions.and().equal("FVL.VerifyLevel", vo.getVerifyLevel());

        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        
        List<FormVerifyLogEntity> levelList = jdbcRepository.
                queryForList(resource, conditions, params, FormVerifyLogEntity.class);
        
        resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
        
        for (FormVerifyLogEntity entity : levelList) {
            if (isAcceptable(vo, entity, resource, userInfo) &&
                    isParallelApprover(vo, entity, userInfo)) {
                isVerifyAcceptable = true;
                break;
            }
        }

        return isVerifyAcceptable;
    }

    @Override
    public boolean isFormClosed(SpJobCFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist(SpJobCFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (SpJobCFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public void immediateClose (SpJobCFormVO vo) {
         closeFormForImmdiation(vo);
    }

    @Override
    public SpJobCFormVO newerProcessDetail(String division, FormEnum e) {
        return BeanUtil.fromJson(
            getRecentlyDetail(division, e), SpJobCFormVO.class);
    }

    @Override
    public SpJobCFormVO getFormDetailInfo(String formId) {
        SpJobCFormVO vo = new SpJobCFormVO();
        BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), vo, new String[] {"updatedAt"});
        return vo;
    }

    @Override
    public boolean isAllStretchDied (SpJobCFormVO vo) {
        return super.isAllStretchDied(vo);
    }
    

    @Override
    public void lockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_YES, formId);
    }

    @Override
    public void unlockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_NO, formId);
    }

	@Override
    public void sendSplitProcess(SpJobCFormVO vo) {
    	String sendUserId = vo.getCurrentProcess();
    	LdapUserEntity loginUser = sysUserService.findUserByUserId(UserInfoUtil.loginUserId());
    	LdapUserEntity sendUser = sysUserService.findUserByUserId(sendUserId);
    	SysGroupEntity sysGroup = sysGroupRepository.findBySysGroupId(Long.parseLong(sendUser.getSysGroupId()));
    	String sendGroupId = sysGroup.getGroupId();

    	Date today = new Date();
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        BeanUtil.copyProperties(vo, form);
        formRepo.save(form);
    	
        // Update
        List<FormJobDivisionMappingEntity> entities = jobDivisionMappingRepo.findByDivision(vo.getCurrentProcess());
        String processName = CollectionUtils.isNotEmpty(entities) ? entities.get(0).getDivisionName() : vo.getCurrentProcess();
        FormVerifyLogEntity back = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        if (back != null) {
        	String comment = getMessage("form.question.form.info.splitprocess.message", new String[] {loginUser.getName(), sendUser.getName()});
        	back.setVerifyComment(comment);
        	back.setVerifyResult(FormEnum.AGREED.name());
        	back.setUpdatedBy(UserInfoUtil.loginUserId());
        	back.setUpdatedAt(today);
        	back.setCompleteTime(today);
        	verifyRepo.save(back);
        }
        
        // Save
        FormVerifyLogEntity verifyPojo = new FormVerifyLogEntity();
        if (back != null) {
        	BeanUtil.copyProperties(back, verifyPojo, "id", "completeTime", "verifyComment");
        }
        verifyPojo.setUserId(sendUserId);
        verifyPojo.setGroupId(sendGroupId);
        verifyPojo.setSubmitTime(today);
        verifyPojo.setCreatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setCreatedAt(today);
        verifyPojo.setUpdatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setUpdatedAt(today);
        verifyPojo.setVerifyResult(FormEnum.PENDING.name());
        verifyRepo.save(verifyPojo);
    }
    
    @Override
    protected String getReviewLastLevel (String detailId) {
        return String.valueOf(
                reviewRepo.findByDetailId(detailId).size());
    }

    @Override
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        if(FormEnum.APPLY.name().equalsIgnoreCase(verifyType)) {
            int count = applyRepo.countByDetailId(detailId);
            return (count == Integer.parseInt(verifyLevel))? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
        }
        
        return StringConstant.SHORT_NO;
    }

    @Override
    protected String getFormApplyGroupInfo(SpJobCFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity formProcessDetailApplySrEntity = applyRepo
            .findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity formProcessDetailApplySrEntity = applyRepo
                .findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(SpJobCFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewJobEntity formProcessDetailReviewJobEntity = reviewRepo
            .findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));
        
        return formProcessDetailReviewJobEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewJobEntity formProcessDetailReviewJobEntity = reviewRepo
                .findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewJobEntity.getGroupId();
    }


    // 新增下一關
    private void addNextLevel (
            SpJobCFormVO vo,
            int processOrder,
            FormEnum verifyResult,
            FormProcessDetailApplyJobEntity apply) {
        boolean isParallel = StringConstant.
                SHORT_YES.equals(apply.getIsParallel());
        
        if (isParallel) {
            saveParallels(vo, apply, processOrder, verifyResult);
        } else {
            vo.setParallel(null);
            saveLevel(vo, processOrder, FormEnum.APPLY, verifyResult);
        }
        
        vo.setIsParallel(isParallel ?
                StringConstant.SHORT_YES : StringConstant.SHORT_NO);
    }

    // 新增退回關卡
    private void addBackLevel (SpJobCFormVO vo, int processOrder) {
        boolean isInTheList = false;
        boolean isParallel = !StringUtils.isBlank(vo.getParallel());
        boolean isCsPerson = StringUtils.isNotEmpty(vo.getCsPerson());
        ResourceEnum resource = ResourceEnum.
                SQL_FORM_OPERATION.getResource("FIND_DISTINCT_BACK_LIST");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyType", vo.getVerifyType());
        params.put("verifyLevel", vo.getJumpLevel());
        List<FormVerifyLogEntity> backList = jdbcRepository.
                queryForList(resource, params, FormVerifyLogEntity.class);
        List<String> parallelIds = fetchParallelIds(vo.getFormId());

        List<String> recipients = new ArrayList<>();
        deprecatedParallelChildFroms(vo, isParallel);
        for (FormVerifyLogEntity back : backList) {
            isParallel = !StringUtils.isBlank(back.getParallel());
            isInTheList = parallelIds.contains(back.getUserId().toUpperCase());
            
            if (isCsPerson) {// 是否抓到流程作業人員
                vo.setUserId(vo.getCsPerson());
            } else if (!isParallel) {
                vo.setUserId(null);
            } else {
                if (!isInTheList) {
                    // 只有在該表單"當前"所選的平行會辦人員能夠記錄在退關歷程內。
                    continue;
                }
                vo.setUserId(back.getUserId());
            }
            
            vo.setParallel(back.getParallel());
            saveLevel(vo, processOrder, FormEnum.APPLY, FormEnum.PENDING);
            recipients.add(back.getUserId());
            vo.setUserId(back.getUserId());// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
            
            if (!isParallel) {
                // 如果不是平行會辦關卡, 但又是以群組的方式審核的關卡, 
                // 關卡審核者有機率為不同人, 因此會導致退關卡清單出現重複的關卡, 
                // 最後在審核紀錄押上多關待審關卡造成資料錯誤。
                // 因此如果不是平行會辦關卡, 直接取退關清單內第一筆資料。
                break;
            }
        }
        
        vo.setRecipients(recipients);// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        vo.setIsParallel(isParallel ?
                StringConstant.SHORT_YES : StringConstant.SHORT_NO);
    }

    // 找目前最新的平行會辦人員清單
    private List<String> fetchParallelIds (String formId) {
        List<String> names = new ArrayList<>();
        
        FormJobInfoSysDetailEntity detail = formDetailRepo.findByFormId(formId);
        List<HtmlVO> spcs = BeanUtil.fromJsonToList(detail.getSpcGroups(), HtmlVO.class);
        for (HtmlVO spc : spcs) {
            names.add(spc.getUserId().toUpperCase());
        }
        
        return names;
    }

    private void saveParallels (SpJobCFormVO vo, FormProcessDetailApplyJobEntity apply, int processOrder, FormEnum verifyResult) {
        SpJobCFormVO tempVO = new SpJobCFormVO();
        FormJobInfoSysDetailEntity detail = formDetailRepo.findByFormId(vo.getFormId());
        List<HtmlVO> spcGroups = BeanUtil.fromJsonToList(detail.getSpcGroups(), HtmlVO.class);

        List<String> recipients = new ArrayList<>();
        for (HtmlVO spc : spcGroups) {
            BeanUtil.copyProperties(vo, tempVO);
            tempVO.setUserId(spc.getUserId());
            tempVO.setParallel(spc.getValue());
            saveLevel(tempVO, processOrder, FormEnum.APPLY, verifyResult);
            recipients.add(tempVO.getUserId());
        }
        
        vo.setRecipients(recipients);// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
    }

    private boolean isNotPrepared (SpJobCFormVO vo) {
        return !(FormEnum.APPROVING.name().equals(vo.getFormStatus()) ||
                FormEnum.ASSIGNING.name().equals(vo.getFormStatus()));
    }
    
    private String isCreateJobIssue (SpJobCFormVO vo) {
        boolean isCreating = StringConstant.SHORT_YES.equals(vo.getIsCreateSPJobIssue());
        boolean isMatchDivision = fetchLoginUser().getDivisionSolving().equals(vo.getDivisionSolving());
        return (isCreating && isMatchDivision) ? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
    }
    
    /*
     * 1. 新增待申請關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否轉換為審核流程
     */
    private void applyVerification(SpJobCFormVO vo, List<FormProcessDetailApplyJobEntity> jumpList) {
        boolean isParallel = Boolean.valueOf(vo.getIsParallel());
        
        // 申請流程最後一關已通過並進入審核流程第一階段
        if (CollectionUtils.isEmpty(jumpList) && !isParallel) {
            vo.setUserId(null);
            vo.setParallel(null);
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
        if (!isParallel) {
            FormEnum verifyResult = null;
            FormProcessDetailApplyJobEntity nextLevel = jumpList.get(size - 1);
            boolean isWorkLevel = StringConstant.SHORT_YES.equals(nextLevel.getIsWorkLevel());
            FormJobCheckPersonListEntity checkPerson = personRepo.
                    findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

            vo.setCompleteTime(null);
            vo.setGroupId(nextLevel.getGroupId());
            if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
                verifyResult = nextLevel.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
                vo.setUserId(isWorkLevel ? checkPerson.getUserId() : null);
                addNextLevel(vo, nextLevel.getProcessOrder(), verifyResult, nextLevel);
            } else {// 寫入關卡前取得前一關資訊
                //退回關卡是否有指定的處理人員
                if(ObjectUtils.isNotEmpty(checkPerson)) {
                    vo.setCsPerson(checkPerson.getUserId());
                }
                addBackLevel(vo, nextLevel.getProcessOrder());
            }
        }
    }

    /*
     * 1. 新增待審核關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否可結案
     */
    private void reviewVerification (SpJobCFormVO vo, List<FormProcessDetailReviewJobEntity> jumpList) {
        if (CollectionUtils.isEmpty(jumpList)) {
            vo.setVerifyResult(FormEnum.CLOSED.name());
            FormEntity formPojo = formRepo.findByFormId(vo.getFormId());
            formPojo.setFormStatus(FormEnum.CLOSED.name());
            formPojo.setProcessStatus(FormEnum.CLOSED.name());
            formRepo.save(formPojo);
            
            return;
        }

        if (!vo.getIsNextLevel()) {
            Collections.reverse(jumpList);
        }
        
        // 新增待審核關卡
        int size = jumpList.size();
        FormProcessDetailReviewJobEntity nextLevel = jumpList.get(size - 1);
        boolean isWorkLevel = StringConstant.SHORT_YES.equals(nextLevel.getIsWorkLevel());
        FormJobCheckPersonListEntity checkPerson = personRepo.
                findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

        vo.setCompleteTime(null);
        vo.setUserId(isWorkLevel ? checkPerson.getUserId() : null);
        if (vo.getIsNextLevel()) {
            vo.setGroupId(nextLevel.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {// 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo.
                    findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            vo.setUserId(back.getUserId());// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        }
        
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

}
