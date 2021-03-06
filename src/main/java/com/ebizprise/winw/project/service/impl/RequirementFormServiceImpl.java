package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInfoSrDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoUserEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplySrEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewSrEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormInfoChgDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInfoSrDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoUserRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplySrRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewSrRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.RequirementFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * ????????? ??????/??????/??????/???????????????
 * 
 * @author adam.yeh
 */
@Transactional
@Service("requirementFormService")
public class RequirementFormServiceImpl extends BaseFormService<RequirementFormVO> implements IBaseFormService<RequirementFormVO> {

    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoUserRepository formUserRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoSrDetailRepository formDetailRepo;
    @Autowired
    private IFormProcessDetailApplySrRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewSrRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private IFormFileRepository formFileRepository;
    @Autowired
    private IFormInfoChgDetailRepository formChgDetailRepo;
    @Autowired
    private ISysParameterRepository sysParameterRepo;
    @Autowired
    private ICommonFormService commonFormService;
    
    @Override
    public void updateVerifyLog (RequirementFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void ectExtended (RequirementFormVO vo) {
        ectExtendedForStretchs(vo);
    }
    
    @ModLog
    @Override
    public void getFormInfo (RequirementFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        RequirementFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, RequirementFormVO.class);

        vo.setFormWording(getMessage(FormEnum.SR.wording()));
        
        // ???????????????????????????
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, RequirementFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setFormClass(FormEnum.SR.name());
            vo.setFormType(FormEnum.SR.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }
        
        BeanUtil.copyProperties(getProgram(vo.getFormId()),vo);
        BeanUtil.copyProperties(formInfo, vo);
        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        
        FormEnum formStatus = FormEnum.valueOf(vo.getFormStatus());
        FormEnum processStatus = FormEnum.valueOf(vo.getProcessStatus());
        
        vo.setFormType(formStatus.formType());
        vo.setStatusWording(processStatus.status());
        
        if (FormEnum.PROPOSING == formStatus) {
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(vo.getGroupName()));
            vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), vo.getGroupName()));
            return;
        }

        BaseFormVO levelInfo = getCurrentLevelInfo(vo);

        if (isClosed(vo.getFormId())) {
            vo.setFormStatusWording(formStatus.formStatus(levelInfo.getGroupName()));
            vo.setProcessStatusWording(String.format(processStatus.processStatus(), levelInfo.getGroupName()));
            return;
        }

        // ?????????????????????
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateChangeIssue(isSubCreation(vo.getIsCreateChangeIssue(), levelInfo.getGroupId()));
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        vo.setFormStatusWording(formStatus.formStatus(levelInfo.getGroupName()));
        vo.setProcessStatusWording(String.format(processStatus.processStatus(), levelInfo.getGroupName()));

        // ?????????????????????????????????????????????
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        //?????????????????????
        vo.setIsCloseForm(verifyIsFormClose(vo));
        //??????????????????
        vo.setIsApprover(isApprover(vo));
        //?????????????????????????????????
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
        vo.setIsEctExtended(StringConstant.SHORT_YES.equals(vo.getIsExtended()));

        setLastJobAct(vo);
    }

    @Override
    public void sendToVerification (RequirementFormVO vo) {
        Date today = new Date();
        FormEntity pojo = formRepo.findByFormId(vo.getFormId());
        pojo.setCreateTime(today);
        formRepo.save(pojo);
        
        // ?????????????????????
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(today);
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }
        
        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplySrEntity> applyList =
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel);

        // ??????????????????????????????, ????????????????????????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(applyList)) {
            vo.setUserId(null);
            vo.setCompleteTime(null);
        }
        
        applyVerification(vo, applyList);
        formDateRepo.updateOectByFormId(vo.getEct(), vo.getFormId());
    }
    
    @Override
    public void prepareVerifying(RequirementFormVO vo) {
        prepareVerifyWording(vo);
    }

    @Override
    public void verifying (RequirementFormVO vo) {
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
                List<FormProcessDetailApplySrEntity> applyList =
                        applyRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                applyVerification(vo, applyList);
                
                break;

            case REVIEW:
                List<FormProcessDetailReviewSrEntity> reviewList =
                        reviewRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                reviewVerification(vo, reviewList);
                
                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(RequirementFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // ???????????????????????????????????????
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm (
            RequirementFormVO vo, FormVerifyType type) {
        boolean result = false;
        
        if (type == FormVerifyType.STRETCH_ZERO) {
            result = getStretchs(vo.getFormId()) > 0;
        } else if (type == FormVerifyType.STRETCH_FINISHED) {
            result = isStretchFormClosed(vo);
        }
        
        return result;
    }

    @Override
    public void deleteForm (RequirementFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo (RequirementFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// ??????
            BaseFormVO newerDetail = newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass()));
            BeanUtil.copyProperties(newerDetail, vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.SR));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormInfoUserEntity formUserPojo = new FormInfoUserEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoSrDetailEntity formDetailPojo = new FormInfoSrDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formUserPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formRepo.save(formPojo);
            formUserRepo.save(formUserPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
        } else {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_SR.getResource("UPDATE_SR_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
            saveFormAlterResult(formId, false);
        }
    }

    @Override
    public boolean isVerifyAcceptable (RequirementFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed (RequirementFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (RequirementFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }
    
    @Override
    public void notifyProcessMail (RequirementFormVO vo) {
        asyncMailLauncher(vo);
    }

    /**
     * ???????????????????????????
     * 
     * @param vo
     */
    @Override
    public void saveProgram (RequirementFormVO vo) {
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

    /**
     * ???????????????????????????
     * 
     * @param formId
     */
    @Override
    public RequirementFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        RequirementFormVO vo = new RequirementFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }
    
    /**
     * ??????/?????? ??????????????????
     * 
     * @param vo
     */
    @Override
    public void immediateClose (RequirementFormVO vo) {
        closeFormForImmdiation(vo);
    }

    @Override
    public RequirementFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), RequirementFormVO.class);
    }

    @Override
    public RequirementFormVO getFormDetailInfo(String formId) {
        RequirementFormVO vo = new RequirementFormVO();
        BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), vo, new String[] {"updatedAt"});
        return vo;
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
    public boolean isAllStretchDied (RequirementFormVO vo) {
        return super.isAllStretchDied(vo);
    }
    
    @Override
    public List<String> getFormCountsignList(String formId) {
        FormInfoSrDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
        List<String> rtnLs = new ArrayList<>();
        
        if(!Objects.isNull(detailEntity) && StringUtils.isNotBlank(detailEntity.getCountersigneds())) {
            rtnLs = Arrays.asList(detailEntity.getCountersigneds().split(","));
        }
        return rtnLs;
    }
    
    /**
     * ?????????:???????????????????????????????????????????????????????????????????????????????????????<br>
     * ????????????: (1)????????????????????? (2)????????????????????????<br><br>
     * ??????:ADMIN???????????????????????????????????????????????????????????????????????????
     * 
     * @param vo
     * @return
     */
    public boolean isAlertECTWarning (RequirementFormVO vo) {
        Date ect = vo.getEct();
        Date eot = vo.getEot();
        boolean isAlert = false;
        boolean isPic = commonFormService.isPic();
        boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
        boolean isProposing = FormEnum.PROPOSING.name().equals(vo.getFormStatus());
        
        if ((ect != null && eot != null) && ect.after(eot)) {
            if (isPic && isProposing) {
                isAlert = true;
            } else if (isVice && !isProposing) {
                isAlert = true;
            }
        }
        
        return isAlert;
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
    protected String getFormApplyGroupInfo(RequirementFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailApplySrEntity formProcessDetailApplySrEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailApplySrEntity formProcessDetailApplySrEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(RequirementFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailReviewSrEntity formProcessDetailReviewSrEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailReviewSrEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailReviewSrEntity formProcessDetailReviewSrEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewSrEntity.getGroupId();
    }
    
    /**
     * 0007089: ???008???ISO_7_???????????????????????????
     * ????????????????????????APPLY_4??????->REVIEW_1???????????????????????????(?????????=????????????????????????=Y)???????????????????????????????????????
     */
    @Override
    public String checkAttachmentExists(RequirementFormVO vo) {
        boolean allClose = true;
        int newSystemCount = 0;
        StringBuffer errorMsg = new StringBuffer();

        // ???????????????,?????????????????????
        List<FormEntity> changeList = formRepo.findBySourceIdAndFormClass(vo.getFormId(), FormEnum.CHG.name());
        for (FormEntity form : changeList) {
            String formStatus = form.getFormStatus();
            if(!(FormEnum.CLOSED.name().equals(formStatus) || FormEnum.DEPRECATED.name().equals(formStatus))) {
                allClose = false;
                break;
            }
        }

        if (!allClose) {
            String msg = getMessage("form.requirement.stretch.form.chg.not.all.closed");
            errorMsg.append(msg).append("\r\n");
        } else { // ???????????????????????????????????????
            List<String> formIds = changeList.stream()
                    .filter(it -> it.getFormStatus().equals(FormEnum.CLOSED.name()))
                    .map(it -> it.getFormId())
                    .collect(Collectors.toList());

            newSystemCount = formChgDetailRepo.countByFormIdInAndIsNewSystem(formIds, StringConstant.SHORT_YES);
        }

        // ??????????????????: ????????????????????????
        if (newSystemCount > 0) {
            List<FormFileEntity> fileDetailLs = formFileRepository.findByFormIdOrderByCreatedAtDesc(vo.getFormId());
            SysParameterEntity entity = sysParameterRepo.findByParamKey(SysParametersEnum.REQUIREMENT_FORM_ATTACHMENT_NAME_NEW_SYSTEM.name());
            String targetFileName = entity.getParamValue();
            boolean targetFileExsits = false;

            for (FormFileEntity target : fileDetailLs) {
                if ((target.getName().contains(targetFileName))) {
                    targetFileExsits = true;
                    break;
                }
            }

            if (!targetFileExsits) {
                String msg = getMessage("form.requirement.verify.attachment.file.name.error", targetFileName);
                errorMsg.append(msg).append("\r\n");
            }
        }

        return errorMsg.toString();
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????????????????
     */
    private void applyVerification (RequirementFormVO vo, List<FormProcessDetailApplySrEntity> jumpList) {
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
        FormProcessDetailApplySrEntity applyPojo = jumpList.get(size-1);

        vo.setCompleteTime(null);
        if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
            verifyResult = applyPojo.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
            vo.setUserId(null);
            vo.setGroupId(applyPojo.getGroupId());
            
            if (StringUtils.isEmpty(vo.getUserId()) && sysUserService.isPic(applyPojo.getGroupId())) {
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
            vo.setUserId(back.getUserId());
        }
    }

    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????
     */
    private void reviewVerification (RequirementFormVO vo, List<FormProcessDetailReviewSrEntity> jumpList) {
        if (CollectionUtils.isEmpty(jumpList)) {
            vo.setVerifyResult(FormEnum.CLOSED.name());
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            closeForm(form);
            
            return;
        }

        if (!vo.getIsNextLevel()) {
            Collections.reverse(jumpList);
        }
        
        // ?????????????????????
        int size = jumpList.size();
        FormProcessDetailReviewSrEntity reviewPojo = jumpList.get(size-1);
        if (vo.getIsNextLevel()) {
            vo.setUserId(null);
            vo.setGroupId(reviewPojo.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
        } else {
            // ????????????????????????????????????
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // ????????????
            vo.setUserId(null); // ????????????
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, reviewPojo.getProcessOrder(), FormEnum.REVIEW, FormEnum.PENDING);
            // ????????????????????????????????????( ?????????????????????????????? )
            vo.setUserId(back.getUserId());
        }
    }
    
    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

}
