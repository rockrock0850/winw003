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
 * @author gary.tsai 2019/6/28
 */
@Entity
@Table(name = "LDAP_USER")
public class LdapUserEntity extends BaseEntity {

	private Long id;
	private String userId;
	private String name;
	private String rocId;
	private String title;
	private String password;
	private String phone;
	private String email;
	private String ldapDn;
	private String ldapOu;
	private String groups;
	private String sysGroupId;
	private String subGroup;
	private String authorLevel;
	private String isEnabled;
	private String isSynced;

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
	@Column(name = "UserId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	@Column(name = "RocId")
	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	@Basic
	@Column(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Basic
	@Column(name = "Password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "Phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Basic
	@Column(name = "Email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "LdapDn")
	public String getLdapDn() {
		return ldapDn;
	}

	public void setLdapDn(String ldapDn) {
		this.ldapDn = ldapDn;
	}

	@Basic
	@Column(name = "LdapOu")
	public String getLdapOu() {
		return ldapOu;
	}

	public void setLdapOu(String ldapOu) {
		this.ldapOu = ldapOu;
	}

	@Basic
	@Column(name = "Groups")
	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	@Basic
	@Column(name = "SysGroupId")
	public String getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(String sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	@Basic
	@Column(name = "SubGroup")
	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	@Basic
	@Column(name = "AuthorLevel")
	public String getAuthorLevel() {
		return authorLevel;
	}

	public void setAuthorLevel(String authorLevel) {
		this.authorLevel = authorLevel;
	}

	@Basic
	@Column(name = "IsEnabled")
	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Basic
	@Column(name = "IsSynced")
	public String getIsSynced() {
		return isSynced;
	}

	public void setIsSynced(String isSynced) {
		this.isSynced = isSynced;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LdapUserEntity that = (LdapUserEntity) o;
		return id == that.id && Objects.equals(userId, that.userId) && Objects.equals(name, that.name)
				&& Objects.equals(rocId, that.rocId) && Objects.equals(title, that.title)
				&& Objects.equals(password, that.password) && Objects.equals(phone, that.phone)
				&& Objects.equals(email, that.email) && Objects.equals(ldapDn, that.ldapDn)
				&& Objects.equals(ldapOu, that.ldapOu) && Objects.equals(groups, that.groups)
				&& Objects.equals(sysGroupId, that.sysGroupId) && Objects.equals(authorLevel, that.authorLevel)
				&& Objects.equals(isEnabled, that.isEnabled) && Objects.equals(isSynced, that.isSynced)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, name, rocId, title, password, phone, email, ldapDn, ldapOu, groups, sysGroupId,
				authorLevel, isEnabled, isSynced, this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(),
				this.getCreatedAt());
	}

	@Override
	public String toString() {
		return "LdapUserEntity{" +
				"id=" + id +
				", userId='" + userId + '\'' +
				", name='" + name + '\'' +
				", rocId='" + rocId + '\'' +
				", title='" + title + '\'' +
				", password='" + password + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", ldapDn='" + ldapDn + '\'' +
				", ldapOu='" + ldapOu + '\'' +
				", groups='" + groups + '\'' +
				", sysGroupId='" + sysGroupId + '\'' +
				", authorLevel='" + authorLevel + '\'' +
				", isEnabled='" + isEnabled + '\'' +
				", isSynced='" + isSynced + '\'' +
				'}';
	}
	
}
