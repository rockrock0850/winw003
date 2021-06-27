package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 表單查詢資料物件
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年8月27日
 */
public class FormSearchVO extends BaseFormVO {
    
    private boolean isSearch;
    private String formFields;
    private List<String> columnOrder;        // 欄位顯示排序
    private Map<String, Object> formInfo;
    
    // 欄位資訊
    private String isAlterDone;              // 變更成功失敗
    private String value;
    private int index;
    private int length;
    private String name;
    private String type;
    private String voName;
    private String createTimeStart;          // 開單時間-起日
    private String createTimeEnd;            // 開單時間-迄日
    private String updatedAtStart;           // 知識庫前次變更時間-起日
    private String updatedAtEnd;             // 知識庫前次變更時間-迄日
    private String ectStart;                 // 預計完成時間-起日
    private String ectEnd;                   // 預計完成時間-迄日
    private String astStart;                 // 實際開始時間-起日
    private String astEnd;                   // 實際開始時間-迄日
    private String actStart;                 // 實際完成時間-起日
    private String actEnd;                   // 實際完成時間-迄日
    private String catStart;                 // 變更申請時間-起日
    private String catEnd;                   // 變更申請時間-迄日
    private String cctStart;                 // 預計變更結束時間-起日
    private String cctEnd;                   // 預計變更結束時間-迄日
    private String eotStart;                 // 與業務單位確認預計上線時間-起日
    private String eotEnd;                   // 與業務單位確認預計上線時間-迄日
    private String reportTimeStart;          // 報告時間-起日
    private String reportTimeEnd;            // 報告時間-迄日
    private String observationStart;         // 問題單觀察期-起日
    private String observationEnd;           // 問題單觀察期-起日
    private String sClass;                   // 服務類別
    private String sSubClass;                // 服務子類別
    private String system;                   // 系統名稱
    private String systemId;                 // 系統代碼
    private String assetGroup;               // 資訊資產群組
    private String cCategory;                // 組態分類
    private String cClass;                   // 組態元件類別
    private String cComponent;               // 組態元件
    private String effectScope;              // 影響範圍
    private String urgentLevel;              // 緊急程度
    private String requireRank;              // 需求等級
    private String division;                 // 負責科
    private String countersigneds;           // 會辦科
    private String apcCountersigneds;        // 工作會辦科
    private String questionPriority;         // 問題優先順序
    private Date infoDateCreateTime;         // 開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
    private String infoDateCreateTimeStart;  // 起日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
    private String infoDateCreateTimeEnd;    // 迄日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
    private String eventClass;               // 事件單主類別
    private String eventType;                // 事件類型
    private String eventSecurity;            // 資安事件
    private String eventPriority;            // 事件優先順序
    private String excludeTimeStart;         // 事件(當下)排除時間-起日
    private String excludeTimeEnd;           // 事件(當下)排除時間-迄日
    private String isOnlineFail;             // 上線失敗
    private String onlineTime;               // 上線時間
    private String onlineJobFormId;          // (上線失敗的)工作單單號
    private String effectSystem;             // 變更影響系統
    private String isNewSystem;              // 是新系統
    private String isNewService;             // 是新服務暨重大服務
    private String isUrgent;                 // 緊急變更
    private String standard;                 // 標準變更作業
    private String changeType;               // 變更類型
    private String changeRank;               // 變更等級
    private String isEffectField;            // 有新增異動欄位影響到資料倉儲系統產出資料
    private String isEffectAccountant;       // 有新增異動會計科目影響到資料倉儲系統產出資料
    private String isModifyProgram;          // 未有修改程式
    private Date mect;                       // 主單預計完成時間
    private String mectStart;                // 主單預計完成時間-起日
    private String mectEnd;                  // 主單預計完成時間-迄日
    private String hostHandle;               // 主辦科處理情形
    private String countersignedHandle;      // 會辦科處理情形
    private String userGroup;                // 處理人員群組
    private String knowledge1;               // 知識庫原因類別
    private String knowledge2;               // 知識庫原因子類別
    private String knowledges;               // 知識庫根因
    private String solutions;                // 知識庫相關解決方案
    
    //其他頁簽欄位
    private String formUserRecord;           // 日誌
    private String associationForm;          // 關聯表單
    private String formFile;                 // 附件
    private String evaluation;               // 衝擊分析-影響評估
    private String solution;                 // 衝擊分析-因應措施
    private String total;                    // 分數
    
    //工作單欄位
    private String aStatus;                  // 核准狀態
    private String isHandleFirst;            // 先處理後呈閱
    private String isCorrect;                // 上線修正
    private String isAddFuntion;             // 新增系統功能
    private String isAddReport;              // 新增報表
    private String isAddFile;                // 新增檔案
    private String isProgramOnline;          // 程式上線
    private String purpose;                  // 作業目的
    private String isWatching;               // 送交監督人員
    private String isPlaning;                // 會造成服務中斷或需要停機，屬於計畫性系統維護
    private String isUnPlaning;              // 會造成服務中斷或需要停機，屬於非計畫性系統維護
    private String offLineTimeStart;         // 公告停機時間-起日
    private String offLineTimeEnd;           // 公告停機時間-迄日
    private String istStart;                 // 中斷起始時間-起日
    private String istEnd;                   // 中斷起始時間-迄日
    private String ictStart;                 // 中斷結束時間-起日
    private String ictEnd;                   // 中斷結束時間-迄日
    private String tctStart;                 // 測試系統完成時間-起日
    private String tctEnd;                   // 測試系統完成時間-迄日
    private String sctStart;                 // 連線系統完成時間-起日
    private String sctEnd;                   // 連線系統完成時間-迄日
    private String sitStart;                 // 連線系統實施時間-起日
    private String sitEnd;                   // 連線系統實施時間-迄日
    private String working;                  // 工作項目
    private String remark;                   // 工作項目
    private String reset;                    // 工作項目
    private String isReset;                  // 回復原廠設定
    private String isTest;                   // TEST
    private String isProduction;             // PRODUCTION
    
    // 工作會辦頁簽
    private String cCountersigneds;          // 工作會辦處理情形
    private String cDivision;                // 會辦頁簽科別
    private String cDataType;                // DC科會辦頁簽型態
    private String cType;                    // 工作頁簽資料型態
    private String cUserId;                  // 系統負責人
    private String cbFormId;                 // 批次變更的表單編號
    private String cbUserId;                 // 系統負責人
    private Date cTct;                       // 測試系統完成日期
    private String cTctStart;                // 測試系統完成日期-起日
    private String cTctEnd;                  // 測試系統完成日期-迄日
    private Date cSct;                       // 連線系統完成日期
    private String cSctStart;                // 連線系統完成日期-起日
    private String cSctEnd;                  // 連線系統完成日期-迄日
    private Date cSit;                       // 連線系統實施日期
    private String cSitStart;                // 連線系統實施日期-起日
    private String cSitEnd;                  // 連線系統實施日期-迄日
    private Date cIt;                        // 程式CL及工作執行日期
    private String cItStart;                 // 程式CL及工作執行日期-起日
    private String cItEnd;                   // 程式CL及工作執行日期-迄日
    private Date cEit;                       // 預計實施日期
    private String cEitStart;                // 預計實施日期-起日
    private String cEitEnd;                  // 預計實施日期-迄日
    private Date cAst;                       // 實際實施日期
    private String cAstStart;                // 實際實施日期-起日
    private String cAstEnd;                  // 實際實施日期-迄日
    private String cDescription;             // 修改詳細說明
    private String cIsTest;                  // TEST
    private String cIsProduction;            // PRODUCTION
    private String cBook;                    // Book
    private String cBookNumber;              // Book支數
    private String cOnlyCode;                // Compile Only Return_Code
    private String cOnly;                    // Compile Only
    private String cOnlyNumber;              // Compile Only程式支數
    private String cLinkCode;                // Compile Link Return_Code
    private String cLink;                    // Compile Link
    private String cLinkNumber;              // Compile Link程式支數
    private String cLinkOnly;                // Link Only
    private String cLinkOnlyNumber;          // Link Only程式支數
    private String cOther;                   // 其他事項
    private String cbOther;                  // 其他事項
    private String cIsRollback;              // 回復到前一版
    private String cbIsRollback;             // 回復到前一版
    private String cRollbackDesc;            // 其他
    private String cPsb;                     // PSB名稱
    private String cIsChange;                // 批次作業流程變更
    private String cIsOtherDesc;             // 其他(綁工作內容或附件說明)
    private String cIsHelp;                  // 執行HELP程式
    private String cIsAllow;                 // 允許JOB在TWS執行時，Return Code <=4視為正常
    private String cCljcl;                   // CL JCL
    private String cIsOther;                 // 其他
    private String cJcl;                     // 執行JCL
    private String cIsHelpCl;                // HELP程式CL
    private String cProgramName;             // 程式名稱
    private String cProgramNumber;           // 程式支數
    private String cReason;                  // 申請原因
    private String cContent;                 // 資管科工作內容
    private String cRemark;                  // 批次單的備註
    private String cbRemark;                 // 批次單的備註
    private String cDbName;                  // 資料庫名稱
    private String cPsbName;                 // PSB名稱
    private String cAlterWay;                // 變更方式
    
    // 批次作業中斷對策表管理
    private String batchName;                // 批次工作名稱
    private String executeTime;              // 執行時間
    private String dbInUse;                  // 使用資料庫
    private Date effectDate;                 // 生效日期
    private String effectDateStart;          // 生效日期-起日
    private String effectDateEnd;            // 生效日期-迄日
    
    // 開關
    private boolean isDefaultCol;            // 是否是預設查詢欄位
    
    public Map<String, Object> getFormInfo () {
        return formInfo;
    }

    public void setFormInfo (Map<String, Object> formInfo) {
        this.formInfo = formInfo;
    }

    public boolean getIsSearch () {
        return isSearch;
    }

    public void setIsSearch (boolean isSearch) {
        this.isSearch = isSearch;
    }

    public String getFormFields() {
        return formFields;
    }

    public void setFormFields(String formFields) {
        this.formFields = formFields;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVoName() {
        return voName;
    }

    public void setVoName(String voName) {
        this.voName = voName;
    }

    public String getRequireRank() {
        return requireRank;
    }

    public void setRequireRank(String requireRank) {
        this.requireRank = requireRank;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getEffectScope() {
        return effectScope;
    }

    public void setEffectScope(String effectScope) {
        this.effectScope = effectScope;
    }

    public String getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getEctStart() {
        return ectStart;
    }

    public void setEctStart(String ectStart) {
        this.ectStart = ectStart;
    }

    public String getEctEnd() {
        return ectEnd;
    }

    public void setEctEnd(String ectEnd) {
        this.ectEnd = ectEnd;
    }

    public String getActStart() {
        return actStart;
    }

    public void setActStart(String actStart) {
        this.actStart = actStart;
    }

    public String getActEnd() {
        return actEnd;
    }

    public void setActEnd(String actEnd) {
        this.actEnd = actEnd;
    }

    public String getEotStart() {
        return eotStart;
    }

    public void setEotStart(String eotStart) {
        this.eotStart = eotStart;
    }

    public String getEotEnd() {
        return eotEnd;
    }

    public void setEotEnd(String eotEnd) {
        this.eotEnd = eotEnd;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getCountersigneds() {
        return countersigneds;
    }

    public void setCountersigneds(String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public String getQuestionPriority() {
        return questionPriority;
    }

    public void setQuestionPriority(String questionPriority) {
        this.questionPriority = questionPriority;
    }

    public String getReportTimeStart() {
        return reportTimeStart;
    }

    public void setReportTimeStart(String reportTimeStart) {
        this.reportTimeStart = reportTimeStart;
    }

    public String getReportTimeEnd() {
        return reportTimeEnd;
    }

    public void setReportTimeEnd(String reportTimeEnd) {
        this.reportTimeEnd = reportTimeEnd;
    }

    public String getAstStart() {
        return astStart;
    }

    public void setAstStart(String astStart) {
        this.astStart = astStart;
    }

    public String getAstEnd() {
        return astEnd;
    }

    public void setAstEnd(String astEnd) {
        this.astEnd = astEnd;
    }

    public String getObservationStart() {
        return observationStart;
    }

    public void setObservationStart(String observationStart) {
        this.observationStart = observationStart;
    }

    public String getObservationEnd() {
        return observationEnd;
    }

    public void setObservationEnd(String observationEnd) {
        this.observationEnd = observationEnd;
    }

    public String getInfoDateCreateTimeStart() {
        return infoDateCreateTimeStart;
    }

    public void setInfoDateCreateTimeStart(String infoDateCreateTimeStart) {
        this.infoDateCreateTimeStart = infoDateCreateTimeStart;
    }

    public String getInfoDateCreateTimeEnd() {
        return infoDateCreateTimeEnd;
    }

    public void setInfoDateCreateTimeEnd(String infoDateCreateTimeEnd) {
        this.infoDateCreateTimeEnd = infoDateCreateTimeEnd;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventSecurity() {
        return eventSecurity;
    }

    public void setEventSecurity(String eventSecurity) {
        this.eventSecurity = eventSecurity;
    }

    public String getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(String eventPriority) {
        this.eventPriority = eventPriority;
    }

    public String getExcludeTimeStart() {
        return excludeTimeStart;
    }

    public void setExcludeTimeStart(String excludeTimeStart) {
        this.excludeTimeStart = excludeTimeStart;
    }

    public String getExcludeTimeEnd() {
        return excludeTimeEnd;
    }

    public void setExcludeTimeEnd(String excludeTimeEnd) {
        this.excludeTimeEnd = excludeTimeEnd;
    }

    public Date getInfoDateCreateTime() {
        return infoDateCreateTime;
    }

    public void setInfoDateCreateTime(Date infoDateCreateTime) {
        this.infoDateCreateTime = infoDateCreateTime;
    }

    public String getEffectSystem() {
        return effectSystem;
    }

    public void setEffectSystem(String effectSystem) {
        this.effectSystem = effectSystem;
    }

    public String getIsNewSystem() {
        return isNewSystem;
    }

    public void setIsNewSystem(String isNewSystem) {
        this.isNewSystem = isNewSystem;
    }

    public String getIsNewService() {
        return isNewService;
    }

    public void setIsNewService(String isNewService) {
        this.isNewService = isNewService;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeRank() {
        return changeRank;
    }

    public void setChangeRank(String changeRank) {
        this.changeRank = changeRank;
    }

    public String getCatStart() {
        return catStart;
    }

    public void setCatStart(String catStart) {
        this.catStart = catStart;
    }

    public String getCatEnd() {
        return catEnd;
    }

    public void setCatEnd(String catEnd) {
        this.catEnd = catEnd;
    }

    public String getCctStart() {
        return cctStart;
    }

    public void setCctStart(String cctStart) {
        this.cctStart = cctStart;
    }

    public String getCctEnd() {
        return cctEnd;
    }

    public void setCctEnd(String cctEnd) {
        this.cctEnd = cctEnd;
    }

    public String getIsEffectField() {
        return isEffectField;
    }

    public void setIsEffectField(String isEffectField) {
        this.isEffectField = isEffectField;
    }

    public String getIsEffectAccountant() {
        return isEffectAccountant;
    }

    public void setIsEffectAccountant(String isEffectAccountant) {
        this.isEffectAccountant = isEffectAccountant;
    }

    public String getsSubClass() {
        return sSubClass;
    }

    public void setsSubClass(String sSubClass) {
        this.sSubClass = sSubClass;
    }

    public String getAssetGroup() {
        return assetGroup;
    }

    public void setAssetGroup(String assetGroup) {
        this.assetGroup = assetGroup;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getcCategory() {
        return cCategory;
    }

    public void setcCategory(String cCategory) {
        this.cCategory = cCategory;
    }

    public String getcClass() {
        return cClass;
    }

    public void setcClass(String cClass) {
        this.cClass = cClass;
    }

    public String getcComponent() {
        return cComponent;
    }

    public void setcComponent(String cComponent) {
        this.cComponent = cComponent;
    }

    public String getFormUserRecord() {
        return formUserRecord;
    }

    public void setFormUserRecord(String formUserRecord) {
        this.formUserRecord = formUserRecord;
    }

    public String getAssociationForm() {
        return associationForm;
    }

    public void setAssociationForm(String associationForm) {
        this.associationForm = associationForm;
    }

    public String getFormFile() {
        return formFile;
    }

    public void setFormFile(String formFile) {
        this.formFile = formFile;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIsHandleFirst() {
        return isHandleFirst;
    }

    public void setIsHandleFirst(String isHandleFirst) {
        this.isHandleFirst = isHandleFirst;
    }

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getIsAddFuntion() {
        return isAddFuntion;
    }

    public void setIsAddFuntion(String isAddFuntion) {
        this.isAddFuntion = isAddFuntion;
    }

    public String getIsAddReport() {
        return isAddReport;
    }

    public void setIsAddReport(String isAddReport) {
        this.isAddReport = isAddReport;
    }

    public String getIsAddFile() {
        return isAddFile;
    }

    public void setIsAddFile(String isAddFile) {
        this.isAddFile = isAddFile;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getIsWatching() {
        return isWatching;
    }

    public void setIsWatching(String isWatching) {
        this.isWatching = isWatching;
    }

    public String getIsPlaning() {
        return isPlaning;
    }

    public void setIsPlaning(String isPlaning) {
        this.isPlaning = isPlaning;
    }

    public String getIsUnPlaning() {
        return isUnPlaning;
    }

    public void setIsUnPlaning(String isUnPlaning) {
        this.isUnPlaning = isUnPlaning;
    }

    public String getOffLineTimeStart() {
        return offLineTimeStart;
    }

    public void setOffLineTimeStart(String offLineTimeStart) {
        this.offLineTimeStart = offLineTimeStart;
    }

    public String getOffLineTimeEnd() {
        return offLineTimeEnd;
    }

    public void setOffLineTimeEnd(String offLineTimeEnd) {
        this.offLineTimeEnd = offLineTimeEnd;
    }

    public String getIstStart() {
        return istStart;
    }

    public void setIstStart(String istStart) {
        this.istStart = istStart;
    }

    public String getIstEnd() {
        return istEnd;
    }

    public void setIstEnd(String istEnd) {
        this.istEnd = istEnd;
    }

    public String getIctStart() {
        return ictStart;
    }

    public void setIctStart(String ictStart) {
        this.ictStart = ictStart;
    }

    public String getIctEnd() {
        return ictEnd;
    }

    public void setIctEnd(String ictEnd) {
        this.ictEnd = ictEnd;
    }

    public String getTctStart() {
        return tctStart;
    }

    public void setTctStart(String tctStart) {
        this.tctStart = tctStart;
    }

    public String getTctEnd() {
        return tctEnd;
    }

    public void setTctEnd(String tctEnd) {
        this.tctEnd = tctEnd;
    }

    public String getSctStart() {
        return sctStart;
    }

    public void setSctStart(String sctStart) {
        this.sctStart = sctStart;
    }

    public String getSctEnd() {
        return sctEnd;
    }

    public void setSctEnd(String sctEnd) {
        this.sctEnd = sctEnd;
    }

    public String getSitStart() {
        return sitStart;
    }

    public void setSitStart(String sitStart) {
        this.sitStart = sitStart;
    }

    public String getSitEnd() {
        return sitEnd;
    }

    public void setSitEnd(String sitEnd) {
        this.sitEnd = sitEnd;
    }

    public boolean getIsDefaultCol () {
        return isDefaultCol;
    }

    public void setIsDefaultCol (boolean isDefaultCol) {
        this.isDefaultCol = isDefaultCol;
    }

    public String getMectStart () {
        return mectStart;
    }

    public void setMectStart (String mectStart) {
        this.mectStart = mectStart;
    }

    public String getMectEnd () {
        return mectEnd;
    }

    public void setMectEnd (String mectEnd) {
        this.mectEnd = mectEnd;
    }

    public Date getMect () {
        return mect;
    }

    public void setMect (Date mect) {
        this.mect = mect;
    }

    public String getHostHandle () {
        return hostHandle;
    }

    public void setHostHandle (String hostHandle) {
        this.hostHandle = hostHandle;
    }

    public String getCountersignedHandle () {
        return countersignedHandle;
    }

    public void setCountersignedHandle (String countersignedHandle) {
        this.countersignedHandle = countersignedHandle;
    }

    public String getUserGroup () {
        return userGroup;
    }

    public void setUserGroup (String userGroup) {
        this.userGroup = userGroup;
    }

    public String getWorking () {
        return working;
    }

    public void setWorking (String working) {
        this.working = working;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getReset () {
        return reset;
    }

    public void setReset (String reset) {
        this.reset = reset;
    }

    public String getIsReset () {
        return isReset;
    }

    public void setIsReset (String isReset) {
        this.isReset = isReset;
    }

    public String getIsTest () {
        return isTest;
    }

    public void setIsTest (String isTest) {
        this.isTest = isTest;
    }

    public String getIsProduction () {
        return isProduction;
    }

    public void setIsProduction (String isProduction) {
        this.isProduction = isProduction;
    }

    public String getcUserId () {
        return cUserId;
    }

    public void setcUserId (String cUserId) {
        this.cUserId = cUserId;
    }

    public String getcSctStart () {
        return cSctStart;
    }

    public void setcSctStart (String cSctStart) {
        this.cSctStart = cSctStart;
    }

    public String getcSctEnd () {
        return cSctEnd;
    }

    public void setcSctEnd (String cSctEnd) {
        this.cSctEnd = cSctEnd;
    }

    public String getcSitStart () {
        return cSitStart;
    }

    public void setcSitStart (String cSitStart) {
        this.cSitStart = cSitStart;
    }

    public String getcSitEnd () {
        return cSitEnd;
    }

    public void setcSitEnd (String cSitEnd) {
        this.cSitEnd = cSitEnd;
    }

    public String getcItStart () {
        return cItStart;
    }

    public void setcItStart (String cItStart) {
        this.cItStart = cItStart;
    }

    public String getcItEnd () {
        return cItEnd;
    }

    public void setcItEnd (String cItEnd) {
        this.cItEnd = cItEnd;
    }

    public String getcEitStart () {
        return cEitStart;
    }

    public void setcEitStart (String cEitStart) {
        this.cEitStart = cEitStart;
    }

    public String getcEitEnd () {
        return cEitEnd;
    }

    public void setcEitEnd (String cEitEnd) {
        this.cEitEnd = cEitEnd;
    }

    public String getcAstStart () {
        return cAstStart;
    }

    public void setcAstStart (String cAstStart) {
        this.cAstStart = cAstStart;
    }

    public String getcAstEnd () {
        return cAstEnd;
    }

    public void setcAstEnd (String cAstEnd) {
        this.cAstEnd = cAstEnd;
    }

    public String getcDescription () {
        return cDescription;
    }

    public void setcDescription (String cDescription) {
        this.cDescription = cDescription;
    }

    public String getcIsTest () {
        return cIsTest;
    }

    public void setcIsTest (String cIsTest) {
        this.cIsTest = cIsTest;
    }

    public String getcIsProduction () {
        return cIsProduction;
    }

    public void setcIsProduction (String cIsProduction) {
        this.cIsProduction = cIsProduction;
    }

    public String getcBook () {
        return cBook;
    }

    public void setcBook (String cBook) {
        this.cBook = cBook;
    }

    public String getcBookNumber () {
        return cBookNumber;
    }

    public void setcBookNumber (String cBookNumber) {
        this.cBookNumber = cBookNumber;
    }

    public String getcOnlyCode () {
        return cOnlyCode;
    }

    public void setcOnlyCode (String cOnlyCode) {
        this.cOnlyCode = cOnlyCode;
    }

    public String getcOnly () {
        return cOnly;
    }

    public void setcOnly (String cOnly) {
        this.cOnly = cOnly;
    }

    public String getcOnlyNumber () {
        return cOnlyNumber;
    }

    public void setcOnlyNumber (String cOnlyNumber) {
        this.cOnlyNumber = cOnlyNumber;
    }

    public String getcLinkCode () {
        return cLinkCode;
    }

    public void setcLinkCode (String cLinkCode) {
        this.cLinkCode = cLinkCode;
    }

    public String getcLink () {
        return cLink;
    }

    public void setcLink (String cLink) {
        this.cLink = cLink;
    }

    public String getcLinkNumber () {
        return cLinkNumber;
    }

    public void setcLinkNumber (String cLinkNumber) {
        this.cLinkNumber = cLinkNumber;
    }

    public String getcOther () {
        return cOther;
    }

    public void setcOther (String cOther) {
        this.cOther = cOther;
    }

    public String getcIsRollback () {
        return cIsRollback;
    }

    public void setcIsRollback (String cIsRollback) {
        this.cIsRollback = cIsRollback;
    }

    public String getcRollbackDesc () {
        return cRollbackDesc;
    }

    public void setcRollbackDesc (String cRollbackDesc) {
        this.cRollbackDesc = cRollbackDesc;
    }

    public String getcPsb () {
        return cPsb;
    }

    public void setcPsb (String cPsb) {
        this.cPsb = cPsb;
    }

    public String getcIsChange () {
        return cIsChange;
    }

    public void setcIsChange (String cIsChange) {
        this.cIsChange = cIsChange;
    }

    public String getcIsOtherDesc () {
        return cIsOtherDesc;
    }

    public void setcIsOtherDesc (String cIsOtherDesc) {
        this.cIsOtherDesc = cIsOtherDesc;
    }

    public String getcIsHelp () {
        return cIsHelp;
    }

    public void setcIsHelp (String cIsHelp) {
        this.cIsHelp = cIsHelp;
    }

    public String getcIsAllow () {
        return cIsAllow;
    }

    public void setcIsAllow (String cIsAllow) {
        this.cIsAllow = cIsAllow;
    }

    public String getcCljcl () {
        return cCljcl;
    }

    public void setcCljcl (String cCljcl) {
        this.cCljcl = cCljcl;
    }

    public String getcIsOther () {
        return cIsOther;
    }

    public void setcIsOther (String cIsOther) {
        this.cIsOther = cIsOther;
    }

    public String getcJcl () {
        return cJcl;
    }

    public void setcJcl (String cJcl) {
        this.cJcl = cJcl;
    }

    public String getcIsHelpCl () {
        return cIsHelpCl;
    }

    public void setcIsHelpCl (String cIsHelpCl) {
        this.cIsHelpCl = cIsHelpCl;
    }

    public String getcProgramName () {
        return cProgramName;
    }

    public void setcProgramName (String cProgramName) {
        this.cProgramName = cProgramName;
    }

    public String getcProgramNumber () {
        return cProgramNumber;
    }

    public void setcProgramNumber (String cProgramNumber) {
        this.cProgramNumber = cProgramNumber;
    }

    public String getcReason () {
        return cReason;
    }

    public void setcReason (String cReason) {
        this.cReason = cReason;
    }

    public String getcContent () {
        return cContent;
    }

    public void setcContent (String cContent) {
        this.cContent = cContent;
    }

    public String getcRemark () {
        return cRemark;
    }

    public void setcRemark (String cRemark) {
        this.cRemark = cRemark;
    }

    public String getcDbName () {
        return cDbName;
    }

    public void setcDbName (String cDbName) {
        this.cDbName = cDbName;
    }

    public String getcPsbName () {
        return cPsbName;
    }

    public void setcPsbName (String cPsbName) {
        this.cPsbName = cPsbName;
    }

    public String getcAlterWay () {
        return cAlterWay;
    }

    public void setcAlterWay (String cAlterWay) {
        this.cAlterWay = cAlterWay;
    }

    public String getcTctStart () {
        return cTctStart;
    }

    public void setcTctStart (String cTctStart) {
        this.cTctStart = cTctStart;
    }

    public String getcTctEnd () {
        return cTctEnd;
    }

    public void setcTctEnd (String cTctEnd) {
        this.cTctEnd = cTctEnd;
    }

    public String getcDivision () {
        return cDivision;
    }

    public void setcDivision (String cDivision) {
        this.cDivision = cDivision;
    }

    public String getcDataType () {
        return cDataType;
    }

    public void setcDataType (String cDataType) {
        this.cDataType = cDataType;
    }

    public String getcType () {
        return cType;
    }

    public void setcType (String cType) {
        this.cType = cType;
    }

    public String getCbUserId () {
        return cbUserId;
    }

    public void setCbUserId (String cbUserId) {
        this.cbUserId = cbUserId;
    }

    public String getCbOther () {
        return cbOther;
    }

    public void setCbOther (String cbOther) {
        this.cbOther = cbOther;
    }

    public String getCbIsRollback () {
        return cbIsRollback;
    }

    public void setCbIsRollback (String cbIsRollback) {
        this.cbIsRollback = cbIsRollback;
    }

    public String getCbRemark () {
        return cbRemark;
    }

    public void setCbRemark (String cbRemark) {
        this.cbRemark = cbRemark;
    }

    public String getcLinkOnly () {
        return cLinkOnly;
    }

    public void setcLinkOnly (String cLinkOnly) {
        this.cLinkOnly = cLinkOnly;
    }

    public String getcLinkOnlyNumber () {
        return cLinkOnlyNumber;
    }

    public void setcLinkOnlyNumber (String cLinkOnlyNumber) {
        this.cLinkOnlyNumber = cLinkOnlyNumber;
    }

    public Date getcTct () {
        return cTct;
    }

    public void setcTct (Date cTct) {
        this.cTct = cTct;
    }

    public Date getcSct () {
        return cSct;
    }

    public void setcSct (Date cSct) {
        this.cSct = cSct;
    }

    public Date getcSit () {
        return cSit;
    }

    public void setcSit (Date cSit) {
        this.cSit = cSit;
    }

    public Date getcIt () {
        return cIt;
    }

    public void setcIt (Date cIt) {
        this.cIt = cIt;
    }

    public Date getcEit () {
        return cEit;
    }

    public void setcEit (Date cEit) {
        this.cEit = cEit;
    }

    public Date getcAst () {
        return cAst;
    }

    public void setcAst (Date cAst) {
        this.cAst = cAst;
    }

    public String getcCountersigneds () {
        return cCountersigneds;
    }

    public void setcCountersigneds (String cCountersigneds) {
        this.cCountersigneds = cCountersigneds;
    }

    public String getaStatus () {
        return aStatus;
    }

    public void setaStatus (String aStatus) {
        this.aStatus = aStatus;
    }

    public List<String> getColumnOrder () {
        return columnOrder;
    }

    public void setColumnOrder (List<String> columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getDbInUse() {
        return dbInUse;
    }

    public void setDbInUse(String dbInUse) {
        this.dbInUse = dbInUse;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getEffectDateStart() {
        return effectDateStart;
    }

    public void setEffectDateStart(String effectDateStart) {
        this.effectDateStart = effectDateStart;
    }

    public String getEffectDateEnd() {
        return effectDateEnd;
    }

    public void setEffectDateEnd(String effectDateEnd) {
        this.effectDateEnd = effectDateEnd;
    }

    public String getIsAlterDone () {
        return isAlterDone;
    }

    public void setIsAlterDone (String isAlterDone) {
        this.isAlterDone = isAlterDone;
    }

    public String getApcCountersigneds () {
        return apcCountersigneds;
    }

    public void setApcCountersigneds (String apcCountersigneds) {
        this.apcCountersigneds = apcCountersigneds;
    }

    public String getCbFormId () {
        return cbFormId;
    }

    public void setCbFormId (String cbFormId) {
        this.cbFormId = cbFormId;
    }

    public String getIsProgramOnline () {
        return isProgramOnline;
    }

    public void setIsProgramOnline (String isProgramOnline) {
        this.isProgramOnline = isProgramOnline;
    }

    public String getKnowledge1 () {
        return knowledge1;
    }

    public void setKnowledge1 (String knowledge1) {
        this.knowledge1 = knowledge1;
    }

    public String getKnowledge2 () {
        return knowledge2;
    }

    public void setKnowledge2 (String knowledge2) {
        this.knowledge2 = knowledge2;
    }
    
    public String getSystemId () {
        return systemId;
    }

    public void setSystemId (String systemId) {
        this.systemId = systemId;
    }

    public String getIsOnlineFail() {
        return isOnlineFail;
    }

    public void setIsOnlineFail(String isOnlineFail) {
        this.isOnlineFail = isOnlineFail;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getOnlineJobFormId() {
        return onlineJobFormId;
    }

    public void setOnlineJobFormId(String onlineJobFormId) {
        this.onlineJobFormId = onlineJobFormId;
    }

    public String getIsModifyProgram() {
        return isModifyProgram;
    }

    public void setIsModifyProgram(String isModifyProgram) {
        this.isModifyProgram = isModifyProgram;
    }

    public String getKnowledges () {
        return knowledges;
    }

    public void setKnowledges (String knowledges) {
        this.knowledges = knowledges;
    }

    public String getSolutions () {
        return solutions;
    }

    public void setSolutions (String solutions) {
        this.solutions = solutions;
    }

    public String getUpdatedAtStart () {
        return updatedAtStart;
    }

    public void setUpdatedAtStart (String updatedAtStart) {
        this.updatedAtStart = updatedAtStart;
    }

    public String getUpdatedAtEnd () {
        return updatedAtEnd;
    }

    public void setUpdatedAtEnd (String updatedAtEnd) {
        this.updatedAtEnd = updatedAtEnd;
    }

}
