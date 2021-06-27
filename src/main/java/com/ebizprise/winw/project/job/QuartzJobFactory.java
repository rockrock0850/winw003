package com.ebizprise.winw.project.job;

import com.ebizprise.winw.project.entity.ScheduleJobLogEntity;
import com.ebizprise.winw.project.enums.JobStatusEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.repository.IScheduleJobLogRepository;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Date;

@Transactional
public abstract class QuartzJobFactory extends QuartzJobBean {
    
	private static final Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);

	@Autowired
	protected Environment env;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	protected JdbcRepositoy jdbcRepository;
	@Autowired
	private IScheduleJobLogRepository jobLogRepo;
	
	@Override
	protected void executeInternal(JobExecutionContext jobCtx) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		ScheduleJobLogEntity jobLog = new ScheduleJobLogEntity();
		jobLog.setStartTime(new Date());
		jobLog.setJobName(jobCtx.getJobDetail().getKey().getName());
		jobLog.setJobDescription(jobCtx.getJobDetail().getDescription());
		
		try {
			jobLog.setStatus(JobStatusEnum.RUNNING.status());
			jobLogRepo.saveAndFlush(jobLog);
			logger.info(getMessage("job.start.info", new String[]{jobLog.getJobDescription()}));
			jobLog.setMessage(executeJob(jobCtx));
			logger.info(getMessage("job.complete.info", new String[]{jobLog.getJobDescription()}));
			jobLog.setStatus(JobStatusEnum.COMPLETE.status());
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
			jobLog.setStatus(JobStatusEnum.ERROR.status());
			jobLog.setMessage(e.getClass().getName() + "," + e.getMessage());
		} finally {
			jobLog.setEndTime(new Date());
			jobLogRepo.saveAndFlush(jobLog);
		}
	}

	protected String getMessage(String key, String[] args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}

	protected abstract String executeJob (JobExecutionContext jobCtx) throws Exception;
	
}
