package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 系統科的會辦頁簽
 * @author adam.yeh
 */
public enum SP implements FormFields {
    
    COL2(2006, "form.job.form.isTest", SELECT, "cIsTest"),
    COL3(2007, "form.job.form.isProduction", SELECT, "cIsProduction"),
    COL4(2008, "form.countersigned.form.description", INPUT, "cDescription"),
    COL5(2009, "form.countersigned.form.sct", DATE_RANGE, "cSct");
    
    private static final Map<String, SP> BY_INDEX = new HashMap<String, SP>();
    private static final Map<String, SP> BY_VONAME = new HashMap<String, SP>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (SP e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static SP valueOfNumber (String number) {
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

    private SP (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private SP (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private SP (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private SP (int index, String name, String type, String voName, boolean isDefaultCol) {
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