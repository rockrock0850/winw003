package com.ebizprise.winw.project.test.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;
import com.ebizprise.winw.project.repository.ISysUserLogRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class LdapUserLogRepositoryTest extends TestBase {

	@Autowired
	private ISysUserLogRepository sysUserLogRepository;

	@Test
	public void testFindSysUserLogLastRecord() {
		System.out.println(UserStatusEnum.LOGIN.desc);
		SysUserLogEntity sysUserLogEntityList = sysUserLogRepository
				.findTop1ByUserIdAndStatusOrderByTimeDesc("Builder", UserStatusEnum.LOGIN.desc);
		Assert.assertNotNull(sysUserLogEntityList);
	}
}