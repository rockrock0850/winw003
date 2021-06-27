package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * AP工作單 資料物件
 * 
 * @author adam.yeh
 */
public class ApJobFormVO extends BaseFormVO {

    private String sClass;                  //服務類別
    private String sSubClass;               //服務子類別
    private String changeType;              //變更類型
    private String changeRank;              //變更等級
    private String systemId;                //系統ID
    private String system;                  //系統名稱
    private String isHandleFirst;           //先處理後呈閱
    private String isCorrect;               //上線修正
    private String isAddFuntion;            //新增系統功能
    private String isWatching;              //送交監督人員
    private String isAddReport;             //新增報表
    private String isAddFile;               //新增檔案
    private String isProgramOnline;         //程式上線
    private String isPlaning;               //計劃性系統維護
    private String isUnPlaning;             //非計劃性系統維護
    private String purpose;                 //作業目的
    private String countersigneds;          //會辦科
    private String isCreateJobCIssue;       //是否開啟工作會辦單
    private String isCreateSpJobIssue;      //是否新增SP工作單
    private String isOwner;                 //是否為工作項目分配人員
    private String workProject;             //作業關卡代號
    private String workProjectName;         //作業關卡名稱
    private String isModifyProgram;         //未有修改程式
    private boolean isWithoutWatching;      //是否不需要監督人員進行審核
    private boolean isBackToApplyLevel1;    //是否回到申請等級一
    
    private List<ApJobFormVO> verifyLogs;   // 審核歷程
    
    /*
     * 工作單內的程式庫頁簽需要介接其他系統的版本控制資料
     */
    private String fn;          // 表單編號
    private Date time;          // 程式庫頁簽裡面的檔案儲存進資料庫的時間
    private String msg;         // 回傳訊息
    private byte[] data;        // 檔案內容
    private String fileName;    // 程式庫頁簽裡面的檔案名稱
    private String baseLine;    // 程式庫頁簽裡面的BaseLine編號
    private String rowType;     // 程式庫的資料/BaseLine的資料
    private String qyStatus;    // 狀態碼(根據文件所代表意義會有所不同)

    public List<ApJobFormVO> getVerifyLogs() {
        return verifyLogs;
    }

    public void setVerifyLogs(List<ApJobFormVO> verifyLogs) {
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

    public boolean getIsWithoutWatching() {
        return isWithoutWatching;
    }

    public void setIsWithoutWatching(boolean isWithoutWatching) {
        this.isWithoutWatching = isWithoutWatching;
    }
    
    public String getIsCreateSpJobIssue() {
        return isCreateSpJobIssue;
    }

    public void setIsCreateSpJobIssue(String isCreateSpJobIssue) {
        this.isCreateSpJobIssue = isCreateSpJobIssue;
    }

    public Date getTime () {
        return time;
    }

    public void setTime (Date time) {
        this.time = time;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
    }

    public byte[] getData () {
        return data;
    }

    public void setData (byte[] data) {
        this.data = data;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public String getBaseLine () {
        return baseLine;
    }

    public void setBaseLine (String baseLine) {
        this.baseLine = baseLine;
    }

    public String getRowType () {
        return rowType;
    }

    public void setRowType (String rowType) {
        this.rowType = rowType;
    }

    public String getQyStatus () {
        return qyStatus;
    }

    public void setQyStatus (String qyStatus) {
        this.qyStatus = qyStatus;
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

    public String getWorkProject () {
        return workProject;
    }

    public void setWorkProject (String workProject) {
        this.workProject = workProject;
    }

    public String getIsProgramOnline () {
        return isProgramOnline;
    }

    public void setIsProgramOnline (String isProgramOnline) {
        this.isProgramOnline = isProgramOnline;
    }

    public String getIsModifyProgram() {
        return isModifyProgram;
    }

    public void setIsModifyProgram(String isModifyProgram) {
        this.isModifyProgram = isModifyProgram;
    }
    
}
