package com.ebizprise.winw.project.vo;

/**
 * JobInfoListVO.formDataList的資料物件
 * @author adam.yeh
 */
public class FormDataListVO {
    
    public String fn;          // 表單編號
    public String publishDate; // 工作單連線系統實施日期(YYYY-mm-DD)
    
    /*
     * 狀態代號(及說明) : 0 = 不是、1 = 是
     * ※滿足以下條件的表單皆等於1。
     * (1)擬訂中、(2)申請第1關、(3)已作廢
     */
    public String fnStatus;

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
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
    
}
