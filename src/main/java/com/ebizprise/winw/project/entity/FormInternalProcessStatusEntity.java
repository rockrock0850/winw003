package com.ebizprise.winw.project.entity;

import java.util.Date;
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
 * @author gary.tsai 2019/6/26
 */
@Entity
@Table(name = "FORM_INTERNAL_PROCESS_STATUS")
public class FormInternalProcessStatusEntity extends BaseEntity {

	private Long id;
	private String formId;
	private String division;
	private String isProcessDone;

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
	@Column(name = "Division")
	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	@Basic
	@Column(name = "IsProcessDone")
	public String getIsProcessDone() {
		return isProcessDone;
	}

	public void setIsProcessDone(String isProcessDone) {
		this.isProcessDone = isProcessDone;
	}

}
