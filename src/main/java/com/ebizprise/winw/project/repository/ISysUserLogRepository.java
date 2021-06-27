package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.SysUserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("sysUserLogRepository")
public interface ISysUserLogRepository extends JpaRepository<SysUserLogEntity, Long> {

	public SysUserLogEntity findByUserId(String userId);

	public SysUserLogEntity findTop1ByUserIdAndStatusOrderByTimeDesc(String userId, String status);

	public List<SysUserLogEntity> findAllByTimeBefore(@Param("delDate") Date delDate);
}
