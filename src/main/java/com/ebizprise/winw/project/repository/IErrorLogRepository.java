package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.ErrorLogEntity;

import java.util.Date;
import java.util.List;

@Repository("errorLogRepository")
public interface IErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {

	public ErrorLogEntity findByServerIp(String serverIp);

	public List<ErrorLogEntity> findAllByTimeBefore(@Param("delDate") Date delDate);
}