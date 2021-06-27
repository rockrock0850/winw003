package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 工作單 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentJobFormVO extends BaseFormProcessManagmentFormVo {
    
    private List<FormProcessManagmentJobApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentJobReviewVO> reviewProcessList;//審核流程

    public List<FormProcessManagmentJobApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentJobApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentJobReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentJobReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
