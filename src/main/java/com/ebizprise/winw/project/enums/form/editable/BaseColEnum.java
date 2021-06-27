package com.ebizprise.winw.project.enums.form.editable;

/**
 * 系統表單HTML欄位
 * 
 * @author adam.yeh
 */
public interface BaseColEnum {

    /** 審核頁簽所有欄位 */
    public String ALL_VERIFY_COLS = "form#verifyForm textarea";
    /** 處理方案頁簽所有欄位 */
    public String ALL_PROGRAM_COLS = "form#programForm input,form#programForm textarea,form#programForm select";
    /** 衝擊分析頁簽所有欄位 */
    public String ALL_IMPACT_COLS = "form#impactForm textarea,form#impactForm button";
    /** 表單資訊頁簽所有欄位 */
    public String ALL_INFO_COLS = "form#infoForm input,form#infoForm textarea,form#infoForm select,form#infoForm button";
    /** 工作會辦單科別頁簽所有欄位 */
    public String ALL_JOB_C_COLS =
            "form#tabBtForm input,form#tabBtForm textarea," +
            "form#tabOaForm input,form#tabOaForm textarea," +
            "form#tabEaForm input,form#tabEaForm textarea," +
            "form#tabPtForm input,form#tabPtForm textarea," +
            "form#tabSpForm input,form#tabSpForm textarea," +
            "form#tabPm1Form input,form#tabPm1Form textarea," +
            "form#tabPm2Form input,form#tabPm2Form textarea," +
            "form#tabDc1Form input,form#tabDc1Form textarea," +
            "form#tabDc2Form input,form#tabDc2Form textarea," +
            "form#tabDc3Form input,form#tabDc3Form textarea," +
            "form#tabAp1Form input,form#tabAp1Form textarea," +
            "form#tabAp2Form input,form#tabAp2Form textarea," +
            "form#tabAp3Form input,form#tabAp3Form textarea," +
            "form#tabAp4Form input,form#tabAp4Form textarea," +
            "form#tabDbAlterForm input,form#tabDbAlterForm textarea,form#tabDbAlterForm button," +
            "table#libraryTables button";

    /** 會辦頁簽-批次 */
    public String JOB_C_BATCH = "form#tabBtForm input,form#tabBtForm textarea";
    /** 會辦頁簽-OPEN */
    public String JOB_C_DC3_OPEN = "form#tabDc3Form input,form#tabDc3Form textarea";
    /** 會辦頁簽-BATCH */
    public String JOB_C_DC2_BATCH = "form#tabDc2Form input,form#tabDc2Form textarea";
    /** 會辦頁簽-ONLINE */
    public String JOB_C_DC1_ONLINE = "form#tabDc1Form input,form#tabDc1Form textarea";
    /** 會辦頁簽-DB變更 */
    public String JOB_C_DB = "form#tabDbAlterForm input,form#tabDbAlterForm textarea,form#tabDbAlterForm button";

    /** 預計上線時間( Estimate Online Time ) */
    public String EOT = "input[name='eot']";
    /** 預計完成時間( Estimate Complete Time )、目標解決時間、預計變更結束時間 */
    public String ECT = "input[name='ect']";
    /** 實際開始時間( Actual Start Time ) */
    public String AST = "input[name='ast']";
    /** 實際完成時間( Actual Complete Time )、事件完成時間 */
    public String ACT = "input[name='act']";
    /** 變更申請時間 */
    public String CAT = "input[name='cat']";
    /** 預計變更結束時間 */
    public String CCT = "input[name='cct']";
    /** 中斷起始時間 */
    public String IST = "input[name='ist']";
    /** 中斷結束時間 */
    public String ICT = "input[name='ict']";
    /** 測試系統完成時間 */
    public String TCT = "input[name='tct']";
    /** 連線系統完成日期 */
    public String SCT = "input[name='sct']";
    /** 連線系統實施日期 */
    public String SIT = "input[name='sit']";
    /** 主單預計完成時間 */
    public String MECT = "input[name='mect']";
    /** Compile Only Return_Code */
    public String ONLY_CODE = "input[name='onlyCode']";
    /** Compile Link Return_Code */
    public String LINK_CODE = "input[name='linkCode']";
    /** Book 程式支數 */
    public String BOOK_NUMBER = "input[name='bookNumber']";
    /** Compile Link 程式支數 */
    public String LINK_NUMBER = "input[name='linkNumber']";
    /** Compile Only 程式支數 */
    public String ONLY_NUMBER = "input[name='onlyNumber']";
    /** 程式支數 */
    public String LINKONLY_NUMBER = "input[name='linkOnlyNumber']";
    /** 摘要 */
    public String SUMMARY = "input[name='summary']";
    /** 變更類型 */
    public String CHANGE_TYPE = "input[name='changeType']";
    /** 變更等級 */
    public String CHANGE_RANK = "input[name='changeRank']";
    /** 電話 */
    public String PHONE = "input[name='phone']";
    /** 郵件地址 */
    public String EMAIL = "input[name='email']";
    /** 系統狀態 */
    public String STATUS = "input[name='status']";
    /** 審核人員、提出人員編號、(表單資訊)系統負責人 */
    public String USER_ID = "input[name='userId']";
    /** 提出人員單位、負責單位 */
    public String UNIT_ID = "input[name='unitId']";
    /** 系統名稱 */
    public String SYSTEM_2 = "input[name='system']";
    /** 核准狀態 */
    public String ASTATUS = "input[name='astatus']";
    /** 會辦處理人員 */
    public String C_USER_ID = "input[name='cuserId']";
    /** 系統編號 */
    public String SYSTEM_ID = "input[name='systemId']";
    /** 審核人員名稱、提出人員名稱、(表單資訊)系統負責人名稱 */
    public String USER_NAME = "input[name='userName']";
    /** 事件類型 */
    public String EVENT_TYPE = "input[name='eventType']";
    /** 處理人員群組 */
    public String USER_GROUP = "input[name='userGroup']";
    /** 報告日期 */
    public String REPORT_TIME = "input[name='reportTime']";
    /** 事件發生時間、變更申請時間、表單分派時間 */
    public String ASSIGN_TIME = "input[name='assignTime']";
    /** 公告恢復時間 */
    public String ONLINE_TIME = "input[name='onLineTime']";
    /** 問題單觀察期 */
    public String OBSERVATION = "input[name='observation']";
    /** 影響範圍 */
    public String EFFECT_SCOPE = "input[name='effectScope']";
    /** 影響範圍 */
    public String EFFECT_SCOPE_2 = "textarea[name='effectScope']";
    /** 事件(當下)排除時間 */
    public String EXCLUDE_TIME = "input[name='excludeTime']";
    /** 系統名稱識別碼( SystemId+_+Dpartment+MboName+Opinc+Apinc+Limit ) */
    public String SYSTEM_BRAND = "input[name='systemBrand']";
    /** 需求等級 */
    public String REQUIRE_RANK = "input[name='requireRank']";
    /** 公告停機時間 */
    public String OFFLINE_TIME = "input[name='offLineTime']";
    /** 緊急程度 */
    public String URGENT_LEVEL = "input[name='urgentLevel']";
    /** 事件優先順序 */
    public String EVENT_PRIORITY = "input[name='eventPriority']";
    /** 問題優先順序 */
    public String QUESTIONP_RIORITY = "input[name='questionPriority']";
    /** 生效日期 */
    public String EFFECT_DATE = "input[name='effectDate']";

    /** 事件來源為IVR */
    public String IS_IVR = "input[name='isIVR']";
    /** TEST */
    public String IS_TEST = "input[name='isTest']";
    /** 回復原設定 */
    public String IS_RESET = "input[name='isReset']";
    /** 緊急變更 */
    public String IS_URGENT = "input[name='isUrgent']";
    /** 併入主要事件單 */
    public String MAINEVENT = "input[name='mainEvent']";
    /** 特殊結案 */
    public String IS_SPECIAL = "input[name='isSpecial']";
    /** 上線修正 */
    public String IS_CORRECT = "input[name='isCorrect']";
    /** 新增檔案 */
    public String IS_ADDFILE = "input[name='isAddFile']";
    /** 程式上線 */
    public String IS_PROGRAM_ONLINE = "input[name='isProgramOnline']";
    /** 未有修改程式 */
    public String IS_MODIFY_PROGRAM = "input[name='isModifyProgram']";
    /** 會造成服務中斷或需要停機，屬於計劃性系統維護 */
    public String IS_PLANING = "input[name='isPlaning']";
    /** 是否需送交組態維護人員 */
    public String IS_FORWARD = "input[name='isForward']";
    /** 回復到前一版 */
    public String IS_ROLLBACK = "input[name='isRollback']";
    /** 是新系統 */
    public String IS_NEWSYSTEM = "input[name='isNewSystem']";
    /** 會造成服務中斷或需要停機，屬於非計劃性系統維護 */
    public String IS_UNPLANING = "input[name='isUnPlaning']";
    /** 設為主要事件單 */
    public String IS_MAINEVENT = "input[name='isMainEvent']";
    /** 全部功能服務中斷 */
    public String IS_INTERRUPT = "input[name='isInterrupt']";
    /** 新增報表 */
    public String IS_ADDREPORT = "input[name='isAddReport']";
    /** 新服務暨重大服務 */
    public String IS_NEWSERVICE = "input[name='isNewService']";
    /** 新增系統功能 */
    public String IS_ADDFUNTION = "input[name='isAddFuntion']";
    /** PRODUCTION */
    public String IS_PRODUCTION = "input[name='isProduction']";
    /** 是否建議加入處理方案、暫時性解決方案，且無法於事件目標解決時間內根本解決者 */
    public String IS_SUGGESTCASE = "input[name='isSuggestCase']";
    /** 有新增異動欄位影響到資料倉儲系統產出資料 */
    public String IS_EFFECTFIELD = "input[name='isEffectField']";
    /** 未有修改程式 */
    public String IS_MODIFYPROGRAM = "input[name='isModifyProgram']";
    /** 先處理後呈閱 */
    public String IS_HANDLEFIRST = "input[name='isHandleFirst']";
    /** 負責科文字顯示欄位 */
    public String DIVISION_WORDING = "input[id='divisionWording']";
    /** 有新增異動會計科目影響到資料倉儲系統產出資料 */
    public String IS_EFFECTACCOUNTANT = "input[name='isEffectAccountant']";

    /** Book */
    public String BOOK = "textarea[name='book']";
    /** Compile Only */
    public String ONLY = "textarea[name='only']";
    /** Compile Link */
    public String LINK = "textarea[name='link']";
    /** 其他事項 */
    public String OTHER = "textarea[name='other']";
    /** 其他回復作業 */
    public String RESET = "textarea[name='reset']";
    /** 原因、(批次頁籤)申請原因 */
    public String REASON = "textarea[name='reason']";
    /** 備註 */
    public String REMARK = "textarea[name='remark']";
    /** 系統名稱 */
    public String SYSTEM = "textarea[name='system']";
    /** 內容、(批次頁籤)資管科工作內容 */
    public String CONTENT = "textarea[name='content']";
    /** 作業目的 */
    public String PURPOSE = "textarea[name='purpose']";
    /** 因應措施 */
    public String SOLUTION = "textarea[name='solution']";
    /** Link Only */
    public String LINK_ONLY = "textarea[name='linkOnly']";
    /** 組態元件 */
    public String CCOMPONENT = "textarea[name='cComponent']";
    /** 影響評估 */
    public String EVALUATION = "textarea[name='evaluation']";
    /** 征兆 */
    public String INDICATION = "textarea[name='indication']";
    /** 資訊資產群組 */
    public String ASSET_GROUP = "textarea[name='assetGroup']";
    /** 工作說明 */
    public String DESCRIPTION = "textarea[name='description']";
    /** 變更影響系統   */
    public String EFFECT_SYSTEM = "textarea[name='effectSystem']";
    /** 其他 */
    public String ROLLBACK_DESC = "textarea[name='rollbackDesc']";
    /** 審核意見 */
    public String VERIFY_COMMENT = "textarea[name='verifyComment']";
    /** 工作會辦處理情形 */
    public String COUNTERSIGNEDS = "textarea[name='countersigneds']";
    /** 處理方案 */
    public String PROCESS_PROGRAM = "textarea[name='processProgram']";
    /** 暫時性解決方案 */
    public String TEMPORARY = "textarea[name='temporary']";
    /** 緊急程度 */
    public String URGENT_LEVEL_WORDING = "textarea[name='urgentLevelWording']";
    /** 影響範圍 */
    public String EFFECT_SCOPE_WORDING = "textarea[name='effectScopeWording']";
    /** 會辦科處理情形 */
    public String COUNTERSIGNED_HANDLE = "textarea[name='countersignedHandle']";
    /** 事件優先順序 */
    public String EVENTP_RIORITY_WORDING = "textarea[name='eventPriorityWording']";
    /** 問題優先順序 */
    public String QUESTION_PRIORITY_WORDING = "textarea[name='questionPriorityWording']";
    /** 批次工作名稱 */
    public String BATCH_NAME_WORDING = "textarea[name='batchName']";
    /** 作業名稱描述 */
    public String SUMMARY_WORDING = "textarea[name='summary']";
    /** 執行時間 */
    public String EXECUTE_TIME_WORDING = "textarea[name='executeTime']";
    /** 使用資料庫 */
    public String DB_IN_USE_WORDING = "textarea[name='dbInUse']";
    /** 會辦機房內容 */
    public String COUNTERSIGNED_IDC = "textarea[name='countersignedIDC']";

    /** 服務類別 */
    public String SCLASS = "select[name='sClass']";
    /** 知識庫原因類別 */
    public String KNOWLEDGE_1 = "select[name='knowledge1']";
    /** 知識庫原因子類別 */
    public String KNOWLEDGE_2 = "select[name='knowledge2']";
    /** 組態元件類別 */
    public String CCLASS = "select[name='cClass']";
    /** 工作組別 */
    public String WORKING = "select[name='working']";
    /** 提出人員單位、負責單位 */
    public String UNIT_ID_2 = "select[name='unitId']";
    /** 標準變更作業 */
    public String STANDARD = "select[name='standard']";
    /** 負責科 */
    public String DIVISION = "select[name='division']";
    /** 組態分類 */
    public String CCATEGORY = "select[name='cCategory']";
    /** 服務子類別 */
    public String SSUBCLASS = "select[name='sSubClass']";
    /** 事件主類別 */
    public String EVENT_CLASS = "select[name='eventClass']";
    /** 問題來源編號 */
    public String QUESTION_ID = "select[name='questionId']";
    /** 處理人員 */
    public String USER_SOLVING = "select[name='userSolving']";
    /** 開單人員 */
    public String USER_CREATED = "select[name='userCreated']";
    /** 提出單位分類 */
    public String UNIT_CATEGORY = "select[name='unitCategory']";
    /** 資安事件 */
    public String EVENT_SECURITY = "select[name='eventSecurity']";
    /** 特殊結案狀態 */
    public String SPECIAL_ENDCASETYPE = "select[name='specialEndCaseType']";

    /** 搜尋系統名稱的按鈕 */
    public String SYSTEM_DIALOG = "button#systemDialog";
    /** 組態元件 */
    public String CCOMPONENT_DIALOG = "button#cComponentDialog";

    /** 會辦科模組 */
    public String CLIST = "table#cList input";
    /** 會辦系統科群組 */
    public String SPC_GROUPS = "table#spcGroupTable input,table#spcGroupTable button";

    /** 併入主要事件單後面的icon */
    public String INCFORM_DAILOG = "i#incFormDailog";
    
    /** 參照知識庫 */
    public String KNOWLEDGE_DIALOG = "button#knowledgeDialog";
    /** 清除知識庫 */
    public String KNOWLEDGE_DELETE = "button#knowledgeDelete";

    /** 作業關卡清單裡面的按鈕 */
    public String PERSON = "#checkPersonForm table#table button";
    /** 工作要項內的所有按鈕 */
    public String WORK_ITEM = "td#workingItemModel input,button";

    /** 影響範圍按鈕 */
    public String EFFECT_SCOPE_BTN = "button#effectScopeBtn";
    /** 緊急程度按鈕 */
    public String URGENT_LEVEL_BTN = "button#urgentLevelBtn";
    /** 併入主要事件單按鈕 */
    public String INCFORM_DIALOG_BTN = "button#incFormDailog";
    /** 衝擊分析題目的按鈕 */
    public String IMPACT_DIALOG_BTN = "button.impactDialogBtn";
    /** 衝擊分析題目的按鈕 */
    public String ONLINE_FAIL_BTN = "button#onlineJobFormIdBtn";
    
    /** 上線失敗 */
    public String ONLINE_FAIL_CHECKBOX = "input[name='isOnlineFail']";
    /** 上線時間 */
    public String ONLINE_TIME_CALENDAR = "input[name='onlineTime']";
    /** (上線失敗的)工作單單號 */
    public String ONLINE_JOB_FROM_ID = "input[name='onlineJobFormId']";
    /** 同一事件兩日內復發 */
    public String IS_SAME_INC = "input[name='isSameInc']";
    
}