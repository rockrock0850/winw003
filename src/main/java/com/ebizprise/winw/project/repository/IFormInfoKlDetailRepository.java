package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInfoKlDetailEntity;

@Repository("formInfoKlDetailRepository")
public interface IFormInfoKlDetailRepository extends JpaRepository<FormInfoKlDetailEntity, Long> {

    public FormInfoKlDetailEntity findByFormId (String formId);
    
}
