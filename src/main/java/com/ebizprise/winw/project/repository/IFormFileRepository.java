package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormFileEntity;

@Repository("formFileRepository")
public interface IFormFileRepository extends JpaRepository<FormFileEntity, Long> {

	public List<FormFileEntity> findByFormIdOrderByCreatedAtDesc(String formId);

    public FormFileEntity findByIdAndFormId(Long id, String formId);

	public FormFileEntity findByFormIdAndName(String formId, String name);

	public FormFileEntity findByName(String name);

	public void deleteByFormIdAndName(String formId, String name);

    public void deleteByIdAndFormId(Long id, String formId);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormFileEntity SET Islocked = :islocked WHERE FormId = :formId")
    public void updateFormFileStatusByFormId(@Param("islocked")String islocked,@Param("formId")String formId);

    public List<FormFileEntity> findByFormIdAndTypeOrderByCreatedAtDesc (String formId, String type);

    public List<FormFileEntity> findByFormId (String formId);

    public void deleteByformId (String formId);
    
}