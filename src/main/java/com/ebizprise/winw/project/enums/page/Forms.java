package com.ebizprise.winw.project.enums.page;

/**
 * 表單
 * 
 * @author adam.yeh
 */
public interface Forms extends Pages {

    /**
     * 表頭
     * 
     * @return
     */
    default public String header () {
        return "";
    }
    
    /**
     * 頁簽 表單資訊
     * 
     * @return
     */
    default public String formInfo () {
        return "";
    }
    
    /**
     * 頁簽 表單資訊2
     * 
     * @return
     */
    default public String checkPerson () {
        return "";
    }
    
    /**
     * 頁簽 衝擊分析
     * 
     * @return
     */
    default public String impactAnalysis () {
        return "";
    }
    
    /**
     * 頁簽 處理方案
     * 
     * @return
     */
    default public String program () {
        return "";
    }
    
    /**
     * 頁簽 日誌
     * 
     * @return
     */
    default public String log () {
        return "";
    }
    
    /**
     * 頁簽 審核
     * 
     * @return
     */
    default public String checkLog () {
        return "";
    }
    
    /**
     * 頁簽 附件
     * 
     * @return
     */
    default public String fileList () {
        return "";
    }
    
    /**
     * 頁簽 關連表單
     * 
     * @return
     */
    default public String linkList () {
        return "";
    }
    
    /**
     * 頁簽 流程資訊
     * 
     * @return
     */
    default public String processInfo () {
        return "";
    }

    
    /**
     * 頁簽 系統
     * 
     * @return
     */
    default public String sp () {
        return "";
    }
    
    /**
     * 頁簽 連管
     * 
     * @return
     */
    default public String pt () {
        return "";
    }
    
    /**
     * 頁簽 程式清單
     * 
     * @return
     */
    default public String programs () {
        return "";
    }

    /**
     * 頁簽 資安規劃/資安管理
     * 
     * @return
     */
    default public String planmgmt () {
        return "";
    }

    /**
     * 頁簽 OPEN清單
     * 
     * @return
     */
    default public String open () {
        return "";
    }

    /**
     * 頁簽 OA
     * 
     * @return
     */
    default public String oa () {
        return "";
    }

    /**
     * 頁簽 程式庫
     * 
     * @return
     */
    default public String library () {
        return "";
    }

    /**
     * 頁簽 電商
     * 
     * @return
     */
    default public String ea () {
        return "";
    }

    /**
     * 頁簽 資管
     * 
     * @return
     */
    default public String dc () {
        return "";
    }

    /**
     * 頁簽 DB
     * 
     * @return
     */
    default public String db () {
        return "";
    }

    /**
     * 頁簽 批次
     * 
     * @return
     */
    default public String batch () {
        return "";
    }

    /**
     * 頁簽 設計1234科
     * 
     * @return
     */
    default public String ap () {
        return "";
    }
    
}
