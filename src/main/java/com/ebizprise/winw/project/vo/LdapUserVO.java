package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 
 * 系統人員Vo
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年6月26日
 */
public class LdapUserVO extends BaseVO {
    
    private String userId;      //使用者編號
    private String name;        //使用者名稱
    private String rocId;       //身分證字號
    private String title;       //使用者職稱
    private String password;    //使用者密碼
    private String phone;       //使用者電話
    private String email;       //使用者信箱
    private String ldapDn;      //使用者DN
    private String ldapOu;      //使用者OU
    private String groups;      //使用者LDAP所屬群組
    private String sysGroupId;  //使用者系統所屬群組
    private String authorLevel; //使用者權限等級
    private String isEnabled;   //是否開放使用者
    private String subGroup;    //工作群組
    private String groupName;   //群組名稱
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRocId() {
        return rocId;
    }

    public void setRocId(String rocId) {
        this.rocId = rocId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public String getLdapOu() {
        return ldapOu;
    }

    public void setLdapOu(String ldapOu) {
        this.ldapOu = ldapOu;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(String sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getAuthorLevel() {
        return authorLevel;
    }

    public void setAuthorLevel(String authorLevel) {
        this.authorLevel = authorLevel;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getSubGroup () {
        return subGroup;
    }

    public void setSubGroup (String subGroup) {
        this.subGroup = subGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
}
