package com.ebizprise.winw.project.xml.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;
import com.ebizprise.winw.project.vo.MenuVO;

/**
 * @author gary.tsai 2019/5/31
 */
public class SysUserVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String title;
    private String name;
    private String groupId;
    private String groupName;
    private String authorLevel;
    private Date loginTime;
    private String departmentId;
    private String division;
    private String subGroup;
    private List<MenuVO> menuList;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public Date getLoginTime () {
        return loginTime;
    }

    public void setLoginTime (Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public String getAuthorLevel () {
        return authorLevel;
    }

    public void setAuthorLevel (String authorLevel) {
        this.authorLevel = authorLevel;
    }

    public String getDepartmentId () {
        return departmentId;
    }

    public void setDepartmentId (String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDivision () {
        return division;
    }

    public void setDivision (String division) {
        this.division = division;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public List<MenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
    }

    /**
     * 直接組合departmentId + '-' + division
     * @return
     */
    public String getDivisionSolving () {
        return this.departmentId + "-" + this.division;
    }

    public String getSubGroup () {
        return subGroup;
    }

    public void setSubGroup (String subGroup) {
        this.subGroup = subGroup;
    }
    
}
