package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

public class CountersignedFormVO extends BaseFormVO {
    
    // 明細欄位
    private String sClass;                        // 服務類別
    private String systemId;                      // 系統編號
    private String system;                        // 系統名稱
    private String assetGroup;                    // 資訊資產群組
    private String hostHandle;                    // 主辦科處理情形
    private String countersignedHandle;           // 會辦科處理情形
    private String userGroup;                     // 處理人員群組
    private String spcGroups;                     // 會辦系統科群組
    private List<CountersignedFormVO> verifyLogs; // 審核歷程
    private boolean isBackToApplyLevel1;          // 是否回到申請等級一
    private boolean isScopeChanged;               // 變更範圍是否不同
    private boolean isEctExtended;                // 預計完成時間是否展延
    
    // 表單資訊頁簽-日期區塊欄位
    private Date mect; // 主單預計完成時間
    
    // 起單狀態
    private boolean isAssigning;                  // 是否為人員指派流程

    public List<CountersignedFormVO> getVerifyLogs () {
        return verifyLogs;
    }

    public void setVerifyLogs (List<CountersignedFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public boolean getIsAssigning () {
        return isAssigning;
    }

    public void setIsAssigning (boolean isAssigning) {
        this.isAssigning = isAssigning;
    }

    public String getsClass () {
        return sClass;
    }

    public void setsClass (String sClass) {
        this.sClass = sClass;
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

    public Date getMect () {
        return mect;
    }

    public void setMect (Date mect) {
        this.mect = mect;
    }

    public String getSpcGroups () {
        return spcGroups;
    }

    public void setSpcGroups (String spcGroups) {
        this.spcGroups = spcGroups;
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
