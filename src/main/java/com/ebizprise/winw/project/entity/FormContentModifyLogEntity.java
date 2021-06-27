package com.ebizprise.winw.project.entity;

import javax.persistence.*;

import com.ebizprise.winw.project.base.entity.BaseEntity;

import java.util.Objects;

/**
 * @author gary.tsai 2019/7/4
 */
@Entity
@Table(name = "FORM_CONTENT_MODIFY_LOG")
public class FormContentModifyLogEntity extends BaseEntity {
    private Long id;
    private String formId;
    private String contents;

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
    @Column(name = "Contents")
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormContentModifyLogEntity that = (FormContentModifyLogEntity) o;
        return id == that.id &&
                Objects.equals(formId, that.formId) &&
                Objects.equals(contents, that.contents) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
                && Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
                && Objects.equals(this.getCreatedBy(), this.getCreatedBy())
                && Objects.equals(this.getCreatedAt(), this.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formId, contents, this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(),
                this.getCreatedAt());
    }
}
