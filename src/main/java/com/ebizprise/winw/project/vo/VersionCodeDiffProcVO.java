package com.ebizprise.winw.project.vo;

import java.util.Date;

import com.ebizprise.winw.project.base.vo.BaseResponseVO;

/**
 * 查詢工作單是否有版控差異資訊檔案的請求資料物件
 * @author adam.yeh
 */
public class VersionCodeDiffProcVO extends BaseResponseVO {

    private String fn;        // 表單編號
    private Date time;        // 查詢時間
    private String msg;       // 代表訊息
    private String fileName;  // 要取得比對檔案的名稱

    /*
     * 0 = 工作單單號未綁定任何版控分支
     * 1 = 已有比對資料可取得
     * 2 = 此表單尚未執行差異比對作業
     * 3 = 比對資料已產出差異檔案已不存在請重新執行差異比對
     * 4 = 版控管理系統作業異常
     */
    private String qy_status;

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public String getQy_Status () {
        return qy_status;
    }

    public void setQy_Status (String qy_status) {
        this.qy_status = qy_status;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public Date getTime () {
        return time;
    }

    public void setTime (Date time) {
        this.time = time;
    }

}
