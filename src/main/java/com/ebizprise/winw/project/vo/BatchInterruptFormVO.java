package com.ebizprise.winw.project.vo;

import java.util.Date;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 批次作業中斷對策表管理 資料物件
 * 
 * @author Bernard.Yu
 */
public class BatchInterruptFormVO extends BaseFormVO {
    
    // 明細欄位
    private String batchName;                       // 批次工作名稱
    private String division;                        // 負責科
    private String executeTime;                     // 執行時間
    private String dbInUse;                         // 使用資料庫
    private Date effectDate;                        // 生效日期
    private List<BatchInterruptFormVO> verifyLogs;  // 審核歷程
    private boolean isBackToApplyLevel1;            // 是否回到申請等級一
    
    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDivision () {
        return division;
    }
    
    public void setDivision (String division) {
        this.division = division;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getDbInUse() {
        return dbInUse;
    }

    public void setDbInUse(String dbInUse) {
        this.dbInUse = dbInUse;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public List<BatchInterruptFormVO> getVerifyLogs () {
        return verifyLogs;
    }

    public void setVerifyLogs (List<BatchInterruptFormVO> verifyLogs) {
        this.verifyLogs = verifyLogs;
    }

    public boolean isBackToApplyLevel1() {
        return isBackToApplyLevel1;
    }

    public void setIsBackToApplyLevel1(boolean isBackToApplyLevel1) {
        this.isBackToApplyLevel1 = isBackToApplyLevel1;
    }

}
