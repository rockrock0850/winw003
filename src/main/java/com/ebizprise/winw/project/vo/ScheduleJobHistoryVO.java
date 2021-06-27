package com.ebizprise.winw.project.vo;

import java.io.Serializable;
import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class ScheduleJobHistoryVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String jobName;                         // 排程識別碼
	private String jobDescription;                         // 排程識別碼
	private List<ScheduleJobVO> scheduleJobVOList;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<ScheduleJobVO> getScheduleJobVOList() {
		return scheduleJobVOList;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public void setScheduleJobVOList(List<ScheduleJobVO> scheduleJobVOList) {
		this.scheduleJobVOList = scheduleJobVOList;
	}
}
