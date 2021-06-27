package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 變更單 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentChgFormVO extends BaseFormProcessManagmentFormVo {
    
    private List<FormProcessManagmentChgApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentChgReviewVO> reviewProcessList;//審核流程

    public List<FormProcessManagmentChgApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentChgApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentChgReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentChgReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
