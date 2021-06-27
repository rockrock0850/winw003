package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.SysHolidayEntity;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sysHolidayRepository")
public interface ISysHolidayRepository extends JpaRepository<SysHolidayEntity, Long> {

    public List<SysHolidayEntity> findByYear (String year);

    public List<SysHolidayEntity> findByYearOrderById (String year);

    public int deleteByYear (String year);

    public int countByDateAndIsHoliday (Date d, String isHoliday);

}