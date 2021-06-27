package com.ebizprise.winw.project.entity;

import com.ebizprise.winw.project.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author gary.tsai 2019/6/14
 */
@Entity
@Table(name = "SCHEDULE_JOB_LOG")
public class ScheduleJobLogEntity extends BaseEntity {

	private Long id;
	private String jobName;
	private String jobDescription;
	private Integer status;
	private Date startTime;
	private Date endTime;
	private String message;

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
	@Column(name = "JobDescription")
	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
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
	@Column(name = "StartTime")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Basic
	@Column(name = "EndTime")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Basic
	@Column(name = "Message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ScheduleJobLogEntity that = (ScheduleJobLogEntity) o;
		return id == that.id && Objects.equals(jobName, that.jobName)
				&& Objects.equals(jobDescription, that.jobDescription) && Objects.equals(status, that.status)
				&& Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime)
				&& Objects.equals(message, that.message) && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, jobName, jobDescription, status, startTime, endTime, message, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
