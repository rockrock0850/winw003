package com.ebizprise.winw.project.vo;

import java.util.Date;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class LogonRecordVO extends BaseVO {

    private String userId;          // 帳號
    private String userName;        // 姓名
    private String email;           // 信箱
    private String status;          // 狀態
    private Date time;              // 時間
    private String queryStartDate;  // 查詢起始日期
    private String queryEndDate;    // 查詢結束日期
    private String ip;              // IP
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(String queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public String getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
