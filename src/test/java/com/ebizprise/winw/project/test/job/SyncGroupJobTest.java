package com.ebizprise.winw.project.test.job;

import java.io.File;

import org.junit.Test;

import com.ebizprise.project.utility.doc.xml.JaxbUtil;
import com.ebizprise.winw.project.test.base.TestBase;
import com.ebizprise.winw.project.xml.vo.AllGroupVO;

public class SyncGroupJobTest extends TestBase {

	@Test
	public void test() throws Exception {
		JaxbUtil util = new JaxbUtil(AllGroupVO.class);
		try {
			AllGroupVO head = util.fromFileConvertObject(new File("C:/temp/groups_sample.xml"));
			System.out.println(head.groupVOList.size());
		} catch (Exception e) {
			throw e;
		}
	}
	
}
