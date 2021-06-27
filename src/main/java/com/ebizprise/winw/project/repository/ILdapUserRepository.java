package com.ebizprise.winw.project.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.LdapUserEntity;

@Repository("ldapUserRepository")
public interface ILdapUserRepository extends JpaRepository<LdapUserEntity, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Override
    public List<LdapUserEntity> findAll();

	public LdapUserEntity findByUserId(String userId);
	
	public LdapUserEntity findByUserIdAndIsEnabled(String userId, String enabled);
	
	@Modifying
	@Query("update LdapUserEntity u set u.isEnabled = :enabled where u.isSynced = :sync")
	public void updateAllUsersEnableValue(@Param("enabled") String enabled, @Param("sync") String sync);

	public List<LdapUserEntity> findBySysGroupId(String sysGroupId);

	public List<LdapUserEntity> findBySysGroupIdAndIsEnabled(String sysGroupId, String isEnabled);

	public List<LdapUserEntity> findByIsEnabledOrderByUpdatedAtDesc (String isEnabled);

	@Query("SELECT ldap FROM LdapUserEntity ldap WHERE ldap.name LIKE %:name%")
	public List<LdapUserEntity> findByName(@Param("name")String name);

}
