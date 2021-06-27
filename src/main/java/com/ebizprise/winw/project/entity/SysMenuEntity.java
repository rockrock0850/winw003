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
 * @author gary.tsai 2019/6/14
 */
@Entity
@Table(name = "SYS_MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
public class SysMenuEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
    private String menuId;
	private String menuName;
	private String path;
	private String comment;
	private Integer orderId;
	private Boolean enabled;
    private String parentId;
    private Boolean openWindow;

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
	@Column(name = "MenuId")
    public String getMenuId() {
		return menuId;
	}

    public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@Basic
	@Column(name = "MenuName")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Basic
	@Column(name = "Path")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Basic
	@Column(name = "Comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Basic
	@Column(name = "OrderId")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Basic
	@Column(name = "Enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
    @Column(name = "OpenWindow")
    public Boolean getOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(Boolean openWindow) {
        this.openWindow = openWindow;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysMenuEntity that = (SysMenuEntity) o;
		return id == that.id && menuId == that.menuId && Objects.equals(menuName, that.menuName)
				&& Objects.equals(path, that.path) && Objects.equals(comment, that.comment)
				&& Objects.equals(orderId, that.orderId) && Objects.equals(enabled, that.enabled)
				&& Objects.equals(parentId, that.parentId) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt())
		        && Objects.equals(this.openWindow, this.openWindow);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, menuId, menuName, path, comment, orderId, enabled, parentId, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt(),openWindow);
	}

}
