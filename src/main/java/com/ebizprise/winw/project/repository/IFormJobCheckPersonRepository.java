package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobCheckPersonListEntity;

@Repository("formJobCheckPersonRepository")
public interface IFormJobCheckPersonRepository extends JpaRepository<FormJobCheckPersonListEntity, Long> {

    public void deleteByFormId (String formId);

    public FormJobCheckPersonListEntity findByFormIdAndSort (String formId, String sort);

    public FormJobCheckPersonListEntity findByFormIdAndLevel (String formId, String name);
    
}
