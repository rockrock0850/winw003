package com.ebizprise.winw.project.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormInternalProcessStatusEntity;
import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;
import com.ebizprise.winw.project.entity.FormJobInfoApDetailEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobLibraryEntity;
import com.ebizprise.winw.project.entity.FormJobLibraryFileEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.enums.MailTemplate;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoQDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoSrDetailRepository;
import com.ebizprise.winw.project.repository.IFormInternalProcessStatusRepository;
import com.ebizprise.winw.project.repository.IFormJobCheckPersonRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoApDetailRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormJobLibraryFileRepository;
import com.ebizprise.winw.project.repository.IFormJobLibraryRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.ApJobFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * AP????????? ??????/??????/??????/???????????????
 * 
 * @author adam.yeh
 */
@Transactional
@Service("apJobFormService")
public class ApJobFormServiceImpl extends BaseFormService<ApJobFormVO> implements IBaseFormService<ApJobFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(ApJobFormServiceImpl.class);

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
    private IFormJobLibraryFileRepository fileRepo;
    @Autowired
    private IFormJobLibraryRepository libraryRepo;
    @Autowired
    private ILdapUserRepository userRepo;
    @Autowired
    private IFormInfoSrDetailRepository srFormDetailRepo;
    @Autowired
    private IFormInfoQDetailRepository qFormDetailRepo;
    @Autowired
    private IFormInfoIncDetailRepository incFormDetailRepo;
    @Autowired
    private IFormJobCheckPersonRepository personRepo;

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * ???????????????????????????????????????
     * @param formId
     * @return
     * @author adam.yeh
     * @param rowType
     */
    public List<ApJobFormVO> getVersionCodes (String formId, String rowType) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("FIND_VERSION_CODES");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", formId);
        params.put("rowType", rowType);
        List<Map<String, Object>> entityList = jdbcRepository.queryForList(resource, params);
        
        List<ApJobFormVO> voList = new ArrayList<>();
        
        for (Map<String, Object> entity : entityList) {
            ApJobFormVO vo = new ApJobFormVO();
            vo.setId(MapUtils.getLong(entity, "Id"));
            vo.setFn(MapUtils.getString(entity, "FormId"));
            vo.setTime((Date) MapUtils.getObject(entity, "Time"));
            vo.setMsg(MapUtils.getString(entity, "Result"));
            vo.setFileName(MapUtils.getString(entity, "Content"));
            vo.setBaseLine(MapUtils.getString(entity, "Content"));
            vo.setQyStatus(MapUtils.getString(entity, "QyStatus"));
            voList.add(vo);
        }
        
        return voList;
    }

    /**
     * ?????????????????????
     * @param vo
     * @return
     * @author adam.yeh
     * @throws IOException
     */
    public File download (ApJobFormVO vo) throws IOException {
        FormJobLibraryFileEntity fileEntity = fileRepo.findByIdAndFormId(vo.getId(), vo.getFormId());
        File file = new File(genFilePath(fileEntity.getName()));

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        file = FileUtil.getFileFromByte(fileEntity.getData(), file.getAbsolutePath());
        vo.setFileName(fileEntity.getName());

        return file;
    }

    /**
     * ??????????????????API??????????????????????????????????????????
     * @param vo
     * @author adam.yeh
     */
    public void saveCodeDiff (ApJobFormVO vo) {
        FormJobEnum type = FormJobEnum.valueOf(vo.getRowType());
        String content = FormJobEnum.LIBRARY == type ? vo.getFileName() : vo.getBaseLine();
                
        SysUserVO user = fetchLoginUser();
        FormJobLibraryEntity library = new FormJobLibraryEntity();
        library.setQyStatus(vo.getQyStatus());
        library.setContent(content);
        library.setRowType(vo.getRowType());
        library.setFormId(vo.getFormId());
        library.setTime(vo.getTime());
        library.setResult(vo.getMsg());
        library.setUpdatedAt(vo.getTime());
        library.setUpdatedBy(user.getUserId());
        library.setCreatedAt(vo.getTime());
        library.setCreatedBy(user.getUserId());
        libraryRepo.save(library);
    }

    /**
     * ??????????????????API????????????????????????????????????
     * @param vo
     * @throws IOException
     * @author adam.yeh
     */
    public void saveDiffFile (ApJobFormVO vo) throws IOException {
        SysUserVO user = fetchLoginUser();
        FormJobLibraryFileEntity file = new FormJobLibraryFileEntity();
        file.setFormId(vo.getFn());
        file.setTime(vo.getTime());
        file.setName(vo.getFileName());
        file.setData(vo.getData());
        file.setUpdatedAt(vo.getTime());
        file.setUpdatedBy(user.getUserId());
        file.setCreatedAt(vo.getTime());
        file.setCreatedBy(user.getUserId());
        fileRepo.save(file);
        vo.setId(file.getId());
    }

    @Override
    public void create (ApJobFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        SysUserVO loginUser = fetchLoginUser();
        
        //??????????????????,??????????????????
        getParentMainFormDetail(vo);
        
        //?????????????????????????????????????????????????????????AP?????????????????????????????????????????????????????????
        if (StringConstant.SHORT_NO.equals(vo.getIsModifyProgram())) {
            vo.setIsProgramOnline(StringConstant.SHORT_YES);
        }
        
        vo.setFormClass(FormEnum.JOB_AP.name());
        vo.setSourceId(formId);
        vo.setUserCreated(loginUser.getUserId());
        vo.setUserSolving(loginUser.getUserId());
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setProcessStatus(FormEnum.PROPOSING.name());
        vo.setStatusWording(FormEnum.PROPOSING.status());
        vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(loginUser.getGroupName()));
        vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), loginUser.getGroupName()));
        vo.setCreatedAt(today);
        vo.setCreatedBy(UserInfoUtil.loginUserId());
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(UserInfoUtil.loginUserId());
        vo.setPurpose(vo.getSummary());

        vo.setCountersigneds(null);
        vo.setFormId(null);
        vo.setFormStatus(null);
        vo.setEot(null);
        vo.setAst(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setFormId(null);
        vo.setCreateTime(null);
        vo.setVerifyType(null);
        vo.setVerifyLevel(null);
        vo.setAssignTime(null);
    }

    @ModLog
    @Override
    public void getFormInfo (ApJobFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        /*
         * ??????SQL???????????????race conditions???????????? 
         * ????????????SQL????????????????????????, 
         * ???????????????????????????????????????ResourceEnum.getResource()?????????, ????????????????????????SQL??????
         * FIXME ?????????????????????????????????SQL?????????
         */
        int count = 0;
        int buffer = 1000;
        logger.info("?????????????????????????????????????????????" + resource.file());
        while (!StringUtils.contains(resource.file(), "FIND_FORM_INFO_JOB_BY_CONDITIONS")) {
            count++;
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
            
            if (count > buffer) {
                logger.info("????????????????????????, ????????????????????????SQL????????????");
                break;
            }
        }
        logger.info("?????????????????????????????????????????????" + resource.file());
        
        ApJobFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, ApJobFormVO.class);

        vo.setFormWording(getMessage(FormEnum.JOB_AP.wording()));
        
        // ???????????????????????????
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, ApJobFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setCreateTime(today);
            vo.setFormClass(FormEnum.JOB_AP.name());
            vo.setFormType(FormEnum.JOB_AP.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }

        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        BeanUtil.copyProperties(formInfo, vo);
        vo.setUserId(userRepo.findByUserId(vo.getUserCreated()).getName());
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
        ApJobFormVO levelInfo = jdbcRepository.queryForBean(resource, params, ApJobFormVO.class);

        if (isClosed(vo.getFormId())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
            return;
        }

        // ?????????????????????
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateJobCIssue(isSubCreation(vo.getIsCreateJobCIssue(), levelInfo.getGroupId()));
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
        // ?????????????????????????????????????????????
        isWithoutWatching(vo);
    }

    @Override
    public void sendToVerification (ApJobFormVO vo) {
        // ?????????????????????
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(new Date());
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }
        
        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyJobEntity> applyList =
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel, FormJobEnum.CSPERSON.name());

        // ??????????????????????????????, ????????????????????????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(applyList)) {
            vo.setUserId(null);
            vo.setCompleteTime(null);
        }

        sendMailToCsPerson(vo);
        applyVerification(vo, applyList);
    }
    
    @Override
    public void prepareVerifying(ApJobFormVO vo) {
        prepareVerifyWording(vo);
    }

    @Override
    public void verifying (ApJobFormVO vo) {
        updateCurrentLevel(vo);
        
        // ????????????
        if (FormEnum.DEPRECATED.equals(
                FormEnum.valueOf(vo.getFormStatus()))) {
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
            
            return;
        }
        
        if (isBackToApplyProcess(vo)) {
            backToApply(vo);
        }
        
        if (isBackToApplyLevel1(vo)) {
            vo.setIsBackToApplyLevel1(true);
        }

        List<Integer> limits = calculateLimitNumber(vo);
        
        // ????????????????????????
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                /*
                 * ?????????PRO????????????????????????????????????????????????????????????????????????, 
                 * ??????????????????????????????????????????????????????????????????
                 * ???????????????????????????, SQL1???PRO???UAT????????????????????????SQL2?????????????????????
                 * ??????????????????????????????????????????, ?????????????????????SQL?????????????????????????????????????????????
                 * 1. ???SELECT * FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = '20201123141318707' AND (ProcessOrder > 0 AND ProcessOrder <= 1) AND WorkProject <> 'PPERSON'???
                 * 2. ???SELECT * FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = '20201123141318707' AND (ProcessOrder > 0 AND ProcessOrder <= 1) AND ISNULL(WorkProject, '') <> 'PPERSON'???
                 */
                ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("FIND_APPLY_LIMIT_LIST");
                
                Map<String, Object> params = new HashMap<>();
                params.put("detailId", vo.getDetailId());
                params.put("startLevel", limits.get(0));
                params.put("endedLevel", limits.get(1));
                params.put("workProject", FormJobEnum.CSPERSON.name());
                
                List<FormProcessDetailApplyJobEntity> applyList = jdbcRepository.queryForList(resource, params, FormProcessDetailApplyJobEntity.class);
                
                /*
                 * ?????????????????????????????????????????????, ??????????????????????????????(Apply 1)?????????
                 * ?????????applyList??????????????????????????????????????????????????????(?????????)
                 * P.S. SIT???UAT???????????????, ????????????Log???????????????????????????
                 */
                if (CollectionUtils.isNotEmpty(applyList)) {
                    logger.info("?????????????????????????????????====");
                    for (FormProcessDetailApplyJobEntity e : applyList) {
                        logger.info("???????????? : " + e.getGroupId());
                        logger.info("???????????? : " + e.getWorkProject());
                        logger.info("???????????? : " + e.getProcessOrder());
                    }
                    logger.info("?????????????????????????????????====");
                } else {
                    logger.info("????????????????????????========");
                }
                
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
    public void createVerifyCommentByVice(ApJobFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // ???????????????????????????????????????
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm (ApJobFormVO vo, FormVerifyType type) {
        boolean result = false;
        
        if (type == FormVerifyType.STRETCH_ZERO) {
            result = getStretchs(vo.getFormId()) > 0;
        } else if (type == FormVerifyType.STRETCH_FINISHED) {
            result = isStretchFormClosed(vo);
        }
        
        return result;
    }

    @Override
    public void deleteForm (ApJobFormVO vo) {
        deleteFormTables(vo.getFormId());
    }
    
    @Override
    public void updateVerifyLog (ApJobFormVO vo) {
        super.updateVerifyLog(vo);
    }

    @Override
    public void mergeFormInfo (ApJobFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// ??????
            BeanUtil.copyProperties(
                    newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass())), vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.JOB));
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
        } else {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("UPDATE_JOB_AP_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
        }
        mergeFormInternalProcessStatus(formId, vo.getCountersigneds());
    }

    @Override
    public boolean isVerifyAcceptable (ApJobFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed (ApJobFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (ApJobFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (ApJobFormVO vo) {
        asyncMailLauncher(vo);
    }

    /**
     * ???????????????????????????
     * 
     * @param vo
     */
    @Override
    public void saveProgram (ApJobFormVO vo) {
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
    public ApJobFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        ApJobFormVO vo = new ApJobFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }
    
    /**
     * ??????/?????? ??????????????????
     * 
     * @param vo
     */
    @Override
    public void immediateClose (ApJobFormVO vo) {
        if (vo.getIsWithoutWatching()) {
            closeFormForImmdiation(vo, FormEnum.AGREED);
        } else {
            closeFormForImmdiation(vo);
        }
    }

    @Override
    public ApJobFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), ApJobFormVO.class);
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
    public boolean isAllStretchDied (ApJobFormVO vo) {
        return super.isAllStretchDied(vo);
    }

    @Override
    public ApJobFormVO getFormDetailInfo(String formId) {
        ApJobFormVO vo = new ApJobFormVO();
        FormJobInfoApDetailEntity entity = formDetailRepo.findByFormId(formId);
        BeanUtil.copyProperties(entity, vo, new String[] {"updatedAt"});
        // ???AP??????????????????????????????"????????????"?????????????????????mail ?????????????????????
        vo.setSummary(entity.getPurpose());
        
        return vo;
    }
    
    /**
     * ??????FormId????????????????????????????????????
     * 
     * @param formId
     * @return List
     */
    @Override
    public List<String> getFormCountsignList(String formId) {
        FormJobInfoApDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
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
    protected String getFormApplyGroupInfo(ApJobFormVO vo) {
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
    protected String getFormReviewGroupInfo(ApJobFormVO vo) {
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

    @Override
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        if(FormEnum.APPLY.name().equalsIgnoreCase(verifyType)) {
            int count = applyRepo.countByDetailId(detailId);
            return (count == Integer.parseInt(verifyLevel))? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
        }
        
        return StringConstant.SHORT_NO;
    }

    /**
     * AP??????????????????????????????????????????????????????????????????
     * @param vo
     * @author adam.yeh
     */
    private void sendMailToCsPerson (ApJobFormVO vo) {
        FormJobCheckPersonListEntity checkPerson = personRepo.findByFormIdAndLevel(vo.getFormId(), FormJobEnum.CSPERSON.name());
        
        if (checkPerson != null && StringUtils.isNotBlank(checkPerson.getUserId())) {
            List<String> mailList = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            
            LdapUserEntity ldapUser = ldapUserRepository.findByUserId(checkPerson.getUserId());
            SysGroupEntity userGroup = sysGroupRepository.findBySysGroupId(Long.valueOf(ldapUser.getSysGroupId()));
            String formName = getMessage(FormEnum.JOB_AP.wording());
            String subject = getMessage("form.mail.handle.group.subject", new String[] {userGroup.getGroupName(), vo.getFormId(), formName});
            
            mailList.add(ldapUser.getEmail());
            params.put("formName", formName);
            params.put("group", userGroup.getGroupName());
            params.put("userCreated", vo.getUserCreated());
            params.put("template", MailTemplate.AGREED_PIC.src());
            params.put("verifyType", getMessage("report.operation.status.approving"));
            params.put("formClass", formName);                                                     // ????????????
            params.put("user", ldapUser.getName());                                                // ???????????????
            params.put("formId", vo.getFormId());                                                  // ????????????
            params.put("link", getFormUrl(vo.getFormId()));                                        // ???????????????
            params.put("divisionCreated", vo.getDivisionCreated());                                // ????????????
            params.put("summary", getFormDetailInfo(vo.getFormId()).getSummary());                 // ??????
            params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // ????????????
            params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // ????????????
            sendMail(subject, mailList, params);
        }
    }
    
    /**
     * ?????????????????????,???????????????????????????</br>
     * P.S.
     * ???????????????????????????????????????, ?????????????????????, ?????????????????????????????????</br>
     * ????????????, ??????????????????, ?????????????????????
     * @param vo
     */
    private ApJobFormVO isWithoutWatching (ApJobFormVO vo) {
        FormJobInfoApDetailEntity entiey = formDetailRepo.findByFormId(vo.getFormId());
        
        if ("REVIEW".equals(vo.getVerifyType()) &&
                StringConstant.SHORT_NO.equals(entiey.getIsWatching())) {
            int currentLevel = Integer.parseInt(vo.getVerifyLevel());
            List<FormProcessDetailReviewJobEntity> entityLs = reviewRepo.findByDetailId(vo.getDetailId());
            vo.setIsWithoutWatching((entityLs.size() - currentLevel) == 1);// ??????????????????1??????, ????????????????????????????????????????????????
        }
        
        return vo;
    }

    private String genFilePath (String name) {
        return env.getProperty("form.file.download.dir") + File.separatorChar
        + DateUtils.getCurrentDate(DateUtils._PATTERN_YYYYMMDD) + File.separatorChar + name;
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????????????????
     */
    private void applyVerification (ApJobFormVO vo, List<FormProcessDetailApplyJobEntity> jumpList) {        
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
        FormJobCheckPersonListEntity checkPerson = personRepo.
                findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

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
            verifyResult = FormEnum.PENDING;
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.APPLY, verifyResult);
            vo.setUserId(back.getUserId());// ????????????????????????????????????( ?????????????????????????????? )
        }
        
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????
     */
    private void reviewVerification (ApJobFormVO vo, List<FormProcessDetailReviewJobEntity> jumpList) {
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
        FormJobCheckPersonListEntity checkPerson = personRepo.
                findByFormIdAndSort(vo.getFormId(), String.valueOf(nextLevel.getProcessOrder()));

        vo.setCompleteTime(null);
        vo.setUserId(isWorkLevel ? checkPerson.getUserId() : null);
        if (vo.getIsNextLevel()) {
            vo.setGroupId(nextLevel.getGroupId());
            
            if (sysUserService.isPic(nextLevel.getGroupId()) && StringUtils.isEmpty(vo.getUserId())) {
                vo.setUserId(formRepo.findByFormId(vo.getFormId()).getUserSolving());
            }
            
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {// ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            vo.setUserId(back.getUserId());// ????????????????????????????????????( ?????????????????????????????? )
        }
    }
    
    /**
     * ???????????????????????????,??????????????????
     * 
     * @param formVO
     */
    private void getParentMainFormDetail(ApJobFormVO formVO) {
        List<BaseFormVO> parentFormLs = getSourceFormList(formVO.getSourceId());
        
        for(BaseFormVO target : parentFormLs) {
            String targetFormId = target.getFormId();
            //????????????????????????
            switch (FormEnum.valueOf(target.getFormClass())) {
                case SR:
                    BeanUtil.copyProperties(srFormDetailRepo.findByFormId(targetFormId), formVO);
                    break;
                case Q:
                    BeanUtil.copyProperties(qFormDetailRepo.findByFormId(targetFormId), formVO);
                    break;
                case INC:
                    BeanUtil.copyProperties(incFormDetailRepo.findByFormId(targetFormId), formVO);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }
    
    /**
     * ?????? AP?????????+SP????????????????????????=??????????????????????????????
     * @param vo
     * @return
     * @author jacky.fu
     */
    public List<ApJobFormVO> getJobForms (ApJobFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("FIND_JOB_FORMS_BY_CONDITION");
        Conditions conditions = new Conditions();
        // ????????????
        if (StringUtils.isNotBlank(vo.getFormId())) {
            conditions.and().like("f.formId", vo.getFormId());
        }
        // ????????????
        if (StringUtils.isNotBlank(vo.getDivisionSolving())) {
            conditions.and().like("f.DivisionSolving", vo.getDivisionSolving());
        }
        // ????????????
        if (StringUtils.isNotBlank(vo.getUserSolving())) {
            conditions.and().like("f.UserSolving", vo.getUserSolving());
        }
        // ????????????
        if (vo.getCreateTime() != null) {
            String dateStr = DateUtils.toString(vo.getCreateTime(), DateUtils._PATTERN_YYYYMMDD_SLASH);
            conditions.and().equal("CONVERT(date, f.CreateTime, 111)", dateStr);
        }
        // ????????????
        if (StringUtils.isNotBlank(vo.getPurpose())) {
            conditions.and().like("FIDTL.Purpose", vo.getPurpose());
        }
        List<ApJobFormVO> resultList = jdbcRepository.queryForList(resource, conditions, vo, ApJobFormVO.class);
        for(ApJobFormVO voo : resultList) {
            for(FormEnum fenum : FormEnum.values()) {
                if(fenum.toString().equals(voo.getFormStatus())) {
                    voo.setFormStatusWording(fenum.status());
                }
            }
        }
        
        return resultList;
    }
    
}
