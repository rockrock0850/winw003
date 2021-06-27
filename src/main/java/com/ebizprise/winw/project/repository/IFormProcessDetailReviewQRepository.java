package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewQEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailReviewQRepository")
public interface IFormProcessDetailReviewQRepository extends JpaRepository<FormProcessDetailReviewQEntity, Long> {
	
    @Query("FROM FormProcessDetailReviewQEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewQEntity> findByProcessId(String processId);

    public FormProcessDetailReviewQEntity findByDetailIdAndProcessOrder (String detailId, int i);

    public List<FormProcessDetailReviewQEntity> findByDetailId (String detailId);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT Q FROM FormProcessDetailReviewQEntity Q WHERE Q.detailId = :detailId AND (Q.processOrder > :startLevel AND Q.processOrder <= :endLevel) ORDER BY Q.processOrder ASC")
    public List<FormProcessDetailReviewQEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailReviewQEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);
}