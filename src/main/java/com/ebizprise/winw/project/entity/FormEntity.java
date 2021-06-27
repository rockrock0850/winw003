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
@Table(name = "FORM")
public class FormEntity extends BaseEntity {

	private Long id;
	private String formId;
	private String detailId;
	private String sourceId;
	private String formClass;
	private String processName;
	private String formStatus;
	private String processStatus;
	private String divisionCreated;
	private String userCreated;
	private String divisionSolving;
	private String userSolving;
	private String groupSolving;
	private Date createTime;
    private String parallel;
    private String IsAlterDone;
    private String isExtended;

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
	@Column(name = "SourceId")
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
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
	@Column(name = "ProcessName")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Basic
	@Column(name = "FormStatus")
	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	@Basic
	@Column(name = "ProcessStatus")
	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	@Basic
	@Column(name = "DivisionCreated")
	public String getDivisionCreated() {
		return divisionCreated;
	}

	public void setDivisionCreated(String divisionCreated) {
		this.divisionCreated = divisionCreated;
	}

	@Basic
	@Column(name = "UserCreated")
	public String getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(String userCreated) {
		this.userCreated = userCreated;
	}

	@Basic
	@Column(name = "DivisionSolving")
	public String getDivisionSolving() {
		return divisionSolving;
	}

	public void setDivisionSolving(String divisionSolving) {
		this.divisionSolving = divisionSolving;
	}

	@Basic
	@Column(name = "UserSolving")
	public String getUserSolving() {
		return userSolving;
	}

	public void setUserSolving(String userSolving) {
		this.userSolving = userSolving;
	}

	@Basic
	@Column(name = "GroupSolving")
	public String getGroupSolving() {
		return groupSolving;
	}

	public void setGroupSolving(String groupSolving) {
		this.groupSolving = groupSolving;
	}

	@Basic
	@Column(name = "CreateTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Basic
	@Column(name = "DetailId")
	public String getDetailId () {
		return detailId;
	}

	public void setDetailId (String detailId) {
		this.detailId = detailId;
	}

    @Basic
    @Column(name = "Parallel")
    public String getParallel () {
        return parallel;
    }

    public void setParallel (String parallel) {
        this.parallel = parallel;
    }

    @Basic
    @Column(name = "IsAlterDone")
    public String getIsAlterDone () {
        return IsAlterDone;
    }

    public void setIsAlterDone (String isAlterDone) {
        IsAlterDone = isAlterDone;
    }

    @Basic
    @Column(name = "IsExtended")
    public String getIsExtended() {
        return isExtended;
    }

    public void setIsExtended(String isExtended) {
        this.isExtended = isExtended;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormEntity that = (FormEntity) o;
		return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(sourceId, that.sourceId)
				&& Objects.equals(formClass, that.formClass) && Objects.equals(processName, that.processName)
				&& Objects.equals(formStatus, that.formStatus) && Objects.equals(processStatus, that.processStatus)
				&& Objects.equals(divisionCreated, that.divisionCreated)
				&& Objects.equals(userCreated, that.userCreated)
				&& Objects.equals(divisionSolving, that.divisionSolving)
				&& Objects.equals(userSolving, that.userSolving) && Objects.equals(groupSolving, that.groupSolving)
				&& Objects.equals(createTime, that.createTime)
                && Objects.equals(parallel, that.parallel)
                && Objects.equals(isExtended, that.isExtended)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, formId, sourceId, formClass, processName, formStatus, processStatus, divisionCreated,
                userCreated, divisionSolving, userSolving, groupSolving, parallel, isExtended,
                this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
	
}
