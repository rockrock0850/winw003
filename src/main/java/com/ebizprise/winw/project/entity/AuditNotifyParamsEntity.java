package com.ebizprise.winw.project.entity;
// Generated 2020/9/1 下午 04:21:07 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * AuditNotifyParams generated by hbm2java
 */
@Entity
@Table(name = "AUDIT_NOTIFY_PARAMS")
public class AuditNotifyParamsEntity extends BaseEntity {

    private Long id;
    private String time;
    private String isSc;
    private String isD1;
    private String isD2;
    private String isPic;
    private String isVsc;
    private String formType;
    private String notifyType;
    private String notifyMails;

    public AuditNotifyParamsEntity() {
    }

    public AuditNotifyParamsEntity(Long id, String formType, String notifyType, String isSc, String isD1,
            String isD2, String isPic, String isVsc) {
        this.id = id;
        this.formType = formType;
        this.notifyType = notifyType;
        this.isSc = isSc;
        this.isD1 = isD1;
        this.isD2 = isD2;
        this.isPic = isPic;
        this.isVsc = isVsc;
    }

    public AuditNotifyParamsEntity(Long id, String updatedBy, Date updatedAt, String createdBy,
            Date createdAt, String formType, String notifyType, String time, String isSc, String isD1,
            String isD2, String isPic, String isVsc, String notifyMails) {
        this.id = id;
        this.formType = formType;
        this.notifyType = notifyType;
        this.time = time;
        this.isSc = isSc;
        this.isD1 = isD1;
        this.isD2 = isD2;
        this.isPic = isPic;
        this.isVsc = isVsc;
        this.notifyMails = notifyMails;
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

    @Column(name = "FormType", nullable = false)
    public String getFormType () {
        return this.formType;
    }

    public void setFormType (String formType) {
        this.formType = formType;
    }

    @Column(name = "NotifyType", nullable = false)
    public String getNotifyType () {
        return this.notifyType;
    }

    public void setNotifyType (String notifyType) {
        this.notifyType = notifyType;
    }

    @Column(name = "Time")
    public String getTime () {
        return this.time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    @Column(name = "IsSc", nullable = false, length = 1)
    public String getIsSc () {
        return this.isSc;
    }

    public void setIsSc (String isSc) {
        this.isSc = isSc;
    }

    @Column(name = "IsD1", nullable = false, length = 1)
    public String getIsD1 () {
        return this.isD1;
    }

    public void setIsD1 (String isD1) {
        this.isD1 = isD1;
    }

    @Column(name = "IsD2", nullable = false, length = 1)
    public String getIsD2 () {
        return this.isD2;
    }

    public void setIsD2 (String isD2) {
        this.isD2 = isD2;
    }

    @Column(name = "IsPic", nullable = false, length = 1)
    public String getIsPic () {
        return this.isPic;
    }

    public void setIsPic (String isPic) {
        this.isPic = isPic;
    }

    @Column(name = "IsVsc", nullable = false, length = 1)
    public String getIsVsc () {
        return this.isVsc;
    }

    public void setIsVsc (String isVsc) {
        this.isVsc = isVsc;
    }

    @Column(name = "NotifyMails")
    public String getNotifyMails () {
        return this.notifyMails;
    }

    public void setNotifyMails (String notifyMails) {
        this.notifyMails = notifyMails;
    }

}