/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.base.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.doc.excel.ExcelUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.service.IReportOperationService;
import com.ebizprise.winw.project.vo.ReportOperationVO;


/**
 * 取得各類報表作業相關資訊	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月16日
 */
public abstract class BaseReportOperationService extends BaseService implements IReportOperationService {
    private static final Logger logger = LoggerFactory.getLogger(BaseReportOperationService.class);
    
    protected String pageUrl;
    
    private String notExists;
    
    private ResourceEnum resourceEnum;
    
 
    /**
     * 報表 : 事件單應用系統異常統計表、事件單機房事件統計表 <br>
     * 找出 : 各科未結案事件單追蹤統計表 (應用系統異常事件明細)、各科未結案事件單追蹤統計表 (機房事件明細)<br>
     * 
     * @param vo
     * @param securitySQL
     * @param eventClass
     * @return
     * @author adam.yeh
     */
    protected List<Map<String, Object>> findTraceDetail (ReportOperationVO vo, String securitySQL, String eventClass) {
        ResourceEnum resource = ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("FIND_TRACE_STATISTICS_DETAIL_WITH_COUNTERSIGNED");
        
        Map<String, Object> param = new HashMap<>();
        param.put("startDate", vo.getStartInterval());
        param.put("endDate", vo.getEndInterval());
        param.put("eventClass", eventClass);
        
        Conditions conditions = new Conditions();
        if(StringConstant.SHORT_YES.equals(vo.getIsExclude())) {
            resource = resource.getResource("FIND_TRACE_STATISTICS_DETAIL_WITH_COUNTERSIGNED_EXCLUDE");
        }
        
        List<Map<String, Object>> detailList = jdbcRepository.queryForList(resource, conditions, param);
        
        String tempStr;
        for (Map<String, Object> map : detailList) {
            tempStr = MapUtils.getString(map, "Countersigned");
            map.put("Countersigned", StringUtils.replace(tempStr, ",", "<br>"));
            tempStr = MapUtils.getString(map, "FormStatus");
            map.put("FormStatus", FormEnum.valueOf(tempStr).status());
        }
        
        return detailList;
    }

    /**
     * 報表 : 事件數量排名統計表 <br>
     * 找出所選該年該月的事件單選了哪些系統名稱並統計數量
     * 
     * @param vo
     * @param securitySQL
     * @param eventClass
     * @return
     * @author adam.yeh
     */
    protected Map<String, Object> findCountsByMonths (ReportOperationVO vo, String securitySQL, String eventClass) {
        String startOfMonth;
        Conditions conditions;
        Map<String, Object> param;
        String endInterval = vo.getEndInterval();
        String startInterval = vo.getStartInterval();
        Map<String, Object> dataMap = new HashMap<>();
        ResourceEnum resource = ResourceEnum.SQL_REPORT_OPERATION_INC.getResource("FIND_ALL_SYSTEM_WITH_COUNT");
        
        for (String date : vo.getMonths()) {
            startOfMonth = date + "/01";
            param = new HashMap<>();
            param.put("eventClass", eventClass);
            param.put("startDate", StringUtils.contains(startInterval, date) ? startInterval : startOfMonth);
            param.put("endDate", StringUtils.contains(endInterval, date) ? 
                    endInterval : DateUtils.getEOMonth(startOfMonth, DateUtils._PATTERN_YYYYMMDD_SLASH));
            
            conditions = new Conditions();
            if(StringConstant.SHORT_YES.equals(vo.getIsExclude())) {
                resource = resource.getResource("FIND_ALL_SYSTEM_WITH_COUNT_EXCLUDE");
            }
            
            dataMap.put(date, jdbcRepository.queryForList(resource, conditions, param));
        }
        
        return dataMap;
    }

    /**
     * 移除總和為0的行
     * 
     * @param dataList
     * @author adam.yeh
     */
    protected void removeEmptyRow (List<String[]> dataList) {
        List<String[]> remove = new ArrayList<>();
        
        for (String[] row : dataList) {
            if ("0".equals(row[row.length-1])) {
                remove.add(row);
            }
        }
        
        for (String[] row : remove) {
            dataList.remove(row);
        }
    }
    
    /**
     * 總和(加總每行的值並填入最後一欄)
     * 
     * @param rows
     * @param skips
     * @author adam.yeh
     */
    protected void sum (List<String[]> rows) {
        sum(rows, new Integer[] {0});
    } 
    
    /**
     * 總和(加總每行的值並填入最後一欄)
     * 
     * @param rows
     * @param skips
     * @author adam.yeh
     */
    protected void sum (List<String[]> rows, Integer[] skips) {
        int sum = 0;
        String[] split = {};

        for (String[] row : rows) {
            for (int column = 0; column < row.length; column++) {
                if (column == 0) {// 把第0欄的辨識符號去掉
                    split = StringUtils.split(row[column], "&");
                    row[column] = 
                            split.length > 1 ? split[1] : split[0];
                    continue;
                }
                
                if (Arrays.asList(skips).contains(column)) {
                    continue;
                }
                
                sum += Integer.valueOf(row[column]);
            }
            
            row[row.length-1] = String.valueOf(sum);
            sum = 0;
        }
    } 
    
    @Override
    public String getPageUrl() {
        return getUrl();
    }
    
    @Override
    public void setResultDataList(ReportOperationVO reportOperation) {
        setReportOperationVO(reportOperation);
    }    

    @Override
    public void exportExcel(ReportOperationVO reportOperation, HttpServletResponse response) {
        try {
            //檔案名稱
            String fileName = getTitle(reportOperation.getOperation()) + 
                                DateUtils.toString(new Date(), DateUtils.pattern29);
            //response 設定
            setHttpServletResponse(response,fileName);            
            Collection<Object> dataSet = new ArrayList<Object>();
            //寫入 excel檔 資料
            setDataSet(dataSet,reportOperation);        
            //封裝寫入檔案
            ExcelUtil.exportExcel(dataSet, response.getOutputStream());            
            
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e+"");
        }       
    }
    
    /**
     * @return the notExists
     */
    public String getNotExists() {
        return notExists;
    }

    /**
     * @param notExists the notExists to set
     */
    public void setNotExists(String notExists) {
        this.notExists = notExists;
    }

    /**
     * @return the resourceEnum
     */
    public ResourceEnum getResourceEnum() {
        return resourceEnum;
    }

    /**
     * @param resourceEnum the resourceEnum to set
     */
    public void setResourceEnum(ResourceEnum resourceEnum) {
        this.resourceEnum = resourceEnum;
    }

    public void setUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * 取報表資料(會自動加上一些條件)<br>
     * 1. 是否排除資安部條件<br>
     * 2. 查詢區間<br>
     * 3. SECTION (未知)<br>
     * 4. UNIVERSAL (未知)<br>
     * @param vo
     * @param resource
     * @param securitySQL
     * @param params
     * @return
     * @author adam.yeh
     */
    protected List<Map<String,Object>> getDataList (
            ReportOperationVO vo, 
            ResourceEnum resource, 
            String securitySQL,
            Map<String, Object> params){
        if (StringUtils.isNotBlank(vo.getStartInterval())) {
            params.put(ReportOperationEnum.START_DATE.getName(), vo.getStartInterval()+
                    ReportOperationEnum.START_DATE.getValue());
        }
        
        if (StringUtils.isNotBlank(vo.getEndInterval())) {
            params.put(ReportOperationEnum.END_DATE.getName(), vo.getEndInterval()+
                    ReportOperationEnum.END_DATE.getValue());         
        }
        
        if (StringUtils.isNotBlank(vo.getUniversal())) {
            params.put(ReportOperationEnum.MAP_KEY_UNIVERSAL.getName(), vo.getUniversal());            
        }
        
        Conditions conditions = new Conditions();
        
        if (StringConstant.SHORT_YES.equals(vo.getIsExclude())) {
            resource = resource.getResource(securitySQL);
        }
        
        if (StringUtils.isNotBlank(vo.getSection()) 
                && !vo.getSection().equals(ReportOperationEnum.ALL_SECTION.getName())) {
            conditions.and().equal(ReportOperationEnum.SECTION.getName(), vo.getSection());
        }
        
        return jdbcRepository.queryForList(resource, conditions, params);  
    } 

    public List<Map<String,Object>> getDataList(
            ReportOperationVO reportOperation, ResourceEnum resourceEnum, String notExists){
        Map<String,Object> param = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(reportOperation.getStartInterval())) {
            param.put(ReportOperationEnum.START_DATE.getName(), reportOperation.getStartInterval()+
                    ReportOperationEnum.START_DATE.getValue());
        }
        
        if(StringUtils.isNotBlank(reportOperation.getEndInterval())) {
            param.put(ReportOperationEnum.END_DATE.getName(), reportOperation.getEndInterval()+
                    ReportOperationEnum.END_DATE.getValue());         
        }
        
        if(StringUtils.isNotBlank(reportOperation.getUniversal())) {
            param.put(ReportOperationEnum.MAP_KEY_UNIVERSAL.getName(), reportOperation.getUniversal());            
        }
        
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_EXCEPTION.getValue().equals(reportOperation.getEventType())) {
            param.put("eventType", reportOperation.getEventType());            
        }
        
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_REQUEST.getValue().equals(reportOperation.getEventType())) {
            param.put("eventType", reportOperation.getEventType());            
        }
        
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_COUNSEL.getValue().equals(reportOperation.getEventType())) {
            param.put("eventType", reportOperation.getEventType());            
        }
        
        Conditions conditions = new Conditions();
        
        //是否排除資安部
        if(reportOperation.getIsExclude().equals(StringConstant.SHORT_YES)) {
            resourceEnum = resourceEnum.getResource(notExists);
        }
        
        if(StringUtils.isNotBlank(reportOperation.getSection()) 
                && !reportOperation.getSection().equals(ReportOperationEnum.ALL_SECTION.getName())) {
            conditions.and().equal(ReportOperationEnum.SECTION.getName(), reportOperation.getSection());
        }
        
        //服務異常
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_EXCEPTION.getValue().equals(reportOperation.getEventType())) {
            conditions.and().equal("eventType", reportOperation.getEventType());
        }
        
        //服務請求
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_REQUEST.getValue().equals(reportOperation.getEventType())) {
            conditions.and().equal("eventType", reportOperation.getEventType());
        }
        
        //服務諮詢
        if(StringUtils.isNotBlank(reportOperation.getEventType()) &&
                ReportOperationEnum.SERVICE_COUNSEL.getValue().equals(reportOperation.getEventType())) {
            conditions.and().equal("eventType", reportOperation.getEventType());
        }
        
        return jdbcRepository.queryForList(resourceEnum, conditions, param);  
    }    
    
    protected void setHttpServletResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        response.reset();                
        response.setContentType(StringConstant.SET_CONTENT_TYPE);
        response.setHeader(StringConstant.SET_HEADER_COOKIE,
                            StringConstant.SET_HEADER_FILEDOWNLOAD_PATH);       
        response.setHeader(StringConstant.SET_HEADER_CONTENT_DISPOSITION,
                           StringConstant.SET_HEADER_ATTACHMENT_FILENAME 
                           + URLEncoder.encode( fileName + StringConstant.DOT + StringConstant.EXCEL_SEXTENSION, "UTF-8"));
    }
    
    protected String getFormStatus(String status) {
        return formStatus().containsKey(status)?getMessage(formStatus().get(status)):StringUtils.EMPTY;
     }    
    
    protected String getFormStatus(String status, String groupName) {
       return formStatus().containsKey(status)?getMessage(formStatus().get(status), groupName):StringUtils.EMPTY;
    }
    
    protected String getFromType(String type) {
        return formType().containsKey(type)?getMessage(formType().get(type)):StringUtils.SPACE;
    }   
    
    protected Map<String,String> formStatus(){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ReportOperationEnum.PROPOSING.getName(), ReportOperationEnum.PROPOSING.getI18n());
        map.put(ReportOperationEnum.APPROVING.getName(), ReportOperationEnum.APPROVING_0.getI18n());
        map.put(ReportOperationEnum.CHARGING.getName(), ReportOperationEnum.CHARGING_0.getI18n());
        map.put(ReportOperationEnum.CLOSED.getName(), ReportOperationEnum.CLOSED.getI18n());
        map.put(ReportOperationEnum.DEPRECATED.getName(), ReportOperationEnum.DEPRECATED.getI18n());
        map.put(ReportOperationEnum.ASSIGNING.getName(), ReportOperationEnum.ASSIGNING.getI18n());
        map.put(ReportOperationEnum.SELFSOLVE.getName(), ReportOperationEnum.SELFSOLVE.getI18n());
        map.put(ReportOperationEnum.WATCHING.getName(), ReportOperationEnum.WATCHING.getI18n());        
        return map;
    }
    
    protected Map<String,String> formType(){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ReportOperationEnum.FORM_SR.getName(), ReportOperationEnum.FORM_SR.getI18n());
        map.put(ReportOperationEnum.FORM_SR_C.getName(), ReportOperationEnum.FORM_SR_C.getI18n());
        map.put(ReportOperationEnum.FORM_Q.getName(), ReportOperationEnum.FORM_Q.getI18n());
        map.put(ReportOperationEnum.FORM_Q_C.getName(), ReportOperationEnum.FORM_Q_C.getI18n());
        map.put(ReportOperationEnum.FORM_INC.getName(), ReportOperationEnum.FORM_INC.getI18n());
        map.put(ReportOperationEnum.FORM_INC_C.getName(), ReportOperationEnum.FORM_INC_C.getI18n());
        return map; 
     }
    
    protected String getTitle(String option) {
        return title().containsKey(option)?getMessage(title().get(option)):StringUtils.EMPTY;
    }
    
    protected Map<String,String> title(){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ReportOperationEnum.SR_ACCURACY.getName(),  ReportOperationEnum.SR_ACCURACY.getI18n());
        map.put(ReportOperationEnum.SR_SUCCESS_RATE.getName(),  ReportOperationEnum.SR_SUCCESS_RATE.getI18n());
        map.put(ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getName(),  ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getI18n());
        map.put(ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getName(),  ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getI18n());
        map.put(ReportOperationEnum.Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M.getName(),  ReportOperationEnum.Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M.getI18n());
        map.put(ReportOperationEnum.Q_SPECIAL_CLOSE.getName(),  ReportOperationEnum.Q_SPECIAL_CLOSE.getI18n());
        map.put(ReportOperationEnum.Q_STATISTIC_CLOSE.getName(),  ReportOperationEnum.Q_STATISTIC_CLOSE.getI18n());
        map.put(ReportOperationEnum.Q_STATISTIC_LEVEL.getName(),  ReportOperationEnum.Q_STATISTIC_LEVEL.getI18n());
        map.put(ReportOperationEnum.Q_STATISTIC_SOURCE.getName(),  ReportOperationEnum.Q_STATISTIC_SOURCE.getI18n());
        map.put(ReportOperationEnum.INC_LIST_REPORT.getName(),  ReportOperationEnum.INC_LIST_REPORT.getI18n());
        map.put(ReportOperationEnum.INC_CLOSE_RATE_ON_TIME_RATE_REPORT.getName(),  ReportOperationEnum.INC_CLOSE_RATE_ON_TIME_RATE_REPORT.getI18n());
        map.put(ReportOperationEnum.INC_INTERRUPT.getName(),  ReportOperationEnum.INC_INTERRUPT.getI18n());
        map.put(ReportOperationEnum.INC_SECURITY_EVENT.getName(),  ReportOperationEnum.INC_SECURITY_EVENT.getI18n());
        map.put(ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getName(),  ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getI18n());
        map.put(ReportOperationEnum.INC_STATISTIC_REPORT_DETAIL.getName(),  ReportOperationEnum.INC_STATISTIC_REPORT_DETAIL.getI18n());
        map.put(ReportOperationEnum.INC_STATISTIC_REPORT.getName(),  ReportOperationEnum.INC_STATISTIC_REPORT.getI18n());
        map.put(ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getName(),  ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getI18n());
        map.put(ReportOperationEnum.INC_STATISTIC_REPORT_I_D_C.getName(),  ReportOperationEnum.INC_STATISTIC_REPORT_I_D_C.getI18n());
        map.put(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getName(),  ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getI18n());
        map.put(ReportOperationEnum.CHG_STATISTIC_LEVEL.getName(),  ReportOperationEnum.CHG_STATISTIC_LEVEL.getI18n());
        map.put(ReportOperationEnum.CHG_STATISTIC_SOURCE.getName(),  ReportOperationEnum.CHG_STATISTIC_SOURCE.getI18n());
        map.put(ReportOperationEnum.CHG_STATISTIC_TYPE.getName(),  ReportOperationEnum.CHG_STATISTIC_TYPE.getI18n());
        return map;
    }

    protected Map<String, Object> tempData;
    
    protected Map<String,List<Map<String,Object>>> dataSetsTemp;
    
    protected List<Map<String,Object>> resultTemp;
    
    protected List<Map<String,Object>> dataListTemp;
    
    protected int total;
            
    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the dataListTemp
     */
    public List<Map<String, Object>> getDataListTemp() {
        return dataListTemp;
    }

    /**
     * @param dataListTemp the dataListTemp to set
     */
    public void setDataListTemp(List<Map<String, Object>> dataListTemp) {
        this.dataListTemp = dataListTemp;
    }

    /**
     * @return the dataSetsTemp
     */
    public Map<String, List<Map<String, Object>>> getDataSetsTemp() {
        return dataSetsTemp;
    }

    /**
     * @param dataSetsTemp the dataSetsTemp to set
     */
    public void setDataSetsTemp(Map<String, List<Map<String, Object>>> dataSetsTemp) {
        this.dataSetsTemp = dataSetsTemp;
    }

    /**
     * @return the resultTemp
     */
    public List<Map<String, Object>> getResultTemp() {
        return resultTemp;
    }

    /**
     * @param resultTemp the resultTemp to set
     */
    public void setResultTemp(List<Map<String, Object>> resultTemp) {
        this.resultTemp = resultTemp;
    }

    public abstract String getUrl();
    
    public abstract void setDataSet(Collection<Object> dataSet, ReportOperationVO reportOperation);
    
    public abstract void setReportOperationVO(ReportOperationVO reportOperation);

    public Map<String, Object> getTempData () {
        return tempData;
    }

    public void setTempData (Map<String, Object> tempData) {
        this.tempData = tempData;
    }

}
