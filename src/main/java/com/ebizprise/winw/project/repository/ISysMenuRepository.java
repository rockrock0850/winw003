package com.ebizprise.winw.project.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SysMenuEntity;

@Repository("sysMenuRepository")
public interface ISysMenuRepository extends JpaRepository<SysMenuEntity, Long> {
    
    /**
     * 找所有enabled = true的項目
     * 
     * @return
     * @author adam.yeh
     */
    @Query("SELECT menu FROM SysMenuEntity menu WHERE menu.enabled = 1 ORDER BY menu.parentId ASC, menu.orderId ASC")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<SysMenuEntity> findActivited ();
    
}