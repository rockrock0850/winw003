package com.ebizprise.winw.project.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInfoSrDetailEntity;

@Repository("formInfoSrDetailRepository")
public interface IFormInfoSrDetailRepository extends JpaRepository<FormInfoSrDetailEntity, Long> {

    public void deleteByFormId (String formId);

    public FormInfoSrDetailEntity findByFormId (String formId);

    @Modifying
    @Query("UPDATE FormInfoSrDetailEntity Set countersigneds = :countersigneds, updatedBy = :userId, updatedAt = :updatedAt WHERE formId = :formId")
    public void updateCountersigneds (
            @Param("countersigneds") String countersigneds,
            @Param("userId") String userId,
            @Param("updatedAt") Date updatedAt,
            @Param("formId") String formId);
}
