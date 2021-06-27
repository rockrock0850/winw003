package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormInfoBaDetailEntity;

@Repository("formInfoBaDetailRepository")
public interface IFormInfoBaDetailRepository extends JpaRepository<FormInfoBaDetailEntity, Long> {

    public void deleteByFormId (String formId);

    public FormInfoBaDetailEntity findByFormId (String formId);

}
