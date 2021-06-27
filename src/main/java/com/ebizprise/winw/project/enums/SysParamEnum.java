package com.ebizprise.winw.project.enums;

/**
 * @author gary.tsai 2019/6/17
 */
public enum SysParamEnum {
    
    INSERT("I"), UPDATE("U"), DELETE("D"),
    
    /**
     * 服務類別代號(OptionId)
     */
    SERVICE("2"),

    /**
     * 服務子類別代號(OptionId)
     */
    SERVICE_2("0"),

    /**
     * 原因類別代號(OptionId)
     */
    KNOWLEDGE("knowledge"),

    /**
     * 原因子類別代號(OptionId)
     */
    KNOWLEDGE_2("knowledge_2");

    private String action;

    SysParamEnum (String action) {
        this.action = action;
    }

    public String action () {
        return action;
    }
    
}
