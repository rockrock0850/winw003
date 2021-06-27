package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplyBaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailApplyBaRepository")
public interface IFormProcessDetailApplyBaRepository extends JpaRepository<FormProcessDetailApplyBaEntity, Long> {
	
    @Query("FROM FormProcessDetailApplyBaEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplyBaEntity> findByProcessId(String processId);

    public FormProcessDetailApplyBaEntity findByDetailIdAndProcessOrder (String detailId, int processOrder);

    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return
     * @author adam.yeh
     */
    @Query("SELECT BA FROM FormProcessDetailApplyBaEntity BA WHERE BA.detailId = :detailId AND (BA.processOrder > :startLevel AND BA.processOrder <= :endLevel) ORDER BY BA.processOrder ASC")
    public List<FormProcessDetailApplyBaEntity> findLimitList (
            @Param("detailId") String detailId,
            @Param("startLevel") int startLevel,
            @Param("endLevel") int endLevel);

    public FormProcessDetailApplyBaEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public int countByDetailId (String detailId);

}