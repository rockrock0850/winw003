package com.ebizprise.winw.project.test.util;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import com.ebizprise.project.utility.doc.excel.ExcelLogs;
import com.ebizprise.project.utility.doc.excel.ExcelUtil;
import com.ebizprise.winw.project.excel.model.TestExcelModel;

/**
 * 測試導入Excel
 */
public class TestImportExcel {

	private String EXCEL_XLS_FILE = "test_files/testMap.xls";
	private String EXCEL_XLSX_FILE = "test_files/testMap.xlsx";

	// @Ignore
	@Test
	public void importXls() {
		File f = new File(EXCEL_XLS_FILE);
		ExcelLogs logs = new ExcelLogs();
		Collection<TestExcelModel> importExcel = ExcelUtil
				.importExcel(f, TestExcelModel.class, "yyyy/MM/dd HH:mm:ss",
				logs, 0);

		for (TestExcelModel m : importExcel) {
			System.out.println(m);
		}
	}

	// @Ignore
	@Test
	public void importXlsx() {
		File f = new File(EXCEL_XLSX_FILE);

		ExcelLogs logs = new ExcelLogs();
		Collection<TestExcelModel> importExcel = ExcelUtil.importExcel(f, TestExcelModel.class, "yyyy/MM/dd HH:mm:ss",
				logs, 0);

		for (TestExcelModel m : importExcel) {
			System.out.println(m);
		}
	}
}