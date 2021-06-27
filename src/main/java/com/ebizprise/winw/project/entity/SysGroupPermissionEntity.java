package com.ebizprise.winw.project.entity;

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
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年6月27日
 */
@Entity
@Table(name = "SYS_GROUP_PERMISSION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysGroupPermissionEntity extends BaseEntity {

	private Long id;
	private Long sysGroupId;
    private String menuId;

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
    @Column(name = "MenuId")
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysGroupPermissionEntity that = (SysGroupPermissionEntity) o;
        return id == that.id && sysGroupId == that.sysGroupId && Objects.equals(menuId, that.menuId);
	}

	@Override
	public int hashCode() {
        return Objects.hash(id, sysGroupId, menuId);
	}
}
