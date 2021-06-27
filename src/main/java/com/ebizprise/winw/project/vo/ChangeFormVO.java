package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 變更單 Value Object
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年7月29日
 */
public class ChangeFormVO extends BaseFormVO {
    
    private String effectSystem;                //變更影響之系統
    private String cCategory;                   //組態分類
    private String cClass;                      //組態元件類別
    private String cComponent;                  //組態元件
    private String standard;                    //標準變更作業
    private String changeType;                  //變更類型
    private String changeRank;                  //變更等級
    private String isNewSystem;                 //是新系統
    private String isNewService;                //新服務暨重大服務
    private String isUrgent;                    //緊急變更
    private String isEffectField;               //有新增異動欄位影響到資料倉儲系統產出資料
    private String isEffectAccountant;          //有新增異動會計科目影響到資料倉儲系統產出資料
    private String totalFraction;               //衝擊分析 總分
    private String isCreateJobIssue;            //可否產生工作單
    private String isBusinessImpactAnalysis;    //是否協助衝擊分析判斷
    private String solution;                    //衝擊分析-因應措施
    private String evaluation;                  //衝擊分析-影響評估
    private String isScopeChanged;              //變更範圍是否不同
    private String isModifyProgram;             //未有修改程式 
    private boolean isFromCountersign;          //是否來自會辦單
    private boolean isBackToApplyLevel1;        //是否回到申請等級一
    
    private ChangeFormVO impactForm;
    private List<BaseFormVO> verifyLogs;        //審核歷程

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

    public String getEffectSystem() {
        return effectSystem;
    }

    public void setEffectSystem(String effectSystem) {
        this.effectSystem = effectSystem;
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

    public List<BaseFormVO> getVerifyLogs() {
        return verifyLogs;
    }

    public void setVerifyLogs(List<BaseFormVO> list) {
        this.verifyLogs = list;
    }

    public String getTotalFraction() {
        return totalFraction;
    }

    public void setTotalFraction(String totalFraction) {
        this.totalFraction = totalFraction;
    }

    public boolean getIsFromCountersign() {
        return isFromCountersign;
    }

    public void setIsFromCountersign(boolean isFromCountersign) {
        this.isFromCountersign = isFromCountersign;
    }

    public String getIsCreateJobIssue () {
        return isCreateJobIssue;
    }

    public void setIsCreateJobIssue (String isCreateJobIssue) {
        this.isCreateJobIssue = isCreateJobIssue;
    }

    public String getIsBusinessImpactAnalysis() {
        return isBusinessImpactAnalysis;
    }

    public void setIsBusinessImpactAnalysis(String isBusinessImpactAnalysis) {
        this.isBusinessImpactAnalysis = isBusinessImpactAnalysis;
    }

    public boolean isBackToApplyLevel1() {
        return isBackToApplyLevel1;
    }

    public void setIsBackToApplyLevel1(boolean isBackToApplyLevel1) {
        this.isBackToApplyLevel1 = isBackToApplyLevel1;
    }

    public String getSolution () {
        return solution;
    }

    public void setSolution (String solution) {
        this.solution = solution;
    }

    public String getEvaluation () {
        return evaluation;
    }

    public void setEvaluation (String evaluation) {
        this.evaluation = evaluation;
    }

    public ChangeFormVO getImpactForm () {
        return impactForm;
    }

    public void setImpactForm (ChangeFormVO impactForm) {
        this.impactForm = impactForm;
    }

    public String getIsScopeChanged () {
        return isScopeChanged;
    }

    public void setIsScopeChanged (String isScopeChanged) {
        this.isScopeChanged = isScopeChanged;
    }

    public String getIsModifyProgram() {
        return isModifyProgram;
    }

    public void setIsModifyProgram(String isModifyProgram) {
        this.isModifyProgram = isModifyProgram;
    }

}
