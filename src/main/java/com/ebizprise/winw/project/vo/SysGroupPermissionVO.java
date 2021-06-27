package com.ebizprise.winw.project.vo;

import org.hibernate.validator.constraints.NotEmpty;

public class SysGroupPermissionVO {

    @NotEmpty
    private Long sysGroupId;             //自訂群組ID
    @NotEmpty
    private String menuId; // 選單編號

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

}
