package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseResponseVO;

/**
 * 查詢工作單狀態的資料物件
 * @author adam.yeh
 */
public class JobInfoVO extends BaseResponseVO {
    
    private String fn; // 表單編號

    /*
     * 0 = 無此單號
     * 1 = 查詢成功
     */
    private String qyStatus;
    
    /*
     * 狀態代號(及說明) : 0 = 不是、1 = 是
     * ※滿足以下條件的表單皆等於1。
     * (1)擬訂中、(2)申請第1關、(3)已作廢
     */
    private String fnStatus;

    public String getFn () {
        return fn;
    }

    public void setFn (String fn) {
        this.fn = fn;
    }

    public String getFnStatus () {
        return fnStatus;
    }

    public void setFnStatus (String fnStatus) {
        this.fnStatus = fnStatus;
    }

    public String getQyStatus () {
        return qyStatus;
    }

    public void setQyStatus (String qyStatus) {
        this.qyStatus = qyStatus;
    }

}
