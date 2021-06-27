package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

public enum JOB_SP implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime"),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated"),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving"),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving"),

    JOB_1(201, "form.search.column.sourceId", INPUT, "sourceId"),
    JOB_27(227, "form.job.form.summary", INPUT, "summary", true),
    JOB_12(229, "form.job.form.effectScope", INPUT, "effectScope"),
    JOB_13(213, "form.job.form.content", INPUT, "content"),
    JOB_4(230, "form.job.form.remark", INPUT, "remark"),
    JOB_28(231, "form.job.form.reset", INPUT, "reset"),
    JOB_2(232, "form.job.form.isReset", SELECT, "isReset"),
    JOB_3(233, "form.job.form.isTest", SELECT, "isTest"),
    JOB_5(234, "form.job.form.isProduction", SELECT, "isProduction"),
    JOB_7(235, "form.search.column.job.isHandleFirst", SELECT, "isHandleFirst"),
    JOB_6(236, "form.job.form.isForward", SELECT, "isForward"),
    JOB_8(237, "form.job.form.isInterrupt", SELECT, "isInterrupt"),
    JOB_9(238, "form.job.form.working", SELECT, "working", true),
    JOB_10(239, "form.search.column.cCategory", SELECT, "cCategory"),
    JOB_11(240, "form.search.column.cClass", SELECT, "cClass"),
    JOB_14(241, "form.search.column.cComponent", SELECT, "cComponent"),
    JOB_19(242, "form.job.form.eot", DATE_RANGE, "eot"),
    JOB_20(243, "form.job.form.ect", DATE_RANGE, "mect", true),
    JOB_22(244, "form.job.form.ast", DATE_RANGE, "ast"),
    JOB_16(216, "form.search.column.countersigneds", SELECT, "countersigneds"),
    JOB_23(245, "form.job.form.act", DATE_RANGE, "act"),

    COL42("form.search.column.job.userId", INPUT, "cUserId"),
    COL38("form.search.column.job.c.item", SELECT, "cType"),
    COL6("form.countersigned.form.book2", INPUT, "cBook"),
    COL7("form.countersigned.form.bookNumber", INPUT, "cBookNumber"),
    COL8("form.countersigned.form.onlyCode", INPUT, "cOnlyCode"),
    COL9("form.countersigned.form.only2", INPUT, "cOnly"),
    COL0("form.countersigned.form.onlyNumber", INPUT, "cOnlyNumber"),
    COL15("form.countersigned.form.linkCode", INPUT, "cLinkCode"),
    COL16("form.countersigned.form.linkNumber", INPUT, "cLinkNumber"),
    COL17("form.countersigned.form.link2", INPUT, "cLink"),
    COL10("form.countersigned.form.linkOnly2", INPUT, "cLinkOnly"),
    COL11("form.countersigned.form.linkOnlyNumber", INPUT, "cLinkOnlyNumber"),
    COL12("form.countersigned.form.other2", INPUT, "cOther"),
    COL13("form.countersigned.form.isRollback", SELECT, "cIsRollback"),
    COL14("form.countersigned.form.rollbackDesc", INPUT, "cRollbackDesc"),
    COL39("form.countersigned.form.other2", INPUT, "cbOther"),
    COL40("form.countersigned.form.isRollback", SELECT, "cbIsRollback"),
    COL19("form.countersigned.form.batch.1", INPUT, "cPsb"),
    COL20("form.countersigned.form.batch.6", INPUT, "cCljcl"),
    COL21("form.countersigned.form.batch.7", INPUT, "cJcl"),
    COL22("form.countersigned.form.batch.8", INPUT, "cProgramName"),
    COL23("form.countersigned.form.batch.10", INPUT, "cReason"),
    COL24("form.countersigned.form.batch.11", INPUT, "cContent"),
    COL25("form.countersigned.form.batch.12", INPUT, "cbRemark"),
    COL26("form.countersigned.form.it", DATE_RANGE, "cIt"),
    COL27("form.countersigned.form.batch.2", SELECT, "cIsChange"),
    COL28("form.countersigned.form.batch.3", SELECT, "cIsOtherDesc"),
    COL29("form.countersigned.form.batch.4", SELECT, "cIsHelp"),
    COL30("form.countersigned.form.batch.5", SELECT, "cIsAllow"),
    COL31("form.countersigned.form.rollbackDesc", SELECT, "cIsOther"),
    COL32("form.countersigned.form.batch.9", SELECT, "cIsHelpCl"),
    COL33("form.countersigned.form.batch.13", INPUT, "cDbName"),
    COL34("form.countersigned.form.batch.1", INPUT, "cPsbName"),
    COL35("form.countersigned.form.batch.15", INPUT, "cAlterWay"),
    COL36("form.countersigned.form.batch.16", DATE_RANGE, "cEit"),
    COL37("form.countersigned.form.batch.17", DATE_RANGE, "cAst"),
    COL1("form.countersigned.form.tct", DATE_RANGE, "cTct"),
    COL41("form.countersigned.form.sct", DATE_RANGE, "cSct"),
    COL5("form.countersigned.form.sit", DATE_RANGE, "cSit"),
    COL2("form.job.form.isTest", SELECT, "cIsTest"),
    COL3("form.job.form.isProduction", SELECT, "cIsProduction"),
    COL4("form.countersigned.form.description", INPUT, "cDescription");
    
    private static final Map<String, JOB_SP> BY_INDEX = new HashMap<String, JOB_SP>();
    private static final Map<String, JOB_SP> BY_VONAME = new HashMap<String, JOB_SP>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (JOB_SP e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static JOB_SP valueOfNumber (String number) {
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

    private JOB_SP (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }

    private JOB_SP (String name, String type, String voName) {
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private JOB_SP (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private JOB_SP (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private JOB_SP (int index, String name, String type, String voName, boolean isDefaultCol) {
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