package com.ebizprise.winw.project.enums.form;

/**
 * 1. 紀錄檢核表單會定義到的檢核型態分類<br>
 * 2. 紀錄稽催通知相關參數
 * @author adam.yeh
 */
public enum FormVerifyType {

    /**
     * 是否有開過延伸單
     */
    STRETCH_ZERO,

    /**
     * 1. 判斷是否需要等待延伸單全部結案</br>
     * 2. 判斷延伸單是否已全部結案
     */
    STRETCH_FINISHED,
    
    /**
     * 問題單稽催通知代表符號
     */
    Q("問題單"), 

    /**
     * 需求單稽催通知代表符號
     */
    SR("需求單"),

    /**
     * 事件單稽催通知代表符號
     */
    INC("事件單"),

    /**
     * 事件單(時)稽催通知代表符號
     */
    INCH("事件單(時)"),

    /**
     * 已逾期
     */
    EXPIRED("已逾期"),

    /**
     * 即將逾期
     */
    EXPIRE_SOON("即將逾期");
    
    String desc;
    
    private FormVerifyType () {
    }

    private FormVerifyType (String desc) {
        this.desc = desc;
    }
    
    /**
     * 檢查輸入的要比對的參數是否已定義
     * @param value
     * @return
     * @author adam.yeh
     */
    public static boolean contains (String value) {
        for (FormVerifyType v : FormVerifyType.values()) {
            if (v.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

    public String desc () {
        return desc;
    }
    
}
