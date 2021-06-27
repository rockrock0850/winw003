package com.ebizprise.winw.project.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 介接外部系統會用到的常數<br>
 * P.S. <br>
 * 因外部系統回來的鍵值不是使用Java常規的Lower camel case命名法則, <br>
 * 所以統一將鍵值記錄在這, 且考量到往後會有重名等擴充性議題, <br>
 * 將變數名稱以原始鍵值名稱來定義。<br><br>
 * 參考文件 : WINW003_電子表單API 服務_版控介接資訊_1212.docx
 * @author adam.yeh
 */
public enum WsEnum {

    /* 版控系統回來的參數鍵值
    ============================================ */
    fN, msg, baseLine, fileName, qY_Status,
    
    /* VersionCodeDiffProc的狀態碼
    ============================================ */
    VCDIFF_0("0", "version.form.error.5"),
    VCDIFF_1("1", "version.form.success.2"),
    VCDIFF_2("2", "version.form.error.6"),
    VCDIFF_3("3", "version.form.error.7"),
    VCDIFF_4("4", "version.form.error.8"),
    
    /* VersionCodeBaseLine的狀態碼
    ============================================ */
    VCBASE_0("0", "version.form.error.9"),
    VCBASE_1("1", "version.form.success.3"),
    VCBASE_2("2", "version.form.error.10"),
    VCBASE_3("3", "version.form.error.8");
    
    private static final Map<String, String> FROM_VCDIFF = new HashMap<>();
    private static final Map<String, String> FROM_VCBASE = new HashMap<>();
    
    private String code, msgCode;

    static {
        for (WsEnum e : values()) {
            if (e.name().contains("VCDIFF")) {
                FROM_VCDIFF.put(e.code, e.msgCode);
            }
            
            if (e.name().contains("VCBASE")) {
                FROM_VCBASE.put(e.code, e.msgCode);
            }
        }
    }
    
    public static String fromVcDiff (String statusCode) {
        return FROM_VCDIFF.get(statusCode);
    }
    
    public static String fromVcBase (String statusCode) {
        return FROM_VCBASE.get(statusCode);
    }
    
    private WsEnum () {
        
    };

    private WsEnum (String code, String msgCode) {
        this.code = code;
        this.msgCode = msgCode;
    };
    
    public String code () {
        return this.code;
    }
    
    public String msg () {
        return this.msgCode;
    }
    
}
