package com.ebizprise.winw.project.job;

import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.ScheduleEnum;
import com.ebizprise.winw.project.enums.JobStatusEnum;
import com.ebizprise.winw.project.vo.ScheduleJobVO;

/**
 * 排程基礎類別
 * 
 * @author gary.tsai, adam.yeh
 *
 */
public abstract class BaseJob extends BaseController {
    
	private static final Logger logger = LoggerFactory.getLogger(BaseJob.class);

	@Autowired
	private Scheduler scheduler;

	/**
	 * 只執行一次
	 * 
	 * @param detail
	 */
	protected void executeJobOnce (ScheduleJobVO detail) {
	    scheduleJobStatusProcess(detail, JobStatusEnum.ONCE);
    }

	/**
	 * 刪除排程
	 * 
	 * @param scheduleJobDetail
	 * @return
	 */
	protected ScheduleJobVO deleteJobProcess(ScheduleJobVO scheduleJobDetail) {
		scheduleJobDetail = this.scheduleJobStatusProcess(scheduleJobDetail, JobStatusEnum.DELETE);
		return scheduleJobDetail;
	}

	/**
	 * 停止排程
	 * 
	 * @param scheduleJobDetail
	 * @return
	 */
	protected ScheduleJobVO suspendJobProcess(ScheduleJobVO scheduleJobDetail) {
		scheduleJobDetail = this.scheduleJobStatusProcess(scheduleJobDetail, JobStatusEnum.SUSPEND);
		return scheduleJobDetail;
	}

	/**
	 * 重新啟動排程
	 * 
	 * @param scheduleJobDetail
	 * @return
	 */
	protected ScheduleJobVO resumeJobProcess(ScheduleJobVO scheduleJobDetail) {
		scheduleJobDetail = this.scheduleJobStatusProcess(scheduleJobDetail, JobStatusEnum.RESUME);
		return scheduleJobDetail;
	}

	/**
	 * 新增或更新排程
	 * 
	 * @param scheduleJobDetail
	 * @return
	 */
	protected ScheduleJobVO mergeJobProcess(ScheduleJobVO scheduleJobDetail) {
        scheduleJobDetail = this.scheduleJobStatusProcess(scheduleJobDetail, JobStatusEnum.MERGE);
		return scheduleJobDetail;
	}

    @SuppressWarnings({ "incomplete-switch", "static-access" })
    private ScheduleJobVO scheduleJobStatusProcess (
            ScheduleJobVO scheduleJobDetail, JobStatusEnum scheduleJobStatusEnum) {
        int status = scheduleJobDetail.getStatus();
		JobKey jobKey = JobKey.jobKey(scheduleJobDetail.getJobName(), scheduleJobDetail.getJobGroup());
		
		try {
			switch (scheduleJobStatusEnum) {
    			case MERGE:
                    String time = scheduleJobDetail.getNextFireTime();
                    Date nextFireTime = new Date(Long.valueOf(time));
    		        
    				if (!scheduler.checkExists(jobKey)) {
                        this.createJob(nextFireTime, scheduleJobDetail);
    				} else {
    				    this.updateTrigger(jobKey, nextFireTime, scheduleJobDetail);
    				}
    				
    				break;
    				
    			case SUSPEND:
    				scheduler.pauseJob(jobKey);
    				break;
    				
    			case RESUME:
    				scheduler.resumeJob(jobKey);
    				break;
    				
    			case DELETE:
    				scheduler.deleteJob(jobKey);
    				break;

                case ONCE:
                    if (scheduleJobStatusEnum.SUSPEND.status() == status) {
                        String message = getMessage("job.exception.message.2", scheduleJobDetail.getJobDescription());
                        scheduleJobDetail.setValidateLogicError(message);
                    } else {
                        this.executeOnce(jobKey, scheduleJobDetail);
                    }
                    
                    break;
			}
		} catch (Exception e) {
		    String message = getMessage("job.exception.message.1", scheduleJobDetail.getJobName());
		    scheduleJobDetail.setMessage(message);
		    logger.error(message, e);
		}

		return scheduleJobDetail;
	}

    private ScheduleJobVO createJob (Date nextFireTime, ScheduleJobVO scheduleJobDetail) throws Exception {
        JobDetail jobDetail = buildJobDetail(scheduleJobDetail);
        Trigger trigger = buildJobTrigger(jobDetail, nextFireTime, scheduleJobDetail);
        scheduler.scheduleJob(jobDetail, trigger);
        logger.debug("Job created: " + jobDetail);

        return scheduleJobDetail;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JobDetail buildJobDetail (ScheduleJobVO scheduleJobDetail) throws Exception {
        Class clazz = Class.forName(scheduleJobDetail.getJobClass());

        if (null == scheduleJobDetail.getJobDataMap()) {
            scheduleJobDetail.setJobDataMap(new JobDataMap());
        }

        return JobBuilder
                .newJob(clazz)
                .withIdentity(scheduleJobDetail.getJobName(), scheduleJobDetail.getJobGroup())
                .withDescription(scheduleJobDetail.getJobDescription())
                .usingJobData(scheduleJobDetail.getJobDataMap())
                .storeDurably()
                .build();
    }

    @SuppressWarnings("unchecked")
    private Trigger buildJobTrigger (
            JobDetail jobDetail, Date startTime, ScheduleJobVO scheduleJobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
                .withDescription(scheduleJobDetail.getJobDescription())
                .startAt(startTime)
                .endAt(null)
                .withSchedule(ScheduleEnum.getTimeUnit(scheduleJobDetail.getTimeUnit())
                .setCycle(scheduleJobDetail.getRepeatInterval(), scheduleJobDetail.getExecuteTimes()))
                .build();
    }

    @SuppressWarnings("unchecked")
    private void updateTrigger (JobKey jobKey, Date nextFireTime, ScheduleJobVO newDetail) throws Exception {
        JobDetail oriDetail = scheduler.getJobDetail(jobKey);
        TriggerKey triggerKey = TriggerKey.triggerKey(oriDetail.getKey().getName(), oriDetail.getKey().getGroup());  
        Trigger trigger = scheduler.getTrigger(triggerKey);
        
        /*
         * 若是在編輯畫面選擇執行頻率「一次」的話會導致舊的Trigger因為已經跑完而銷毀, 
         * 且已銷毀Trigger的JobDetail無法refresh新的Trigger, 
         * 所以直接砍掉重建。
         */
        if (trigger == null) {
            scheduler.deleteJob(jobKey);
            createJob(nextFireTime, newDetail);
        } else {
            trigger = trigger
                        .getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .startAt(nextFireTime)
                        .endAt(null)
                        .withSchedule(ScheduleEnum.getTimeUnit(newDetail.getTimeUnit())
                        .setCycle(newDetail.getRepeatInterval(), newDetail.getExecuteTimes()))
                        .build();
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.debug("Job existed but update trigger: " + trigger);
        }
    }

    private void executeOnce (JobKey jobKey, ScheduleJobVO scheduleJobDetail) throws Exception {
        JobDetail detail = scheduler.getJobDetail(jobKey);
        
        // 若是新的排程則新建一筆資料進quartz table。 
        if (detail == null) {
            createJob(new Date(), scheduleJobDetail);
            detail = scheduler.getJobDetail(jobKey);
        }
        
        Trigger newTrigger = TriggerBuilder
                                .newTrigger()
                                .startNow()
                                .forJob(detail)
                                .withIdentity(UUID.randomUUID().toString())
                                .withDescription(scheduleJobDetail.getJobDescription())
                                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1, 1))
                                .build();
        
        scheduler.scheduleJob(newTrigger);
    }
    
}
