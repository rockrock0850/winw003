/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.enums;

/**
 * 各類報表作業 列舉
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月16日
 */
public enum ReportOperationEnum {
    
    SR_ACCURACY(
            "function/reportOperation/sr/sRAccuracy",
            "sRAccuracy",
            "SR_ACCURACY%d",
            "",
            "report.operation.title.sr.accuracy"),
    
    SR_SUCCESS_RATE(
            "function/reportOperation/sr/sRSuccessRate",
            "sRSuccessRate",
            "SR_SUCCESS_RATE",
            "SR_SUCCESS_RATE_EXCLUDE",
            "report.operation.title.sr.successRate"),
    
    SR_NOT_FINISH_BF_TARGET_TIME(
            "function/reportOperation/sr/sRNotFinishBfTargetTime",
            "sRNotFinishBfTargetTime",
            "SR_NOT_FINISH_BF_TARGET_TIME",
            "SR_NOT_FINISH_BF_TARGET_TIME_EXCLUDE",
            "report.operation.title.sr.notFinishBfTargetTime"),
    
    Q_CLOSE_ON_TIME_FOR_SHIRLEY(
            "function/reportOperation/q/qCloseOnTimeForShirley",
            "qCloseOnTimeForShirley",
            "Q_CLOSE_ON_TIME_FOR_SHIRLEY",
            "Q_CLOSE_ON_TIME_FOR_SHIRLEY_EXCLUDE",
            "report.operation.title.q.closeOnTimeForShirley"),
    
    Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M(
            "function/reportOperation/q/qCloseOnTimeInTargetFinishYM",
            "qCloseOnTimeInTargetFinishYM",
            "Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M",
            "Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M_EXCLUDE",
            "report.operation.title.q.closeOnTimeInTargetFinishYM"),
    
    Q_SPECIAL_CLOSE(
            "function/reportOperation/q/qSpecialClose",
            "qSpecialClose",
            "Q_SPECIAL_CLOSE",
            "Q_SPECIAL_CLOSE_EXCLUDE",
            "report.operation.title.q.specialClose"),
    
    Q_STATISTIC_CLOSE(
            "function/reportOperation/q/qStatisticClose",
            "qStatisticClose",
            "Q_STATISTIC_CLOSE%d",
            "",
            "report.operation.title.q.statisticClose"),
  
    Q_STATISTIC_LEVEL(
            "function/reportOperation/q/qStatisticLevel",
            "qStatisticLevel",
            "Q_STATISTIC_LEVEL",
            "Q_STATISTIC_LEVEL_EXCLUDE",
            "report.operation.title.q.statisticLevel"),
    
    Q_STATISTIC_SOURCE(
            "function/reportOperation/q/qStatisticSource",
            "qStatisticSource",
            "Q_STATISTIC_SOURCE",
            "Q_STATISTIC_SOURCE_EXCLUDE",
            "report.operation.title.q.statisticSource"),
    
    INC_LIST_REPORT(
            "function/reportOperation/inc/iNCListReport",
            "iNCListReport",
            "INC_LIST_REPORT",
            "INC_LIST_REPORT_EXCLUDE",
            "report.operation.title.inc.listReport"),
    
    INC_CLOSE_RATE_ON_TIME_RATE_REPORT(
            "function/reportOperation/inc/iNCCloseRateOnTimeRateReport",
            "iNCCloseRateOnTimeRateReport",
            "INC_CLOSE_RATE_ON_TIME_RATE_REPORT%d",
            "",
            "report.operation.title.inc.closeRateOnTimeRateReport"),
    
    INC_INTERRUPT(
            "function/reportOperation/inc/iNCInterrupt",
            "iNCInterrupt",
            "INC_INTERRUPT%d",
            "",
            "report.operation.title.inc.interrupt"),
    
    INC_SECURITY_EVENT(
            "function/reportOperation/inc/iNCSecurityEvent",
            "iNCSecurityEvent",
            "INC_SECURITY_EVENT",
            "INC_SECURITY_EVENT_EXCLUDE",
            "report.operation.title.inc.securityEvent"),
    
    INC_STATISTIC_MULIT_MONTH(
            "function/reportOperation/inc/iNCStatisticMulitMonth",
            "iNCStatisticMulitMonth",
            "INC_STATISTIC_MULIT_MONTH",
            "INC_STATISTIC_MULIT_MONTH_EXCLUDE",
            "report.operation.title.inc.statisticMulitMonth"),
    
    INC_STATISTIC_REPORT_DETAIL(
            "function/reportOperation/inc/iNCStatisticReportDetail",
            "iNCStatisticReportDetail",
            "INC_STATISTIC_REPORT_DETAIL%d",
            "",
            "report.operation.title.inc.statisticReportDetail"),
    
    INC_STATISTIC_REPORT(
            "function/reportOperation/inc/iNCStatisticReport",
            "iNCStatisticReport",
            "INC_STATISTIC_REPORT",
            "FIND_ALL_SYSTEM_BY_CONDITIONS_EXCLUDE",
            "report.operation.title.inc.statisticReport"),
    
    INC_AP_REPORT(
            "function/reportOperation/inc/apReport",
            "apReport",
            "INC_AP_REPORT",
            "FIND_AP_REPORT_EXCLUDE",
            "report.operation.title.inc.statisticReport.1"),
    
    INC_STATISTIC_REPORT_A_P_ISSUE(
            "function/reportOperation/inc/iNCStatisticReportAPIssue",
            "iNCStatisticReportAPIssue",
            "INC_STATISTIC_REPORT_A_P_ISSUE",
            "",
            "report.operation.title.inc.statisticReportAPIssue"),
    
    INC_STATISTIC_REPORT_I_D_C(
            "function/reportOperation/inc/iNCStatisticReportIDC",
            "iNCStatisticReportIDC",
            "INC_STATISTIC_REPORT_I_D_C",
            "",
            "report.operation.title.inc.statisticReportIDC"),
    
    CHG_STATISTIC_IS_NEW_SERVICE(
            "function/reportOperation/chg/cHGStatisticIsNewService",
            "cHGStatisticIsNewService",
            "CHG_STATISTIC_IS_NEW_SERVICE",
            "CHG_STATISTIC_IS_NEW_SERVICE_EXCLUDE"
            ,"report.operation.title.chg.statisticIsNewService"),
    
    CHG_ALTER_RESULT_SERVICE(
            "function/reportOperation/chg/cHGAlterResult",
            "cHGAlterResult",
            "CHG_ALTER_RESULT_SERVICE",
            "CHG_ALTER_RESULT_SERVICE_EXCLUDE",
            "report.operation.title.chg.alterResultService"),
    
    CHG_STATISTIC_LEVEL(
            "function/reportOperation/chg/cHGStatisticLevel",
            "cHGStatisticLevel",
            "CHG_STATISTIC_LEVEL",
            "CHG_STATISTIC_LEVEL_EXCLUDE",
            "report.operation.title.chg.statisticLevel"),
    
    CHG_STATISTIC_SOURCE(
            "function/reportOperation/chg/cHGStatisticSource",
            "cHGStatisticSource",
            "CHG_STATISTIC_SOURCE",
            "CHG_STATISTIC_SOURCE_EXCLUDE",
            "report.operation.title.chg.statisticSource"),
    
    CHG_STATISTIC_TYPE(
            "function/reportOperation/chg/cHGStatisticType",
            "cHGStatisticType",
            "CHG_STATISTIC_TYPE",
            "CHG_STATISTIC_TYPE_EXCLUDE",
            "report.operation.title.chg.statisticType"),
    
    SEARCH_CONDITION("","","","","global.query.condition"),
    
    SEARCH_DATE("","","","","report.operation.search.date"),
    
    SEARCH_EXCLUDE("","","","","form.report.search.exclude"),
    
    SEARCH_RESULT("","","","","global.query.result"),
    
    DEPARTMENT("","","","","report.operation.department"),
    
    SECONDARY_CHANGE("","","","","report.operation.chg.secondary.change"),
    
    IMPORTANT_CHANGE("","","","","report.operation.chg.important.change"),
    
    NOT_NEW_SYSTEM("","","","","report.operation.chg.not.newSystem"),
    
    NEW_SYSTEM("","","","","report.operation.chg.new.system"),
    
    NEW_SERVICE("","newService","","","report.operation.chg.newService"),

    ALTER_RESULT("","alterResult","","","report.operation.chg.alterResult"),
    
    SECTION("","section","","",""),
    
    KIND("","kind","","",""),
    
    TOTAL("","total","","","report.operation.chg.total"),
    
    SEARCH_TOTAL("","","","","form.report.search.total"),
    
    SEARCH_UNFINISH("","","","","form.report.search.unfinish"),
    
    UNFINISHED("","","","","report.operation.q.form.incl.unfinished"),
    
    SEARCH_HOST("","","","","form.report.search.host"),
    
    EVENT_NUM("","","","","report.operation.information.security.event.num"),
    
    GRADE_TOTAL("","","","","report.operation.q.form.grade.total"),
    
    Q_GRADE("","","","","report.operation.q.form.grade"),
    
    TOTAL_NUMBER("","","","","q.report.operation.specialcase.totalNumber"),
    
    WORK_GROUP("","","","","report.operation.inc.work.group"),
    
    SCLASS("","","","","form.search.column.sClass"),
    
    Q_TOTAL("","","","","q.report.operation.specialcase.total"),
    
    COUNT("","count","","","report.operation.chg.count"),
    
    FORM_TYPE_CODE("","","","","report.operation.form.type.code"),
    
    FORM_WONUM("","","","","report.operation.form.wonum"),
    
    FORM_TYPE("","","","","report.operation.form.type"),
    
    TYPE_NORMAL("","","","","form.search.html.change.type.normal"),
    
    TYPE_STANDARD("","","","","form.search.html.change.type.standard"),
    
    IS_URGENT("","","","","form.change.form.info.isUrgent"),
    
    NON_URGENT_CHANGE("","","","","report.operation.chg.nonUrgent.change"),
    
    SERIAL_NUMBER("","","","","form.report.search.serialnumber"),
    
    EXCLUDE_TIME("","","","","report.operation.event.exclude.time"),
    
    TARGET_SOLVED_TIME("","","","","report.operation.target.solved.time"),
    
    PRINCIPAL("","","","","form.report.search.principal"),
    
    BE_RESPONSIBLE_FOR("","","","","form.report.search.beResponsibleFor"),
    
    AIMS_COMPLETION_DATE("","","","","form.report.search.aimsCompletionDate"),
    
    ACTUAL_COMPLETION_DATE("","","","","form.report.search.actualCompletionDate"),
    
    TITLE("","","","","q.report.operation.description.title"),
    
    IS_SPECIAL("","IsSpecial","","","form.search.column.q.isSpecial"),
    
    Q_CONTENT_1("","","","","q.report.operation.description.content.1"),
    
    Q_CONTENT_2("","","","","q.report.operation.description.content.2"),
    
    Q_CONTENT_3("","","","","q.report.operation.description.content.3"),
    
    Q_CONTENT_4("","","","","q.report.operation.description.content.4"),
    
    Q_CONTENT_5("","","","","q.report.operation.description.content.5"),
    
    Q_CONTENT_6("","","","","q.report.operation.description.content.6"),
    
    Q_CONTENT_7("","","","","q.report.operation.description.content.7"),
    
    Q_CONTENT_8("","","","","q.report.operation.description.content.8"),
    
    Q_CONTENT_6_1("","","","","report.operation.q.description.content.6"),
    
    Q_CONTENT_7_1("","","","","report.operation.q.description.content.7"),
    
    Q_CONTENT_8_1("","","","","report.operation.q.description.content.8"),
    
    Q_CONTENT_9_1("","","","","report.operation.q.description.content.9"),
    
    SR_CONTENT_1("","","","","report.operation.sr.description.content.1"),
    
    SR_CONTENT_2("","","","","report.operation.sr.description.content.2"),
    
    SR_CONTENT_3("","","","","report.operation.sr.description.content.3"),
    
    SR_CONTENT_4("","","","","report.operation.sr.description.content.4"),
    
    SR_CONTENT_5("","","","","report.operation.sr.description.content.5"),
    
    CLOSED_METHOD("","","","","report.operation.q.form.closed.method"),
    
    HANDLING("","","","","q.report.operation.specialcase.handling"),
    
    NUM("","","","","report.operation.q.form.num"),
    
    TARGET("","","","","q.report.operation.yearMonth.target"),
    
    QUANTITY_A("","","","","q.report.operation.form.quantity.a"),
    
    FINISHED_B("","","","","q.report.operation.form.finished.b"),
    
    PERCENT("","","","","q.report.operation.form.percent"),
    
    EVENT_GRADE("","","","","report.operation.event.grade"),
    
    EVENT_SUMMARY("","","","","report.operation.event.summary"),
    
    START_DATE("","startDate",""," 00:00:00.000",""),
    
    END_DATE("","endDate",""," 23:59:59.997",""),
    
    DEPARTMENT_ID("","DepartmentId","","",""),
    
    SEARCH_MONTH("","","","","report.operation.sr.form.searchMonth"),
    
    RUN_SR_JOB_SP_FORM("","","","","report.operation.sr.form.spNumber"),
    
    RUN_SR_JOB_AP_FORM("","","","","report.operation.sr.form.apNumber"),
    
    RUN_SR_ONLINE("","","","","report.operation.sr.form.onlineNumber"),
    
    RUN_SR_SUCCESS_RATE("","","","","report.operation.sr.form.successRate"),
    
    AP_DEPARTMENT("","","","","form.search.ap.department"),
    
    AP_FORM_NUMBER("","","","","form.search.ap.bill.num"),
    
    DIVISION("","division","","",""),
    
    FORM_ID("","FormId","","","form.search.column.formId"),
    
    INTERRUPT("","","","","report.operation.inc.form.interrupt"),
    
    PRODUCTION("","","","","report.operation.inc.form.production"),
    
    DETAIL_ID("","DetailId","","",""),
    
    FORM_STATUS("","FormStatus","","","form.report.search.formStatus"),
    
    BILLING_TIME("","","","","dashboard.form.billingTime"),
    
    SYSTEM_NAME("","","","","form.search.column.system"),
    
    INFO_DATE_CREATETIME("","","","","form.search.column.inc.infoDateCreateTime"),
    
    OFF_LINE_TIME("","OffLineTime","","","form.job.form.offLineTime"),
    
    ON_LINE_TIME("","OnLineTime","","","form.job.form.onLineTime"),
    
    EXCLUDETIME("","ExcludeTime","","","form.search.column.inc.excludeTime2"),
    
    SECURITY_EVENT_TYPE("","","","","report.operation.information.security.event.type"),
    
    UNFINISHED_C_FORM("","","","","report.operation.unfinished.c.form"),
    
    USER_CREATED("","UserCreated","","",""),
    
    COLUMN_4("","","","","form.link.column.4"),
    
    SUMMARY("","Summary","","","form.report.search.summary"),
    
    Q_FORM_SOURCE("","","","","report.operation.q.form.source"),
    
    USER_SOLVING("","","","","form.search.column.userSolving"),
    
    SYSTEM("","System","","","form.question.form.info.system.name"),
    
    INC_REASON("","","","","report.operation.inc.reason%d"),
    
    GRAND_TOTAL("","Grand Total","","","report.operation.inc.reason11"),
    
    HEADER("","header","","",""),
    
    BODY("","body","","",""),
    
    EVENT_TYPE("","EventSecurity","","",""),
    
    CORRESPOND_STATISTICS("","","","","report.operation.inc.correspond.statistics"),
    
    ECT("","ECT","","","form.question.form.info.ect"),
    
    ACT("","ACT","","","form.question.form.info.act"),
    
    INFLUENCES("","EffectScope","","","q.report.operation.specialcase.influences"),
    
    URGENT("","UrgentLevel","","","q.report.operation.specialcase.urgent"),
    
    ORDER("","QuestionPriority","","","q.report.operation.specialcase.order"),
    
    CONTENT("","Content","","","q.report.operation.specialcase.content"),
    
    SIGN("","Indication","","","q.report.operation.specialcase.sign"),
    
    REASON("","Reason","","","q.report.operation.specialcase.reason"),
    
    SOLVE("","ProcessProgram","","","q.report.operation.specialcase.solve"),
    
    TREATMENT_PLAN("","","","","q.report.operation.specialcase.treatmentPlan"),
    
    TEMPORARY_SOLUTION("","IsSuggestCase","","","q.report.operation.specialcase.temporarySolution"),
    
    PROCESSING_UNI("","","","","q.report.operation.specialcase.processingUnit"),
    
    GROUP_ID("","GroupId","","",""),
    
    GROUP_NAME("","GroupName","","",""),
    
    CREATE_TIME("","CreateTime","","",""),
    
    NAME("","Name","","",""),
    
    WORKING("","Working","","",""),
    
    DEPARTMENT_NAME("","DepartmentName","","",""),
    
    PROPOSING("","PROPOSING","","","repott.operation.form.status.proposing"),
    
    APPROVING("","APPROVING","","","repott.operation.form.status.approving"),
    
    CHARGING("","CHARGING","","","repott.operation.form.status.charging"),

    APPROVING_0("","APPROVING","","","repott.operation.form.status.approving.0"),
    
    CHARGING_0("","CHARGING","","","repott.operation.form.status.charging.0"),
    
    CLOSED("","CLOSED","","","repott.operation.form.status.closed"),
    
    DEPRECATED("","DEPRECATED","","","repott.operation.form.status.deprecated"),
    
    ASSIGNING("","ASSIGNING","","","repott.operation.form.status.assigning"),
    
    SELFSOLVE("","SELFSOLVE","","","repott.operation.form.status.selfsolve"),
    
    WATCHING("","WATCHING","","","repott.operation.form.status.watching"),
    
    SPECIAL_CLOSE("","","","","q.report.operation.special.close.description"),
    
    FORM_SR("","SR","","","requirement.form"),
    
    FORM_SR_C("","SR_C","","","requirement.countersigned.form"),
    
    FORM_Q("","Q","","","question.form"),
    
    FORM_Q_C("","Q_C","","","question.countersigned.form"),
    
    FORM_INC("","INC","","","event.form"),
    
    FORM_INC_C("","INC_C","","","event.countersigned.form"),
    
    MAP_KEY_FORM_CLASS("","FormClass","","",""),
    
    MAP_KEY_TIMES("","times","","",""),
    
    TYPE_STATISTIC("","typeStatistic","","","report.operation.inc.type.statistics"),
    
    REASON_STATISTIC("","reasonStatistic","","","report.operation.inc.reason.statistics"),
    
    LEVEL_STATISTIC("","levelStatistic","","","report.operation.inc.level.statistics"),
    
    INC_EVEN_TYPE("","","","","report.operation.inc.type"),
    
    INC_MAIN("","","","","report.operation.inc.main"),
    
    INC_SEQUENCE("","","","","report.operation.inc.sequence"),
    
    INC_NUMBER_OVERDUE("","","","","report.operation.number.overdue"),
    
    INC_UNFINISHED("","","","","report.operation.inc.unfinished"),
    
    NUMBER_ACCEPTED("","","","","report.operation.number.accepted"),
    
    SERVICE_EXCEPTION("","exception","","服務異常","report.operation.service.exception"),
    
    SERVICE_REQUEST("","request","","服務請求","report.operation.service.request"),
    
    SERVICE_COUNSEL("","counsel","","服務諮詢","report.operation.service.counsel"),
    
    SERVICE_TOTAL("","total","","","report.operation.service.total"),
    
    MAP_KEY_SECTION("","section","","","q.report.operation.specialcase.section"),
    
    MAP_KEY_COUNTERSIGNEDS("","Countersigneds","","",""),
    
    MAP_KEY_SECONDARY("","secondary","","",""),
    
    MAP_KEY_NEW_SYSTEM("","newSystem","","",""),
    
    MAP_KEY_NOT_NEW_SYSTEM("","notNewSystem","","",""),
    
    MAP_KEY_GENERAL_YES("","generalYes","","",""),
    
    MAP_KEY_GENERAL_NO("","generalNo","","",""),
    
    MAP_KEY_STANDARD_YES("","standardYes","","",""),
    
    MAP_KEY_STANDARD_NO("","standardNo","","",""),
    
    MAP_KEY_DISPLAY("","display","","",""),
    
    MAP_KEY_COUNTER("","counter","","",""),
    
    MAP_KEY_104("","104","","104",""),
    
    MAP_KEY_105("","105","","105",""),
    
    MAP_KEY_106("","106","","106",""),
    
    MAP_KEY_107("","107","","107",""),
    
    MAP_KEY_108("","108","","108",""),
    
    MAP_KEY_109("","109","","109",""),
    
    MAP_KEY_110("","110","","110",""),
    
    MAP_KEY_TOTAL("","total","","total",""),
    
    MAP_KEY_VALUE("","value","","value",""),
    
    MAP_KEY_GRADE("","grade","","",""),
    
    MAP_KEY_ALL("","all","","","report.operation.select.division.all"),
    
    MAP_KEY_UNFINISHED("","unfinished","","",""),
    
    MAP_KEY_YEAR_MONTH("","yearmonth","","",""),
    
    MAP_KEY_FINISHED("","finished","","",""),
    
    MAP_KEY_PERCENTAGE("","percentage","","",""),
    
    MAP_KEY_UNIVERSAL("","universal","","",""),
    
    MAP_KEY_S_END_1("","S_END_1","","",""),
    
    MAP_KEY_S_END_2("","S_END_2","","",""),
    
    MAP_KEY_CLOSE_METHOD("","closeMethod","","",""),
    
    MAP_KEY_SP("","sp","","",""),
    
    MAP_KEY_AP("","ap","","",""),
    
    MAP_KEY_CORRECT("","correct","","",""),
    
    BRAKETS("","","","(%d)",""),
    
    BRAKETS_STRING("","","","(%s)",""),
    
    PROCESS_DIVISION("","","","","form.header.process.division"),
    
    SOLVED_AS_SCHEDULED("","","","","report.operation.solved.as.scheduled"),
    
    UNSOLVED_AS_SCHEDULED("","","","","report.operation.unsolved.as.scheduled"),
    
    CLOSEING_RATE("","","","","report.operation.closing.rate"),
    
    SOLVED_TIME("","","","","report.operation.average.solved.time"),
    
    DD_HH_MM_SS("","","","","report.operation.dd.hh.MM.ss"),
    
    SECOND_TOTAL("","","","","report.operation.second.total"),
    
    YEAR_MONTH("","","","","report.operation.year.month"),
    
    START_END("","","","","report.operation.start.end"),
    
    URGENT_LEVEL("","","","","form.search.column.urgentLevel"),
    
    ACCEPT_EVENT("","","","","report.operation.accept.event"),
    
    EVENT_TIME("","","","","report.operation.event.time"),
    
    COMPLETION_DATE("","","","","report.operation.expected.completion.date"),
    
    MAP_KEY_1("","1","","","q.report.operation.general.close"),
    
    MAP_KEY_2("","2","","","q.report.operation.specialcase.close"),
    
    MAP_KEY_3("","3","","","q.report.operation.specialcase.trace"),
    
    MAP_KEY_4("","4","","","report.operation.q.form.unfinished"),
    
    MAP_KEY_SOURCE_ID("","SourceId","","","report.operation.q.source.total"),
    
    MAP_KEY_FORM_ID("","formid","","",""),
    
    MAP_KEY_NAME("","name","","",""),
    
    MAP_KEY_DATASETS("","dataSets","","",""),
    
    MAP_KEY_GROUP_NAME("","GroupName","","",""),
    
    MAP_KEY_FORM_STATUS("","FormStatus","","",""),
    
    MAP_KEY_NEW_FIELD("","newField","","",""),
    
    MAP_KEY_FORM_APPROVING("","APPROVING","","","report.operation.status.approving"),
    
    MAP_KEY_PICK_MONTH("","pickMonth","","",""),
    
    MAP_KEY_EP("","ep","","",""),
    
    MAP_KEY_H_M_D("","hmd","","",""),
    
    MAP_KEY_AVG("","avg","","",""),
    
    MAP_KEY_EVENT_PRIORITY("","EventPriority","","",""),
    
    MAP_KEY_USER_SOLVING("","usersolving","","",""),
    
    MAP_KEY_GROUP_ID("","GroupId","","",""),
    
    MAP_KEY_VERIFY_LEVEL("","VerifyLevel","","",""),
    
    MAP_KEY_ITEMS("","item","","",""),
    
    ALL_SECTION("","allSection","","","")
    ;
    
    private String path;
    private String name;
    private String sqlFileName;
    private String value;
    private String i18n;
    
    private ReportOperationEnum(String... value) {
       init(value);
    }
    
    private void init(String... value) {
        if(value !=null) {
            for(int i =0;i<value.length;i++) {
                if(i==0) {
                    this.path=value[i];
                }
                if(i==1) {
                    this.name=value[i];
                }
                if(i==2) {
                    this.sqlFileName=value[i];
                }
                if(i==3) {
                    this.value=value[i];
                }
                if(i==4) {
                    this.i18n=value[i];
                }
            }
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getSqlFileName() {
        return sqlFileName;
    }
 
    public String getPath() {
        return path;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getI18n() {
        return i18n;
    }
     
}
