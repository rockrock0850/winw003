package com.ebizprise.winw.project.enums.form;

import java.util.HashMap;
import java.util.Map;

/**
 * 記錄工作單相關常數
 * @author adam.yeh
 */
public enum FormJobEnum implements Form {

/* 非系統科工作單頁簽
============================== */
    AP1, AP2, AP3, AP4, SP, PT, EA, OA,
    
    // 資料管制科底下分五個可會辦項目
    DC, DC1, DC2, DC3, DB, BATCH,
    
    // 資安規劃/資安管理
    PLAN, MGMT,

/* 程式庫分為兩個資料集, 程式差異清單/BaseLine編號
============================== */
    LIBRARY,
    BASELINE,
    
/* 作業關卡的工作項目代號
============================== */
    /**
     * 測試人員
     */
    TPERSON("測試人員"),
    
    /**
     * 會辦處理人員
     */
    CSPERSON("會辦處理人員"),
    
    /**
     * 處理人員
     */
    SPERSON("處理人員"),
    
/* 限制跨部開會辦單
 * 資安部(A01421)與資訊部(A01419)的資料要切割顯示
============================== */
    /**
     * 資安部的頁簽代表Id
     */
    SECURITY("A01421", "tabPlan,tabMgmt");
    
    private static final Map<String, FormJobEnum> BY_NAME = new HashMap<>();
    
    private String symbol;
    private String wording;

    static {
        for (FormJobEnum e : values()) {
            BY_NAME.put(e.name(), e);
        }
    }
    
    public static boolean isContaining (String name) {
        return BY_NAME.containsKey(name);
    }
    
    private FormJobEnum () {}
    
    private FormJobEnum (String wording) {
        this.wording = wording;
    }

    private FormJobEnum (String symbol, String wording) {
        this.wording = wording;
        this.symbol = symbol;
    }
    
    public String wording () {
        return this.wording;
    }

    public String symbol () {
        return symbol;
    }
    
}
