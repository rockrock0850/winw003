package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單流程管理  單筆資料查詢結果 統一共用 VO
 * 
 * The <code>FormProcessManagmentFormVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentResultVO extends BaseFormProcessManagmentFormVo {

    private List<? extends BaseFormProcessManagmentDetailVO> applyProcessList;//申請流程
    private List<? extends BaseFormProcessManagmentDetailVO> reviewProcessList;//審核流程

    public List<? extends BaseFormProcessManagmentDetailVO> getApplyProcessList() {
        return applyProcessList;
    }

    public void setApplyProcessList(List<? extends BaseFormProcessManagmentDetailVO> applyProcessList) {
        this.applyProcessList = applyProcessList;
    }

    public List<? extends BaseFormProcessManagmentDetailVO> getReviewProcessList() {
        return reviewProcessList;
    }

    public void setReviewProcessList(List<? extends BaseFormProcessManagmentDetailVO> reviewProcessList) {
        this.reviewProcessList = reviewProcessList;
    }

}
