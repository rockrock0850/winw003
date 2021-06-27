package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormProgramEntity;

@Repository("formProgramRepository")
public interface IFormProgramRepository extends JpaRepository<FormProgramEntity, Long> {

    FormProgramEntity findByFormId (String formId);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormProgramEntity SET IsSuggestCase = 'Y' WHERE FormId = :formId")
    public void updateIsSuggestCaseByFormId(@Param("formId") String formId);

}
