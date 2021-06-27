/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service;

import javax.servlet.http.HttpServletResponse;

import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 取得報表作業 相關資訊	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月16日
 */
public interface IReportOperationService {
    /**
     * 前往報表頁面
     * 
     * @return
     */    
    @SuppressWarnings("javadoc")
    String getPageUrl(); 
    
    /**
     * 取得查詢結果
     * 
     * @param reportOperation
     * 
     */
   void setResultDataList(ReportOperationVO reportOperation); 
    
    /**
     * 取得匯出Excel檔案流
     * 
     * @param reportOperation
     * @param response
     * 
     */    
    void exportExcel(ReportOperationVO reportOperation,HttpServletResponse response);
}
