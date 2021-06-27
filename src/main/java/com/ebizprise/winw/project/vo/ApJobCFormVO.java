package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * AP工作會辦單 資料物件
 * 
 * @author emily.lai
 */
public class ApJobCFormVO extends BaseFormVO {

    private String sClass;                 // 服務類別
    private String sSubClass;              // 服務子類別
    private String changeType;             // 變更類型
    private String changeRank;             // 變更等級
    private String systemId;               // 系統ID
    private String system;                 // 系統名稱
    private String isHandleFirst;          // 先處理後呈閱
    private String isCorrect;              // 上線修正
    private String isAddFuntion;           // 新增系統功能
    private String isWatching;             // 送交監督人員
    private String isAddReport;            // 新增報表
    private String isAddFile;              // 新增檔案
    private String isPlaning;              // 計劃性系統維護
    private String isUnPlaning;            // 非計劃性系統維護
    private String isProgramOnline;        // 程式上線
    private String purpose; 			   // 作業目的
    private String countersigneds; 		   // 會辦科
    private String isCreateJobCIssue;	   // 是否開啟工作會辦單
    private String isOwner;				   // 是否為工作項目分配人員
    private String workProjectName;		   // 作業關卡名稱
    private String isModifyProgram;        // 未有修改程式
    private boolean isBackToApplyLevel1;   // 是否回到申請等級一
    private String currentProcess;         // 當前處理的內會流程
    private String internalProcessItems;   // 已勾選的內會流程
    
	private List<ApJobCFormVO> verifyLogs; // 審核歷程


    public List<ApJobCFormVO> getVerifyLogs() {
        return verifyLogs;
    }

    public void setVerifyLogs(List<ApJobCFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public String getSclass() {
        return sClass;
    }

    public void setSclass(String sClass) {
        this.sClass = sClass;
    }

    public String getSsubClass() {
        return sSubClass;
    }

    public void setSsubClass(String sSubClass) {
        this.sSubClass = sSubClass;
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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
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

    public String getIsWatching() {
        return isWatching;
    }

    public void setIsWatching(String isWatching) {
        this.isWatching = isWatching;
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

    public String getCountersigneds() {
        return countersigneds;
    }

    public void setCountersigneds(String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getsSubClass() {
        return sSubClass;
    }

    public void setsSubClass(String sSubClass) {
        this.sSubClass = sSubClass;
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

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getIsCreateJobCIssue() {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue(String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
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
    
    public String getIsProgramOnline() {
        return isProgramOnline;
    }

    public void setIsProgramOnline(String isProgramOnline) {
        this.isProgramOnline = isProgramOnline;
    }

    public String getIsModifyProgram() {
        return isModifyProgram;
    }

    public void setIsModifyProgram(String isModifyProgram) {
        this.isModifyProgram = isModifyProgram;
    }
    
    public String getCurrentProcess() {
		return currentProcess;
	}

	public void setCurrentProcess(String currentProcess) {
		this.currentProcess = currentProcess;
	}
	
    public String getInternalProcessItems() {
		return internalProcessItems;
	}

	public void setInternalProcessItems(String internalProcessItems) {
		this.internalProcessItems = internalProcessItems;
	}
}
