package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.AuditInformVO;

/**
 * 稽核通知相關排程共用介面
 * @author adam.yeh
 */
public interface AuditInform {
    
    public final String MAIL_TEMPLATE = "auditInformContent.vm";
    public final String MAIL_TEMPLATE_INC = "auditInformINCContent.vm";

    /**
     * 取流程中需要檢核的表單資料
     * @return
     * @author adam.yeh
     */
    public List<AuditInformVO> getAuditFormList() throws Exception;
    
}
