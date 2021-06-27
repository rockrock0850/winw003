package com.ebizprise.winw.project.vo;

import java.util.Date;

import com.ebizprise.winw.project.base.vo.BaseResponseVO;

/**
 * 查詢工作單於版控中的BaseLine編號的請求資料物件
 * @author adam.yeh
 */
public class VersionCodeBaseLineVO extends BaseResponseVO {

    private String fn;  // 表單編號
    private Date time;  // 查詢時間
    private String msg; // 代表訊息
    
    /*
     * 當QY_Status欄位為1時，
     * 為BaseLine編號；否則為空字串 or Null。
     */
    private String baseLine;

    /*
     * 0 = 查無此工作單號(表示未綁定任何版控)
     * 1 = 查詢OK(可由BaseLine欄位取值)
     * 2 = 此工作單的版控作業尚未產出BaeseLine
     * 3 = 版控管理系統作業異常
     */
    private String qy_Status;

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
    }

    public Date getTime () {
        return time;
    }

    public void setTime (Date time) {
        this.time = time;
    }

    public String getBaseLine () {
        return baseLine;
    }

    public void setBaseLine (String baseLine) {
        this.baseLine = baseLine;
    }

    public String getQy_Status () {
        return qy_Status;
    }

    public void setQy_Status (String qy_Status) {
        this.qy_Status = qy_Status;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

}
