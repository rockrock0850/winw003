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
import com.microsoft.sqlserver.jdbc.StringUtils;

/**
 * 資安事件統計表	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCSecurityEventService")
public class INCSecurityEventServiceImpl extends BaseReportOperationService{
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private IDashBoardService dashBoardService;
    
    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.INC_SECURITY_EVENT.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.INC_SECURITY_EVENT.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource(
                ReportOperationEnum.INC_SECURITY_EVENT.getSqlFileName()));
        
        List<Map<String,Object>> result = getDataList(reportOperation,getResourceEnum(),getNotExists());
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        if(result != null) {
            for(Map<String,Object> map:result) {
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

                String key = MapUtils.getString(map,ReportOperationEnum.DIVISION.getName())
                              + MapUtils.getString(map,ReportOperationEnum.MAP_KEY_SECTION.getName());

                if(dataSets.containsKey(key)) {
                    list = dataSets.get(key);
                }
                map.put(ReportOperationEnum.MAP_KEY_SECTION.getName(), 
                        MapUtils.getString(map,ReportOperationEnum.MAP_KEY_SECTION.getName()));
                
                map.put(ReportOperationEnum.FORM_ID.getName(), 
                        MapUtils.getString(map,ReportOperationEnum.FORM_ID.getName())); 
                
                map.put(ReportOperationEnum.SUMMARY.getName(), 
                        MapUtils.getString(map,ReportOperationEnum.SUMMARY.getName())); 
                 
                map.put(ReportOperationEnum.USER_CREATED.getName(),                
                        sysUserService.getLdapUser().get(MapUtils.getString(map,ReportOperationEnum.USER_CREATED.getName()))==null?
                             StringUtils.EMPTY:sysUserService.getLdapUser().get(MapUtils.getString(map,ReportOperationEnum.USER_CREATED.getName())));                
                
                String formStatus = dashBoardService.getFormStatus(
                        MapUtils.getString(map,ReportOperationEnum.MAP_KEY_GROUP_ID.getName()),
                        MapUtils.getString(map,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()), 
                        MapUtils.getString(map,ReportOperationEnum.MAP_KEY_VERIFY_LEVEL.getName()), 
                        (Integer)map.get(ReportOperationEnum.MAP_KEY_ITEMS.getName()));

                map.put(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(),formStatus);
                
                map.put(ReportOperationEnum.SYSTEM.getName(), 
                        MapUtils.getString(map,ReportOperationEnum.SYSTEM.getName())); 
                
                map.put(ReportOperationEnum.EVENT_TYPE.getName(), 
                        MapUtils.getString(map,ReportOperationEnum.EVENT_TYPE.getName())); 
                               
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
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.INC_SECURITY_EVENT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_17));
        //dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));  
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_DATE.getI18n())              
                ,reportOperation.getStartInterval()+StringConstant.TILDE
                +reportOperation.getEndInterval(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_11)); 
        
        Map<String,List<Map<String,Object>>> result = getDataSetsTemp();
        int total = 0;
        if(result!=null) {
            for(Map.Entry<String,List<Map<String,Object>>> entry:result.entrySet()) {
               total += entry.getValue().size();
            }
        }
        
        //查詢結果
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_TOTAL.getI18n())+
                StringConstant.COLON+String.valueOf(total)
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_17));
        
        //dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        if(result != null) {
            for(Map.Entry<String,List<Map<String,Object>>> entry:result.entrySet()) {
                //搜尋結果
               
                dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL+StringConstant.PAPAM_1
                        ,getMessage(ReportOperationEnum.SEARCH_HOST.getI18n())+
                        StringConstant.COLON+
                        (entry.getValue().get(0).get(
                                ReportOperationEnum.MAP_KEY_SECTION.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(entry.getValue().get(0)
                                ,ReportOperationEnum.MAP_KEY_SECTION.getName())),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.EVENT_NUM.getI18n())+
                        StringConstant.COLON+entry.getValue().size(),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_6
                        ));
                
                dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_1,
                        getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        
                        getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_6,
                        
                        getMessage(ReportOperationEnum.HANDLING.getI18n()),
                        
                        getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        
                        getMessage(ReportOperationEnum.SYSTEM_NAME.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        
                        getMessage(ReportOperationEnum.SECURITY_EVENT_TYPE.getI18n()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                        
                        ));
                for(Map<String,Object> m : entry.getValue()) {
                    dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_1,
                            
                            m.get(ReportOperationEnum.MAP_KEY_SECTION.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.MAP_KEY_SECTION.getName()),                        
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            m.get(ReportOperationEnum.SUMMARY.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()),                        
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_6,
                            
                            m.get(ReportOperationEnum.USER_CREATED.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.USER_CREATED.getName()),                        

                            m.get(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),                        
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                            
                            m.get(ReportOperationEnum.SYSTEM.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.SYSTEM.getName()),                        
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            m.get(ReportOperationEnum.EVENT_TYPE.getName())==null?
                            StringUtils.SPACE:        
                            MapUtils.getString(m,ReportOperationEnum.EVENT_TYPE.getName()),                        
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1                            
                            ));                    
                }
                
                dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
            }
        }
                
    }  
    
}
