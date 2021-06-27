package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SysMailLogEntity;

@Repository("sysMailLogRepository")
public interface ISysMailLogRepository extends JpaRepository<SysMailLogEntity, Long> {
}