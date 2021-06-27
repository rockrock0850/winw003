package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormImpactAnalysisEntity;

@Repository("formImpactAnalysisRepository")
public interface IFormImpactAnalysisRepository extends JpaRepository<FormImpactAnalysisEntity, Long> {

    public void deleteByFormId(String formId);

    public List<FormImpactAnalysisEntity> findByFormId(String formId);

    public List<FormImpactAnalysisEntity> findByFormIdOrderByIdAsc (String formId);
    
}