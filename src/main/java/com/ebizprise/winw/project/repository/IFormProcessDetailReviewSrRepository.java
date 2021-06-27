package com.ebizprise.winw.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewSrEntity;

@Repository("formProcessDetailReviewSrRepository")
public interface IFormProcessDetailReviewSrRepository extends JpaRepository<FormProcessDetailReviewSrEntity, Long> {
	
    @Query("FROM FormProcessDetailReviewSrEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewSrEntity> findByProcessId(String processId);

    public FormProcessDetailReviewSrEntity findByDetailIdAndProcessOrder (String detailId, int i);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT SR FROM FormProcessDetailReviewSrEntity SR WHERE SR.detailId = :detailId AND (SR.processOrder > :startLevel AND SR.processOrder <= :endLevel) ORDER BY SR.processOrder ASC")
    public List<FormProcessDetailReviewSrEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailReviewSrEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public List<FormProcessDetailReviewSrEntity> findByDetailId (String detailId);

}