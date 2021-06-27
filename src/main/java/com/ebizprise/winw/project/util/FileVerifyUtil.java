package com.ebizprise.winw.project.util;

import java.io.File;

import com.ebizprise.project.utility.str.StringConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 檔案 驗證共用工具
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年8月1日
 */
@Component
public class FileVerifyUtil extends DataVerifyUtil {
    
    public FileVerifyUtil() {
        errorMessage = new StringBuffer();
    }
    
    /**
     * 檢查檔案是否存在
     * 
     * @return FileVerifyUtil
     */
    public FileVerifyUtil exists(MultipartFile file) {
        if(file == null) {
            errorMessage.append("請選擇檔案").append(StringConstant.SEMICOLON);//請選擇檔案
        }
        
        return this;
    }
    
    /**
     * 檢查檔案是否存在
     * 
     * @return  FileVerifyUtil
     */
    public FileVerifyUtil exists(File file) {
        if(file == null) {
            errorMessage.append("請選擇檔案").append(StringConstant.SEMICOLON);//請選擇檔案
        }
        
        return this;
    }
    
    /**
     * 檢查是否為允許的副檔名
     * 
     * @param extensionName
     * @param allowExtension
     * @return FileVerifyUtil
     */
    public FileVerifyUtil extension(String extensionName,String allowExtension) {
        if(StringUtils.isNotBlank(extensionName) && StringUtils.isNotBlank(allowExtension)) {
            if(allowExtension.indexOf(extensionName) == -1) {
                errorMessage.append("非允許上傳的上傳檔案類型").append(StringConstant.SEMICOLON);//非允許上傳的上傳檔案類型
            }
        }
        
        return this;
    }
    
    /**
     * 檢核檔案大小
     * 
     * @param file
     * @param allowSize
     * @return FileVerifyUtil
     */
    public FileVerifyUtil fileSize(MultipartFile file,Long allowSize) {
        if(file != null) {
            Long fileSize = file.getSize() / (1024 * 1024);

            if(fileSize > allowSize) {
                errorMessage.append("上傳檔案超過大小限制").append(StringConstant.SEMICOLON);//上傳檔案超過大小限制
            }
        } else {
            errorMessage.append("請選擇檔案");//請請選擇檔案
        }
        
        return this;
    }
    
    /**
     * 檢核檔案大小
     * 
     * @param file
     * @param allowSize
     * @return FileVerifyUtil
     */
    public FileVerifyUtil fileSize(File file,Long allowSize) {
        if(file != null) {
            Long fileSize = file.length() / (1024 * 1024);
            
            if(fileSize > allowSize) {
                errorMessage.append("上傳檔案超過大小限制").append(StringConstant.SEMICOLON);//上傳檔案超過大小限制
            }
        } else {
            errorMessage.append("請選擇檔案");//請選擇檔案
        }
        
        return this;
    }
    
}
