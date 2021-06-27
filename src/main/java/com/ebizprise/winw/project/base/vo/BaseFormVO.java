package com.ebizprise.winw.project.base.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.vo.LdapUserVO;

/**
 * 系統表單 基礎類別
 *
 * @author adam.yeh
 */
public class BaseFormVO extends BaseVO {

    // 表頭欄位
    private String formId;                   // 表單編號
    private String formWording;              // 表單編號
    private String formStatus;               // 表單狀態
    private String sourceId;                 // 來源表單
    private String processStatus;            // 流程狀態
    private String statusWording;            // 表單狀態文字
    private String formStatusWording;        // 表單狀態文字 Tip
    private String processStatusWording;     // 流程狀態文字 Tip
    private String detailId;                 // 流程編號
    private String processName;              // 流程名稱
    private String groupSolving;             // 處理群組
    private String groupName;                // 群組名稱
    private String divisionCreated;          // 開單科別
    private String userCreated;              // 開單人員
    private String divisionSolving;          // 處理科別
    private String userSolving;              // 處理人員
    private Date createTime;                 // 建立日期
    private String formClass;                // 表單類型
    private Integer formType;                // 流程類型
    private String departmentId;             // 部門編號

    // 表單資訊頁簽-使用者資訊區塊欄位
    private String unitId;                   // 提出人員單位、負責單位
    private String questionId;               // 問題來源編號
    private String questionIdWording;        // 問題來源文字說明
    private String phone;                    // 電話
    private String email;                    // 郵件地址
    private String unitCategory;             // 提出單位分類
    private String isForward;                // 送交處理(轉開其他表單)

    // 表單資訊頁簽-明細區塊欄位
    private String systemBrand;              // 系統名稱識別碼( SystemId+_+Dpartment+MboName+Opinc+Apinc+Limit )
    private String summary;                  // 摘要
    private String content;                  // 內容、(批次頁籤)資管科工作內容
    private Date assignTime;                 // 事件發生時間、變更申請時間、表單分派時間
                                  
    // 表單資訊頁簽-日期區塊欄位
    private Date ect;                        // 預計完成時間( Estimate Complete Time )、目標解決時間、預計變更結束時間
    private Date eot;                        // 預計上線時間( Estimate Online Time )
    private Date act;                        // 實際完成時間( Actual Complete Time )、事件完成時間
    private Date ast;                        // 實際開始時間( Actual Start Time )
    private Date cat;                        // 變更申請時間 ( Change Apply Time )
    private Date cct;                        // 變更結束時間 ( Change Complete Time )
    private Date tct;                        // 測試系統完成時間 ( Testing Complete Time )
    private Date sct;                        // 連線系統完成日期 ( System Complete Time )
    private Date sit;                        // 連線系統實施日期 ( System Implement Time )
    private Date ist;                        // 中斷起始時間 ( Interrupt Start Time )
    private Date ict;                        // 中斷結束時間 ( Interrupt Complete Time )
    private Date offLineTime;                // 公告停機時間
    private Date reportTime;                 // 報告日期
    private Date excludeTime;                // 事件(當下)排除時間
    private Date observation;                // 問題單觀察期
    private String mainEvent;                // 併入主要事件單
    private String isIVR;                    // 事件來源為IVR
    private String isSpecial;                // 特殊結案
    private String isMainEvent;              // 設定為主要事件單
    private String isSameInc;                // 同一事件兩日內復發
    private String isInterrupt;              // 全部功能中斷
    private String specialEndCaseType;       // 特殊結案狀態
    private String oect;                     // 紀錄原始預計完成時間(給表單是否延期最為基準判斷)
    private String isExtended;               // 表單是否已延期
    
    // 審核欄位
    private String verifyLevel;              // 當前審核關卡
    private String parallel;                 // 平行會辦群組
    private String isParallel;               // 是否為平行會辦關卡
    private String jumpLevel;                // 跳關關卡
    private String verifyType;               // 審核類型
    private Date submitTime;                 // 送出時間
    private Date completeTime;               // 完成時間
    private String userId;                   // 審核人員、提出人員編號、(表單資訊)系統負責人
    private String groupId;                  // 關卡群組編號
    private String userName;                 // 審核人員名稱、提出人員名稱、(表單資訊)系統負責人名稱
    private String verifyResult;             // 審核結果
    private String verifyResultWording;      // 審核結果 外顯值
    private String verifyComment;            // 審核意見
    private String isAddQuestionIssue;       // 是否可開問題單
    private String isCreateChangeIssue;      // 是否可開變更單
    private String isCreateCIssue;           // 是否可開會辦單
    private String isModifyColumnData;       // 是否具有編輯表單權
    private String isWaitForSubIssueFinish;  // 是否等待衍生單完成
    private String isCloseForm;              // 是否直接結案
    private String impactThreshold;          // 衝擊分析門檻總分數
    private String isApprover;               // 是否為審核者
    private String isApplyLastLevel;         // 是否為申請最後一關
    private boolean isNextLevel;             // 是否為向下跳關
    private boolean isVerifyAcceptable;      // 是否具有審核權
    private boolean isBackToApply;           // 是否由審核階段退回至申請階段
    
    // 處理方案
    private String isSuggestCase;            // 是否建議加入處理方案、暫時性解決方案，且無法於事件目標解決時間內根本解決者
    private String indication;               // 征兆
    private String reason;                   // 原因、(批次頁籤)申請原因
    private String processProgram;           // 處理方案
    private String deprecatedReason;         // 作廢原因
                                             
    private String isWorkLevel;              // 是否為作業流程
    private List<String> recipients;         // 收件者的UserId
    
    private String modifyComment;            // 修改原因
    private String isDifferentScope;         // 是否選擇「此單之變更範圍，與原來不同，或增加會辦科。」
    private boolean isSave;                  // 是否以儲存按鈕將資料帶入後端
    private boolean isServerSideUpdated;     // 是否直接在伺服器端改寫資料庫資料並帶回前端更新前端暫存資料
    private boolean isSuperVice;             // 是否為特殊副科權限
    private List<LdapUserVO> mailList;

    public String getFormClass () {
        return formClass;
    }

    public void setFormClass (String formClass) {
        this.formClass = formClass;
    }

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getFormStatus () {
        return formStatus;
    }

    public void setFormStatus (String formStatus) {
        this.formStatus = formStatus;
    }

    public String getProcessName () {
        return processName;
    }

    public void setProcessName (String processName) {
        this.processName = processName;
    }

    public String getGroupSolving () {
        return groupSolving;
    }

    public void setGroupSolving (String groupSolving) {
        this.groupSolving = groupSolving;
    }

    public String getProcessStatus () {
        return processStatus;
    }

    public void setProcessStatus (String processStatus) {
        this.processStatus = processStatus;
    }

    public String getDivisionCreated () {
        return divisionCreated;
    }

    public void setDivisionCreated (String divisionCreated) {
        this.divisionCreated = divisionCreated;
    }

    public String getUserCreated () {
        return userCreated;
    }

    public void setUserCreated (String userCreated) {
        this.userCreated = userCreated;
    }

    public String getDivisionSolving () {
        return divisionSolving;
    }

    public void setDivisionSolving (String divisionSolving) {
        this.divisionSolving = divisionSolving;
    }

    public String getUserSolving () {
        return userSolving;
    }

    public void setUserSolving (String userSolving) {
        this.userSolving = userSolving;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }

    public String getVerifyLevel () {
        return verifyLevel;
    }

    public void setVerifyLevel (String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getVerifyResult () {
        return verifyResult;
    }

    public void setVerifyResult (String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getVerifyComment () {
        return verifyComment;
    }

    public void setVerifyComment (String verifyComment) {
        this.verifyComment = verifyComment;
    }

    public String getUnitId () {
        return unitId;
    }

    public void setUnitId (String unitId) {
        this.unitId = unitId;
    }

    public String getQuestionId () {
        return questionId;
    }

    public void setQuestionId (String questionId) {
        this.questionId = questionId;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getUnitCategory () {
        return unitCategory;
    }

    public void setUnitCategory (String unitCategory) {
        this.unitCategory = unitCategory;
    }

    public String getIsForward () {
        return isForward;
    }

    public void setIsForward (String isForward) {
        this.isForward = isForward;
    }

    public Date getEct () {
        return ect;
    }

    public void setEct (Date ect) {
        this.ect = ect;
    }

    public Date getEot () {
        return eot;
    }

    public void setEot (Date eot) {
        this.eot = eot;
    }

    public Date getAct () {
        return act;
    }

    public void setAct (Date act) {
        this.act = act;
    }

    public Date getAst () {
        return ast;
    }

    public void setAst (Date ast) {
        this.ast = ast;
    }

    public Date getReportTime () {
        return reportTime;
    }

    public void setReportTime (Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getExcludeTime () {
        return excludeTime;
    }

    public void setExcludeTime (Date excludeTime) {
        this.excludeTime = excludeTime;
    }

    public Date getObservation () {
        return observation;
    }

    public void setObservation (Date observation) {
        this.observation = observation;
    }

    public String getMainEvent () {
        return mainEvent;
    }

    public void setMainEvent (String mainEvent) {
        this.mainEvent = mainEvent;
    }

    public String getIsSpecial () {
        return isSpecial;
    }

    public void setIsSpecial (String isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getIsMainEvent () {
        return isMainEvent;
    }

    public void setIsMainEvent (String isMainEvent) {
        this.isMainEvent = isMainEvent;
    }

    public String getIsInterrupt () {
        return isInterrupt;
    }

    public void setIsInterrupt (String isInterrupt) {
        this.isInterrupt = isInterrupt;
    }

    public String getDetailId () {
        return detailId;
    }

    public void setDetailId (String detailId) {
        this.detailId = detailId;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public String getFormStatusWording () {
        return formStatusWording;
    }

    public void setFormStatusWording (String formStatusWording) {
        this.formStatusWording = formStatusWording;
    }

    public String getProcessStatusWording () {
        return processStatusWording;
    }

    public void setProcessStatusWording (String processStatusWording) {
        this.processStatusWording = processStatusWording;
    }

    public Integer getFormType () {
        return formType;
    }

    public void setFormType (Integer formType) {
        this.formType = formType;
    }

    public String getVerifyType () {
        return verifyType;
    }

    public void setVerifyType (String verifyType) {
        this.verifyType = verifyType;
    }

    public String getVerifyResultWording () {
        return verifyResultWording;
    }

    public void setVerifyResultWording (String verifyResultWording) {
        this.verifyResultWording = verifyResultWording;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getJumpLevel () {
        return jumpLevel;
    }

    public void setJumpLevel (String jumpLevel) {
        this.jumpLevel = jumpLevel;
    }

    public boolean getIsNextLevel () {
        return isNextLevel;
    }

    public void setIsNextLevel (boolean isNextLevel) {
        this.isNextLevel = isNextLevel;
    }

    public String getFormWording () {
        return formWording;
    }

    public void setFormWording (String formWording) {
        this.formWording = formWording;
    }

    public String getIsCreateChangeIssue () {
        return isCreateChangeIssue;
    }

    public void setIsCreateChangeIssue (String isCreateChangeIssue) {
        this.isCreateChangeIssue = isCreateChangeIssue;
    }

    public String getIsCreateCIssue () {
        return isCreateCIssue;
    }

    public void setIsCreateCIssue (String isCreateCIssue) {
        this.isCreateCIssue = isCreateCIssue;
    }

    public String getStatusWording () {
        return statusWording;
    }

    public void setStatusWording (String statusWording) {
        this.statusWording = statusWording;
    }

    public String getSummary () {
        return summary;
    }

    public void setSummary (String summary) {
        this.summary = summary;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public boolean getIsVerifyAcceptable () {
        return isVerifyAcceptable;
    }

    public void setIsVerifyAcceptable (boolean isVerifyAcceptable) {
        this.isVerifyAcceptable = isVerifyAcceptable;
    }

    public Date getCompleteTime () {
        return completeTime;
    }

    public void setCompleteTime (Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getIsModifyColumnData () {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData (String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }
    
    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProcessProgram() {
        return processProgram;
    }

    public void setProcessProgram(String processProgram) {
        this.processProgram = processProgram;
    }

    public String getDeprecatedReason() {
        return deprecatedReason;
    }

    public void setDeprecatedReason(String deprecatedReason) {
        this.deprecatedReason = deprecatedReason;
    }

    public String getIsIVR () {
        return isIVR;
    }

    public void setIsIVR (String isIVR) {
        this.isIVR = isIVR;
    }

    public Date getAssignTime () {
        return assignTime;
    }

    public void setAssignTime (Date assignTime) {
        this.assignTime = assignTime;
    }

    public boolean getIsBackToApply () {
        return isBackToApply;
    }

    public void setIsBackToApply (boolean isBackToApply) {
        this.isBackToApply = isBackToApply;
    }

    public String getSpecialEndCaseType() {
        return specialEndCaseType;
    }

    public void setSpecialEndCaseType(String specialEndCaseType) {
        this.specialEndCaseType = specialEndCaseType;
    }
    
    public String getIsSuggestCase() {
        return isSuggestCase;
    }

    public void setIsSuggestCase(String isSuggestCase) {
        this.isSuggestCase = isSuggestCase;
    }

    public Date getCat() {
        return cat;
    }

    public void setCat(Date cat) {
        this.cat = cat;
    }

    public Date getCct() {
        return cct;
    }

    public void setCct(Date cct) {
        this.cct = cct;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getSubmitTime () {
        return submitTime;
    }

    public void setSubmitTime (Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }

    public String getIsCloseForm() {
        return isCloseForm;
    }

    public void setIsCloseForm(String isCloseForm) {
        this.isCloseForm = isCloseForm;
    }

    public Date getTct () {
        return tct;
    }

    public void setTct (Date tct) {
        this.tct = tct;
    }

    public Date getSct () {
        return sct;
    }

    public void setSct (Date sct) {
        this.sct = sct;
    }

    public Date getSit () {
        return sit;
    }

    public void setSit (Date sit) {
        this.sit = sit;
    }

    public Date getIst () {
        return ist;
    }

    public void setIst (Date ist) {
        this.ist = ist;
    }

    public Date getIct () {
        return ict;
    }

    public void setIct (Date ict) {
        this.ict = ict;
    }

    public Date getOffLineTime () {
        return offLineTime;
    }

    public void setOffLineTime (Date offLineTime) {
        this.offLineTime = offLineTime;
    }

    public void setNextLevel (boolean isNextLevel) {
        this.isNextLevel = isNextLevel;
    }

    public void setVerifyAcceptable (boolean isVerifyAcceptable) {
        this.isVerifyAcceptable = isVerifyAcceptable;
    }

    public void setBackToApply (boolean isBackToApply) {
        this.isBackToApply = isBackToApply;
    }

    public String getImpactThreshold() {
        return impactThreshold;
    }

    public void setImpactThreshold(String impactThreshold) {
        this.impactThreshold = impactThreshold;
    }

    public String getIsWorkLevel() {
        return isWorkLevel;
    }

    public void setIsWorkLevel(String isWorkLevel) {
        this.isWorkLevel = isWorkLevel;
    }

    public String getIsAddQuestionIssue() {
        return isAddQuestionIssue;
    }

    public void setIsAddQuestionIssue(String isAddQuestionIssue) {
        this.isAddQuestionIssue = isAddQuestionIssue;
    }

    public String getIsApprover() {
        return isApprover;
    }

    public void setIsApprover(String isApprover) {
        this.isApprover = isApprover;
    }

    public String getSystemBrand () {
        return systemBrand;
    }

    public void setSystemBrand (String systemBrand) {
        this.systemBrand = systemBrand;
    }

    public String getIsApplyLastLevel () {
        return isApplyLastLevel;
    }

    public void setIsApplyLastLevel (String isApplyLastLevel) {
        this.isApplyLastLevel = isApplyLastLevel;
    }

    /**
     * @return the questionIdWording
     */
    public String getQuestionIdWording() {
        return questionIdWording;
    }

    /**
     * @param questionIdWording the questionIdWording to set
     */
    public void setQuestionIdWording(String questionIdWording) {
        this.questionIdWording = questionIdWording;
    }

    public String getParallel () {
        return parallel;
    }

    public void setParallel (String parallel) {
        this.parallel = parallel;
    }

    public String getIsParallel () {
        return isParallel;
    }

    public void setIsParallel (String isParallel) {
        this.isParallel = isParallel;
    }

    public List<String> getRecipients () {
        return recipients;
    }

    public void setRecipients (List<String> recipients) {
        this.recipients = recipients;
    }

    public String getDepartmentId () {
        return departmentId;
    }

    public void setDepartmentId (String departmentId) {
        this.departmentId = departmentId;
    }

    public String getModifyComment() {
        return modifyComment;
    }

    public void setModifyComment(String modifyComment) {
        this.modifyComment = modifyComment;
    }
    
    public List<LdapUserVO> getMailList() {
        return mailList;
    }

    public void setMailList(List<LdapUserVO> mailList) {
        this.mailList = mailList;
    }

    public boolean getIsSave () {
        return isSave;
    }

    public void setIsSave (boolean isSave) {
        this.isSave = isSave;
    }

    public String getIsSameInc () {
        return isSameInc;
    }

    public void setIsSameInc (String isSameInc) {
        this.isSameInc = isSameInc;
    }

    public String getOect() {
        return oect;
    }

    public void setOect(String oect) {
        this.oect = oect;
    }

    public String getIsExtended() {
        return isExtended;
    }

    public void setIsExtended(String isExtended) {
        this.isExtended = isExtended;
    }

    public boolean getIsServerSideUpdated () {
        return isServerSideUpdated;
    }

    public void setIsServerSideUpdated (boolean isServerSideUpdated) {
        this.isServerSideUpdated = isServerSideUpdated;
    }

    public boolean getIsSuperVice() {
        return isSuperVice;
    }

    public void setIsSuperVice(boolean isSuperVice) {
        this.isSuperVice = isSuperVice;
    }

    public String getIsDifferentScope() {
        return isDifferentScope;
    }

    public void setIsDifferentScope(String isDifferentScope) {
        this.isDifferentScope = isDifferentScope;
    }
    
}