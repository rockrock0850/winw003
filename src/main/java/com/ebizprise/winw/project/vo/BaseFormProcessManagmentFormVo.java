package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 表單流程管理 表單項目共用 父類別 Vo
 * 
 * The <code>BaseFormProcessManagmentFormVo</code>	
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年7月4日
 */
public class BaseFormProcessManagmentFormVo extends BaseVO {

    private String processId;//流程ID 
    private String formType;//表單類型
    private String division;//科別
    private String processName;//流程名稱
    private String isEnable;//是否啟用

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    
}
