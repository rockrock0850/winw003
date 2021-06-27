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
 * 每日稽核通知排程(問題單)
 * @author Bernard.Yu 2020/08/27, adam.yeh
 */
public class AuditInformQJob extends QuartzJobFactory implements AuditInform {

    @Autowired
    private IAuditInformService service;
    
    @Override
    public String executeJob(JobExecutionContext jec) throws Exception {
        List<AuditInformVO> dataList = getAuditFormList();
        List<AuditNotifyParamsEntity> anParams = service.getANParams(FormVerifyType.Q.name());
        List<AuditInformVO> checkedList = service.checkList(dataList, anParams);
        
        return service.sendMail(checkedList, AuditInform.MAIL_TEMPLATE, jec.getJobDetail().getKey().getName());
    }
    
    @Override
    public List<AuditInformVO> getAuditFormList () throws Exception {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_Q.getResource("FIND_Q_AUDIT_INFORM");
        Conditions conditions = new Conditions();
        
        List<String> formStatus = new ArrayList<String>();
        formStatus.add(FormEnum.CLOSED.name());
        formStatus.add(FormEnum.PROPOSING.name());
        formStatus.add(FormEnum.DEPRECATED.name());
        formStatus.add(FormEnum.WATCHING.name());//問題單的觀察中不列入清單
        conditions.and().notIn("F.FormStatus", formStatus);
        
        return jdbcRepository.queryForList(resource, conditions, AuditInformVO.class);
    }
    
}
