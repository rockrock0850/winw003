package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * SP工作單 資料物件
 * 
 * @author adam.yeh
 */
public class SpJobFormVO extends BaseFormVO {

    private String effectScope;           //
    private String remark;                //
    private String working;               //
    private String status;                //
    private String cCategory;             // 組態分類
    private String cClass;                // 組態元件類別
    private String cComponent;            // 組態元件
    private String isReset;               //
    private String isProduction;          //
    private String isTest;                //
    private String isHandleFirst;         //
    private String reset;                 //
    private String countersigneds;        //
    private String workingItem;           //
    private String isCreateJobCIssue;     // 是否開啟工作會辦單
    private String isOwner;               // 是否為工作項目分配人員
    private String workProjectName;       // 作業關卡名稱
    private Date mect;                    //
    private Date onLineTime;              //
    
    private List<SpJobFormVO> verifyLogs; // 審核歷程
    
    // 起單狀態
    private boolean isAssigning;          // 是否為人員指派流程
    private boolean isBackToApplyLevel1; // 是否回到申請等級一

    public List<SpJobFormVO> getVerifyLogs () {
        return verifyLogs;
    }

    public void setVerifyLogs (List<SpJobFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public String getEffectScope () {
        return effectScope;
    }

    public void setEffectScope (String effectScope) {
        this.effectScope = effectScope;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getWorking () {
        return working;
    }

    public void setWorking (String working) {
        this.working = working;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getIsReset () {
        return isReset;
    }

    public void setIsReset (String isReset) {
        this.isReset = isReset;
    }

    public String getIsProduction () {
        return isProduction;
    }

    public void setIsProduction (String isProduction) {
        this.isProduction = isProduction;
    }

    public String getIsTest () {
        return isTest;
    }

    public void setIsTest (String isTest) {
        this.isTest = isTest;
    }

    public String getIsHandleFirst () {
        return isHandleFirst;
    }

    public void setIsHandleFirst (String isHandleFirst) {
        this.isHandleFirst = isHandleFirst;
    }

    public String getReset () {
        return reset;
    }

    public void setReset (String reset) {
        this.reset = reset;
    }

    public String getCountersigneds () {
        return countersigneds;
    }

    public void setCountersigneds (String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public Date getOnLineTime () {
        return onLineTime;
    }

    public void setOnLineTime (Date onLineTime) {
        this.onLineTime = onLineTime;
    }

    public String getWorkingItem () {
        return workingItem;
    }

    public void setWorkingItem (String workingItem) {
        this.workingItem = workingItem;
    }

    public Date getMect () {
        return mect;
    }

    public void setMect (Date mect) {
        this.mect = mect;
    }

    public String getIsCreateJobCIssue () {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue (String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
    }

    public String getIsOwner () {
        return isOwner;
    }

    public void setIsOwner (String isOwner) {
        this.isOwner = isOwner;
    }

    public boolean getIsAssigning () {
        return isAssigning;
    }

    public void setIsAssigning (boolean isAssigning) {
        this.isAssigning = isAssigning;
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

    public String getWorkProjectName () {
        return workProjectName;
    }

    public void setWorkProjectName (String workProjectName) {
        this.workProjectName = workProjectName;
    }
    
    public boolean isBackToApplyLevel1() {
        return isBackToApplyLevel1;
    }

    public void setIsBackToApplyLevel1(boolean isBackToApplyLevel1) {
        this.isBackToApplyLevel1 = isBackToApplyLevel1;
    }

}
