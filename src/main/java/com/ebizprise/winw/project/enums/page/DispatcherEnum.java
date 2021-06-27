package com.ebizprise.winw.project.enums.page;

/**
 * 統一管理所有頁面的URI
 * 
 * @author adam.yeh
 *
 */
public enum DispatcherEnum implements Forms {

    /**
     * 登入
     */
    LOGIN {
        @Override
        public String initPage() {
            return "main/login";
        }
    },

    /**
     * 公布欄
     */
    DASHBOARD {
        @Override
        public String initPage() {
            return "main/dashBoard";
        }
    },

    /**
     * 系統批次設定
     */
    SCHEDULE_JOB {
        @Override
        public String initPage() {
            return "function/systemJobs/scheduleFind";
        }

        @Override
        public String editPage() {
            return "function/systemJobs/scheduleEdit";
        }
    },

    /**
     * 登入/登出紀錄
     */
    LOGON_RECORD {
        @Override
        public String initPage() {
            return "function/userActive/logonRecord";
        }
    },

    /**
     * 系統參數
     */
    SYSTEM_CONFIG {
        @Override
        public String initPage() {
            return "function/systemConfig/sysConfigFind";
        }
    },

    /**
     * 系統名稱管理
     */
    SYSTEM_NAME_MANAGE {
        @Override
        public String initPage() {
            return "function/systemFunction/systemNameManageFind";
        }
    },

    /**
     * 標準變更作業管理
     */
    STANDARD_CHANGE_MANAGE {
        @Override
        public String initPage() {
            return "function/standardChangeFunction/standardChangeFind";
        }
    },

    /**
     * 工作組別管理
     */
    JOB_GROUP_MANAGE {
        @Override
        public String initPage() {
            return "function/jobGroupFunction/jobGroupManageFind";
        }
    },

    /**
     * 工作要項管理
     */
    JOB_ITEM_MANAGE {
        @Override
        public String initPage() {
            return "function/jobItemFunction/jobItemManageFind";
        }
    },

    /**
     * 服務類別管理
     */
    SERVICE_TYPE_MANAGE {
        @Override
        public String initPage() {
            return "function/serviceFunction/serviceTypeManageFind";
        }
    },

    /**
     * 知識庫原因維護
     */
    KNOWLEDGE_BASE {
        @Override
        public String initPage() {
            return "function/knowledgeBase/knowledgeBaseFind";
        }
    },

    /**
     * 知識庫查詢
     */
    KNOWLEDGE_FIND {
        @Override
        public String initPage() {
            return "function/knowledgeable/find";
        }
    },

    /**
     * 資訊部門各變更類型統計表
     */
    ALTER_STATISTIC_TYPE {
        @Override
        public String initPage() {
            return "function/alterFormStatisticType/statisticTypeFind";
        }
    },

    /**
     * 衝擊分析題庫維護
     */
    QUESTION_MAINTAIN {
        @Override
        public String initPage() {
            return "function/questionMaintain/questionFind";
        }
    },

    /**
     * 表單查詢
     */
    FORM_SEARCH {
        @Override
        public String initPage() {
            return "function/formOperation/formSearchFind";
        }

        @Override
        public String dispatch() {
            return "function/formOperation/formDispatch";
        }
    },

    /**
     * 匯入假日資料
     */
    HOLIDAY_IMPORT {

        @Override
        public String initPage() {
            return "function/holidayImport/holidayFind";
        }

    },

    /**
     * 新增表單 共用頁面
     */
    FORM {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String log() {
            return "function/formOperation/common/tabs/log";
        }

        @Override
        public String fileList() {
            return "function/formOperation/common/tabs/fileList";
        }

        @Override
        public String linkList() {
            return "function/formOperation/common/tabs/linkList";
        }

        @Override
        public String impactAnalysis() {
            return "function/formOperation/common/tabs/impactAnalysis";
        }

        @Override
        public String processInfo() {
            return "function/formOperation/common/tabs/processInfo";
        }

        @Override
        public String checkLog() {
            return "function/formOperation/common/tabs/verifyLog";
        }
    },
    
    KNOWLEDGE {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/knowledge";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/knowledge";
        }
    },

    /**
     * 新增表單 需求單
     */
    REQUIREMENT {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/requirement";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/requirement";
        }

        @Override
        public String program() {
            return "function/formOperation/common/tabs/program/requirement";
        }
    },

    /**
     * 新增表單 問題單
     */
    QUESTION {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/question";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/question";
        }

        @Override
        public String program() {
            return "function/formOperation/common/tabs/program/question";
        }
    },

    /**
     * 新增表單 事件單
     */
    EVENT {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/event";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/event";
        }

        @Override
        public String program() {
            return "function/formOperation/common/tabs/program/event";
        }
    },

    /**
     * 新增表單 變更單
     */
    CHANGE {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/change";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/change";
        }
    },

    /**
     * 新增表單 會辦單
     */
    COUNTERSIGNED {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/countersigned";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/countersigned/info";
        }
    },

    /**
     * 工作單 共用頁面
     */
    JOB {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String checkPerson() {
            return "function/formOperation/common/jobTabs/checkPerson";
        }

        @Override
        public String ap() {
            return "function/formOperation/common/jobTabs/ap";
        }

        @Override
        public String batch() {
            return "function/formOperation/common/jobTabs/batch";
        }

        @Override
        public String db() {
            return "function/formOperation/common/jobTabs/db";
        }

        @Override
        public String dc() {
            return "function/formOperation/common/jobTabs/dc";
        }

        @Override
        public String ea() {
            return "function/formOperation/common/jobTabs/ea";
        }

        @Override
        public String library() {
            return "function/formOperation/common/jobTabs/library";
        }

        @Override
        public String oa() {
            return "function/formOperation/common/jobTabs/oa";
        }

        @Override
        public String open() {
            return "function/formOperation/common/jobTabs/open";
        }

        @Override
        public String planmgmt() {
            return "function/formOperation/common/jobTabs/planmgmt";
        }

        @Override
        public String programs() {
            return "function/formOperation/common/jobTabs/programs";
        }

        @Override
        public String pt() {
            return "function/formOperation/common/jobTabs/pt";
        }

        @Override
        public String sp() {
            return "function/formOperation/common/jobTabs/sp";
        }
    },

    /**
     * SP工作單
     */
    JOB_SP {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/sp";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/jobTabs/formInfo/sp";
        }
    },

    /**
     * AP工作單
     */
    JOB_AP {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/ap";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/jobTabs/formInfo/ap";
        }
    },

    /**
     * SP工作會辦單
     */
    JOB_SP_C {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/spc";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/jobTabs/formInfo/spc";
        }
    },

    /**
     * AP工作會辦單
     */
    JOB_AP_C {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/apc";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/jobTabs/formInfo/apc";
        }
    },
    
    /**
     * 新增批次作業中斷對策表管理
     */
    BATCHINTERRUPT {
        @Override
        public String initPage() {
            return null;
        }

        @Override
        public String header() {
            return "function/formOperation/common/formHeaders/batchInterrupt";
        }

        @Override
        public String formInfo() {
            return "function/formOperation/common/tabs/formInfo/batchInterrupt";
        }
    },

    /**
     * 稽催通知參數設定
     */
    AUDIT_PARAMS {
        @Override
        public String initPage() {
            return "function/auditParams/auditParamsFind";
        }

        @Override
        public String editPage() {
            return "function/auditParams/auditParamsEdit";
        }
    },

    /**
     * 群組功能管理
     */
    GROUP_FUNCTION {
        @Override
        public String initPage() {
            return "function/groupFunction/groupFind";
        }

        @Override
        public String editPage() {
            return "function/groupFunction/groupEdit";
        }
    },

    /**
     * 表單流程管理
     * 
     */
    FORM_PROCESS_MANAGMENT {
        @Override
        public String initPage() {
            return "function/formProcessManagment/formProcessManagmentFind";
        }
    },

    /**
     * 表單流程管理-需求單
     * 
     */
    FORM_PROCESS_MANAGMENT_SR {
        @Override
        public String initPage() {
            return "function/formProcessManagment/SR/formProcessManagmentSrAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/SR/formProcessManagmentSrAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/SR/formProcessManagmentSrEdit";
        }
    },

    /**
     * 表單流程管理-問題單
     * 
     */
    FORM_PROCESS_MANAGMENT_Q {
        @Override
        public String initPage() {
            return "function/formProcessManagment/Question/formProcessManagmentQuestionAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/Question/formProcessManagmentQuestionAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/Question/formProcessManagmentQuestionEdit";
        }
    },

    /**
     * 表單流程管理-事件單
     * 
     */
    FORM_PROCESS_MANAGMENT_INC {
        @Override
        public String initPage() {
            return "function/formProcessManagment/INC/formProcessManagmentIncAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/INC/formProcessManagmentIncAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/INC/formProcessManagmentIncEdit";
        }
    },

    /**
     * 表單流程管理-變更單
     * 
     */
    FORM_PROCESS_MANAGMENT_CHG {
        @Override
        public String initPage() {
            return "function/formProcessManagment/CHG/formProcessManagmentChgAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/CHG/formProcessManagmentChgAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/CHG/formProcessManagmentChgEdit";
        }
    },

    /**
     * 表單流程管理-工作單
     * 
     */
    FORM_PROCESS_MANAGMENT_JOB {
        @Override
        public String initPage() {
            return "function/formProcessManagment/JOB/formProcessManagmentJobAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/JOB/formProcessManagmentJobAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/JOB/formProcessManagmentJobEdit";
        }
    },
    
    /**
     * 表單流程管理-批次作業中斷對策表管理
     * 
     */
    FORM_PROCESS_MANAGMENT_BA {
        @Override
        public String initPage() {
            return "function/formProcessManagment/BA/formProcessManagmentBaAdd";
        }

        @Override
        public String addPage() {
            return "function/formProcessManagment/BA/formProcessManagmentBaAdd";
        }

        @Override
        public String editPage() {
            return "function/formProcessManagment/BA/formProcessManagmentBaEdit";
        }
    },

    /**
     * 應用系統之需求未於預計完成日修改完畢的個數
     * 
     */
    SR_NOT_FINISH_BF_TARGET_TIME {
        @Override
        public String initPage() {
            return "function/reportOperation/SR/sRNotFinishBfTargetTime";
        }
    };

}