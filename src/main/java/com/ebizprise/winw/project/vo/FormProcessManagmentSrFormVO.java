package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 需求單 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentSrFormVO extends BaseFormProcessManagmentFormVo {

    private List<FormProcessManagmentSrApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentSrReviewVO> reviewProcessList;//審核流程

    public List<FormProcessManagmentSrApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentSrApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentSrReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentSrReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
