package com.ebizprise.winw.project.entity;
// Generated 2019/8/30 下午 02:45:36 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * FormJobBatch generated by hbm2java
 */
@Entity
@Table(name = "FORM_JOB_BATCH")
public class FormJobBatchEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String userId;
    private String psb;
    private String programName;
    private String programNumber;
    private String jcl;
    private String cljcl;
    private Date it;
    private String isHelp;
    private String isCange;
    private String isAllow;
    private String isOther;
    private String isHelpCl;
    private String isRollback;
    private String isOtherDesc;
    private String reason;
    private String content;
    private String other;
    private String remark;
    private String countersignedIDC;

    public FormJobBatchEntity() {
    }

    public FormJobBatchEntity(Long id, String formId) {
        this.id = id;
        this.formId = formId;
    }

    public FormJobBatchEntity(Long id, String formId, String userId, String psb, String programName,
            String programNumber, String jcl, String cljcl, Date it, String isHelp, String isCange,
            String isAllow, String isOther, String isHelpCl, String isRollback, String isOtherDesc,
            String reason, String content, String other, String remark, String updatedBy, Date updatedAt,
            String createdBy, Date createdAt, String countersignedIDC) {
        this.id = id;
        this.formId = formId;
        this.userId = userId;
        this.psb = psb;
        this.programName = programName;
        this.programNumber = programNumber;
        this.jcl = jcl;
        this.cljcl = cljcl;
        this.it = it;
        this.isHelp = isHelp;
        this.isCange = isCange;
        this.isAllow = isAllow;
        this.isOther = isOther;
        this.isHelpCl = isHelpCl;
        this.isRollback = isRollback;
        this.isOtherDesc = isOtherDesc;
        this.reason = reason;
        this.content = content;
        this.other = other;
        this.remark = remark;
        this.countersignedIDC = countersignedIDC;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    @Column(name = "FormId", unique = true, nullable = false)
    public String getFormId () {
        return this.formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    @Column(name = "UserId")
    public String getUserId () {
        return this.userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    @Column(name = "PSB")
    public String getPsb () {
        return this.psb;
    }

    public void setPsb (String psb) {
        this.psb = psb;
    }

    @Column(name = "ProgramName")
    public String getProgramName () {
        return this.programName;
    }

    public void setProgramName (String programName) {
        this.programName = programName;
    }

    @Column(name = "ProgramNumber")
    public String getProgramNumber () {
        return this.programNumber;
    }

    public void setProgramNumber (String programNumber) {
        this.programNumber = programNumber;
    }

    @Column(name = "JCL")
    public String getJcl () {
        return this.jcl;
    }

    public void setJcl (String jcl) {
        this.jcl = jcl;
    }

    @Column(name = "CLJCL")
    public String getCljcl () {
        return this.cljcl;
    }

    public void setCljcl (String cljcl) {
        this.cljcl = cljcl;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IT", length = 23)
    public Date getIt () {
        return this.it;
    }

    public void setIt (Date it) {
        this.it = it;
    }

    @Column(name = "IsHelp", length = 1)
    public String getIsHelp () {
        return this.isHelp;
    }

    public void setIsHelp (String isHelp) {
        this.isHelp = isHelp;
    }

    @Column(name = "IsCange", length = 1)
    public String getIsCange () {
        return this.isCange;
    }

    public void setIsCange (String isCange) {
        this.isCange = isCange;
    }

    @Column(name = "IsAllow", length = 1)
    public String getIsAllow () {
        return this.isAllow;
    }

    public void setIsAllow (String isAllow) {
        this.isAllow = isAllow;
    }

    @Column(name = "IsOther", length = 1)
    public String getIsOther () {
        return this.isOther;
    }

    public void setIsOther (String isOther) {
        this.isOther = isOther;
    }

    @Column(name = "IsHelpCL", length = 1)
    public String getIsHelpCl () {
        return this.isHelpCl;
    }

    public void setIsHelpCl (String isHelpCl) {
        this.isHelpCl = isHelpCl;
    }

    @Column(name = "IsRollback", length = 1)
    public String getIsRollback () {
        return this.isRollback;
    }

    public void setIsRollback (String isRollback) {
        this.isRollback = isRollback;
    }

    @Column(name = "IsOtherDesc", length = 1)
    public String getIsOtherDesc () {
        return this.isOtherDesc;
    }

    public void setIsOtherDesc (String isOtherDesc) {
        this.isOtherDesc = isOtherDesc;
    }

    @Column(name = "Reason")
    public String getReason () {
        return this.reason;
    }

    public void setReason (String reason) {
        this.reason = reason;
    }

    @Column(name = "Content")
    public String getContent () {
        return this.content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    @Column(name = "Other")
    public String getOther () {
        return this.other;
    }

    public void setOther (String other) {
        this.other = other;
    }

    @Column(name = "Remark")
    public String getRemark () {
        return this.remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    @Column(name = "CountersignedIDC")
    public String getCountersignedIDC () {
    	return this.countersignedIDC;
    }
    
    public void setCountersignedIDC (String countersignedIDC) {
    	this.countersignedIDC = countersignedIDC;
    }
}
