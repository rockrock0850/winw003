package com.ebizprise.winw.project.service;

import com.ebizprise.winw.project.vo.FormRemindUserRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @author gary.tsai 2019/8/28
 */
public interface IRemindFormUserRecordService {

    void checkFormIntervalMonthsOfLog(List<FormRemindUserRecordVO> formRemindUserRecordVOList, Map<String, FormRemindUserRecordVO> formDataMap);

    void checkFormOverETCOfLog(List<FormRemindUserRecordVO> formRemindUserRecordVOList, Map<String, FormRemindUserRecordVO> formDataMap) throws Exception;

    void sendMailToCreateUser(Map<String, FormRemindUserRecordVO> formDataMap) throws Exception;

    void recordMailLog(Map<String, FormRemindUserRecordVO> formDataMap);
}
