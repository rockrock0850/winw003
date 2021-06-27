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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 各科未結案事件單追蹤統計表
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月1日
 */
@Service("iNCListReportService")
public class INCListReportServiceImpl  extends BaseReportOperationService{
    private static final Logger logger = LoggerFactory.getLogger(INCListReportServiceImpl.class);
    
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private IDashBoardService dashBoardService;
    
    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.INC_LIST_REPORT.getPath());
        return pageUrl;
    }
    
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.INC_LIST_REPORT.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_INC.getResource(
               ReportOperationEnum.INC_LIST_REPORT.getSqlFileName()));
        
        List<Map<String,Object>> dataList = getDataList(reportOperation,getResourceEnum(),getNotExists());
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        
        if(dataList != null) {
            for(Map<String,Object> m:dataList) {
                logger.info("GroupId應該要有值 : " + m.toString());
                
                Map<String,Object> newMap = new HashMap<String,Object>();
                newMap.put(ReportOperationEnum.SECTION.getName(),
                        MapUtils.getString(m,ReportOperationEnum.SECTION.getName()));
                
                newMap.put(ReportOperationEnum.MAP_KEY_FORM_ID.getName(),
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_ID.getName()));
                
                newMap.put(ReportOperationEnum.SUMMARY.getName(),
                        MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()));
                
                newMap.put(ReportOperationEnum.MAP_KEY_USER_SOLVING.getName(),
                        sysUserService.getLdapUser().get(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_USER_SOLVING.getName()))==null?
                             StringUtils.EMPTY:sysUserService.getLdapUser().get(MapUtils.getString(m,ReportOperationEnum.MAP_KEY_USER_SOLVING.getName())));
                
                String formStatus = dashBoardService.getFormStatus(
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_GROUP_ID.getName()),
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_VERIFY_LEVEL.getName()),
                        (Integer)m.get(ReportOperationEnum.MAP_KEY_ITEMS.getName()));
                
                logger.info("表單狀態 : " + formStatus);

                newMap.put(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName(),formStatus);

                newMap.put(ReportOperationEnum.MAP_KEY_NEW_FIELD.getName(),
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_NEW_FIELD.getName())
                            .replaceAll(StringConstant.COMMA, StringConstant.LINE_SEPERATOR3));
                
                newMap.put(ReportOperationEnum.ACT.getName(),
                        MapUtils.getString(m,ReportOperationEnum.ACT.getName()));
                
                newMap.put(ReportOperationEnum.ECT.getName(),
                        MapUtils.getString(m,ReportOperationEnum.ECT.getName()));
                                                              
                list.add(newMap);
            }
        }

        dataSets.put(ReportOperationEnum.MAP_KEY_DATASETS.getName(), list);
        setDataSetsTemp(dataSets);
        reportOperation.setResultDataList(dataSets);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.INC_LIST_REPORT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_17));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.MAP_KEY_SECTION.getI18n()) + StringConstant.COLON
                +(reportOperation.getSection().equals(ReportOperationEnum.ALL_SECTION.getName())?
                getMessage(ReportOperationEnum.MAP_KEY_ALL.getI18n()):
                reportOperation.getSection())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n())+
                StringConstant.COLON +
                reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_14
                
                ));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //搜尋結果
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        
        dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_2,
                
                getMessage(ReportOperationEnum.SEARCH_HOST.getI18n()),
                
                getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                
                getMessage(ReportOperationEnum.USER_SOLVING.getI18n()),
               
                getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                
                getMessage(ReportOperationEnum.UNFINISHED_C_FORM.getI18n())
                +StringConstant.LINE_SEPERATOR2+
                getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n())+
                StringConstant.SLASH+
                getMessage(ReportOperationEnum.PROCESS_DIVISION.getI18n())+
                StringConstant.SLASH+
                getMessage(ReportOperationEnum.USER_SOLVING.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                
                getMessage(ReportOperationEnum.EVENT_TIME.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                
                getMessage(ReportOperationEnum.COMPLETION_DATE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                
                ));
        Map<String,List<Map<String,Object>>> result = getDataSetsTemp();
        if(result!=null) {
            List<Map<String,Object>> list = result.get(ReportOperationEnum.MAP_KEY_DATASETS.getName());
            for(Map<String,Object> m:list) {
                dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_2,
                        m.get(ReportOperationEnum.SECTION.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.SECTION.getName()),
                        
                        m.get(ReportOperationEnum.MAP_KEY_FORM_ID.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_ID.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        
                        m.get(ReportOperationEnum.SUMMARY.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_3,
                        
                        m.get(ReportOperationEnum.MAP_KEY_USER_SOLVING.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_USER_SOLVING.getName()),
                       
                        m.get(ReportOperationEnum.MAP_KEY_FORM_STATUS.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_FORM_STATUS.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        
                        m.get(ReportOperationEnum.MAP_KEY_NEW_FIELD.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.MAP_KEY_NEW_FIELD.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        
                        m.get(ReportOperationEnum.ACT.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.ACT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                        
                        m.get(ReportOperationEnum.ECT.getName())==null?
                        StringUtils.SPACE:
                        MapUtils.getString(m,ReportOperationEnum.ECT.getName()),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                        ));
            }
            
            //總件數
            dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                    ,getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                    StringConstant.COLON
                    ,StringUtils.SPACE+StringUtils.SPACE+String.valueOf(list.size())
                    ));
            dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
            
        }
        
    }

}
