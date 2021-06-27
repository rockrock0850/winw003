package com.ebizprise.winw.project.entity;
// Generated 2019/7/2 下午 04:39:10 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Basic;
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
 * FormVerifyLog generated by hbm2java
 */
@Entity
@Table(name = "FORM_VERIFY_LOG")
public class FormVerifyLogEntity extends BaseEntity {

    private long id;
    private String formId;
    private String verifyLevel;
    private String verifyType;
    private Date submitTime;
    private Date completeTime;
    private String userId;
    private String verifyResult;
    private String verifyComment;
    private String parallel;
    private String groupId;

    public FormVerifyLogEntity() {
    }

    public FormVerifyLogEntity(long id, String formId, String verifyLevel) {
        this.id = id;
        this.formId = formId;
        this.verifyLevel = verifyLevel;
    }

    public FormVerifyLogEntity(long id, String formId, String verifyLevel, Date submitTime, Date completeTime,
            String userId, String verifyResult, String verifyComment, String updatedBy, Date updatedAt,
            String createdBy, Date createdAt, String parallel, String groupId) {
        this.id = id;
        this.formId = formId;
        this.verifyLevel = verifyLevel;
        this.submitTime = submitTime;
        this.completeTime = completeTime;
        this.userId = userId;
        this.verifyResult = verifyResult;
        this.verifyComment = verifyComment;
        this.parallel = parallel;
        this.groupId = groupId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    public long getId () {
        return this.id;
    }

    public void setId (long id) {
        this.id = id;
    }

    @Column(name = "FormId", unique = true, nullable = false)
    public String getFormId () {
        return this.formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    @Column(name = "VerifyLevel", nullable = false)
    public String getVerifyLevel () {
        return this.verifyLevel;
    }

    public void setVerifyLevel (String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SubmitTime", length = 23)
    public Date getSubmitTime () {
        return this.submitTime;
    }

    public void setSubmitTime (Date submitTime) {
        this.submitTime = submitTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CompleteTime", length = 23)
    public Date getCompleteTime () {
        return this.completeTime;
    }

    public void setCompleteTime (Date completeTime) {
        this.completeTime = completeTime;
    }

    @Column(name = "UserId")
    public String getUserId () {
        return this.userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    @Column(name = "VerifyResult")
    public String getVerifyResult () {
        return this.verifyResult;
    }

    public void setVerifyResult (String verifyResult) {
        this.verifyResult = verifyResult;
    }

    @Column(name = "VerifyComment")
    public String getVerifyComment () {
        return this.verifyComment;
    }

    public void setVerifyComment (String verifyComment) {
        this.verifyComment = verifyComment;
    }

    @Column(name = "VerifyType")
    public String getVerifyType () {
        return verifyType;
    }

    public void setVerifyType (String verifyType) {
        this.verifyType = verifyType;
    }

    public String getParallel () {
        return parallel;
    }

    public void setParallel (String parallel) {
        this.parallel = parallel;
    }

    @Basic
    @Column(name = "GroupId")
    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

}