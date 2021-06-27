package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * SP工作會辦單 資料物件
 * 
 * @author emily.lai
 */
public class SpJobCFormVO extends BaseFormVO {

    private String purpose;                     // 作業目的
    private String remark;                      // 修改詳細說明
    private String countersigneds;              // 工作會辦處理情形
    private String isProduction;                // PRODUCTION
    private String isTest;                      // TEST
    private String isHandleFirst;               // 先處理後呈閱
    private String system;                      // 系統名稱
    private String cuserId;                     // 會辦處理人員
    private String astatus;                     // 核准狀態( Approval Status )
    private String astatusWording;              // 核准狀態文字
    private String status;                      // 系統狀態
    private String sstatusWording;              // 系統狀態文字
    private String isCreateSPJobIssue;          // 是否可開工作單
    private String workProjectName;             // 作業關卡名稱
    private String isOwner;                     // 是否為工作項目分配人員
    private Date mect;                          // 主單預計完成時間( Main Estimate Complete Time )
    private String spcGroups;                   // 會辦系統科群組
    private List<SpJobCFormVO> verifyLogs;      // 審核歷程
    private boolean isBackToApplyLevel1;        // 是否回到申請等級一
    private String csPerson;                    // 會辦單流程人員
    private String currentProcess;              // 當前處理的內會流程

    // 起單狀態
    private boolean isAssigning;                // 是否為人員指派流程

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getCountersigneds() {
        return countersigneds;
    }
    
    public void setCountersigneds(String countersigneds) {
        this.countersigneds = countersigneds;
    }
    
    public String getIsProduction() {
        return isProduction;
    }
    
    public void setIsProduction(String isProduction) {
        this.isProduction = isProduction;
    }
    
    public String getIsTest() {
        return isTest;
    }
    
    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }
    
    public String getIsHandleFirst() {
        return isHandleFirst;
    }
    
    public void setIsHandleFirst(String isHandleFirst) {
        this.isHandleFirst = isHandleFirst;
    }
    
    public String getSystem() {
        return system;
    }
    
    public void setSystem(String system) {
        this.system = system;
    }
    
    public String getCuserId() {
        return cuserId;
    }
    
    public void setCuserId(String cuserId) {
        this.cuserId = cuserId;
    }
    
    public String getAstatus() {
        return astatus;
    }
    
    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }
    
    public String getAstatusWording() {
        return astatusWording;
    }
    
    public void setAstatusWording(String astatusWording) {
        this.astatusWording = astatusWording;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSstatusWording() {
        return sstatusWording;
    }
    
    public void setSstatusWording(String sstatusWording) {
        this.sstatusWording = sstatusWording;
    }
    
    public Date getMect() {
        return mect;
    }
    
    public void setMect(Date mect) {
        this.mect = mect;
    }
    
    public List<SpJobCFormVO> getVerifyLogs() {
        return verifyLogs;
    }

    public void setVerifyLogs(List<SpJobCFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }
    
    public boolean getIsAssigning() {
        return isAssigning;
    }
    
    public void setIsAssigning(boolean isAssigning) {
        this.isAssigning = isAssigning;
    }

    public String getIsCreateSPJobIssue () {
        return isCreateSPJobIssue;
    }

    public void setIsCreateSPJobIssue (String isCreateSPJobIssue) {
        this.isCreateSPJobIssue = isCreateSPJobIssue;
    }

    public String getWorkProjectName () {
        return workProjectName;
    }

    public void setWorkProjectName (String workProjectName) {
        this.workProjectName = workProjectName;
    }

    public String getSpcGroups () {
        return spcGroups;
    }

    public void setSpcGroups (String spcGroups) {
        this.spcGroups = spcGroups;
    }

    public String getIsOwner () {
        return isOwner;
    }

    public void setIsOwner (String isOwner) {
        this.isOwner = isOwner;
    }
    
    public boolean isBackToApplyLevel1() {
        return isBackToApplyLevel1;
    }

    public void setIsBackToApplyLevel1(boolean isBackToApplyLevel1) {
        this.isBackToApplyLevel1 = isBackToApplyLevel1;
    }

    public String getCsPerson() {
        return csPerson;
    }

    public void setCsPerson(String csPerson) {
        this.csPerson = csPerson;
    }
    
    public String getCurrentProcess() {
		return currentProcess;
	}

	public void setCurrentProcess(String currentProcess) {
		this.currentProcess = currentProcess;
	}
}
