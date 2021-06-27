package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.DashboardDirectAuthEntity;

@Repository("dashBoardDirectAuthRepository")
public interface IDashBoardDirectAuthRepository extends JpaRepository<DashboardDirectAuthEntity, Long> {

    public DashboardDirectAuthEntity findByDivisionLike (String nextGroup);

    public DashboardDirectAuthEntity findByDivision (String division);
    
}