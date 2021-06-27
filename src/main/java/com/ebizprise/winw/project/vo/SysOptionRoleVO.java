package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * SYS_OPTION_ROLE value object
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2020年3月5日
 */
public class SysOptionRoleVO extends BaseVO {

    private String roleId;
    private String optionId;
    private String value;
    private String description;
    private String condition;

    public String getRoleId () {
        return roleId;
    }

    public void setRoleId (String roleId) {
        this.roleId = roleId;
    }

    public String getOptionId () {
        return optionId;
    }

    public void setOptionId (String optionId) {
        this.optionId = optionId;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getCondition () {
        return condition;
    }

    public void setCondition (String condition) {
        this.condition = condition;
    }

}
