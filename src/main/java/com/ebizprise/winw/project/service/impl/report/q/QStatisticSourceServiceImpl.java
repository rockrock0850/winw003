/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.q;

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
 * 問題單來源數量統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("qStatisticSourceService")
public class QStatisticSourceServiceImpl extends BaseReportOperationService{
    public static final int ZERO = 0;
    public static final String O = "0";
    public static final String I = "1";
    
    private Map<Integer,Map<String,Object>> dataSave;
    
    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.Q_STATISTIC_SOURCE.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.Q_STATISTIC_SOURCE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource(
                ReportOperationEnum.Q_STATISTIC_SOURCE.getSqlFileName())); 
        
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());       

        int otherItem = 8;
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>(); 
        if(result != null) {
            for(Map<String,Object> m:result) {
                Map<String,Object> newMap = new HashMap<String,Object>();
                int i = 1;   
                int num = 0;
                newMap.put(String.valueOf(i--),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_COUNTER.getName()));
                int idx = getItemSort(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_VALUE.getName()));
                if(idx != ZERO) {
                    newMap.put(String.valueOf(i),
                            String.format(ReportOperationEnum.BRAKETS.getValue(), idx)+
                            MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName())); 
                    num = idx;
                }else {
                    if(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_VALUE.getName())
                            .equals(ReportOperationEnum.MAP_KEY_TOTAL.getValue())) {
                        newMap.put(String.valueOf(i),
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName()));
                        num = otherItem + 1;
                    }else {
                        newMap.put(String.valueOf(i),
                                String.format(ReportOperationEnum.BRAKETS.getValue(), otherItem)+
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName())); 
                        num = otherItem;
                        otherItem++;
                    }
                    
                }
                dataSet.put(num, newMap);
            }
        }
        setDataSave(dataSet);
        reportOperation.setStatisticsResult(dataSet);         
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })    
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.Q_STATISTIC_SOURCE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_7));
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
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));   
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADER,
                getMessage(ReportOperationEnum.Q_FORM_SOURCE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_5,
                StringUtils.SPACE+StringUtils.SPACE+
                getMessage(ReportOperationEnum.NUM.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                               
                ));
        
        Map<Integer,Map<String,Object>> result = getDataSave();
        if(result != null) {
            for (Map.Entry<Integer,Map<String,Object>> entry : result.entrySet()) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODY,
                        entry.getValue().get(O).equals(ReportOperationEnum.MAP_KEY_TOTAL.getName())?
                        getMessage(ReportOperationEnum.MAP_KEY_SOURCE_ID.getI18n()):entry.getValue().get(O),  
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_5,
                        StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+
                        entry.getValue().get(I),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                        ));
            }
        } 

    }    
    
    /**
     * @return the dataSave
     */
    public Map<Integer, Map<String, Object>> getDataSave() {
        return dataSave;
    }

    /**
     * @param dataSave the dataSave to set
     */
    public void setDataSave(Map<Integer, Map<String, Object>> dataSave) {
        this.dataSave = dataSave;
    }

    private Map<String,Integer> itemSort(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        int count = 1;
        map.put(ReportOperationEnum.MAP_KEY_108.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_109.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_110.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_104.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_105.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_106.getName(), count++);
        map.put(ReportOperationEnum.MAP_KEY_107.getName(), count++);
        return map;
    }
    
    private Integer getItemSort(String value) {
       if(itemSort().containsKey(value)) {
           return itemSort().get(value);
       }else {
           return ZERO;
       }
    }
    
    
}
