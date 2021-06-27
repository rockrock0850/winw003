/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.inc;

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
 * 事件管理工作量統計表(含明細)	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCStatisticReportDetailService")
public class INCStatisticReportDetailServiceImpl extends BaseReportOperationService{
    
    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String ELEVEN = "11";

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.INC_STATISTIC_REPORT_DETAIL.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists("INC_STATISTIC_REPORT_DETAIL1_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_STATISTIC_REPORT_DETAIL1"));
        Map<String,List<Map<String,Object>>> dataSheet = new HashMap<String,List<Map<String,Object>>>();
        
        //各類別事件統計表
        List<Map<String,Object>> result1 = getDataList(reportOperation,getResourceEnum(),getNotExists()); 
        if(result1 != null) {
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> m :result1) {
                int i = 0;
                Map<String,Object> map = new HashMap<String,Object>();
                String type = StringUtils.EMPTY;
                if(MapUtils.getString(m,ReportOperationEnum.KIND.getName()).equals(
                        ReportOperationEnum.SERVICE_EXCEPTION.getName())) {
                    type = getMessage(ReportOperationEnum.SERVICE_EXCEPTION.getI18n());
                }else if(MapUtils.getString(m,ReportOperationEnum.KIND.getName()).equals(
                        ReportOperationEnum.SERVICE_REQUEST.getName())) {
                    type = getMessage(ReportOperationEnum.SERVICE_REQUEST.getI18n());
                }else if(MapUtils.getString(m,ReportOperationEnum.KIND.getName()).equals(
                        ReportOperationEnum.SERVICE_COUNSEL.getName())) {
                    type = getMessage(ReportOperationEnum.SERVICE_COUNSEL.getI18n());
                }else if(MapUtils.getString(m,ReportOperationEnum.KIND.getName()).equals(
                        ReportOperationEnum.SERVICE_TOTAL.getName())) {
                    type = getMessage(ReportOperationEnum.SERVICE_TOTAL.getI18n());
                }
                
                map.put(String.valueOf(i++), type);
                map.put(String.valueOf(i++), MapUtils.getString(m,ReportOperationEnum.MAP_KEY_TIMES.getName()));
                list.add(map);
            }
            dataSheet.put(ReportOperationEnum.TYPE_STATISTIC.getName(), list);
        }

        setNotExists("INC_STATISTIC_REPORT_DETAIL2_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_STATISTIC_REPORT_DETAIL2"));
        
        //事件發生原因統計表
        List<Map<String,Object>> result2 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        if(result2 != null) {
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> m :result2) {
                int i = 0;
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(String.valueOf(i++), MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName())
                        .equals(ReportOperationEnum.SERVICE_TOTAL.getName())?
                        getMessage(ReportOperationEnum.SERVICE_TOTAL.getI18n()):MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName()));
                map.put(String.valueOf(i++), MapUtils.getString(m,ReportOperationEnum.MAP_KEY_TIMES.getName()));
                list.add(map);
            }
            dataSheet.put(ReportOperationEnum.REASON_STATISTIC.getName(), list);
        }

        setNotExists("INC_STATISTIC_REPORT_DETAIL3_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_STATISTIC_REPORT_DETAIL3"));
        
        //各等級事件逾期處理之事件統計表
        List<Map<String,Object>> result3 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        if(result3 != null) {
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> m :result3) {
                int i = 0;
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(String.valueOf(i++), MapUtils.getString(m,ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName()).
                        equals(ReportOperationEnum.SERVICE_TOTAL.getName())?
                                getMessage(ReportOperationEnum.SERVICE_TOTAL.getI18n()):MapUtils.getString(m,ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName()));
                map.put(String.valueOf(i++), MapUtils.getString(m,ReportOperationEnum.MAP_KEY_TIMES.getName()));
                list.add(map);
            }
            dataSheet.put(ReportOperationEnum.LEVEL_STATISTIC.getName(), list);
        }
 
        reportOperation.setResultDataList(dataSheet);

        setNotExists("INC_STATISTIC_REPORT_DETAIL4_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_STATISTIC_REPORT_DETAIL4"));
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        
        //各科未結案事件單追蹤統計表 (明細)
        List<Map<String,Object>> result4 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        if(result4 != null) {
            for(Map<String,Object> m :result4) {
                String status = getFormStatus(MapUtils.getString(m,ReportOperationEnum.FORM_STATUS.getName()),
                        MapUtils.getString(m,ReportOperationEnum.GROUP_NAME.getName()));
                String countersigned = MapUtils.getString(m,ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName());
                m.put(ReportOperationEnum.FORM_STATUS.getName(), status);
                m.put(ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName(),
                        countersigned.replaceAll(StringConstant.SLASH, StringUtils.SPACE)
                        .replaceAll(StringConstant.COMMA, StringConstant.LINE_SEPERATOR3));
                resultList.add(m);
            }
        }
        
        reportOperation.setResultList(resultList);

        setNotExists("INC_STATISTIC_REPORT_DETAIL5_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_STATISTIC_REPORT_DETAIL5"));

        List<String> notShow = new ArrayList<String>();
        //事件發生原因對應服務類別統計表
        List<Map<String,Object>> result5 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        Map<String,List<Map<String,Object>>> dynamicData = new HashMap<String,List<Map<String,Object>>>();
        if(result5 != null) {
            List<Map<String,Object>> dataList1 = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> m :result5) {                
                if(MapUtils.getString(m,ReportOperationEnum.SYSTEM.getName()).equals(
                        ReportOperationEnum.TOTAL.getName())) {
                    Map<String,Object> map = new HashMap<String,Object>();
                    int count = 0;
                    for (Map.Entry<String,Object> entry : m.entrySet()) {  
                        if(String.valueOf(entry.getValue()).equals(ZERO)) {
                            notShow.add(entry.getKey());
                            count++;
                            continue;
                        }
                        map.put(String.valueOf(count),
                                getMessage(String.format(ReportOperationEnum.INC_REASON.getI18n(),count++)));
                    }
                    dataList1.add(map);
                }
            }
            dynamicData.put(ReportOperationEnum.HEADER.getName(), dataList1);
            List<Map<String,Object>> dataList2 = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> m :result5) {
                Map<String,Object> map = new HashMap<String,Object>();
                int count = 0;
                for (Map.Entry<String,Object> entry : m.entrySet()) {
                    if(notShow.contains(entry.getKey())) {
                        count++;
                        continue;
                    }
                    if(entry.getValue().equals(ReportOperationEnum.TOTAL.getName())) {
                        map.put(String.valueOf(count++), ReportOperationEnum.GRAND_TOTAL.getName());
                    }else {
                        map.put(String.valueOf(count++), entry.getValue());
                    }
                }
                dataList2.add(map);       
            }
            dynamicData.put(ReportOperationEnum.BODY.getName(), dataList2);
        }
        
        reportOperation.setDynamicData(dynamicData);
    }  

    @SuppressWarnings({ "rawtypes", "unchecked" })    
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.INC_STATISTIC_REPORT_DETAIL.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n())              
                ,reportOperation.getStartInterval()+StringConstant.TILDE
                +reportOperation.getEndInterval(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_8)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        setReportOperationVO(reportOperation);
        //查詢結果
        Map<String,List<Map<String,Object>>> result = reportOperation.getResultDataList();
        //各類別事件統計表
        List<Map<String,Object>> type = result.get(ReportOperationEnum.TYPE_STATISTIC.getName());
        if(type != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.TYPE_STATISTIC.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));  
            
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.INC_EVEN_TYPE.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                    getMessage(ReportOperationEnum.NUMBER_ACCEPTED.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3              
                    ));            
            for(Map<String,Object> m:type) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM+StringConstant.PAPAM_1,
                        MapUtils.getString(m, ZERO),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        MapUtils.getString(m, ONE),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                        
                        ));               
            }
        }
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        //事件發生原因統計表
        List<Map<String,Object>> reason = result.get(ReportOperationEnum.REASON_STATISTIC.getName());
        if(reason != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.REASON_STATISTIC.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));  
            
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.INC_MAIN.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                    getMessage(ReportOperationEnum.NUMBER_ACCEPTED.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3              
                    ));            
            for(Map<String,Object> m:reason) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM+StringConstant.PAPAM_1,
                        MapUtils.getString(m, ZERO),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        MapUtils.getString(m, ONE),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                        
                        ));               
            }
        }
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        //各等級事件逾期處理之事件統計表
        List<Map<String,Object>> level = result.get(ReportOperationEnum.LEVEL_STATISTIC.getName());
        if(reason != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.LEVEL_STATISTIC.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));  
            
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.INC_SEQUENCE.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                    getMessage(ReportOperationEnum.INC_NUMBER_OVERDUE.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3              
                    ));            
            for(Map<String,Object> m:level) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM+StringConstant.PAPAM_1,
                        MapUtils.getString(m, ZERO),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        MapUtils.getString(m, ONE),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                        
                        ));               
            }
        }
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        List<Map<String,Object>> resultList = reportOperation.getResultList();
        //各科未結案事件單追蹤統計表 (明細)
        if(resultList != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.INC_UNFINISHED.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));  
            
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.SEARCH_HOST.getI18n()),
                    getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                    getMessage(ReportOperationEnum.HANDLING.getI18n()),
                    getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.UNFINISHED_C_FORM.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2, 
                    getMessage(ReportOperationEnum.COLUMN_4.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.COMPLETION_DATE.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                    ));
            for(Map<String,Object> m:resultList) {
                //String replaceBrTag = StringUtils.EMPTY;
                
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.NAME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.NAME.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName())
                                .replaceAll(StringConstant.LINE_SEPERATOR3, StringConstant.LINE_SEPERATOR2),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.ECT.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.ECT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                        ));                 
                
            }
            //總筆數
            dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                    StringConstant.COLON+ StringUtils.SPACE+resultList.size(),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));                                    
            dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        }
        
        //事件發生原因對應服務類別統計表
        Map<String,List<Map<String,Object>>> dynamic = reportOperation.getDynamicData();
        if(dynamic != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.CORRESPOND_STATISTICS.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));
            List<Map<String,Object>> header = dynamic.get(ReportOperationEnum.HEADER.getName());
            if(header != null) {
                for(Map<String,Object> m : header) {
                    List<String> list = new ArrayList<String>();
                    list.add(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_2);
                    String lastValue = StringUtils.EMPTY;
                    for (Map.Entry<String,Object> entry : m.entrySet()) {
                        if(entry.getKey().equals(ELEVEN)) {
                            lastValue = String.valueOf(entry.getValue());
                            continue;
                        }
                        list.add(String.valueOf(entry.getValue()));
                        if(entry.getKey().equals(ZERO)) {
                            list.add(StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1);
                        }
                    }
                    list.add(lastValue);
                    list.add(StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1);
                    Object[] array = list.toArray();
                    dataSet.add(new ExcelModel(array));
                }
            }
            List<Map<String,Object>> body = dynamic.get(ReportOperationEnum.BODY.getName());
            if(body != null) {
                for(Map<String,Object> m : body) {
                    List<String> list = new ArrayList<String>();
                    list.add(StringConstant.PARAM_BODYM+StringConstant.PAPAM_2);
                    String lastValue = StringUtils.EMPTY;
                    for (Map.Entry<String,Object> entry : m.entrySet()) {
                        if(entry.getKey().equals(ELEVEN)) {
                            lastValue = String.valueOf(entry.getValue());
                            continue;
                        }                       
                        list.add(String.valueOf(entry.getValue()));
                        if(entry.getKey().equals(ZERO)) {
                            list.add(StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1);
                        }                        
                    }
                    list.add(lastValue);
                    list.add(StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1);
                    Object[] array = list.toArray();
                    dataSet.add(new ExcelModel(array));
                }
            }            
            
        }
    }   
    
}
