package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParamEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.service.IHierarchicalOptionService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/*
 * 服務類別管理
 * 
 */
@Transactional
@Service("serviceTypeManagementService")
public class ServiceTypeManagementServiceImpl extends BaseService implements IHierarchicalOptionService {

    @Autowired
    private ISysOptionRepository sysOptionRepo;

    @Override
    public List<SystemOptionVO> getHierachicalList (SystemOptionVO search) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("FIND_SYS_OPTION_BY_CONDITION");
        String active = search.getActive();
        String display = search.getDisplay();
        String parentId = search.getParentId();

        if (StringUtils.isNoneBlank(display)) {
            conditions.and().like("Display", display);
        }

        if (StringUtils.isNoneBlank(parentId)) { // 服務子類別
            conditions.and().equal("ParentId", parentId);
            conditions.and().equal("OptionId", SysParamEnum.SERVICE_2.action());
        } else { // 服務類別
            conditions.and().equal("OptionId", SysParamEnum.SERVICE.action());
        }

        if (StringUtils.isNoneBlank(active)) { // 判斷狀態:啟用/停用
            conditions.and().equal("Active", active);
        }

        return jdbcRepository.queryForList(resource, conditions, SystemOptionVO.class);
        
    }

    @Override
    public SystemOptionVO create (SystemOptionVO vo) {
        Date today = new Date();
        SysOptionEntity entity = new SysOptionEntity();
        BeanUtil.copyProperties(vo, entity);
        boolean isKnowledge = Boolean.valueOf(vo.getIsKnowledge());
        
        if (isKnowledge) {
            entity.setIsKnowledge(StringConstant.SHORT_YES);
        } else {
            entity.setIsKnowledge(StringConstant.SHORT_NO);
        }

        entity.setName(vo.getDisplay());
        entity.setValue(String.valueOf(System.currentTimeMillis()));
        entity.setUpdatedBy(UserInfoUtil.loginUserId());
        entity.setUpdatedAt(today);
        entity.setCreatedBy(UserInfoUtil.loginUserId());
        entity.setCreatedAt(today);
        sysOptionRepo.save(entity);

        return vo;
    }

    @Override
    public SystemOptionVO update (SystemOptionVO vo) {
        SysOptionEntity entity = new SysOptionEntity();
        BeanUtil.copyProperties(vo, entity);
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(UserInfoUtil.loginUserId());
        sysOptionRepo.save(entity);

        return vo;
    }

}
