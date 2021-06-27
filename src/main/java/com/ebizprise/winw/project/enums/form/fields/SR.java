package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-需求單的欄位
 * @author adam.yeh
 */
public enum SR implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated"),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),

    FORM_PROGRAM_1(801, "form.search.column.form.program.processProgram", INPUT, "processProgram"),
    FORM_PROGRAM_2(802, "form.search.column.form.program.indication", INPUT, "indication"),
    FORM_PROGRAM_3(803, "form.search.column.form.program.reason", INPUT, "reason"),

    SR_1(11, "form.search.column.sr.summary", INPUT, "summary", true),
    SR_2(12, "form.search.column.sr.content", INPUT, "content"),
    SR_3(13, "form.search.column.sr.ect", DATE_RANGE, "ect", true),
    SR_4(14, "form.search.column.sr.act", DATE_RANGE, "act"),
    SR_5(15, "form.search.column.sr.eot", DATE_RANGE, "eot", true),
    SR_6(16, "form.search.column.sClass", SELECT, "sClass"),
    SR_7(17, "form.search.column.sSubClass", SELECT, "sSubClass"),
    SR_8(18, "form.search.column.system", INPUT, "system", true),
    SR_9(19, "form.search.column.assetGroup", INPUT, "assetGroup"),
    SR_10(20, "form.search.column.cCategory", SELECT, "cCategory"),
    SR_11(21, "form.search.column.cClass", SELECT, "cClass"),
    SR_12(22, "form.search.column.cComponent", SELECT, "cComponent"),
    SR_13(23, "form.search.column.effectScope", SELECT, "effectScope"),
    SR_14(24, "form.search.column.urgentLevel", SELECT, "urgentLevel"),
    SR_15(25, "form.search.column.sr.requireRank", INPUT, "requireRank"),
    SR_16(26, "form.search.column.sr.division", INPUT, "division"),
    SR_18(1000, "report.operation.chg.alterResult.1", SELECT, "isAlterDone"),
    SR_17(27, "form.search.column.countersigneds", SELECT, "countersigneds");
    
    private static final Map<String, SR> BY_INDEX = new HashMap<String, SR>();
    private static final Map<String, SR> BY_VONAME = new HashMap<String, SR>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (SR e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static SR valueOfNumber (String number) {
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

    private SR (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private SR (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private SR(int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private SR(int index, String name, String type, String voName, boolean isDefaultCol) {
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