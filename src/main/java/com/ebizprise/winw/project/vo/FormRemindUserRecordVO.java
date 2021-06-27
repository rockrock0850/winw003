package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class FormRemindUserRecordVO extends BaseVO {

    private String formId;
    private String userCreated;
    private String divisionSolving;
    private String eCT;
    private String createTime;
    private String verifyLevel;
    private String detailId;
    private String formClass;
    private boolean checkSend;// mail是否發送成功
    private String logMessage;// 寄送mail失敗的訊息

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getDivisionSolving() {
        return divisionSolving;
    }

    public void setDivisionSolving(String divisionSolving) {
        this.divisionSolving = divisionSolving;
    }

    public String geteCT() {
        return eCT;
    }

    public void seteCT(String eCT) {
        this.eCT = eCT;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVerifyLevel() {
        return verifyLevel;
    }

    public void setVerifyLevel(String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFormClass() {
        return formClass;
    }

    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }

    public boolean isCheckSend() {
        return checkSend;
    }

    public void setCheckSend(boolean checkSend) {
        this.checkSend = checkSend;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
