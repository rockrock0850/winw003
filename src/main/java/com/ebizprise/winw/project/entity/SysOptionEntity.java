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
 * @author gary.tsai 2019/6/27
 * 
 */
@Entity
@Table(name = "SYS_OPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysOptionEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id; //流水號
	private String optionId; //選單編號
    private Integer sort; //順序
	private String name; //選單名稱
	private String value; //選單值
	private String display; //選單顯示名稱
	private String parentId; //父選單編號
	private String active; //狀態 (啟用:Y/停用:N)
	private String isKnowledge; //是否加入知識庫 (Y/N)
	
	public SysOptionEntity() {
	}
	
    public SysOptionEntity(Long id, String optionId, String value, String display, String active) {
        this.id = id;
        this.optionId = optionId;
        this.value = value;
        this.display = display;
        this.active = active;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
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
    @Column(name = "Sort")
    public Integer getSort () {
        return sort;
    }

    public void setSort (Integer sort) {
        this.sort = sort;
    }

	@Basic
	@Column(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	@Column(name = "Display")
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@Basic
	@Column(name = "ParentId")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Basic
    @Column(name = "Active")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
    @Basic
    @Column(name = "IsKnowledge")
    public String getIsKnowledge () {
        return isKnowledge;
    }

    public void setIsKnowledge (String isKnowledge) {
        this.isKnowledge = isKnowledge;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysOptionEntity that = (SysOptionEntity) o;
		return id == that.id && optionId == that.optionId && Objects.equals(name, that.name)
				&& Objects.equals(value, that.value) && Objects.equals(display, that.display)
				&& Objects.equals(parentId, that.parentId) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, optionId, name, value, display, parentId, this.getUpdatedBy(), this.getUpdatedAt(),
				this.getCreatedBy(), this.getCreatedAt());
	}

}
