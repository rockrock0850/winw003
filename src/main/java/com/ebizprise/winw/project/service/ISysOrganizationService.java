package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.entity.LdapOrganizationEntity;

public interface ISysOrganizationService {

	public void saveOrganizationFromLDAP(List<LdapOrganizationEntity> ldapOrganizationEntityList);

	public LdapOrganizationEntity findByOrgId(String orgId);

}
