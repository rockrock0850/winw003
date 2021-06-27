package com.ebizprise.winw.project.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInfoCDetailEntity;

@Repository("formInfoCDetailRepository")
public interface IFormInfoCDetailRepository extends JpaRepository<FormInfoCDetailEntity, Long> {

    public FormInfoCDetailEntity findByFormId (String formId);

    public void deleteByFormId (String formId);
    
    @Modifying
    @Query("UPDATE FormInfoCDetailEntity Set content = :content, updatedBy = :userId, updatedAt = :updatedAt WHERE formId = :formId")
    public void updateContent (
            @Param("content") String content,
            @Param("userId") String userId,
            @Param("updatedAt") Date updatedAt,
            @Param("formId") String formId);
}
