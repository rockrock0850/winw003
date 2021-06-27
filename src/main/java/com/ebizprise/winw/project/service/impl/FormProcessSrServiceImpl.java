package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseProcessService;
import com.ebizprise.winw.project.entity.FormProcessDetailApplySrEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewSrEntity;
import com.ebizprise.winw.project.entity.FormProcessEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.jdbc.FormProcessManagmentJDBC;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplySrRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewSrRepository;
import com.ebizprise.winw.project.repository.IFormProcessRepository;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentSrApplyVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentSrFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentSrReviewVO;
import com.google.gson.JsonArray;

/**
 * 表單流程管理 共用服務
 * 
 * The <code>FormProcessServiceImpl</code>  
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月28日
 */
@Transactional
@Service("formProcessSrService")
public class FormProcessSrServiceImpl extends BaseProcessService implements IFormProcessManagmentService<FormProcessManagmentSrFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(FormProcessSrServiceImpl.class);
    
    @Autowired
    private IFormProcessRepository formProcessRepository;
    
    @Autowired
    private IFormProcessDetailApplySrRepository formProcessDetailApplyRepository;
    
    @Autowired
    private IFormProcessDetailReviewSrRepository formProcessDetailReviewRepository;
    
    @Autowired
    private FormProcessManagmentJDBC formProcessManagmentJDBC;

    @Override
    public boolean insertFormProcess(FormProcessManagmentSrFormVO vo) {
        FormProcessEntity formProcessEntity = new FormProcessEntity();
        String processId = DateUtils.getCurrentDate(DateUtils.pattern29);//用時間來作為ProcessId
        String detailId = DateUtils.getCurrentDate(DateUtils.pattern29);//用時間來作為申請以及審核流程的DetailId
        String departmentId = vo.getDivision().split(StringConstant.DASH)[0];
        String division = vo.getDivision().split(StringConstant.DASH)[1];
        Date currentDate = new Date();
        String currentUser = UserInfoUtil.loginUserId();
        
        formProcessEntity.setProcessId(processId);
        formProcessEntity.setFormType(Integer.parseInt(vo.getFormType()));
        formProcessEntity.setDivision(division);
        formProcessEntity.setDepartmentId(departmentId);
        formProcessEntity.setProcessName(vo.getProcessName());
        formProcessEntity.setIsEnable(StringConstant.SHORT_NO);//默認為DisEnable狀態
        formProcessEntity.setCreatedAt(currentDate);
        formProcessEntity.setCreatedBy(currentUser);
        formProcessEntity.setUpdatedAt(formProcessEntity.getCreatedAt());
        formProcessEntity.setUpdatedBy(formProcessEntity.getCreatedBy());

        formProcessRepository.save(formProcessEntity);
        
        List<FormProcessDetailApplySrEntity> applyEntityList = new ArrayList<>();
        for(FormProcessManagmentSrApplyVO target : vo.getApplyProcessList()) {
            FormProcessDetailApplySrEntity entity = new FormProcessDetailApplySrEntity();
            
            saveWording(target.getLevelWordings(),detailId,target.getProcessOrder(),FormEnum.APPLY);

            BeanUtil.copyProperties(target, entity);
            //以下資訊需額外寫程式輸入
            entity.setDetailId(detailId);
            entity.setProcessId(processId);
            entity.setCreatedAt(currentDate);
            entity.setCreatedBy(currentUser);
            entity.setUpdatedAt(entity.getCreatedAt());
            entity.setUpdatedBy(entity.getCreatedBy());
            
            applyEntityList.add(entity);
        }
        
        List<FormProcessDetailReviewSrEntity> reviewEntityList = new ArrayList<>();
        for(FormProcessManagmentSrReviewVO target : vo.getReviewProcessList()) {
            FormProcessDetailReviewSrEntity entity = new FormProcessDetailReviewSrEntity();
            
            saveWording(target.getLevelWordings(),detailId,target.getProcessOrder(),FormEnum.REVIEW);

            BeanUtil.copyProperties(target, entity);
            //以下資訊需額外寫程式輸入
            entity.setDetailId(detailId);
            entity.setProcessId(processId);
            entity.setCreatedAt(currentDate);
            entity.setCreatedBy(currentUser);
            entity.setUpdatedAt(entity.getCreatedAt());
            entity.setUpdatedBy(entity.getCreatedBy());
            
            reviewEntityList.add(entity);
        }
        
        formProcessDetailApplyRepository.saveAll(applyEntityList);//批次新增申請流程
        formProcessDetailReviewRepository.saveAll(reviewEntityList);//批次新增審核流程
        
        return true;
    }

    @Override
    public boolean updateFormProcess(FormProcessManagmentSrFormVO vo) {
        String detailId = DateUtils.getCurrentDate(DateUtils.pattern29);//用時間來作為申請以及審核流程的DetailId
        String processId = vo.getProcessId(); 
        String processName = vo.getProcessName();
        Date currentDate = new Date();
        String currentUser = UserInfoUtil.loginUserId();
        
        Map<String,Object> sqlParams = new HashMap<>();
        sqlParams.put("processId", processId);
        sqlParams.put("processName", processName);
        sqlParams.put("updatedBy", currentUser);
        sqlParams.put("updatedAt", currentDate);
        
        //一次update兩張表,先透過ProcessId,把該ID下的Apply & Review兩張table所關聯到的所有資料的ProcessId改掉
        formProcessManagmentJDBC.update(ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_FORM_PROCESS_SR_APPLY_AND_SR_REVIEW_PROCESS_ID_BY_PROCESS_ID"), sqlParams);
        
        //然後在執行更新流程名稱
        formProcessManagmentJDBC.update(ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_FORM_PROCESS_TABLE_PROCESS_NAME_BY_PROCESS_ID"),sqlParams);
        //再執行新增Apply & Review的內容,這邊的流程與Insert的完全一致
        List<FormProcessDetailApplySrEntity> applyEntityList = new ArrayList<>();
        for(FormProcessManagmentSrApplyVO target : vo.getApplyProcessList()) {
            FormProcessDetailApplySrEntity entity = new FormProcessDetailApplySrEntity();
            
            saveWording(target.getLevelWordings(),detailId,target.getProcessOrder(),FormEnum.APPLY);
            
            BeanUtil.copyProperties(target, entity);
            //以下資訊需額外寫程式輸入
            entity.setDetailId(detailId);
            entity.setProcessId(processId);
            entity.setCreatedAt(currentDate);
            entity.setCreatedBy(currentUser);
            entity.setUpdatedAt(entity.getCreatedAt());
            entity.setUpdatedBy(entity.getCreatedBy());
            
            applyEntityList.add(entity);
        }
        
        List<FormProcessDetailReviewSrEntity> reviewEntityList = new ArrayList<>();
        for(FormProcessManagmentSrReviewVO target :  vo.getReviewProcessList()) {
            FormProcessDetailReviewSrEntity entity = new FormProcessDetailReviewSrEntity();
            
            saveWording(target.getLevelWordings(),detailId,target.getProcessOrder(),FormEnum.REVIEW);
            
            BeanUtil.copyProperties(target, entity);
            //以下資訊需額外寫程式輸入
            entity.setDetailId(detailId);
            entity.setProcessId(processId);
            entity.setCreatedAt(currentDate);
            entity.setCreatedBy(currentUser);
            entity.setUpdatedAt(entity.getCreatedAt());
            entity.setUpdatedBy(entity.getCreatedBy());
            
            reviewEntityList.add(entity);
        }
        
        formProcessDetailApplyRepository.saveAll(applyEntityList);//批次新增申請流程
        formProcessDetailReviewRepository.saveAll(reviewEntityList);//批次新增審核流程
        
        return true;
    }

    @Override
    public FormProcessManagmentResultVO getFormProcessManagmentFormById(Long id) {
        FormProcessManagmentResultVO rtnVo = new FormProcessManagmentResultVO();
        List<FormProcessManagmentSrApplyVO> applyVoLs = new ArrayList<>();
        List<FormProcessManagmentSrReviewVO> reviewVoLs = new ArrayList<>();
        
        if(id > 0) {
            FormProcessEntity mainEntity = (formProcessRepository.findById(new Long(id))).get();
            String divisionText = mainEntity.getDepartmentId() + StringConstant.DASH + mainEntity.getDivision();
            rtnVo.setDivision(divisionText);
            rtnVo.setFormType(String.valueOf(mainEntity.getFormType()));
            rtnVo.setProcessName(mainEntity.getProcessName());
            rtnVo.setProcessId(mainEntity.getProcessId());
            rtnVo.setUpdatedAt(mainEntity.getUpdatedAt());
            rtnVo.setUpdatedBy(mainEntity.getUpdatedBy());
            
            //透過ProcessId 查詢該表單流程所有的申請以及審核流程資訊
            List<FormProcessDetailApplySrEntity> applyEnentityLs = formProcessDetailApplyRepository.findByProcessId(rtnVo.getProcessId());
            List<FormProcessDetailReviewSrEntity> reviewEnentityLs = formProcessDetailReviewRepository.findByProcessId(rtnVo.getProcessId());

            for(FormProcessDetailApplySrEntity entity : applyEnentityLs) {
                FormProcessManagmentSrApplyVO applyVo = new FormProcessManagmentSrApplyVO();
                BeanUtil.copyProperties(entity, applyVo);
                JsonArray arr = getWordingByCondition(applyVo.getDetailId(),FormEnum.APPLY,String.valueOf(applyVo.getProcessOrder()));
                applyVo.setLevelWordings(arr.toString());
                applyVoLs.add(applyVo);
            }
            
            for(FormProcessDetailReviewSrEntity entity : reviewEnentityLs) {
                FormProcessManagmentSrReviewVO reviewVo = new FormProcessManagmentSrReviewVO();
                BeanUtil.copyProperties(entity, reviewVo);
                JsonArray arr = getWordingByCondition(reviewVo.getDetailId(),FormEnum.REVIEW,String.valueOf(reviewVo.getProcessOrder()));
                reviewVo.setLevelWordings(arr.toString());
                reviewVoLs.add(reviewVo);
            }
            
            rtnVo.setApplyProcessList(applyVoLs);
            rtnVo.setReviewProcessList(reviewVoLs);
            
        } else {
            logger.error("錯誤,未傳入表單流程ID");
        }

        return rtnVo;
    }

    @Override
    public int getFormProcessOrder(String detailId, String groupId, String verifyType) {
        return getFormProcessDetailProcessOrder(detailId, groupId, verifyType);
    }

    @Override
    protected int fetchApplyProcessDetailProcessOrder(String detailId, String groupId) {
        FormProcessDetailApplySrEntity formProcessDetailApplySrEntity = formProcessDetailApplyRepository.findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc(detailId, groupId);
        if (Objects.isNull(formProcessDetailApplySrEntity)) {
            return 0;
        }
        return formProcessDetailApplySrEntity.getProcessOrder();
    }

    @Override
    protected int fetchReviewProcessDetailProcessOrder(String detailId, String groupId) {
        FormProcessDetailReviewSrEntity formProcessDetailReviewSrEntity = formProcessDetailReviewRepository.findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc(detailId, groupId);
        if (Objects.isNull(formProcessDetailReviewSrEntity)) {
            return 0;
        }
        return formProcessDetailReviewSrEntity.getProcessOrder();
    }
}
