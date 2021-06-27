package com.ebizprise.winw.project.vo;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 表單流程管理 VO
 * 
 * The <code>FormProcessManagmentVO</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public class FormProcessManagmentBaseVO extends BaseVO {

    private String detailId;
    private String processId;
    private int formType;
    private String formName;// 這邊的值會根據formType的數字透過switch case來取得
    private String departmentId;
    private String departmentName;
    private String division;
    private String divisionText;
    private String processName;
    private String isEnable;
    private String updatedAtText;
    private String isC;//是否為會辦單

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public String getFormName() {
        switch (formType) {
            // TODO I18N
            case 1:
                formName = "需求單";// 需求單
                break;

            case 2:
                formName = "需求會辦單";// 需求會辦單
                break;

            case 3:
                formName = "問題單";// 問題單
                break;

            case 4:
                formName = "問題會辦單";// 問題會辦單
                break;

            case 5:
                formName = "事件單";// 事件單
                break;

            case 6:
                formName = "事件會辦單";// 事件會辦單
                break;

            case 7:
                formName = "變更單";// 變更單
                break;

            case 8:
                formName = "工作單";// 工作單
                break;

            case 9:
                formName = "工作會辦單";// 工作會辦單
                break;

            case 10:
                formName = "批次作業中斷對策表管理";// 批次作業中斷對策表管理
                break;

            default:
                formName = "Unknown";
                break;

        }
        return formName;
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

    public String getUpdatedAtText() {
        if(super.getUpdatedAt() != null) {
            this.updatedAtText = DateUtils.toString(super.getUpdatedAt(), DateUtils.pattern12);
        }
        return updatedAtText;
    }

    public void setUpdatedAtText(String updatedAtText) {
        this.updatedAtText = updatedAtText;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDivisionText() {
        divisionText = departmentName + "-" + division;
        return divisionText;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIsC() {
        return isC;
    }

    public void setIsC(String isC) {
        this.isC = isC;
    }

    public String getDetailId () {
        return detailId;
    }

    public void setDetailId (String detailId) {
        this.detailId = detailId;
    }

}
