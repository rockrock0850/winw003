package com.ebizprise.winw.project.job;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.service.IRemindFormUserRecordService;
import com.ebizprise.winw.project.vo.FormRemindUserRecordVO;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提醒表單日誌填寫_排程
 */
public class RemindFormUserRecordJob extends QuartzJobFactory {
    private static final Logger logger = LoggerFactory.getLogger(RemindFormUserRecordJob.class);

    @Autowired
    private IRemindFormUserRecordService remindFormUserRecordService;

    @Override
    protected String executeJob(JobExecutionContext jobCtx) throws Exception {
        Map<String, FormRemindUserRecordVO> formDataMap = new HashMap<>(); // 紀錄需提醒的表單資料
        Map<String, Object> params = new HashMap<>();

        // 超過預計完成日且未經科長審核結案的各類表單
        getAfterExpiryOfETCDate(formDataMap, params);
        // 預計完成天數２個月以上的表單資料處理(審核流程)
        getETCDateOverTwoMonths(formDataMap, params);
        // 寄送 Mail 程序
        sendMailProcess(formDataMap);
        
        return "";
    }

    /**
     * 超過預計完成日且未經科長審核結案的各類表單
     *
     * @param formDataMap
     * @param params
     */
    private void getAfterExpiryOfETCDate(Map<String, FormRemindUserRecordVO> formDataMap, Map<String, Object> params) throws Exception{
        params.put("today", DateUtils.getCurrentDate(DateUtils.pattern11));
        try {
            // 超過預計完成日並未結案之各類表單最新的同意審核紀錄
            List<FormRemindUserRecordVO> formDataOverETCList = jdbcRepository.queryForList(ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_TODAY_MORE_THAN_ECT"), params, FormRemindUserRecordVO.class);
            if (CollectionUtils.isNotEmpty(formDataOverETCList)) {
                // 未經科長審核結案的各類表單
                remindFormUserRecordService.checkFormOverETCOfLog(formDataOverETCList, formDataMap);
            }
            formDataOverETCList.clear();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("超過預計完成日且未經科長審核結案的表單，流程出現問題");
            throw e;
        }
    }

    /**
     * 預計完成天數２個月以上之表單
     *
     * @param formDataMap
     * @param params
     */
    private void getETCDateOverTwoMonths(Map<String, FormRemindUserRecordVO> formDataMap, Map<String, Object> params) {
        // 預計完成天數２個月以上的各類表單資料處理(審核流程)
        params.put("months", 2); //  設定為超過兩個月期限之各類表單
        try {
            List<FormRemindUserRecordVO> formDateOverTwoMonthsList = jdbcRepository.queryForList(ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_USER_RECORD_OVER_MONTH"), params, FormRemindUserRecordVO.class);
            if (CollectionUtils.isNotEmpty(formDateOverTwoMonthsList)) {
                remindFormUserRecordService.checkFormIntervalMonthsOfLog(formDateOverTwoMonthsList, formDataMap);
            }
            formDateOverTwoMonthsList.clear();
        } catch (Exception e) {
            logger.error("預計完成天數２個月以上之表單，流程出現問題");
            throw e;
        }
    }

    /**
     * 寄送 Mail 流程
     *
     * @param formDataMap
     * @throws Exception
     */
    private void sendMailProcess(Map<String, FormRemindUserRecordVO> formDataMap) throws Exception {
        remindFormUserRecordService.sendMailToCreateUser(formDataMap);
        remindFormUserRecordService.recordMailLog(formDataMap);
    }
}
