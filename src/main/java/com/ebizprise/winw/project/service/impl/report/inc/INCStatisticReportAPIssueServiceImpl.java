/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.entity.SystemEntity;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.repository.ISystemRepository;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 事件單應用系統異常統計表
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCStatisticReportAPIssueService")
public class INCStatisticReportAPIssueServiceImpl extends BaseReportOperationService{

    @Autowired
    private ISystemRepository systemRepo;

    @Override
    public String getUrl() {
        return ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getPath();
    }
    
    @Override
    public void setReportOperationVO (ReportOperationVO vo) {
        Map<String, Object> resultMap = new TreeMap<>();
        List<SystemEntity> systems = systemRepo.findByMboName("PROBLEM");
        String securitySQL = ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getValue();
        
        // 應用系統異常事件
        int howManyColumns = CollectionUtils.isEmpty(vo.getMonths()) ? 0 : vo.getMonths().size();
        Map<String, Object> dataMap = findCountsByMonths(vo, securitySQL, "114");
        List<String[]> abnormalList = initTableRow(systems, howManyColumns, 3);// 頭跟尾加上臨界值=3
        setRowsByMonths(vo.getMonths(), dataMap, abnormalList);
        sum(abnormalList, new Integer[] {1});
        removeEmptyRow(abnormalList);
        resultMap.put("abnormalList", abnormalList);
        
        // 各科未結案事件單追蹤統計表 (應用系統異常事件明細)
        resultMap.put("detailList", findTraceDetail(vo, securitySQL, "114"));

        vo.setResultMap(resultMap);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setDataSet (Collection<Object> dataSet, ReportOperationVO vo) {
        // 抬頭
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLEM,
                getMessage(ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_14
        ));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        setReportOperationVO(vo);
        createExcelTable("應用系統異常事件", dataSet, vo, (List<String[]>) MapUtils.getObject(vo.getResultMap(), "abnormalList"));
        createExcelTable("各科未結案事件單追蹤統計表 (應用系統異常事件明細)", dataSet, (List<Map<String, Object>>) MapUtils.getObject(vo.getResultMap(), "detailList"));
    }

    // 初始化表格
    private List<String[]> initTableRow (List<SystemEntity> systems, int howManyColumns, int gap) {
        String id, name;
        String idFormat = "%s&%s";// 組成[辨識符號&項目名稱]
        List<String[]> rankList = new ArrayList<>();
        
        for (int i = 0; i < systems.size(); i++) {
            String[] row = new String[howManyColumns + gap];
            Arrays.fill(row, "0");// 預設全塞0

            // 補上預設資料
            id = systems.get(i).getSystemBrand();
            name = systems.get(i).getSystemName();
            row[1] = systems.get(i).getLimit();
            
            if (StringUtils.isBlank(id)) {
                continue;
            }
            
            row[0] = String.format(idFormat, id, StringUtils.isBlank(name) ? id : name);
            rankList.add(row);
        }
        
        return rankList;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createExcelTable (
            String title, Collection<Object> dataSet, ReportOperationVO vo, List<String[]> dataList) {
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                title,
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1
        ));

        String[] headers;
        List<String> months = vo.getMonths();
        headers = new String[months.size() + 3];
        headers[0] = "系統名稱";
        headers[1] = "臨界值";
        for (int i = 0; i < months.size(); i++) {
            headers[i + 2] = months.get(i);
        }
        headers[headers.length-1] = "總和";
        dataSet.add(new ExcelModel((Object[]) headers));
        
        for (String[] row : dataList) {
            dataSet.add(new ExcelModel((Object[]) row));
        }
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createExcelTable (
            String title, Collection<Object> dataSet, List<Map<String, Object>> dataList) {
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                title,
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1
        ));

        dataSet.add(new ExcelModel(
                "主辦科", "表單編號", "處理人員", "表單狀態", "事件發生時間", "預計完成時間", "摘要", "未結案會辦單"));
        
        int i = 0;
        Date tempDate;
        String tempStr;
        String[] row;
        for (Map<String, Object> map : dataList) {
            row = new String[map.keySet().size()];
            
            for (String k : map.keySet()) {
                tempStr = MapUtils.getString(map, k);
                
                if (i == 2) {// 排除員編
                    i++;
                    continue;
                }
                
                if (i == 5 || i == 6) {// 事件發生時間、預計完成時間
                    tempDate = DateUtils.fromString(tempStr, DateUtils.pattern11);
                    tempStr = DateUtils.toString(tempDate, DateUtils._PATTERN_YYYYMMDD_SLASH);
                }
                
                if (i == row.length-1) {// 未結案會辦單
                    tempStr = StringUtils.replace(tempStr, "<br>", "\r\n");
                }

                row[i] = StringUtils.isEmpty(tempStr) ? "" : tempStr;
                i++;
            }
            
            dataSet.add(new ExcelModel((Object[]) row));
            i = 0;
        }
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
    }

}
