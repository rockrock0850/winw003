/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service;

import javax.servlet.http.HttpServletResponse;

import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 取得各報表資訊實作類別
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月17日
 */
public interface IReportOperationSelectService {
    /**
     * 取得網頁路篒
     * 
     * @param operation
     * @param type
     * @return ReportOperationVO
     */    
    String getPageUrl(String operation);
    
    /**
     * 取得各類報表查詢的回傳轡果
     * 
     * @param reportOperation
     * 
     */    
    void setReportOperationResult(ReportOperationVO reportOperation);
    /**
     * 匯出EXCEL
     * 
     * @param reportOperation
     * @param response 
     */     
    void exportExcel(ReportOperationVO reportOperation, HttpServletResponse response);
}
