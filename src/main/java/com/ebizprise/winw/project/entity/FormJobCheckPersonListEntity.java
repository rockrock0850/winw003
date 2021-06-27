package com.ebizprise.winw.project.entity;
// Generated 2019/8/30 下午 02:45:36 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * FormJobCheckPersonList generated by hbm2java
 */
@Entity
@Table(name = "FORM_JOB_CHECK_PERSON_LIST")
public class FormJobCheckPersonListEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String sort;
    private String groupId;
    private String level;
    private String userId;

    public FormJobCheckPersonListEntity() {
    }

    public FormJobCheckPersonListEntity(Long id, String formId) {
        this.id = id;
        this.formId = formId;
    }

    public FormJobCheckPersonListEntity(Long id, String formId, String sort, String groupId, String level,
            String userId, String updatedBy, Date updatedAt, String createdBy, Date createdAt) {
        this.id = id;
        this.formId = formId;
        this.sort = sort;
        this.groupId = groupId;
        this.level = level;
        this.userId = userId;
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

    @Column(name = "FormId", nullable = false)
    public String getFormId () {
        return this.formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    @Column(name = "GroupId")
    public String getGroupId () {
        return this.groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "Level")
    public String getLevel () {
        return this.level;
    }

    public void setLevel (String level) {
        this.level = level;
    }

    @Column(name = "UserId")
    public String getUserId () {
        return this.userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    @Column(name = "Sort")
    public String getSort () {
        return this.sort;
    }

    public void setSort (String sort) {
        this.sort = sort;
    }

}
