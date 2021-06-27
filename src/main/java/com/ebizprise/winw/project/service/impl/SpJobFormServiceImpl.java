package com.ebizprise.winw.project.service.impl;

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
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobInfoSysDetailEntity;
import com.ebizprise.winw.project.entity.FormJobInfoWorkingItemsEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormJobCheckPersonRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoSysDetailRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoWorkingItemsRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SpJobFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * SP工作單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@Transactional
@Service("spJobFormService")
public class SpJobFormServiceImpl extends BaseFormService<SpJobFormVO> implements IBaseFormService<SpJobFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(SpJobFormServiceImpl.class);

    @Autowired
    private IFormJobInfoWorkingItemsRepository formItemRepo;
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormJobInfoDateRepository formDateRepo;
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
    private IFormJobInfoSysDetailRepository formDetailRepo;
    @Autowired
    private IFormInfoDateRepository formInfoDateRepo;
    @Autowired
    private IFormJobCheckPersonRepository personRepo;
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    @Override
    protected SpJobFormVO getCurrentLevelInfo (SpJobFormVO vo) {
        String clazz = vo.getFormClass();
        String verifyType = vo.getVerifyType();
        SpJobFormVO currentLevel = new SpJobFormVO();
        
        if (StringUtils.isNoneBlank(clazz) &&
                StringUtils.isNoneBlank(verifyType)) {
            FormEnum clazzEnum = FormEnum.valueOf(clazz);
            FormEnum typeEnum = FormEnum.valueOf(verifyType);
            
            ResourceEnum resource = formHelper.getLevelInfoResource(clazzEnum, typeEnum);
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            params.put("verifyLevel", vo.getVerifyLevel());
            
            currentLevel = jdbcRepository.queryForBean(resource, params, SpJobFormVO.class);
        }

        return currentLevel;
    }
    
    @Override
    public void updateVerifyLog (SpJobFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void create (SpJobFormVO vo) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        
        vo.setSourceId(vo.getFormId());
        vo.setGroupSolving(loginUser.getGroupId());
        vo.setUserCreated(loginUser.getUserId());
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setUserSolving(loginUser.getUserId());
        vo.setDivisionSolving(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setProcessStatus(FormEnum.PROPOSING.name());
        vo.setStatusWording(FormEnum.PROPOSING.status());
        vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(loginUser.getGroupName()));
        vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), loginUser.getGroupName()));
        vo.setCreatedAt(today);
        vo.setCreatedBy(UserInfoUtil.loginUserId());
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(UserInfoUtil.loginUserId());
        
        String sourceId = getSourceFormList(vo.getFormId()).get(0).getFormId();
        FormInfoDateEntity datePojo = formInfoDateRepo.findByFormId(sourceId);
        vo.setEct(datePojo.getEct());

        vo.setEot(null);
        vo.setAst(null);
        vo.setAct(null);
        vo.setUserId(null);
        vo.setFormId(null);
        vo.setCreateTime(null);
        vo.setVerifyType(null);
        vo.setFormStatus(null);
        vo.setAssignTime(null);
        vo.setVerifyLevel(null);
        vo.setCountersigneds(null);
    }

    @ModLog
    @Override
    public void getFormInfo (SpJobFormVO vo) {
        vo.setFormWording(getMessage(FormEnum.JOB_SP.wording()));
        
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_JOB_BY_CONDITIONS");
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
        
        SpJobFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, SpJobFormVO.class);
        
        if (formInfo == null) {
            return;
        }

        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        BeanUtil.copyProperties(formInfo, vo);
        vo.setFormType(FormEnum.valueOf(vo.getFormClass()).formType());
        vo.setStatusWording(FormEnum.valueOf(vo.getFormStatus()).status());

        if (FormEnum.PROPOSING.name().equals(vo.getFormStatus())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(vo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), vo.getGroupName()));
            return;
        }
        
        SpJobFormVO currentLevel = getCurrentLevelInfo(vo);

        if (isClosed(vo.getFormId())) {
            vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(currentLevel.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), currentLevel.getGroupName()));
            return;
        }

        // 取得該關卡資訊
        BeanUtil.copyProperties(currentLevel, vo);
        vo.setIsCreateJobCIssue(isSubCreation(vo.getIsCreateJobCIssue(), currentLevel.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), currentLevel.getGroupId()));
        
        String formStatusWording =
                StringConstant.SHORT_YES.equals(vo.getIsWorkLevel()) ?
                        currentLevel.getWorkProjectName() : currentLevel.getGroupName();
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(formStatusWording));
        
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), currentLevel.getGroupName()));

        // 判斷該登入者是否有具有審核資格
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        // 是否可直接結案
        vo.setIsCloseForm(verifyIsFormClose(vo));
        // 是否為作業關卡的指定人員
        vo.setIsOwner(isOwner(vo));
        //是否為審核者
        vo.setIsApprover(isApprover(vo));
        //是否為申請流程最後一關
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
    }

    @Override
    public void sendToVerification (SpJobFormVO vo) {
        // 初始化申請流程
        if (verifyRepo.countByFormId(vo.getFormId()) <= 0) {
            vo.setCompleteTime(new Date());
            vo.setUserId(UserInfoUtil.loginUserId());
            vo.setGroupId(fetchLoginUser().getGroupId());
            saveLevel(vo, 1, FormEnum.APPLY, FormEnum.SENT);
        }
        
        int verifyLevel = Integer.valueOf(vo.getVerifyLevel());
        int jumpLevel = Integer.valueOf(vo.getJumpLevel());
        List<FormProcessDetailApplyJobEntity> applyList =
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel);

        // 申請只有一關的情況下, 會直接進入審核階段並初始化審核階段第一關的資料。
        if (CollectionUtils.isEmpty(applyList)) {
            vo.setUserId(null);
            vo.setCompleteTime(null);
        }
        
        applyVerification(vo, applyList);
    }

    @Override
    public void prepareVerifying(SpJobFormVO vo) {
        prepareVerifyWording(vo);
    }
    
    @Override
    public void verifying (SpJobFormVO vo) {
        updateCurrentLevel(vo);
        
        // 表單作廢
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
    public void createVerifyCommentByVice(SpJobFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // 系統原本更新關卡資料的邏輯
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm (SpJobFormVO vo, FormVerifyType type) {
        boolean result = false;
        
        if (type == FormVerifyType.STRETCH_ZERO) {
            result = getStretchs(vo.getFormId()) > 0;
        } else if (type == FormVerifyType.STRETCH_FINISHED) {
            result = isStretchFormClosed(vo);
        }
        
        return result;
    }

    @Override
    public void deleteForm (SpJobFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo (SpJobFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// 新增
            BeanUtil.copyProperties(
                    newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass())), vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.JOB));
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
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("UPDATE_JOB_SP_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
        }

        saveWorkingItems(vo);
    }

    @Override
    public boolean isVerifyAcceptable (SpJobFormVO vo, SysUserVO userInfo) {
        FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
        ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));
      
        return isAcceptable(vo, verifyPojo, resource, userInfo);
    }

    @Override
    public boolean isFormClosed (SpJobFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (SpJobFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
    }

    @Override
    public void notifyProcessMail (SpJobFormVO vo) {
        asyncMailLauncher(vo);
    }

    /**
     * 儲存處理方案的資料
     * 
     * @param vo
     */
    @Override
    public void saveProgram (SpJobFormVO vo) {
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
     * 取得處理方案的資料
     * 
     * @param formId
     */
    @Override
    public SpJobFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        SpJobFormVO vo = new SpJobFormVO();
        BeanUtil.copyProperties(pojo, vo);
        
        return vo;
    }
    
    /**
     * 副科/副理 表單直接結案
     * 
     * @param vo
     */
    @Override
    public void immediateClose (SpJobFormVO vo) {
         closeFormForImmdiation(vo);
    }

    @Override
    public SpJobFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), SpJobFormVO.class);
    }

    @Override
    public SpJobFormVO getFormDetailInfo(String formId) {
        SpJobFormVO vo = new SpJobFormVO();
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
    public boolean isAllStretchDied (SpJobFormVO vo) {
        return super.isAllStretchDied(vo);
    }
    
    /**
     * 透過FormId取得該表單會辦科部門資訊
     * 
     * @param formId
     * @return List
     */
    @Override
    public List<String> getFormCountsignList(String formId) {
        FormJobInfoSysDetailEntity detailEntity = formDetailRepo.findByFormId(formId);
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
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        if(FormEnum.APPLY.name().equalsIgnoreCase(verifyType)) {
            int count = applyRepo.countByDetailId(detailId);
            return (count == Integer.parseInt(verifyLevel))? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
        }
        
        return StringConstant.SHORT_NO;
    }

    @Override
    protected String getFormApplyGroupInfo(SpJobFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity formProcessDetailApplySrEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity formProcessDetailApplySrEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailApplySrEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(SpJobFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewJobEntity formProcessDetailReviewSrEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return formProcessDetailReviewSrEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewJobEntity formProcessDetailReviewSrEntity = reviewRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return formProcessDetailReviewSrEntity.getGroupId();
    }
    
    //================================================================
    
    private void saveWorkingItems (SpJobFormVO vo) {
        formItemRepo.deleteByFormId(vo.getFormId());
        
        if(StringUtils.isNotBlank(vo.getWorkingItem())) {
            List<FormJobInfoWorkingItemsEntity> list = BeanUtil.fromJsonToList(vo.getWorkingItem(), FormJobInfoWorkingItemsEntity.class);
            
            for (FormJobInfoWorkingItemsEntity pojo : list) {
                BeanUtil.copyProperties(vo, pojo);
                pojo.setCreatedBy(pojo.getUpdatedBy());
                pojo.setCreatedAt(pojo.getUpdatedAt());
            }
            
            formItemRepo.saveAll(list);
        }
    }

    /*
     * 1. 新增待申請關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否轉換為審核流程
     */
    private void applyVerification (SpJobFormVO vo, List<FormProcessDetailApplyJobEntity> jumpList) {
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
        } else {// 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            vo.setGroupId(back.getGroupId());
            verifyResult = FormEnum.PENDING;
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.APPLY, verifyResult);
            vo.setUserId(back.getUserId());// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        }
        
    }
    
    /*
     * 1. 新增待審核關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否可結案
     */
    private void reviewVerification (SpJobFormVO vo, List<FormProcessDetailReviewJobEntity> jumpList) {
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
        } else {// 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
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
