package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.LdapGroupEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysGroupPermissionEntity;
import com.ebizprise.winw.project.entity.WorkingItemEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.repository.ILdapGroupRepository;
import com.ebizprise.winw.project.repository.ISysGroupPermissionRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.IWorkingItemRepository;
import com.ebizprise.winw.project.service.ISysGroupService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.SysGroupPermissionVO;
import com.ebizprise.winw.project.xml.vo.UserIdVO;

@Transactional
@Service("sysGroupService")
public class SysGroupServiceImpl extends BaseService implements ISysGroupService {

    @Autowired
    private ILdapGroupRepository ldapGroupRepository;

    @Autowired
    private ISysGroupRepository sysGroupRepository;

    @Autowired
    private ISysGroupPermissionRepository sysGroupPermissionRepository;
    
    @Autowired
    private IWorkingItemRepository workingItemRepository;

    /**
     * 主要處理從 WebService 取得的 xml 解析後將該 VO 轉存為資料庫的物件
     * 
     * @param groupVOList
     */
    @Override
    public void saveGroupsFromWebService(List<UserIdVO> groupVOList) {
        if (CollectionUtils.isNotEmpty(groupVOList)) {
            // 將所有群組取出並儲存成 Map
            List<LdapGroupEntity> ldapGroupEntityList = ldapGroupRepository.findAll();
            Map<String, Long> groupMap = new HashMap<>();
            for (LdapGroupEntity ldapGroupEntity : ldapGroupEntityList) {
                groupMap.put(ldapGroupEntity.getGroupId(), ldapGroupEntity.getId());
            }

            List<LdapGroupEntity> newLdapGroupEntityList = new ArrayList<>();
            // 用 Map key 比對，如果 null 則建立新的物件
//            for (UserIdVO groupVO : groupVOList) {
//                LdapGroupEntity ldapGroupEntity = ldapGroupRepository.findByGroupId(groupVO.getName());
//                if (groupMap.get(groupVO.getName()) == null) {
//                    ldapGroupEntity = new LdapGroupEntity();
//                    ldapGroupEntity.setGroupId(groupVO.getName());
//                }
//                ldapGroupEntity.setName(groupVO.getDescription());
//                ldapGroupEntity.setEnabled(StringConstant.SHORT_YES);
//                newLdapGroupEntityList.add(ldapGroupEntity);
//            }
            ldapGroupRepository.saveAll(newLdapGroupEntityList);
        }
    }

    @Override
    public void saveGroupsFromLDAP(List<LdapGroupEntity> ldapGroupEntityList) {
        // 在更新群組資訊前先將 enabled 狀態設為 N 關閉狀態
        // 如果LDAP中還存在該群組才會再開啟
        ldapGroupRepository.updateAllGroupsEnableValue(StringConstant.SHORT_NO);

        // 當資料庫查無該群組時會新增該群組資料否則為更新群組資料
        if (CollectionUtils.isNotEmpty(ldapGroupEntityList)) {
            LdapGroupEntity dbLdapGroupEntity;
            List<LdapGroupEntity> toDbSysGroupEntityList = new ArrayList<>();
            String[] dnSplit;
            for (LdapGroupEntity ldapGroupEntity : ldapGroupEntityList) {
                dbLdapGroupEntity = ldapGroupRepository.findByGroupId(ldapGroupEntity.getGroupId());
                if (Objects.isNull(dbLdapGroupEntity)) {
                    dbLdapGroupEntity = new LdapGroupEntity();
                    dbLdapGroupEntity.setGroupId(ldapGroupEntity.getGroupId());
                    dbLdapGroupEntity.setCreatedBy(UserEnum.SYSTEM.wording());
                    dbLdapGroupEntity.setCreatedAt(new Date());
                    toDbSysGroupEntityList.add(dbLdapGroupEntity);
                } else {
                    dbLdapGroupEntity.setUpdatedBy(UserEnum.SYSTEM.wording());
                    dbLdapGroupEntity.setUpdatedAt(new Date());
                }

                // 將群組DN拆解
                // 如:CN=OA,OU=A01419,OU=eBizprise,DC=ebizprise,DC=corp
                dnSplit = ldapGroupEntity.getLdapDn().split(StringConstant.COMMA);

                // 該群組所在的單位
                if(dnSplit[1].startsWith("OU=")){
                    dbLdapGroupEntity.setLdapOu(dnSplit[1]);
                }

                // 該群組所在的上一層單位
                if(dnSplit[2].startsWith("OU=")){
                    dbLdapGroupEntity.setUpperOu(dnSplit[2]);
                }
                dbLdapGroupEntity.setLdapDn(ldapGroupEntity.getLdapDn());
                dbLdapGroupEntity.setName(ldapGroupEntity.getName());
                dbLdapGroupEntity.setEnabled(ldapGroupEntity.getEnabled());
            }
            ldapGroupRepository.saveAll(toDbSysGroupEntityList);
        }
    }

    @Override
    public LdapGroupEntity findByGroupId(String groupId) {
        return ldapGroupRepository.findByGroupId(groupId);
    }
    
    /**
     * 查詢所有群組資料
     * 
     * @return
     * @author willy.peng
     */
    @Override
    public List<GroupFunctionVO> getAllSysGroupVOs() {
        List<SysGroupEntity> groupLists = sysGroupRepository.findAll();
        return BeanUtil.copyList(groupLists, GroupFunctionVO.class);
    }

    /**
     * 模糊查詢群組資料
     * 
     * @return
     * @author willy.peng
     */
    @Override
    public List<GroupFunctionVO> findSysGroupByName(GroupFunctionVO queryVo) {
        List<SysGroupEntity> groupLists = sysGroupRepository.findByGroupNameLike(queryVo.getGroupName());
        return BeanUtil.copyList(groupLists, GroupFunctionVO.class);
    }

    /**
     * 用自訂群組ID查詢群組資料
     * 
     * @return
     * @author willy.peng
     */
    @Override
    public GroupFunctionVO findBySysGroupId(Long sysGroupId) {
        SysGroupEntity groupList = sysGroupRepository.findBySysGroupId(sysGroupId);
        GroupFunctionVO groupListVo = new GroupFunctionVO();

        BeanUtil.copyProperties(groupList, groupListVo);

        return groupListVo;
    }

    /**
     * 更新群組功能資料
     * 
     * @author willy.peng
     */
    @Override
    public void saveGroupFunction(GroupFunctionVO groupVo) {
        // 更新群組資料
        updateSysGroup(groupVo);
        // 更新權限
        updateGroupPermission(groupVo);
    }

    /**
     * 查詢群組選單權限
     * 
     * @return
     * @author willy.peng
     */
    @Override
    public List<SysGroupPermissionVO> findGroupMenuPermissionBySysGroupId(Long sysGroupId) {
        List<SysGroupPermissionEntity> groupMenus = sysGroupPermissionRepository.findBySysGroupId(sysGroupId);
        return BeanUtil.copyList(groupMenus, SysGroupPermissionVO.class);
    }
    
    @Override
    public List<HtmlVO> getSysGroupSelectorReverse () {
        List<HtmlVO> voList = new ArrayList<>();
        Set<String> distinct = new HashSet<>();
        String manager = UserEnum.MANAGER.symbol();
        String departmentId, deparmentName, division;
        List<SysGroupEntity> groups = sysGroupRepository.findAll();
        
        for(SysGroupEntity entity : groups) {
            division = entity.getDivision();
            departmentId = entity.getDepartmentId();
            deparmentName = entity.getDepartmentName();
            
            if (StringUtils.isBlank(departmentId) ||
                    StringUtils.isBlank(division)) {
                continue;
            }

            if (manager.equals(division)) {
                continue;
            }

            HtmlVO vo = new HtmlVO();
            String key = departmentId + "-" + division;
            vo.setWording(key);
            vo.setValue(deparmentName + "-" + division);
            
            if(!distinct.contains(key)) {
                voList.add(vo);
                distinct.add(key);
            }
        }
        
        return voList;
    }
    
    @Override
    public List<HtmlVO> getSysGroupSelectorOnlyUseIncListReport(String exclude){
        List<HtmlVO> voList = new ArrayList<>();
        List<HtmlVO> originalList = getSysGroupSelectorReverse ();
        HtmlVO all = new HtmlVO();
        all.setWording(getMessage("report.operation.select.division.all"));
        all.setValue("allSection");
        voList.add(all);
        for(HtmlVO vo : originalList) {
            HtmlVO v = new HtmlVO();
            String[] keys = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                    vo.getWording(),StringConstant.DASH);
            if(keys != null && keys.length>1) {
                if(exclude.equals(StringConstant.SHORT_YES)) {
                    if(keys[0].equals("A01419")) {
                        v.setWording(keys[1]);
                        v.setValue(keys[1]);
                        voList.add(v);
                    }
                }else {
                    v.setWording(keys[1]);
                    v.setValue(keys[1]);
                    voList.add(v);
                }
            }
        }
        return voList;
    }
    
    @Override
    public List<HtmlVO> getSpGroupSelectorReverse() {
        List<HtmlVO> voList = new ArrayList<>();
        Set<String> distinct = new HashSet<>();
        String manager = UserEnum.MANAGER.symbol();
        String spGroup;
        List<WorkingItemEntity> dataLs = workingItemRepository.findAll();
        
        for(WorkingItemEntity entity : dataLs) {
            spGroup = entity.getSpGroup();
            
            if (StringUtils.isBlank(spGroup)) {
                continue;
            }

            if (manager.equals(spGroup)) {
                continue;
            }

            HtmlVO vo = new HtmlVO();
            String key = spGroup;
            vo.setWording(key);
            vo.setValue(key);
            
            if(!distinct.contains(key)) {
                voList.add(vo);
                distinct.add(key);
            }
        }
        
        return voList;
    }
    
    private void updateSysGroup(GroupFunctionVO groupVo) {
        SysGroupEntity group = sysGroupRepository.findBySysGroupId(groupVo.getSysGroupId());

        group.setAuthType(groupVo.getAuthType());
        group.setAllowBatchReview(groupVo.isAllowBatchReview());
        group.setDisplayKpi(groupVo.isDisplayKpi());
        group.setUpdatedBy(UserInfoUtil.loginUserId());
        group.setUpdatedAt(new Date());

        sysGroupRepository.save(group);
    }

    private void updateGroupPermission(GroupFunctionVO groupVo) {
        sysGroupPermissionRepository.deleteBySysGroupId(groupVo.getSysGroupId());

        if (groupVo.getGroupPermissions().size() > 0) {
            List<SysGroupPermissionEntity> permissions = new ArrayList<>();
            permissions = BeanUtil.copyList(groupVo.getGroupPermissions(), SysGroupPermissionEntity.class);
            setBaseColumnInfo(permissions);
            sysGroupPermissionRepository.saveAll(permissions);
        }
    }

    @Override
    public List<SysGroupEntity> findSysGroupByDivision (String division) {
        return sysGroupRepository.findSysGroupByDivision(division);
    }

    private void setBaseColumnInfo(List<SysGroupPermissionEntity> permissions) {
        String userId = UserInfoUtil.loginUserId();
        Date currentDate = new Date();
        
        for(SysGroupPermissionEntity p : permissions) {
            p.setCreatedAt(currentDate);
            p.setCreatedBy(userId);
            p.setUpdatedAt(currentDate);
            p.setUpdatedBy(userId);
        }
    }
}
