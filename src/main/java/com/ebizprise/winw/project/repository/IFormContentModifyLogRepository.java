package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormContentModifyLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("formContentModifyLogRepository")
public interface IFormContentModifyLogRepository extends JpaRepository<FormContentModifyLogEntity, Long> {

    public FormContentModifyLogEntity findByFormId(String formId);

}