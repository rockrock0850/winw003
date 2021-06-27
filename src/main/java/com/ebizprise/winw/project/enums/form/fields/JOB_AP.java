package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

public enum JOB_AP implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus"),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime"),

    COMMON_1(227, "form.search.column.divisionCreated", INPUT, "divisionCreated", true),
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated", true),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving"),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),

    JOB_1(201, "form.search.column.sourceId", INPUT, "sourceId"),
    JOB_12(212, "form.search.column.job.ap.purpose", INPUT, "purpose", true),
    JOB_13(213, "form.search.column.job.ap.content", INPUT, "content"),
    JOB_4(204, "form.search.column.system", INPUT, "system"),
    JOB_2(202, "form.search.column.sClass", SELECT, "sClass"),
    JOB_3(203, "form.search.column.sSubClass", SELECT, "sSubClass"),
    JOB_5(205, "form.search.column.chg.changeType", SELECT, "changeType"),
    JOB_6(206, "form.search.column.chg.changeRank", SELECT, "changeRank"),
    JOB_7(207, "form.search.column.job.isHandleFirst", SELECT, "isHandleFirst"),
    JOB_8(208, "form.search.column.job.ap.isCorrect", SELECT, "isCorrect"),
    JOB_9(209, "form.search.column.job.ap.isAddFuntion", SELECT, "isAddFuntion"),
    JOB_10(210, "form.search.column.job.ap.isAddReport", SELECT, "isAddReport"),
    JOB_11(211, "form.search.column.job.ap.isAddFile", SELECT, "isAddFile"),
    JOB_14(236, "form.search.column.job.ap.isForward", SELECT, "isForward"),
    JOB_15(215, "form.search.column.job.ap.isWatching", SELECT, "isWatching"),
    JOB_16(216, "form.search.column.countersigneds", SELECT, "countersigneds"),
    JOB_17(217, "form.search.column.job.date.isPlaning", SELECT, "isPlaning"),
    JOB_18(218, "form.search.column.job.date.isUnPlaning", SELECT, "isUnPlaning"),
    JOB_19(219, "form.search.column.job.date.offLineTime", DATE_RANGE, "offLineTime"),
    JOB_20(220, "form.search.column.job.date.ist", DATE_RANGE, "ist"),
    JOB_21(221, "form.search.column.job.date.ict", DATE_RANGE, "ict"),
    JOB_22(222, "form.search.column.job.date.tct", DATE_RANGE, "tct"),
    JOB_23(223, "form.search.column.job.date.sct", DATE_RANGE, "sct", true),
    JOB_24(224, "form.search.column.job.date.sit", DATE_RANGE, "sit"),
    JOB_25(225, "form.search.column.chg.cat", DATE_RANGE, "cat"),
    JOB_26(226, "form.search.column.chg.cct", DATE_RANGE, "cct"),
    JOB_27(228, "form.search.column.job.ap.isProgramOnline", SELECT, "isProgramOnline"),
    JOB_28(255, "form.search.column.job.ap.isModifyProgram", SELECT, "isModifyProgram"),

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
    
    private static final Map<String, JOB_AP> BY_INDEX = new HashMap<String, JOB_AP>();
    private static final Map<String, JOB_AP> BY_VONAME = new HashMap<String, JOB_AP>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (JOB_AP e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static JOB_AP valueOfNumber (String number) {
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

    private JOB_AP (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }

    private JOB_AP (String name, String type, String voName) {
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private JOB_AP (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private JOB_AP (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private JOB_AP (int index, String name, String type, String voName, boolean isDefaultCol) {
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