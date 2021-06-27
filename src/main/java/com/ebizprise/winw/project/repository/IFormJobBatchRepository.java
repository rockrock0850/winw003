/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobBatchEntity;

@Repository("formJobBatchRepository")
public interface IFormJobBatchRepository extends JpaRepository<FormJobBatchEntity, Long> {

    public FormJobBatchEntity findByFormId (String formId);

    public FormJobBatchEntity findByIdAndFormId (Long id, String formId);
    
}
