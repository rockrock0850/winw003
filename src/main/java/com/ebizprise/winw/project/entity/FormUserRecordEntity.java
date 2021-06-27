package com.ebizprise.winw.project.entity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * @author gary.tsai 2019/6/27
 */
@Entity
@Table(name = "FORM_USER_RECORD")
public class FormUserRecordEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String summary;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FormId")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Basic
    @Column(name = "Summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormUserRecordEntity that = (FormUserRecordEntity) o;
        return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(summary, that.summary)
                && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
                && Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
                && Objects.equals(this.getCreatedBy(), this.getCreatedBy())
                && Objects.equals(this.getCreatedAt(), this.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formId, summary, this.getUpdatedBy(), this.getUpdatedAt(),
                this.getCreatedBy(), this.getCreatedAt());
    }
}
