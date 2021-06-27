package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplyChgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailApplyChgRepository")
public interface IFormProcessDetailApplyChgRepository extends JpaRepository<FormProcessDetailApplyChgEntity, Long> {
	
    @Query("FROM FormProcessDetailApplyChgEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplyChgEntity> findByProcessId(String processId);
    
    @Query("FROM FormProcessDetailApplyChgEntity T1 WHERE T1.detailId = ?1" )
    public List<FormProcessDetailApplyChgEntity> findByDetailId(String detailId);

    public FormProcessDetailApplyChgEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("FROM FormProcessDetailApplyChgEntity Chg WHERE Chg.detailId = :detailId AND (Chg.processOrder > :startLevel AND Chg.processOrder <= :endLevel) ORDER BY Chg.processOrder ASC")
    public List<FormProcessDetailApplyChgEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailApplyChgEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public int countByDetailId (String detailId);

}