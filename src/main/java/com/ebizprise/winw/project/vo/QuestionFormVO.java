package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 問題單 Value Object
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年7月16日
 */
public class QuestionFormVO extends BaseFormVO {
    
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
    private String questionPriority;                // 問題優先順序
    private String effectScopeWording;              // 影響範圍說明
    private String urgentLevelWording;              // 緊急程度說明
    private String questionPriorityWording;         // 問題優先順序說明
    private String countersigneds;                  // 會辦科(CSV格式)
    private String temporary;                       // 暫時性解決方案
    private String knowledges;                      // 知識庫原因+根因類別
    private String solutions;                       // 參照知識庫
    private String isKnowledgeable;                 // 是否勾選簽核視窗的:「建議加入處理方案?」
    private boolean isBackToApplyLevel1;            // 是否回到申請等級一
    private boolean isScopeChanged;                 // 變更範圍是否不同
    private boolean isEctExtended;                  // 預計完成時間是否展延

    private List<QuestionFormVO> verifyLogs;        // 審核歷程

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getAssetGroup() {
        return assetGroup;
    }

    public void setAssetGroup(String assetGroup) {
        this.assetGroup = assetGroup;
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

    public String getQuestionPriority() {
        return questionPriority;
    }

    public void setQuestionPriority(String questionPriority) {
        this.questionPriority = questionPriority;
    }

    public String getCountersigneds() {
        return countersigneds;
    }

    public void setCountersigneds(String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public List<QuestionFormVO> getVerifyLogs() {
        return verifyLogs;
    }

    public void setVerifyLogs(List<QuestionFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
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

    public String getQuestionPriorityWording () {
        return questionPriorityWording;
    }

    public void setQuestionPriorityWording (String questionPriorityWording) {
        this.questionPriorityWording = questionPriorityWording;
    }

    public String getTemporary () {
        return temporary;
    }

    public void setTemporary (String temporary) {
        this.temporary = temporary;
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

    public String getSolutions () {
        return solutions;
    }

    public void setSolutions (String solutions) {
        this.solutions = solutions;
    }

    public String getKnowledges () {
        return knowledges;
    }

    public void setKnowledges (String knowledges) {
        this.knowledges = knowledges;
    }

    public String getIsKnowledgeable() {
        return isKnowledgeable;
    }

    public void setIsKnowledgeable(String isKnowledgeable) {
        this.isKnowledgeable = isKnowledgeable;
    }

}
