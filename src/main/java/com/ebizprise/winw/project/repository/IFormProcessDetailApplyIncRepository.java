package com.ebizprise.winw.project.repository;

import com.ebizprise.winw.project.entity.FormProcessDetailApplyIncEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("formProcessDetailApplyIncRepository")
public interface IFormProcessDetailApplyIncRepository extends JpaRepository<FormProcessDetailApplyIncEntity, Long> {
	
    @Query("FROM FormProcessDetailApplyIncEntity T1 WHERE T1.processId = ?1" )
    public List<FormProcessDetailApplyIncEntity> findByProcessId(String processId);

    public FormProcessDetailApplyIncEntity findByDetailIdAndProcessOrder (String detailId, int i);


    /**
     * 找出跳關清單
     * 
     * @param detailId
     * @param startLevel
     * @param endLevel
     * @return List
     * @author AndrewLee
     */
    @Query("SELECT INC FROM FormProcessDetailApplyIncEntity INC WHERE INC.detailId = :detailId AND (INC.processOrder > :startLevel AND INC.processOrder <= :endLevel) ORDER BY INC.processOrder ASC")
    public List<FormProcessDetailApplyIncEntity> findLimitList (
            @Param("detailId") String detailId, 
            @Param("startLevel") int startLevel, 
            @Param("endLevel") int endLevel);

    public FormProcessDetailApplyIncEntity findTop1ByDetailIdAndGroupIdOrderByProcessOrderDesc (String detailId, String groupId);

    public int countByDetailId (String detailId);

}