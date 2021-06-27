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
 * 各服務資訊服務之服務水準達成率	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCInterruptService")
public class INCInterruptServiceImpl  extends BaseReportOperationService{

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.INC_INTERRUPT.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists("INC_INTERRUPT1_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_INTERRUPT1"));

        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        setResultTemp(result);
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        setTotal(result.size());
        
        if(result != null) {
            for(Map<String,Object> map:result) {
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                String key = MapUtils.getString(map,ReportOperationEnum.DIVISION.getName())
                             + MapUtils.getString(map,ReportOperationEnum.SECTION.getName()); 
                if(dataSets.containsKey(key)) {
                    list = dataSets.get(key);
                }
                map.put(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(), 
                        getFormStatus(MapUtils.getString(map,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName())));
                list.add(map);
                dataSets.put(key, list);
           }
        }
        
        setDataSetsTemp(dataSets);
        reportOperation.setResultDataList(dataSets); 

        setNotExists("INC_INTERRUPT2_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_INTERRUPT2"));
        List<Map<String,Object>> result2 = getDataList(reportOperation,getResourceEnum(),getNotExists());

        setNotExists("INC_INTERRUPT3_EXCLUDE");
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("INC_INTERRUPT3"));
        List<Map<String,Object>> result3 = getDataList(reportOperation,getResourceEnum(),getNotExists());
                
        List<Map<String,Object>> dataSets2 = new ArrayList<Map<String,Object>>();
        if(result2 != null) {
            for(Map<String,Object> map:result2) {
                Map<String,Object> ser = getSClass( result3,
                        MapUtils.getString(map,ReportOperationEnum.FORM_ID.getName()));
                map.put(ReportOperationEnum.SYSTEM.getName(),
                        MapUtils.getString(ser,ReportOperationEnum.SYSTEM.getName()));
                map.put(ReportOperationEnum.MAP_KEY_DISPLAY.getName(),
                        MapUtils.getString(ser,ReportOperationEnum.MAP_KEY_DISPLAY.getName()));
                dataSets2.add(map);       
            }
        }
        setResultTemp(dataSets2);
        reportOperation.setResultList(dataSets2);        
    }     

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.INC_INTERRUPT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));
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
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_8)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        Map<String,List<Map<String,Object>>> maps =  getDataSetsTemp();
        
        List<Map<String,Object>> lists = getResultTemp();
        
        //查詢結果
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));         
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_3
                ,getMessage(ReportOperationEnum.INTERRUPT.getI18n())+
                StringConstant.COLON+ StringUtils.SPACE +getTotal()+
                StringConstant.LINE_SEPERATOR2+StringConstant.LINE_SEPERATOR2+
                getMessage(ReportOperationEnum.PRODUCTION.getI18n())+
                StringConstant.COLON+ StringUtils.SPACE + lists.size()                
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));      
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        if(maps != null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.INTERRUPT.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));            
            
            for (Map.Entry<String,List<Map<String,Object>>> entry : maps.entrySet()) {                
                //欄位名稱
                dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        getMessage(ReportOperationEnum.SEARCH_HOST.getI18n()),
                        getMessage(ReportOperationEnum.HANDLING.getI18n()),
                        getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                        getMessage(ReportOperationEnum.SYSTEM_NAME.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.INFO_DATE_CREATETIME.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.EXCLUDETIME.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                        
                        ));
                
                 //表格內容
                for(Map<String,Object> m:entry.getValue()) {
                    dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_1,
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_SECTION.getName()),
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.NAME.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.NAME.getName()),
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.SYSTEM.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.SYSTEM.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.EXCLUDETIME.getName()))?
                                    StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.EXCLUDETIME.getName()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                            
                         ));             
                }
                
                //總筆數
                dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                        StringConstant.COLON+ StringUtils.SPACE+entry.getValue().size(),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));                        
                dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
            }
        }
        //第二張表
        if(lists!=null) {
            dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                    ,getMessage(ReportOperationEnum.PRODUCTION.getI18n())
                    ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));
            //欄位名稱
            dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                    getMessage(ReportOperationEnum.HANDLING.getI18n()),
                    getMessage(ReportOperationEnum.WORK_GROUP.getI18n()),
                    getMessage(ReportOperationEnum.SCLASS.getI18n()),
                    getMessage(ReportOperationEnum.SYSTEM_NAME.getI18n()),
                    getMessage(ReportOperationEnum.BILLING_TIME.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.OFF_LINE_TIME.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.ON_LINE_TIME.getI18n()),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                    
                    ));
            
             //表格內容                        
            for(Map<String,Object> m:lists) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.FORM_ID.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.SUMMARY.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.NAME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.NAME.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.WORKING.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.WORKING.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.MAP_KEY_DISPLAY.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.MAP_KEY_DISPLAY.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.SYSTEM.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.SYSTEM.getName()),
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.CREATE_TIME.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.OFF_LINE_TIME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.OFF_LINE_TIME.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        StringUtils.isBlank(MapUtils.getString(m, ReportOperationEnum.ON_LINE_TIME.getName()))?
                                StringUtils.SPACE:MapUtils.getString(m, ReportOperationEnum.ON_LINE_TIME.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                        
                     ));                
                
            }
            
            //總筆數
            dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_1,
                    getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                    StringConstant.COLON+ StringUtils.SPACE+lists.size(),
                    StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14));   
            
        }
        
    }
    
    @Override
    protected Map<String,String> formStatus(){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ReportOperationEnum.PROPOSING.getName(), ReportOperationEnum.PROPOSING.getI18n());
        map.put(ReportOperationEnum.APPROVING_0.getName(), ReportOperationEnum.APPROVING_0.getI18n());
        map.put(ReportOperationEnum.CHARGING_0.getName(), ReportOperationEnum.CHARGING_0.getI18n());
        map.put(ReportOperationEnum.CLOSED.getName(), ReportOperationEnum.CLOSED.getI18n());
        map.put(ReportOperationEnum.DEPRECATED.getName(), ReportOperationEnum.DEPRECATED.getI18n());
        map.put(ReportOperationEnum.ASSIGNING.getName(), ReportOperationEnum.ASSIGNING.getI18n());
        map.put(ReportOperationEnum.SELFSOLVE.getName(), ReportOperationEnum.SELFSOLVE.getI18n());
        map.put(ReportOperationEnum.WATCHING.getName(), ReportOperationEnum.WATCHING.getI18n());        
        return map;
    }
    
    private Map<String,Object> getSClass(List<Map<String,Object>> dataList, String formId){
        Map<String,Object> map = new HashMap<String,Object>();
        boolean t = true;
        String id = formId;     
        while(t) {
            int count = 1;
            for(Map<String,Object> m :dataList ) {
                if(MapUtils.getString(m,ReportOperationEnum.FORM_ID.getName()).equals(id)) {
                    id = MapUtils.getString(m,ReportOperationEnum.MAP_KEY_SOURCE_ID.getName());
                    if(id==null) {
                        map.put(ReportOperationEnum.SYSTEM.getName(),
                                MapUtils.getString(m,ReportOperationEnum.SYSTEM.getName()));
                        map.put(ReportOperationEnum.MAP_KEY_DISPLAY.getName(),
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_DISPLAY.getName()));
                    }
                    break;
                }
                count++;
            }
            if((count >= dataList.size()) || (id == null)) {
                break;
            }
        }
        if(!map.containsKey(ReportOperationEnum.SYSTEM.getName())) {
            map.put(ReportOperationEnum.SYSTEM.getName(),
                    StringUtils.EMPTY);            
        }
        if(!map.containsKey(ReportOperationEnum.MAP_KEY_DISPLAY.getName())) {
            map.put(ReportOperationEnum.MAP_KEY_DISPLAY.getName(),
                    StringUtils.EMPTY);            
        }
        return map;
    }
    
}
