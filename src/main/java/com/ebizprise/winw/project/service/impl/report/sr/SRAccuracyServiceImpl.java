/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.sr;

import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 上線準確率
 *
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("sRAccuracyService")
public class SRAccuracyServiceImpl extends BaseReportOperationService{
    private final String A = "(A)";
    private final String B = "(B)";
    private final String C = "(C)";

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.SR_ACCURACY.getPath());
        return pageUrl;
    }

    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists("SR_ACCURACY1_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_SR.getResource("SR_ACCURACY1"));
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());

        setResultTemp(result);

        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>();
        if(result != null) {
            int count = 0;
            for(Map<String,Object> map:result) {
                Map<String,Object> m = new HashMap<String,Object>();
                int i = 0;
                m.put(String.valueOf(i++),MapUtils.getString(map,ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()));
                m.put(String.valueOf(i++),MapUtils.getString(map,ReportOperationEnum.MAP_KEY_SP.getName()));
                m.put(String.valueOf(i++),MapUtils.getString(map,ReportOperationEnum.MAP_KEY_AP.getName()));
                m.put(String.valueOf(i++),MapUtils.getString(map,ReportOperationEnum.MAP_KEY_CORRECT.getName()));
                m.put(String.valueOf(i++),MapUtils.getString(map,ReportOperationEnum.MAP_KEY_PERCENTAGE.getName()));
                dataSet.put(count++, m);
            }
        }
        //放入VO
        reportOperation.setStatisticsResult(dataSet);

        setNotExists("SR_ACCURACY2_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_SR.getResource("SR_ACCURACY2"));
        List<Map<String,Object>> result2 = getDataList(reportOperation,getResourceEnum(),getNotExists());

        Map<String,List<Map<String,Object>>> resultDataList = new HashMap<String,List<Map<String,Object>>>();

        if(result2 != null) {
            for(Map<String,Object> map:result2) {
               String key = MapUtils.getString(map,ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName());
               List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
               Map<String,Object> m = new HashMap<String,Object>();
               if(resultDataList.containsKey(key)) {
                   list = resultDataList.get(key);
               }
               int i = 0;
               m.put(String.valueOf(i++), MapUtils.getString(map,ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()));
               m.put(String.valueOf(i++), MapUtils.getString(map,ReportOperationEnum.SECTION.getName()));
               m.put(String.valueOf(i++), MapUtils.getString(map,ReportOperationEnum.COUNT.getName()));
               list.add(m);
               resultDataList.put(key, list);
            }
        }

        //暫存匯出EXCEL用
        setDataSetsTemp(resultDataList);
        //放入VO
        reportOperation.setResultDataList(resultDataList);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.SR_ACCURACY.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        String [] startMonth = StringUtils.split(reportOperation.getStartInterval(),StringConstant.SLASH);
        String [] endMonth = StringUtils.split(reportOperation.getEndInterval(),StringConstant.SLASH);
        String start = StringUtils.EMPTY;
        String end = StringUtils.EMPTY;
        if(startMonth.length>=2) {
            start = startMonth[0] + StringConstant.SLASH + startMonth[1];
        }

        if(endMonth.length>=2) {
            end = endMonth[0] + StringConstant.SLASH + endMonth[1];
        }

        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n())
                ,start+StringConstant.TILDE+end,StringConstant.PARAM_MERGE_COLUMNS+
                StringConstant.PAPAM_2,getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_8));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        //報表備註
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.TITLE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_3
                ,getMessage(ReportOperationEnum.SR_CONTENT_1.getI18n())+
                 StringConstant.COLON+
                 getMessage(ReportOperationEnum.SR_CONTENT_2.getI18n())+
                 StringUtils.SPACE + StringUtils.SPACE + StringUtils.SPACE +
                 getMessage(ReportOperationEnum.SR_CONTENT_3.getI18n())+
                 StringConstant.COLON+
                 getMessage(ReportOperationEnum.SR_CONTENT_4.getI18n())+
                 StringConstant.LINE_SEPERATOR2+
                 getMessage(ReportOperationEnum.SR_CONTENT_5.getI18n()),
                 StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14
                ));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        List<Map<String,Object>> result = getResultTemp();
        //統計表
        if(result != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.SEARCH_MONTH.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                    getMessage(ReportOperationEnum.RUN_SR_JOB_SP_FORM.getI18n())
                    +StringConstant.LINE_SEPERATOR2+A,
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                    getMessage(ReportOperationEnum.RUN_SR_JOB_AP_FORM.getI18n())
                    +StringConstant.LINE_SEPERATOR2+B,
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                    getMessage(ReportOperationEnum.RUN_SR_ONLINE.getI18n())
                    +StringConstant.LINE_SEPERATOR2+C,
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                    getMessage(ReportOperationEnum.RUN_SR_SUCCESS_RATE.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2
                    ));

            for(Map<String,Object> m:result) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM+StringConstant.PAPAM_1,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SP.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_AP.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_CORRECT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_PERCENTAGE.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2
                        ));
            }
        }

        //空行
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        Map<String,List<Map<String,Object>>> detailList = getDataSetsTemp();

        //明細資料
        if(detailList != null) {
            for (Map.Entry<String,List<Map<String,Object>>> entry : detailList.entrySet()) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                        ,entry.getKey()
                        ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
                //表格欄位名稱
                dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                        getMessage(ReportOperationEnum.SEARCH_MONTH.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                        getMessage(ReportOperationEnum.AP_DEPARTMENT.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                        getMessage(ReportOperationEnum.AP_FORM_NUMBER.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4
                        ));
                //資料行
                for(Map<String,Object> m :entry.getValue()) {
                    int i = 0;
                    dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                            MapUtils.getString(m, String.valueOf(i++)),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                            MapUtils.getString(m, String.valueOf(i++)),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                            MapUtils.getString(m, String.valueOf(i++)),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4
                            ));
                }
                dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

            }
        }
    }

}
