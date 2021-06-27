package com.ebizprise.winw.project.test.repository;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ebizprise.winw.project.entity.ScheduleJobEntity;
import com.ebizprise.winw.project.repository.IScheduleJobRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class ScheduleJobRepositoryTest extends TestBase {

	@Autowired
	private IScheduleJobRepository scheduleJobEntityRepository;

	@Test
	public void testFindAllJobs() {
    List<ScheduleJobEntity> scheduleJobEntityList = scheduleJobEntityRepository.findAll();
    Assert.notEmpty(scheduleJobEntityList, "Not null!!");
	}
}