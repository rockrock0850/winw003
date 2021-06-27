package com.ebizprise.winw.project.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.WorkingItemEntity;

@Repository("workingItemRepository")
public interface IWorkingItemRepository extends JpaRepository<WorkingItemEntity, Long> {
    
    @Query("FROM WorkingItemEntity wi WHERE wi.spGroup = :spGroup AND wi.workingItemName LIKE %:workingItemName%")
    public List<WorkingItemEntity> findBySpGroupAndWorkingItemNameLike(@Param("spGroup") String spGroup,
                                                                       @Param("workingItemName") String workingItemName);
    
    @Query("FROM WorkingItemEntity wi WHERE wi.workingItemName LIKE %?1%")
    public List<WorkingItemEntity> findByWorkingItemNameLike(String workingItemName);

    @Query("FROM WorkingItemEntity wi WHERE concat(wi.workingItemName, wi.spGroup) LIKE %?1% AND Active = 'Y'")
    public List<WorkingItemEntity> findByWorkingItemNameLikeAndSpGroupLike(String keyword);
    
    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<WorkingItemEntity> findAll();
    
    public List<WorkingItemEntity> findAllByOrderByIdAsc();

    public List<WorkingItemEntity> findByActive (String active);
    
}
