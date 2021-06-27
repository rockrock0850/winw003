package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 表單查詢-批次作業中斷對策表管理的欄位
 * @author Bernard.Yu
 */
public enum BA implements FormFields {

    DEFAULT_2(2, "form.search.column.formStatus", SELECT, "formStatus", true),
    DEFAULT_3(3, "form.search.column.createTime", DATE_RANGE, "createTime", true),
    
    COMMON_2(5, "form.search.column.userCreated", INPUT, "userCreated", true),
    COMMON_3(6, "form.search.column.divisionSolving", INPUT, "divisionSolving", true),
    COMMON_4(7, "form.search.column.userSolving", INPUT, "userSolving", true),

    BA_1(81, "form.search.column.ba.batch.name", INPUT, "batchName", true),
    BA_2(82, "form.search.column.ba.summary", INPUT, "summary", true),
    BA_3(83, "form.search.column.ba.execute.time", INPUT, "executeTime", true),
    BA_4(84, "form.search.column.ba.dbinuse", INPUT, "dbInUse", true),
    BA_5(85, "form.search.column.ba.effect.date", DATE_RANGE, "effectDate", true),
    BA_6(86, "form.search.column.ba.division", INPUT, "division", true);
    
    private static final Map<String, BA> BY_INDEX = new HashMap<String, BA>();
    private static final Map<String, BA> BY_VONAME = new HashMap<String, BA>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (BA e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static BA valueOfNumber (String number) {
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

    private BA (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private BA (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private BA(int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private BA(int index, String name, String type, String voName, boolean isDefaultCol) {
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