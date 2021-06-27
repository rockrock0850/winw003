package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.entity.FormJobBatchEntity;
import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;
import com.ebizprise.winw.project.entity.FormJobCountersignedEntity;
import com.ebizprise.winw.project.entity.FormJobDivisionMappingEntity;
import com.ebizprise.winw.project.entity.FormJobSegmentListEntity;
import com.ebizprise.winw.project.entity.FormJobWorkingEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormJobBatchRepository;
import com.ebizprise.winw.project.repository.IFormJobCheckPersonRepository;
import com.ebizprise.winw.project.repository.IFormJobCountersignedRepository;
import com.ebizprise.winw.project.repository.IFormJobDivisionMappingRepository;
import com.ebizprise.winw.project.repository.IFormJobSegmentRepository;
import com.ebizprise.winw.project.repository.IFormJobWorkingRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.service.ICommonJobFormService;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.CommonCheckPersonVO;
import com.ebizprise.winw.project.vo.CommonJobFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobApplyVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.SegmentVO;

@Service("commonJobFormService")
public class CommonJobFormServiceImpl extends BaseService implements ICommonJobFormService {

    @Autowired
    private IFormJobCountersignedRepository formJobCountersignedRepository;

    @Autowired
    private IFormJobBatchRepository formJobBatchRepository;
    
    @Autowired
    private IFormJobDivisionMappingRepository divisionMapRepo;
    
    @Autowired
    protected JdbcRepositoy jdbcRepository;
    
    @Autowired
    private IFormJobCheckPersonRepository formJobCheckPersonRepository;
    
    @Autowired
    private IFormProcessDetailApplyJobRepository applyJobRepo;

    @Autowired
    private IFormProcessDetailReviewJobRepository reviewJobRepo;

    @Autowired
    private IFormJobWorkingRepository workingRepo;

    @Autowired
    private IFormJobSegmentRepository segmentRepo;

    @Autowired
    private IFormFileRepository formFileRepo;

    @Override
    public boolean hasWorkLevel (String detailId, String verifyType) {
        if (StringUtils.isBlank(verifyType)) {
            return false;
        }
        
        int count = 0;
        FormEnum type = FormEnum.valueOf(verifyType);
        if (FormEnum.APPLY == type) {
            count = applyJobRepo.countByDetailIdAndIsWorkLevel(detailId, StringConstant.SHORT_YES);
        } else {
            count = reviewJobRepo.countByDetailIdAndIsWorkLevel(detailId, StringConstant.SHORT_YES);
        }
        
        return (count > 0);
    }

    @Override
    public List<FormProcessManagmentJobApplyVO> getJobWorkItems (BaseFormVO vo, boolean isVerify) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_CHECK_PERSON_LIST_BY_CONDITION");

        Conditions conditions = new Conditions();
        conditions.and().equal("T1.FormId", vo.getFormId());
        conditions.and().equal("T2.detailId", vo.getDetailId());
        conditions.and().equal("T2.IsWorkLevel", StringConstant.SHORT_YES);
        
        if (isVerify) {
            conditions.and().notNull("T5.UserId");
        }
        
        List<FormProcessManagmentJobApplyVO> checkPersionList = 
                jdbcRepository.queryForList(resource, conditions, FormProcessManagmentJobApplyVO.class);
        
        return checkPersionList;
    }
    
    @Override
    public Map<String, Object> checkJobPeasonExistByFormId(String formId) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_CHECK_PERSON_INFO");

        Map<String, Object> params = new HashMap<>();
        params.put("formId", formId);

        List<LdapUserVO> userInfoList = jdbcRepository.queryForList(resource, params, LdapUserVO.class);

        StringBuilder errMsg = new StringBuilder();
        Map<String, Object> resultMp = new HashMap<>();

        for (LdapUserVO user : userInfoList) {
            if (user.getIsEnabled().equals(StringConstant.SHORT_NO)) {
                errMsg.append("作業關卡員工: " + user.getName() + " 帳號已停用，請重新填選!!\r\n");
            }
        }
        
        resultMp.put("isSuccess", !(errMsg.length() > 0));
        resultMp.put("errorMsg", errMsg.toString());
        
        return resultMp;
    }

    @Override
    public CommonJobFormVO getCountersignedDetail(String formId, String division) {
        CommonJobFormVO vo = new CommonJobFormVO();
        BeanUtil.copyProperties(
            formJobCountersignedRepository.findByFormIdAndDivision(formId, division), vo);
        return vo;
    }
    
    @Override
    public void saveCountersignedDetail(CommonJobFormVO vo) {
        FormJobCountersignedEntity entity = 
                formJobCountersignedRepository.findByIdAndFormId(vo.getId(), vo.getFormId());

        if (entity == null) {
            vo.setId(null);
            entity = new FormJobCountersignedEntity();
        }
        
        BeanUtil.copyProperties(vo, entity, true);
        entity.setCreatedAt(new Date());
        entity.setCreatedBy(UserInfoUtil.loginUserId());
        entity.setUpdatedAt(entity.getCreatedAt());
        entity.setUpdatedBy(entity.getCreatedBy());
        BeanUtil.copyProperties(formJobCountersignedRepository.save(entity), vo);
    }

    @Override
    public CommonJobFormVO getJobBatchDetail (CommonJobFormVO vo) {
        BeanUtil.copyProperties(
                formJobBatchRepository.findByFormId(vo.getFormId()), vo);
        return vo;
    }

    @Override
    public void saveJobBatchDetail (CommonJobFormVO vo) {
        FormJobBatchEntity entity = 
                formJobBatchRepository.findByIdAndFormId(vo.getId(), vo.getFormId());
        
        if (entity == null) {
            vo.setId(null);
            entity = new FormJobBatchEntity();
        }
        
        BeanUtil.copyProperties(vo, entity, true);
        entity.setCreatedAt(new Date());
        entity.setCreatedBy(UserInfoUtil.loginUserId());
        entity.setUpdatedAt(entity.getCreatedAt());
        entity.setUpdatedBy(entity.getCreatedBy());
        BeanUtil.copyProperties(formJobBatchRepository.save(entity), vo);
    }

    @Override
    public CommonJobFormVO isSecurityDeptTabs (String department) {
        CommonJobFormVO vo = new CommonJobFormVO();
        String divisions = FormJobEnum.SECURITY.wording();
        String securityDept = FormJobEnum.SECURITY.symbol();
        vo.setDivision(divisions);
        vo.setIsApTabs(!department.equals(securityDept));
        
        return vo;
    }

    @Override
    public List<String> getJobDivisionTabList(String division) {
        List<String> rtnLs = new ArrayList<>();
        
        //若為A01419-OA 的格式,則取 - 號後面的科別代碼
        if(division.indexOf("-") != -1) {
            division = division.split("-")[1];
        }
        
        List<FormJobDivisionMappingEntity> dataLs = 
                divisionMapRepo.findByDivision(division);
        
        for(FormJobDivisionMappingEntity entity : dataLs) {
            rtnLs.add(entity.getJobTabName());
        }
        
        return rtnLs;
    }
    
    @Override
	public List<String> getInternalProcessJobDivisionTabList(String formId) {
    	List<String> rtnLs = new ArrayList<>();
    	
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INTERNAL_PROCESS_TAB");
        Conditions conditions = new Conditions();
        conditions.and().equal("fips.FormId", formId);
        
        List<FormJobDivisionMappingEntity> entities = jdbcRepository.queryForList(resource, conditions, FormJobDivisionMappingEntity.class);
        
    	for(FormJobDivisionMappingEntity entity : entities) {
    		rtnLs.add(entity.getJobTabName());
    	}
    	
    	return rtnLs;
    }
    
    @Override
    @Transactional
    public void saveCheckPerson (CommonJobFormVO vo) {
        Date today = new Date();
        formJobCheckPersonRepository.deleteByFormId(vo.getFormId());
        formJobCheckPersonRepository.flush();

        List<FormJobCheckPersonListEntity> list = new ArrayList<FormJobCheckPersonListEntity>();
        
        if(CollectionUtils.isNotEmpty(vo.getCheckPersonList())) {
            for (CommonCheckPersonVO checkperson : vo.getCheckPersonList()) {
                FormJobCheckPersonListEntity pojo = new FormJobCheckPersonListEntity();
                pojo.setFormId(vo.getFormId());
                pojo.setSort(checkperson.getOrder());
                pojo.setUpdatedAt(today);
                BeanUtil.copyProperties(checkperson, pojo);
                list.add(pojo);
            }
            formJobCheckPersonRepository.saveAll(list);
        }
    }
    
    @Override
    public void validatePersonIsExist (String formId, DataVerifyUtil verifyUtil) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_CHECK_PERSON_INFO");

        Map<String, Object> params = new HashMap<>();
        params.put("formId", formId);
        List<LdapUserVO> userInfoList = jdbcRepository.queryForList(resource, params, LdapUserVO.class);
        
        if (CollectionUtils.isEmpty(userInfoList)) {
            verifyUtil.append("請選擇作業關卡員工!");
        }
    }

    @Override
    public void validatePersonList (CommonJobFormVO vo, List<CommonCheckPersonVO> persons, DataVerifyUtil verifyUtil) {
        FormEnum clazz = FormEnum.valueOf(vo.getFormClass());
        
        switch (clazz) {
            case JOB_SP:
                hasTestPerson(persons, verifyUtil);
                break;

            case JOB_SP_C:
                hasUserSolving(vo, persons, verifyUtil);
                break;

            default:
                // AP or AP_C aren't validating.
                break;
        }
    }

    @Override
    public CommonJobFormVO getJobWorkingDetail (CommonJobFormVO vo) {
        FormJobWorkingEntity working = workingRepo.
                findByFormIdAndType(vo.getFormId(), vo.getType());
        BeanUtil.copyProperties(working, vo);
        List<FormJobSegmentListEntity> entitys = 
                segmentRepo.findByFormIdAndType(vo.getFormId(), vo.getType());
        List<SegmentVO> segemtns = BeanUtil.copyList(entitys, SegmentVO.class);
        vo.setSegments(segemtns);
        
        return vo;
    }

    @Override
    public void mergeJobWorkingDetail (CommonJobFormVO vo) {    
        mergeWorkingData(vo);
        mergeWorkingSubData(vo);
    }

    @Override
    public void mergeJobWorkingDetail (List<Long> ids) {
        for (Long id : ids) {
            if (segmentRepo.countById(id) > 0) {
                segmentRepo.deleteById(id);
            }
        }
    }

    // 新增/儲存 頁簽資料
    private void mergeWorkingData (CommonJobFormVO vo) {
        Long id = vo.getId();
        Date today = new Date();
        String formId = vo.getFormId();
        String sourceId = vo.getSourceId();
        String userId = fetchLoginUser().getUserId();
        FormJobWorkingEntity working = 
                workingRepo.findByIdAndFormId(id, formId);
        
        if (working == null) {// 代表是新資料
            vo.setId(null);
            vo.setUpdatedAt(today);
            vo.setCreatedAt(today);
            vo.setUpdatedBy(userId);
            vo.setCreatedBy(userId);
            working = new FormJobWorkingEntity();
        } else {
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(userId);
        }
        
        BeanUtil.copyProperties(vo, working, true);
        BeanUtil.copyProperties(
                workingRepo.save(working), vo);
        overrideDbTempFile(formId, sourceId);
    }

    // 新增/儲存 頁簽裡面的清單的資料
    private void mergeWorkingSubData (CommonJobFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = fetchLoginUser().getUserId();
        List<SegmentVO> segments = vo.getSegments();
        
        if (!CollectionUtils.isEmpty(segments)) {
            Long id;
            String sFormId;
            
            for (SegmentVO segment : segments) {
                id = segment.getId();
                sFormId = segment.getFormId();
                
                FormJobSegmentListEntity entity = 
                        segmentRepo.findByIdAndFormId(id, sFormId);
                if (entity == null) {// 代表是新資料
                    segment.setId(null);
                    segment.setFormId(formId);
                    segment.setUpdatedAt(today);
                    segment.setCreatedAt(today);
                    segment.setUpdatedBy(userId);
                    segment.setCreatedBy(userId);
                } else {
                    segment.setUpdatedAt(today);
                    segment.setUpdatedBy(userId);
                }
            }
            
            List<FormJobSegmentListEntity> entitys = BeanUtil.
                    copyList(segments, FormJobSegmentListEntity.class);
            vo.setSegments(BeanUtil.
                    copyList(segmentRepo.saveAll(entitys), SegmentVO.class));
        }
    }

    // 若DB變更頁簽內有新增附件, 會再AP工作單新開AP會辦單的時候, 將附件存進資料庫併暫存。
    // 正式開單的時候再從這邊覆寫成正確的表單編號。
    private void overrideDbTempFile (String formId, String sourceId) {
        List<FormFileEntity> tempFiles = 
                formFileRepo.findByFormId(sourceId + "TEMP");
        
        if (CollectionUtils.isNotEmpty(tempFiles)) {
            for (FormFileEntity file : tempFiles) {
                file.setFormId(formId);
            }
            
            formFileRepo.saveAll(tempFiles);
        }
    }

    private void hasUserSolving (CommonJobFormVO vo, List<CommonCheckPersonVO> persons, DataVerifyUtil verifyUtil) {
        boolean hasUserSolving = false;
        
        for (CommonCheckPersonVO person : persons) {
            if (vo.getUserSolving().equals(person.getUserId())) {
                hasUserSolving = true;
                break;
            }
        }
        
        if (!hasUserSolving) {
            verifyUtil.append("審核人員清單中必須有一員為「處理人員」。");
        }
    }

    private void hasTestPerson (List<CommonCheckPersonVO> persons, DataVerifyUtil verifyUtil) {
        int testPerson = 0;
        int testerLimit = 1;
        
        for (CommonCheckPersonVO person : persons) {
            if (testPerson >= testerLimit) {// 測試人員一定要有兩位, 教育訓練告知。
                break;
            }
            
            if (isTPerson(person)) {
                testPerson++;
            }
        }
        
        if (testPerson < testerLimit) {
            verifyUtil.append("「" + FormJobEnum.TPERSON.wording() + "」項目必填1位。");
        }
    }

    private boolean isTPerson (CommonCheckPersonVO person) {
        return !StringUtils.isBlank(person.getUserId()) &&
                FormJobEnum.TPERSON.name().equals(person.getLevel());
    }
    
}
