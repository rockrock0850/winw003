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
 * @author gary.tsai 2019/6/20
 */
@Entity
@Table(name = "LDAP_ORGANIZATION")
public class LdapOrganizationEntity extends BaseEntity {

  private long id;
  private String orgId;
  private String name;
  private String ldapDn;
  private String ldapOu;
  private String upperOu;
  private String enabled;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "OrgId")
  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
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
  @Column(name = "UpperOu")
  public String getUpperOu() {
    return upperOu;
  }

  public void setUpperOu(String upperOu) {
    this.upperOu = upperOu;
  }

  @Basic
  @Column(name = "Enabled")
  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LdapOrganizationEntity that = (LdapOrganizationEntity) o;
		return id == that.id && Objects.equals(orgId, that.orgId) && Objects.equals(name, that.name)
				&& Objects.equals(ldapDn, that.ldapDn) && Objects.equals(ldapOu, that.ldapOu)
				&& Objects.equals(upperOu, that.upperOu) && Objects.equals(enabled, that.enabled)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orgId, name, ldapDn, ldapOu, upperOu, enabled, this.getUpdatedBy(), this.getUpdatedAt(),
				this.getCreatedBy(), this.getCreatedAt());
	}
}
