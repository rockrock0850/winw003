package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 知識庫 資料物件
 * @author adam.yeh
 */
public class KnowledgeFormVO extends BaseFormVO {

    // 明細欄位
    private String systemId;        // 系統編號
    private String system;          // 系統名稱
    private String assetGroup;      // 資訊資產群組
    private String sClass;          // 服務類別
    private String sSubClass;       // 服務子類別
    private String knowledges;      // 問題根因
    private String solutions;       // 建議解決方案
    private String flowName;        // 流程別(與表單類別一致)
    private String flowNameDisplay; // 流程別(與表單類別一致)
    private String isEnabled;       // 是否啟用
    
    private boolean isDetails;      // 是否要找表單內容的相關處理方案清單

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

    public String getKnowledges () {
        return knowledges;
    }

    public void setKnowledges (String knowledges) {
        this.knowledges = knowledges;
    }

    public String getSolutions () {
        return solutions;
    }

    public void setSolutions (String solutions) {
        this.solutions = solutions;
    }

    public boolean getIsDetails () {
        return isDetails;
    }

    public void setIsDetails (boolean isDetails) {
        this.isDetails = isDetails;
    }

    public String getFlowName () {
        return flowName;
    }

    public void setFlowName (String flowName) {
        this.flowName = flowName;
    }

    public String getIsEnabled () {
        return isEnabled;
    }

    public void setIsEnabled (String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getFlowNameDisplay () {
        return flowNameDisplay;
    }

    public void setFlowNameDisplay (String flowNameDisplay) {
        this.flowNameDisplay = flowNameDisplay;
    }

}
