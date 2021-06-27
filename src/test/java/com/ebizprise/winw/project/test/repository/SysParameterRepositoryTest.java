package com.ebizprise.winw.project.test.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class SysParameterRepositoryTest extends TestBase {

	@Autowired
	private ISysParameterRepository sysParameterRepository;

	@Test
	public void testFindParameterByParamKey() {
		List<SysParameterEntity> sysParameterEntityList = sysParameterRepository.findByParamKeyLike("mail");
		Assert.assertNotNull(sysParameterEntityList);
	}
}