package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormFileLogEntity;

@Repository("formFileLogRepository")
public interface IFormFileLogRepository extends JpaRepository<FormFileLogEntity, Long> {

}