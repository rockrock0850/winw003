package com.ebizprise.winw.project.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SysGroupPermissionEntity;

@Repository("sysGroupPermissionRepository")
public interface ISysGroupPermissionRepository extends JpaRepository<SysGroupPermissionEntity, Long> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<SysGroupPermissionEntity> findBySysGroupId(Long sysGroupId);

    public void deleteBySysGroupId(Long sysGroupId);
}
