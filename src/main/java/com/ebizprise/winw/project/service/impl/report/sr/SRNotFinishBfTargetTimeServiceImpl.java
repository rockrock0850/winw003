/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.sr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 應用系統之需求未於預計完成日修改完畢的個數
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("sRNotFinishBfTargetTimeService")
public class SRNotFinishBfTargetTimeServiceImpl extends BaseReportOperationService{

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_SR.getResource(
                ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getSqlFileName()));
        
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        setResultTemp(result);
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        if(result != null) {
            for(Map<String,Object> map:result) {
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                String key = MapUtils.getString(map,ReportOperationEnum.DEPARTMENT_ID.getName())
                              + MapUtils.getString(map,ReportOperationEnum.DIVISION.getName());
                if(dataSets.containsKey(key)) {
                    list = dataSets.get(key);
                }  
                map.put(ReportOperationEnum.ECT.getName(), 
                        map.get(ReportOperationEnum.ECT.getName())==null?"":
                            DateUtils.toString((Date)map.get(ReportOperationEnum.ECT.getName()), 
                                    DateUtils._PATTERN_YYYYMMDD_SLASH));
                map.put(ReportOperationEnum.ACT.getName(), 
                        map.get(ReportOperationEnum.ACT.getName())==null?"":
                            DateUtils.toString((Date)map.get(ReportOperationEnum.ACT.getName()), 
                                    DateUtils._PATTERN_YYYYMMDD_SLASH));
                map.put(ReportOperationEnum.FORM_STATUS.getName(),
                        getFormStatus(MapUtils.getString(map ,ReportOperationEnum.FORM_STATUS.getName()),
                        MapUtils.getString(map ,ReportOperationEnum.GROUP_NAME.getName())));                
                list.add(map);
                dataSets.put(key, list);                           
            }
        }
        setDataSetsTemp(dataSets);
        reportOperation.setResultDataList(dataSets);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })    
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_16));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n()),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1              
                ,reportOperation.getStartInterval()+StringConstant.TILDE
                +reportOperation.getEndInterval(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_9)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));         
 
        //查詢結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                ,getMessage(ReportOperationEnum.SEARCH_TOTAL.getI18n())+
                StringConstant.COLON+(getResultTemp()==null?0:getResultTemp().size())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_12));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1)); 
        
        Map<String,List<Map<String,Object>>> dataSets = getDataSetsTemp();
        if(dataSets != null) {
            for (Map.Entry<String,List<Map<String,Object>>> entry : dataSets.entrySet()) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                        ,getMessage(ReportOperationEnum.SEARCH_HOST.getI18n())+
                        StringConstant.COLON+MapUtils.getString(entry.getValue().get(0)
                        ,ReportOperationEnum.DEPARTMENT_NAME.getName())+
                        StringConstant.DASH+MapUtils.getString(entry.getValue().get(0)
                        ,ReportOperationEnum.DIVISION.getName())
                        ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                        
                        ,getMessage(ReportOperationEnum.SEARCH_UNFINISH.getI18n())+
                        StringConstant.COLON+entry.getValue().size()
                        ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_6));
                //表格抬頭
                dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_2,
                        getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                        getMessage(ReportOperationEnum.PRINCIPAL.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        getMessage(ReportOperationEnum.BE_RESPONSIBLE_FOR.getI18n()),
                        getMessage(ReportOperationEnum.AIMS_COMPLETION_DATE.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.ACTUAL_COMPLETION_DATE.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                        ));
                //表格內容
                for(Map<String,Object> m:entry.getValue()) {
                    dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM+StringConstant.PAPAM_2,
                            MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                            MapUtils.getString(m, ReportOperationEnum.NAME.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                            MapUtils.getString(m, ReportOperationEnum.DIVISION.getName()),
                            MapUtils.getString(m, ReportOperationEnum.ECT.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            MapUtils.getString(m, ReportOperationEnum.ACT.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                            
                         ));
                }
                //空格
                dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));  
                dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
            }
        }
    }
    
}
