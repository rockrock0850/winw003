package com.ebizprise.winw.project.test.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.LdapGroupEntity;
import com.ebizprise.winw.project.repository.ILdapGroupRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class LdapGroupRepositoryTest extends TestBase {

	@Autowired
	private ILdapGroupRepository ldapGroupRepository;

	@Test
	public void testFindAllGroup() {

		List<LdapGroupEntity> ldapGroupEntityList = ldapGroupRepository.findAll();
		Assert.assertNotNull(ldapGroupEntityList);
	}
}