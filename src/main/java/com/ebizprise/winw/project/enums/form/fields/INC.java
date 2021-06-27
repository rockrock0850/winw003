package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-事件單的欄位
 * @author adam.yeh
 */
public enum INC implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime"),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated", true),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),
    
    FORM_PROGRAM_1(801, "form.search.column.form.program.processProgram", INPUT, "processProgram"),
    FORM_PROGRAM_2(802, "form.search.column.form.program.indication", INPUT, "indication"),
    FORM_PROGRAM_3(803, "form.search.column.form.program.reason", INPUT, "reason"),

    INC_1(49, "form.search.column.inc.summary", INPUT, "summary", true),
    INC_2(50, "form.search.column.inc.content", INPUT, "content"),
    INC_3(51, "form.search.column.inc.eventClass", SELECT, "eventClass"),
    INC_4(52, "form.search.column.inc.eventType", INPUT, "eventType"),
    INC_5(53, "form.search.column.inc.eventSecurity", SELECT, "eventSecurity"),
    INC_6(54, "form.search.column.inc.eventPriority", INPUT, "eventPriority"),
    INC_7(55, "form.search.column.inc.isMainEvent", SELECT, "isMainEvent"),
    INC_8(56, "form.search.column.q.main.event", INPUT, "mainEvent"),
    INC_9(57, "form.search.column.inc.isInterrupt", SELECT, "isInterrupt"),
    INC_10(59, "form.search.column.inc.infoDateCreateTime", DATE_RANGE, "infoDateCreateTime"),
    INC_11(60, "form.search.column.inc.act", DATE_RANGE, "act"),
    INC_12(61, "form.search.column.inc.excludeTime", DATE_RANGE, "excludeTime"),
    INC_13(62, "form.search.column.inc.ect", DATE_RANGE, "ect", true),
    INC_14(63, "form.search.column.sClass", SELECT, "sClass"),
    INC_15(64, "form.search.column.sSubClass", SELECT, "sSubClass"),
    INC_16(65, "form.search.column.system", INPUT, "system"),
    INC_17(66, "form.search.column.assetGroup", INPUT, "assetGroup"),
    INC_18(67, "form.search.column.cCategory", SELECT, "cCategory"),
    INC_19(68, "form.search.column.cClass", SELECT, "cClass"),
    INC_20(69, "form.search.column.cComponent", SELECT, "cComponent"),
    INC_21(70, "form.search.column.effectScope", SELECT, "effectScope"),
    INC_22(71, "form.search.column.urgentLevel", SELECT, "urgentLevel"),
    INC_23(72, "form.search.column.countersigneds", SELECT, "countersigneds"),
    INC_25(1000, "report.operation.chg.alterResult.1", SELECT, "isAlterDone"),
    INC_24(73, "form.search.column.q.isIVR", SELECT, "isIVR"),
    INC_26(1001, "form.search.column.inc.isSameInc", SELECT, "isSameInc"),
    INC_27(1002, "form.search.column.inc.isOnlineFail", SELECT, "isOnlineFail"),
    INC_28(1003, "form.search.column.inc.onlineTime", DATE_RANGE, "onlineTime"),
    INC_29(1004, "form.search.column.inc.onlineJobFormId", INPUT, "onlineJobFormId");
    
    
    private static final Map<String, INC> BY_INDEX = new HashMap<String, INC>();
    private static final Map<String, INC> BY_VONAME = new HashMap<String, INC>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (INC e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static INC valueOfNumber (String number) {
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

    private INC (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private INC (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private INC(int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private INC(int index, String name, String type, String voName, boolean isDefaultCol) {
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