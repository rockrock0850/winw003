package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplyChgEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;

@Repository("formProcessDetailApplyJobRepository")
public interface IFormProcessDetailApplyJobRepository extends JpaRepository<FormProcessDetailApplyJobEntity, Long> {
	
    @Query("FROM FormProcessDetailApplyJobEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplyJobEntity> findByProcessId(String processId);

    public FormProcessDetailApplyJobEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);


    public FormProcessDetailApplyJobEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);

    /**
     * 找出跳關清單
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("SELECT JOB FROM FormProcessDetailApplyJobEntity JOB WHERE JOB.detailId = :detailId AND (JOB.processOrder > :startLevel AND JOB.processOrder <= :endLevel) ORDER BY JOB.processOrder ASC")
    public List<FormProcessDetailApplyJobEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    /**
     * 找出跳關清單
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("SELECT JOB FROM FormProcessDetailApplyJobEntity JOB WHERE JOB.detailId = :detailId AND (JOB.processOrder > :startLevel AND JOB.processOrder <= :endLevel) AND JOB.workProject <> :workProject ORDER BY JOB.processOrder ASC")
    public List<FormProcessDetailApplyJobEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel, 
            @Param("workProject") String workProject);

    public List<FormProcessDetailApplyChgEntity> findByDetailId (String detailId);

    public int countByDetailId (String detailId);

    public int countByDetailIdAndIsWorkLevel (String detailId, String isWorkLevel);
    
}