package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebizprise.winw.project.entity.LdapGroupEntity;
import org.springframework.stereotype.Repository;

@Repository("ldapGroupRepository")
public interface ILdapGroupRepository extends JpaRepository<LdapGroupEntity, Long> {

	public LdapGroupEntity findByGroupId(String groupId);

	@Modifying
	@Query("update LdapGroupEntity g set g.enabled = :enabled")
	public void updateAllGroupsEnableValue(@Param("enabled") String enabled);

}
