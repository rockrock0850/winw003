package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.WorkingItemEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IWorkingItemRepository;
import com.ebizprise.winw.project.service.IJobItemManagementService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.JobManagementVO;

/*
 * 工作要項管理
 * 
 */
@Transactional
@Service("jobItemManagementService")
public class JobItemManagementServiceImpl extends BaseService implements IJobItemManagementService {

    @Autowired
    private IWorkingItemRepository workingItemRepository;

    // 工作要項管理 查詢
    @Override
    public List<JobManagementVO> getJobItemManagementByCondition(JobManagementVO search) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("FIND_ALL_WORKING_ITEM_LIST");
        String workingItemName = search.getWorkingItemName();
        String spGroup = search.getSpGroup();
        String isImpact = search.getIsImpact();
        String isReview = search.getIsReview();

        if (StringUtils.isNoneBlank(search.getWorkingItemName())) {
            conditions.and().like("WorkingItemName", workingItemName);
        }

        if (StringUtils.isNoneBlank(search.getSpGroup())) {
            conditions.and().like("SpGroup", spGroup);
        }

        if (StringUtils.isNoneBlank(search.getIsImpact())) {
            conditions.and().equal("IsImpact", isImpact);
        }

        if (StringUtils.isNoneBlank(search.getIsReview())) {
            conditions.and().equal("IsReview", isReview);
        }

        return jdbcRepository.queryForList(resource, conditions, JobManagementVO.class);

    }

    // 新增工作要項名稱
    @Override
    public JobManagementVO createData(JobManagementVO vo) {
        WorkingItemEntity workingItemEntity = new WorkingItemEntity();
        BeanUtil.copyProperties(vo, workingItemEntity);
        Date today = new Date();

        // 工作要項識別碼 = WorkingItemName + '_' + "Working" + '_' + IsImpact + '_' + IsReview
        workingItemEntity.setWorkingItemId(vo.getWorkingItemName() + "_" + "Working" + "_" + vo.getIsImpact() + "_" + vo.getIsReview());
        workingItemEntity.setUpdatedBy(UserInfoUtil.loginUserId());
        workingItemEntity.setUpdatedAt(today);
        workingItemEntity.setCreatedBy(UserInfoUtil.loginUserId());
        workingItemEntity.setCreatedAt(today);
        workingItemEntity.setActive(StringConstant.SHORT_YES); // 默認為啟用
        workingItemRepository.save(workingItemEntity);

        return vo;
    }

    // 編輯工作要項名稱
    @Override
    public JobManagementVO update(JobManagementVO vo) {
        if (vo != null) {
            Map<String, Object> params = new HashMap<>();
            Date today = new Date();

            params.put("id", vo.getId());
            params.put("spGroup", vo.getSpGroup());
            params.put("isImpact", vo.getIsImpact());
            params.put("isReview", vo.getIsReview());
            params.put("updatedBy", UserInfoUtil.loginUserId());
            params.put("updatedAt", today);
            params.put("active", vo.getActive());

            jdbcRepository.update(ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("UPDATE_WORKING_ITEM_MANAGEMENT"), params);
        }

        return vo;

    }
}