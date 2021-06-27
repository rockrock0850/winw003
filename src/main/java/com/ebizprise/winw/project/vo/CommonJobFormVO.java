/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 工作會辦單資料物件
 * 
 * @author emily.lai
 * @version 1.0, Created at 2019/09/11
 */
public class CommonJobFormVO extends BaseFormVO {
    
    // 共用
    private String other;                 // 其他事項、其他(批次)
    private String isRollback;            // 回復到前一版(Y/N)
    private boolean isApTabs;             // 是否要取得Ap工作單的頁簽
                                          
    // 明細欄位                           
    private String division;              // 會辦單科別
    private String dataType;              // 資料型態
    private String description;           // 工作說明
    private String book;                  // Book
    private String bookNumber;            // Book支數
    private String only;                  // Compile Only
    private String onlyNumber;            // Compile Only程式支數
    private String onlyCode;              // Compile Only Return_Code
    private String link;                  // Compile Link
    private String linkNumber;            // Compile Link程式支數
    private String linkCode;              // Compile Link Return_Code
    private String linkOnly;              // Link Only
    private String linkOnlyNumber;        // Link Only程式支數
    private String rollbackDesc;          // 回復作業-其他
    private String isTest;                // 工作單的系統頁簽上的Test check box
    private String isProduction;          // 工作單的系統頁簽上的Production check box
                                          
    // 批次頁簽                           
    private String remark;                // 批次單的備註
    private String psb;                   // PSB名稱
    private String programName;           // 程式名稱
    private String programNumber;         // 程式支數
    private String jcl;                   // 執行JCL
    private String cljcl;                 // CL JCL
    private String isHelp;                // 執行HELP程式
    private String isCange;               // 批次作業流程變更
    private String isAllow;               // 允許JCB在TWS執行時，Return Code <=4視為正常
    private String isOther;               // 其他
    private String isHelpCl;              // HELP程式 CL
    private String isOtherDesc;           // 其他(綁工作內容或附件說明)
    private Date it;                      // 程式CL及工作執行日期 ( Implement Time )
    private String countersignedIDC;      // 會辦機房內容
                                          
    // DB變更、OPEN清單、程式清單頁簽     
    private String type;                  // 清單型態
    private String dbname;                // 資料庫名稱
    private String psbname;               // PSB名稱
    private String alterWay;              // 變更方式
    private String veew;                  // veew
    private String stream;                // Stream
    private String ucmproject;            // UCM Project
    private Date eit;                     // 預計實施日期 ( Estimate Implement Time )
    
    // DB變更、OPEN清單、程式清單頁簽裡面的資料庫變更清單
    private List<SegmentVO> segments;
    
    // 作業人員
    private List<CommonCheckPersonVO> checkPersonList;
	
    // 工作單會辦頁簽名稱(若有值,代表可查看該頁簽)
	private List<String> jobTabName;
    
    
    public String getDivision() {
        return division;
    }
    
    public void setDivision(String division) {
        this.division = division;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getBook() {
        return book;
    }
    
    public void setBook(String book) {
        this.book = book;
    }
    
    public String getBookNumber() {
        return bookNumber;
    }
    
    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }
    
    public String getOnly() {
        return only;
    }
    
    public void setOnly(String only) {
        this.only = only;
    }
    
    public String getOnlyNumber() {
        return onlyNumber;
    }
    
    public void setOnlyNumber(String onlyNumber) {
        this.onlyNumber = onlyNumber;
    }
    
    public String getOnlyCode() {
        return onlyCode;
    }
    
    public void setOnlyCode(String onlyCode) {
        this.onlyCode = onlyCode;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getLinkNumber() {
        return linkNumber;
    }
    
    public void setLinkNumber(String linkNumber) {
        this.linkNumber = linkNumber;
    }
    
    public String getLinkCode() {
        return linkCode;
    }
    
    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }
    
    public String getLinkOnly() {
        return linkOnly;
    }
    
    public void setLinkOnly(String linkOnly) {
        this.linkOnly = linkOnly;
    }
    
    public String getLinkOnlyNumber() {
        return linkOnlyNumber;
    }
    
    public void setLinkOnlyNumber(String linkOnlyNumber) {
        this.linkOnlyNumber = linkOnlyNumber;
    }
    
    public String getOther() {
        return other;
    }
    
    public void setOther(String other) {
        this.other = other;
    }
    
    public String getIsRollback() {
        return isRollback;
    }
    
    public void setIsRollback(String isRollback) {
        this.isRollback = isRollback;
    }
    
    public String getRollbackDesc() {
        return rollbackDesc;
    }
    
    public void setRollbackDesc(String rollbackDesc) {
        this.rollbackDesc = rollbackDesc;
    }

    public String getIsTest () {
        return isTest;
    }

    public void setIsTest (String isTest) {
        this.isTest = isTest;
    }

    public String getIsProduction () {
        return isProduction;
    }

    public void setIsProduction (String isProduction) {
        this.isProduction = isProduction;
    }

    public Date getIt () {
        return it;
    }

    public void setIt (Date it) {
        this.it = it;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getPsb () {
        return psb;
    }

    public void setPsb (String psb) {
        this.psb = psb;
    }

    public String getProgramName () {
        return programName;
    }

    public void setProgramName (String programName) {
        this.programName = programName;
    }

    public String getProgramNumber () {
        return programNumber;
    }

    public void setProgramNumber (String programNumber) {
        this.programNumber = programNumber;
    }

    public String getJcl () {
        return jcl;
    }

    public void setJcl (String jcl) {
        this.jcl = jcl;
    }

    public String getCljcl () {
        return cljcl;
    }

    public void setCljcl (String cljcl) {
        this.cljcl = cljcl;
    }

    public String getIsHelp () {
        return isHelp;
    }

    public void setIsHelp (String isHelp) {
        this.isHelp = isHelp;
    }

    public String getIsCange () {
        return isCange;
    }

    public void setIsCange (String isCange) {
        this.isCange = isCange;
    }

    public String getIsAllow () {
        return isAllow;
    }

    public void setIsAllow (String isAllow) {
        this.isAllow = isAllow;
    }

    public String getIsOther () {
        return isOther;
    }

    public void setIsOther (String isOther) {
        this.isOther = isOther;
    }

    public String getIsHelpCl () {
        return isHelpCl;
    }

    public void setIsHelpCl (String isHelpCl) {
        this.isHelpCl = isHelpCl;
    }

    public String getIsOtherDesc () {
        return isOtherDesc;
    }

    public void setIsOtherDesc (String isOtherDesc) {
        this.isOtherDesc = isOtherDesc;
    }

    public List<CommonCheckPersonVO> getCheckPersonList () {
        return checkPersonList;
    }

    public void setCheckPersonList (List<CommonCheckPersonVO> checkPersonList) {
        this.checkPersonList = checkPersonList;
    }
    
    public List<String> getJobTabName() {
        return jobTabName;
    }

    public void setJobTabName(List<String> jobTabName) {
        this.jobTabName = jobTabName;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getDbname () {
        return dbname;
    }

    public void setDbname (String dbname) {
        this.dbname = dbname;
    }

    public String getPsbname () {
        return psbname;
    }

    public void setPsbname (String psbname) {
        this.psbname = psbname;
    }

    public String getAlterWay () {
        return alterWay;
    }

    public void setAlterWay (String alterWay) {
        this.alterWay = alterWay;
    }

    public String getVeew () {
        return veew;
    }

    public void setVeew (String veew) {
        this.veew = veew;
    }

    public String getStream () {
        return stream;
    }

    public void setStream (String stream) {
        this.stream = stream;
    }

    public String getUcmproject () {
        return ucmproject;
    }

    public void setUcmproject (String ucmproject) {
        this.ucmproject = ucmproject;
    }

    public Date getEit () {
        return eit;
    }

    public void setEit (Date eit) {
        this.eit = eit;
    }

    public List<SegmentVO> getSegments () {
        return segments;
    }

    public void setSegments (List<SegmentVO> dbList) {
        this.segments = dbList;
    }

    public boolean getIsApTabs () {
        return isApTabs;
    }

    public void setIsApTabs (boolean isApTabs) {
        this.isApTabs = isApTabs;
    }

    public String getCountersignedIDC() {
		return countersignedIDC;
	}

	public void setCountersignedIDC(String countersignedIDC) {
		this.countersignedIDC = countersignedIDC;
	}
    
}
