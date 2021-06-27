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
 * @author gary.tsai 2019/6/26
 * @modifier AndrewLee 2019/6/26
 */
@Entity
@Table(name = "FORM_PROCESS")
public class FormProcessEntity extends BaseEntity {

	private Long id;
	private String processId;
	private int formType;
	private String division;
	private String departmentId;
	private String processName;
	private String isEnable;

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
	@Column(name = "ProcessId")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Basic
	@Column(name = "FormType")
	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
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
	@Column(name = "DepartmentId")
	public String getDepartmentId() {
	    return departmentId;
	}
	
	public void setDepartmentId(String departmentId) {
	    this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "ProcessName")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Basic
	@Column(name = "IsEnable")
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormProcessEntity that = (FormProcessEntity) o;
		return id == that.id && formType == that.formType && Objects.equals(processName, that.processName)
				&& Objects.equals(processId, that.processId) && Objects.equals(division, that.division) && Objects.equals(departmentId, that.departmentId)
				&& Objects.equals(isEnable, that.isEnable) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, processId, formType, division,departmentId, processName, isEnable, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
