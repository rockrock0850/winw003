package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;

@Repository("formProcessDetailReviewJobRepository")
public interface IFormProcessDetailReviewJobRepository extends JpaRepository<FormProcessDetailReviewJobEntity, Long> {
	
    @Query("FROM FormProcessDetailReviewJobEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewJobEntity> findByProcessId(String processId);

    public FormProcessDetailReviewJobEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);
    

    public FormProcessDetailReviewJobEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);
    
    public List<FormProcessDetailReviewJobEntity> findByDetailId (String detailId);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("SELECT JOB FROM FormProcessDetailReviewJobEntity JOB WHERE JOB.detailId = :detailId AND (JOB.processOrder > :startLevel AND JOB.processOrder <= :endLevel) ORDER BY JOB.processOrder ASC")
    public List<FormProcessDetailReviewJobEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public int countByDetailIdAndIsWorkLevel (String detailId, String isWorkLevel);
}