package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

public enum JOB_SP_C implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated"),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving"),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving"),

    COL2(2001, "form.job.form.cuserId", INPUT, "cUserId", true),
    JOB_1(201, "form.search.column.sourceId", INPUT, "sourceId"),
    COL3(212, "form.search.column.job.ap.purpose", INPUT, "purpose"),
    JOB_27(227, "form.job.form.summary", INPUT, "summary", true),
    JOB_13(213, "form.job.form.content", INPUT, "content"),
    JOB_4(230, "form.job.form.remark", INPUT, "remark"),
    JOB_3(233, "form.job.form.isTest", SELECT, "isTest"),
    JOB_5(234, "form.job.form.isProduction", SELECT, "isProduction"),
    JOB_7(235, "form.search.column.job.isHandleFirst", SELECT, "isHandleFirst"),
    COL4(18, "form.search.column.system", INPUT, "system"),
    COL6(20, "form.job.form.astatus", INPUT, "aStatus"),
    JOB_20(243, "form.job.form.mect", DATE_RANGE, "mect"),
    JOB_19(242, "form.job.form.eot", DATE_RANGE, "eot"),
    JOB_24(246, "form.job.form.ect", DATE_RANGE, "ect", true),
    JOB_22(244, "form.job.form.ast", DATE_RANGE, "ast"),
    JOB_23(245, "form.job.form.act", DATE_RANGE, "act");
    
    private static final Map<String, JOB_SP_C> BY_INDEX = new HashMap<String, JOB_SP_C>();
    private static final Map<String, JOB_SP_C> BY_VONAME = new HashMap<String, JOB_SP_C>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (JOB_SP_C e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static JOB_SP_C valueOfNumber (String number) {
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

    private JOB_SP_C (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private JOB_SP_C (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private JOB_SP_C (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private JOB_SP_C (int index, String name, String type, String voName, boolean isDefaultCol) {
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