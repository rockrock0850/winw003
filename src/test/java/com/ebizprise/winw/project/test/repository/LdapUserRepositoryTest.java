package com.ebizprise.winw.project.test.repository;

import com.ebizprise.project.utility.str.StringConstant;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class LdapUserRepositoryTest extends TestBase {

	@Autowired
	private ILdapUserRepository ldapUserRepository;

//	@Ignore
	@Test
	public void testUpdateLdapUserToDisabled() {
		ldapUserRepository.updateAllUsersEnableValue(StringConstant.SHORT_NO, StringConstant.SHORT_YES);
	}

	@Ignore
	@Test
	public void testFindLdapUserByEnabled() {
		LdapUserEntity sysUserEntity = ldapUserRepository.findByUserIdAndIsEnabled("Builder", StringConstant.SHORT_YES);
		Assert.assertNotNull(sysUserEntity);
	}
}