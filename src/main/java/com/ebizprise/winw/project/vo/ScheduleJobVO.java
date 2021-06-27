package com.ebizprise.winw.project.vo;

import java.io.Serializable;
import java.util.Date;

import org.quartz.JobDataMap;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class ScheduleJobVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String jobName;        // 排程識別碼
    private String jobDescription; // 排程名稱      
    private Date lastFireTime;     // 開始時間、首次執行時間
    private String nextFireTime;   // 下次執行時間
    private int timeUnit;          // 排程執行頻率
    private String exeStatus;      // 執行狀態
    private int status;            // 使用狀態
	private String jobGroup;       // 工作群組  
	private String jobClass;       // 工作所執行的程式名稱
	private int repeatInterval;    // 重複執行的時間區間       
	private int executeTimes;      // 執行次數 
    private String message;        // 備註
    private Date startTime;        // 排程開始時間
    private Date endTime;          // 排程結束時間
    private JobDataMap jobDataMap;   

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String group) {
		this.jobGroup = group;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String description) {
		this.jobDescription = description;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String classPath) {
		this.jobClass = classPath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(int interval) {
		this.repeatInterval = interval;
	}

	public int getExecuteTimes() {
		return executeTimes;
	}

	public void setExecuteTimes(int repeatTimes) {
		this.executeTimes = repeatTimes;
	}

	public int getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}

	public JobDataMap getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

    public String getExeStatus () {
        return exeStatus;
    }

    public void setExeStatus (String exeStatus) {
        this.exeStatus = exeStatus;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public String getJobGoup () {
        return jobGroup;
    }

    public void setJobGoup (String jobGoup) {
        this.jobGroup = jobGoup;
    }

    public Date getLastFireTime () {
        return lastFireTime;
    }

    public void setLastFireTime (Date lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public String getNextFireTime () {
        return nextFireTime;
    }

    public void setNextFireTime (String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getStartTime () {
        return startTime;
    }

    public void setStartTime (Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime () {
        return endTime;
    }

    public void setEndTime (Date endTime) {
        this.endTime = endTime;
    }

}
