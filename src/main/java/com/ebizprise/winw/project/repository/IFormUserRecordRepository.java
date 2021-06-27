package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormUserRecordEntity;

@Repository("formUserRecordRepository")
public interface IFormUserRecordRepository extends JpaRepository<FormUserRecordEntity, Long> {

    public List<FormUserRecordEntity> findAllByFormIdOrderByUpdatedAtDesc(String formId);

    public FormUserRecordEntity findByIdAndFormId(Long id, String formId);

    public Long countByFormId(String formId);

    @Query("SELECT f FROM FormUserRecordEntity f WHERE f.formId = :formId AND f.summary LIKE %:summary%")
    public List<FormUserRecordEntity> findByFormIdAndSummary(
            @Param("formId") String formId,
            @Param("summary") String summary);
}