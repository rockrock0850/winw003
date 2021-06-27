/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.q;

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
 * 問題單結案數量統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("qStatisticCloseService")
public class QStatisticCloseServiceImpl extends BaseReportOperationService{
    public static final int NUMBER = 3;
    
    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.Q_STATISTIC_CLOSE.getPath());
        return pageUrl;
    }

    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists("Q_STATISTIC_CLOSE1_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource("Q_STATISTIC_CLOSE1"));
        
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        setResultTemp(result);
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>(); 
        
        //取得畫面資料
        int count = 0;
        if(result != null) {
            for(Map<String,Object> m:result) {
               Map<String,Object> newMap = new HashMap<String,Object>();
               int i = 0;
               int item = StringUtils.isBlank(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_CLOSE_METHOD.getName()))?
                       0:Integer.valueOf(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_CLOSE_METHOD.getName()));
               newMap.put(String.valueOf(i++),String.format(ReportOperationEnum.BRAKETS.getValue(), item)+getItemName(item));
               newMap.put(String.valueOf(i++),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_TOTAL.getName()));
               dataSet.put(count++, newMap);
            }
        }

        setNotExists("Q_STATISTIC_CLOSE2_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource("Q_STATISTIC_CLOSE2"));
        List<Map<String,Object>> dataList = getDataList(reportOperation,getResourceEnum(),getNotExists());
        setDataListTemp(dataList);
        
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(dataList != null) {
            for(Map<String,Object> m:dataList) {
                String formStatus = MapUtils.getString(m,ReportOperationEnum.MAP_KEY_GROUP_NAME.getName())+" "+
                        getFormStatus(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()));
                m.put(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(),formStatus);
                m.put(ReportOperationEnum.MAP_KEY_NEW_FIELD.getName(), 
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_NEW_FIELD.getName())
                            .replaceAll(StringConstant.COMMA, StringConstant.LINE_SEPERATOR3));                
                list.add(m);                            
            }
        }
        
        dataSets.put(ReportOperationEnum.MAP_KEY_DATASETS.getName(), list);
        //放入VO
        reportOperation.setStatisticsResult(dataSet);
        reportOperation.setResultDataList(dataSets);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })   
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.Q_STATISTIC_CLOSE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_18));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n())              
                ,reportOperation.getStartInterval()+StringConstant.TILDE
                +reportOperation.getEndInterval(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_12)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //報表備註
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.TITLE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_3
                ,getMessage(ReportOperationEnum.Q_CONTENT_7_1.getI18n())+
                StringConstant.LINE_SEPERATOR2+
                getMessage(ReportOperationEnum.Q_CONTENT_8_1.getI18n())+
                StringConstant.LINE_SEPERATOR2+
                getMessage(ReportOperationEnum.Q_CONTENT_9_1.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_18
                ));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.CLOSED_METHOD.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2)); 
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADER,
                getMessage(ReportOperationEnum.CLOSED_METHOD.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.NUM.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                              
                ));
        //統計表
        List<Map<String,Object>> result = getResultTemp();
        if(result != null) {
            for(Map<String,Object> m:result) {
                int item = StringUtils.isBlank(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_CLOSE_METHOD.getName()))?
                        0:Integer.valueOf(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_CLOSE_METHOD.getName()));
                
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODY,       
                        String.format(ReportOperationEnum.BRAKETS.getValue(), item)+getItemName(item),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,                   
                        StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+
                        MapUtils.getString(m, ReportOperationEnum.TOTAL.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                 
                     ));                
            }            
        }
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.UNFINISHED.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2)); 
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.FORM_ID.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.MAP_KEY_SECTION.getI18n()),
                getMessage(ReportOperationEnum.HANDLING.getI18n()),
                getMessage(ReportOperationEnum.IS_SPECIAL.getI18n()),
                getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.BILLING_TIME.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.ECT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.ACT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.PROCESSING_UNI.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2                
                ));
        //明細
        List<Map<String,Object>> dataList = getDataListTemp();
        if(dataList != null) {
            for(Map<String,Object> m:dataList) {
                dataSet.add(new ExcelModel(
                        StringConstant.PARAM_BODY,
                        MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName(), ""), 
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                        m.get(ReportOperationEnum.MAP_KEY_SECTION.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()), 
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_NAME.getName(), ""),
                        MapUtils.getString(m, ReportOperationEnum.IS_SPECIAL.getName(), ""),
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(), ""), 
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                        m.get(ReportOperationEnum.CREATE_TIME.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                        m.get(ReportOperationEnum.ECT.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.ECT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                        m.get(ReportOperationEnum.ACT.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.ACT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                        m.get(ReportOperationEnum.MAP_KEY_NEW_FIELD.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.MAP_KEY_NEW_FIELD.getName()).replaceAll(StringConstant.COMMA, StringConstant.LINE_SEPERATOR2),
                        StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1,
                        m.get(ReportOperationEnum.SUMMARY.getName()) == null ? StringUtils.SPACE : MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()), StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2                        
                ));
            }
        }    
    } 
    
    private Map<Integer,String> itemName(){
        Map<Integer,String> map = new HashMap<Integer,String>();
        int i=1;
        map.put(i++,ReportOperationEnum.MAP_KEY_1.getI18n());
        map.put(i++,ReportOperationEnum.MAP_KEY_2.getI18n());
        map.put(i++,ReportOperationEnum.MAP_KEY_3.getI18n());
        map.put(i++,ReportOperationEnum.MAP_KEY_4.getI18n());
        return map;
    }
    
    private String getItemName(int num) {
        return itemName().containsKey(num)?getMessage(itemName().get(num)):StringUtils.SPACE;
    }

}
