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

/**
 * 各科對應新服務暨重大服務變更類型統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("cHGStatisticIsNewServiceService")
public class CHGStatisticIsNewServiceServiceImpl  extends BaseReportOperationService{
    
    @Autowired
    private IFormProcessBaseService processService;
  
    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getPath());
        return pageUrl;
    }  
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
      setNotExists(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getValue());
      setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
              ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getSqlFileName()));
      
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
                  dataSet.put(count++, combineResult(compare, result, false));
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
              ,getMessage(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getI18n())
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
              getMessage(ReportOperationEnum.DEPARTMENT.getI18n()),
              StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
              getMessage(ReportOperationEnum.NEW_SERVICE.getI18n()),
              StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3));
      
      setNotExists(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getValue());
      setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(
              ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getSqlFileName()));

      List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());

      if (result != null) {
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
                  newResult.add(combineResult(compare, result, true));
              }
          }
          
          for(Map<String,Object> m:newResult) {
              dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                 MapUtils.getString(m, ReportOperationEnum.NEW_SERVICE.getName()).equals(ReportOperationEnum.TOTAL.getName())?
                 getMessage(ReportOperationEnum.COUNT.getI18n()):        
                 MapUtils.getString(m, ReportOperationEnum.NEW_SERVICE.getName()),
                 StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                 MapUtils.getString(m, ReportOperationEnum.SECTION.getName()),
                 StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3
              ));
          }
      }
    }

    private Map<String, Object> combineResult (String compare, List<Map<String, Object>> result, boolean isExport) {
        String compare1, compare2, key1, key2;

        // 前端顯示跟匯出檔案把兩個key整個弄反
        // 太忙沒空修正 TODO
        if (isExport) {
            key1 = "newService";
            key2 = "section";
        } else {
            key1 = "section";
            key2 = "newService";
        }
        
        Map<String, Object> newMap = new HashMap<>();
        newMap.put(key1, isExport ? compare : 0);
        newMap.put(key2, isExport ? 0 : compare);
        
        for (Map<String, Object> exist : result) {
            compare1 = MapUtils.getString(exist, key1);
            compare2 = MapUtils.getString(exist, key2);
            if (compare.equals(isExport ? compare2 : compare1)) {
                newMap.put(isExport ? key2 : key1, 
                        isExport ? compare1 : compare2);
                break;
            }
        }
        
        return newMap;
    }
    
}
