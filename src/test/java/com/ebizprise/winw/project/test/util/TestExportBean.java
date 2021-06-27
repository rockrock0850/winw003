package com.ebizprise.winw.project.test.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.ebizprise.project.utility.doc.excel.ExcelUtil;

public class TestExportBean {

	private String EXCEL_EXPORT_FILE = "C:/temp/testBean.xls";

    @Test
	public void exportXls() throws IOException {
		// 用排序的Map且Map的鍵應與ExcelCell註解的index對應
//		Map<String, String> map = new LinkedHashMap<>();
//		map.put("a", "姓名");
//		map.put("b", "年齡");
//		map.put("c", "數字");
//		map.put("d", "性別");
//		map.put("f", "出生日期");
//		Collection<Object> dataset = new ArrayList<Object>();
//		dataset.add(new TestExcelModel2("", "", 12, null, null));
//		dataset.add(new TestExcelModel2(null, null, 23, null, null));
//		dataset.add(new TestExcelModel2("Gary", "34", 1144, "男", new Date()));
//		File f = new File(EXCEL_EXPORT_FILE);
//		OutputStream out = new FileOutputStream(f);
//
//		ExcelUtil.exportExcel(map, dataset, out);
//		out.close();
		
//		Map<Integer,Map<Integer,String>> iMap = new LinkedHashMap<>();
//		Map<Integer, String> map = new LinkedHashMap<>();
//		Map<Integer, String> otherMap = new LinkedHashMap<>();
//		Map<Integer, String> newMap = new LinkedHashMap<>();
//		
//		otherMap.put(0, "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
//		newMap.put(0, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		map.put(0, "姓名");
//        map.put(1, "年齡");
//        map.put(2, "數字");
//        map.put(3, "性別");
//        map.put(4, "出生日期");
//        iMap.put(0, otherMap);
//        iMap.put(1, map);
//        iMap.put(2, newMap);
//        
//        Collection<Object> dataset = new ArrayList<Object>();
//        dataset.add(new TestExcelModel2("test001", "19", 2212, "男", new Date()));
//        dataset.add(new TestExcelModel2("test002", "31", 6623, "男", new Date()));
//        dataset.add(new TestExcelModel2("test003", "14", 1144, "男", new Date())); 
//        
//        Map<Integer,Collection<Object>> ic = new LinkedHashMap<>();
//        ic.put(1, dataset);
//        
//        File f = new File(EXCEL_EXPORT_FILE);
//        OutputStream out = new FileOutputStream(f);
//        
//        ExcelUtil.exportExcel(iMap, ic, out);
//        out.close();
//	  File fi=new File("C:/temp/sample.xls");  
      //Map<String, String> map = new LinkedHashMap<>();
      //map.put("a", "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
     // map.put("b", "年齡");
      
      Collection<Object> dataset = new ArrayList<Object>();
      
      
      List<String> data = new ArrayList<String>();
      
      data.add("性別");
      data.add("出生日期");
      data.add("年齡");
      data.add("數字");
      data.add("姓名");
     // 
      
//      String[] array = data.toArray(new String[data.size()]);
      
//      dataset.add(new ExcelModel(array));
            
      //ExcelModel t3 = new ReportOperationExcelModel();
      
      //dataset.add(new ExcelModel("QQQQQQQQQQQQQQ"));
     // dataset.add(new ExcelModel("姓名","數字","年齡","性別","出生日期"));
//      for(int i=0;i<10;i++) {
//          dataset.add(new ExcelModel("姓名","數字","年齡","性別","出生日期"));      
//      }
//      for(int i=0;i<10;i++) {
//          dataset.add(new ExcelModel("姓名","數字","年齡"));
//      }
//      
//      for(int i=0;i<10;i++) {
//          dataset.add(new ExcelModel("姓名"));
//      }     
//      dataset.add(new ExcelModel(StringConstant.PARAM_BODY+"=3","c"+StringConstant.LINE_SEPERATOR+"ccc",StringConstant.PARAM_MERGE_COLUMNS,StringConstant.PARAM_MERGE_COLUMNS,StringConstant.PARAM_MERGE_COLUMNS,"33333",StringConstant.PARAM_MERGE_COLUMNS,StringConstant.PARAM_MERGE_COLUMNS));
//      dataset.add(new ExcelModel(StringConstant.PARAM_BODY,"333\nooooooooooooooooooooooooooooooooooooooooooooooooooooo33","44444"));
//      dataset.add(new ExcelModel(StringConstant.PARAM_BODY,"3\n3\n3\n3\n3",999999,12345,new Date()));
 //     dataset.add(new ExcelModel(StringConstant.PARAM_BODY+"=1","cccc","wwwwwwwwwwwwwww","wwwwwwwwwwwwwww","wwwwwwwwwwwwwww",StringConstant.PARAM_MERGE_COLUMNS,StringConstant.PARAM_MERGE_COLUMNS,StringConstant.PARAM_MERGE_COLUMNS,"44444"));
      
      File f = new File(EXCEL_EXPORT_FILE);
      OutputStream out = new FileOutputStream(f);

      ExcelUtil.exportExcel(dataset, out);
      //out.close();      
	}
}