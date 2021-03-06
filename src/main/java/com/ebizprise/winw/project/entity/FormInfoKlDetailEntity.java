package com.ebizprise.winw.project.entity;
// Generated 2021/4/13 上午 11:59:06 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * FormInfoKlDetail generated by hbm2java
 */
@Entity
@Table(name = "FORM_INFO_KL_DETAIL")
public class FormInfoKlDetailEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String summary;
    private String indication;
    private String reason;
    private String processProgram;
    private String systemId;
    private String system;
    private String assetGroup;
    private String sClass;
    private String sSubClass;
    private String knowledges;
    private String solutions;
    private String flowName;
    private String isEnabled;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
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

    @Column(name = "Summary")
    public String getSummary () {
        return this.summary;
    }

    public void setSummary (String summary) {
        this.summary = summary;
    }

    @Column(name = "Indication")
    public String getIndication () {
        return this.indication;
    }

    public void setIndication (String indication) {
        this.indication = indication;
    }

    @Column(name = "Reason")
    public String getReason () {
        return this.reason;
    }

    public void setReason (String reason) {
        this.reason = reason;
    }

    @Column(name = "ProcessProgram")
    public String getProcessProgram () {
        return this.processProgram;
    }

    public void setProcessProgram (String processProgram) {
        this.processProgram = processProgram;
    }

    @Column(name = "SystemId")
    public String getSystemId () {
        return this.systemId;
    }

    public void setSystemId (String systemId) {
        this.systemId = systemId;
    }

    @Column(name = "System")
    public String getSystem () {
        return this.system;
    }

    public void setSystem (String system) {
        this.system = system;
    }

    @Column(name = "AssetGroup")
    public String getAssetGroup () {
        return this.assetGroup;
    }

    public void setAssetGroup (String assetGroup) {
        this.assetGroup = assetGroup;
    }

    @Column(name = "SClass")
    public String getsClass () {
        return this.sClass;
    }

    public void setsClass (String sclass) {
        this.sClass = sclass;
    }

    @Column(name = "SSubClass")
    public String getsSubClass () {
        return this.sSubClass;
    }

    public void setsSubClass (String ssubClass) {
        this.sSubClass = ssubClass;
    }

    @Column(name = "Knowledges")
    public String getKnowledges () {
        return this.knowledges;
    }

    public void setKnowledges (String knowledges) {
        this.knowledges = knowledges;
    }

    @Column(name = "Solutions")
    public String getSolutions () {
        return this.solutions;
    }

    public void setSolutions (String solutions) {
        this.solutions = solutions;
    }

    @Column(name = "FlowName")
    public String getFlowName () {
        return flowName;
    }

    public void setFlowName (String flowName) {
        this.flowName = flowName;
    }

    @Column(name = "IsEnabled")
    public String getIsEnabled () {
        return isEnabled;
    }

    public void setIsEnabled (String isEnabled) {
        this.isEnabled = isEnabled;
    }

}
