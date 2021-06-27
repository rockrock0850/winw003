package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SystemEntity;

/*
 * 系統名稱 搜尋模組
 */
@Repository("systemRepository")
public interface ISystemRepository extends JpaRepository<SystemEntity, Long> {

    public List<SystemEntity> findByDepartment (String division);

    public List<SystemEntity> findByDepartmentContaining (String department);

    public List<SystemEntity> findByDepartmentAndMboName (String division, String mobName);

    public List<SystemEntity> findByDepartmentContainingAndMboName (String department, String mboName);

    public List<SystemEntity> findByDepartmentContainingAndMboNameAndActive (String division, String symbol, String active);

    public List<SystemEntity> findByMboNameAndActive (String symbol, String active);

    public List<SystemEntity> findByDepartmentAndMboNameNotAndMboNameNotAndMboNameNotAndActive (
            String division,
            String symbol1,
            String symbol2,
            String symbol3,
            String active);

    public List<SystemEntity> findByMboName (String mboName);
    
    public List<SystemEntity> findAllByOrderByIdAsc();

}
