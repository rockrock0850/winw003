package com.ebizprise.winw.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormProcessDetailReviewBaEntity;

@Repository("formProcessDetailReviewBaRepository")
public interface IFormProcessDetailReviewBaRepository extends JpaRepository<FormProcessDetailReviewBaEntity, Long> {
	
    @Query("FROM FormProcessDetailReviewBaEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailReviewBaEntity> findByProcessId(String processId);

    public FormProcessDetailReviewBaEntity findByDetailIdAndProcessOrder (String detailId, int i);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT BA FROM FormProcessDetailReviewBaEntity BA WHERE BA.detailId = :detailId AND (BA.processOrder > :startLevel AND BA.processOrder <= :endLevel) ORDER BY BA.processOrder ASC")
    public List<FormProcessDetailReviewBaEntity> findLimitList (
            @Param("detailId") String detailId,
            @Param("startLevel") int startLevel,
            @Param("endLevel") int endLevel);

    public FormProcessDetailReviewBaEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public List<FormProcessDetailReviewBaEntity> findByDetailId (String detailId);

}