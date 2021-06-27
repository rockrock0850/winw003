package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseRequestVO;

/**
 * 工作單內的程式庫頁簽需要介接其他系統的版本控制資料的請求資料類別
 * @author adam.yeh
 */
public class VersionFormVO extends BaseRequestVO {

    private String fn;          // 表單編號
    private byte[] data;        // 檔案內容
    private String userId;      // 人員AD帳戶名稱
    private String fileName;    // 要取得比對檔案的名稱
    private String publishDate; // 工作單連線系統實施日期(YYYY-mm-DD)
    
    /*
     * 狀態代號(及說明) : 0 = 不是、1 = 是
     * ※滿足以下條件的表單皆等於1。
     * (1)擬訂中、(2)申請第1關、(3)已作廢
     */
    private String fnStatus;
    
    /*
     * 狀態代號(及說明) : 0 = 不是、1 = 是
     * ※只要知道(表單流程是否在開單人員申請)
     */
    private String formStatus;

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getPublishDate () {
        return publishDate;
    }

    public void setPublishDate (String publishDate) {
        this.publishDate = publishDate;
    }

    public String getFnStatus () {
        return fnStatus;
    }

    public void setFnStatus (String fnStatus) {
        this.fnStatus = fnStatus;
    }

    public String getFormStatus () {
        return formStatus;
    }

    public void setFormStatus (String formStatus) {
        this.formStatus = formStatus;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData () {
        return data;
    }

    public void setData (byte[] data) {
        this.data = data;
    }

}
