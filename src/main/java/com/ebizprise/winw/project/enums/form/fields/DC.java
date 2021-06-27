package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * DC科與底下五個可會辦項目
 * @author adam.yeh
 */
public enum DC implements FormFields {
    
    // 查詢會辦項目的下拉選單
    COL2(2011, "form.search.column.job.c.item", SELECT, "cType"),
    
    // ONLINE, OPEN, BATCH
    COL1(2012, "form.search.column.job.userId", INPUT, "cUserId"),
    COL3(2013, "form.countersigned.form.tct", DATE_RANGE, "cTct"),
    COL4(2014, "form.countersigned.form.sct", DATE_RANGE, "cSct"),
    COL5(2015, "form.countersigned.form.sit", DATE_RANGE, "cSit"),
    COL6(2016, "form.countersigned.form.book2", INPUT, "cBook"),
    COL7(2017, "form.countersigned.form.bookNumber", INPUT, "cBookNumber"),
    COL8(2018, "form.countersigned.form.onlyCode", INPUT, "cOnlyCode"),
    COL9(2019, "form.countersigned.form.only2", INPUT, "cOnly"),
    COL0(2020, "form.countersigned.form.onlyNumber", INPUT, "cOnlyNumber"),
    COL15(2021, "form.countersigned.form.linkCode", INPUT, "cLinkCode"),
    COL16(2022, "form.countersigned.form.linkNumber", INPUT, "cLinkNumber"),
    COL17(2023, "form.countersigned.form.link2", INPUT, "cLink"),
    COL10(2024, "form.countersigned.form.linkOnly2", INPUT, "cLinkOnly"),
    COL11(2025, "form.countersigned.form.linkOnlyNumber", INPUT, "cLinkOnlyNumber"),
    COL12(2026, "form.countersigned.form.other2", INPUT, "cOther"),
    COL13(2027, "form.countersigned.form.isRollback", SELECT, "cIsRollback"),
    COL14(2028, "form.countersigned.form.rollbackDesc", INPUT, "cRollbackDesc"),
    COL18(2029, "form.countersigned.form.description", INPUT, "cDescription"),

    // 批次
    COL38(2049, "form.search.column.job.userId", INPUT, "cbUserId"),
    COL39(2050, "form.countersigned.form.other2", INPUT, "cbOther"),
    COL40(2051, "form.countersigned.form.isRollback", SELECT, "cbIsRollback"),
    COL19(2030, "form.countersigned.form.batch.1", INPUT, "cPsb"),
    COL20(2031, "form.countersigned.form.batch.6", INPUT, "cCljcl"),
    COL21(2032, "form.countersigned.form.batch.7", INPUT, "cJcl"),
    COL22(2033, "form.countersigned.form.batch.8", INPUT, "cProgramName"),
    COL23(2034, "form.countersigned.form.batch.10", INPUT, "cReason"),
    COL24(2035, "form.countersigned.form.batch.11", INPUT, "cContent"),
    COL25(2036, "form.countersigned.form.batch.12", INPUT, "cbRemark"),
    COL26(2037, "form.countersigned.form.it", DATE_RANGE, "cIt"),
    COL27(2038, "form.countersigned.form.batch.2", SELECT, "cIsChange"),
    COL28(2039, "form.countersigned.form.batch.3", SELECT, "cIsOtherDesc"),
    COL29(2040, "form.countersigned.form.batch.4", SELECT, "cIsHelp"),
    COL30(2041, "form.countersigned.form.batch.5", SELECT, "cIsAllow"),
    COL31(2042, "form.countersigned.form.rollbackDesc", SELECT, "cIsOther"),
    COL32(2043, "form.countersigned.form.batch.9", SELECT, "cIsHelpCl"),
    
    // DB變更
    COL33(2044, "form.countersigned.form.batch.13", INPUT, "cDbName"),
    COL34(2045, "form.countersigned.form.batch.1", INPUT, "cPsbName"),
    COL35(2046, "form.countersigned.form.batch.15", INPUT, "cAlterWay"),
    COL36(2047, "form.countersigned.form.batch.16", DATE_RANGE, "cEit"),
    COL37(2048, "form.countersigned.form.batch.17", DATE_RANGE, "cAst");
    
    private static final Map<String, DC> BY_INDEX = new HashMap<String, DC>();
    private static final Map<String, DC> BY_VONAME = new HashMap<String, DC>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (DC e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static DC valueOfNumber (String number) {
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

    private DC (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private DC (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private DC (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private DC (int index, String name, String type, String voName, boolean isDefaultCol) {
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