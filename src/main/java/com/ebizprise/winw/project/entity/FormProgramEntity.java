package com.ebizprise.winw.project.entity;
// Generated 2019/7/22 下午 02:10:25 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * FormProgram generated by hbm2java
 */
@Entity
@Table(name = "FORM_PROGRAM")
public class FormProgramEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String indication;
    private String reason;
    private String processProgram;
    private String deprecatedReason;
    private String isSuggestCase;
    private String temporary;
    private String knowledges;

    public FormProgramEntity() {
    }

    public FormProgramEntity(Long id, String formId) {
        this.id = id;
        this.formId = formId;
    }
    public FormProgramEntity(Long id, String formId, String indication, String reason, String processProgram,
            String deprecatedReason,String isSuggestCase, String temporary, String knowledges) {
        this.id = id;
        this.formId = formId;
        this.indication = indication;
        this.reason = reason;
        this.processProgram = processProgram;
        this.deprecatedReason = deprecatedReason;
        this.isSuggestCase = isSuggestCase;
        this.temporary = temporary;
        this.knowledges = knowledges;
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

    @Column(name = "DeprecatedReason")
    public String getDeprecatedReason () {
        return this.deprecatedReason;
    }

    public void setDeprecatedReason (String deprecatedReason) {
        this.deprecatedReason = deprecatedReason;
    }

    @Column(name = "IsSuggestCase")
    public String getIsSuggestCase() {
        return isSuggestCase;
    }

    public void setIsSuggestCase(String isSuggestCase) {
        this.isSuggestCase = isSuggestCase;
    }

    @Column(name = "Temporary")
    public String getTemporary () {
        return temporary;
    }

    public void setTemporary (String temporary) {
        this.temporary = temporary;
    }

    @Column(name = "Knowledges")
    public String getKnowledges () {
        return knowledges;
    }

    public void setKnowledges (String knowledges) {
        this.knowledges = knowledges;
    }

}
