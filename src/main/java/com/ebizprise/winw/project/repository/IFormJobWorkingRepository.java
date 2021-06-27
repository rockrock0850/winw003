package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobWorkingEntity;

@Repository("formJobWorkingRepository")
public interface IFormJobWorkingRepository extends JpaRepository<FormJobWorkingEntity, Long> {

    public FormJobWorkingEntity findByFormIdAndType (String formId, String type);

    public FormJobWorkingEntity findByIdAndFormId (Long id, String formId);
    
}