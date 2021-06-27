package com.ebizprise.winw.project.vo;

/**
 * 表單流程管理 - 變更單- 申請流程 VO
 * 
 * The <code>FormProcessManagmentApplyVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentChgApplyVO extends BaseFormProcessManagmentDetailVO {

    private String isSrCheckLevel;//需求單檢核關卡
    private String isSrCCheckLevel;//需求會辦單檢核關卡
    private String isQuestionCheckLevel;//問題單關卡
    private String isQuestionCCheckLevel;//問題會辦單關卡
    private String isBusinessImpactAnalysis;//衝擊分析判斷
    private String isModifyColumnData;//可修改欄位資料
    private String isWaitForSubIssueFinish;//等待衍生單完成

    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

    public String getIsSrCheckLevel() {
        return isSrCheckLevel;
    }
    public void setIsSrCheckLevel(String isSrCheckLevel) {
        this.isSrCheckLevel = isSrCheckLevel;
    }
    public String getIsSrCCheckLevel() {
        return isSrCCheckLevel;
    }
    public void setIsSrCCheckLevel(String isSrCCheckLevel) {
        this.isSrCCheckLevel = isSrCCheckLevel;
    }
    public String getIsQuestionCheckLevel() {
        return isQuestionCheckLevel;
    }
    public void setIsQuestionCheckLevel(String isQuestionCheckLevel) {
        this.isQuestionCheckLevel = isQuestionCheckLevel;
    }
    public String getIsQuestionCCheckLevel() {
        return isQuestionCCheckLevel;
    }
    public void setIsQuestionCCheckLevel(String isQuestionCCheckLevel) {
        this.isQuestionCCheckLevel = isQuestionCCheckLevel;
    }
    public String getIsBusinessImpactAnalysis() {
        return isBusinessImpactAnalysis;
    }
    public void setIsBusinessImpactAnalysis(String isBusinessImpactAnalysis) {
        this.isBusinessImpactAnalysis = isBusinessImpactAnalysis;
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
