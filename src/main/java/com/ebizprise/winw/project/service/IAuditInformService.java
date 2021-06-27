package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;
import com.ebizprise.winw.project.vo.AuditInformVO;

/**
 * 稽核通知排程共用 介面
 * @author Bernard.yu 2020/09/04
 */
public interface IAuditInformService {

    /**
     * 取排程設定參數
     * @param formType
     * @throws Exception
     */
    List<AuditNotifyParamsEntity> getANParams(String formType) throws Exception;

    /**
     * 篩選 時限+審核進度未達副理
     *
     * @param dataList
     * @param anParams
     * @throws Exception
     */
    List<AuditInformVO> checkList(List<AuditInformVO> dataList, List<AuditNotifyParamsEntity> anParams) throws Exception;

    /**
     * 寄送通知mail及紀錄 mail log
     *
     * @param checkedList
     * @param template
     * @param jobName
     * @return
     * @throws Exception
     */
    String sendMail(List<AuditInformVO> checkedList, String template, String jobName) throws Exception;
    
}
