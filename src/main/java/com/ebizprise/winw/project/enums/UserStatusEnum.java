package com.ebizprise.winw.project.enums;

/**
 * @author gary.tsai 2019/5/22
 */
public enum UserStatusEnum {
    LOGIN("1", "LOGIN"),
    LOGOUT("2", "LOGOUT"),
    TIMEOUT("4", "TIMEOUT"),
    FAIL("3", "FAIL");

    public String status;
    public String desc;

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    UserStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
