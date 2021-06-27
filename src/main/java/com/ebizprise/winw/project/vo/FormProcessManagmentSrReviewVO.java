package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理  - 需求單- 審核流程 VO
 * 
 * The <code>FormProcessManagmentReviewVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentSrReviewVO extends BaseFormProcessManagmentDetailVO {

    private String isModifyColumnData;
    private String isCloseForm;//可否直接結案
    private String isWaitForSubIssueFinish;//等待衍生單完成
    

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }
    

    public String getIsCloseForm() {
        return isCloseForm;
    }

    public void setIsCloseForm(String isCloseForm) {
        this.isCloseForm = isCloseForm;
    }

    /**
     * @return the isWaitForSubIssueFinish
     */
    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    /**
     * @param isWaitForSubIssueFinish the isWaitForSubIssueFinish to set
     */
    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }

}
