package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-問題單的欄位
 * @author adam.yeh
 */
public enum Q implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated", true),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),
    
    FORM_PROGRAM_1(801, "form.search.column.form.program.processProgram", INPUT, "processProgram"),
    FORM_PROGRAM_2(802, "form.search.column.form.program.indication", INPUT, "indication"),
    FORM_PROGRAM_3(803, "form.search.column.form.program.reason", INPUT, "reason"),
    
    Q_1(28, "form.search.column.q.questionId", SELECT, "questionId"),
    Q_2(29, "form.search.column.q.summary", INPUT, "summary", true),
    Q_3(30, "form.search.column.q.content", INPUT, "content"),
    Q_4(31, "form.search.column.q.questionPriority", INPUT, "questionPriority"),
    Q_5(32, "form.search.column.q.isSpecial", SELECT, "isSpecial"),
    Q_6(33, "form.search.column.q.specialEndCaseType", SELECT, "specialEndCaseType"),
    Q_7(34, "form.search.column.q.reportTime", DATE_RANGE, "reportTime", true),
    Q_8(35, "form.search.column.q.ect", DATE_RANGE, "ect", true),
    Q_9(36, "form.search.column.q.ast", DATE_RANGE, "ast"),
    Q_10(37, "form.search.column.q.act", DATE_RANGE, "act"),
    Q_11(38, "form.search.column.q.observation", DATE_RANGE, "observation"),
    Q_12(39, "form.search.column.sClass", SELECT, "sClass"),
    Q_13(40, "form.search.column.sSubClass", SELECT, "sSubClass"),
    Q_14(41, "form.search.column.system", INPUT, "system"),
    Q_15(42, "form.search.column.assetGroup", INPUT, "assetGroup"),
    Q_16(43, "form.search.column.cCategory", SELECT, "cCategory"),
    Q_17(44, "form.search.column.cClass", SELECT, "cClass"),
    Q_18(45, "form.search.column.cComponent", SELECT, "cComponent"),
    Q_19(46, "form.search.column.effectScope", SELECT, "effectScope"),
    Q_20(47, "form.search.column.urgentLevel", SELECT, "urgentLevel"),
    Q_22(1000, "report.operation.chg.alterResult.1", SELECT, "isAlterDone"),
    Q_21(48, "form.search.column.countersigneds", SELECT, "countersigneds"),
    Q_23(1005, "form.search.column.sourceId", INPUT, "sourceId");

    private static final Map<String, Q> BY_INDEX = new HashMap<String, Q>();
    private static final Map<String, Q> BY_VONAME = new HashMap<String, Q>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (Q e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static Q valueOfNumber (String number) {
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

    private Q (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private Q (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private Q (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private Q (int index, String name, String type, String voName, boolean isDefaultCol) {
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