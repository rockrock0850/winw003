package com.ebizprise.winw.project.schedule;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.ctrl.JobTimer;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.service.startup.FormLockHelper;

/**
 * 表單 共用後台排程
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年9月9日
 */
@Service
public class CommonFormScheduleJob {

    private static final String SYS_SCHEDULE = "SystemSchedule";
    
    private static final Logger logger = LoggerFactory.getLogger(CommonFormScheduleJob.class);
    
    @Autowired
    private JdbcRepositoy jdbcRepository;
    @Autowired
    private FormLockHelper helper;
    
    /**
     * 
     * 掃描所有表單狀態為觀察中的表單,若其觀察時間已超過,則將其狀態改為結案
     * 目前為每天的00:00啟動
     * 
     */
    @Scheduled(cron="${commonformschedulejob.updateWatchingFormStatus}")
    public void updateWatchingFormStatus() {
        JobTimer timer = new JobTimer().start("updateWatchingFormStatus");
        
        Date currentDate = new Date();
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("UPDATE_FORM_WATCH_STATUS_TO_CLOSED");
        
        Map<String,Object> params = new HashMap<>();
        params.put("closed", FormEnum.CLOSED.name());
        params.put("watchingStatus",FormEnum.WATCHING.name());
        params.put("currentDate", currentDate);
        params.put("updatedBy", SYS_SCHEDULE);
        
        int count = jdbcRepository.update(resource, params);
        
        logger.info(count + " data was updated");
        timer.stop();
    }
    
    /**
     * 每十分鐘檢查表單鎖定控制器內的表單是否已過期<br>
     * 是, 則解除開表單的控制鎖<br>
     * 否, 則繼續觀察
     * 
     * @author adam.yeh
     */
    @Scheduled(cron="${commonformschedulejob.formlockchecker}")
    public void formLockChecker () throws Throwable {
        JobTimer timer = new JobTimer().start("formLockChecker");

        Object vo;
        Class<?> clazz;
        ClassLoader loader;
        Method getUpdatedAt;
        Date d = new Date();
        long today, previous, duration;
        long max = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);
        
        for (String key : helper.getLocks()) {
            vo = helper.get(key);
            today = d.getTime();
            loader = vo.getClass().getClassLoader();
            clazz = loader.loadClass(vo.getClass().getName());
            getUpdatedAt = clazz.getMethod("getUpdatedAt");
            previous = ((Date) getUpdatedAt.invoke(vo)).getTime();
            duration = today - previous;
            
            if (duration > max) {
                helper.unlock(key);
            }
        }

        d = null;
        timer.stop();
    }
    
}
