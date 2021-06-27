package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-會辦單的欄位
 * @author adam.yeh
 */
public enum C implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated"),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),

    C_16(66, "form.search.column.sourceId", INPUT, "sourceId"),
    C_1(875, "form.search.column.c.summary", INPUT, "summary", true),
    C_2(876, "form.search.column.c.content", INPUT, "content"),
    C_3(877, "form.search.column.c.hostHandle", INPUT, "hostHandle"),
    C_4(878, "form.search.column.c.countersignedHandle", INPUT, "countersignedHandle"),
    C_5(879, "form.search.column.system", INPUT, "system"),
    C_6(880, "form.search.column.assetGroup", INPUT, "assetGroup"),
    C_7(881, "form.search.column.solvingGroup", INPUT, "userGroup"),
    C_8(882, "form.search.column.userSolving", INPUT, "userId"),
    C_9(883, "form.search.column.sClass", SELECT, "sClass"),
    C_0(884, "form.search.column.c.assignTime", DATE_RANGE, "assignTime"),
    C_10(885, "form.search.column.c.ast", DATE_RANGE, "ast"),
    C_11(886, "form.search.column.c.act", DATE_RANGE, "act"),
    C_12(887, "form.search.column.c.eot", DATE_RANGE, "eot"),
    C_13(888, "form.search.column.c.ect", DATE_RANGE, "ect"),
    C_15(1000, "report.operation.chg.alterResult.1", SELECT, "isAlterDone"),
    C_14(907, "form.search.column.c.mect", DATE_RANGE, "mect", true);
    
    private static final Map<String, C> BY_INDEX = new HashMap<String, C>();
    private static final Map<String, C> BY_VONAME = new HashMap<String, C>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (C e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static C valueOfNumber (String number) {
        return BY_INDEX.get(number);
    }
    
    public static boolean isContaining (String number) {
        return BY_INDEX.containsKey(number);
    }
    
    public static Map<String, String> valueOfVoName(String voName) {
        Map<String, String> map = new HashMap<>();
        map.put("name", BY_VONAME.get(voName).getName());
        map.put("type", BY_VONAME.get(voName).getType());
        return map;
    }

    public static boolean existVoName(String voName) {
        return BY_VONAME.containsKey(voName);
    }

    private C (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private C (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private C(int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private C(int index, String name, String type, String voName, boolean isDefaultCol) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
        this.isDefaultCol = isDefaultCol;
    }

    public int getIndex () {
        return index;
    }

    public int getLength () {
        return length;
    }

    public String getName () {
        return name;
    }

    public String getType () {
        return type;
    }

    public String getVoName () {
        return voName;
    }

    public boolean getIsDefaultCol () {
        return isDefaultCol;
    }

}