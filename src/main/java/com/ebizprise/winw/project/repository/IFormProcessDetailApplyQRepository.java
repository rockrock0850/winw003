package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplyQEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailApplyQRepository")
public interface IFormProcessDetailApplyQRepository extends JpaRepository<FormProcessDetailApplyQEntity, Long> {
	
    @Query("FROM FormProcessDetailApplyQEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplyQEntity> findByProcessId(String processId);

    public FormProcessDetailApplyQEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("SELECT Q FROM FormProcessDetailApplyQEntity Q WHERE Q.detailId = :detailId AND (Q.processOrder > :startLevel AND Q.processOrder <= :endLevel) ORDER BY Q.processOrder ASC")
    public List<FormProcessDetailApplyQEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailApplyQEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public int countByDetailId (String detailId);

}