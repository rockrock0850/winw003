package com.ebizprise.winw.project.test.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ebizprise.project.utility.doc.velocity.VelocityUtil;
import com.ebizprise.winw.project.test.base.TestBase;

public class VelocityUtilTest extends TestBase {

	@Test
	public void testGenerateFile() {
		VelocityUtil createHtml = new VelocityUtil();

		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("name", "Gary Tsai");
		//
		// try {
		// createHtml.initFileSystemPath("/home/zipe/tmp");
		// createHtml.writeTemplateOutput("helloTmp.vm", "/home/zipe/tmp/test212.html",
		// map);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// createHtml.close();
		// }

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Gary Tsai");

		try {
			createHtml.initFilePath();
			createHtml.writeTemplateOutput("template/helloTmp.vm", "/home/zipe/tmp/test212.html", map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			createHtml.close();
		}

		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("name", "Gary Tsai");
		//
		// try {
		// createHtml.initClassPath();
		// createHtml.writeTemplateOutput("temp/helloClass.vm",
		// "/home/zipe/tmp/test222.html", map);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// createHtml.close();
		// }
	}
}
