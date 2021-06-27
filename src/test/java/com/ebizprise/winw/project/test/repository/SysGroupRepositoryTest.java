package com.ebizprise.winw.project.test.repository;

import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.test.base.TestBase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SysGroupRepositoryTest extends TestBase {

	@Autowired
	private ISysGroupRepository sysGroupRepository;

	@Ignore
	@Test
	public void testFindSysGroupByGroupName() {

		List<SysGroupEntity> ldapGroupEntityList = sysGroupRepository.findByGroupNameLike("副科長");
		sysGroupRepository.findByGroupNameLike("副科長");
		Assert.assertNotNull(ldapGroupEntityList);
	}

//	@Ignore
	@Test
	public void testFindByDivisionAndGroupName() {

		SysGroupEntity sysGroupEntity = sysGroupRepository.findByDivisionAndGroupNameLike("A01419","OA", "[_]" + UserEnum.DIVISION_CHIEF.wording());
		Assert.assertNotNull(sysGroupEntity);

	}

	@Ignore
	@Test
	public void testFindGroupNameJoinForm() {
		String groupName = sysGroupRepository.findGroupNameJoinForm("SR-00000033");
		Assert.assertNotNull(groupName);

	}
}