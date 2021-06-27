package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInfoUserEntity;

@Repository("formInfoUserRepository")
public interface IFormInfoUserRepository extends JpaRepository<FormInfoUserEntity, Long> {

    public void deleteByFormId (String formId);
    
    public FormInfoUserEntity findByFormId (String formId);

}
