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
import com.ebizprise.winw.project.entity.SystemEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.ISystemRepository;
import com.ebizprise.winw.project.service.ISystemManagementService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SystemManagementVO;

/*
 * 系統名稱管理
 * 
 */
@Transactional
@Service("systemManagementService")
public class SystemManagementServiceImpl extends BaseService implements ISystemManagementService {

    @Autowired
    private ISystemRepository systemRepository;

    // 系統名稱管理 查詢
    @Override
    public List<SystemManagementVO> getSystemManagmentByCondition(SystemManagementVO search) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_COMMON.getResource("FIND_ALL_SYSTEM_LIST");
        String systemId = search.getSystemId();
        String systemName = search.getSystemName();
        String mark = search.getMark();
        String divisionCreated = search.getDepartment();

        if (StringUtils.isNoneBlank(systemId)) {
            conditions.and().like("SystemId", systemId);
        }

        if (StringUtils.isNoneBlank(systemName)) {
            conditions.and().like("SystemName", systemName);
        }

        if (StringUtils.isNoneBlank(mark)) {
            conditions.and().like("Mark", mark);
        }

        if (StringUtils.isNoneBlank(divisionCreated)) {
            conditions.and().equal("Department", divisionCreated.split("-")[1]);
        }

        return jdbcRepository.queryForList(resource, conditions, SystemManagementVO.class);
    }

    // 新增系統名稱
    @Override
    public SystemManagementVO createData(SystemManagementVO vo) {
        SystemEntity systemEntity = new SystemEntity();
        BeanUtil.copyProperties(vo, systemEntity);
        String departmentId = vo.getDepartment().split(StringConstant.DASH)[1];
        Date today = new Date();

        // 系統名稱識別碼 = SystemId + '_' + Department + MboName + Opinc + Apinc + Limit
        systemEntity.setSystemBrand(vo.getSystemId() + "_" + vo.getDepartment() + "PROBLEM" + vo.getOpinc() + vo.getApinc() + vo.getLimit());
        systemEntity.setDescription(vo.getSystemName()); // 與SystemName一致
        systemEntity.setDepartment(departmentId);
        systemEntity.setMboName("PROBLEM"); // 預設值為PROBLEM
        systemEntity.setActive(StringConstant.SHORT_YES); // 默認為啟用狀態
        systemEntity.setUpdatedBy(UserInfoUtil.loginUserId());
        systemEntity.setUpdatedAt(today);
        systemEntity.setCreatedBy(UserInfoUtil.loginUserId());
        systemEntity.setCreatedAt(today);
        systemEntity.setOpinc(Integer.valueOf(vo.getOpinc()));
        systemEntity.setApinc(Integer.valueOf(vo.getApinc()));
        systemRepository.save(systemEntity);

        return vo;
    }

    // 編輯系統名稱
    @Override
    public SystemManagementVO update(SystemManagementVO vo) {
        if (vo != null) {
            Map<String, Object> params = new HashMap<>();
            Date today = new Date();

            params.put("id", vo.getId());
            params.put("systemName", vo.getSystemName());
            params.put("mark", vo.getMark());
            params.put("active", vo.getActive());
            params.put("department", vo.getDepartment().split(StringConstant.DASH)[1]);
            params.put("opinc", vo.getOpinc());
            params.put("apinc", vo.getApinc());
            params.put("limit", vo.getLimit());
            params.put("updatedBy", UserInfoUtil.loginUserId());
            params.put("updatedAt", today);

            jdbcRepository.update(ResourceEnum.SQL_SYSTEM_NAME_MANAGEMENT.getResource("UPDATE_SYSTEM_NAME_MANAGEMENT"), params);
        }

        return vo;
        
    }

}
