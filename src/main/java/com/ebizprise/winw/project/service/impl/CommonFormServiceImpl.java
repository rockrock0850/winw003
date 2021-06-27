package com.ebizprise.winw.project.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.entity.FormImpactAnalysisEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormUserRecordEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.QuestionMaintainEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysCommonEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.jdbc.criteria.SQL;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormImpactAnalysisRepository;
import com.ebizprise.winw.project.repository.IFormInfoCDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoQDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoSrDetailRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormUserRecordRepository;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.repository.IQuestionMaintainRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.FormImpactAnalysisVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

@Transactional
@Service("commonFormService")
public class CommonFormServiceImpl extends BaseFormService<CommonFormVO> implements ICommonFormService {

    private static final Logger logger = LoggerFactory.getLogger(CommonFormServiceImpl.class);
    
    @Autowired
    private IFormUserRecordRepository formUserRecordRepository;
    
    @Autowired
    private ILdapUserRepository ldapUserRepository;
    
    @Autowired
    private IQuestionMaintainRepository questionMaintainRepository;
    
    @Autowired
    private IFormImpactAnalysisRepository formImpactAnalysisRepo;
    
    @Autowired
    private IFormRepository formRepo;
    
    @Autowired
    private FormHelper formHelper;
    
    @Autowired
    private IFormProcessDetailApplyJobRepository jobRepo;
    
    @Autowired
    private ISysGroupRepository sysGroupRepo;

    @Autowired
    private IFormFileRepository formFileRepo;
    
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    @Autowired
    private IFormInfoSrDetailRepository srFormDetailRepo;
    
    @Autowired
    private IFormInfoQDetailRepository qFormDetailRepo;
    
    @Autowired
    private IFormInfoIncDetailRepository incFormDetailRepo;
    
    @Autowired
    private IFormInfoCDetailRepository cFormDetailRepo;
    
    @Override
    public List<BaseFormVO> getFormLogs (BaseFormVO vo, SysUserVO userInfo) {
        List<BaseFormVO> finalList = new ArrayList<BaseFormVO>();
        
        //第一次排除「完成時間」為空值
        ResourceEnum excludeNullValue = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_VERIFY_LOGS_EXCLUDE_NULL_VALUE");
        Map<String, Object> params = new HashMap<>();
        Conditions conditions = new Conditions();
        conditions.orderBy("FVL.CompleteTime", SQL.ASC);
        params.put("formId", vo.getFormId());
        finalList.addAll(jdbcRepository.queryForList(excludeNullValue, conditions, params, BaseFormVO.class));
        
        //第二次只撈「完成時間」為空值
        ResourceEnum completeTimeIsNull = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_VERIFY_LOGS_BY_CONDITIONS");
        conditions = new Conditions();
        conditions.orderBy("FVL.Id", SQL.ASC);
        finalList.addAll(jdbcRepository.queryForList(completeTimeIsNull, conditions, params, BaseFormVO.class));
        
        for (BaseFormVO formLog : finalList) {
            setVerifyResultWording(formLog, userInfo);
        }
        
        return finalList; //審核結果為「待審」時，排序在最後一列
    }
    
    @Override
    public List<String> getEnabledButtons (CommonFormVO vo, SysUserVO userInfo) {
        SysUserVO loginUser = fetchLoginUser();
        FormEnum clazz = FormEnum.valueOf(vo.getFormClass());
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isNotCreateYet = StringUtils.isBlank(vo.getFormId());
        boolean isVice = sysUserService.isVice(loginUser.getGroupId());
        FormColumnValidate validate = formHelper.getFormColumnValidateHelper(clazz);
        boolean isDeprecated = FormEnum.DEPRECATED.name().equals(vo.getFormStatus());
        boolean isAdmin = UserEnum.ADMIN.wording().equalsIgnoreCase(loginUser.getUserId());
        
        if (isAdmin) {
            return validate.adminButtons();
        }
        
        if (isVice && !isDeprecated && !isVerifyAcceptable) {
            return validate.viceButtons(vo, userInfo);
        }
        
        if (isNotCreateYet) {
            return validate.initButtons(vo);
        }

        boolean isJobForm = clazz.name().contains(FormEnum.JOB.name());
        boolean isApJobForm = FormEnum.JOB_AP.name().equals(clazz.name());
        boolean isApJobCForm = FormEnum.JOB_AP_C.name().equals(clazz.name());
        boolean isApply = FormEnum.APPLY.name().equals(vo.getVerifyType());
        boolean isCsPersonLevel = FormJobEnum.CSPERSON.name().equals(vo.getWorkProject());

        // 如果是AP工作單內的會辦處理人員關卡就不給操作權限只給瀏覽。
        if (isApply && isApJobForm && isCsPersonLevel) {
            return new ArrayList<>();
        }
        
        if (isApply && isJobForm) {
            fetchJobWorkProjectName(vo);
        }
        
        GroupFunctionVO groupFunction = sysUserService.getLoginUserGroupFunction();

        List<String> buttons = new ArrayList<String>();
        FormEnum status = FormEnum.valueOf(vo.getFormStatus());
        
        switch (status) {
            case PROPOSING:
                buttons = validate.proposeButtons(vo, loginUser);
                break;
                
            case ASSIGNING:
            	if (isApJobCForm) {
            		buttons = validate.assignButtons(vo, loginUser, groupFunction);
            	} else {
            		buttons = validate.assignButtons(vo, loginUser);
            	}
                break;

            case WATCHING:
            case APPROVING:
                buttons = validate.approveButtons(vo, loginUser, groupFunction);
                break;

            default:
                break;
        }
        
        return buttons;
    }

    @Override
    public List<String> getDisabledCols (CommonFormVO vo) {
        SysUserVO loginUser = fetchLoginUser();
        List<String> columns = new ArrayList<String>();
        FormEnum clazz = FormEnum.valueOf(vo.getFormClass());
        boolean isNotCreateYet = StringUtils.isBlank(vo.getFormId());
        boolean isVice = sysUserService.isVice(loginUser.getGroupId());
        boolean isApJobForm = FormEnum.JOB_AP.name().equals(clazz.name());
        boolean isApply = FormEnum.APPLY.name().equals(vo.getVerifyType());
        FormColumnValidate validate = formHelper.getFormColumnValidateHelper(clazz);
        boolean isDeprecated = FormEnum.DEPRECATED.name().equals(vo.getFormStatus());
        boolean isCsPersonLevel = FormJobEnum.CSPERSON.name().equals(vo.getWorkProject());
        boolean isAdmin = UserEnum.ADMIN.wording().equalsIgnoreCase(loginUser.getUserId());

        if (isAdmin) {
            return columns;
        }
        
        if (isVice && !isDeprecated) {
            return columns;
            
        }

        // 如果是AP工作單內的會辦處理人員關卡就不給操作權限只給瀏覽。
        if (isApply && isApJobForm && isCsPersonLevel) {
            return validate.disableAllColumns(vo);
        }
        
        if (isNotCreateYet) {
            return validate.initColumns(vo);
        }

        FormEnum status = FormEnum.valueOf(vo.getFormStatus());
        
        switch (status) {
            case PROPOSING:
                columns = validate.proposeColumns(vo, loginUser);
                break;

            default:
                columns.addAll(validate.disableAllColumns(vo));
                break;
        }
        
        return columns;
    }

    @Override
    public List<String> getEnabledCols (CommonFormVO vo, SysUserVO userInfo) {
        SysUserVO loginUser = fetchLoginUser();
        List<String> columns = new ArrayList<String>();
        FormEnum clazz = FormEnum.valueOf(vo.getFormClass());
        boolean isEmptyType = StringUtils.isBlank(vo.getVerifyType());
        boolean isJobForm = clazz.name().contains(FormEnum.JOB.name());
        boolean isVice = sysUserService.isVice(loginUser.getGroupId());
        boolean isApJobForm = FormEnum.JOB_AP.name().equals(clazz.name());
        boolean isApply = FormEnum.APPLY.name().equals(vo.getVerifyType());
        FormColumnValidate validate = formHelper.getFormColumnValidateHelper(clazz);
        boolean isDeprecated = FormEnum.DEPRECATED.name().equals(vo.getFormStatus());
        boolean isCsPersonLevel = FormJobEnum.CSPERSON.name().equals(vo.getWorkProject());
        boolean isAdmin = UserEnum.ADMIN.wording().equalsIgnoreCase(loginUser.getUserId());

        if (isAdmin) {
            return validate.adminColumns();
        }
        
        if (isVice && !isDeprecated) {
            return validate.viceColumns(vo, userInfo);
        }

        // 如果是AP工作單內的會辦處理人員關卡就不給操作權限只給瀏覽。
        if (isApply && isApJobForm && isCsPersonLevel) {
            return new ArrayList<>();
        }
        
        if (!isEmptyType) {
            if (isApply && isJobForm) {
                fetchJobWorkProjectName(vo);
            }
            
            GroupFunctionVO groupFunc = sysUserService.getLoginUserGroupFunction();
            
            FormEnum status = FormEnum.valueOf(vo.getFormStatus());
            if (FormEnum.APPROVING == status || FormEnum.ASSIGNING == status || FormEnum.WATCHING == status) {
                switch (FormEnum.valueOf(vo.getVerifyType())) {
                    case APPLY:
                    	if (clazz.equalsAny(FormEnum.JOB_AP_C, FormEnum.Q)) {
                    		columns = validate.applyColumns(vo, loginUser, groupFunc);
                    	} else {
                    		columns = validate.applyColumns(vo, loginUser);
                    	}
                        break;

                    case REVIEW:
                        columns = validate.reviewColumns(vo, loginUser);
                        break;

                    default:
                        break;
                }
            }
        }
        
        return columns;
    }
    
    @Override
    public void saveFile (
            String type,
            String formId,
            String description,
            String alterContent,
            String layoutDataset,
            MultipartFile file) throws Exception {
        saveFormFile(
                type,
                formId,
                description,
                alterContent,
                layoutDataset,
                file);
    }
    
    @Override
    public List<CommonFormVO> getFiles (CommonFormVO vo) {
        String type = vo.getType();
        String formId = vo.getFormId();
        String sourceId = vo.getSourceId();
        boolean isDbTab = SysCommonEnum.DB.name().equals(type);
        
        List<FormFileEntity> pojoList = formFileRepo.
                    findByFormIdAndTypeOrderByCreatedAtDesc(formId, type);
        
        boolean isNewSubDbTab = isDbTab && StringUtils.isBlank(formId);
        
        if (isNewSubDbTab) {
            pojoList = formFileRepo.
                    findByFormIdAndTypeOrderByCreatedAtDesc(sourceId, type);
        }
        
        List<CommonFormVO> voList = BeanUtil.copyList(pojoList, CommonFormVO.class);
        
        if (isNewSubDbTab) {
            voList = cloneParentFile(voList, pojoList);
        }
        
        renderFileSize(voList, pojoList);
        
        return voList;
    }

    @Override
    public void deleteFiles (List<CommonFormVO> voList) {
        for (CommonFormVO vo : voList) {
            delFormFile(vo.getId(), vo.getFormId());
        }
    }

    @Override
    public File download (CommonFormVO vo) throws Exception {
        return downloadFile(vo.getId(), vo.getFormId());
    }

    @Override
    public List<CommonFormVO> getLogs (String formId) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_LOGS");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", formId);
        
        return jdbcRepository.queryForList(resource, params, CommonFormVO.class);
    }

    @Override
    public void deleteLogs (List<CommonFormVO> voList) {
        for (CommonFormVO vo : voList) {
            delFormUserRecord(
                    formUserRecordRepository.findByIdAndFormId(vo.getId(), vo.getFormId()));
        }
    }

    @Override
    public void saveLogs (List<CommonFormVO> voList) {
        Date currentDate = new Date();
        boolean isAdmin = UserEnum.ADMIN.wording()
                    .equalsIgnoreCase(fetchLoginUser().getUserId());
        
        for (CommonFormVO vo : voList) {
            vo.setUpdatedAt(currentDate);
            
            if (!isAdmin) {
                vo.setUpdatedBy(UserInfoUtil.loginUserId());
            }
            
            FormUserRecordEntity pojo = new FormUserRecordEntity();
            BeanUtil.copyProperties(vo, pojo);
            saveOrUpdateFormUserRecord(pojo);
        }
    }

    @Override
    public List<LdapUserVO> getLdapUserByUserName(String userName) {
        List<LdapUserVO> rtnLs = new ArrayList<>();
        
        if(StringUtils.isNoneBlank(userName)) {
            LdapUserVO vo = new LdapUserVO();
            List<LdapUserEntity> entityLs = ldapUserRepository.findByName(userName);
            
            for(LdapUserEntity entity : entityLs) {
                BeanUtil.copyProperties(entity, vo);
                rtnLs.add(vo);
            }
        }
        
        return rtnLs;
    }
    
    @Override
    public List<FormImpactAnalysisVO> getFormImpactAnalysis () {
        return findAllQuestions();
    }
    
    @Override
    public List<FormImpactAnalysisVO> getFormImpactAnalysis(String formId) {
        List<FormImpactAnalysisVO> impactList = new ArrayList<>();
        
        if (StringUtils.isBlank(formId)) {
            impactList = findAllQuestions();
        } else {
            List<FormImpactAnalysisEntity> existList = formImpactAnalysisRepo.findByFormIdOrderByIdAsc(formId);
            
            if (CollectionUtils.isEmpty(existList)) {
                impactList = findAllQuestions();
                logger.warn("無法取得任何衝擊分析資訊!");
            } else {
                for (FormImpactAnalysisEntity e : existList) {
                    FormImpactAnalysisVO vo = new FormImpactAnalysisVO();
                    BeanUtil.copyProperties(e, vo);
                    
                    if (StringUtils.isNoneBlank(e.getFraction())) {
                        vo.setFractionLs(Arrays.asList(e.getFraction().split(StringConstant.SEMICOLON)));
                    }
                    
                    impactList.add(vo);
                }
            }
            
        }
        
        return impactList;
    }

    @Override
    public void saveFormImpactAnalysis(FormImpactAnalysisVO vo) {
        String formId = vo.getFormId();
        
        if (StringUtils.isNotBlank(formId)) {
            Date currentDate = new Date();
            String currentUser = UserInfoUtil.loginUserId();

            List<FormImpactAnalysisVO> impactList = vo.getImpactList();
            List<FormImpactAnalysisEntity> entityLs = new ArrayList<>();
            
            //先刪除該表單的衝擊分析資訊
            formImpactAnalysisRepo.deleteByFormId(formId);
            formImpactAnalysisRepo.flush();
            
            //然後在Insert資料
            for(FormImpactAnalysisVO targetVo : impactList) {
                FormImpactAnalysisEntity entity = new FormImpactAnalysisEntity();
                BeanUtil.copyProperties(targetVo, entity);
                entity.setFormId(formId);
                entity.setCreatedAt(currentDate);
                entity.setCreatedBy(currentUser);
                entity.setUpdatedAt(currentDate);
                entity.setUpdatedBy(currentUser);
                
                entityLs.add(entity);
            }
            
            formImpactAnalysisRepo.saveAll(entityLs);
        }
    }
    
    /**
     * 傳入formId,查詢該表單的關聯資訊
     * 
     * @param formId
     * @return List
     */
    @Override
    public List<BaseFormVO> getFormRelationship(String formId) {
        Conditions conditions = new Conditions();
        List<BaseFormVO> resultLs = new ArrayList<>();
        
        List<String> linkFormIdLs = new ArrayList<>();
        List<BaseFormVO> childFormLs = getChildFormList(formId);
        List<BaseFormVO> parentFormLs = getSourceFormList(formId);
        
        if(CollectionUtils.isNotEmpty(parentFormLs)) {
            for(BaseFormVO formVO : parentFormLs) {
                linkFormIdLs.add(formVO.getFormId());
            }
        }
        
        if(CollectionUtils.isNotEmpty(childFormLs)) {
            for(BaseFormVO formVO : childFormLs) {
                linkFormIdLs.add(formVO.getFormId());
            }
        }
        
        /**
         * 因關聯表單數不太可能超過SQLServer in 上限
         * 若真發生此問題,則需要將上述兩段SQL,整合進FIND_FORM_LINK_LIST
         * 在將該SQL的查詢結果,整合進TempTable中,在使用F.FormId IN的方式,到該TempTable去對應所需的值
         * 以上做法會增加可讀性的難度,請確認有發生問題後在調整
         */
        if(CollectionUtils.isNotEmpty(linkFormIdLs)) {
            // 用LAMBDA剔除linkFormIdLs當前formId的資料, 避免前端畫面重複顯示資料
            // 不建議大量使用LAMBDA, 除非邏輯非常簡單, 不然建議使用forEach來進行處理
            linkFormIdLs = linkFormIdLs.parallelStream().filter(target -> !target.equals(formId)).collect(Collectors.toList());
            conditions.and().in("F.FormId", linkFormIdLs);
            resultLs.addAll(jdbcRepository.queryForList(ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_LINK_LIST"), conditions, BaseFormVO.class));
        }
        
        return resultLs;
    }
    
    @Override
    public boolean isCounterSign(String formId) {
        FormEntity entity = formRepo.findByFormId(formId);
        if(!Objects.isNull(entity)) {
            if(FormEnum.SR_C.name().equals(entity.getFormClass()) ||
                    FormEnum.Q_C.name().equals(entity.getFormClass())  ||
                    FormEnum.INC_C.name().equals(entity.getFormClass()) ||
                    FormEnum.JOB_AP_C.name().equals(entity.getFormClass()) ||
                    FormEnum.JOB_SP_C.name().equals(entity.getFormClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPic() {
        String groupId = fetchLoginUser().getGroupId();
        SysGroupEntity sysGroupEneity = sysGroupRepo.findByGroupId(groupId);
        return "0".equals(sysGroupEneity.getAuthType());//若為0,代表是經辦
    }
    
    /**
     * 儲存Countersigneds
     * @param formId
     * @param formClass
     * @param countersigneds
     * @author bernard.yu
     */
    @Override
    public void updateCountersigneds (String formId, String formClass, String countersigneds) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        
        if (StringUtils.equals(FormEnum.SR.name(), formClass)) {
            srFormDetailRepo.updateCountersigneds(countersigneds, loginUser.getUserId(), today, formId);
        } else if (StringUtils.equals(FormEnum.Q.name(), formClass)) {
            qFormDetailRepo.updateCountersigneds(countersigneds, loginUser.getUserId(), today, formId);
        } else if (StringUtils.equals(FormEnum.INC.name(), formClass)) {
            incFormDetailRepo.updateCountersigneds(countersigneds, loginUser.getUserId(), today, formId);
        }
        
    };
    
    /**
     * 做廢表單
     * @param idList
     * @author bernard.yu
     */
    @Override
    public void deprecatedForms (List<String> formIdList) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        for (String formId: formIdList) {
            formRepo.updateStatusByFormId(formId, FormEnum.DEPRECATED.name(), loginUser.getUserId(), today);
        }
    };
    
    /**
     * 更新會辦科處理情況
     * @param formIdList
     * @author bernard.yu
     */
    @Override
    public void updateContent(List<String> formIdList) {
        Date today = new Date();
        SysUserVO loginUser = fetchLoginUser();
        String wording = getMessage("form.common.is.countersignds.wording.1");
        for (String formId: formIdList) {
            cFormDetailRepo.updateContent(wording, loginUser.getUserId(), today, formId);
        }
    };
    
    /**
     * 副科於非自身關卡修改並儲存表單後，新增一筆簽核紀錄
     * @author bernard.yu
     */
    @Override
    public void createVerifyCommentByVice(CommonFormVO vo) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (!isVerifyAcceptable) {
            saveLevel(vo);
        } else {
            // 系統原本更新關卡資料的邏輯
            updateCurrentLevel(vo);
        }
    }
    
    /**
    * 鎖定表單檔案狀態
    * @param formId
    * @author bernard.yu
    */
    @Override
    public void lockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_YES, formId);
    }
    
    /**
    * 寄送mail
    */
    @Override
    public void notifyProcessMail (CommonFormVO vo) {
        asyncMailLauncher(vo);
    }
    
    /**
    * 從FORM_INFO_SR_DETAIL,FORM_INFO_Q_DETAIL,FORM_INFO_INC_DETAIL中取summary
    * @param formId
    * @author bernard.yu
    */
    @Override
    public CommonFormVO getFormDetailInfo(String formId) {
        CommonFormVO vo = new CommonFormVO();
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_SUMMARY_FROM_TABLES");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formId", formId);
        List<BaseFormVO> resultList = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        for (BaseFormVO result:resultList) {
            vo.setSummary(result.getSummary());
        }
        
        return vo;
    }
    
    private List<FormImpactAnalysisVO> findAllQuestions () {
        List<FormImpactAnalysisVO> questions = new ArrayList<>();
        List<QuestionMaintainEntity> eList = questionMaintainRepository.findAll();
        
        for (QuestionMaintainEntity e : eList) {
            // 檢查是否啟用(Y or null)
            if (StringUtils.isBlank(e.getIsEnable()) || StringConstant.SHORT_YES.equals(e.getIsEnable())) {
                FormImpactAnalysisVO vo = new FormImpactAnalysisVO();
                BeanUtil.copyProperties(e, vo);
                vo.setFractionLs(Arrays.asList(e.getFraction().split(StringConstant.SEMICOLON)));
                questions.add(vo);
            }
        }
        
        return questions;
    }

    // 複製工作單上層表單的DB變更頁簽裡面的檔案並取得資料庫裏面新的Id還有新的表單編號
    private List<CommonFormVO> cloneParentFile (List<CommonFormVO> voList, List<FormFileEntity> pojoList) {
        if (CollectionUtils.isNotEmpty(voList)) {
            String formId = null;
            byte[] bytes;
            
            for (CommonFormVO file : voList) {
                formId = file.getFormId();
                formId = formId + "TEMP";
                file.setId(null);
                file.setFormId(formId);
            }
            
            List<FormFileEntity> cloneList =
                    BeanUtil.copyList(voList, FormFileEntity.class);
            formFileRepo.deleteByformId(formId);
            
            for (int i = 0; i < pojoList.size(); i++) {
                bytes = pojoList.get(i).getData();
                cloneList.get(i).setData(bytes);
            }
            
            cloneList = formFileRepo.saveAll(cloneList);
            
            voList = BeanUtil.copyList(cloneList, CommonFormVO.class);
        }
        
        return voList;
    }

    private void fetchJobWorkProjectName (CommonFormVO vo) {
        if (!StringUtils.isBlank(vo.getVerifyLevel())) {
            FormProcessDetailApplyJobEntity entity =
                    jobRepo.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
            vo.setUserName(entity.getWorkProject());
        }
    }
    
    // 將檔案容量轉成MB
    private void renderFileSize (List<CommonFormVO> voList, List<FormFileEntity> pojoList) {
        byte[] bytes;
        double data;
        
        for (int i = 0; i < voList.size(); i++) {
            bytes = pojoList.get(i).getData();
            data = (double) bytes.length / CommonStringUtil.MEGABYTE;
            voList.get(i).setData(CommonStringUtil.
                    numberFormat(data, CommonStringUtil.NUMBER_FORMAT3));
        }
    }
    
    /**
    * 從FORM_PROCESS_DETAIL_APPLY_SR,
    *   FORM_PROCESS_DETAIL_APPLY_Q,
    *   FORM_PROCESS_DETAIL_APPLY_INC中取groupId
    * @param detailId
    * @param processOrder
    * @return groupId
    * @author bernard.yu
    */
    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        String groupId = null;
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_GROUPID_FROM_APPLY_TABLES");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("detailId", detailId);
        params.put("processOrder", processOrder);
        List<BaseFormVO> resultList = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        for (BaseFormVO result:resultList) {
            groupId = result.getGroupId();
        }
        
        return groupId;
    }

    /**
    * 從FORM_PROCESS_DETAIL_REVIEW_SR,
    *   FORM_PROCESS_DETAIL_REVIEW_Q,
    *   FORM_PROCESS_DETAIL_REVIEW_INC中取groupId
    * @param detailId
    * @param processOrder
    * @return groupId
    * @author bernard.yu
    */
    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        String groupId = null;
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_GROUPID_FROM_REVIEW_TABLES");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("detailId", detailId);
        params.put("processOrder", processOrder);
        List<BaseFormVO> resultList = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        for (BaseFormVO result:resultList) {
            groupId = result.getGroupId();
        }
        
        return groupId;
    }
    

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(CommonFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(CommonFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String getReviewLastLevel (String detailId) {
        return null;
    }
    
}
