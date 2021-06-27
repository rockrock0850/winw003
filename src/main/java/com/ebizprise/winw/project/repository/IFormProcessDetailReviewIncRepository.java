package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewIncEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailReviewIncRepository")
public interface IFormProcessDetailReviewIncRepository extends JpaRepository<FormProcessDetailReviewIncEntity, Long> {
	
    @Query("FROM FormProcessDetailReviewIncEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewIncEntity> findByProcessId(String processId);

    public FormProcessDetailReviewIncEntity findByDetailIdAndProcessOrder (String detailId, int i);


    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT INC FROM FormProcessDetailReviewIncEntity INC WHERE INC.detailId = :detailId AND (INC.processOrder > :startLevel AND INC.processOrder <= :endLevel) ORDER BY INC.processOrder ASC")
    public List<FormProcessDetailReviewIncEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailReviewIncEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public List<FormProcessDetailReviewIncEntity> findByDetailId (String detailId);
    
}