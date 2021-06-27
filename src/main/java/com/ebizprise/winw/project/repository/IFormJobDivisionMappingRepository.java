package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormJobDivisionMappingEntity;

@Repository("formJobDivisionMappingRepository")
public interface IFormJobDivisionMappingRepository extends JpaRepository<FormJobDivisionMappingEntity, Long> {

    List<FormJobDivisionMappingEntity> findByDivision(String division);
    
    List<FormJobDivisionMappingEntity> findByDivisionLike(String division);
}
