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
 * 報表作業 特殊結案問題單彚總報表 	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月22日
 */
@Service("qSpecialCloseService")
public class QSpecialCloseServiceImpl extends BaseReportOperationService{

    @Override
    public String getUrl() {
        setUrl(ReportOperationEnum.Q_SPECIAL_CLOSE.getPath());
        return pageUrl;
    }
 
    @Override
    public void setReportOperationVO(ReportOperationVO reportOperation) {
        setNotExists(ReportOperationEnum.Q_SPECIAL_CLOSE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_Q.getResource(
                ReportOperationEnum.Q_SPECIAL_CLOSE.getSqlFileName()));
        reportOperation.setUniversal(ReportOperationEnum.MAP_KEY_S_END_1.getName());
        List<Map<String,Object>> sEnd1 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        reportOperation.setUniversal(ReportOperationEnum.MAP_KEY_S_END_2.getName());
        List<Map<String,Object>> sEnd2 = getDataList(reportOperation,getResourceEnum(),getNotExists());
        
        Map<String,List<Map<String,Object>>> dataSets = new HashMap<String,List<Map<String,Object>>>();
        dataSets.put(ReportOperationEnum.MAP_KEY_S_END_1.getName(), sEnd1);
        dataSets.put(ReportOperationEnum.MAP_KEY_S_END_2.getName(), sEnd2);
        setDataSetsTemp(dataSets);
        reportOperation.setResultDataList(dataSets);
    }     

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation) {
        //抬頭
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLEM
                ,getMessage(ReportOperationEnum.Q_SPECIAL_CLOSE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_18));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        //查詢條件
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1
                ,reportOperation.getIsExclude(),StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_16)); 
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE)); 
        
        //報表備註
        dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                ,getMessage(ReportOperationEnum.TITLE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));        
        dataSet.add(new ExcelModel(StringConstant.PARAM_CONTENT+StringConstant.PAPAM_1
                ,getMessage(ReportOperationEnum.SPECIAL_CLOSE.getI18n())
                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_18
                ));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
        
        Map<String,List<Map<String,Object>>> result = getDataSetsTemp();
        if(result!=null) {
            //搜尋結果
            if(result.containsKey(ReportOperationEnum.MAP_KEY_S_END_1.getName())) {
                List<Map<String,Object>> sEnd1 = result.get(ReportOperationEnum.MAP_KEY_S_END_1.getName());
                if(sEnd1.size()>0) {
                    dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                            ,getMessage(ReportOperationEnum.MAP_KEY_3.getI18n())
                            ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
            
                    dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_3,
                            getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.MAP_KEY_SECTION.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.PROCESSING_UNI.getI18n()),
                            
                            getMessage(ReportOperationEnum.HANDLING.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                            
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            getMessage(ReportOperationEnum.BILLING_TIME.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ECT.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ACT.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.INFLUENCES.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.URGENT.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ORDER.getI18n()),
                            
                            getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.CONTENT.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                            
                            getMessage(ReportOperationEnum.SIGN.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.REASON.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.SOLVE.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.TREATMENT_PLAN.getI18n()),
                            getMessage(ReportOperationEnum.TEMPORARY_SOLUTION.getI18n())
                            ));
                    for(Map<String,Object> m:sEnd1) {
                        dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_3,
                                MapUtils.getString(m,ReportOperationEnum.FORM_ID.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                
                                (m.get(ReportOperationEnum.MAP_KEY_SECTION.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_SECTION.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName())
                                ),
                                
                                (m.get(ReportOperationEnum.NAME.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.NAME.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                getMessage(ReportOperationEnum.MAP_KEY_3.getI18n())
                                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                
                                (m.get(ReportOperationEnum.CREATE_TIME.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.CREATE_TIME.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ECT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ECT.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ACT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ACT.getName())),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                                                
                                (m.get(ReportOperationEnum.INFLUENCES.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.INFLUENCES.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.URGENT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.URGENT.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ORDER.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ORDER.getName())),
                                
                                m.get(ReportOperationEnum.SUMMARY.getName())==null?
                                StringUtils.SPACE:          
                                MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                
                                m.get(ReportOperationEnum.CONTENT.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.CONTENT.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                                
                                m.get(ReportOperationEnum.SIGN.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.SIGN.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
 
                                m.get(ReportOperationEnum.REASON.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.REASON.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,

                                m.get(ReportOperationEnum.SOLVE.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.SOLVE.getName()),
                                
                                m.get(ReportOperationEnum.TEMPORARY_SOLUTION.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.TEMPORARY_SOLUTION.getName())
                                ));
                    }
                    
                    //總件數
                    dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                            ,getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                            StringConstant.COLON
                            ,StringUtils.SPACE+StringUtils.SPACE+String.valueOf(sEnd1.size())
                            ));
                    dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
                    
                }
            }

            if(result.containsKey(ReportOperationEnum.MAP_KEY_S_END_2.getName())) {
                List<Map<String,Object>> sEnd1 = result.get(ReportOperationEnum.MAP_KEY_S_END_2.getName());
                if(sEnd1.size()>0) {
                    dataSet.add(new ExcelModel(StringConstant.PARAM_TITLE
                            ,getMessage(ReportOperationEnum.MAP_KEY_2.getI18n())
                            ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1));
            
                    dataSet.add(new ExcelModel(StringConstant.PARAM_HEADERM+StringConstant.PAPAM_3,
                            getMessage(ReportOperationEnum.SERIAL_NUMBER.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.MAP_KEY_SECTION.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.PROCESSING_UNI.getI18n()),
                            
                            getMessage(ReportOperationEnum.HANDLING.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.FORM_STATUS.getI18n()),
                            
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            getMessage(ReportOperationEnum.BILLING_TIME.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ECT.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ACT.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                            
                            getMessage(ReportOperationEnum.INFLUENCES.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.URGENT.getI18n())+
                            StringConstant.SLASH+StringConstant.LINE_SEPERATOR2+
                            getMessage(ReportOperationEnum.ORDER.getI18n()),
                            
                            getMessage(ReportOperationEnum.SUMMARY.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                            
                            getMessage(ReportOperationEnum.CONTENT.getI18n()),
                            StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_5
                            ));
                    
                    for(Map<String,Object> m:sEnd1) {
                        dataSet.add(new ExcelModel(StringConstant.PARAM_BODY+StringConstant.PAPAM_3,
                                MapUtils.getString(m,ReportOperationEnum.FORM_ID.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                
                                (m.get(ReportOperationEnum.MAP_KEY_SECTION.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_SECTION.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.MAP_KEY_COUNTERSIGNEDS.getName())
                                ),
                                
                                (m.get(ReportOperationEnum.NAME.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.NAME.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                getMessage(ReportOperationEnum.MAP_KEY_2.getI18n())
                                ,StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                
                                (m.get(ReportOperationEnum.CREATE_TIME.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.CREATE_TIME.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ECT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ECT.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ACT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ACT.getName())),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_1,
                                                                
                                (m.get(ReportOperationEnum.INFLUENCES.getName())==null?
                                StringUtils.SPACE:
                                MapUtils.getString(m,ReportOperationEnum.INFLUENCES.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.URGENT.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.URGENT.getName()))+
                                StringConstant.LINE_SEPERATOR2+
                                (m.get(ReportOperationEnum.ORDER.getName())==null?
                                StringUtils.SPACE:        
                                MapUtils.getString(m,ReportOperationEnum.ORDER.getName())),
                                
                                m.get(ReportOperationEnum.SUMMARY.getName())==null?
                                StringUtils.SPACE:          
                                MapUtils.getString(m,ReportOperationEnum.SUMMARY.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                                
                                m.get(ReportOperationEnum.CONTENT.getName())==null?
                                StringUtils.SPACE:                                
                                MapUtils.getString(m,ReportOperationEnum.CONTENT.getName()),
                                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_5
                                ));
                    }
                    
                    //總件數
                    dataSet.add(new ExcelModel(StringConstant.PARAM_TOTAL
                            ,getMessage(ReportOperationEnum.TOTAL_NUMBER.getI18n())+
                            StringConstant.COLON
                            ,StringUtils.SPACE+StringUtils.SPACE+String.valueOf(sEnd1.size())
                            ));
                    dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));
                    
                }
            }            
        }
    }   
}


