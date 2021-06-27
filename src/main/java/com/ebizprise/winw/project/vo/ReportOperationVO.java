/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.vo;

import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 報表作業共用VO
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月16日
 */
public class ReportOperationVO  extends BaseVO {
    
    private String operation;
    private String isExclude;
    private String startInterval;
    private String endInterval; 
    private String universal;
    private String section;
    private String eventType;
    private String formStatus;
    private List<String> months;

    private Map<String,List<Map<String,Object>>> resultDataList;
    private Map<String,List<Map<String,Object>>> dynamicData;
    private Map<Integer,Map<String,Object>> statisticsResult;
    private Map<String, Object> resultMap;
    private List<Map<String,Object>> resultList;
    
    @Override
    public String toString() {
        return operation;
    }
    
    public Map<String, List<Map<String, Object>>> getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(Map<String, List<Map<String, Object>>> dynamicData) {
        this.dynamicData = dynamicData;
    }

    /**
     * @return the resultList
     */
    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * @return the universal
     */
    public String getUniversal() {
        return universal;
    }

    /**
     * @param universal the universal to set
     */
    public void setUniversal(String universal) {
        this.universal = universal;
    }

    /**
     * @return the resultDataList
     */
    public Map<String, List<Map<String, Object>>> getResultDataList() {
        return resultDataList;
    }

    /**
     * @param resultDataList the resultDataList to set
     */
    public void setResultDataList(Map<String, List<Map<String, Object>>> resultDataList) {
        this.resultDataList = resultDataList;
    }

    /**
     * @return the statisticsResult
     */
    public Map<Integer, Map<String, Object>> getStatisticsResult() {
        return statisticsResult;
    }

    /**
     * @param statisticsResult the statisticsResult to set
     */
    public void setStatisticsResult(Map<Integer, Map<String, Object>> statisticsResult) {
        this.statisticsResult = statisticsResult;
    }

    /**
     * @return the startInterval
     */
    public String getStartInterval() {
        return startInterval;
    }

    /**
     * @param startInterval the startInterval to set
     */
    public void setStartInterval(String startInterval) {
        this.startInterval = startInterval;
    }

    /**
     * @return the endInterval
     */
    public String getEndInterval() {
        return endInterval;
    }

    /**
     * @param endInterval the endInterval to set
     */
    public void setEndInterval(String endInterval) {
        this.endInterval = endInterval;
    }

    /**
     * @return the isExclude
     */
    public String getIsExclude() {
        return isExclude;
    }

    /**
     * @param isExclude the isExclude to set
     */
    public void setIsExclude(String isExclude) {
        this.isExclude = isExclude;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getResultMap () {
        return resultMap;
    }

    public void setResultMap (Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public List<String> getMonths () {
        return months;
    }

    public void setMonths (List<String> months) {
        this.months = months;
    }
    
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFormStatus () {
        return formStatus;
    }

    public void setFormStatus (String formStatus) {
        this.formStatus = formStatus;
    }

}
