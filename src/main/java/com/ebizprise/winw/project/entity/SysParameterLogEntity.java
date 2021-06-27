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
 * @author gary.tsai 2019/6/14
 */
@Entity
@Table(name = "SYS_PARAMETER_LOG")
public class SysParameterLogEntity extends BaseEntity {

	private Long id;
	private Integer paramId;
	private String paramKey;
	private String paramValue;
	private String userId;
	private String action;
	private Boolean isPassword;
	private String description;
	private Date time;

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
	@Column(name = "ParamId")
	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	@Basic
	@Column(name = "ParamKey")
	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	@Basic
	@Column(name = "ParamValue")
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Basic
	@Column(name = "UserId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Basic
	@Column(name = "Action")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Basic
	@Column(name = "IsPassword")
	public Boolean getPassword() {
		return isPassword;
	}

	public void setPassword(Boolean password) {
		isPassword = password;
	}

	@Basic
	@Column(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "Time")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysParameterLogEntity that = (SysParameterLogEntity) o;
		return id == that.id && Objects.equals(paramId, that.paramId) && Objects.equals(paramKey, that.paramKey)
				&& Objects.equals(paramValue, that.paramValue) && Objects.equals(userId, that.userId)
				&& Objects.equals(action, that.action) && Objects.equals(isPassword, that.isPassword)
				&& Objects.equals(description, that.description) && Objects.equals(time, that.time)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, paramId, paramKey, paramValue, userId, action, isPassword, description, time,
				this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
