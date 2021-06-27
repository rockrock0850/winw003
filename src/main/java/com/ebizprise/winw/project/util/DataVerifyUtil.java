package com.ebizprise.winw.project.util;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.CommonStringUtil;

/**
 * 驗證資料共用工具
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年7月26日
 */
public class DataVerifyUtil {
    
    public static final String ERROR_MSG = "errorMsg";// 錯誤訊息Key
    
    protected StringBuffer errorMessage;
    
    public DataVerifyUtil() {
        errorMessage = new StringBuffer();
    }
    
    /**
     * 比對o1裡面的欄位值是否與o2相同
     * @param o1
     * @param o2
     * @param clazz
     * @param fields getter的名稱(EX. getUserCreated)
     * @return
     * @author adam.yeh
     * @throws Exception 
     */
    public <T> DataVerifyUtil equals (Object o1, Object o2, List<String> fields, Class<T> clazz) throws Exception {
        if (o1 == null || o2 == null) {
            throw new NullPointerException("o1 == null || o2 == null from DataVerifyUtil equals().");
        }

        Object v1, v2;
        Method method;
        
        for (String field : fields) {
            method = clazz.getMethod(field);
            v1 = method.invoke(o1);
            v2 = method.invoke(o2);
            
            if (v1 == null || v2 == null) {
                continue;
            }
            
            if (!v1.equals(v2)) {
                errorMessage.append("表頭資料發生錯誤, 請重新進入表單。");
            }
        }
        
        return this;
    }

    /**
     * 檢核字串是否為空
     * 
     * @param value 驗證的值
     * @param columnName 欄位中文名稱
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil string(String value,String columnName) {
        if(StringUtils.isBlank(value)) {
            errorMessage.append("請輸入").append(" ").append(columnName).append(";");
            
            return this;
        }
        return this;
    }
    
    /**
     * 檢核字串是否為空,若檢核失敗則使用傳入的錯誤訊息
     * 
     * @param value 驗證的值
     * @param message 錯誤訊息
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil stringWithErrorMsg(String value,String message) {
        if(StringUtils.isBlank(value)) {
            errorMessage.append(message).append(";");
            
            return this;
        }
        return this;
    }
    
    /**
     * 檢核字串長度
     * 
     * @param value 驗證的值
     * @param columnName 欄位中文名稱
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil length(String value,int length,String columnName) {
        String errorMsg = columnName + " ";
        
        if(StringUtils.isBlank(value)) {
            errorMessage.append("請輸入").append(" ").append(columnName).append(";");//請輸入 xxxxx
            
            return this;
        }
        
        if(value.length() > length) {
            errorMessage.append(errorMsg).append("超過長度,長度上限為:").append(length).append(";");//超過長度,長度上限為:
            
            return this;
        }
        return this;
    }
    
    /**
     * 驗證數字
     * 
     * @param value 驗證的值
     * @param columnName 欄位中文名稱
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil number(String value,String columnName) {
        String errorMsg = columnName + " ";
        
        if(!StringUtils.isNumeric(value)) {
            errorMessage.append(errorMsg).append("請輸入正確的數字").append(";");//請輸入正確的數字
            
            return this;
        }
        
        if(Integer.parseInt(value) == 0) {
            errorMessage.append("請輸入").append(" ").append(columnName).append(";");//請輸入 xxxxx
            
            return this;
        }
        
        return this;
    }
    
    /**
     * 驗證電話
     * 
     * @param value 驗證的值
     * @param columnName 欄位中文名稱
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil phone(String value,String columnName) {
        String errorMsg = columnName + " ";
        
        value = value.replace("-", "").replace("#", "");
        
        if(StringUtils.isBlank(value)) {
            errorMessage.append(errorMsg).append("請輸入正確的電話格式").append(";");//請輸入正確的電話格式
            
            return this;
        }
        
        if(!StringUtils.isNumeric(value)) {
            errorMessage.append(errorMsg).append("請輸入正確的數字").append(";");//請輸入正確的數字
            
            return this;
        }
        return this;
    }
    
    /**
     * 驗證Email
     * 
     * @param value
     * @param columnName
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil email(String value,String columnName) {
        if(!CommonStringUtil.isEmailFormat(value)) {
            errorMessage.append(columnName).append(" ").append("格式不正確").append(";");//格式不正確;
        }

        return this;
    }
    
    /**
     * 檢核日期
     * 
     * @param date
     * @param columnName
     * @return FormColumnVerifyUtil
     */
    public DataVerifyUtil date(Date date,String columnName) {
        try {
            if(Objects.isNull(date)) {
                errorMessage.append("請輸入").append(" ").append(columnName).append(";");//請選擇

                return this;
            } else {
                String dateStr = DateUtils.toString(date, DateUtils._PATTERN_YYYYMMDD);
                if(StringUtils.isBlank(dateStr)) {
                    errorMessage.append(columnName).append(" ").append("格式錯誤").append(";"); //格式錯誤;
                }
                return this;
            }
        } catch(Exception e) {
            errorMessage.append(columnName).append(" ").append("格式錯誤").append(";"); //格式錯誤;
            
            return this;
        }
    }
    
    /**
     * 傳入物件以及自定義訊息,檢查物件是否存在
     * 
     * @param object 物件
     * @param message 自定義訊息
     */
    public DataVerifyUtil object(Object object,String message) {
        if(Objects.isNull(object)) {
            errorMessage.append(message).append(";");
        }
        return this;
    }
    
    /**
     * 檢查集合是否為空
     * 
     * @param list
     * @param message 自定義訊息
     * @author adam.yeh
     */
    public DataVerifyUtil list (List<?> list, String message) {
        if(CollectionUtils.isEmpty(list)) {
            errorMessage.append(message).append(";");
        }
        return this;
    }
    
    /**
     * 輸出Map
     * 
     * @return Map<String,Object>
     */
    public Map<String,Object> build() {
        Map<String,Object> resultMp = new HashMap<>();

        if(errorMessage.length() > 0) {
            String result = errorMessage.toString();
            result = (result.substring(0,result.length() -1)).replace(";", "\r\n");
            resultMp.put(ERROR_MSG, result);
        }

        resultMp.put("isSuccess", !(errorMessage.length() > 0));
        
        return resultMp;
    }
    
    /**
     * 輸出字串
     * 
     * @return String
     */
    public String buildErrorMsg() {
        String result = "";
        
        if(errorMessage.length() > 0) {
            result = errorMessage.toString();
            result = (result.substring(0,result.length() -1)).replace(";", "\r\n");
        }
        
        return result;
    }

    /**
     *  自定義錯誤訊息
     * 
     * @param error
     * @author adam.yeh
     */
    public void append (String error) {
        errorMessage.append(error).append(";");
    }
    
}
