package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseResponseVO;

/**
 * 查詢User開立工作單資訊清單的資料物件
 * @author adam.yeh
 */
public class JobInfoListVO extends BaseResponseVO {

    private String userId;                     // 人員AD帳戶名稱
    private int formCounts;                    // 表單數量
    private List<FormDataListVO> formDataList; // 表單單號資料
    
    /*
     * 1 = 查詢成功
     * 2 = 發生錯誤
     * 0 = 流程表單糸統不認得此帳戶
     */
    private String qyStatus;
    
    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public int getFormCounts () {
        return formCounts;
    }

    public void setFormCounts (int formCounts) {
        this.formCounts = formCounts;
    }

    public List<FormDataListVO> getFormDataList () {
        return formDataList;
    }

    public void setFormDataList (List<FormDataListVO> formDataList) {
        this.formDataList = formDataList;
    }

    public String getQyStatus () {
        return qyStatus;
    }

    public void setQyStatus (String qyStatus) {
        this.qyStatus = qyStatus;
    }

    
}
