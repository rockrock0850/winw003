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
 * FormJobWorking generated by hbm2java
 */
@Entity
@Table(name = "FORM_JOB_WORKING")
public class FormJobWorkingEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String type;
    private String dbname;
    private String psbname;
    private String alterWay;
    private String Veew;
    private String stream;
    private String ucmproject;
    private Date eit;
    private Date ast;
    private String remark;

    public FormJobWorkingEntity() {
    }

    public FormJobWorkingEntity(Long id, String formId, String type) {
        this.id = id;
        this.formId = formId;
        this.type = type;
    }

    public FormJobWorkingEntity(Long id, String formId, String type, String dbname, String psbname,
            String alterWay, String view, String stream, String ucmproject, Date eit, String updatedBy,
            Date updatedAt, String createdBy, Date createdAt, String remark, Date ast) {
        this.id = id;
        this.formId = formId;
        this.type = type;
        this.dbname = dbname;
        this.psbname = psbname;
        this.alterWay = alterWay;
        this.Veew = view;
        this.stream = stream;
        this.ucmproject = ucmproject;
        this.eit = eit;
        this.ast = ast;
        this.remark = remark;
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

    @Column(name = "Type", nullable = false)
    public String getType () {
        return this.type;
    }

    public void setType (String type) {
        this.type = type;
    }

    @Column(name = "DBName")
    public String getDbname () {
        return this.dbname;
    }

    public void setDbname (String dbname) {
        this.dbname = dbname;
    }

    @Column(name = "PSBName")
    public String getPsbname () {
        return this.psbname;
    }

    public void setPsbname (String psbname) {
        this.psbname = psbname;
    }

    @Column(name = "AlterWay")
    public String getAlterWay () {
        return this.alterWay;
    }

    public void setAlterWay (String alterWay) {
        this.alterWay = alterWay;
    }

    @Column(name = "Veew")
    public String getVeew () {
        return this.Veew;
    }

    public void setVeew (String veew) {
        this.Veew = veew;
    }

    @Column(name = "Stream")
    public String getStream () {
        return this.stream;
    }

    public void setStream (String stream) {
        this.stream = stream;
    }

    @Column(name = "UCMProject")
    public String getUcmproject () {
        return this.ucmproject;
    }

    public void setUcmproject (String ucmproject) {
        this.ucmproject = ucmproject;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EIT", length = 23)
    public Date getEit () {
        return this.eit;
    }

    public void setEit (Date eit) {
        this.eit = eit;
    }

    @Column(name = "Remark")
    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AST", length = 23)
    public Date getAst () {
        return ast;
    }

    public void setAst (Date ast) {
        this.ast = ast;
    }

}
