package com.ebizprise.winw.project.vo;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class GroupFunctionVO extends BaseVO {

    @NotEmpty
    private Long sysGroupId;                                 //自訂群組ID
    private String departmentId;                             //部門代碼
    private String departmentName;                           //部門名稱
    private String division;                                 //科別
    private String groupId;                                  //群組代碼
    private String groupName;                                //群組名稱
    @NotEmpty
    private String authType;                                 //群組身分(0:經辦人員,1:審核人)
    @NotEmpty
    private String isAllowBatchReview;                       //是否允許批次審核
    @NotEmpty
    private String isDisplayKpi;                             //是否顯示首頁KPI
    private List<MenuVO> menus;                              //系統功能表
    private List<SysGroupPermissionVO> groupPermissions;     //群組選單權限
    private List<LdapUserVO> ldapUsers;                      //系統使用者表

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String isAllowBatchReview() {
        return isAllowBatchReview;
    }

    public void setAllowBatchReview(String isAllowBatchReview) {
        this.isAllowBatchReview = isAllowBatchReview;
    }

    public String isDisplayKpi() {
        return isDisplayKpi;
    }

    public void setDisplayKpi(String isDisplayKpi) {
        this.isDisplayKpi = isDisplayKpi;
    }

    public List<MenuVO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuVO> menus) {
        this.menus = menus;
    }

    public List<SysGroupPermissionVO> getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(List<SysGroupPermissionVO> groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    public List<LdapUserVO> getLdapUsers() {
        return ldapUsers;
    }

    public void setLdapUsers(List<LdapUserVO> ldapUsers) {
        this.ldapUsers = ldapUsers;
    }


}
