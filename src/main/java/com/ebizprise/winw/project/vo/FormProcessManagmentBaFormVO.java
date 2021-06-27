package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理 批次作業中斷對策表管理 新增 & 編輯頁面 傳入物件VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author Bernard.Yu
 * @version 1.0, Created at 2020年10月15日
 */
public class FormProcessManagmentBaFormVO extends BaseFormProcessManagmentFormVo {

    private List<FormProcessManagmentBaApplyVO> applyProcessList;//申請流程
    private List<FormProcessManagmentBaReviewVO> reviewProcessList;//審核流程

    public List<FormProcessManagmentBaApplyVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<FormProcessManagmentBaApplyVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<FormProcessManagmentBaReviewVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<FormProcessManagmentBaReviewVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
