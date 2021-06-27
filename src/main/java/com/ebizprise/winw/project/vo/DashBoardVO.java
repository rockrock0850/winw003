/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * @author suho.yeh
 * @version 1.0, Created at 2019年7月15日
 */
public class DashBoardVO extends BaseVO {

    private String htmlId;
    private List<DashBoardVO> formList;
    private Integer count;
    private String nextExpiration;
    private String authType;
    private boolean isAllowBatchReview;
    private boolean isNextLevel;
    private String isDisplayKpi;
    private Integer kpiDemand;
    private Integer kpiIssue;
    private Integer kpiEvent;
    private Integer actualDemand;
    private Integer actualIssue;
    private Integer actualEvent;
    private Date submitTime;
    private Map<String, List<Map<String, Object>>> kpiList;
    private String orderNumber;
    private String orderStatus;
    private String orderType;
    private String sender;
    private String billingTime;
    private String processStatus;
    private String formId;
    private String detailId;
    private String formClass;
    private String formStatus;
    private String verifyType;
    private String verifyLevel;
    private String jumpLevel;
    private String verifyResult;
    private String arrivedTime;
    private String summary;
    private String finishDate;
    private String userSolving;
    private String groupSolving;
    private String userId;
    private String divisionSolving; // 處理科別
    private String actualCompDate; // 實際完成時間
    private String observation; // 問題單觀察期
    private String tct;
    
    public Date getSubmitTime () {
        return submitTime;
    }

    public void setSubmitTime (Date submitTime) {
        this.submitTime = submitTime;
    }

    public Map<String, List<Map<String, Object>>> getKpiList () {
        return kpiList;
    }

    public void setKpiList (Map<String, List<Map<String, Object>>> kpiList) {
        this.kpiList = kpiList;
    }

    public Integer getKpiDemand () {
        return kpiDemand;
    }

    public void setKpiDemand (Integer kpiDemand) {
        this.kpiDemand = kpiDemand;
    }

    public Integer getKpiIssue () {
        return kpiIssue;
    }

    public void setKpiIssue (Integer kpiIssue) {
        this.kpiIssue = kpiIssue;
    }

    public Integer getKpiEvent () {
        return kpiEvent;
    }

    public void setKpiEvent (Integer kpiEvent) {
        this.kpiEvent = kpiEvent;
    }

    public Integer getActualDemand () {
        return actualDemand;
    }

    public void setActualDemand (Integer actualDemand) {
        this.actualDemand = actualDemand;
    }

    public Integer getActualIssue () {
        return actualIssue;
    }

    public void setActualIssue (Integer actualIssue) {
        this.actualIssue = actualIssue;
    }

    public Integer getActualEvent () {
        return actualEvent;
    }

    public void setActualEvent (Integer actualEvent) {
        this.actualEvent = actualEvent;
    }

    public void setAllowBatchReview (boolean isAllowBatchReview) {
        this.isAllowBatchReview = isAllowBatchReview;
    }

    public boolean getIsAllowBatchReview () {
        return isAllowBatchReview;
    }

    public void setIsAllowBatchReview (boolean isAllowBatchReview) {
        this.isAllowBatchReview = isAllowBatchReview;
    }

    public String getIsDisplayKpi () {
        return isDisplayKpi;
    }

    public void setIsDisplayKpi (String isDisplayKpi) {
        this.isDisplayKpi = isDisplayKpi;
    }

    public String getAuthType () {
        return authType;
    }

    public void setAuthType (String authType) {
        this.authType = authType;
    }

    public String getNextExpiration () {
        return nextExpiration;
    }

    public void setNextExpiration (String nextExpiration) {
        this.nextExpiration = nextExpiration;
    }

    public Integer getCount () {
        return count;
    }

    public void setCount (Integer count) {
        this.count = count;
    }

    public String getHtmlId () {
        return htmlId;
    }

    public void setHtmlId (String htmlId) {
        this.htmlId = htmlId;
    }

    public String getOrderNumber () {
        return orderNumber;
    }

    public void setOrderNumber (String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus (String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType () {
        return orderType;
    }

    public void setOrderType (String orderType) {
        this.orderType = orderType;
    }

    public String getSender () {
        return sender;
    }

    public void setSender (String sender) {
        this.sender = sender;
    }

    public String getBillingTime () {
        return billingTime;
    }

    public void setBillingTime (String billingTime) {
        this.billingTime = billingTime;
    }

    public String getProcessStatus () {
        return processStatus;
    }

    public void setProcessStatus (String processStatus) {
        this.processStatus = processStatus;
    }

    public String getFormClass () {
        return formClass;
    }

    public void setFormClass (String formClass) {
        this.formClass = formClass;
    }

    public String getFormStatus () {
        return formStatus;
    }

    public void setFormStatus (String formStatus) {
        this.formStatus = formStatus;
    }

    public String getVerifyType () {
        return verifyType;
    }

    public void setVerifyType (String verifyType) {
        this.verifyType = verifyType;
    }

    public String getVerifyLevel () {
        return verifyLevel;
    }

    public void setVerifyLevel (String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public String getArrivedTime () {
        return arrivedTime;
    }

    public void setArrivedTime (String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public List<DashBoardVO> getFormList () {
        return formList;
    }

    public void setFormList (List<DashBoardVO> formList) {
        this.formList = formList;
    }

    public String getSummary () {
        return summary;
    }

    public void setSummary (String summary) {
        this.summary = summary;
    }

    public boolean getIsNextLevel () {
        return isNextLevel;
    }

    public void setIsNextLevel (boolean isNextLevel) {
        this.isNextLevel = isNextLevel;
    }

    public String getVerifyResult () {
        return verifyResult;
    }

    public void setVerifyResult (String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getDetailId () {
        return detailId;
    }

    public void setDetailId (String detailId) {
        this.detailId = detailId;
    }

    public String getJumpLevel () {
        return jumpLevel;
    }

    public void setJumpLevel (String jumpLevel) {
        this.jumpLevel = jumpLevel;
    }

    /**
     * @return the finishDate
     */
    public String getFinishDate() {
        return finishDate;
    }

    /**
     * @param finishDate the finishDate to set
     */
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * @return the userSolving
     */
    public String getUserSolving() {
        return userSolving;
    }

    /**
     * @param userSolving the userSolving to set
     */
    public void setUserSolving(String userSolving) {
        this.userSolving = userSolving;
    }

    public String getGroupSolving () {
        return groupSolving;
    }

    public void setGroupSolving (String groupSolving) {
        this.groupSolving = groupSolving;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getUserId () {
        return userId;
    }

    public String getDivisionSolving() {
        return divisionSolving;
    }

    public void setDivisionSolving(String divisionSolving) {
        this.divisionSolving = divisionSolving;
    }

    public String getActualCompDate() {
        return actualCompDate;
    }

    public void setActualCompDate(String actualCompDate) {
        this.actualCompDate = actualCompDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getTct () {
        return tct;
    }

    public void setTct (String tct) {
        this.tct = tct;
    }

}
