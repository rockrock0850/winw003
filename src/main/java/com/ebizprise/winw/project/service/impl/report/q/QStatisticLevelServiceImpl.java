/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.q;

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
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 各等級問題數量統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("qStatisticLevelService")
public class QStatisticLevelServiceImpl extends BaseReportOperationService{

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.Q_STATISTIC_LEVEL.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.Q_STATISTIC_LEVEL.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource(ReportOperationEnum.Q_STATISTIC_LEVEL.getSqlFileName())); 
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>();
        
        //取得畫面資料
        int i, count = 0;
        if (result != null) {
            for (Map<String,Object> m : result) {
               i = 0;
               Map<String,Object> newMap = new HashMap<String,Object>();
               newMap.put(String.valueOf(i++), MapUtils.getString(m, ReportOperationEnum.MAP_KEY_GRADE.getName()));
               newMap.put(String.valueOf(i++), MapUtils.getString(m, ReportOperationEnum.MAP_KEY_ALL.getName()));
               newMap.put(String.valueOf(i++), MapUtils.getString(m, ReportOperationEnum.MAP_KEY_UNFINISHED.getName()));
               dataSet.put(count++, newMap);
            }
        }
        
        setResultTemp(result);
        reportOperation.setStatisticsResult(dataSet);
    }    
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.Q_STATISTIC_LEVEL.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_11));
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
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_5)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        //報表備註
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.TITLE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.Q_CONTENT_6_1.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_11
                ));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.Q_GRADE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.MAP_KEY_4.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                               
                ));

        List<Map<String,Object>> result = getResultTemp();
        if(result != null) {
            for(Map<String,Object> m:result) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_GRADE.getName()).equals(ReportOperationEnum.TOTAL.getName())?
                   getMessage(ReportOperationEnum.GRADE_TOTAL.getI18n()):        
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_GRADE.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,                   
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_ALL.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_UNFINISHED.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                  
                ));
            }
        }          
    }
       
}
