package com.ebizprise.winw.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormEntity;

@Repository("formRepository")
public interface IFormRepository extends JpaRepository<FormEntity, Long> {

    public FormEntity findByFormId (String formId);

    public FormEntity findTop1ByFormIdOrderByIdAsc(String formId);

    public List<FormEntity> findBySourceId (String sourceId);

    public List<FormEntity> findBySourceIdAndFormClass(String sourceId, String formClass);

    public void deleteByFormId (String formId);

    public List<FormEntity> findByFormIdAndFormClass (String formId, String formClass);

    public int countBySourceIdAndFormStatusNotIn (String formId, List<String> formStatusLs);
    
    public int countBySourceIdAndFormStatusNotInAndFormClassNotIn(String formId, List<String> formStatusNoInList,List<String> formClassNoInList);

    public List<FormEntity> findBySourceIdAndFormStatusIn(String scouceId, List<String> formStatusLs);

    @Modifying
    @Query("UPDATE FormEntity Set formStatus = :status, processStatus = :status, updatedBy = :userId, updatedAt = :updatedAt WHERE formId = :formId")
    public int updateStatusByFormId(
            @Param("formId") String formId,
            @Param("status") String status,
            @Param("userId") String userId,
            @Param("updatedAt") Date updatedAt);
    
    @Query("SELECT f FROM FormEntity f WHERE f.sourceId = :sourceId AND f.formId LIKE %:formId%")
    public List<FormEntity> findBySourceIdAndFormId(
            @Param("sourceId") String sourceId,
            @Param("formId") String formId);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormEntity SET IsExtended = :isExtended WHERE FormId = :formId")
    public void updateIsExtendedByFormId(@Param("isExtended")String isExtended, @Param("formId")String formId);

}