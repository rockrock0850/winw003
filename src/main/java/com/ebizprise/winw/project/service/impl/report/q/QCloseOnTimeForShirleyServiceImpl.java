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
 * 繼承取得各報表資訊的類別並覆寫所繼承問題處理完成率(含特殊結案)的方法	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月17日
 */
@Service("qCloseOnTimeForShirleyService")
public class QCloseOnTimeForShirleyServiceImpl extends BaseReportOperationService{

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource(
                ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getSqlFileName())); 
        
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        setResultTemp(result);
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>(); 
        
        //取得畫面資料
        int count = 0;
        if(result != null) {
            for(Map<String,Object> m:result) {
               Map<String,Object> newMap = new HashMap<String,Object>();
               int i = 0;
               newMap.put(String.valueOf(i++),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()));
               newMap.put(String.valueOf(i++),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_ALL.getName()));
               newMap.put(String.valueOf(i++),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FINISHED.getName()));
               newMap.put(String.valueOf(i++),MapUtils.getString(m,ReportOperationEnum.MAP_KEY_PERCENTAGE.getName()));
               dataSet.put(count++, newMap);
            }
        }
        
        //放入VO
        reportOperation.setStatisticsResult(dataSet);         
    }     

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getI18n())
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
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_4
                ,getMessage(ReportOperationEnum.Q_CONTENT_1.getI18n())+
                 StringConstant.COLON+
                 getMessage(ReportOperationEnum.Q_CONTENT_2.getI18n())+
                 StringUtils.SPACE + StringUtils.SPACE + StringUtils.SPACE +
                 getMessage(ReportOperationEnum.Q_CONTENT_3.getI18n())+
                 StringConstant.COLON+
                 getMessage(ReportOperationEnum.Q_CONTENT_4.getI18n())+
                 StringConstant.LINE_SEPERATOR2+
                 getMessage(ReportOperationEnum.Q_CONTENT_5.getI18n())+
                 StringConstant.LINE_SEPERATOR2+
                 getMessage(ReportOperationEnum.Q_CONTENT_6.getI18n())+
                 getMessage(ReportOperationEnum.Q_CONTENT_7.getI18n())+
                 getMessage(ReportOperationEnum.Q_CONTENT_8.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14
                ));        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.TARGET.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.QUANTITY_A.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.FINISHED_B.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                getMessage(ReportOperationEnum.PERCENT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                                
                ));

        List<Map<String,Object>> result = getResultTemp();
        if(result != null) {
            int count = 1;
            for(Map<String,Object> m:result) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODYM,
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()).equals(ReportOperationEnum.TOTAL.getName())?
                   getMessage(ReportOperationEnum.GRADE_TOTAL.getI18n()):        
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_YEAR_MONTH.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,                   
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_ALL.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                   MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FINISHED.getName()),
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                   count==result.size()
                   ?StringUtils.SPACE
                   :MapUtils.getString(m, ReportOperationEnum.MAP_KEY_PERCENTAGE.getName())
                   +StringConstant.PERCENT,
                   StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3                   
                ));
                count ++;
            }
        }        
    }    
}
