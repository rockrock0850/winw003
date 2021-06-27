package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理 - 工作單- 審核流程 VO
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentJobReviewVO extends BaseFormProcessManagmentDetailVO {

    private String workProject;// 工作項目
    private String isWorkLevel;// 作業關卡
    private String isCreateJobCIssue;// 是否產生工作會辦單
    private String isWaitForSubIssueFinish;// 是否等待衍生單完成
    private String isModifyColumnData;// 可修改欄位資料
    private String isCloseForm;//可否直接結案

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

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
    

    public String getIsCloseForm() {
        return isCloseForm;
    }

    public void setIsCloseForm(String isCloseForm) {
        this.isCloseForm = isCloseForm;
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

}
