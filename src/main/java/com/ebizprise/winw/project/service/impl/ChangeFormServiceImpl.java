package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
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
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.entity.FormImpactAnalysisEntity;
import com.ebizprise.winw.project.entity.FormInfoCDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoChgDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInfoIncDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoQDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoSrDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoUserEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyChgEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewChgEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormImpactAnalysisRepository;
import com.ebizprise.winw.project.repository.IFormInfoCDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoChgDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoQDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoSrDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoUserRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyChgRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewChgRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.ChangeFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * ????????? ????????????
 * 
 * The <code>QuestionFormServiceImpl</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019???7???16???
 */
@Transactional
@Service("changeFormService")
public class ChangeFormServiceImpl extends BaseFormService<ChangeFormVO> implements IBaseFormService<ChangeFormVO>{
    
    @Autowired
    private ICommonFormService commonFormService;
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoUserRepository formUserRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoChgDetailRepository formDetailRepo;
    @Autowired
    private IFormInfoSrDetailRepository srFormDetailRepo;
    @Autowired
    private IFormInfoQDetailRepository qFormDetailRepo;
    @Autowired
    private IFormInfoIncDetailRepository incFormDetailRepo;
    @Autowired
    private IFormInfoCDetailRepository cFormDetailRepo;
    @Autowired
    private IFormProcessDetailApplyChgRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewChgRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private IFormImpactAnalysisRepository impactRepo;
    @Autowired
    private ISysParameterRepository sysParameterRepo;
    @Autowired
    private IFormFileRepository formFileRepository;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public void updateVerifyLog (ChangeFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    /**
     * ??????????????????????????????????????????
     * @param vo
     * @author adam.yeh
     */
    public void isImpactFinished (ChangeFormVO vo) {
        String score, qType;
        List<FormImpactAnalysisEntity> impactList = impactRepo.findByFormId(vo.getFormId());
        
        for (FormImpactAnalysisEntity impact : impactList) {
            qType = impact.getQuestionType();
            score = impact.getTargetFraction();
            
            if (isNotFraction(qType)) {
                continue;
            }
            
            if (isFractionEmpty(score)) {
                vo.setTotalFraction("0");
                break;
            }
        }
    }

    /**
     * ?????????????????????????????????
     * @param vo
     * @author adam.yeh
     */
    @Override
    public void jumpToReview (ChangeFormVO vo) {
        super.jumpToReview(vo);
    }
    
    /**
     * ???????????????????????????
     * @param sourceId
     * @return
     * @author adam.yeh
     */
    public boolean isMainForms (String sourceId) {
        if (StringUtils.isBlank(sourceId)) {
            return false;
        }
        
        FormEntity e = formRepo.findByFormId(sourceId);
        FormEnum formClass = FormEnum.valueOf(e.getFormClass());
        return (FormEnum.SR == formClass ||
                FormEnum.Q == formClass ||
                FormEnum.INC == formClass);
    }
    
    @Override
    public void create (ChangeFormVO vo) {
        String formId = vo.getFormId();
        SysUserVO loginUser = fetchLoginUser();
        FormEntity source = formRepo.findByFormId(vo.getSourceId());
        FormInfoDateEntity dateEntity = formDateRepo.findByFormId(vo.getFormId());
        
        if (source != null) { //???????????????????????????????????????
            //??????????????????????????????APPLY_??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            String sourceFormId = source.getFormId().split("-")[0];
            if (FormEnum.BA.name().equals(sourceFormId) || FormEnum.CHG.name().equals(sourceFormId) || FormEnum.INC.name().equals(sourceFormId) || 
                    FormEnum.SR.name().equals(sourceFormId) || FormEnum.Q.name().equals(sourceFormId)) {
                FormInfoUserEntity detail = formUserRepo.findByFormId(source.getFormId());
                Date mect = new Date(dateEntity.getMect().getTime());
                vo.setUnitId(detail.getUnitId());
                vo.setUserName(detail.getUserName());
                vo.setUnitCategory(detail.getUnitCategory());
                vo.setEct(mect);
            }
        }
        
        FormProgramEntity e = programRepo.findByFormId(formId);
        if (e == null) {
            vo.setContent(null);
        } else {
            vo.setContent(e.getProcessProgram());
        }
        
        vo.setSourceId(formId);
        vo.setUserSolving(loginUser.getUserId());
        vo.setUserCreated(loginUser.getUserId());
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setCct(vo.getEct());

        vo.setFormId(null);
        vo.setFormStatus(null);
        vo.setEot(null);
        vo.setAst(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setVerifyType(null);
        vo.setVerifyLevel(null);
        vo.setCreateTime(null);
        vo.setGroupSolving(null);
        vo.setFormStatusWording(null);
    }

    @Override
    @ModLog
    public void getFormInfo(ChangeFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());

        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        ChangeFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, ChangeFormVO.class);
        vo.setFormWording(getMessage(FormEnum.CHG.wording()));

        
        // ???????????????????????????
        if (formInfo == null) {
            //????????????,?????????????????????????????????
            getFormInfoBySourceId(vo);
            
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, ChangeFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setFormClass(FormEnum.CHG.name());
            vo.setFormType(FormEnum.CHG.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            vo.setIsFromCountersign(commonFormService.isCounterSign(vo.getFormId()));
            
            return;
        }
        
        //??????formId,?????????ID,???????????????
        String formId = vo.getFormId();
        if(StringUtils.isNotBlank(formId)) {
            FormInfoChgDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
            FormInfoUserEntity userEntity = formUserRepo.findByFormId(formId);
            FormInfoDateEntity dateEntity = formDateRepo.findByFormId(formId);
            List<FormImpactAnalysisEntity> formImpactAnalysisEntityLs = impactRepo.findByFormId(formId);
            
            BeanUtil.copyProperties(detailEntity, formInfo);
            BeanUtil.copyProperties(userEntity, formInfo);
            BeanUtil.copyProperties(dateEntity, formInfo);
            BeanUtil.copyProperties(formInfo, vo);
            
            //????????????????????????
            int totalFraction = 0;
            for(FormImpactAnalysisEntity eneity : formImpactAnalysisEntityLs) {
                if(StringUtils.isNumeric(eneity.getTargetFraction())) {
                    //????????????,?????????
                    if(!"T".equals(eneity.getQuestionType())) {
                        totalFraction += Integer.parseInt(eneity.getTargetFraction());
                    }
                    
                }
            }
            vo.setTotalFraction(String.valueOf(totalFraction));
        }

        // ???????????????????????????????????????
        vo.setFormType(FormEnum.valueOf(vo.getFormClass()).formType());
        vo.setStatusWording(FormEnum.valueOf(vo.getFormStatus()).status());
        SysParameterEntity impact = sysParameterRepo.findByParamKey(SysParametersEnum.IMPACT_THRESHOLD.name());
        vo.setImpactThreshold(impact == null ? "" : impact.getParamValue());

        if (FormEnum.PROPOSING.name().equals(vo.getFormStatus())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(vo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), vo.getGroupName()));
            vo.setIsFromCountersign(commonFormService.isCounterSign(vo.getSourceId()));
            
            return;
        }

        resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());
        ChangeFormVO levelInfo = jdbcRepository.queryForBean(resource, params, ChangeFormVO.class);

        if (isClosed(formInfo.getFormId())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
            return;
        }

        // ?????????????????????
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));// ?????????????????????????????????????????????
        vo.setIsCloseForm(verifyIsFormClose(vo));//??????????????????
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));//?????????????????????????????????
        vo.setIsCreateJobIssue(isCreateJobIssue(vo));
        vo.setIsFromCountersign(commonFormService.isCounterSign(vo.getSourceId()));
        //??????????????????
        vo.setIsApprover(isApprover(vo));
    }

    @Override
    public void mergeFormInfo(ChangeFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(userId);
        
        if (StringUtils.isEmpty(formId)) {// ??????
            BaseFormVO newerDetail = newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass()));
            BeanUtil.copyProperties(newerDetail, vo);
            vo.setCat(today);
            vo.setCreatedAt(today);
            vo.setCreateTime(today);
            vo.setCreatedBy(userId);
            vo.setIsServerSideUpdated(true);
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.CHG));
            
            FormEntity formPojo = new FormEntity();
            FormInfoUserEntity formUserPojo = new FormInfoUserEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoChgDetailEntity formDetailPojo = new FormInfoChgDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formUserPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formRepo.save(formPojo);
            formUserRepo.save(formUserPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
            
            //????????????????????????isScopeChanged?????????N
            formDetailRepo.updateIsScopeChangedByFormId(StringConstant.SHORT_NO, vo.getFormId());
        } else {
            updateChangeForm(vo, vo.getUpdatedAt());
        }
    }

    @Override
    public void sendToVerification(ChangeFormVO vo) {
        Date today = new Date();
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        form.setCreateTime(today);
        formRepo.save(form);
        
        // ?????????????????????
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(new Date());
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }

        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyChgEntity> applyList =
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel);

        // ??????????????????????????????, ????????????????????????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(applyList)) {
            vo.setUserId(null);
            vo.setCompleteTime(null);
        }
        
        applyVerification(vo, applyList);
    }

    @Override
    public void prepareVerifying(ChangeFormVO vo) {
        prepareVerifyWording(vo);
    }
    
    @Override
    public void verifying(ChangeFormVO vo) {
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
                List<FormProcessDetailApplyChgEntity> applyList =
                    applyRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                applyVerification(vo, applyList);
                break;

            case REVIEW:
                List<FormProcessDetailReviewChgEntity> reviewList =
                    reviewRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                reviewVerification(vo, reviewList);
                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(ChangeFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // ???????????????????????????????????????
            updateCurrentLevel(vo);
        }
    }

    @Override
    public void deleteForm(ChangeFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public boolean isVerifyAcceptable(ChangeFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed(ChangeFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (ChangeFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (ChangeFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public ChangeFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), ChangeFormVO.class);
    }
    
    @Override
    public boolean verifyStretchForm(ChangeFormVO vo, FormVerifyType type) {
        return isStretchFormClosed(vo);
    }

    @Override
    public ChangeFormVO getFormDetailInfo(String formId) {
        ChangeFormVO vo = new ChangeFormVO();
        BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), vo, new String[] {"updatedAt"});
        return vo;
    }

    @Override
    public ChangeFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        ChangeFormVO vo = new ChangeFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }
    
    @Override
    public void saveProgram (ChangeFormVO vo) {
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
    
    @Override
    public void immediateClose(ChangeFormVO vo) {
         closeFormForImmdiation(vo);
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
    public boolean isAllStretchDied (ChangeFormVO vo) {
        return super.isAllStretchDied(vo);
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
    protected String getFormApplyGroupInfo(ChangeFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailApplyChgEntity formProcessDetailApplyChgEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplyChgEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailApplyChgEntity formProcessDetailApplyChgEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplyChgEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(ChangeFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailReviewChgEntity formProcessDetailReviewChgEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailReviewChgEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailReviewChgEntity formProcessDetailReviewChgEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewChgEntity.getGroupId();
    }
    
    @Override
    public String checkAttachmentExists(ChangeFormVO vo) {
        List<FormFileEntity> fileDetailLs = new ArrayList<>();
        String targetFileName1 = "";
        String targetFileName2 = "";
        String errMsgI18n = "";
        String isNewSystem = vo.getIsNewSystem();//"Y"
        String isNewService = vo.getIsNewService();// ????????????????????????:Y/N
        int totalFraction = Integer.valueOf(vo.getTotalFraction()); // ???????????? ??????
        String impactThreshold = sysParameterRepo.findByParamKey(SysParametersEnum.IMPACT_THRESHOLD.name()).getParamValue();
        StringBuffer errorMsg = new StringBuffer();
        
        // 0007089: ???008???ISO_7_???????????????????????????
        // case1. ????????? ?????????
        // ??????????????????: ?????????????????????????????????????????????
        if(StringConstant.SHORT_YES.equalsIgnoreCase(isNewSystem)) {
            errMsgI18n = "form.change.verify.attachment.file.name.error.system";
            fileDetailLs = formFileRepository.findByFormIdOrderByCreatedAtDesc(vo.getFormId());

            SysParameterEntity entity1 = sysParameterRepo.findByParamKey(SysParametersEnum.CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_FEASIBIILITY_ANALYSIS_REPORT.name());
            SysParameterEntity entity2 = sysParameterRepo.findByParamKey(SysParametersEnum.CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_PUBLISH_ACCEPT_RULE.name());
            targetFileName1 = entity1.getParamValue();
            targetFileName2 = entity2.getParamValue();
            
            checkAttachmentFile(targetFileName1, targetFileName2, fileDetailLs, errMsgI18n, errorMsg);
        }
        
        // case2. ????????? ????????????????????????
        // ??????????????????: ??????????????????????????????????????????????????????????????????
        if (StringConstant.SHORT_YES.equalsIgnoreCase(isNewService)) {
            errMsgI18n = "form.change.verify.attachment.file.name.error.service";
            if (CollectionUtils.isEmpty(fileDetailLs)) {
                fileDetailLs = formFileRepository.findByFormIdOrderByCreatedAtDesc(vo.getFormId());
            }

            SysParameterEntity entity1 = sysParameterRepo.findByParamKey(SysParametersEnum.CHANGE_FORM_ATTACHMENT_NAME_SERVICE_FEASIBIILITY_ANALYSIS_REPORT.name());
            SysParameterEntity entity2 = sysParameterRepo.findByParamKey(SysParametersEnum.CHANGE_FORM_ATTACHMENT_NAME_SERVICE_PLANNING_REPORT.name());
            targetFileName1 = entity1.getParamValue();
            targetFileName2 = entity2.getParamValue();

            checkAttachmentFile(targetFileName1, targetFileName2, fileDetailLs, errMsgI18n, errorMsg);
        }

        // case3. ???????????????????????????????????? ???????????????????????????????????????>=1000???????????????
        // ??????????????????: ?????????????????????
        if (StringConstant.SHORT_NO.equalsIgnoreCase(isNewSystem)
                && StringConstant.SHORT_NO.equalsIgnoreCase(isNewService)
                && totalFraction >= Integer.valueOf(impactThreshold)) {
            
            if (CollectionUtils.isEmpty(fileDetailLs)) {
                fileDetailLs = formFileRepository.findByFormIdOrderByCreatedAtDesc(vo.getFormId());
            }

            SysParameterEntity entity = sysParameterRepo.findByParamKey(SysParametersEnum.CHANGE_FORM_ATTACHMENT_NAME_OVER_IMPACT_THRESHOLD.name());
            targetFileName1 = entity.getParamValue();
            boolean targetFileExsits = false;

            for (FormFileEntity target : fileDetailLs) {
                if ((target.getName().contains(targetFileName1))) {
                    targetFileExsits = true;
                    break;
                }
            }

            if (!targetFileExsits) {
                String msg = getMessage("form.change.verify.attachment.file.name.error.impact", targetFileName1);
                errorMsg.append(msg).append("\r\n");
            }
        }

        return errorMsg.toString();
    }

    private boolean isFractionEmpty (String score) {
        return StringUtils.isBlank(score) || Integer.valueOf(score) == 0;
    }

    private boolean isNotFraction (String qType) {
        boolean isSolution = FormEnum.S.name().equals(qType);
        boolean isEvaluation = FormEnum.E.name().equals(qType);
        boolean isTotalFraction = FormEnum.T.name().equals(qType);
        
        return (isSolution || isEvaluation || isTotalFraction);
    }
    
    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????????????????
     */
    private void applyVerification (ChangeFormVO vo, List<FormProcessDetailApplyChgEntity> jumpList) {
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
        FormProcessDetailApplyChgEntity applyPojo = jumpList.get(size-1);

        vo.setCompleteTime(null);
        if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
            verifyResult = applyPojo.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
            vo.setUserId(null);
            vo.setGroupId(applyPojo.getGroupId());

            if (sysUserService.isPic(applyPojo.getGroupId()) && StringUtils.isEmpty(vo.getUserId())) {
                vo.setUserId(formRepo.findByFormId(vo.getFormId()).getUserSolving());
            }
            
            saveLevel(vo, applyPojo.getProcessOrder(), FormEnum.APPLY, verifyResult);
        } else {
            // ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // ????????????
            verifyResult = FormEnum.PENDING;
            vo.setUserId(null); // ????????????
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, applyPojo.getProcessOrder(), FormEnum.APPLY, verifyResult);
            // ????????????????????????????????????( ?????????????????????????????? )
            String userId = "";
            
            if(!Objects.isNull(back)) {
                userId = back.getUserId();
            } else {
                FormEntity formEntity =  formRepo.findByFormId(vo.getFormId());
                if(!Objects.isNull(formEntity)) {
                    userId = formEntity.getCreatedBy();
                } else {
                    userId = fetchLoginUser().getUserId();
                }
            }
            
            vo.setUserId(userId);
        }
        
    }
    
    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????
     */
    private void reviewVerification (ChangeFormVO vo, List<FormProcessDetailReviewChgEntity> jumpList) {
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
        FormProcessDetailReviewChgEntity reviewPojo = jumpList.get(size-1);

        vo.setCompleteTime(null);
        if (vo.getIsNextLevel()) {
            vo.setUserId(null);
            vo.setGroupId(reviewPojo.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {
            // ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo.
                    findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(
                            vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // ????????????
            vo.setUserId(null); // ????????????
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            
            // ??????????????????,???userId??????vo??????,????????????????????????
            setBackLevelMailUserId(vo,back);
        }
        
    }
    
    /**
     * ??????SourceId ??????????????????
     * 
     * @param vo
     * @throws Exception
     */
    private void getFormInfoBySourceId(ChangeFormVO vo) {
        //?????????SourceId ???????????????????????????
        FormEntity formEntity = formRepo.findByFormId(vo.getSourceId());
        
        String sourceId = formEntity.getFormId();
        String formClass = formEntity.getFormClass();
         
         switch (FormEnum.valueOf(formClass)) {
            case SR:
                if(StringUtils.isNoneBlank(sourceId)) {
                    FormInfoSrDetailEntity detailEntity = srFormDetailRepo.findByFormId(sourceId);
                    BeanUtil.copyProperties(detailEntity, vo);
                }
                break;
                
            case Q:
                if(StringUtils.isNoneBlank(sourceId)) {
                    FormInfoQDetailEntity detailEntity = qFormDetailRepo.findByFormId(sourceId);
                    BeanUtil.copyProperties(detailEntity, vo);
                    
                }
                break;
                
            case INC:
                if(StringUtils.isNoneBlank(sourceId)) {
                    FormInfoIncDetailEntity detailEntity = incFormDetailRepo.findByFormId(sourceId);
                    BeanUtil.copyProperties(detailEntity, vo);
                    
                }
                break;
            
            case SR_C:
            case Q_C:
            case INC_C:
            case JOB_AP_C:
            case JOB_SP_C:
                if(StringUtils.isNoneBlank(sourceId)) {
                    FormInfoCDetailEntity detailEntity = cFormDetailRepo.findByFormId(sourceId);
                    BeanUtil.copyProperties(detailEntity, vo);
                }
                break;

            default:
                break;
             }

         if(StringUtils.isNoneBlank(sourceId)) {
             FormInfoUserEntity userEntity = formUserRepo.findByFormId(sourceId);
             BeanUtil.copyProperties(userEntity, vo);
         }
         
        //????????????ID,??????????????????????????????
        vo.setFormId("");
        vo.setSummary("");
        vo.setContent("");
    }
    
    /**
     * ????????????????????????,????????????????????????
     * 
     * @param vo
     * @param updateTime ?????????Update??????
     */
    private void updateChangeForm(ChangeFormVO vo,Date updateTime) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_CHG.getResource("UPDATE_CHG_FORM");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("detailId", vo.getDetailId());
        params.put("formClass", vo.getFormClass());
        params.put("formStatus", vo.getFormStatus());
        params.put("processStatus", vo.getProcessStatus());
        params.put("divisionCreated", vo.getDivisionCreated());
        params.put("userCreated", vo.getUserCreated());
        params.put("divisionSolving", vo.getDivisionSolving());
        params.put("userSolving", vo.getUserSolving());
        params.put("groupSolving", vo.getGroupSolving());
        params.put("cat", vo.getCat());
        params.put("cct", vo.getCct());
        params.put("unitId", vo.getUnitId());
        params.put("userName", vo.getUserName());
        params.put("phone", vo.getPhone());
        params.put("email", vo.getEmail());
        params.put("unitCategory", vo.getUnitCategory());
        params.put("summary", vo.getSummary());
        params.put("content", vo.getContent());
        params.put("effectSystem", vo.getEffectSystem());
        params.put("cCategory", vo.getcCategory());
        params.put("cClass", vo.getcClass());
        params.put("cComponent", vo.getcComponent());
        params.put("changeType", vo.getChangeType());
        params.put("changeRank", vo.getChangeRank());
        params.put("standard", vo.getStandard());
        params.put("isNewSystem", vo.getIsNewSystem());
        params.put("isNewService", vo.getIsNewService());
        params.put("isUrgent", vo.getIsUrgent());
        params.put("isEffectField", vo.getIsEffectField());
        params.put("isEffectAccountant", vo.getIsEffectAccountant());
        params.put("isModifyProgram", vo.getIsModifyProgram());
        params.put("updatedBy", vo.getUpdatedBy());
        params.put("updatedAt", updateTime);
        
        jdbcRepository.update(resource, params);
    }
    
    private String isCreateJobIssue (ChangeFormVO vo) {
        boolean isCreating = StringConstant.SHORT_YES.equals(vo.getIsCreateJobIssue());
        boolean isMatchDivision = fetchLoginUser().getDivisionSolving().equals(vo.getDivisionSolving());
        return (isCreating && isMatchDivision) ? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    private void checkAttachmentFile(String tFileName1, String tFileName2, List<FormFileEntity> fileDetailLs,
            String errMsgI18n, StringBuffer errorMsg) {

        List<String> targetFileList = new ArrayList<>();
        for (FormFileEntity target : fileDetailLs) {
            String fileName = target.getName();
            if (fileName.contains(tFileName1)) {
                targetFileList.add(tFileName1);
            } else if (fileName.contains(tFileName2)) {
                targetFileList.add(tFileName2);
            }

            if (targetFileList.contains(tFileName1) && targetFileList.contains(tFileName2)) {
                break;
            }
        }

        if (!targetFileList.contains(tFileName1) || !targetFileList.contains(tFileName2)) {
            String msg = getMessage(errMsgI18n, new String[] { tFileName1, tFileName2 });
            errorMsg.append(msg).append("\r\n");
        }
    }

}
