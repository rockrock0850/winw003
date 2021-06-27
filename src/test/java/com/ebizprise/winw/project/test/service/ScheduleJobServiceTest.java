package com.ebizprise.winw.project.test.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.service.IScheduleJobService;
import com.ebizprise.winw.project.test.base.TestBase;
import com.ebizprise.winw.project.vo.ScheduleJobVO;

public class ScheduleJobServiceTest extends TestBase {

	@Autowired
	private IScheduleJobService scheduleJobService;

	@Test
	public void testFindAllJobs(){
		List<ScheduleJobVO> scheduleJobEntityList = scheduleJobService.findAllJobs();
		ScheduleJobVO scheduleJobVO = new ScheduleJobVO();
		scheduleJobVO.setJobName("test");
		scheduleJobVO.setMessage("測試");
		scheduleJobService.saveOrUpdate(scheduleJobVO);
		scheduleJobService.findAllJobs();
		Assert.assertNotNull(scheduleJobEntityList);
	}
}
