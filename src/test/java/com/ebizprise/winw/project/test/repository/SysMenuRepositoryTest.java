package com.ebizprise.winw.project.test.repository;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.SysMenuEntity;
import com.ebizprise.winw.project.jdbc.SysMenuJDBC;
import com.ebizprise.winw.project.repository.ISysMenuRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class SysMenuRepositoryTest extends TestBase {

	@Autowired
	private ISysMenuRepository sysMenuEntityRepository;

	@Autowired
	private SysMenuJDBC sysMenuJDBC;

	@Test
	public void testFindAllByTreeLevel() {
		List<Map<String,Object>> sysMenuList = sysMenuJDBC.findByLevel(2);
		for (Map<String, Object> map : sysMenuList) {
			for (Object key : map.keySet()) {
				System.out.println("Key : " + key.toString() + " Value : " + map.get(key));
			}
		}
		Assert.assertNotNull(sysMenuList);
	}

	@Test
	public void testFindAll() {
		List<SysMenuEntity> sysMenuList = sysMenuEntityRepository.findActivited();
		// for (Map map : sysMenuList) {
		// for (Object key : map.keySet()) {
		// System.out.println("Key : " + key.toString() + " Value : "
		// + map.get(key));
		// }
		// }
		Assert.assertNotNull(sysMenuList);
	}
}