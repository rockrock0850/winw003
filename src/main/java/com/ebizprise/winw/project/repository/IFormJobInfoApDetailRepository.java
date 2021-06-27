package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobInfoApDetailEntity;

@Repository("formJobInfoApDetailRepository")
public interface IFormJobInfoApDetailRepository extends JpaRepository<FormJobInfoApDetailEntity, Long> {

    public void deleteByFormId (String formId);

    public FormJobInfoApDetailEntity findByFormId (String formId);
    
}