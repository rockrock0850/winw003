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
@Table(name = "FORM_INFO_USER")
public class FormInfoUserEntity extends BaseEntity {

	private Long id;
	private String formId;
	private String formClass;
	private String unitId;
	private String userId;
	private String questionId;
	private String phone;
	private String email;
	private String unitCategory;
	private String isForward;

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
	@Column(name = "FormClass")
	public String getFormClass() {
		return formClass;
	}

	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}

	@Basic
	@Column(name = "UnitId")
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "UserName")
	public String getUserName() {
		return userId;
	}

	public void setUserName(String userId) {
		this.userId = userId;
	}

	@Basic
	@Column(name = "QuestionId")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	@Basic
	@Column(name = "Phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Basic
	@Column(name = "Email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "UnitCategory")
	public String getUnitCategory() {
		return unitCategory;
	}

	public void setUnitCategory(String unitCategory) {
		this.unitCategory = unitCategory;
	}

	@Basic
	@Column(name = "IsForward")
	public String getIsForward() {
		return isForward;
	}

	public void setIsForward(String isForward) {
		this.isForward = isForward;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormInfoUserEntity that = (FormInfoUserEntity) o;
		return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(formClass, that.formClass)
				&& Objects.equals(unitId, that.unitId) && Objects.equals(userId, that.userId)
				&& Objects.equals(questionId, that.questionId) && Objects.equals(phone, that.phone)
				&& Objects.equals(email, that.email) && Objects.equals(unitCategory, that.unitCategory)
				&& Objects.equals(isForward, that.isForward) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, formId, formClass, unitId, userId, questionId, phone, email, unitCategory, isForward,
				this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
