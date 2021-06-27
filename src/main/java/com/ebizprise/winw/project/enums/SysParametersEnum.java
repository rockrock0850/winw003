package com.ebizprise.winw.project.enums;

public enum SysParametersEnum {

    MAIL_DEBUG("mail.debug", "Y", "N", "(Y/N)發送mail是否印出詳細資訊"),
    MAIL_SERVER_ACCOUNT("mail.server.account", "", "N", "登入 mail server的帳號"),
    MAIL_SERVER_PASSWORD("mail.server.password", "", "N", "登入 mail server 的密碼"),
    MAIL_SERVER_EMAIL("mail.server.email", "ISWP@tcb-bank.com.tw", "N", "發送mail時的寄件者信箱"),
    MAIL_SERVER_HOST("mail.server.host", "mail.tcb-bank.com", "N", "mail server 的 HOST 位置"),
    MAIL_SERVER_PORT("mail.smtp.port", "25", "N", "mail server 的 smtp port"),
    MAIL_SMTP_AUTH("mail.smtp.auth", "false", "N", "mail server smtp 是否開啟認證"),
    MAIL_SERVER_TEST("mail.test.mail", "ISWP@tcb-bank.com.tw", "N", "測試發送MAIL"),
    FILE_EXTENSION("file.extension", "csv;txt;doc;docx;pdf;xls;xlsx", "N", "可上傳的文件副檔名(分號區隔)"),
    FILE_SIZE("file.size", "10", "N", "可上傳的檔案大小(M)"),
    IMPACT_THRESHOLD("impact.threshold", "1000", "N", "衝擊分析門檻分數"),
    IMPACT_ANALYSIS_VALIDATE_FRACTION("impact.analysis.validate.fraction", "75", "N", "衝擊分析題目需填寫說明分數門檻"),
    KPI_DEMAND("kpi.demand", "10", "N", "需求單KPI目標"),
    KPI_ISSUE("kpi.issue", "11", "N", "問題單KPI目標"),
    KPI_EVENT("kpi.event", "12", "N", "事件單KPI目標"),
    KEEPING_DATA_MONTHS("keeping.data.months", "12", "N", "保留資料的月份數"),
    CHANGE_FORM_ATTACHMENT_NAME_VERIFY_ROLE("change.form.attachment.name.verify.role","上線驗收準則","N","變更單,變更等級=「重大變更」時，應檢核附件檔名=「上線驗收準則」"),
    CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_PUBLISH_CHECK_LIST("change.form.attachment.name.system.publish.check.list","新系統上線檢核表","N","變更單,新系統=「Y」時，需求單、問題單(來源表單)，應檢核附件檔名「新系統上線檢核表」"),
    CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_FEASIBIILITY_ANALYSIS_REPORT("change.form.attachment.name.system.feasibility.analysis.report","可行性分析報告","N","變更單,新系統=「Y」時，變更單送出(送審)應檢核附件檔名「可行性分析報告」"),
    CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_PUBLISH_ACCEPT_RULE("change.form.attachment.name.system.publish.accept.rule","上線驗收準則","N","變更單,新系統=「Y」時，變更單送出(送審)應檢核附件檔名「上線驗收準則」"),
    CHANGE_FORM_ATTACHMENT_NAME_SERVICE_FEASIBIILITY_ANALYSIS_REPORT("change.form.attachment.name.service.feasibility.analysis.report","可行性分析報告","N","變更單,新服務暨重大服務=「Y」時，變更單送出(送審)應檢核附件檔名「可行性分析報告」"),
    CHANGE_FORM_ATTACHMENT_NAME_SERVICE_PLANNING_REPORT("change.form.attachment.name.service.planning.report","新服務暨服務異動評估規劃報告","N","變更單,新服務暨重大服務=「Y」時，變更單送出(送審)應檢核附件檔名「新服務暨服務異動評估規劃報告」"),
    CHANGE_FORM_ATTACHMENT_NAME_OVER_IMPACT_THRESHOLD("change.form.attachment.name.over.impact.threshold","上線驗收準則","N","變更單,新系統=「N」、新服務暨重大服務=「N」且衝擊分析>=1000之重大變更時，變更單送出(送審)應檢核附件檔名「上線驗收準則」"),
    QUESTION_FORM_ATTACHMENT_NAME_NEW_SYSTEM("question.form.attachment.name.new.system","新系統上線檢核表","N","問題單的變更單有勾選新系統時，問題單送出(送審)應檢核附件檔名「新系統上線檢核表」"),
    REQUIREMENT_FORM_ATTACHMENT_NAME_NEW_SYSTEM("requirement.form.attachment.name.new.system","新系統上線檢核表","N","需求單的變更單有勾選新系統時，需求單送出(送審)應檢核附件檔名「新系統上線檢核表」"),
	APJOB_TESTING_REPORT_NAME("apjob.testing.report.name", "測試", "N", "測試");
    
    public String name;
    public String value;
    public String encryption;
    public String desc;

    SysParametersEnum(String name, String value, String encryption, String desc) {
        this.name = name;
        this.value = value;
        this.encryption = encryption;
        this.desc = desc;
    }

    /**
     * key 是否存在
     *
     * @param key
     * @return
     */
    public static boolean isKeyExists(String key) {
        for (SysParametersEnum param : SysParametersEnum.values()) {
            if (param.name().equals(key)) {
                return true;
            }
        }
        return false;
    }

}
