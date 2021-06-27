package com.ebizprise.winw.project.vo;

import java.util.List;

/**
 * 表單 衝擊分析 VO
 * @author andrew.lee
 * @version 1.0, Created at 2019年8月7日
 */
public class FormImpactAnalysisVO {

    private Long id;
    private String formId;//表單ID
    private String questionType;//問題類型
    private String content;//內容
    private String targetFraction;//當前分數、總分
    private String description;//描述
    private String fraction;//分數(DB中取出來的原資料,多個分數之間以 ; 區隔)
    private String isValidateFraction;//是否需要檢核分數
    private List<String> fractionLs;//分數(fraction轉換過後的List物件)
    private String isAddUp; // 衝擊分析分數是否需要加總
    
    private List<FormImpactAnalysisVO> impactList;//多筆衝擊分析資訊,用於保存

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTargetFraction() {
        return targetFraction;
    }

    public void setTargetFraction(String targetFraction) {
        this.targetFraction = targetFraction;
    }

    public List<String> getFractionLs() {
        return fractionLs;
    }

    public void setFractionLs(List<String> fractionLs) {
        this.fractionLs = fractionLs;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FormImpactAnalysisVO> getImpactList() {
        return impactList;
    }

    public void setImpactList(List<FormImpactAnalysisVO> impactList) {
        this.impactList = impactList;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getIsValidateFraction() {
        return isValidateFraction;
    }

    public void setIsValidateFraction(String isValidateFraction) {
        this.isValidateFraction = isValidateFraction;
    }

    public String getIsAddUp() {
        return isAddUp;
    }

    public void setIsAddUp(String isAddUp) {
        this.isAddUp = isAddUp;
    }

}
