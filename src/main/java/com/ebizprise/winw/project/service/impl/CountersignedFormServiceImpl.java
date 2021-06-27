package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ebizprise.winw.project.entity.FormInfoCDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormProgramEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.jdbc.criteria.SQL;
import com.ebizprise.winw.project.repository.IFormInfoCDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyIncRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyQRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplySrRepository;
import com.ebizprise.winw.project.repository.IFormProgramRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.ISysGroupService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.CountersignedFormVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 會辦單 新增/修改/審核/退回等作業
 * 
 * @author adam.yeh
 */
@Transactional
@Service("countersignedFormService")
public class CountersignedFormServiceImpl extends BaseFormService<CountersignedFormVO> implements IBaseFormService<CountersignedFormVO> {

    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoCDetailRepository formDetailRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    @Autowired
    private IFormProgramRepository programRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private ISysGroupService sysGroupService;
    @Autowired
    private IFormProcessDetailApplyQRepository qApplyRepo;
    @Autowired
    private IFormProcessDetailApplySrRepository srApplyRepo;
    @Autowired
    private IFormProcessDetailApplyIncRepository incApplyRepo;
    
    @Override
    public void ectExtended (CountersignedFormVO vo) {
        // mect: 主單預計完成時間
        vo.setEct(vo.getMect());
        ectExtendedForStretchs(vo);
    }

    @Override
    public void immediateClose (CountersignedFormVO vo) {
        closeFormForImmdiation(vo);
    }

    @Override
    public void updateVerifyLog (CountersignedFormVO vo) {
        super.updateVerifyLog(vo);
    }
    
    @Override
    public void create (CountersignedFormVO vo) {
        Date today = new Date();
        String formClass = vo.getFormClass();
        SysUserVO loginUser = fetchLoginUser();
        
        vo.setSourceId(vo.getFormId());
        vo.setMect(vo.getEct());
        vo.setUserCreated(loginUser.getUserId());
        vo.setUnitId(passDivision(vo.getDivisionCreated()));
        vo.setDivisionCreated(loginUser.getDepartmentId() + "-" + loginUser.getDivision());
        vo.setProcessStatus(FormEnum.PROPOSING.name());
        vo.setStatusWording(FormEnum.PROPOSING.status());
        vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(vo.getUserGroup()));
        vo.setProcessStatusWording(String.format(FormEnum.PROPOSING.processStatus(), vo.getUserGroup()));
        vo.setCreatedAt(today);
        vo.setCreatedBy(UserInfoUtil.loginUserId());
        vo.setUpdatedAt(today);
        vo.setUpdatedBy(UserInfoUtil.loginUserId());
        vo.setAssignTime(today);
        
        if (FormEnum.SR_C.name().equals(formClass)) {
            vo.setHostHandle(vo.getProcessProgram());
        }

        vo.setFormId(null);
        vo.setFormStatus(null);
        vo.setUserSolving(null);
        vo.setEot(null);
        vo.setAst(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setVerifyType(null);
        vo.setCreateTime(null);
        vo.setGroupSolving(null);
    }

    @Override
    @ModLog
    public void getFormInfo (CountersignedFormVO vo) {
        vo.setFormWording(getMessage(FormEnum.valueOf(vo.getFormClass()).wording()));

        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());

        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }

        CountersignedFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, CountersignedFormVO.class);

        if (formInfo == null) {
            return;
        }

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
        CountersignedFormVO levelInfo = jdbcRepository.queryForBean(resource, params, CountersignedFormVO.class);

        // 取得該關卡資訊
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateChangeIssue(isSubCreation(vo.getIsCreateChangeIssue(), levelInfo.getGroupId()));
        vo.setIsCreateCIssue(isSubCreation(vo.getIsCreateCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(levelInfo.getGroupName()));
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));
        vo.setParallel(fetchParallel(vo));
        // 可否直接結案
        vo.setIsCloseForm(verifyIsFormClose(vo));
        //是否為審核者
        vo.setIsApprover(isApprover(vo));
        // 是否為平行會辦期間
        vo.setIsParallel(isParalleling(vo));
        // 判斷該登入者是否有具有審核資格
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        //是否為申請流程最後一關
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getFormClass(), vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
        vo.setIsEctExtended(StringConstant.SHORT_YES.equals(vo.getIsExtended()));
        
        setLastJobAct(vo);
    }

    @Override
    public void sendToVerification (CountersignedFormVO vo) {
        ResourceEnum resource;
        Date today = new Date();
        vo.setCreateTime(today);
        vo.setAssignTime(today);
        vo.setFormStatus(FormEnum.ASSIGNING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());

        resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
        Map<String, Object> params = new HashMap<>();
        params.put("detailId", vo.getDetailId());
        params.put("processOrder", 2);
        Map<String, Object> pojo = jdbcRepository.queryForMap(resource, params);
        vo.setGroupSolving(MapUtils.getString(pojo, "GroupId"));
        
        mergeFormInfo(vo);// 更新表單
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        form.setCreateTime(today);
        formRepo.save(form);
        vo.setCompleteTime(today);
        vo.setUserId(UserInfoUtil.loginUserId());
        vo.setGroupId(fetchLoginUser().getGroupId());

        // 進入指派人員階段
        saveLevel(vo, "經辦處理", FormEnum.APPLY, FormEnum.SENT);
        vo.setUserId(null);
        vo.setVerifyLevel("1");
        vo.setCompleteTime(null);
        vo.setVerifyType(FormEnum.APPLY.name());
        vo.setGroupId(MapUtils.getString(getProcessLevel(
                vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
        saveLevel(vo, 1, FormEnum.APPLY, FormEnum.PENDING);
    }

    @Override
    public CountersignedFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), CountersignedFormVO.class);
    }

    @Override
    public void prepareVerifying(CountersignedFormVO vo) {
        prepareVerifyWording(vo);
    }
    
    @Override
    public void verifying (CountersignedFormVO vo) {
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
        }

        ResourceEnum resource;
        Conditions conditions;
        List<Integer> limits = calculateLimitNumber(vo);
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());

        // 跳關以及新增下關
        switch (verifyType) {
            case APPLY:
                resource = formHelper.getApplyLimitResource(formClass);
                conditions = new Conditions()
                        .and().equal("PROCESS.detailId", vo.getDetailId())
                        .leftPT(SQL.AND)
                        .gt("PROCESS.processOrder", String.valueOf(limits.get(0)))
                        .and()
                        .ltEqual("PROCESS.processOrder", String.valueOf(limits.get(1)))
                        .RightPT();
                List<Map<String, Object>> applyList = jdbcRepository.queryForList(resource, conditions);
                applyVerification(vo, applyList);

                break;

            case REVIEW:
                resource = formHelper.getReviewLimitResource(formClass);
                conditions = new Conditions()
                        .and().equal("PROCESS.detailId", vo.getDetailId())
                        .leftPT(SQL.AND)
                        .gt("PROCESS.processOrder", String.valueOf(limits.get(0)))
                        .and()
                        .ltEqual("PROCESS.processOrder", String.valueOf(limits.get(1)))
                        .RightPT();
                List<Map<String, Object>> reviewList = jdbcRepository.queryForList(resource, conditions);
                reviewVerification(vo, reviewList);

                break;

            default:
                break;
        }
    }
    
    @Override
    public void createVerifyCommentByVice(CountersignedFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // 系統原本更新關卡資料的邏輯
            updateCurrentLevel(vo);
        }
    }

    @Override
    public boolean verifyStretchForm (CountersignedFormVO vo, FormVerifyType type) {
        if(FormEnum.INC_C.toString().equals(vo.getFormClass())){
            return isStretchFormClosed(vo,true);
        }else {
            return isStretchFormClosed(vo);
        }
    }

    @Override
    public void deleteForm (CountersignedFormVO vo) {
        deleteFormTables(vo.getFormId());
    }

    @Override
    public void mergeFormInfo (CountersignedFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();

        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// 新增
            BaseFormVO newerDetail = newerProcessDetail(vo.getDivisionSolving(), FormEnum.valueOf(vo.getFormClass()));
            BeanUtil.copyProperties(newerDetail, vo);
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.valueOf(vo.getFormClass())));
            vo.setFormStatus(FormEnum.PROPOSING.name());
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoCDetailEntity formDetailPojo = new FormInfoCDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formDetailPojo.setUnit(vo.getUnitId());
            formDatePojo.setCreateTime(vo.getAssignTime());

            formRepo.save(formPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
            
            FormInfoDateEntity infoDate = formDateRepo.findByFormId(vo.getFormId());
            formDateRepo.updateOectByFormId(infoDate.getMect(), vo.getFormId());

            vo.setVerifyType(null);
            vo.setVerifyLevel(null);
            vo.setIsCreateCIssue(null);
            vo.setIsModifyColumnData(null);
            vo.setIsCreateChangeIssue(null);
            vo.setIsVerifyAcceptable(false);
        } else {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_C.getResource("UPDATE_C_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
            saveFormAlterResult(formId, true);
        }
    }

    @Override
    public void saveProgram (CountersignedFormVO vo) {
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
    public CountersignedFormVO getProgram (String formId) {
        FormProgramEntity pojo = programRepo.findByFormId(formId);
        CountersignedFormVO vo = new CountersignedFormVO();
        BeanUtil.copyProperties(pojo, vo);

        return vo;
    }

    @Override
    public boolean isVerifyAcceptable (CountersignedFormVO vo, SysUserVO userInfo) {
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

        if (FormEnum.REVIEW.name().equals(vo.getVerifyType())) {
            resource = formHelper.getReviewGroupResource(FormEnum.valueOf(vo.getFormClass()));
        } else {
            resource = formHelper.getApplyGroupResource(FormEnum.valueOf(vo.getFormClass()));
        }
        
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
    public boolean isFormClosed (CountersignedFormVO vo) {
        return isClosed(vo.getFormId());
    }

    @Override
    public boolean isNewerDetailExist (CountersignedFormVO vo) {
        String division = vo.getDivisionSolving();
        FormEnum e = FormEnum.valueOf(vo.getFormClass());

        return newerProcessDetail(division, e) == null;
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
    public boolean isAllStretchDied (CountersignedFormVO vo) {
        return super.isAllStretchDied(vo);
    }

    @Override
    protected String getReviewLastLevel (String detailId) {
        return "";
    }

    @Override
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        int count = 0;

        if(FormEnum.APPLY.name().equalsIgnoreCase(verifyType)) {
            FormEnum clazz = FormEnum.valueOf(formClazz);
        
            if (FormEnum.Q_C == clazz) {
                count = qApplyRepo.countByDetailId(detailId);
            } else if (FormEnum.SR_C == clazz) {
                count = srApplyRepo.countByDetailId(detailId);
            } else if (FormEnum.INC_C == clazz) {
                count = incApplyRepo.countByDetailId(detailId);
            }
        }

        return (count == Integer.parseInt(verifyLevel)) ? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
    }

    private String passDivision (String division) {
        String compare = "";
        List<HtmlVO> voList = sysGroupService.getSysGroupSelectorReverse();
        
        for (HtmlVO htmlVO : voList) {
            compare = htmlVO.getWording();
            if (compare.equals(division)) {
                compare = htmlVO.getValue();
                break;
            }
        }
        
        return compare;
    }

    private boolean isNotPrepared (CountersignedFormVO vo) {
        return !(FormEnum.APPROVING.name().equals(vo.getFormStatus()) ||
                FormEnum.ASSIGNING.name().equals(vo.getFormStatus()));
    }

    private void setUpFormProcess (CountersignedFormVO vo) {
        boolean isAssigning = vo.getIsAssigning();
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        
        if (isAssigning) {
            vo.setFormStatus(FormEnum.APPROVING.name());
            BeanUtil.copyProperties(vo, form);
            formRepo.save(form);
        }
    }

    /*
     * 1. 新增待申請關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否轉換為審核流程
     */
    private void applyVerification (CountersignedFormVO vo, List<Map<String,Object>> jumpList) {
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
        int processOrder;
        int size = jumpList.size();
        
        if (!isParallel) {
            FormEnum verifyResult = null;
            Map<String,Object> apply = jumpList.get(size-1);
            processOrder = MapUtils.getInteger(apply, "ProcessOrder");
    
            vo.setCompleteTime(null);
            vo.setGroupId(MapUtils.getString(apply, "GroupId"));
            if (vo.getIsNextLevel()) {
                vo.setUserId(vo.getIsAssigning() ?
                        formRepo.findByFormId(vo.getFormId()).getUserSolving() : null);
                verifyResult = processOrder == 1 ? FormEnum.SENT : FormEnum.PENDING;
                addNextLevel(processOrder, verifyResult, vo, apply);
            } else {// 寫入關卡前取得前一關資訊
                addBackLevel(vo, processOrder);
            }
        }
    }

    /*
     * 1. 新增待審核關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否可結案
     */
    private void reviewVerification (CountersignedFormVO vo, List<Map<String, Object>> jumpList) {
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
        int processOrder;
        int size = jumpList.size();
        Map<String, Object> reviewPojo = jumpList.get(size-1);
        processOrder = MapUtils.getInteger(reviewPojo, "ProcessOrder");
        
        vo.setCompleteTime(null);
        if (vo.getIsNextLevel()) {
            vo.setUserId(null);
            vo.setGroupId(MapUtils.getString(reviewPojo, "GroupId"));
            saveLevel(vo, processOrder, FormEnum.REVIEW, FormEnum.PENDING);
        } else {
            // 寫入關卡前取得前一關資訊
            FormVerifyLogEntity back = verifyRepo
                    .findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), vo.getJumpLevel(), vo.getVerifyType());
            // 寫入關卡
            vo.setUserId(null); // 退回群組
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, processOrder, FormEnum.REVIEW, FormEnum.PENDING);
            // 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
            vo.setUserId(back.getUserId());
        }
    }

    // 新增下一關
    private void addNextLevel (
            int processOrder,
            FormEnum verifyResult,
            CountersignedFormVO vo,
            Map<String, Object> apply) {
        boolean isParallel = StringConstant.SHORT_YES.
                    equals(MapUtils.getString(apply, "IsParallel"));
        
        if (isParallel) {
            saveParallels(vo, apply, processOrder, verifyResult);
        } else {
            vo.setParallel(null);
            saveLevel(vo, processOrder, FormEnum.APPLY, verifyResult);
        }
        
        vo.setIsParallel(isParallel ?
                StringConstant.SHORT_YES : StringConstant.SHORT_NO);
    }

    private void saveParallels (CountersignedFormVO vo, Map<String, Object> apply, int processOrder, FormEnum verifyResult) {
        CountersignedFormVO tempVO = new CountersignedFormVO();
        FormInfoCDetailEntity detail = formDetailRepo.findByFormId(vo.getFormId());
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

    // 新增退回關卡
    private void addBackLevel (CountersignedFormVO vo, int processOrder) {
        boolean isInTheList = false;
        boolean isParallel = !StringUtils.isBlank(vo.getParallel());
        ResourceEnum resource = ResourceEnum.
                SQL_FORM_OPERATION.getResource("FIND_DISTINCT_BACK_LIST");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyType", vo.getVerifyType());
        params.put("verifyLevel", vo.getJumpLevel());
        List<FormVerifyLogEntity> backList = jdbcRepository.
                queryForList(resource, params, FormVerifyLogEntity.class);
        List<String> parallelIds = fetchParallelIds(vo.getFormId(), isParallel);
        
        List<String> recipients = new ArrayList<>();
        deprecatedParallelChildFroms(vo, isParallel);
        for (FormVerifyLogEntity back : backList) {
            isInTheList = parallelIds.contains(back.getUserId());
            isParallel = !StringUtils.isBlank(back.getParallel());

            if (!isParallel) {
                vo.setUserId(null);
            } else {
                if (!isInTheList) {
                    continue;
                }
                vo.setUserId(back.getUserId());
            }
            
            vo.setParallel(back.getParallel());
            saveLevel(vo, processOrder, FormEnum.APPLY, FormEnum.PENDING);
            recipients.add(back.getUserId());
            
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
    private List<String> fetchParallelIds (String formId, boolean isParallel) {
        List<String> names = new ArrayList<>();
        
        if (isParallel) {
            FormInfoCDetailEntity detail = formDetailRepo.findByFormId(formId);
            List<HtmlVO> spcs = BeanUtil.fromJsonToList(detail.getSpcGroups(), HtmlVO.class);
            for (HtmlVO spc : spcs) {
                names.add(spc.getUserId());
            }
        }
        
        return names;
    }

    @Override
    public CountersignedFormVO getFormDetailInfo (String formId) {
        return this.getVariousCFormDetailInfo(formId);
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(CountersignedFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(CountersignedFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    public void notifyProcessMail (CountersignedFormVO vo) {
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        return null;
    }

}
