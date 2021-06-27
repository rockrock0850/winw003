package com.ebizprise.winw.project.vo;

import java.util.ArrayList;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 服務類別選單 資料物件
 *
 */
public class ServiceTypeMenuVO extends BaseVO {

    private String optionId; //選單編號
    private String name; //選單名稱
    private String value; //選單值
    private String display; //選單名稱顯示
    private String parentId; //父選單編號
    private String active; //狀態 _是否啟用

    private List<SysGroupPermissionVO> groupPermissions; //群組選單權限
    private List<ServiceTypeMenuVO> subMenus = new ArrayList<>();

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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public List<SysGroupPermissionVO> getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(List<SysGroupPermissionVO> groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    public List<ServiceTypeMenuVO> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<ServiceTypeMenuVO> subMenus) {
        this.subMenus = subMenus;
    }
    
}
