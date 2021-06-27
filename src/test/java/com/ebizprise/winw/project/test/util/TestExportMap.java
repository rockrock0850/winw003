package com.ebizprise.winw.project.test.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ebizprise.project.utility.doc.excel.ExcelUtil;

/**
 * The <code>TestExportMap</code>
 */
public class TestExportMap {

	private String EXCEL_EXPORT_FILE = "test_files/test_excel_map.xls";

	@Test
	public void exportXls() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("name", "");
		map.put("age", "");
		map.put("birthday", "");
		map.put("sex", "");
		Map<String, Object> map2 = new LinkedHashMap<String, Object>();
		map2.put("name", "測試是否是中文長度不能自動寬度.測試是否是中文長度不能自動寬度.");
		map2.put("age", null);
		map2.put("sex", null);
		map.put("birthday", null);
		Map<String, Object> map3 = new LinkedHashMap<String, Object>();
		map3.put("name", "張三");
		map3.put("age", 12);
		map3.put("sex", "男");
		map3.put("birthday", new Date());
		list.add(map);
		list.add(map2);
		list.add(map3);
		Map<String, String> map1 = new LinkedHashMap<>();
		map1.put("name", "姓名");
		map1.put("age", "年齡");
		map1.put("birthday", "出生日期");
		map1.put("sex", "性别");
		File f = new File(EXCEL_EXPORT_FILE);
		OutputStream out = new FileOutputStream(f);
		ExcelUtil.exportExcel(map1, list, out);
		out.close();
	}
}