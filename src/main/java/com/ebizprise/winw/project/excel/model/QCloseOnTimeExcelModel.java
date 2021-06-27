/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.excel.model;

import com.ebizprise.project.utility.doc.excel.ExcelCell;

/**
 *報表作業:問題處理完成率 與 問題處理完成率(含特殊結案)   
 *   匯出的值  與  Excel 列號對應 	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月21日
 */
public class QCloseOnTimeExcelModel {
    @ExcelCell(index = 0)
    private String targetFinishedYearMonth;
    @ExcelCell(index = 1)
    private String qFormAll;
    @ExcelCell(index = 2)
    private String qFormFinished;
    @ExcelCell(index = 3)
    private String completionRate;
    
    /**
     * @return the targetFinishedYearMonth
     */
    public String getTargetFinishedYearMonth() {
        return targetFinishedYearMonth;
    }

    /**
     * @param targetFinishedYearMonth the targetFinishedYearMonth to set
     */
    public void setTargetFinishedYearMonth(String targetFinishedYearMonth) {
        this.targetFinishedYearMonth = targetFinishedYearMonth;
    }

    /**
     * @return the qFormAll
     */
    public String getqFormAll() {
        return qFormAll;
    }

    /**
     * @param qFormAll the qFormAll to set
     */
    public void setqFormAll(String qFormAll) {
        this.qFormAll = qFormAll;
    }

    /**
     * @return the qFormFinished
     */
    public String getqFormFinished() {
        return qFormFinished;
    }

    /**
     * @param qFormFinished the qFormFinished to set
     */
    public void setqFormFinished(String qFormFinished) {
        this.qFormFinished = qFormFinished;
    }

    /**
     * @return the completionRate
     */
    public String getCompletionRate() {
        return completionRate;
    }

    /**
     * @param completionRate the completionRate to set
     */
    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public QCloseOnTimeExcelModel() {}
    
    public QCloseOnTimeExcelModel(String targetFinishedYearMonth,
            String qFormAll,String qFormFinished,String completionRate) {
        this.targetFinishedYearMonth = targetFinishedYearMonth;
        this.qFormAll = qFormAll;
        this.qFormFinished = qFormFinished;
        this.completionRate = completionRate;
    }
}
