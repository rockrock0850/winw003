package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SysOptionRoleEntity;

@Repository("sysOptionRoleRepository")
public interface ISysOptionRoleRepository extends JpaRepository<SysOptionRoleEntity, Long> {
    
    public SysOptionRoleEntity findByOptionIdAndValue(String optionId, String value);
    
}