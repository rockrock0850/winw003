package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理 - 批次作業中斷對策表管理- 申請流程 VO
 * 
 * The <code>FormProcessManagmentApplyVO</code>
 * 
 * @author Bernard.Yu
 * @version 1.0, Created at 2020年10月15日
 */
public class FormProcessManagmentBaApplyVO extends BaseFormProcessManagmentDetailVO {

    private String isCreateChangeIssue;
    private String isCreateCIssue;
    private String isWaitForSubIssueFinish;
    private String isCheckLevel;
    private String isModifyColumnData;
    private String isParallel;
    private String parallels;

    public String getIsCreateChangeIssue() {
        return isCreateChangeIssue;
    }

    public void setIsCreateChangeIssue(String isCreateChangeIssue) {
        this.isCreateChangeIssue = isCreateChangeIssue;
    }

    public String getIsCreateCIssue() {
        return isCreateCIssue;
    }

    public void setIsCreateCIssue(String isCreateCIssue) {
        this.isCreateCIssue = isCreateCIssue;
    }

    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }

    public String getIsCheckLevel() {
        return isCheckLevel;
    }

    public void setIsCheckLevel(String isCheckLevel) {
        this.isCheckLevel = isCheckLevel;
    }

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
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