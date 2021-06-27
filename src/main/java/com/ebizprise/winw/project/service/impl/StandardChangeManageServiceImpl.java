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
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.service.IStandardChangeService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 標準變更作業管理
 * 
 */
@Transactional
@Service("standardChangeService")
public class StandardChangeManageServiceImpl extends BaseService implements IStandardChangeService {

    @Autowired
    private ISysOptionRepository sysOptionRepository;

    // 標準變更作業管理 查詢
    @Override
    public List<SystemOptionVO> getSystemOptionByCondition(SystemOptionVO search) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_COMMON.getResource("FIND_ALL_SYSTEM_OPTION_LIST");
        String display = search.getDisplay();

        if (StringUtils.isNoneBlank(search.getDisplay())) {
            conditions.and().like("Display", display); // 模糊查詢
        }

        return jdbcRepository.queryForList(resource, conditions, SystemOptionVO.class);
    }

    // 新增標準變更作業管理
    @Override
    public SystemOptionVO createData(SystemOptionVO vo) {
        SysOptionEntity sysOptionEntity = new SysOptionEntity();
        BeanUtil.copyProperties(vo, sysOptionEntity);
        String display = vo.getDisplay();
        Date today = new Date();

        sysOptionEntity.setSort(null); // 預設null
        sysOptionEntity.setOptionId("StandardChange"); // 預設為StandardChange
        sysOptionEntity.setValue(String.valueOf(System.currentTimeMillis())); // 取當前時間
        sysOptionEntity.setName("標準變更作業"); // 預設為標準變更作業
        sysOptionEntity.setDisplay(display); // 不可重複
        sysOptionEntity.setParentId(null); // 預設null
        sysOptionEntity.setActive(StringConstant.SHORT_YES); // 默認為啟用狀態
        sysOptionEntity.setUpdatedBy(UserInfoUtil.loginUserId());
        sysOptionEntity.setUpdatedAt(today);
        sysOptionEntity.setCreatedBy(UserInfoUtil.loginUserId());
        sysOptionEntity.setCreatedAt(today);
        sysOptionRepository.save(sysOptionEntity);

        return vo;
    }

    // 編輯標準變更作業管理
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
