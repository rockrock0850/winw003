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
import com.ebizprise.winw.project.entity.FormInternalProcessStatusEntity;
import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;
import com.ebizprise.winw.project.entity.FormJobDivisionMappingEntity;
import com.ebizprise.winw.project.entity.FormJobInfoApDetailEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormContentModifyLogRepository;
import com.ebizprise.winw.project.repository.IFormInternalProcessStatusRepository;
import com.ebizprise.winw.project.repository.IFormJobCheckPersonRepository;
import com.ebizprise.winw.project.repository.IFormJobDivisionMappingRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoApDetailRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.ApJobCFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * AP??????????????? ??????/??????/??????/???????????????
 * 
 * @author emily.lai
 */
@Transactional
@Service("apJobCFormService")
public class ApJobCFormServiceImpl extends BaseFormService<ApJobCFormVO> implements IBaseFormService<ApJobCFormVO> {

    private static final Logger log = LoggerFactory.getLogger(ApJobCFormServiceImpl.class);

    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormJobInfoDateRepository formDateRepo;
    @Autowired
    private IFormJobInfoApDetailRepository formDetailRepo;
    @Autowired
    private IFormProcessDetailApplyJobRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewJobRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private IFormJobCheckPersonRepository personRepo;
    @Autowired
    private IFormInternalProcessStatusRepository internalProcessRepo;
    @Autowired
    private IFormJobDivisionMappingRepository jobDivisionMappingRepo;
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public void updateVerifyLog (ApJobCFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void create (ApJobCFormVO vo) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        
        vo.setSourceId(vo.getFormId());
        vo.setUserCreated(loginUser.getUserId());
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setProcessStatus(FormEnum.PROPOSING.name());
        vo.setStatusWording(FormEnum.PROPOSING.status());
        vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(loginUser.getGroupName()));
        vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), loginUser.getGroupName()));
        vo.setCreatedAt(today);
        vo.setCreatedBy(UserInfoUtil.loginUserId());
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(UserInfoUtil.loginUserId());
        vo.setIsModifyProgram(vo.getIsModifyProgram());

        vo.setEot(null);
        vo.setAst(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setFormId(null);
        vo.setCreateTime(null);
        vo.setUserSolving(null);
        vo.setGroupSolving(null);
        vo.setVerifyType(null);
        vo.setVerifyLevel(null);
        vo.setFormStatus(null);
        vo.setAssignTime(null);
        vo.setUserId(null);
        vo.setCountersigneds(null);
    }

    @ModLog
    @Override
    public void getFormInfo(ApJobCFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if(!FormEnum.PROPOSING.name().equals(vo.getFormStatus()) && !isClosed(vo.getFormId())) {
            if (!StringUtils.isBlank(vo.getVerifyType())) {
                conditions.and().isNull("FVL.CompleteTime");
                conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
            }
        }
        
        /*
         * ??????SQL???????????????race conditions???????????? 
         * ????????????SQL????????????????????????, 
         * ???????????????????????????????????????ResourceEnum.getResource()?????????, ????????????????????????SQL??????
         * FIXME ?????????????????????????????????SQL?????????
         */
        int count = 0;
        int buffer = 1000;
        log.info("?????????????????????????????????????????????" + resource.file());
        while (!StringUtils.contains(resource.file(), "FIND_FORM_INFO_JOB_BY_CONDITIONS")) {
            count++;
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
            
            if (count > buffer) {
                log.info("????????????????????????, ????????????????????????SQL????????????");
                break;
            }
        }
        log.info("?????????????????????????????????????????????" + resource.file());
        
        ApJobCFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, ApJobCFormVO.class);

        vo.setFormWording(getMessage(FormEnum.JOB_AP_C.wording()));
        
        // ???????????????????????????
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, ApJobCFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setCreateTime(today);
            vo.setFormClass(FormEnum.JOB_AP_C.name());
            vo.setFormType(FormEnum.JOB_AP_C.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }

        BeanUtil.copyProperties(formInfo, vo);
        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        vo.setFormType(FormEnum.valueOf(vo.getFormClass()).formType());
        vo.setStatusWording(FormEnum.valueOf(vo.getFormStatus()).status());

        if (FormEnum.PROPOSING.name().equals(vo.getFormStatus())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(vo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), vo.getGroupName()));
            return;
        }
        
        resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());
        ApJobCFormVO levelInfo = jdbcRepository.queryForBean(resource, params, ApJobCFormVO.class);

        if (isClosed(vo.getFormId())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
            return;
        }

        // ?????????????????????
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateJobCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        
        String formStatusWording =
                StringConstant.SHORT_YES.equals(vo.getIsWorkLevel()) ?
                levelInfo.getWorkProjectName() : levelInfo.getGroupName();
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(formStatusWording));
        
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));

        // ?????????????????????????????????????????????
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        //?????????????????????
        vo.setIsCloseForm(verifyIsFormClose(vo));
        //????????????????????????????????????
        vo.setIsOwner(isOwner(vo));
        //??????????????????
        vo.setIsApprover(isApprover(vo));
        //?????????????????????????????????
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));

        log.info("========== ??????????????????????????? ==========");
        log.info("???????????? : " + vo.getDivisionSolving());
        log.info("???????????? : " + vo.getGroupSolving());
        log.info("???????????? : " + vo.getUserSolving());
        log.info("???????????? : " + vo.getFormStatus());
        log.info("???????????? : " + vo.getFormStatusWording());
        log.info("========== ??????????????????????????? ==========");
    }

    @ModLog
    @Override
    public void sendToVerification(ApJobCFormVO vo) {
        // ????????????
        Date today = new Date();
        vo.setAssignTime(today);
        vo.setCreateTime(today);
        vo.setFormStatus(FormEnum.ASSIGNING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());
        
        // ????????????2????????????????????????????????????
        ResourceEnum resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
        Map<String, Object> params = new HashMap<>();
        params.put("detailId", vo.getDetailId());
        params.put("processOrder", 2);
        Map<String, Object> pojo = jdbcRepository.queryForMap(resource, params);
        vo.setGroupSolving(MapUtils.getString(pojo, "GroupId"));

        log.info("========== ???????????????????????????????????????????????????ASSIGNING ==========");
        log.info("???????????? : " + vo.getFormStatus());
        log.info("========== ???????????????????????????????????????????????????ASSIGNING ==========");
        
        mergeFormInfo(vo);

        // ?????????????????????
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            SysUserVO loginUser = fetchLoginUser();
            String[] groupNameAry = StringUtils.split(loginUser.getGroupName(), "_");
            String levelStr = ObjectUtils.defaultIfNull(groupNameAry[1], groupNameAry[0]).concat("??????");
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setCompleteTime(today);
            saveLevel(vo, levelStr, FormEnum.APPLY, FormEnum.SENT);
            // ??????????????????
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
    public void prepareVerifying(ApJobCFormVO vo) {
        prepareVerifyWording(vo);
    }

    @Override
    public void verifying(ApJobCFormVO vo) {
        updateCurrentLevel(vo);
        
        // ??????????????????
        if (FormEnum.ASSIGNING.equals(FormEnum.valueOf(vo.getFormStatus()))) {
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            vo.setFormStatus(FormEnum.APPROVING.name());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
        }
        
        // ????????????
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
        
        // ????????????????????????
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
        
        if (StringUtils.equals(FormEnum.DISAGREED.name(), vo.getVerifyResult())) {
        	internalProcessRepo.updateIsProcessDoneByFormId(vo.getFormId(), StringConstant.SHORT_NO);
        }
    }
    
    @Override
    public void createVerifyCommentByVice(ApJobCFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // ???????????????????????????????????????
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm(ApJobCFormVO vo, FormVerifyType type) {
        return isStretchFormClosed(vo);
    }

    @Override
    public void deleteForm(ApJobCFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo(ApJobCFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// ??????
            BeanUtil.copyProperties(
                    newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass())), vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.JOB_C));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormJobInfoDateEntity formDatePojo = new FormJobInfoDateEntity();
            FormJobInfoApDetailEntity formDetailPojo = new FormJobInfoApDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formRepo.save(formPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
            
            String sourceId = formPojo.getSourceId();
            FormInternalProcessStatusEntity e;
            List<FormInternalProcessStatusEntity> insertEntities = new ArrayList<>();
            List<FormInternalProcessStatusEntity> internalProcessEntities = internalProcessRepo.findByFormId(sourceId);
            for (FormInternalProcessStatusEntity entity : internalProcessEntities) {
            	e = new FormInternalProcessStatusEntity();
            	BeanUtil.copyProperties(entity, e, "id");
            	e.setFormId(vo.getFormId());
                e.setCreatedBy(userId);
                e.setCreatedAt(today);
                e.setUpdatedBy(userId);
                e.setUpdatedAt(today);
            	insertEntities.add(e);
            }
            internalProcessRepo.saveAll(insertEntities);
        } else {
        	boolean isVice = sysUserService.isVice(super.fetchLoginUser().getGroupId());
        	boolean isAdmin = sysUserService.isAdmin();
        	boolean isKeepUserSolving = !isAdmin && !isVice;
        	if (isKeepUserSolving) {
        		FormEntity form = formRepo.findByFormId(formId);
        		vo.setUserSolving(form.getUserSolving());
        	}
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("UPDATE_JOB_AP_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
            
            if (isVice || isAdmin) {
            	boolean result = mergeFormInternalProcessStatus(formId, vo.getInternalProcessItems());
            	if (isVice && result) {
            		saveLog(vo);
            	}
            }
        }
    }

    @Override
    public boolean isVerifyAcceptable(ApJobCFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed(ApJobCFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist(ApJobCFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (ApJobCFormVO vo) {
        asyncMailLauncher(vo);
    }

    /**
     * ???????????????????????????
     * 
     * @param vo
     */
    @Override
    public void saveProgram(ApJobCFormVO vo) {
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
    }

    /**
     * ???????????????????????????
     * 
     * @param formId
     */
    @Override
    public ApJobCFormVO getProgram(String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        ApJobCFormVO vo = new ApJobCFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }

    @Override
    public void immediateClose (ApJobCFormVO vo) {
        closeFormForImmdiation(vo);
    }

    @Override
    public ApJobCFormVO newerProcessDetail(String division, FormEnum e) {
        return BeanUtil.fromJson(
            getRecentlyDetail(division, e), ApJobCFormVO.class);
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
    public ApJobCFormVO getFormDetailInfo(String formId) {
        ApJobCFormVO vo = new ApJobCFormVO();
        FormJobInfoApDetailEntity entity = formDetailRepo.findByFormId(formId);
        BeanUtil.copyProperties(entity, vo, new String[] {"updatedAt"});
        // ???AP????????????????????????????????????"????????????"?????????????????????mail ?????????????????????
        vo.setSummary(entity.getPurpose());
        
        return vo;
    }

    @Override
    public boolean isAllStretchDied (ApJobCFormVO vo) {
        return super.isAllStretchDied(vo);
    }

    @Override
    public void doInternalProcess(ApJobCFormVO vo) {
    	String sendUserId = vo.getUserSolving();
    	LdapUserEntity loginUser = sysUserService.findUserByUserId(UserInfoUtil.loginUserId());
    	LdapUserEntity sendUser = sysUserService.findUserByUserId(sendUserId);
    	SysGroupEntity sysGroup = sysGroupRepository.findBySysGroupId(Long.parseLong(sendUser.getSysGroupId()));
    	String sendGroupId = sysGroup.getGroupId();

    	internalProcessRepo.updateIsProcessDoneByFormIdAndDivision(vo.getFormId(), StringConstant.SHORT_YES, vo.getCurrentProcess());
    	
    	Date today = new Date();
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        BeanUtil.copyProperties(vo, form);
        formRepo.save(form);
    	
        // Update
        List<FormJobDivisionMappingEntity> entities = jobDivisionMappingRepo.findByDivision(vo.getCurrentProcess());
        String processName = CollectionUtils.isNotEmpty(entities) ? entities.get(0).getJobTabName() : vo.getCurrentProcess();
        FormVerifyLogEntity back = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        if (back != null) {
        	String comment = getMessage("form.question.form.info.internalprocess.message", new String[] {loginUser.getName(), processName, sendUser.getName()});
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
    public void finishedInternalProcess(ApJobCFormVO vo) {
    	internalProcessRepo.updateIsProcessDoneByFormIdAndDivision(vo.getFormId(), StringConstant.SHORT_YES, vo.getCurrentProcess());
    }
    
	@Override
    public void sendSplitProcess(ApJobCFormVO vo) {
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
    protected String getFormApplyGroupInfo(ApJobCFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailApplyJobEntity FormProcessDetailApplyJobEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));
        return FormProcessDetailApplyJobEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailApplyJobEntity FormProcessDetailApplyJobEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return FormProcessDetailApplyJobEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(ApJobCFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailReviewJobEntity FormProcessDetailReviewJobEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));
        return FormProcessDetailReviewJobEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailReviewJobEntity FormProcessDetailReviewJobEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return FormProcessDetailReviewJobEntity.getGroupId();
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????????????????
     */
    private void applyVerification(ApJobCFormVO vo, List<FormProcessDetailApplyJobEntity> jumpList) {
        // ??????????????????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(jumpList)) {
            vo.setUserId(null);
            vo.setVerifyLevel("1");
            vo.setVerifyType(FormEnum.REVIEW.name());
            vo.setGroupId(MapUtils.getString(getProcessLevel(
                    vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
            saveLevel(vo, 1, FormEnum.REVIEW, FormEnum.PENDING);// ???????????????????????????
            
            return;
        }
        
        if (!vo.getIsNextLevel()) {
            Collections.reverse(jumpList);
        }
        
        // ?????????????????????
        int size = jumpList.size();
        FormEnum verifyResult = null;
        FormProcessDetailApplyJobEntity nextLevel = jumpList.get(size-1);
        boolean isWorkLevel = StringConstant.SHORT_YES.equals(nextLevel.getIsWorkLevel());
        FormJobCheckPersonListEntity checkPerson = personRepo
                .findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

        vo.setCompleteTime(null);
        vo.setUserId(isWorkLevel ? checkPerson.getUserId() : null);
        if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
            verifyResult = nextLevel.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
            vo.setGroupId(nextLevel.getGroupId());
            
            if (sysUserService.isPic(nextLevel.getGroupId()) && StringUtils.isEmpty(vo.getUserId())) {
                vo.setUserId(formRepo.findByFormId(vo.getFormId()).getUserSolving());
            }
            
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.APPLY, verifyResult);
        } else {// ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            vo.setGroupId(back.getGroupId());
            verifyResult = FormEnum.PENDING;
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.APPLY, verifyResult);
            vo.setUserId(back.getUserId());// ????????????????????????????????????( ?????????????????????????????? )
        }
        
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????
     */
    private void reviewVerification(ApJobCFormVO vo, List<FormProcessDetailReviewJobEntity> jumpList) {
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
        
        // ?????????????????????
        int size = jumpList.size();
        FormProcessDetailReviewJobEntity nextLevel = jumpList.get(size-1);
        boolean isWorkLevel = StringConstant.SHORT_YES.equals(nextLevel.getIsWorkLevel());
        FormJobCheckPersonListEntity checkPerson = personRepo
                .findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

        vo.setCompleteTime(null);
        vo.setUserId(isWorkLevel ? checkPerson.getUserId() : null);
        if (vo.getIsNextLevel()) {
            vo.setGroupId(nextLevel.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {// ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            vo.setUserId(back.getUserId());// ????????????????????????????????????( ?????????????????????????????? )
        }
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }
    
}
