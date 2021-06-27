/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 事件管理工作量統計表	
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCStatisticMulitMonthService")
public class INCStatisticMulitMonthServiceImpl extends BaseReportOperationService {
    
    private Comparator<String> sortTreeMap = new Comparator<String>() {

        private String index1, index2;
        
        @Override
        public int compare(String o1, String o2) {
            index1 = o1.substring(1);
            index2 = o2.substring(1);
            return (Integer.valueOf(index1) < Integer.valueOf(index2)) ? -1 : 1;
        }
        
    };

    @Override
    public String getUrl() {
        return ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getPath();
    }
    
    @Override
    public void setReportOperationVO (ReportOperationVO vo) {
        Map<String, List<Map<String, Object>>> dataMap = new TreeMap<>();
        String securitySQL = ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getValue();
        ResourceEnum resource = ResourceEnum.SQL_REPORT_OPERATION_INC.getResource(ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getSqlFileName());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ReportOperationEnum.START_DATE.getName(), vo.getStartInterval());
        findTableClass(vo, resource, securitySQL, param, dataMap);
        findTableReason(vo, resource, securitySQL, param, dataMap);
        findTablePriority(vo, resource, securitySQL, param, dataMap);

        vo.setResultDataList(dataMap);
    }  

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setDataSet (Collection<Object> dataSet, ReportOperationVO vo) {
        // 抬頭
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLEM,
                getMessage(ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_14
        ));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        setReportOperationVO(vo);
        createExcelTable("各類別事件統計表", "各類別事件", vo, dataSet, "tableClass");
        createExcelTable("事件發生原因統計表", "事件發生原因", vo, dataSet, "tableReason");
        createExcelTable("各等級事件逾期處理之事件統計表", "事件處理優先順序", vo, dataSet, "tablePriority");
    } 

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createExcelTable (String title, String tableName, ReportOperationVO vo, Collection<Object> dataSet, String mapKey) {
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                title,
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1
        ));
        
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_HEADERM,
                "",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                vo.getStartInterval() + "年",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_11,
                ""
        ));
        
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_HEADERM,
                tableName,
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                "1季",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                "2季",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                "3季",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                "4季",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                "總和"
        ));
        
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_HEADERM,
                "",
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""
        ));

        Map<String, List<Map<String, Object>>> temp = vo.getResultDataList();
        
        if (MapUtils.isEmpty(temp)) {
            return;
        }
        
        Map<String, Object> tempMap;
        List<Map<String, Object>> dataList = MapUtils.getObject(temp, mapKey);
        for (int i = 0; i < dataList.size(); i++) {
            tempMap = new HashMap<>(dataList.get(i));
            dataSet.add(new ExcelModel(
                    StringConstant.PARAM_BODYM,
                    MapUtils.getString(tempMap, "M0", ""),
                    StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                    MapUtils.getString(tempMap, "M1", "0"),
                    MapUtils.getString(tempMap, "M2", "0"),
                    MapUtils.getString(tempMap, "M3", "0"),
                    MapUtils.getString(tempMap, "M4", "0"),
                    MapUtils.getString(tempMap, "M5", "0"),
                    MapUtils.getString(tempMap, "M6", "0"),
                    MapUtils.getString(tempMap, "M7", "0"),
                    MapUtils.getString(tempMap, "M8", "0"),
                    MapUtils.getString(tempMap, "M9", "0"),
                    MapUtils.getString(tempMap, "M10", "0"),
                    MapUtils.getString(tempMap, "M11", "0"),
                    MapUtils.getString(tempMap, "M12", "0"),
                    MapUtils.getString(tempMap, "M13", "0")
            ));
        }
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
    }

    private void findTableClass (
            ReportOperationVO vo, ResourceEnum resource, String securitySQL, Map<String, Object> param, Map<String, List<Map<String, Object>>> dataMap) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        
        findClassRow("服務異常", vo.getIsExclude(), securitySQL, resource, param, dataList, Arrays.asList("113", "114", "117", "120"));
        findClassRow("服務請求", vo.getIsExclude(), securitySQL, resource, param, dataList, Arrays.asList("115", "116", "119"));
        findClassRow("服務諮詢", vo.getIsExclude(), securitySQL, resource, param, dataList, Arrays.asList("111", "112", "118"));
        
        dataList.add(subTotal(dataList));
        dataMap.put("tableClass", sortListMap(dataList));
    }

    private void findTableReason (
            ReportOperationVO vo, ResourceEnum resource, String securitySQL, Map<String, Object> param, Map<String, List<Map<String, Object>>> dataMap) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        
        findReasonRow("系統操作諮詢", "111", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("業務規定及一般詢問", "112", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("中心系統異常", "113", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("應用系統異常", "114", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("報表資訊", "115", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("帳務資訊", "116", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("數據線路問題問題", "117", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("端末硬體故障-連線問題", "118", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("帳號/密碼重設", "119", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findReasonRow("機房事件", "120", vo.getIsExclude(), securitySQL, resource, param, dataList);
        
        dataList.add(subTotal(dataList));
        dataMap.put("tableReason", sortListMap(dataList));
    }  

    private void findTablePriority (
            ReportOperationVO vo, ResourceEnum resource, String securitySQL, Map<String, Object> param, Map<String, List<Map<String, Object>>> dataMap) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        
        findPriorityRow("1", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findPriorityRow("2", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findPriorityRow("3", vo.getIsExclude(), securitySQL, resource, param, dataList);
        findPriorityRow("4", vo.getIsExclude(), securitySQL, resource, param, dataList);
        
        dataList.add(subTotal(dataList));
        dataMap.put("tablePriority", sortListMap(dataList));
    }
    
    private void findClassRow (
            String rowName, 
            String isExclude, 
            String securitySQL, 
            ResourceEnum resource, 
            Map<String, Object> param, 
            List<Map<String, Object>> dataList, 
            List<String> eventClass) {
        Conditions conditions = new Conditions().and().in("DETAIL.EventClass", eventClass);
        
        if(StringConstant.SHORT_YES.equals(isExclude)) {
            resource = resource.getResource(securitySQL);
        }
        
        addRow(rowName, dataList, jdbcRepository.queryForMap(resource, conditions, param));
    }

    private void findReasonRow (
            String rowName,
            String eventClass, 
            String isExclude, 
            String securitySQL, 
            ResourceEnum resource, 
            Map<String, Object> param,
            List<Map<String, Object>> dataList) {
        Conditions conditions = new Conditions().and().equal("DETAIL.EventClass", eventClass);
        
        if(StringConstant.SHORT_YES.equals(isExclude)) {
            resource = resource.getResource(securitySQL);
        }
        
        addRow(rowName, dataList, jdbcRepository.queryForMap(resource, conditions, param));
    }

    private void findPriorityRow (
            String priority, 
            String isExclude, 
            String securitySQL, 
            ResourceEnum resource, 
            Map<String, Object> param, 
            List<Map<String, Object>> dataList) {
        Conditions conditions = new Conditions().and().equal("DETAIL.EventPriority", priority);
        
        if(StringConstant.SHORT_YES.equals(isExclude)) {
            resource = resource.getResource(securitySQL);
        }
        
        addRow(priority, dataList, jdbcRepository.queryForMap(resource, conditions, param));
    }
    
    private List<Map<String, Object>> sortListMap (List<Map<String, Object>> dataList) {
        List<Map<String, Object>> sorted = new ArrayList<>();
        
        for (Map<String, Object> map : dataList) {
            Map<String, Object> sortMap = new TreeMap<>(sortTreeMap);
            sortMap.putAll(map);
            sorted.add(sortMap);
        }
        
        return sorted;
    }

    private Map<String, Object> subTotal (List<Map<String, Object>> dataList) {
        int M0 = 0, M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0, M6 = 0, M7 = 0, M8 = 0, M9 = 0, M10 = 0, M11 = 0, M12 = 0, M13 = 0;
        
        for (Map<String, Object> map : dataList) {
            for (String k : map.keySet()) {
                M0 = MapUtils.getInteger(map, k, 0);
                
                if (k.equals("M1")) {
                    M1 += M0;
                } else if (k.equals("M2")) {
                    M2 += M0;
                } else if (k.equals("M3")) {
                    M3 += M0;
                } else if (k.equals("M4")) {
                    M4 += M0;
                } else if (k.equals("M5")) {
                    M5 += M0;
                } else if (k.equals("M6")) {
                    M6 += M0;
                } else if (k.equals("M7")) {
                    M7 += M0;
                } else if (k.equals("M8")) {
                    M8 += M0;
                } else if (k.equals("M9")) {
                    M9 += M0;
                } else if (k.equals("M10")) {
                    M10 += M0;
                } else if (k.equals("M11")) {
                    M11 += M0;
                } else if (k.equals("M12")) {
                    M12 += M0;
                } else if (k.equals("M13")) {
                    M13 += M0;
                }
            }
        }
        
        Map<String, Object> subTotal = new HashMap<>();
        subTotal.put("M0", "小計");
        subTotal.put("M1", M1);
        subTotal.put("M2", M2);
        subTotal.put("M3", M3);
        subTotal.put("M4", M4);
        subTotal.put("M5", M5);
        subTotal.put("M6", M6);
        subTotal.put("M7", M7);
        subTotal.put("M8", M8);
        subTotal.put("M9", M9);
        subTotal.put("M10", M10);
        subTotal.put("M11", M11);
        subTotal.put("M12", M12);
        subTotal.put("M13", M13);
        
        return subTotal;
    }
    
    private void addRow (String title, List<Map<String, Object>> dataList, Map<String, Object> data) {
        Map<String, Object> row = new HashMap<>();
        row.put("M0", title);
        row.putAll(data);
        row.put("M13", sum(data));
        dataList.add(row);
    }

    private int sum (Map<String, Object> data) {
        int sum = 0;
        
        for (String k : data.keySet()) {
            sum += MapUtils.getInteger(data, k, 0);
        }
        
        return sum;
    }

}
