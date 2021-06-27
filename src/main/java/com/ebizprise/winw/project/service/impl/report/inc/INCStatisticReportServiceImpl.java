/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.entity.SystemEntity;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.repository.ISystemRepository;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 事件數量排名統計表
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCStatisticReportService")
public class INCStatisticReportServiceImpl extends BaseReportOperationService {

    @Autowired
    private ISystemRepository systemRepo;
    @Autowired
    private ISysOptionRepository sysOptionRepo;

    @Override
    public String getUrl() {
        return ReportOperationEnum.INC_STATISTIC_REPORT.getPath();
    }
 
    @Override
    public void setReportOperationVO(ReportOperationVO vo) {
        int howManyColumns = 0;
        Map<String, Object> dataMap;
        Map<String, Object> resultMap = new TreeMap<>();
        List<SystemEntity> systems = systemRepo.findByMboName("PROBLEM");
        String securitySQL = ReportOperationEnum.INC_STATISTIC_REPORT.getValue();
        
        // 事件數量排名統計表
        howManyColumns = sysOptionRepo.countByOptionId("4");
        List<String[]> rankList = initTableRow(systems, howManyColumns, 2, false);// 頭跟尾=2
        List<String> allSystemId = fetchAllSystemId(systems);
        setRowsByEventClass(1, rankList, findRanksByEventClass("111", vo, securitySQL, allSystemId));
        setRowsByEventClass(2, rankList, findRanksByEventClass("112", vo, securitySQL, allSystemId));
        setRowsByEventClass(3, rankList, findRanksByEventClass("113", vo, securitySQL, allSystemId));
        setRowsByEventClass(4, rankList, findRanksByEventClass("114", vo, securitySQL, allSystemId));
        setRowsByEventClass(5, rankList, findRanksByEventClass("115", vo, securitySQL, allSystemId));
        setRowsByEventClass(6, rankList, findRanksByEventClass("116", vo, securitySQL, allSystemId));
        setRowsByEventClass(7, rankList, findRanksByEventClass("117", vo, securitySQL, allSystemId));
        setRowsByEventClass(8, rankList, findRanksByEventClass("118", vo, securitySQL, allSystemId));
        setRowsByEventClass(9, rankList, findRanksByEventClass("119", vo, securitySQL, allSystemId));
        setRowsByEventClass(10, rankList, findRanksByEventClass("120", vo, securitySQL, allSystemId));
        sum(rankList);
        subTotal(rankList);
        removeEmptyRow(rankList);
        resultMap.put("rankList", rankList);

        Integer[] skips = new Integer[] {1};
        howManyColumns = CollectionUtils.isEmpty(vo.getMonths()) ? 0 : vo.getMonths().size();

        // 應用系統異常事件
        dataMap = findCountsByMonths(vo, securitySQL, "114");
        List<String[]> abnormalList = initTableRow(systems, howManyColumns, 3, true);// 頭跟尾加上臨界值=3
        setRowsByMonths(vo.getMonths(), dataMap, abnormalList);
        sum(abnormalList, skips);
        removeEmptyRow(abnormalList);
        resultMap.put("abnormalList", abnormalList);

        // 機房事件
        dataMap = findCountsByMonths(vo, securitySQL, "120");
        List<String[]> machineList = initTableRow(systems, howManyColumns, 3, true);// 頭跟尾加上臨界值=3
        setRowsByMonths(vo.getMonths(), dataMap, machineList);
        sum(machineList, skips);
        removeEmptyRow(machineList);
        resultMap.put("machineList", machineList);

        vo.setResultMap(resultMap);
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
        createExcelTable("事件數量排名統計表", dataSet, vo, false, (List<String[]>) MapUtils.getObject(vo.getResultMap(), "rankList"));
        createExcelTable("應用系統異常事件", dataSet, vo, true, (List<String[]>) MapUtils.getObject(vo.getResultMap(), "abnormalList"));
        createExcelTable("機房事件", dataSet, vo, true, (List<String[]>) MapUtils.getObject(vo.getResultMap(), "machineList"));
    }

    // 初始化表格
    private List<String[]> initTableRow (List<SystemEntity> systems, int howManyColumns, int gap, boolean isLimit) {
        String id, name;
        int subTotalPos = (isLimit ? 0 : 1);// 是否加上小計那行
        String idFormat = "%s&%s";// 組成[辨識符號&項目名稱]
        List<String[]> rankList = new ArrayList<>();
        
        for (int i = 0; i < systems.size() + subTotalPos; i++) {
            String[] row = new String[howManyColumns + gap];
            Arrays.fill(row, "0");// 預設全塞0

            // 補上預設資料
            id = i == systems.size() ? "小計" : systems.get(i).getSystemBrand();
            name = i == systems.size() ? "小計" : systems.get(i).getSystemName();
            
            if (isLimit) {// 補上臨界值
                row[1] = systems.get(i).getLimit();
            }
            
            if (StringUtils.isBlank(id)) {
                continue;
            }
            
            row[0] = String.format(idFormat, id, StringUtils.isBlank(name) ? id : name);
            rankList.add(row);
        }
        
        return rankList;
    }

    // 將資料填入已初始化的表格
    private void setRowsByEventClass (int column, List<String[]> rows, Map<String, Object> systemIdList) {
        String id = "";
        
        for (String[] row : rows) {
            // 截取辨識碼
            if (StringUtils.split(row[0], "&").length >= 2) {
                id = StringUtils.split(row[0], "&")[0];
            }

            // 依序將Map的值填入每行的指定索引位置
            for (String k : systemIdList.keySet()) {
                if (k.equals(id)) {
                    row[column] = MapUtils.getString(systemIdList, k);
                    break;
                }
            }
        }
    }

    // 將資料填入已初始化的表格
    @SuppressWarnings("unchecked")
    private void setRowsByMonths (List<String> months, Map<String, Object> dataMap, List<String[]> abnormalList) {
        int column = 2;
        String id, systemBrand;
        List<Map<String, Object>> monthOfCounts;
        
        for (String[] row : abnormalList) {// 取出行
            id = row[0].split("&")[0];
            for (String date : months) {// 取出查詢區間(幾年幾月~幾年幾月)
                monthOfCounts = (List<Map<String, Object>>) MapUtils.getObject(dataMap, date);
                for (Map<String,Object> map : monthOfCounts) {// 取出當年當月的統計數資料
                    systemBrand = MapUtils.getString(map, "systemBrand");
                    if (systemBrand.equals(id)) {// 若該資料的系統名稱辨識符與該行的系統名稱辨識符相等, 則將該統計數填入該行的指定欄位
                        row[column] = MapUtils.getString(map, "Count");
                        break;
                    }
                }
                column++;
            }
            column = 2;
        }
    }

    // 找出所有系統名稱所對應的系統辨識符
    private List<String> fetchAllSystemId (List<SystemEntity> systems) {
        List<String> list = new ArrayList<>();
        
        for (SystemEntity entity : systems) {
            list.add(entity.getSystemBrand());
        }
        
        return list;
    }
    
    // 找出該事件主類別底下所有事件單分別選擇了哪些系統名稱並統計數量
    private Map<String, Object> findRanksByEventClass (
            String eventClass, ReportOperationVO vo, String securitySQL, List<String> allSclass) {
        ResourceEnum resource = ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("FIND_ALL_SYSTEM_BY_CONDITIONS");
        
        Map<String, Object> param = new HashMap<>();
        param.put("eventClass", eventClass);
        param.put("endDate", vo.getEndInterval());
        param.put("startDate", vo.getStartInterval());
        
        List<Map<String, Object>> dataList = getDataList(vo, resource, securitySQL, param);
        
        // 統計資料總數, 將資料整理成Map, 服務類別編號為Key且總和為Value
        int sum = 0;
        String value;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (Map<String, Object> map : dataList) {
            value = MapUtils.getString(map, "SystemBrand");
            if (dataMap.containsKey(value)) {
                sum = MapUtils.getInteger(dataMap, value);
            }
            dataMap.put(value, sum+=1);
            sum = 0;
        }
        
        return dataMap;
    }

    // 小計(加總每欄的值並填入最後一行)
    private void subTotal (List<String[]> rows) {
        int sum = 0;
        
        for (int row = 0; row < rows.size(); row++) {
            // 小計不加總
            if (row == rows.size()-1) {
                break;
            }
            
            // 依照每行的索引值依序加總進小計那行的對應欄位
            for (int column = 1; column < rows.get(row).length; column++) {
                sum = Integer.valueOf(rows.get(rows.size()-1)[column]);
                sum += Integer.valueOf(rows.get(row)[column]);
                rows.get(rows.size()-1)[column] = String.valueOf(sum);
                sum = 0;
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createExcelTable (
            String title, Collection<Object> dataSet, ReportOperationVO vo, boolean isLimit, List<String[]> dataList) {
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                title,
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1
        ));

        String[] headers;
        int gap = isLimit ? 3 : 2;
        int begin = isLimit ? 2 : 1;
        if (isLimit) {// 是否有臨界值得表格
            List<String> months = vo.getMonths();
            headers = new String[months.size() + gap];
            headers[0] = "系統名稱";
            headers[1] = "臨界值";
            for (int i = 0; i < months.size(); i++) {
                headers[i + begin] = months.get(i);
            }
        } else {
            List<SysOptionEntity> options = sysOptionRepo.findByOptionIdOrderBySortAsc("4");
            headers = new String[options.size() + gap];
            headers[0] = "系統名稱";
            for (int i = 0; i < options.size(); i++) {
                headers[i + begin] = options.get(i).getDisplay();
            }
        }
        headers[headers.length-1] = "總和";
        dataSet.add(new ExcelModel((Object[]) headers));
        
        for (String[] row : dataList) {
            dataSet.add(new ExcelModel((Object[]) row));
        }
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
    }

}
