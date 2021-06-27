/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobCountersignedEntity;

@Repository("formJobCountersignedRepository")
public interface IFormJobCountersignedRepository extends JpaRepository<FormJobCountersignedEntity, Long> {
    
    public FormJobCountersignedEntity findByFormIdAndDivision(String formId, String division);

    public FormJobCountersignedEntity findByFormId (String formId);

    public FormJobCountersignedEntity findByIdAndFormId (Long id, String formId);
    
}
