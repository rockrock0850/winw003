package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 辦公室自動化科的會辦頁簽
 * @author adam.yeh
 */
public enum OA implements FormFields {
    
    COL2(2001, "form.search.column.job.userId", INPUT, "cUserId"),
    COL3(2002, "form.countersigned.form.description", INPUT, "cDescription"),
    COL1(2003, "form.countersigned.form.tct", DATE_RANGE, "cTct"),
    COL4(2004, "form.countersigned.form.sct", DATE_RANGE, "cSct"),
    COL5(2005, "form.countersigned.form.sit", DATE_RANGE, "cSit");
    
    private static final Map<String, OA> BY_INDEX = new HashMap<String, OA>();
    private static final Map<String, OA> BY_VONAME = new HashMap<String, OA>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (OA e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static OA valueOfNumber (String number) {
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

    private OA (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private OA (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private OA (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private OA (int index, String name, String type, String voName, boolean isDefaultCol) {
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