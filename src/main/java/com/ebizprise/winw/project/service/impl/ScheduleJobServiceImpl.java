package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.ScheduleJobEntity;
import com.ebizprise.winw.project.entity.ScheduleJobLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.ScheduleJobJDBC;
import com.ebizprise.winw.project.repository.IScheduleJobLogRepository;
import com.ebizprise.winw.project.repository.IScheduleJobRepository;
import com.ebizprise.winw.project.service.IScheduleJobService;
import com.ebizprise.winw.project.vo.ScheduleJobVO;

@Transactional
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends BaseService implements IScheduleJobService {

    @Autowired
    private IScheduleJobRepository scheduleJobRepository;

    @Autowired
    private IScheduleJobLogRepository scheduleJobLogRepository;

    @Autowired
    private ScheduleJobJDBC jdbc;
    
    @Override
    public List<ScheduleJobVO> findAllJobs() {
        ResourceEnum resource = ResourceEnum.SQL_JOBS.getResource("FIND_ALL_JOBS");
        List<ScheduleJobVO> detailList = jdbc.queryForList(resource, ScheduleJobVO.class);
        
        return detailList;
    }

    @Override
    public List<ScheduleJobVO> findJobs(ScheduleJobVO jobVO) {
        ResourceEnum resource = ResourceEnum.SQL_JOBS.getResource("FIND_CONTAINING_JOBS");

        Map<String, Object> params = new HashMap<>();
        params.put("jobDescription", jobVO.getJobDescription());
        List<ScheduleJobVO> detailList = jdbc.queryForList(resource, params, ScheduleJobVO.class);

        return detailList;
    }

    @Override
    public ScheduleJobVO findByJobName (ScheduleJobVO jobVO) {
        ResourceEnum resource = ResourceEnum.SQL_JOBS.getResource("FIND_JOB");
        
        Map<String, Object> params = new HashMap<>();
        params.put("jobName", jobVO.getJobName());
        
        return jdbc.queryForBean(resource, params, ScheduleJobVO.class);
    }

    @Override
    public void saveOrUpdate(ScheduleJobVO jobVO) {
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        BeanUtils.copyProperties(jobVO, scheduleJobEntity);
        scheduleJobRepository.save(scheduleJobEntity);
    }

    @Override
    public void delete (ScheduleJobVO scheduleJobDetail) {
        ScheduleJobEntity entity = new ScheduleJobEntity();
        BeanUtils.copyProperties(scheduleJobDetail, entity);
        scheduleJobRepository.deleteByJobName(entity.getJobName());
    }

    @Override
    public List<ScheduleJobVO> findJobHistory(ScheduleJobVO jobVO) {
        List<ScheduleJobLogEntity> entityList = scheduleJobLogRepository.findByJobName(jobVO.getJobName());
        return BeanUtil.copyList(entityList, ScheduleJobVO.class);
    }

    @Override
    public void saveJob(ScheduleJobVO detail) {
        ScheduleJobEntity entity = scheduleJobRepository.findByJobName(detail.getJobName());

        if (entity == null) {
            entity = new ScheduleJobEntity();
        }

        BeanUtil.copyProperties(detail, entity);
        if (!StringUtils.isBlank(detail.getNextFireTime())) {
            entity.setNextFireTime(
                    new Date(Long.valueOf(detail.getNextFireTime())));
        }
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(fetchLoginUser().getName());
        scheduleJobRepository.save(entity);
    }
    
}
