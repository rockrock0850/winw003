package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.fasterxml.jackson.annotation.JsonFormat;

public class EventFormVO extends BaseFormVO {
    
    // 明細欄位
    private String cCategory;             // 組態分類
    private String cClass;                // 組態元件類別
    private String cComponent;            // 組態元件
    private String sClass;                // 服務類別
    private String sSubClass;             // 服務子類別
    private String systemId;              // 系統編號
    private String system;                // 系統名稱
    private String assetGroup;            // 資訊資產群組
    private String effectScope;           // 影響範圍
    private String urgentLevel;           // 緊急程度
    private String eventPriority;         // 事件優先順序
    private String effectScopeWording;    // 影響範圍說明
    private String urgentLevelWording;    // 緊急程度說明
    private String eventPriorityWording;  // 事件優先順序說明
    private String eventClass;            // 事件類別
    private String eventType;             // 事件類型
    private String eventSecurity;         // 資安事件
    private String countersigneds;        // 會辦科(CSV格式)
    private List<EventFormVO> verifyLogs; // 審核歷程
    private String isOnlineFail;          // 上線失敗
    private String onlineJobFormId;       // 工作單單號
    private Date onlineTime;              // 上線時間  
    
    // 起單狀態
    private boolean isAssigning;          // 是否為指派流程
    private boolean isSelfSolve;          // 是否為自行處理
    private boolean isShowBackToPic;      // 是否顯示退回經辦按鈕
    private boolean isBackToApplyLevel1;  // 是否回到申請等級一
    private boolean isScopeChanged;       // 變更範圍是否不同
    private boolean isEctExtended;        // 預計完成時間是否展延
    
    public String getcCategory () {
        return cCategory;
    }

    public void setcCategory (String cCategory) {
        this.cCategory = cCategory;
    }

    public String getcClass () {
        return cClass;
    }

    public void setcClass (String cClass) {
        this.cClass = cClass;
    }

    public String getcComponent () {
        return cComponent;
    }

    public void setcComponent (String cComponent) {
        this.cComponent = cComponent;
    }

    public String getsClass () {
        return sClass;
    }

    public void setsClass (String sClass) {
        this.sClass = sClass;
    }

    public String getsSubClass () {
        return sSubClass;
    }

    public void setsSubClass (String sSubClass) {
        this.sSubClass = sSubClass;
    }

    public String getSystemId () {
        return systemId;
    }

    public void setSystemId (String systemId) {
        this.systemId = systemId;
    }

    public String getSystem () {
        return system;
    }

    public void setSystem (String system) {
        this.system = system;
    }

    public String getAssetGroup () {
        return assetGroup;
    }

    public void setAssetGroup (String assetGroup) {
        this.assetGroup = assetGroup;
    }

    public String getEffectScope () {
        return effectScope;
    }

    public void setEffectScope (String effectScope) {
        this.effectScope = effectScope;
    }

    public String getUrgentLevel () {
        return urgentLevel;
    }

    public void setUrgentLevel (String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public String getEventPriority () {
        return this.eventPriority;
    }

    public void setEventPriority (String eventPriority) {
        this.eventPriority = eventPriority;
    }

    public String getEventClass () {
        return eventClass;
    }

    public void setEventClass (String eventClass) {
        this.eventClass = eventClass;
    }

    public String getEventType () {
        return eventType;
    }

    public void setEventType (String eventType) {
        this.eventType = eventType;
    }

    public String getEventSecurity () {
        return eventSecurity;
    }

    public void setEventSecurity (String eventSecurity) {
        this.eventSecurity = eventSecurity;
    }

    public List<EventFormVO> getVerifyLogs () {
        return verifyLogs;
    }

    public void setVerifyLogs (List<EventFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public boolean getIsAssigning () {
        return isAssigning;
    }

    public void setIsAssigning (boolean isAssigning) {
        this.isAssigning = isAssigning;
    }

    public boolean getIsSelfSolve () {
        return isSelfSolve;
    }

    public void setIsSelfSolve (boolean isSelfSolve) {
        this.isSelfSolve = isSelfSolve;
    }

    public String getCountersigneds () {
        return countersigneds;
    }

    public void setCountersigneds (String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public String getEffectScopeWording () {
        return effectScopeWording;
    }

    public void setEffectScopeWording (String effectScopeWording) {
        this.effectScopeWording = effectScopeWording;
    }

    public String getUrgentLevelWording () {
        return urgentLevelWording;
    }

    public void setUrgentLevelWording (String urgentLevelWording) {
        this.urgentLevelWording = urgentLevelWording;
    }

    public String getEventPriorityWording () {
        return eventPriorityWording;
    }

    public void setEventPriorityWording (String eventPriorityWording) {
        this.eventPriorityWording = eventPriorityWording;
    }

    public boolean getIsShowBackToPic() {
        return isShowBackToPic;
    }

    public void setIsShowBackToPic(boolean isShowBackToPic) {
        this.isShowBackToPic = isShowBackToPic;
    }

    public boolean isBackToApplyLevel1() {
        return isBackToApplyLevel1;
    }

    public void setIsBackToApplyLevel1(boolean isBackToApplyLevel1) {
        this.isBackToApplyLevel1 = isBackToApplyLevel1;
    }

    public boolean getIsScopeChanged () {
        return isScopeChanged;
    }

    public void setIsScopeChanged (boolean isScopeChanged) {
        this.isScopeChanged = isScopeChanged;
    }

    public boolean getIsEctExtended () {
        return isEctExtended;
    }

    public void setIsEctExtended (boolean isEctExtended) {
        this.isEctExtended = isEctExtended;
    }

    public String getIsOnlineFail() {
        return isOnlineFail;
    }

    public void setIsOnlineFail(String isOnlineFail) {
        this.isOnlineFail = isOnlineFail;
    }

    public String getOnlineJobFormId () {
        return onlineJobFormId;
    }

    public void setOnlineJobFormId (String onlineJobFormId) {
        this.onlineJobFormId = onlineJobFormId;
    }

    public Date getOnlineTime () {
        return onlineTime;
    }

    @JsonFormat(pattern="yyyy/MM/dd")
    public void setOnlineTime (Date onlineTime) {
        this.onlineTime = onlineTime;
    }

}
