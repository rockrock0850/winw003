package com.ebizprise.winw.project.enums.form.fields;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.enums.form.Form;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;

/**
 * 定義表單查詢左半邊的搜尋欄位種類
 * 
 * @author adam.yeh
 */
public enum FormFieldsEnum implements FormFields {

    DEFAULT_1(1, "form.search.column.formId", INPUT, "formId", true),

    COMMON_1(4, "form.search.column.divisionCreated", INPUT, "divisionCreated"),
    COMMON_5(8, "form.search.column.unitCategory", SELECT, "unitCategory"),
    COMMON_6(9, "form.search.column.unitId", INPUT, "unitId"),
    COMMON_7(10, "form.search.column.userName", INPUT, "userName"),

    FORM_PROGRAM_4(804, "form.search.column.form.program.isSuggestCase", SELECT, "isSuggestCase"),
    FORM_PROGRAM_5(805, "form.search.column.form.program.isForward", SELECT, "isSuggestCase"),
    
    OTHER_PAGE_1(901, "form.search.column.formUserRecord", INPUT, "formUserRecord"),
    OTHER_PAGE_2(902, "form.search.column.associationForm", INPUT, "associationForm"),
    OTHER_PAGE_3(903, "form.search.column.formFile", INPUT, "formFile"),
    OTHER_PAGE_4(904, "form.search.column.form.impact.analysis.evaluation", INPUT, "evaluation"),
    OTHER_PAGE_5(905, "form.search.column.form.impact.analysis.solution", INPUT, "solution"),
    OTHER_PAGE_6(906, "form.search.column.form.impact.analysis.total", INPUT, "total");
                 
    private static Map<Form, Class<?>> fieldsMap = new HashMap<>();
    private static Map<String, FormFieldsEnum> BY_INDEX = new HashMap<String, FormFieldsEnum>();
    private static Map<String, FormFieldsEnum> BY_VONAME = new HashMap<String, FormFieldsEnum>();
    
    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private boolean isDefaultCol;
    
    static {
        fieldsMap.put(FormEnum.Q, Q.class);
        fieldsMap.put(FormEnum.SR, SR.class);
        fieldsMap.put(FormEnum.INC, INC.class);
        fieldsMap.put(FormEnum.Q_C, C.class);
        fieldsMap.put(FormEnum.SR_C, C.class);
        fieldsMap.put(FormEnum.INC_C, C.class);
        fieldsMap.put(FormEnum.CHG, CHG.class);
        fieldsMap.put(FormEnum.JOB_AP, JOB_AP.class);
        fieldsMap.put(FormEnum.JOB_SP, JOB_SP.class);
        fieldsMap.put(FormEnum.JOB_AP_C, JOB_AP_C.class);
        fieldsMap.put(FormEnum.JOB_SP_C, JOB_SP_C.class);
        fieldsMap.put(FormEnum.KL, KL.class);
        fieldsMap.put(FormEnum.BA, BA.class);
        fieldsMap.put(FormJobEnum.AP1, AP.class);
        fieldsMap.put(FormJobEnum.AP2, AP.class);
        fieldsMap.put(FormJobEnum.AP3, AP.class);
        fieldsMap.put(FormJobEnum.AP4, AP.class);
        fieldsMap.put(FormJobEnum.SP, SP.class);
        fieldsMap.put(FormJobEnum.PT, PT.class);
        fieldsMap.put(FormJobEnum.EA, EA.class);
        fieldsMap.put(FormJobEnum.OA, OA.class);
        fieldsMap.put(FormJobEnum.PLAN, SE.class);
        fieldsMap.put(FormJobEnum.MGMT, SE.class);
        fieldsMap.put(FormJobEnum.DC, DC.class);
        fieldsMap.put(FormJobEnum.DC1, DC.class);
        fieldsMap.put(FormJobEnum.DC2, DC.class);
        fieldsMap.put(FormJobEnum.DC3, DC.class);
        fieldsMap.put(FormJobEnum.DB, DC.class);
        fieldsMap.put(FormJobEnum.BATCH, DC.class);

        for (FormFieldsEnum e : values()) {
            BY_INDEX.put(String.valueOf(e.index), e);
            BY_VONAME.put(String.valueOf(e.voName), e);
        }
    }
    
    /**
     * 取得表單的進階搜尋欄位
     * 
     * @param list
     * @param clazz
     * @return
     */
    public static String advance (List<String> list, Form clazz) {
        ArrayList<FormFields> fields = new ArrayList<>();

        Method isContaining, valueOfNumber;
        Class<?> field = fieldsMap.get(clazz);
        
        for (String number : list) {
            if (BY_INDEX.containsKey(number)) {
                fields.add(BY_INDEX.get(number));
            }
            
            try {
                isContaining = field.getMethod("isContaining", String.class);
                valueOfNumber = field.getMethod("valueOfNumber", String.class);
                
                if ((boolean) isContaining.invoke(field, number)) {
                    fields.add((FormFields) valueOfNumber.invoke(field, number));
                }
            } catch (Exception e) {
                fields.clear();
                e.printStackTrace();
                
                break;
            }
        }
        
        return BeanUtil.toJson(fields);
    }
    
    /**
     * 取得欄位訊息(name, type)
     * 
     * @param voName
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getColumnInfo(String voName, FormEnum clazz) {
        Map<String, String> columnInfo = new HashMap<>();
        String i18nKey = "";
        String type = "";
        Method valueOfVoName, existVoName;
        Class<?> field = fieldsMap.get(clazz);

        if (BY_VONAME.containsKey(voName)) {
            i18nKey = BY_VONAME.get(voName).getName();
            type = BY_VONAME.get(voName).getType();
        }

        try {
            existVoName = field.getMethod("existVoName", String.class);
            valueOfVoName = field.getMethod("valueOfVoName", String.class);

            if ((boolean) existVoName.invoke(field, voName)) {
                Map<String, String> map = (Map<String, String>) valueOfVoName.invoke(field, voName);
                i18nKey = map.get("name");
                type = map.get("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        columnInfo.put("i18nKey", i18nKey);
        columnInfo.put("type", type);

        return columnInfo;
    }

    public static String valueOfVoName(String voName) {
        return BY_VONAME.get(voName).getName();
    }

    public static boolean existVoName(String voName) {
        return BY_VONAME.containsKey(voName);
    }

    private FormFieldsEnum (int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }
    
    private FormFieldsEnum(int index, String name, String type, int length) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    private FormFieldsEnum(int index, String name, String type, String voName) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.voName = voName;
    }

    private FormFieldsEnum(int index, String name, String type, String voName, boolean isDefaultCol) {
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