package com.ebizprise.winw.project.service.impl;

import java.text.DateFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInfoIncDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoQDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoUserEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyQEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewQEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysMailLogEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.enums.MailTemplate;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormInfoChgDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoQDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoUserRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyQRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewQRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ISysMailLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.ISearchExtend;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.EventFormVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.QuestionFormVO;
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
@Service("questionFormService")
public class QuestionFormServiceImpl extends BaseFormService<QuestionFormVO> implements IBaseFormService<QuestionFormVO>{
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionFormServiceImpl.class);
    
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoUserRepository formUserRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoQDetailRepository formDetailRepo;
    @Autowired
    private IFormProcessDetailApplyQRepository applyRepo;
    @Autowired
    private IFormProcessDetailReviewQRepository reviewRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private IFormInfoIncDetailRepository eventFormDetail;
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
    private ISysMailLogRepository mailLogRepo;
    
    @Override
    public void updateVerifyLog (QuestionFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void ectExtended (QuestionFormVO vo) {
        ectExtendedForStretchs(vo);
    }

    @Override
    public void create(QuestionFormVO vo) {
        vo.setFormClass(FormEnum.Q.name());
        vo.setUserCreated(fetchLoginUser().getUserId());
        vo.setFormWording(getMessage(FormEnum.Q.wording()));
    }
    
    /**
     * ???????????????ID??????????????????(??????=?????????)
     * @param vo
     * @param replaceUserCreated
     * @author jacky.fu
     */
    public void create(QuestionFormVO vo, boolean replaceUserCreated) {
        vo.setUserCreated(vo.getUserSolving());
        vo.setFormClass(FormEnum.Q.name());
        vo.setSourceId(vo.getFormId());
        vo.setFormWording(getMessage(FormEnum.Q.wording()));
        vo.setFormStatus(FormEnum.PROPOSING.name());
        vo.setDivisionCreated(vo.getDivisionSolving());
        vo.setFormId("");
    }
    
    @Override
    @ModLog
    public void getFormInfo(QuestionFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());

        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        QuestionFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, QuestionFormVO.class);

        vo.setFormWording(getMessage(FormEnum.Q.wording()));
        
        // ???????????????????????????
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, QuestionFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setFormClass(FormEnum.Q.name());
            vo.setFormType(FormEnum.Q.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }
        
        //??????formId,?????????ID,???????????????
        String formId = vo.getFormId();
        if (StringUtils.isNotBlank(formId)) {
            FormInfoQDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
            FormInfoUserEntity userEntity = formUserRepo.findByFormId(formId);
            FormInfoDateEntity dateEntity = formDateRepo.findByFormId(formId);
            FormProgramEntity programEntity = programRepo.findByFormId(formId);
            
            BeanUtil.copyProperties(programEntity, formInfo, new String[] {"updatedAt"});
            BeanUtil.copyProperties(detailEntity, formInfo, new String[] {"updatedAt"});
            BeanUtil.copyProperties(userEntity, formInfo, new String[] {"updatedAt"});
            BeanUtil.copyProperties(dateEntity, formInfo, new String[] {"updatedAt"});
            BeanUtil.copyProperties(formInfo, vo);
        }
        
        // ???????????????????????????????????????
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
        QuestionFormVO levelInfo = jdbcRepository.queryForBean(resource, params, QuestionFormVO.class);

        if (isClosed(formInfo.getFormId())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
            return;
        }

        // ?????????????????????
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateChangeIssue(isSubCreation(vo.getIsCreateChangeIssue(), levelInfo.getGroupId()));
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
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
    public void mergeFormInfo(QuestionFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// ??????
            BaseFormVO newerDetail = newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass()));
            BeanUtil.copyProperties(newerDetail, vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.Q));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormInfoUserEntity formUserPojo = new FormInfoUserEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoQDetailEntity formDetailPojo = new FormInfoQDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formUserPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);
            
            formRepo.save(formPojo);
            formUserRepo.save(formUserPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
        } else {
            if (isWatchingStatusForm(vo)) {
                vo.setFormStatus(FormEnum.WATCHING.name());
            }
            
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_Q.getResource("UPDATE_Q_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
            saveFormAlterResult(formId, false);
        }
    }

    @Override
    public void sendToVerification(QuestionFormVO vo) {
        Date today = new Date();
        FormEntity pojo = formRepo.findByFormId(vo.getFormId());
        pojo.setCreateTime(today);
        formRepo.save(pojo);
        
        // ?????????????????????
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(new Date());
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }

        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyQEntity> applyList =
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
    public void prepareVerifying(QuestionFormVO vo) {
        prepareVerifyWording(vo);
    }

    @Override
    public void verifying(QuestionFormVO vo) {
        updateCurrentLevel(vo);
        
        //??????????????????????????????????????????????????????????????????????????????????-->??????????????????????????????isSuggestCase?????????Y
        if (FormEnum.REVIEW.name().equals(vo.getVerifyType()) &&
                StringConstant.SHORT_YES.equals(vo.getIsKnowledgeable()) && 
                StringConstant.SHORT_NO.equals(vo.getIsSuggestCase())) {
            programRepo.updateIsSuggestCaseByFormId(vo.getFormId());
        }
        
        // ????????????
        if (FormEnum.DEPRECATED.equals(
                FormEnum.valueOf(vo.getFormStatus()))) {
            FormEntity form = formRepo.findByFormId(vo.getFormId());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
            
            return;
        }
        
        // ???????????????????????????????????????????????????
        if (FormEnum.WATCHING.equals(FormEnum.valueOf(vo.getFormStatus())) && isBackToApplyProcess(vo)) {
            updateObservationStatus(vo, true);
        }
        
        // ????????????????????????????????????
        boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
        if (FormEnum.APPROVING.name().equals(vo.getProcessStatus()) && isVice) {
            updateObservationStatus(vo, false);
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
                List<FormProcessDetailApplyQEntity> applyList =
                    applyRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                applyVerification(vo, applyList);
                
                break;

            case REVIEW:
                List<FormProcessDetailReviewQEntity> reviewList =
                    reviewRepo.findLimitList(vo.getDetailId(), limits.get(0), limits.get(1));
                reviewVerification(vo, reviewList);
                
                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(QuestionFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // ???????????????????????????????????????
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm(QuestionFormVO vo, FormVerifyType type) {
        boolean result = false;
        
        if (type == FormVerifyType.STRETCH_ZERO) {
            result = getStretchs(vo.getFormId()) > 0;
        } else if (type == FormVerifyType.STRETCH_FINISHED) {
            result = isStretchFormClosed(vo);
        }
        
        return result;
    }

    @Override
    public void deleteForm(QuestionFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public boolean isVerifyAcceptable(QuestionFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed(QuestionFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (QuestionFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (QuestionFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public QuestionFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), QuestionFormVO.class);
    }

    @Override
    public QuestionFormVO getFormDetailInfo(String formId) {
        QuestionFormVO vo = new QuestionFormVO();
        BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), vo, new String[] {"updatedAt"});
        return vo;
    }

    /**
     * ???????????????????????????
     * 
     * @param formId
     */
    @Override
    public QuestionFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        QuestionFormVO vo = new QuestionFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }
    
    /**
     * ???????????????????????????
     * 
     * @param vo
     */
    @Override
    public void saveProgram (QuestionFormVO vo) {
        Date today = new Date();
        FormProgramEntity pojo = new FormProgramEntity();
        BeanUtil.copyProperties(vo, pojo);
        pojo.setUpdatedAt(today);
        pojo.setUpdatedBy(UserInfoUtil.loginUserId());
        
        if (StringUtils.isBlank(pojo.getCreatedBy())) {
            pojo.setCreatedAt(today);
            pojo.setCreatedBy(UserInfoUtil.loginUserId());
            programRepo.save(pojo);
        } else {
            Map<String, Object> params = BeanUtil.toMap(pojo);
            jdbcRepository.update(ResourceEnum.SQL_FORM_OPERATION.getResource("UPDATE_PROGRAM_WITH_KNOWLEDGE"), params);
        }
        
        BeanUtil.copyProperties(pojo, vo);
    }

    @Override
    public void lockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_YES, formId);
    }
    
    @Override
    public void unlockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_NO, formId);
    }

    /**
     * ??????/?????? ??????????????????
     * 
     * @param vo
     */
    @Override
    public void immediateClose (QuestionFormVO vo) {
        closeFormForImmdiation(vo);
    }

    @Override
    public boolean isAllStretchDied (QuestionFormVO vo) {
        return super.isAllStretchDied(vo);
    }
    
    @Override
    public List<String> getFormCountsignList(String formId) {
        FormInfoQDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
        List<String> rtnLs = new ArrayList<>();
        
        if(!Objects.isNull(detailEntity) && StringUtils.isNotBlank(detailEntity.getCountersigneds())) {
            rtnLs = Arrays.asList(detailEntity.getCountersigneds().split(","));
        }
        return rtnLs;
    }
    
    /**
     * 0007089: ???008???ISO_7_???????????????????????????
     * ????????????????????????APPLY_4??????->REVIEW_1???????????????????????????(?????????=????????????????????????=Y)???????????????????????????????????????
     */
    @Override
    public String checkAttachmentExists(QuestionFormVO vo) {
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
            String msg = getMessage("form.question.stretch.form.chg.not.all.closed");
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
            SysParameterEntity entity = sysParameterRepo.findByParamKey(SysParametersEnum.QUESTION_FORM_ATTACHMENT_NAME_NEW_SYSTEM.name());
            String targetFileName = entity.getParamValue();
            boolean targetFileExsits = false;

            for (FormFileEntity target : fileDetailLs) {
                if ((target.getName().contains(targetFileName))) {
                    targetFileExsits = true;
                    break;
                }
            }

            if (!targetFileExsits) {
                String msg = getMessage("form.question.verify.attachment.file.name.error", targetFileName);
                errorMsg.append(msg).append("\r\n");
            }
        }

        return errorMsg.toString();
    }

    /**
     * ????????????????????????????????????
     * 
     * @param sourceId
     * @return EventFormVO
     */
    public EventFormVO getSourceEventForm(String sourceId) {
        FormInfoIncDetailEntity entity = eventFormDetail.findByFormId(sourceId);
        EventFormVO rtnVO = new EventFormVO();
        
        BeanUtil.copyProperties(entity, rtnVO);
        
        return rtnVO;
    }
    
    /**
     * ?????????????????????????????????
     * (1)???????????????7??????30?????????????????????
     * (2)??????????????????????????????????????????????????????????????????????????????????????????
     * 
     * @param vo
     * @param verifyUtil
     * @throws Exception
     */
    public void validateObservationLogic(QuestionFormVO vo, Map<String, Object> validateMap, DataVerifyUtil verifyUtil) throws Exception {
        Date today = new Date();
        Date observation = vo.getObservation();
        DateFormat df = DateFormat.getDateInstance();
        boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
        FormInfoDateEntity dbRecord = formDateRepo.findByFormId(vo.getFormId());
        boolean isWatching = FormEnum.WATCHING.name().equals(vo.getFormStatus());
        Date dbObservation = dbRecord.getObservation();
        boolean dbIsSpecial = StringConstant.SHORT_YES.equals(dbRecord.getIsSpecial());

        if (isWatching && dbObservation.compareTo(observation) == 0) return;
        
        int total = 0;
        boolean isSave = MapUtils.getBoolean(validateMap, "isSave");
        boolean isReview = MapUtils.getBoolean(validateMap, "isReview");
        boolean isSpecial = MapUtils.getBoolean(validateMap, "isSpecial");
        boolean isDeprecated = MapUtils.getBoolean(validateMap, "isDeprecated");
        boolean isVerifyAcceptable = MapUtils.getBoolean(validateMap, "isVerifyAcceptable");
        
        if (isVerifyAcceptable && isVice && isReview && !isDeprecated && observation != null && isSave) {
            total = (int) DateUtils.getSmartDiff(today, observation, DateUtils.Type.Day);
            obervationRule(isSpecial, total, verifyUtil);
        }

        if (isVerifyAcceptable && isVice && isReview && !isDeprecated && observation != null && !isSave) {
            total = (int) DateUtils.getSmartDiff(today, observation, DateUtils.Type.Day);

            if (dbObservation == null) {
                verifyUtil.append(getMessage("form.question.form.info.observation.validate.not.saved"));
            } else if (isSpecial != dbIsSpecial ||
                    !df.format(observation).equals(df.format(dbObservation))) {
                obervationRule(isSpecial, total, verifyUtil);
            }
        }
    }

    @Override
    protected String getReviewLastLevel (String detailId) {
        return String.valueOf(
                reviewRepo.findByDetailId(detailId).size());
    }

    @Override
    protected String getFormApplyGroupInfo(QuestionFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailApplyQEntity formProcessDetailApplyQEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplyQEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailApplyQEntity formProcessDetailApplyQEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplyQEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(QuestionFormVO vo) {
        // ?????????????????????????????????
        FormProcessDetailReviewQEntity formProcessDetailReviewQEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailReviewQEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // ?????????????????????????????????
        FormProcessDetailReviewQEntity formProcessDetailReviewQEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewQEntity.getGroupId();
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
     * ????????????7???30?????????
     * @param isSpecial
     * @param total
     * @param verifyUtil
     * @author adam.yeh
     */
    private void obervationRule (boolean isSpecial, int total, DataVerifyUtil verifyUtil) {
        if (isSpecial && total < 30) {
            verifyUtil.append(getMessage("form.question.form.info.observation.validate.1"));
        } else if (total < 6) {
            verifyUtil.append(getMessage("form.question.form.info.observation.validate.2"));
        }
    }
    
    /*
     * 1. ?????????????????????
     * 2. ???????????????????????????
     * 3. ?????????????????????????????????
     */
    private void applyVerification (QuestionFormVO vo, List<FormProcessDetailApplyQEntity> jumpList) {
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
        FormProcessDetailApplyQEntity applyPojo = jumpList.get(size-1);
        
        vo.setCompleteTime(null);
        if (vo.getIsNextLevel() && !vo.getIsBackToApply()) {
            verifyResult = applyPojo.getProcessOrder() == 1 ? FormEnum.SENT : FormEnum.PENDING;
            vo.setUserId(null);

            if (sysUserService.isPic(applyPojo.getGroupId()) && StringUtils.isEmpty(vo.getUserId())) {
                vo.setUserId(formRepo.findByFormId(vo.getFormId()).getUserSolving());
            }
            
            vo.setGroupId(applyPojo.getGroupId());
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
    private void reviewVerification (QuestionFormVO vo, List<FormProcessDetailReviewQEntity> jumpList) {
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
        FormProcessDetailReviewQEntity reviewPojo = jumpList.get(size-1);
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
    
    /**
     * ????????????:????????????????????????????????????????????????????????????????????????
     * 
     * @param vo
     * @return
     */
    private boolean isWatchingStatusForm (QuestionFormVO vo) {
        Date observation = vo.getObservation();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
        boolean isReview = FormEnum.REVIEW.name().equals(vo.getVerifyType());
        boolean isApproving = FormEnum.APPROVING.name().equals(vo.getFormStatus());
        boolean disagreed = FormEnum.DISAGREED.name().equals(vo.getVerifyResult());
        
        return (!vo.getIsNextLevel() && isVerifyAcceptable && isVice && isReview && isApproving && !disagreed && observation != null);
    }

    /**
     * @param vo
     * @param isObserving ??????????????????????????????????????????
     */
    private void updateObservationStatus(QuestionFormVO vo, boolean isObserving) {
        if (isObserving) {
            vo.setObservation(null);
        }

        vo.setFormStatus(FormEnum.APPROVING.name());
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_Q.getResource("UPDATE_Q_FORM_OBSERVATION");
        Map<String, Object> params = BeanUtil.toMap(vo);
        jdbcRepository.update(resource, params);
    }
    
    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }
    
    /**
     * ?????????????????????MAIL?????????????????????<br>
     * ??????:???????????????????????????<br>
     * (1)?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????ID??????????????????<br>
     * (2)????????????????????????????????????????????????????????????
     * @param vo
     * @author jacky.fu
     */
    public void sendMailToDivisionSolving (QuestionFormVO vo) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        List<String> mailList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        
        LdapUserEntity ldapUser = ldapUserRepository.findByUserId(vo.getUserSolving());
        if (ldapUser != null) {
            SysGroupEntity userGroup = sysGroupRepository.findBySysGroupId(Long.valueOf(ldapUser.getSysGroupId()));
            String formName = getMessage(FormEnum.Q.wording());
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
            params.put("summary", "");                                                             // ??????
            params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // ????????????
            params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // ????????????
            sendMail(subject, mailList, params);
            
            SysMailLogEntity mailLog = new SysMailLogEntity();
            mailLog.setCreatedAt(today);
            mailLog.setUpdatedAt(today);
            mailLog.setRecognize(vo.getFormId());
            mailLog.setCreatedBy(loginUser.getUserId());
            mailLog.setUpdatedBy(loginUser.getUserId());
            mailLog.setAddresses(BeanUtil.toJson(mailList));
            mailLogRepo.save(mailLog);
            logger.info("????????????????????? : " + BeanUtil.toJson(mailList));
        }
    }
    
    /**
     * ???????????????????????????????????????????????????
     * @param vo
     * @author jacky.fu
     */
    public void sendMailToVice (QuestionFormVO vo) {
        Date today = new Date();
        String hyperLink = String.format(
                "https://%s/ISWP/formSearch/search/", env.getProperty("mail.ap.domain"));
        
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_NAME_MANAGEMENT.getResource("FIND_USERS_BY_SYSGROUP");
        Conditions conditions = new Conditions();
        
        conditions.and().equal("SG.GroupId", vo.getGroupSolving() + UserEnum.VICE_DIVISION_CHIEF.symbol());
        List<LdapUserVO> vscList = jdbcRepository.queryForList(resource, conditions, LdapUserVO.class);
        
        List<String> mailList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        
        for (LdapUserVO ldapVo : vscList) {
            SysGroupEntity userGroup = sysGroupRepository.findBySysGroupId(Long.valueOf(ldapVo.getSysGroupId()));
            String formName = getMessage(FormEnum.Q.wording());
            String mailSubject = getMessage("schedule.audit.mail.subject.q.observation.expired") + " " + vo.getFormId();
            
            mailList.clear();
            mailList.add(ldapVo.getEmail());
            if (mailList != null && mailList.size() > 0) {
                params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // ????????????
                params.put("group", userGroup.getGroupName());                                         // ????????????
                params.put("formId", vo.getFormId());                                                  // ????????????
                params.put("formClass", vo.getFormClass());                                            // ????????????
                params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // ????????????
                params.put("divisionCreated", vo.getDivisionCreated());                                // ????????????
                params.put("userCreated", vo.getUserCreated());
                params.put("summary", getFormDetailInfo(vo.getFormId()).getSummary());                 // ??????
                params.put("link", hyperLink + vo.getFormId());                                        // ???????????????
                params.put("template", MailTemplate.AGREED.src());                                     // ????????????
                params.put("formName", formName);                                                      // ????????????
                params.put("verifyType", getMessage("report.operation.status.approving"));             // ?????????????????? 
                params.put("formClass", formName);                                                     // ????????????
                params.put("user", ldapVo.getName());                                                  // ???????????????
                sendMail(mailSubject, mailList, params);
                
                SysMailLogEntity mailLog = new SysMailLogEntity();
                String jobName = "NotificationObsExpiredQJob";
                mailLog.setCreatedAt(today);
                mailLog.setUpdatedAt(today);
                mailLog.setRecognize(jobName);
                mailLog.setCreatedBy("SCHEDULE");
                mailLog.setUpdatedBy("SCHEDULE");
                mailLog.setAddresses(BeanUtil.toJson(mailList));
                mailLogRepo.save(mailLog);
                logger.info("????????????????????? : " + BeanUtil.toJson(mailList));
            }
        }
    }
    
    /**
     * ???????????????
     * 
     * @param vo
     * @param iSearchExtend
     * @return
     * @author jacky.fu
     */
    public List<QuestionFormVO> getQForms (QuestionFormVO vo, ISearchExtend iSearchExtend) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_Q.getResource("FIND_Q_FORMS_BY_CONDITION");
        Conditions conditions = new Conditions();
        iSearchExtend.cusCondition(conditions);
        return iSearchExtend.cusQuery(jdbcRepository, resource, conditions);
    }

    /**
     * ??????????????????????????????
     * ????????????????????????????????????????????????:?????????????????????or????????????????????????????????????
     * 
     * @param vo
     * @return
     * @author jacky.fu
     */
    public List<QuestionFormVO> getQFormObsExpired (QuestionFormVO vo) {
        return getQForms(vo, new ISearchExtend() {
            @Override
            public Conditions cusCondition (Conditions con) {
                String dateStr = DateUtils.toString(new Date(), DateUtils._PATTERN_YYYYMMDD_SLASH);
                con.and().notNull("FID.Observation");
                con.and().equal("f.FormStatus", FormEnum.WATCHING.name());
                con.and().ltEqual("CONVERT(date, FID.Observation, 111)", dateStr);
                return con;
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<QuestionFormVO> cusQuery (JdbcRepositoy jdbcRepository, ResourceEnum resource,
                    Conditions conditions) {
                return jdbcRepository.queryForList(resource, conditions, QuestionFormVO.class);
            }
        });
    }
    
}
