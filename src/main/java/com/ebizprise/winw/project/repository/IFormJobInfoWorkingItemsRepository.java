package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobInfoWorkingItemsEntity;

@Repository("formJobInfoWorkingItemsRepository")
public interface IFormJobInfoWorkingItemsRepository extends JpaRepository<FormJobInfoWorkingItemsEntity, Long> {

    public void deleteByFormId (String formId);

    public List<FormJobInfoWorkingItemsEntity> findByFormId (String formId);

}