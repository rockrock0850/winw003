package com.ebizprise.winw.project.vo;

import java.util.Date;

import com.ebizprise.winw.project.base.vo.BaseVO;
import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;

public class AuditInformVO extends BaseVO {

    private String formId;
    private String eventPriority;    //事件優先順序
    private String summary;          //摘要
    private String content;
    private Date ect;                //預計完成時間
    private Date createTime;
    private String formClass;
    private String createName;       //開單人員姓名
    private String solvingName;      //處理人員姓名
    private String userCreated;      //開單人員ID
    private String userSolving;      //處理人員ID
    private String divisionSolving;
    private String detailId;
    private String verifyLevel;
    private String departmentId;
    private String division;
    private String limit;
    private String verifyType;
    
    private boolean isOverdue;       //是否逾期
    private String mailTo;           //收信人(處理人員姓名)
    private String reportPic;        //表單的經辦
    private AuditNotifyParamsEntity anParam;
    
    public String getVerifyType() {
        return verifyType;
    }
    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }
    public String getFormId() {
        return formId;
    }
    public void setFormId(String formId) {
        this.formId = formId;
    }
    public String getEventPriority() {
        return eventPriority;
    }
    public void setEventPriority(String eventPriority) {
        this.eventPriority = eventPriority;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getEct() {
        return ect;
    }
    public void setEct(Date ect) {
        this.ect = ect;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public boolean isOverdue() {
        return isOverdue;
    }
    public void setOverdue(boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    public String getMailTo() {
        return mailTo;
    }
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
    public String getReportPic() {
        return reportPic;
    }
    public void setReportPic(String reportPic) {
        this.reportPic = reportPic;
    }
    public String getFormClass() {
        return formClass;
    }
    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getSolvingName() {
        return solvingName;
    }
    public void setSolvingName(String solvingName) {
        this.solvingName = solvingName;
    }
    public String getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
    public String getUserSolving() {
        return userSolving;
    }
    public void setUserSolving(String userSolving) {
        this.userSolving = userSolving;
    }
    public String getDivisionSolving() {
        return divisionSolving;
    }
    public void setDivisionSolving(String divisionSolving) {
        this.divisionSolving = divisionSolving;
    }
    public String getDetailId() {
        return detailId;
    }
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
    public String getVerifyLevel() {
        return verifyLevel;
    }
    public void setVerifyLevel(String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }
    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public AuditNotifyParamsEntity getAnParam() {
        return anParam;
    }
    public void setAnParam(AuditNotifyParamsEntity anParam) {
        this.anParam = anParam;
    }
    public String getLimit () {
        return limit;
    }
    public void setLimit (String limit) {
        this.limit = limit;
    }
}
