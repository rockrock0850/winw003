package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobSegmentListEntity;

@Repository("formJobSegmentRepository")
public interface IFormJobSegmentRepository extends JpaRepository<FormJobSegmentListEntity, Long> {

    public List<FormJobSegmentListEntity> findByFormIdAndType (String formId, String type);

    public FormJobSegmentListEntity findByIdAndFormId (Long id, String formId);

    public int countById (Long id);
    
}