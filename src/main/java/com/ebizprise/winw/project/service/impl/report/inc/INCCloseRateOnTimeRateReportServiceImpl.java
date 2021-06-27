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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.service.IDashBoardService;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.vo.ReportOperationVO;
import org.apache.commons.lang3.StringUtils;

/**
 * 事件如期結案比率報告
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCCloseRateOnTimeRateReportService")
public class INCCloseRateOnTimeRateReportServiceImpl  extends BaseReportOperationService{
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private IDashBoardService dashBoardService;
    
    private static final String C = "(C)";
    private static final String B = "(B)";
    
    private Map<Integer,Map<String,Object>> dataSetSave;
    
    @Override
    public String getUrl () {
        setUrl(ReportOperationEnum.INC_CLOSE_RATE_ON_TIME_RATE_REPORT.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO (ReportOperationVO reportOperation) {
        String resource1 = "INC_CLOSE_RATE_ON_TIME_RATE_REPORT1";
        String resource2 = "INC_CLOSE_RATE_ON_TIME_RATE_REPORT2";
        
        setNotExists("INC_CLOSE_RATE_ON_TIME_RATE_REPORT1_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource(resource1));
        
        Map<Integer,Map<String,Object>> dataSet = new HashMap<Integer,Map<String,Object>>();
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        //取得畫面資料
        int i, calculate, count = 0;
        String percentage, all, finished, month;
        
        if (result != null) {
            for (Map<String,Object> m : result) {
               i = 0;
               all = MapUtils.getString(m, ReportOperationEnum.MAP_KEY_ALL.getName(), "0");
               finished = MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FINISHED.getName(), "0");
               calculate = Integer.valueOf(all)-Integer.valueOf(finished);
               month = MapUtils.getString(m, ReportOperationEnum.MAP_KEY_PICK_MONTH.getName(), "");
               percentage = MapUtils.getString(m, ReportOperationEnum.MAP_KEY_PERCENTAGE.getName(), "0.00");
               
               Map<String, Object> newMap = new HashMap<String, Object>();
               newMap.put(String.valueOf(i++), month);
               newMap.put(String.valueOf(i++), MapUtils.getString(m, ReportOperationEnum.MAP_KEY_EP.getName()));
               newMap.put(String.valueOf(i++), all);
               newMap.put(String.valueOf(i++), finished);
               newMap.put(String.valueOf(i++), calculate);
               newMap.put(String.valueOf(i++), percentage(Double.valueOf(percentage)));
               newMap.put(String.valueOf(i++), MapUtils.getString(m, ReportOperationEnum.MAP_KEY_H_M_D.getName()));
               newMap.put(String.valueOf(i++), month.equals("total") ? null : MapUtils.getString(m, ReportOperationEnum.MAP_KEY_AVG.getName()));
               dataSet.put(count++, newMap);
            }
        }
        
        setDataSetSave(dataSet);
        setNotExists("INC_CLOSE_RATE_ON_TIME_RATE_REPORT2_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource(resource2));
        
        List<Map<String,Object>> dataList = getDataList(reportOperation,getResourceEnum(),getNotExists());
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        
        if (dataList != null) {
            for (Map<String,Object> m : dataList) {
                Map<String,Object> newMap = new HashMap<String,Object>();
                newMap.put(ReportOperationEnum.MAP_KEY_PICK_MONTH.getName(),
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_PICK_MONTH.getName()));
                
                newMap.put(ReportOperationEnum.MAP_KEY_FORM_ID.getName(),
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_ID.getName()));
                
                newMap.put(ReportOperationEnum.ACT.getName(),
                        MapUtils.getString(m, ReportOperationEnum.ACT.getName()));
                
                newMap.put(ReportOperationEnum.ECT.getName(),
                        MapUtils.getString(m, ReportOperationEnum.ECT.getName()));
                
                String formStatus = dashBoardService.getFormStatus(
                                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_GROUP_ID.getName()),
                                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_VERIFY_LEVEL.getName()),
                                        (Integer)m.get(ReportOperationEnum.MAP_KEY_ITEMS.getName()));
                
                newMap.put(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(), formStatus);
                
                newMap.put(ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName(),
                        MapUtils.getString(m, ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName()));
                
                newMap.put(ReportOperationEnum.SUMMARY.getName(),
                        MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()));
                
                newMap.put(ReportOperationEnum.MAP_KEY_USER_SOLVING.getName(),
                        sysUserService.getLdapUser().get(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_USER_SOLVING.getName())) == null ?
                             StringUtils.EMPTY:sysUserService.getLdapUser().get(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_USER_SOLVING.getName())));
                
                newMap.put(ReportOperationEnum.SECTION.getName(),
                        MapUtils.getString(m, ReportOperationEnum.SECTION.getName()));
                
                if (reportOperation.getEventType().isEmpty()) {
                    newMap.put("eventType", MapUtils.getString(m, "eventType"));
                }
                
                //事件類型:服務異常、服務請求、服務諮詢
                if (reportOperation.getEventType().equals(ReportOperationEnum.SERVICE_EXCEPTION.getValue()) ||
                        reportOperation.getEventType().equals(ReportOperationEnum.SERVICE_REQUEST.getValue()) ||
                        reportOperation.getEventType().equals(ReportOperationEnum.SERVICE_COUNSEL.getValue())) {
                    newMap.put("eventType", MapUtils.getString(m, "eventType"));
                }
                
                list.add(newMap);
            }
        }
        
        setDataListTemp(list);
        
        dataSets.put(ReportOperationEnum.MAP_KEY_DATASETS.getName(), list);
        //放入VO
        reportOperation.setStatisticsResult(dataSet);
        reportOperation.setResultDataList(dataSets);
    }
    
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setDataSet (Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.INC_CLOSE_RATE_ON_TIME_RATE_REPORT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_17));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        String startEnd = reportOperation.getStartInterval() + "~" + reportOperation.getEndInterval();

        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n())
                ,startEnd,StringConstant.PARAM_MERGE_COLUMNS+
                StringConstant.PAPAM_2,getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_11));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.START_END.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.URGENT_LEVEL.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.ACCEPT_EVENT.getI18n())
                +StringConstant.LINE_SEPERATOR+C,
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                               
                getMessage(ReportOperationEnum.SOLVED_AS_SCHEDULED.getI18n())
                +StringConstant.LINE_SEPERATOR+B,
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.UNSOLVED_AS_SCHEDULED.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.CLOSEING_RATE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.SOLVED_TIME.getI18n())
                +StringConstant.LINE_SEPERATOR+
                getMessage(ReportOperationEnum.DD_HH_MM_SS.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                
                getMessage(ReportOperationEnum.SOLVED_TIME.getI18n())
                +StringConstant.LINE_SEPERATOR+
                getMessage(ReportOperationEnum.SECOND_TOTAL.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2
                ));
        
        Map<Integer,Map<String,Object>> result = getDataSetSave();
        if (result!=null) {
            for (Map.Entry<Integer,Map<String,Object>> entry:result.entrySet()) {
               List<String>list = new ArrayList<String>();
               list.add(StringConstant.PARAM_BODYM+StringConstant.PAPAM_1);
               for(int i=0;i<entry.getValue().size();i++) {
                   String values = MapUtils.getString(entry.getValue(),String.valueOf(i));
                   if(entry.getValue().get(String.valueOf(i)) == null) {
                       values = StringUtils.SPACE;
                   }
                   list.add(values);
                   
                   String param = StringConstant.PAPAM_1;
                   if((i==entry.getValue().size()-1) || (i==entry.getValue().size()-2)) {
                       param = StringConstant.PAPAM_2;
                   }
                   list.add(StringConstant.PARAM_MERGE_COLUMNS+param);
               }
              Object[] array = list.toArray(new String[list.size()]);
              dataSet.add(new ExcelModel(array));
            }
            dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        }
        
        //未如期解決明細
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.UNSOLVED_AS_SCHEDULED.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.YEAR_MONTH.getI18n()),
                
                getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.EXCLUDE_TIME.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.TARGET_SOLVED_TIME.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                
                getMessage(ReportOperationEnum.EVENT_GRADE.getI18n()),
 
                getMessage(ReportOperationEnum.EVENT_SUMMARY.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                
                getMessage(ReportOperationEnum.USER_SOLVING.getI18n()),
                
                getMessage(ReportOperationEnum.PROCESS_DIVISION.getI18n())
                ));
        
         List<Map<String,Object>> list = getDataListTemp();
         if (list != null) {
             for(Map<String,Object> m:list) {
                 dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_2,
                         m.get(ReportOperationEnum.MAP_KEY_PICK_MONTH.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.MAP_KEY_PICK_MONTH.getName()),
                         
                         m.get(ReportOperationEnum.MAP_KEY_FORM_ID.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_ID.getName()),
                         StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                         
                         m.get(ReportOperationEnum.ECT.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.ECT.getName()),
                         StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                         
                         m.get(ReportOperationEnum.ACT.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.ACT.getName()),
                         StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                         
                         m.get(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                         StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                         
                         m.get(ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName())==null?
                         StringUtils.SPACE:
                         StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+
                         MapUtils.getString(m,ReportOperationEnum.MAP_KEY_EVENT_PRIORITY.getName()),
                         
                         m.get(ReportOperationEnum.SUMMARY.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()),
                         StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                         
                         m.get(ReportOperationEnum.MAP_KEY_USER_SOLVING.getName())==null?
                         StringUtils.SPACE:
                         MapUtils.getString(m,ReportOperationEnum.MAP_KEY_USER_SOLVING.getName()),
                         
                         m.get(ReportOperationEnum.SECTION.getName())==null?
                         StringUtils.SPACE:
                         StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+StringUtils.SPACE+
                         MapUtils.getString(m,ReportOperationEnum.SECTION.getName())
                         
                         ));
             }
             
             //總數
             dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                     ,getMessage(ReportOperationEnum.Q_TOTAL.getI18n())+
                     StringConstant.COLON
                     ,StringUtils.SPACE+StringUtils.SPACE+String.valueOf(list.size())
                     ));
             dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
         }
         
    }
     
    private String percentage (double value) {
        String result = "0.00";
        long lv = Math.round(value*100);// 四捨五入
        double ret = lv/100.0;// 注意:使用100.0而不是100
        return ret == 0 ? result : String.valueOf(ret);
    }

    /**
     * @return the dataSetSave
     */
    public Map<Integer, Map<String, Object>> getDataSetSave() {
        return dataSetSave;
    }

    /**
     * @param dataSetSave the dataSetSave to set
     */
    public void setDataSetSave (Map<Integer, Map<String, Object>> dataSetSave) {
        this.dataSetSave = dataSetSave;
    }

}
