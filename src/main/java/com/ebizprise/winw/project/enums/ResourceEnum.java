package com.ebizprise.winw.project.enums;

import com.ebizprise.project.utility.str.StringConstant;

/**
 * 記錄Class path下特定目錄的資源檔位址
 * 
 * @author adam.yeh
 * @create date: NOV 16, 2017
 */
public enum ResourceEnum {
    
    SQL("/sql", ".sql"),
    
    SQL_COMMON("/sql/common", ".sql"),

    /**
     * 系統批次管理
     */
    SQL_JOBS("/sql/settingsystemjobs", ".sql"),

    /**
     * 登入登出查詢作業
     */
    SQL_LOGON_RECORD("/sql/logonrecord", ".sql"),
    
    /**
     * 表單流程管理
     */
    SQL_FORM_PROCESS_MANAGMENT("/sql/formprocessmanagment",".sql"),
    
    /**
     * 表單作業
     */
    SQL_FORM_OPERATION("/sql/formoperation",".sql"),
    
    /**
     * 知識庫
     */
    SQL_FORM_OPERATION_KL("/sql/formoperation/kl",".sql"),
    
    /**
     * 需求單
     */
    SQL_FORM_OPERATION_SR("/sql/formoperation/sr",".sql"),
    
    /**
     * 問題單
     */
    SQL_FORM_OPERATION_Q("/sql/formoperation/q",".sql"),
    
    /**
     * 事件單
     */
    SQL_FORM_OPERATION_INC("/sql/formoperation/inc",".sql"),
    
    /**
     * 變更單
     */
    SQL_FORM_OPERATION_CHG("/sql/formoperation/chg",".sql"),
    
    /**
     * 工作單
     */
    SQL_FORM_OPERATION_JOB("/sql/formoperation/job",".sql"),
    
    /**
     * 會辦單
     */
    SQL_FORM_OPERATION_C("/sql/formoperation/c",".sql"),
    
    /**
     * 批次作業中斷對策表管理
     */
    SQL_FORM_OPERATION_BA("/sql/formoperation/ba",".sql"),
    
    /**
     * 首頁資訊
     */
    SQL_DASH_BOARD_DATA("/sql/dashboard",".sql"),
    /**
     * 群組選單權限
     */
    SQL_GROUP_PERMISSION("/sql/grouppermission", ".sql"),
    /**
     * 需求單報表查詢作業
     */
    SQL_REPORT_OPERATION_SR("/sql/reportoperation/sr", ".sql"),
    /**
     * 事件單報表查詢作業
     */
    SQL_REPORT_OPERATION_INC("/sql/reportoperation/inc", ".sql"),
    /**
     * 問題單報表查詢作業
     */
    SQL_REPORT_OPERATION_Q("/sql/reportoperation/q", ".sql"),
    /**
     * 變更單報表查詢作業
     */
    SQL_REPORT_OPERATION_CHG("/sql/reportoperation/chg", ".sql"),
    /**
     * 表單查詢作業
     */
    SQL_FORM_SEARCH("/sql/formoperation/formsearch", ".sql"),
    
    /**
     * 匯入假日資料
     */
    SQL_HOLIDAY_IMPORT("/sql/holidayimport", ".sql"),
    
    /**
     * 系統名稱管理
     */
    SQL_SYSTEM_NAME_MANAGEMENT("/sql/systemoperation", ".sql"),
    
    /**
     * 標準變更作業管理、工作組別管理、工作要項管理、服務類別管理
     */
    SQL_SYSTEM_MANAGEMENT("/sql/systemoperation", ".sql");

    private String dir;
    private String file;
    private String extension;

    /**
     * @param dir       資源擋路徑
     * @param extension 檔案類型
     */
    private ResourceEnum (String dir, String extension) {
        this.dir = dir;
        this.extension = extension;
    }
    
    public ResourceEnum getResource (String name) {
        this.file = StringConstant.SLASH + name;
        return this;
    }
    
    public String file () {
        return this.file;
    }
    
    public String dir () {
        return this.dir;
    }
    
    public String extension () {
        return this.extension;
    }
    
}