package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 事件單 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentIncFormVO extends BaseFormProcessManagmentFormVo {

    private List<FormProcessManagmentIncApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentIncReviewVO> reviewProcessList;//審核流程


    public List<FormProcessManagmentIncApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentIncApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentIncReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentIncReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
