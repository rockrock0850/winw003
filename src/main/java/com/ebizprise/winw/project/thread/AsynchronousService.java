package com.ebizprise.winw.project.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 非同步化服務類別
 * @author adam.yeh
 */
@Transactional
@Service("asynchronousService")
public class AsynchronousService {

    @Autowired
    private TaskExecutor executor;
    
    /**
     * 執行表單流程期間需要寄發的通知信
     * @author adam.yeh
     */
    public void execProcessMail (ProcessMail mail) {
        SpringBeanAutowiringSupport.
                processInjectionBasedOnCurrentContext(mail);
        executor.execute(mail);
    }

}
