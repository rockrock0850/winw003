package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobLibraryFileEntity;

@Repository("formJobLibraryFileRepository")
public interface IFormJobLibraryFileRepository extends JpaRepository<FormJobLibraryFileEntity, Long> {

    public FormJobLibraryFileEntity findByFormIdAndTime (String formId, String time);

    public FormJobLibraryFileEntity findByIdAndFormId (Long id, String formId);
    
}