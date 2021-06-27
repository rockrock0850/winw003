package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-變更單的欄位
 * @author adam.yeh
 */
public enum CHG implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated"),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),

    CHG_1(66, "form.search.column.sourceId", INPUT, "sourceId"),
    CHG_2(67, "form.search.column.chg.summary", INPUT, "summary", true),
    CHG_3(68, "form.search.column.chg.content", INPUT, "content", true),
    CHG_4(3000, "form.search.column.cCategory", SELECT, "cCategory"),
    CHG_5(3001, "form.search.column.cClass", SELECT, "cClass"),
    CHG_6(3002, "form.search.column.cComponent", SELECT, "cComponent"),
    CHG_7(72, "form.search.column.chg.effectSystem", INPUT, "effectSystem", true),
    CHG_8(3003, "form.search.column.chg.isNewSystem", SELECT, "isNewSystem"),
    CHG_9(74, "form.search.column.chg.isNewService", SELECT, "isNewService"),
    CHG_10(75, "form.search.column.chg.isUrgent", SELECT, "isUrgent"),
    CHG_11(76, "form.search.column.chg.standard", SELECT, "standard"),
    CHG_12(77, "form.search.column.chg.changeType", SELECT, "changeType"),
    CHG_13(78, "form.search.column.chg.changeRank", SELECT, "changeRank"),
    CHG_14(79, "form.search.column.chg.isEffectField", SELECT, "isEffectField"),
    CHG_15(80, "form.search.column.chg.isEffectAccountant", SELECT, "isEffectAccountant"),
    CHG_16(81, "form.search.column.chg.cat", DATE_RANGE, "cat"),
    CHG_17(82, "form.search.column.chg.cct", DATE_RANGE, "cct"),
    CHG_18(89, "form.search.column.chg.isModifyProgram", SELECT, "isModifyProgram");
    
    private static final Map<String, CHG> BY_INDEX = new HashMap<String, CHG>();
    private static final Map<String, CHG> BY_VONAME = new HashMap<String, CHG>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (CHG e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static CHG valueOfNumber (String number) {
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

    private CHG (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private CHG (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private CHG (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private CHG (int index, String name, String type, String voName, boolean isDefaultCol) {
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
    
    public String getVoName() {
        return voName;
    }

    public boolean getIsDefaultCol () {
        return isDefaultCol;
    }

}