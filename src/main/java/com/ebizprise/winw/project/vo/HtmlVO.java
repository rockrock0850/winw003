package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 儲存因登入者資訊而變動的前端元件內容值
 * 
 * @author adam.yeh
 */
public class HtmlVO extends BaseVO {
    
    private String formId;
    private String userId;
    private String formClass;

    // 下拉選單 ( Selector ) / 選取方塊 ( checkbox )
    private String optionId;           // 下拉選單編號/選取方塊名稱
    private String value;              // 內顯值
    private String wording;            // 外顯值
    private String display;            // 外顯值
    private String isKnowledge;        // 是否加入知識庫 (Y/N)
    
    // 簽核清單欄位
    private String detailId;
    private String groupId;
    private String groupName;
    private String formType;
    private Integer processOrder;
    
    // 系統名稱清單
    private String systemBrand;        // 系統名稱識別碼
    private String systemId;           // 系統編號
    private String systemName;         // 系統中文說明
    private String department;         // 部門代號
    private String description;        // 系統描述
    private String mboName;            // 表單類型
    private String limit;              // 臨界值(極限值)
    private String mark;               // 系統名稱_資訊資產群組，「對應值」
	private String opinc;			   // 未知
	private String apinc;			   // 未知
	private String active;			   // 狀態
    
    // 表單資訊頁簽-會辦科區塊
    private String countersigneds; 	   // 會辦科清單
    
    // 處理人員清單
    private boolean isAll;  		   // 是否找所有已啟用之處理人員清單
    
    // 工作要項
    private String workingItemId;      // 工作要項識別碼( WorkingItemName+_+Working+_+IsImpact+_+IsReview )
    private String workingItemName;    // 工作要項名稱
    private String spGroup;            // 系統科組別
    private String isImpact;           // 變更衝擊分析
    private String isReview;           // 變更覆核
    
    // 平行會辦群組
    private String subGroup;           // 會辦系統科群組
    private List<String> groupIdList;
    
    private String keyword; 		   // 查詢關鍵字
    

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getWording () {
        return wording;
    }

    public void setWording (String wording) {
        this.wording = wording;
    }

    public String getDetailId () {
        return detailId;
    }

    public void setDetailId (String detailId) {
        this.detailId = detailId;
    }

    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public String getFormType () {
        return formType;
    }

    public void setFormType (String formType) {
        this.formType = formType;
    }

    public Integer getProcessOrder () {
        return processOrder;
    }

    public void setProcessOrder (Integer processOrder) {
        this.processOrder = processOrder;
    }

    public String getOptionId () {
        return optionId;
    }

    public void setOptionId (String optionId) {
        this.optionId = optionId;
    }

    public String getSystemBrand () {
        return systemBrand;
    }

    public void setSystemBrand (String systemBrand) {
        this.systemBrand = systemBrand;
    }

    public String getSystemId () {
        return systemId;
    }

    public void setSystemId (String systemId) {
        this.systemId = systemId;
    }

    public String getDepartment () {
        return department;
    }

    public void setDepartment (String department) {
        this.department = department;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getMboName () {
        return mboName;
    }

    public void setMboName (String mboName) {
        this.mboName = mboName;
    }

    public String getSystemName () {
        return systemName;
    }

    public void setSystemName (String systemName) {
        this.systemName = systemName;
    }

    public String getCountersigneds () {
        return countersigneds;
    }

    public void setCountersigneds (String countersigneds) {
        this.countersigneds = countersigneds;
    }

    public boolean getIsAll () {
        return isAll;
    }

    public void setIsAll (boolean isAll) {
        this.isAll = isAll;
    }

    public String getWorkingItemId() {
        return workingItemId;
    }
    
    public void setWorkingItemId(String workingItemId) {
        this.workingItemId = workingItemId;
    }
    
    public String getWorkingItemName() {
        return workingItemName;
    }
    
    public void setWorkingItemName(String workingItemName) {
        this.workingItemName = workingItemName;
    }
    
    public String getSpGroup() {
        return spGroup;
    }
    
    public void setSpGroup(String spGroup) {
        this.spGroup = spGroup;
    }
    
    public String getIsImpact() {
        return isImpact;
    }
    
    public void setIsImpact(String isImpact) {
        this.isImpact = isImpact;
    }
    
    public String getIsReview() {
        return isReview;
    }
    
    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLimit () {
        return limit;
    }

    public void setLimit (String limit) {
        this.limit = limit;
    }

    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }

    public List<String> getGroupIdList () {
        return groupIdList;
    }

    public void setGroupIdList (List<String> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public String getDisplay () {
        return display;
    }

    public void setDisplay (String display) {
        this.display = display;
    }

    public String getSubGroup () {
        return subGroup;
    }

    public void setSubGroup (String subGroup) {
        this.subGroup = subGroup;
    }

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getFormClass () {
        return formClass;
    }

    public void setFormClass (String formClass) {
        this.formClass = formClass;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getIsKnowledge () {
        return isKnowledge;
    }

    public void setIsKnowledge (String isKnowledge) {
        this.isKnowledge = isKnowledge;
    }

    public String getOpinc() {
        return opinc;
    }

    public void setOpinc(String opinc) {
        this.opinc = opinc;
    }

    public String getApinc() {
        return apinc;
    }

    public void setApinc(String apinc) {
        this.apinc = apinc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
}
