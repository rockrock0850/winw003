package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebizprise.winw.project.entity.LdapOrganizationEntity;
import org.springframework.stereotype.Repository;

@Repository("ldapOrganizationRepository")
public interface ILdapOrganizationRepository extends JpaRepository<LdapOrganizationEntity, Long> {

	public LdapOrganizationEntity findByOrgId(String orgId);

	@Modifying
	@Query("update LdapOrganizationEntity o set o.enabled = :enabled")
	public void updateAllOrganizationEnableValue(@Param("enabled") String enabled);

}
