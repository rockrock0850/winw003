package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplySrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailApplySrRepository")
public interface IFormProcessDetailApplySrRepository extends JpaRepository<FormProcessDetailApplySrEntity, Long> {
	
    @Query("FROM FormProcessDetailApplySrEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplySrEntity> findByProcessId(String processId);

    public FormProcessDetailApplySrEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT SR FROM FormProcessDetailApplySrEntity SR WHERE SR.detailId = :detailId AND (SR.processOrder > :startLevel AND SR.processOrder <= :endLevel) ORDER BY SR.processOrder ASC")
    public List<FormProcessDetailApplySrEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailApplySrEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public int countByDetailId (String detailId);

}