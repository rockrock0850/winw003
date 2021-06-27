package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebizprise.winw.project.entity.SysParameterLogEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("sysParameterLogRepository")
public interface ISysParameterLogRepository extends JpaRepository<SysParameterLogEntity, Long> {
	public SysParameterLogEntity findByUserId(String userId);

	public List<SysParameterLogEntity> findAllByUpdatedAtBefore(@Param("delDate") Date delDate);
}
