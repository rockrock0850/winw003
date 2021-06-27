package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 需求單 資料物件
 * 
 * @author adam.yeh
 */
public class RequirementFormVO extends BaseFormVO {
    
    // 明細欄位
    private String division;                        // 負責科
    private String cCategory;                       // 組態分類
    private String cClass;                          // 組態元件類別
    private String cComponent;                      // 組態元件
    private String sClass;                          // 服務類別
    private String sSubClass;                       // 服務子類別
    private String systemId;                        // 系統編號
    private String system;                          // 系統名稱
    private String assetGroup;                      // 資訊資產群組
    private String effectScope;                     // 影響範圍
    private String urgentLevel;                     // 緊急程度
    private String requireRank;                     // 需求等級
    private String effectScopeWording;              // 影響範圍說明
    private String urgentLevelWording;              // 緊急程度說明
    private String countersigneds;                  // 會辦科(CSV格式)
    private List<RequirementFormVO> verifyLogs;     // 審核歷程
    private boolean isBackToApplyLevel1;            // 是否回到申請等級一
    private boolean isEctExtended;                  // 預計完成時間是否展延
    private boolean isScopeChanged;                 // 變更範圍是否不同
    
    public String getDivision () {
        return division;
    }
    
    public void setDivision (String division) {
        this.division = division;
    }
    
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
    
    public String getRequireRank () {
        return requireRank;
    }
    
    public void setRequireRank (String requireRank) {
        this.requireRank = requireRank;
    }
    
    public String getCountersigneds () {
        return countersigneds;
    }
    
    public void setCountersigneds (String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public List<RequirementFormVO> getVerifyLogs () {
        return verifyLogs;
    }

    public void setVerifyLogs (List<RequirementFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
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

}
