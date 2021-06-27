package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理 - 變更單- 審核流程 VO
 * 
 * 
 * The <code>FormProcessManagmentReviewVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentChgReviewVO extends BaseFormProcessManagmentDetailVO {

    private String isCreateJobIssue;// 可否產生工作單
    private String isWaitForSubIssueFinish;//等待衍生單完成
    private String isModifyColumnData;//可修改欄位資料
    private String isCloseForm;//可否直接結案

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

    public String getIsCreateJobIssue() {
        return isCreateJobIssue;
    }

    public void setIsCreateJobIssue(String isCreateJobIssue) {
        this.isCreateJobIssue = isCreateJobIssue;
    }

    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }

    public String getIsCloseForm() {
        return isCloseForm;
    }

    public void setIsCloseForm(String isCloseForm) {
        this.isCloseForm = isCloseForm;
    }

}
