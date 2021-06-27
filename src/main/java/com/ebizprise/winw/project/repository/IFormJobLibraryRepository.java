package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobLibraryEntity;

@Repository("formJobLibraryRepository")
public interface IFormJobLibraryRepository extends JpaRepository<FormJobLibraryEntity, Long> {

    public List<FormJobLibraryEntity> findByFormId (String formId);

    public List<FormJobLibraryEntity> findByFormIdAndRowType (String formId, String rowType);

}
