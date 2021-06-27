package com.ebizprise.winw.project.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.service.impl.HolidayImportService;
import com.ebizprise.winw.project.service.impl.QuestionFormServiceImpl;
import com.ebizprise.winw.project.vo.QuestionFormVO;

/**
 * 問題單觀察期過期通知
 * 
 * @author jacky.fu
 *
 */
public class NotificationObsExpiredQJob extends QuartzJobFactory {

    private static final Logger logger = LoggerFactory.getLogger(NotificationObsExpiredQJob.class);
    
    @Autowired
    QuestionFormServiceImpl questionFormService;
    @Autowired
    HolidayImportService holidayImportService;
    
    @Override
    public String executeJob (JobExecutionContext jec) throws Exception {
        if (holidayImportService.isHolidayByDate(new Date())) {
            logger.info("not execute on holidays");
            return "例假日不寄送";
        }
        
        List<QuestionFormVO> res = questionFormService.getQFormObsExpired(new QuestionFormVO());
        for(QuestionFormVO vo : res) {
            questionFormService.sendMailToVice(vo);
        }
        
        return StringUtils.EMPTY;
    }

}
