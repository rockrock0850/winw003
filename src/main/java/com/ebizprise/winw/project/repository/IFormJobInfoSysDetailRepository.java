package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobInfoSysDetailEntity;

@Repository("formJobInfoSysDetailRepository")
public interface IFormJobInfoSysDetailRepository extends JpaRepository<FormJobInfoSysDetailEntity, Long> {

    public void deleteByFormId (String formId);

    public FormJobInfoSysDetailEntity findByFormId (String formId);
    
}