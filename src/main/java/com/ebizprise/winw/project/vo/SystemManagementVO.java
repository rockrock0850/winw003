package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 系統名稱管理_資料表:SYSTEM
 * 
 * @author momo.liu 2020/08/14
 */
public class SystemManagementVO extends BaseVO {

    private String systemBrand;     // 系統名稱識別碼(SystemId + '_' + Dpartment + MboName + Opinc + Apinc + Limit)
    private String systemId;        // 系統編號(系統名稱_代碼)
    private String systemName;      // 系統名稱(系統中文說明)
    private String description;     // 系統描述
    private String department;      // 部門代號
    private String mboName;         // 未知
    private String mark;            // 資訊資產群組
    private String opinc;           // 未知
    private String apinc;           // 未知
    private String active;          // 狀態 _是否啟用
    private String limit;           // 極限值
    private String division;        // 科別

    private List<MenuVO> menus;                             // 系統功能表
    private List<SysGroupPermissionVO> groupPermissions;    // 群組選單權限
    private List<LdapUserVO> ldapUsers;                     // 系統使用者表

    public String getSystemBrand() {
        return systemBrand;
    }

    public void setSystemBrand(String systemBrand) {
        this.systemBrand = systemBrand;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMboName() {
        return mboName;
    }

    public void setMboName(String mboName) {
        this.mboName = mboName;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOpinc() {
        return opinc;
    }

    public void setOpinc(String opinc) {
        this.opinc = opinc;
    }

    public String getApinc() {
        return apinc;
    }

    public void setApinc(String apinc) {
        this.apinc = apinc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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
