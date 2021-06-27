package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewChgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailReviewChgRepository")
public interface IFormProcessDetailReviewChgRepository extends JpaRepository<FormProcessDetailReviewChgEntity, Long> {
	
    public FormProcessDetailReviewChgEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);
    
    public List<FormProcessDetailReviewChgEntity> findByDetailId (String detailId);
    
    @Query("FROM FormProcessDetailReviewChgEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewChgEntity> findByProcessId(String processId);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("FROM FormProcessDetailReviewChgEntity Chg WHERE Chg.detailId = :detailId AND (Chg.processOrder > :startLevel AND Chg.processOrder <= :endLevel) ORDER BY Chg.processOrder ASC")
    public List<FormProcessDetailReviewChgEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailReviewChgEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);
}