package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;

@Repository
public interface IAuditNotifyParamsRepository extends JpaRepository<AuditNotifyParamsEntity, Long> {
    
    public List<AuditNotifyParamsEntity> findByFormType(String formType);
    
}