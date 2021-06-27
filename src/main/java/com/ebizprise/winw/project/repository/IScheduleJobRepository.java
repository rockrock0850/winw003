package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.ScheduleJobEntity;
import com.ebizprise.winw.project.vo.ScheduleJobVO;

@Repository("scheduleJobRepository")
public interface IScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long> {
    
	public ScheduleJobEntity findById(int id);

	public ScheduleJobEntity findByJobName(String jobName);

    public List<ScheduleJobVO> findByJobNameContaining (String jobName);

    public void deleteByJobName (String jobName);
    
}