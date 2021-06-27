package com.ebizprise.winw.project.enums.form;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ebizprise.winw.project.enums.UserEnum;

/**
 * 記錄表單相關常數
 * 
 * @author adam.yeh
 */
public enum FormEnum implements Form {
    
/* 表單類型
============================== */
    DEFAULT("", 0, ""),

    /**
     * 問題知識庫(棄用)
     */
    QA("", 0, ""),
    
    /**
     * 需求單
     */
    SR("SR-", 1, "requirement.form"),
    
    /**
     * 需求會辦單
     */
    SR_C("SC-", 2, "requirement.countersigned.form"),

    /**
     * 問題單
     */
    Q("Q-", 3, "question.form"),
    
    /**
     * 問題會辦單
     */
    Q_C("QC-", 4, "question.countersigned.form"),

    /**
     * 事件單
     */
    INC("INC-", 5, "event.form"),
    
    /**
     * 事件會辦單
     */
    INC_C("IC-", 6, "event.countersigned.form"),
    
    /**
     * 變更單
     */
    CHG("CHG-", 7, "change.form"),
    
    /**
     * 工作單
     */
    JOB("JOB-", 8, "work.form"),

    /**
     * 系統科工作單
     */
    JOB_SP("JOB-", 8, "work.form"),

    /**
     * 非系統科工作單
     */
    JOB_AP("JOB-", 8, "work.form"),
    
    /**
     * 工作會辦單
     */
    JOB_C("JC-", 9, "work.countersigned.form"),
    
    /**
     * AP工作會辦單
     */
    JOB_AP_C("JC-", 9, "work.countersigned.form"),
    
    /**
     * SP工作會辦單
     */
    JOB_SP_C("JC-", 9, "work.countersigned.form"),
    
    /**
     * 批次作業中斷對策表管理
     */
    BA("BA-", 10, "batchinterrupt.form"),
    
    /**
     * 知識庫<br>
     * 沒有流程所以流程編號設0
     */
    KL("K-", 0, "knowledge.form"),
/* =========================== */
    
/* 表單狀態
============================== */
    /**
     * [流程群組] 擬訂中
     */
    PROPOSING("擬訂中", "%s 擬訂中", "%s 擬訂中"),

    /**
     * 待 [流程群組] 核可
     */
    APPROVING("審核中", "待 %s 審核中", "%s 審核中"),

    /**
     * [xxx_經辦] 處理中
     */
    CHARGING("%s", "%s 處理中", "%s 處理中"),

    /**
     * [流程群組] 已結案
     */
    CLOSED("已結案", "已結案", "%s 結案"),

    /**
     * [流程群組] 已作廢
     */
    DEPRECATED("已作廢", "已作廢", "%s 作廢"),
    
    /**
     * 人員指派中
     */
    ASSIGNING("人員指派中", "待分派", ""),
    
    /**
     * 自行處理中
     */
    SELFSOLVE("自行處理中", "%s 處理", ""),
    
    /**
     * 觀察中(問題單特有)
     */
    WATCHING("觀察中", "觀察中", "%s 觀察中"),
    
/* =========================== */
    
/* 申請/審核狀態
============================== */
    SENT("發送"),
    AGREED("通過"),
    PENDING("待審"),
    DISAGREED("退回"),
    JUMP("跳關"),
    CLOSE_FORM("直接結案"),
    VSC_MODIFY("副科修改"),
    VSC_PROXY1("代科長審核"),
    VSC_PROXY2("代副理審核"),
/* =========================== */
    
/* 附件/執行動作
============================== */
    ADD("新增"),
    DELETE("刪除"),
/* =========================== */
    
/* 申請/審核類型
============================== */
    /**
     * 申請流程
     */
    APPLY("申請中"),
    
    /**
     * 審核流程
     */
    REVIEW("審核中"),
/* =========================== */
    
/* 衝擊分析
============================== */
    /**
     * 因應措施
     */
    S,
    
    /**
     * 影響評估
     */
    E,
    
    /**
     * 總分
     */
    T;
/* =========================== */

    private static final Map<String, FormEnum> BY_NAME = new HashMap<>();
    private static final Map<String, FormEnum> BY_PREFIX = new HashMap<>();
    private static final Map<Integer, FormEnum> BY_FORM_TYPE = new HashMap<>();
    
    private String status;
    private String prefix;
    private Integer formType;
    private String i18nKey;
    private String formStatus;
    private String verifyType;
    private String processStatus;

    static {
        for (FormEnum e : values()) {
            BY_NAME.put(e.name(), e);
            
            if (e.prefix != null) {
                BY_PREFIX.put(e.prefix, e);
            }
            
            if (e.formType != null) {
                BY_FORM_TYPE.put(e.formType, e);
            }
        }
    }
    
    public static boolean isContaining (String name) {
        return BY_NAME.containsKey(name);
    }
    
    public static FormEnum valueOfFormType (Integer type) {
        return BY_FORM_TYPE.get(type);
    }
    
    public static FormEnum valueOfPrefix (String prefix) {
        return BY_PREFIX.get(prefix);
    }

    private FormEnum () {
    }

    private FormEnum (String verifyType) {
        this.verifyType = verifyType;
    }
    
    private FormEnum (String status, String formStatus, String processStatus) {
        this.status = status;
        this.formStatus = formStatus;
        this.processStatus = processStatus;
    }

    private FormEnum (String prefix, Integer formType, String i18nKey) {
        this.prefix = prefix;
        this.formType = formType;
        this.i18nKey = i18nKey;
    }
    
    /**
     * 表單狀態
     * 
     * @return
     */
    public String status () {
        return this.status;
    }

    /**
     * 表單前綴
     * 
     * @return
     */
    public String prefix () {
        return this.prefix;
    }
    
    /**
     * 流程型態
     * 
     * @return
     */
    public Integer formType () {
        return this.formType;
    }
    
    /**
     * 多國語言參數
     * 
     * @return
     */
    public String wording () {
        return this.i18nKey;
    }
    
    /**
     * 流程狀態文字 & Tip
     * 
     * @return
     */
    public String processStatus () {
        return this.processStatus;
    }
    
    /**
     * 表單 申請/審核 狀態或類型
     * 
     * @return
     */
    public String verifyType () {
        return this.verifyType;
    }

    /**
     * 表單狀態文字 & Tip
     * 
     * @return
     */
    public String formStatus (String groupName) {
        String stat = this.formStatus;
        
        if (this != FormEnum.CLOSED && this != FormEnum.DEPRECATED && this != FormEnum.WATCHING) {
            String[] split = StringUtils.split(groupName, "_");
            
            if ((this != FormEnum.PROPOSING) &&
                (!StringUtils.contains(groupName, UserEnum.DIVISION_CHIEF.wording()) &&
                !StringUtils.contains(groupName, UserEnum.DEPUTY_MANAGER.wording()) &&
                !StringUtils.contains(groupName, UserEnum.ASSISTANT_MANAGER.wording()) &&
                !StringUtils.contains(groupName, UserEnum.VICE_DIVISION_CHIEF.wording()))) {
                stat = FormEnum.CHARGING.formStatus;
            }
            
            if (split != null && split.length > 1) {
                groupName = split[1];
            }
        }
        
        return String.format(stat, groupName == null ? "" : groupName);
    }
    
    public boolean equalsAny(FormEnum... enums) {
    	boolean result = false;
    	if (enums != null && enums.length > 0) {
    		for (FormEnum e : enums) {
    			if (this.equals(e)) {
    				result = true;
    				break;
    			}
    		}
    	}
    	return result;
    }
    
}
