package com.ebizprise.winw.project.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import com.ebizprise.winw.project.base.entity.BaseEntity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author gary.tsai 2019/6/21
 */
@Entity
@Table(name = "SYS_PARAMETER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysParameterEntity extends BaseEntity {

	private Long id;
	private int paramId;
	private String paramKey;
	private String paramValue;
	private String isPassword;
	private String description;

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
	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
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
	@Column(name = "IsPassword")
	public String getIsPassword() {
		return isPassword;
	}

	public void setIsPassword(String pwd) {
		this.isPassword = pwd;
	}

	@Basic
	@Column(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysParameterEntity that = (SysParameterEntity) o;
		return id == that.id && paramId == that.paramId && Objects.equals(paramKey, that.paramKey)
				&& Objects.equals(paramValue, that.paramValue) && Objects.equals(isPassword, that.isPassword)
				&& Objects.equals(description, that.description)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, paramId, paramKey, paramValue, isPassword, description, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
