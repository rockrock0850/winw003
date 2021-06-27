package com.ebizprise.winw.project.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * 
 * The <code>SysOptionRoleEntity</code>	
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2020年3月5日
 */
@Entity
@Table(name = "SYS_OPTION_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysOptionRoleEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String roleId;
	private String optionId;
	private String value;
	private String description;
	private String conditon;

	@Id
	@Column(name = "Id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "OptionId")
	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	@Basic
	@Column(name = "Value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Basic
    @Column(name = "RoleId")
	public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
    @Column(name = "Condition")
    public String getCondition() {
        return conditon;
    }

    public void setCondition(String conditon) {
        this.conditon = conditon;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysOptionRoleEntity that = (SysOptionRoleEntity) o;
		return id == that.id && optionId == that.optionId 
				&& Objects.equals(value, that.value) 
				&& Objects.equals(roleId, that.roleId) 
				&& Objects.equals(description, that.description) 
				&& Objects.equals(conditon, that.conditon) 
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, optionId, value,roleId,description,conditon, this.getUpdatedBy(), this.getUpdatedAt(),
				this.getCreatedBy(), this.getCreatedAt());
	}
}
