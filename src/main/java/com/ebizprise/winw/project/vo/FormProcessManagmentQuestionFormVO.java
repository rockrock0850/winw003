package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 問題單 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentQuestionFormVO extends BaseFormProcessManagmentFormVo {

    private List<FormProcessManagmentQuestionApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentQuestionReviewVO> reviewProcessList;//審核流程

    public List<FormProcessManagmentQuestionApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentQuestionApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentQuestionReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentQuestionReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
