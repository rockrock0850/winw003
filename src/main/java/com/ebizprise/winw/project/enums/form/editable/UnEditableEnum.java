package com.ebizprise.winw.project.enums.form.editable;

/**
 * 紀錄擬定階段的不可編輯欄位<br><br>
 * 
 * 欄位名稱與HTML的欄位屬性name對應<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
public enum UnEditableEnum implements BaseColEnum {

    Q(
        AST, 
        ACT,  
        IS_SPECIAL,
        REPORT_TIME,
        OBSERVATION, 
        SPECIAL_ENDCASETYPE
    ), 
    SR(
        ACT
    ), 
    INC(
        ECT
    ),
    CHG(
        CAT,
        PHONE,
        EMAIL,
        UNIT_ID,
        USER_NAME,
        UNIT_CATEGORY
    ),
    Q_C(
        USER_ID,
        UNIT_ID,
        SCLASS,
        USER_GROUP,
        SYSTEM,
        SYSTEM_ID,
        SPC_GROUPS,
        ASSET_GROUP,
        SYSTEM_BRAND,
        SYSTEM_DIALOG,
        ASSET_GROUP,
        COUNTERSIGNED_HANDLE,
        EOT,
        ECT,
        AST,
        ACT,
        MECT,
        ASSIGN_TIME
    ),
    SR_C(
        SYSTEM_DIALOG,
        ASSET_GROUP,
        USER_ID,
        UNIT_ID,
        SCLASS,
        SYSTEM,
        SYSTEM_ID,
        SPC_GROUPS,
        USER_GROUP,
        ASSET_GROUP,
        SYSTEM_BRAND,
        COUNTERSIGNED_HANDLE,
        MECT,
        ASSIGN_TIME
    ),
    INC_C(
        SYSTEM_DIALOG,
        ASSET_GROUP,
        USER_ID,
        UNIT_ID_2,
        SCLASS,
        SYSTEM_ID,
        SYSTEM,
        USER_GROUP,
        ASSET_GROUP,
        SYSTEM_BRAND,
        COUNTERSIGNED_HANDLE,
        AST,
        ACT,
        MECT
    ),
    JOB_AP(
        CAT,
        CCT,
        USER_ID
    ),
    JOB_SP(
        AST,
        ACT
    ),
    JOB_AP_C(),
    JOB_SP_C(
        IS_TEST,
        IS_PRODUCTION,
        IS_HANDLEFIRST,
        STATUS,
        ASTATUS,
        SYSTEM_2,
        USER_NAME,
        C_USER_ID,
        SPC_GROUPS,
        COUNTERSIGNEDS,
        EOT,
        ECT,
        AST,
        ACT,
        MECT
    ),
    BA(),
    KL();
    
    private String[] columns;
    
    private UnEditableEnum (String... columns) {
        this.columns = columns;
    }
    
    public String[] columns () {
        return this.columns;
    }
    
}
