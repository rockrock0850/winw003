/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.vo;

import java.util.Date;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * The <code>SRNotFinishBfTargetTimeVO</code>	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月7日
 */
public class SRNotFinishBfTargetTimeVO extends BaseVO {
    private String isExclude;
    private String startInterval;
    private String endInterval;
    private String formId;
    private String detailId;
    private String formStatus;
    private String userCreated;
    private Date ect;
    private Date act;
    private String ectString;
    private String actString;    
    private String groupId;
    private String groupName;
    private String name;
    private String departmentId;
    private String departmentName;
    private String division;   
    private String summary;
    
    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }
    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }
    /**
     * @return the formStatus
     */
    public String getFormStatus() {
        return formStatus;
    }
    /**
     * @param formStatus the formStatus to set
     */
    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }
    /**
     * @return the ectString
     */
    public String getEctString() {
        return ectString;
    }
    /**
     * @param ectString the ectString to set
     */
    public void setEctString(String ectString) {
        this.ectString = ectString;
    }
    /**
     * @return the actString
     */
    public String getActString() {
        return actString;
    }
    /**
     * @param actString the actString to set
     */
    public void setActString(String actString) {
        this.actString = actString;
    }
    /**
     * @return the formId
     */
    public String getFormId() {
        return formId;
    }
    /**
     * @param formId the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }
    /**
     * @return the detailId
     */
    public String getDetailId() {
        return detailId;
    }
    /**
     * @param detailId the detailId to set
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
    /**
     * @return the userCreated
     */
    public String getUserCreated() {
        return userCreated;
    }
    /**
     * @param userCreated the userCreated to set
     */
    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
    /**
     * @return the ect
     */
    public Date getEct() {
        return ect;
    }
    /**
     * @param ect the ect to set
     */
    public void setEct(Date ect) {
        this.ect = ect;
    }
    /**
     * @return the act
     */
    public Date getAct() {
        return act;
    }
    /**
     * @param act the act to set
     */
    public void setAct(Date act) {
        this.act = act;
    }
    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }
    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the departmentId
     */
    public String getDepartmentId() {
        return departmentId;
    }
    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }
    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    /**
     * @return the division
     */
    public String getDivision() {
        return division;
    }
    /**
     * @param division the division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }
    /**
     * @return the isExclude
     */
    public String getIsExclude() {
        return isExclude;
    }
    /**
     * @param isExclude the isExclude to set
     */
    public void setIsExclude(String isExclude) {
        this.isExclude = isExclude;
    }
    /**
     * @return the startInterval
     */
    public String getStartInterval() {
        return startInterval;
    }
    /**
     * @param startInterval the startInterval to set
     */
    public void setStartInterval(String startInterval) {
        this.startInterval = startInterval;
    }
    /**
     * @return the endInterval
     */
    public String getEndInterval() {
        return endInterval;
    }
    /**
     * @param endInterval the endInterval to set
     */
    public void setEndInterval(String endInterval) {
        this.endInterval = endInterval;
    }
    
}
