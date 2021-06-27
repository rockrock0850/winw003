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
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.service.IJobGroupManagementService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/*
 * 工作組別管理
 * 
 */
@Transactional
@Service("jobGroupManagementService")
public class JobGroupManagementServiceImpl extends BaseService implements IJobGroupManagementService {

    @Autowired
    private ISysOptionRepository sysOptionRepository;

    // 工作組別管理 查詢
    @ModLog
    @Override
    public List<SystemOptionVO> getJopGroupManagementByCondition(SystemOptionVO search) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("FIND_SYS_OPTION_FOR_JOB_GROUP");
        String display = search.getDisplay();

        if (StringUtils.isNoneBlank(display)) {
            conditions.and().like("Display", display);
        }
        
        return jdbcRepository.queryForList(resource, conditions, SystemOptionVO.class);
        
    }

    // 新增工作組別名稱
    @Override
    public SystemOptionVO createData(SystemOptionVO vo) {
        SysOptionEntity sysOptionEntity = new SysOptionEntity();
        BeanUtil.copyProperties(vo, sysOptionEntity);
        Date today = new Date();

        sysOptionEntity.setSort(null); // 規則預設為null
        sysOptionEntity.setOptionId("WORK_LEVEL"); // 預設為"WORK_LEVEL"
        sysOptionEntity.setName("工作單作業關卡的工作項目");
        sysOptionEntity.setDisplay(vo.getDisplay());
        sysOptionEntity.setValue(vo.getDisplay());
        sysOptionEntity.setParentId(null); // 規則預設為null
        sysOptionEntity.setActive(StringConstant.SHORT_YES); // 默認為啟用狀態
        sysOptionEntity.setUpdatedBy(UserInfoUtil.loginUserId());
        sysOptionEntity.setUpdatedAt(today);
        sysOptionEntity.setCreatedBy(UserInfoUtil.loginUserId());
        sysOptionEntity.setCreatedAt(today);
        sysOptionRepository.save(sysOptionEntity);

        return vo;
    }

    // 編輯工作組別名稱
    @Override
    public SystemOptionVO update(SystemOptionVO vo) {
        if (vo != null) {
            Map<String, Object> params = new HashMap<>();
            Date today = new Date();

            params.put("id", vo.getId());
            params.put("display", vo.getDisplay());
            params.put("active", vo.getActive());
            params.put("updatedBy", UserInfoUtil.loginUserId());
            params.put("updatedAt", today);

            jdbcRepository.update(ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("UPDATE_SYSTEM_OPTION_MANAGEMENT"), params);
        }

        return vo;
        
    }
}
