package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理 - 工作單- 申請流程 VO
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentJobApplyVO extends BaseFormProcessManagmentDetailVO {

    private String workValue;               // 工作項目編號
    private String workProject;             // 工作項目外顯值
    private String isWorkLevel;             // 作業關卡
    private String isCreateSpJobIssue;      // 是否新增系統工作單
    private String isCreateJobCIssue;       // 是否產生工作會辦單
    private String isWaitForSubIssueFinish; // 是否等待衍生單完成
    private String isCreateCompareList;     // 是否產生程式清單
    private String isModifyColumnData;      // 可修改欄位資料
    private String userId;                  // 員工編號
    private String name;                    // 員工姓名
    private String isParallel;
    private String parallels;

    public String getWorkProject() {
        return workProject;
    }

    public void setWorkProject(String workProject) {
        this.workProject = workProject;
    }

    public String getIsWorkLevel() {
        return isWorkLevel;
    }

    public void setIsWorkLevel(String isWorkLevel) {
        this.isWorkLevel = isWorkLevel;
    }

    public String getIsCreateJobCIssue() {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue(String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
    }

    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }

    public String getIsCreateCompareList() {
        return isCreateCompareList;
    }

    public void setIsCreateCompareList(String isCreateCompareList) {
        this.isCreateCompareList = isCreateCompareList;
    }

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

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

    public String getWorkValue () {
        return workValue;
    }

    public void setWorkValue (String workValue) {
        this.workValue = workValue;
    }

    public String getIsCreateSpJobIssue() {
        return isCreateSpJobIssue;
    }

    public void setIsCreateSpJobIssue(String isCreateSpJobIssue) {
        this.isCreateSpJobIssue = isCreateSpJobIssue;
    }

    public String getIsParallel () {
        return isParallel;
    }

    public void setIsParallel (String isParallel) {
        this.isParallel = isParallel;
    }

    public String getParallels () {
        return parallels;
    }

    public void setParallels (String parallels) {
        this.parallels = parallels;
    }

}
