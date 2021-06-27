package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInternalProcessStatusEntity;

@Repository("formInternalProcessStatusRepository")
public interface IFormInternalProcessStatusRepository extends JpaRepository<FormInternalProcessStatusEntity, Long> {

    public void deleteByFormId(String formId);
    
    public void deleteByFormIdAndIsProcessDone(String formId, String isProcessDone);

    public List<FormInternalProcessStatusEntity> findByFormId(String formId);

    public List<FormInternalProcessStatusEntity> findByFormIdOrderByIdAsc (String formId);
    
    @Query("FROM FormInternalProcessStatusEntity fips WHERE fips.isProcessDone = :isProcessDone AND fips.formId = :formId")
    public List<FormInternalProcessStatusEntity> findByFormIdAndIsProcessDone(@Param("formId")String formId, @Param("isProcessDone")String isProcessDone);
    
    @Modifying
    @Query("UPDATE FormInternalProcessStatusEntity fips SET fips.isProcessDone = :isProcessDone WHERE fips.formId = :formId")
    public void updateIsProcessDoneByFormId(@Param("formId")String formId, @Param("isProcessDone")String isProcessDone);
    
    @Modifying
    @Query("UPDATE FormInternalProcessStatusEntity fips SET fips.isProcessDone = :isProcessDone WHERE fips.formId = :formId AND fips.division = :division")
    public void updateIsProcessDoneByFormIdAndDivision(@Param("formId")String formId, @Param("isProcessDone")String isProcessDone, @Param("division")String division);
    
}