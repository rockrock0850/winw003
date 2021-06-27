package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParamEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.service.IHierarchicalOptionService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 知識庫原因維護
 * 
 * @author adam.yeh
 */
@Service
@Transactional
public class KnowledgeBaseServiceImpl extends BaseService implements IHierarchicalOptionService {

    @Autowired
    private ISysOptionRepository sysOptionRepo;

    @Override
    public List<SystemOptionVO> getHierachicalList (SystemOptionVO vo) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("FIND_SYS_OPTION_BY_CONDITION");
        String active = vo.getActive();
        String display = vo.getDisplay();
        String parentId = vo.getParentId();

        if (StringUtils.isNoneBlank(display)) {
            conditions.and().like("Display", display);
        }
        
        if (StringUtils.isNoneBlank(parentId)) {// Level2
            conditions.and().equal("ParentId", parentId);
            conditions.and().equal("OptionId", SysParamEnum.KNOWLEDGE_2.action());
        } else { // Level1
            conditions.and().equal("OptionId", SysParamEnum.KNOWLEDGE.action());
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
