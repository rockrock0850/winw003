package com.ebizprise.winw.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.ScheduleJobLogEntity;

@Repository("scheduleJobLogRepository")
public interface IScheduleJobLogRepository extends JpaRepository<ScheduleJobLogEntity, Long> {

    public ScheduleJobLogEntity findById(int id);

    @Query("SELECT SJOBLOG FROM ScheduleJobLogEntity SJOBLOG WHERE SJOBLOG.jobName = :jobName ORDER BY SJOBLOG.id DESC")
    public List<ScheduleJobLogEntity> findByJobName(@Param("jobName") String jobName);

    public List<ScheduleJobLogEntity> findAllByEndTimeBefore(@Param("delDate") Date delDate);
}