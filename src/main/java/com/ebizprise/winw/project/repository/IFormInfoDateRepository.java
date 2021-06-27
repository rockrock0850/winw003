package com.ebizprise.winw.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormInfoDateEntity;

@Repository("formInfoDateRepository")
public interface IFormInfoDateRepository extends JpaRepository<FormInfoDateEntity, Long> {

    public void deleteByFormId (String formId);

    public FormInfoDateEntity findByFormId(String formId);
    
    public List<FormInfoDateEntity> findByFormIdIn(List<String> formId);

    @Transactional
    @Modifying
    @Query("UPDATE FormInfoDateEntity SET IsSpecial = :isSpecial ,SpecialEndCaseType = :specialEndCaseType WHERE FormId = :formId")
    public void updateIsSpecialByFormId(@Param("formId")String formId,@Param("isSpecial")String isSpecial,@Param("specialEndCaseType")String specialEndCaseType);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormInfoDateEntity SET CCT = :cct WHERE FormId IN :formIds")
    public void updateCctByFormIdIn(@Param("cct") Date cct, @Param("formIds") List<String> formIds);

    @Transactional
    @Modifying
    @Query("UPDATE FormInfoDateEntity SET MECT = :mect WHERE FormId IN :formIds")
    public void updateMectByFormIdIn (@Param("mect")Date ect,@Param("formIds") List<String> countersignedIds);

    @Transactional
    @Modifying
    @Query("UPDATE FormInfoDateEntity SET OECT = :oect WHERE FormId = :formId")
    public void updateOectByFormId(@Param("oect") Date oect, @Param("formId") String formId);

}
