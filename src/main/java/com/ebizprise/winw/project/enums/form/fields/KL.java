package com.ebizprise.winw.project.enums.form.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * 知識庫
 * @author adam.yeh
 */
public enum KL implements FormFields {

    FORM_PROGRAM_1(801, "form.search.column.form.program.processProgram", INPUT, "processProgram", true),
    FORM_PROGRAM_2(802, "form.search.column.form.program.indication", INPUT, "indication", true),
    FORM_PROGRAM_3(803, "form.search.column.form.program.reason", INPUT, "reason", true),

    Q_2(29, "form.search.column.q.summary", INPUT, "summary", true),
    Q_12(39, "form.search.column.sClass", SELECT, "sClass"),
    Q_13(40, "form.search.column.sSubClass", SELECT, "sSubClass"),
    Q_14(41, "form.search.column.system", INPUT, "system"),
    Q_15(42, "form.search.column.assetGroup", INPUT, "assetGroup"),
    Q_16(43, "form.question.process.knowledge.4", DATE_RANGE, "createTime"),
    Q_17(44, "form.question.process.knowledge.5", DATE_RANGE, "updatedAt"),
    Q_22(49, "form.question.process.knowledge.6", INPUT, "knowledges", true),
    Q_23(50, "form.question.process.knowledge.7", INPUT, "solutions", true);
    
    private static final Map<String, KL> BY_INDEX = new HashMap<String, KL>();
    private static final Map<String, KL> BY_VONAME = new HashMap<String, KL>();

    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        for (KL e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    public static KL valueOfNumber (String number) {
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

    private KL (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private KL (int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }
    
    private KL (int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }
    
    private KL (int index, String name, String type, String voName, boolean isDefaultCol) {
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