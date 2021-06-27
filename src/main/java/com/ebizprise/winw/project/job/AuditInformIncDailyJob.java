package com.ebizprise.winw.project.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.service.AuditInform;
import com.ebizprise.winw.project.service.IAuditInformService;
import com.ebizprise.winw.project.vo.AuditInformVO;

/**
 * 每日稽核通知排程(事件單)
 * @author Bernard.Yu 2020/08/27, adam.yeh
 */
public class AuditInformIncDailyJob extends QuartzJobFactory implements AuditInform {

    @Autowired
    private IAuditInformService service;
    
    @Override
    public String executeJob(JobExecutionContext jec) throws Exception {
        List<AuditInformVO> dataList = getAuditFormList();
        List<AuditNotifyParamsEntity> anParams = service.getANParams(FormVerifyType.INC.name());
        List<AuditInformVO> checkedList = service.checkList(dataList, anParams);
        
        return service.sendMail(checkedList, AuditInform.MAIL_TEMPLATE_INC, jec.getJobDetail().getKey().getName());
    }
    
    @Override
    public List<AuditInformVO> getAuditFormList () throws Exception {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_INC.getResource("FIND_INC_AUDIT_INFORM_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        
        List<String> eventPriority = new ArrayList<String>();
        eventPriority.add("3");
        eventPriority.add("4");
        conditions.and().in("FIID.EventPriority", eventPriority);
        
        List<String> formStatus = new ArrayList<String>();
        formStatus.add(FormEnum.CLOSED.name());
        formStatus.add(FormEnum.PROPOSING.name());
        formStatus.add(FormEnum.DEPRECATED.name());
        conditions.and().notIn("F.FormStatus", formStatus);
        
        return jdbcRepository.queryForList(resource, conditions, AuditInformVO.class);
    }
    
}
