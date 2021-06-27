package com.ebizprise.winw.project.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * @author gary.tsai 2019/6/14
 */
@Entity
@Table(name = "SCHEDULE_JOB")
public class ScheduleJobEntity extends BaseEntity {

	private Long id;
	private String jobName;
	private String jobGroup;
	private String jobDescription;
	private String jobClass;
	private Integer status;
	private Integer timeUnit;
	private Integer repeatInterval;
	private Integer executeTimes;
	private Date lastFireTime;
	private Date nextFireTime;

	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "JobName")
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Basic
	@Column(name = "JobGroup")
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	@Basic
	@Column(name = "JobDescription")
	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	@Basic
	@Column(name = "JobClass")
	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	@Basic
	@Column(name = "Status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Basic
	@Column(name = "TimeUnit")
	public Integer getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Basic
	@Column(name = "RepeatInterval")
	public Integer getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	@Basic
	@Column(name = "ExecuteTimes")
	public Integer getExecuteTimes() {
		return executeTimes;
	}

	public void setExecuteTimes(Integer executeTimes) {
		this.executeTimes = executeTimes;
	}

	@Basic
	@Column(name = "LastFireTime")
	public Date getLastFireTime() {
		return lastFireTime;
	}

	public void setLastFireTime(Date lastFireTime) {
		this.lastFireTime = lastFireTime;
	}

	@Basic
	@Column(name = "NextFireTime")
	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ScheduleJobEntity that = (ScheduleJobEntity) o;
		return id == that.id && Objects.equals(jobName, that.jobName) && Objects.equals(jobGroup, that.jobGroup)
				&& Objects.equals(jobDescription, that.jobDescription) && Objects.equals(jobClass, that.jobClass)
				&& Objects.equals(status, that.status) && Objects.equals(timeUnit, that.timeUnit)
				&& Objects.equals(repeatInterval, that.repeatInterval)
				&& Objects.equals(executeTimes, that.executeTimes) && Objects.equals(lastFireTime, that.lastFireTime)
				&& Objects.equals(nextFireTime, that.nextFireTime) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, jobName, jobGroup, jobDescription, jobClass, status, timeUnit, repeatInterval,
				executeTimes, lastFireTime, nextFireTime, this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(),
				this.getCreatedAt());
	}
}
