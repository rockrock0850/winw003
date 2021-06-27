package com.ebizprise.winw.project.vo;

import java.util.ArrayList;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 系統選單 資料物件
 * 
 * @author adam.yeh
 *
 */
public class MenuVO extends BaseVO {

    private String menuId;
    private String menuName;
    private String path;
    private Integer orderId;
    private Boolean openWindow;
    private List<MenuVO> subMenus = new ArrayList<>();

    public String getMenuName () {
        return menuName;
    }

    public void setMenuName (String menuName) {
        this.menuName = menuName;
    }

    public List<MenuVO> getSubMenus () {
        return subMenus;
    }

    public void setSubMenus (List<MenuVO> subMenus) {
        this.subMenus = subMenus;
    }

    public String getPath () {
        return path;
    }

    public void setPath (String path) {
        this.path = path;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Integer getOrderId () {
        return orderId;
    }

    public void setOrderId (Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(Boolean openWindow) {
        this.openWindow = openWindow;
    }

}
