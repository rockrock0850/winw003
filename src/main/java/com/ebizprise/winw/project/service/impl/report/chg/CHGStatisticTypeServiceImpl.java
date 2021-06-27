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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.ReportOperationVO;
import com.microsoft.sqlserver.jdbc.StringUtils;

/**
 * 各變更類型統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("cHGStatisticTypeService")
public class CHGStatisticTypeServiceImpl  extends BaseReportOperationService{

    @Autowired
    private IFormProcessBaseService processService;

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.CHG_STATISTIC_TYPE.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.CHG_STATISTIC_TYPE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
                ReportOperationEnum.CHG_STATISTIC_TYPE.getSqlFileName())); 

        List<HtmlVO> voList = processService.getSysGroupSelector();
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>(); 
        boolean isExclude = StringConstant.SHORT_YES.equals(reportOperation.getIsExclude());
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());

        if (result != null) {
            int count = 0;
            String compare;
            
            HtmlVO total = new HtmlVO();
            total.setWording("這是為了取到撈出來的資料的總數-total");
            voList.add(total);
            
            for (HtmlVO vo : voList) {
                compare = vo.getWording().split("-")[1];
                
                if (isExclude && (compare.equals("PLAN") || compare.equals("MGMT"))) {
                    continue;
                } else {
                    dataSet.put(count++, combineResult(vo, compare, result, false));
                }
            }
        }
        
        reportOperation.setStatisticsResult(dataSet);         
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })     
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.CHG_STATISTIC_TYPE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_8));
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
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.DEPARTMENT.getI18n()),
                getMessage(ReportOperationEnum.TYPE_NORMAL.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.TYPE_STANDARD.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                StringUtils.SPACE,
                getMessage(ReportOperationEnum.IS_URGENT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.NON_URGENT_CHANGE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.IS_URGENT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.NON_URGENT_CHANGE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ));
        
        setNotExists(ReportOperationEnum.CHG_STATISTIC_TYPE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
                ReportOperationEnum.CHG_STATISTIC_TYPE.getSqlFileName())); 

        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        if(result != null) {
            String compare;
            List<Map<String,Object>> newResult = new ArrayList<>();
            List<HtmlVO> voList = processService.getSysGroupSelector();
            boolean isExclude = StringConstant.SHORT_YES.equals(reportOperation.getIsExclude());
            
            HtmlVO total = new HtmlVO();
            total.setWording("這是為了取到撈出來的資料的總數-total");
            voList.add(total);
            
            for (HtmlVO vo : voList) {
                compare = vo.getWording().split("-")[1];
                
                if (isExclude && (compare.equals("PLAN") || compare.equals("MGMT"))) {
                    continue;
                } else {
                    newResult.add(combineResult(vo, compare, result, true));
                }
            }
            
            for(Map<String,Object> m:newResult) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()).equals(ReportOperationEnum.TOTAL.getName())?
                        getMessage(ReportOperationEnum.COUNT.getI18n()):        
                        MapUtils.getString(m, ReportOperationEnum.SECTION.getName()),
                        String.valueOf(m.get(ReportOperationEnum.MAP_KEY_GENERAL_YES.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        String.valueOf(m.get(ReportOperationEnum.MAP_KEY_GENERAL_NO.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        String.valueOf(m.get(ReportOperationEnum.MAP_KEY_STANDARD_YES.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        String.valueOf(m.get(ReportOperationEnum.MAP_KEY_STANDARD_NO.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                        
                     ));
            }       
        }
        
    }   

    private Map<String, Object> combineResult (HtmlVO vo, String compare, List<Map<String,Object>> result, boolean isExport) {
        String section, key1, key2, key3, key4, key5;
        
        if (isExport) {
            key1 = ReportOperationEnum.MAP_KEY_SECTION.getName();
            key2 = ReportOperationEnum.MAP_KEY_GENERAL_YES.getName();
            key3 = ReportOperationEnum.MAP_KEY_GENERAL_NO.getName();
            key4 = ReportOperationEnum.MAP_KEY_STANDARD_YES.getName();
            key5 = ReportOperationEnum.MAP_KEY_STANDARD_NO.getName();
        } else {
            key1 = "0";
            key2 = "1";
            key3 = "2";
            key4 = "3";
            key5 = "4";
        }
        
        Map<String,Object> newMap = new HashMap<String,Object>();
        newMap.put(key1, compare);
        newMap.put(key2, 0);
        newMap.put(key3, 0);
        newMap.put(key4, 0);
        newMap.put(key5, 0);
        
        for (Map<String,Object> exist : result) {
            section = (String) exist.get(ReportOperationEnum.MAP_KEY_SECTION.getName());
            
            if (compare.equals(section)) {
                newMap.put(key1, section);
                newMap.put(key2, exist.get(ReportOperationEnum.MAP_KEY_GENERAL_YES.getName()));
                newMap.put(key3, exist.get(ReportOperationEnum.MAP_KEY_GENERAL_NO.getName()));
                newMap.put(key4, exist.get(ReportOperationEnum.MAP_KEY_STANDARD_YES.getName()));
                newMap.put(key5, exist.get(ReportOperationEnum.MAP_KEY_STANDARD_NO.getName()));
                break;
            }
        }
        
        return newMap;
    } 

}
