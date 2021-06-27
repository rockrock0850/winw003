package com.ebizprise.winw.project.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * @author gary.tsai 2019/6/26
 */
@Entity
@Table(name = "SYS_GROUP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysGroupEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long sysGroupId;
	private String departmentId;
	private String departmentName;
	private String division;
	private String groupId;
	private String groupName;
    private String authType;
    private String isAllowBatchReview;
    private String isDisplayKpi;

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
	@Column(name = "SysGroupId")
	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
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
	@Column(name = "DepartmentName")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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
	@Column(name = "GroupId")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Basic
	@Column(name = "GroupName")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Basic
	@Column(name = "IsAllowBatchReview")
    public String getAllowBatchReview() {
		return isAllowBatchReview;
	}

    public void setAllowBatchReview(String allowBatchReview) {
        this.isAllowBatchReview = allowBatchReview;
	}

	@Basic
	@Column(name = "IsDisplayKpi")
    public String getDisplayKpi() {
		return isDisplayKpi;
	}

    public void setDisplayKpi(String displayKpi) {
        this.isDisplayKpi = displayKpi;
	}

    @Basic
    @Column(name = "AuthType")
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysGroupEntity that = (SysGroupEntity) o;
		return id == that.id && sysGroupId == that.sysGroupId && Objects.equals(departmentId, that.departmentId)
				&& Objects.equals(departmentName, that.departmentName) && Objects.equals(division, that.division)
				&& Objects.equals(groupId, that.groupId) && Objects.equals(groupName, that.groupName)
                && Objects.equals(authType, that.authType)
				&& Objects.equals(isAllowBatchReview, that.isAllowBatchReview)
				&& Objects.equals(isDisplayKpi, that.isDisplayKpi)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sysGroupId, departmentId, departmentName, division, groupId, groupName,
                authType, isAllowBatchReview, isDisplayKpi, this.getUpdatedBy(), this.getUpdatedAt(),
                this.getCreatedBy(), this.getCreatedAt());
	}
}
