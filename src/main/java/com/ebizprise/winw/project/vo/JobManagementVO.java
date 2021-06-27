package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 資料表:WORKING_ITEM (工作要項)
 * 
 * @author momo.liu 2020/08/26
 * 
 */
public class JobManagementVO extends BaseVO {

    private String workingItemId;       // 工作要項識別碼
    private String workingItemName;     // 工作要項名稱
    private String spGroup;             // 系統科組別
    private String isImpact;            // 變更衝擊分析
    private String isReview;            // 變更覆核
    private String active;              // 狀態 _是否啟用

    private List<MenuVO> menus;                             // 系統功能表
    private List<SysGroupPermissionVO> groupPermissions;    // 群組選單權限
    private List<LdapUserVO> ldapUsers;                     // 系統使用者表

    public String getWorkingItemId() {
        return workingItemId;
    }

    public void setWorkingItemId(String workingItemId) {
        this.workingItemId = workingItemId;
    }

    public String getWorkingItemName() {
        return workingItemName;
    }

    public void setWorkingItemName(String workingItemName) {
        this.workingItemName = workingItemName;
    }

    public String getSpGroup() {
        return spGroup;
    }

    public void setSpGroup(String spGroup) {
        this.spGroup = spGroup;
    }

    public String getIsImpact() {
        return isImpact;
    }

    public void setIsImpact(String isImpact) {
        this.isImpact = isImpact;
    }

    public String getIsReview() {
        return isReview;
    }

    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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
