/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.chg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 各變更單來源統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("cHGStatisticSourceService")
public class CHGStatisticSourceServiceImpl  extends BaseReportOperationService{
    
    private static List<String> clazzList;
    
    static {
        clazzList = new ArrayList<String>();
        clazzList.add(FormEnum.INC.name());
        clazzList.add(FormEnum.INC_C.name());
        clazzList.add(FormEnum.Q.name());
        clazzList.add(FormEnum.Q_C.name());
        clazzList.add(FormEnum.SR.name());
        clazzList.add(FormEnum.SR_C.name());
        clazzList.add("total");
    }

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.CHG_STATISTIC_SOURCE.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.CHG_STATISTIC_SOURCE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
                ReportOperationEnum.CHG_STATISTIC_SOURCE.getSqlFileName())); 
        
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>();
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        if (result != null) {
            int count = 0;
            
            for (String compare : clazzList) {
                dataSet.put(count++, combineResult(compare, result, false));
            }
        }
        
        reportOperation.setStatisticsResult(dataSet);         
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" }) 
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.CHG_STATISTIC_SOURCE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_10));
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
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.FORM_TYPE_CODE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                getMessage(ReportOperationEnum.FORM_TYPE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.FORM_WONUM.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2                
                ));
 
        setNotExists(ReportOperationEnum.CHG_STATISTIC_SOURCE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
                ReportOperationEnum.CHG_STATISTIC_SOURCE.getSqlFileName())); 

        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        if(result != null) {
            List<Map<String,Object>> newResult = new ArrayList<>();
            
            for (String compare : clazzList) {
                newResult.add(combineResult(compare, result, true));
            }
            
            for(Map<String,Object> m:newResult) {
                String num = "0";
                String fc = MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_CLASS.getName());
                if(m.get(ReportOperationEnum.MAP_KEY_TIMES.getName())!=null) {
                    num = String.valueOf(m.get(ReportOperationEnum.MAP_KEY_TIMES.getName()));
                }
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_CLASS.getName()).equals(ReportOperationEnum.TOTAL.getName())?
                        getMessage(ReportOperationEnum.TOTAL.getI18n()):fc,
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                        getFromType(MapUtils.getString(
                                m,ReportOperationEnum.MAP_KEY_FORM_CLASS.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        num,
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2                        
                     ));               
            }        
        }
        
        
    }   

    private Map<String, Object> combineResult (String compare, List<Map<String,Object>> result, boolean isExport) {
        String fc, key1, key2, key3;
        
        if (isExport) {
            key1 = ReportOperationEnum.MAP_KEY_FORM_CLASS.getName();
            key2 = "";
            key3 = ReportOperationEnum.MAP_KEY_TIMES.getName();
        } else {
            key1 = "0";
            key2 = "1";
            key3 = "2";
        }
        
        Map<String,Object> newMap = new HashMap<String,Object>();
        newMap.put(key1, compare); 
        if (!isExport) {
            newMap.put(key2, getFromType(compare));  
        }
        newMap.put(key3, 0);  
        
        for (Map<String,Object> exist : result) {
            fc = MapUtils.getString(exist, ReportOperationEnum.MAP_KEY_FORM_CLASS.getName());
            if (compare.equals(fc)) {
                newMap.put(key3, exist.get(ReportOperationEnum.MAP_KEY_TIMES.getName()));
                break;
            }
        }
        
        return newMap;
    }

}
