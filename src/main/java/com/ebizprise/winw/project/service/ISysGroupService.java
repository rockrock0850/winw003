package com.ebizprise.winw.project.service;

import java.util.List;
import com.ebizprise.winw.project.entity.LdapGroupEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.SysGroupPermissionVO;
import com.ebizprise.winw.project.xml.vo.UserIdVO;

public interface ISysGroupService {

    public void saveGroupsFromWebService(List<UserIdVO> list);

    public void saveGroupsFromLDAP(List<LdapGroupEntity> ldapGroupEntityList);

    public LdapGroupEntity findByGroupId(String groupId);

    public List<GroupFunctionVO> getAllSysGroupVOs();

    public List<GroupFunctionVO> findSysGroupByName(GroupFunctionVO queryVo);

    public GroupFunctionVO findBySysGroupId(Long sysGroupId);

    public void saveGroupFunction(GroupFunctionVO groupVo);

    public List<SysGroupPermissionVO> findGroupMenuPermissionBySysGroupId(Long sysGroupId);
    
    /**
     * 取得科別下拉選單</br>
     * value = DepartmentName+Division</br>
     * wording = DepartmentId+Division
     * 
     * @return
     * @author adam.yeh
     */
    public List<HtmlVO> getSysGroupSelectorReverse ();

    /**
     * 取得系統科組別下拉選單
     * 
     * @return
     */
    public List<HtmlVO> getSpGroupSelectorReverse ();

    public List<SysGroupEntity> findSysGroupByDivision (String division);
    
    /**
     * a各科未結案事件單追蹤統計表專用下拉選單
     * 
     * @return
     */    
    public List<HtmlVO> getSysGroupSelectorOnlyUseIncListReport(String exclude);

}
