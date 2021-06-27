package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 稽催通知參數設定 資料物件
 * @author adam.yeh
 */
public class AuditNotifyParamsVO extends BaseVO {
    
    String isSc;                      // 科長
    String isD1;                      // 副理
    String isD2;                      // 協理
    String isPic;                     // 經辦
    String isVsc;                     // 副科
    String time;                      // 即將逾期臨界值
    String formType;                  // 表單類型
    String notifyType;                // 通知類型 (即將逾期/已逾期)
    String notifyMails;               // 負責人的郵件清單
    List<AuditNotifyParamsVO> params; // 通知參數清單

    public List<AuditNotifyParamsVO> getParams () {
        return params;
    }

    public void setParams (List<AuditNotifyParamsVO> params) {
        this.params = params;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getIsSc () {
        return isSc;
    }

    public void setIsSc (String sc) {
        this.isSc = sc;
    }

    public String getIsD1 () {
        return isD1;
    }

    public void setIsD1 (String d1) {
        this.isD1 = d1;
    }

    public String getIsD2 () {
        return isD2;
    }

    public void setIsD2 (String d2) {
        this.isD2 = d2;
    }

    public String getIsPic () {
        return isPic;
    }

    public void setIsPic (String pic) {
        this.isPic = pic;
    }

    public String getIsVsc () {
        return isVsc;
    }

    public void setIsVsc (String vsc) {
        this.isVsc = vsc;
    }

    public String getNotifyMails () {
        return notifyMails;
    }

    public void setNotifyMails (String notifyMails) {
        this.notifyMails = notifyMails;
    }

    public String getFormType () {
        return formType;
    }

    public void setFormType (String formType) {
        this.formType = formType;
    }

    public String getNotifyType () {
        return notifyType;
    }

    public void setNotifyType (String notifyType) {
        this.notifyType = notifyType;
    }
    
}
