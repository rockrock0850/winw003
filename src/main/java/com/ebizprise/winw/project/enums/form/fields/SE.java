package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 資安處理/資安規劃的會辦頁簽
 * @author adam.yeh
 */
public enum SE implements FormFields {
    
    COL2(2001, "form.search.column.job.userId", INPUT, "cUserId"),
    COL3(2002, "form.countersigned.form.description", INPUT, "cDescription"),
    COL6(2010, "form.countersigned.form.isRollback", SELECT, "cIsRollback"),
    COL7(2011, "form.countersigned.form.rollbackDesc", INPUT, "cRollbackDesc"),
    COL1(2003, "form.countersigned.form.tct", DATE_RANGE, "cTct"),
    COL4(2004, "form.countersigned.form.sct", DATE_RANGE, "cSct"),
    COL5(2005, "form.countersigned.form.sit", DATE_RANGE, "cSit");
    
    private static final Map<String, SE> BY_INDEX = new HashMap<String, SE>();
    private static final Map<String, SE> BY_VONAME = new HashMap<String, SE>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (SE e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static SE valueOfNumber (String number) {
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

    private SE (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private SE (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private SE (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private SE (int index, String name, String type, String voName, boolean isDefaultCol) {
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