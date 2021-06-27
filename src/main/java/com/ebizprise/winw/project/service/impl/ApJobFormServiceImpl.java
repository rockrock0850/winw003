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
 * AP工作單 新增/修改/審核/退回等作業
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
     * 取得程式庫差異檔案查詢紀錄
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
     * 下載程式庫檔案
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
     * 儲存版控系統API回來的程式庫差異比較結果資訊
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
     * 儲存版控系統API回來的程式庫回傳的差異檔
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
        
        //先從來源主表,帶回相關資訊
        getParentMainFormDetail(vo);
        
        //變更單無勾選「未有修改程式」，所開立之AP工作單，預設勾選「程式上線」且可以更改
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
        
        ApJobFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, ApJobFormVO.class);

        vo.setFormWording(getMessage(FormEnum.JOB_AP.wording()));
        
        // 初始化表單基本資料
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

        // 取得該關卡資訊
        BeanUtil.copyProperties(levelInfo, vo);
        vo.setIsCreateJobCIssue(isSubCreation(vo.getIsCreateJobCIssue(), levelInfo.getGroupId()));
        vo.setIsModifyColumnData(isModifyInfo(vo.getIsModifyColumnData(), levelInfo.getGroupId()));
        
        String formStatusWording =
                StringConstant.SHORT_YES.equals(vo.getIsWorkLevel()) ?
                levelInfo.getWorkProjectName() : levelInfo.getGroupName();
        vo.setFormStatusWording(FormEnum.valueOf(vo.getFormStatus()).formStatus(formStatusWording));
        
        vo.setProcessStatusWording(
                String.format(FormEnum.valueOf(vo.getProcessStatus()).processStatus(), levelInfo.getGroupName()));

        // 判斷該登入者是否有具有審核資格
        vo.setIsVerifyAcceptable(isVerifyAcceptable(vo, fetchLoginUser()));
        //是否可直接結案
        vo.setIsCloseForm(verifyIsFormClose(vo));
        //是否為作業關卡的指定人員
        vo.setIsOwner(isOwner(vo));
        //是否為審核者
        vo.setIsApprover(isApprover(vo));
        //是否為申請流程最後一關
        vo.setIsApplyLastLevel(isApplyLastLevel(vo.getDetailId(), vo.getVerifyType(), vo.getVerifyLevel()));
        // 送交監督人員決定是否可直接結案
        isWithoutWatching(vo);
    }

    @Override
    public void sendToVerification (ApJobFormVO vo) {
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
                applyRepo.findLimitList(vo.getDetailId(), verifyLevel, jumpLevel, FormJobEnum.CSPERSON.name());

        // 申請只有一關的情況下, 會直接進入審核階段並初始化審核階段第一關的資料。
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
                /*
                 * 因為在PRO發生進入「程式進館員」關卡退關回第一關經辦的時候, 
                 * 抓不到關卡導致系統以為要進入審核流程的情況。
                 * 與客戶偕同測試發現, SQL1在PRO和UAT會抓不到資料要用SQL2才抓的到資料。
                 * 但很弔詭的是手動測試卻又正常, 所以只能先調整SQL的撰寫方法試試看能否解決問題。
                 * 1. 「SELECT * FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = '20201123141318707' AND (ProcessOrder > 0 AND ProcessOrder <= 1) AND WorkProject <> 'PPERSON'」
                 * 2. 「SELECT * FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = '20201123141318707' AND (ProcessOrder > 0 AND ProcessOrder <= 1) AND ISNULL(WorkProject, '') <> 'PPERSON'」
                 */
                ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("FIND_APPLY_LIMIT_LIST");
                
                Map<String, Object> params = new HashMap<>();
                params.put("detailId", vo.getDetailId());
                params.put("startLevel", limits.get(0));
                params.put("endedLevel", limits.get(1));
                params.put("workProject", FormJobEnum.CSPERSON.name());
                
                List<FormProcessDetailApplyJobEntity> applyList = jdbcRepository.queryForList(resource, params, FormProcessDetailApplyJobEntity.class);
                
                /*
                 * 工作單於申請流程進行審核的時候, 在程式進館員退回經辦(Apply 1)的時候
                 * 會因為applyList為空導致流程直接進入審核流程的第一關(副科長)
                 * P.S. SIT和UAT都無法復現, 因此補強Log觀察系統執行狀況。
                 */
                if (CollectionUtils.isNotEmpty(applyList)) {
                    logger.info("申請流程即將審核的關卡====");
                    for (FormProcessDetailApplyJobEntity e : applyList) {
                        logger.info("群組編號 : " + e.getGroupId());
                        logger.info("工作項目 : " + e.getWorkProject());
                        logger.info("流程關卡 : " + e.getProcessOrder());
                    }
                    logger.info("申請流程即將審核的關卡====");
                } else {
                    logger.info("即將跳入審核流程========");
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
            // 系統原本更新關卡資料的邏輯
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
     * 儲存處理方案的資料
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
     * 取得處理方案的資料
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
     * 副科/副理 表單直接結案
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
        // 因AP工作單沒有摘要，需由"作業目的"欄位資料取代，mail 才會有摘要資訊
        vo.setSummary(entity.getPurpose());
        
        return vo;
    }
    
    /**
     * 透過FormId取得該表單會辦科部門資訊
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
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity FormProcessDetailApplyJobEntity = applyRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return FormProcessDetailApplyJobEntity.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailApplyJobEntity FormProcessDetailApplyJobEntity = applyRepo.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));

        return FormProcessDetailApplyJobEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(ApJobFormVO vo) {
        // 查詢表單指定關卡的資訊
        FormProcessDetailReviewJobEntity FormProcessDetailReviewJobEntity = reviewRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getJumpLevel()));

        return FormProcessDetailReviewJobEntity.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        // 查詢表單指定關卡的資訊
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
     * AP工作單的會辦處理人員在表單送出之後要寄信通知
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
            params.put("formClass", formName);                                                     // 表單類型
            params.put("user", ldapUser.getName());                                                // 收信者名稱
            params.put("formId", vo.getFormId());                                                  // 表單編號
            params.put("link", getFormUrl(vo.getFormId()));                                        // 表單超鏈結
            params.put("divisionCreated", vo.getDivisionCreated());                                // 開單科別
            params.put("summary", getFormDetailInfo(vo.getFormId()).getSummary());                 // 摘要
            params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // 系統時間
            params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // 建立日期
            sendMail(subject, mailList, params);
        }
    }
    
    /**
     * 根據傳入的狀態,決定是否可直接結案</br>
     * P.S.
     * 若使用者勾選了送交監督人員, 則審核最後一關, 需要交由副理進行審查；</br>
     * 若無勾選, 則跳過該關卡, 允許直接結案。
     * @param vo
     */
    private ApJobFormVO isWithoutWatching (ApJobFormVO vo) {
        FormJobInfoApDetailEntity entiey = formDetailRepo.findByFormId(vo.getFormId());
        
        if ("REVIEW".equals(vo.getVerifyType()) &&
                StringConstant.SHORT_NO.equals(entiey.getIsWatching())) {
            int currentLevel = Integer.parseInt(vo.getVerifyLevel());
            List<FormProcessDetailReviewJobEntity> entityLs = reviewRepo.findByDetailId(vo.getDetailId());
            vo.setIsWithoutWatching((entityLs.size() - currentLevel) == 1);// 若兩者相減為1的話, 則代表只剩下需副理審核的最後一關
        }
        
        return vo;
    }

    private String genFilePath (String name) {
        return env.getProperty("form.file.download.dir") + File.separatorChar
        + DateUtils.getCurrentDate(DateUtils._PATTERN_YYYYMMDD) + File.separatorChar + name;
    }

    /*
     * 1. 新增待申請關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否轉換為審核流程
     */
    private void applyVerification (ApJobFormVO vo, List<FormProcessDetailApplyJobEntity> jumpList) {        
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
            verifyResult = FormEnum.PENDING;
            vo.setGroupId(back.getGroupId());
            saveLevel(vo, nextLevel.getProcessOrder(), FormEnum.APPLY, verifyResult);
            vo.setUserId(back.getUserId());// 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        }
        
    }

    /*
     * 1. 新增待審核關卡
     * 2. 判斷是否有跳關關卡
     * 3. 判斷是否可結案
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
    
    /**
     * 取得父主表單的資訊,帶入工作單中
     * 
     * @param formVO
     */
    private void getParentMainFormDetail(ApJobFormVO formVO) {
        List<BaseFormVO> parentFormLs = getSourceFormList(formVO.getSourceId());
        
        for(BaseFormVO target : parentFormLs) {
            String targetFormId = target.getFormId();
            //取得最上層的主表
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
     * 查詢 AP工作單+SP工作單；表單狀態=「審核中」「已結案」
     * @param vo
     * @return
     * @author jacky.fu
     */
    public List<ApJobFormVO> getJobForms (ApJobFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_JOB.getResource("FIND_JOB_FORMS_BY_CONDITION");
        Conditions conditions = new Conditions();
        // 表單編號
        if (StringUtils.isNotBlank(vo.getFormId())) {
            conditions.and().like("f.formId", vo.getFormId());
        }
        // 處理科別
        if (StringUtils.isNotBlank(vo.getDivisionSolving())) {
            conditions.and().like("f.DivisionSolving", vo.getDivisionSolving());
        }
        // 處理人員
        if (StringUtils.isNotBlank(vo.getUserSolving())) {
            conditions.and().like("f.UserSolving", vo.getUserSolving());
        }
        // 開單時間
        if (vo.getCreateTime() != null) {
            String dateStr = DateUtils.toString(vo.getCreateTime(), DateUtils._PATTERN_YYYYMMDD_SLASH);
            conditions.and().equal("CONVERT(date, f.CreateTime, 111)", dateStr);
        }
        // 作業目的
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
