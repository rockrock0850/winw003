package com.ebizprise.winw.project.enums.page;

/**
 * 定義所有頁面需要用到的頁面種類
 * 
 * @author adam.yeh
 *
 */
public interface Pages {

    /**
     * 首頁
     * 
     * @return
     */
    public String initPage ();
    
    /**
     * 新增頁
     * 
     * @return
     */
    default public String addPage () {
        return "";
    }
    
    /**
     * 編輯頁
     * 
     * @return
     */
    default public String editPage () {
        return "";
    }
    
    /**
     * 刪除頁
     * 
     * @return
     */
    default public String deletePage () {
        return "";
    }

    /**
     * 導頁控制器
     * 
     * @return
     */
    default public String dispatch () {
        return "";
    };
    
}
