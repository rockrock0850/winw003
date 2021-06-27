package com.ebizprise.winw.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;

@Repository("formJobInfoDateRepository")
public interface IFormJobInfoDateRepository extends JpaRepository<FormJobInfoDateEntity, Long> {

    public void deleteByFormId (String formId);
    
    public List<FormJobInfoDateEntity> findByFormIdIn(List<String> formId);

    public FormJobInfoDateEntity findByFormId (String formId);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormJobInfoDateEntity SET CCT = :cct WHERE FormId IN :formIds")
    public void updateCctByFormIdIn(@Param("cct") Date ect, @Param("formIds") List<String> apJobIds);
    
}