package com.ebizprise.winw.project.base.vo;

import java.util.Date;

import com.ebizprise.project.utility.bean.BeanUtil;

/**
 * Web Service/API共用基礎類別
 * 
 * @author adam.yeh
 */
public abstract class BaseRequestVO {
    
    /*
     * 中文字為保傳送相容性, 使用utf8格式並轉換成base64編碼, 接收端再將base64轉回utf8
     */
    private String msg;  // 回傳訊息
    private Date time;   // 查詢時間

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
    
    @Override
    public String toString () {
        return BeanUtil.toJson(this);
    }

}
