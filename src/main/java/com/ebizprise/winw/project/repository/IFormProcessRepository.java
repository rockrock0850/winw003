package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormProcessEntity;

@Repository("formProcessRepository")
public interface IFormProcessRepository extends JpaRepository<FormProcessEntity, Long> {

	public FormProcessEntity findTop1ByFormTypeOrderByUpdatedAtDesc (String formType);
	
	@Query("FROM FormProcessEntity T1 WHERE T1.formType = :formType AND T1.division = :division AND T1.departmentId = :departmentId AND T1.isEnable = :isEnable")
	public FormProcessEntity findTop1ByFormTypeAndDivisionAndDepartmentIdAndIsEnable (@Param("formType")int formType,@Param("division")String division,@Param("departmentId")String departmentId,@Param("isEnable")String isEnable);
	
}