package com.ebizprise.winw.project.vo;

import java.util.ArrayList;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 資料表:SYS_OPTION
 * 
 * @author momo.liu 2020/08/24
 */
public class SystemOptionVO extends BaseVO {

    private Integer sort;           // 順序
    private String optionId;        // 選單編號
    private String name;            // 選單名稱 (name與display的欄位內容一致)
    private String value;           // 選單值
    private String display;         // 選單名稱顯示
    private String parentId;        // 父選單編號
    private String active;          // 狀態 _是否啟用
    private String searchCol1;
    private String searchCol2;
    private String isKnowledge;     // 建議加入「處理方案」於問題知識庫?

    private List<SystemOptionVO> serviceType;                           // 父選單(服務類別)
    private List<SystemOptionVO> subServiceType = new ArrayList<>();    // 子選單(服務子類別)

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<SystemOptionVO> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<SystemOptionVO> serviceType) {
        this.serviceType = serviceType;
    }

    public List<SystemOptionVO> getSubServiceType() {
        return subServiceType;
    }

    public void setSubServiceType(List<SystemOptionVO> subServiceType) {
        this.subServiceType = subServiceType;
    }

    public String getActive () {
        return active;
    }

    public void setActive (String active) {
        this.active = active;
    }

    public String getSearchCol1 () {
        return searchCol1;
    }

    public void setSearchCol1 (String searchCol1) {
        this.searchCol1 = searchCol1;
    }

    public String getSearchCol2 () {
        return searchCol2;
    }

    public void setSearchCol2 (String searchCol2) {
        this.searchCol2 = searchCol2;
    }

    public String getIsKnowledge () {
        return isKnowledge;
    }

    public void setIsKnowledge (String isKnowledge) {
        this.isKnowledge = isKnowledge;
    }

}
