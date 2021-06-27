package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.FormInfoChgDetailEntity;

@Repository("formInfoChgDetailRepository")
public interface IFormInfoChgDetailRepository extends JpaRepository<FormInfoChgDetailEntity, Long> {
    
    public void deleteByFormId (String formId);

    public FormInfoChgDetailEntity findByFormId (String formId);
    
    public int countByFormIdInAndIsNewSystem (List<String> formIds, String isNewSystem);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormInfoChgDetailEntity SET IsScopeChanged = :isScopeChanged WHERE FormId = :formId")
    public void updateIsScopeChangedByFormId(@Param("isScopeChanged") String isScopeChanged, @Param("formId") String formId);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormInfoChgDetailEntity SET IsScopeChanged = :isScopeChanged WHERE FormId IN :formIds")
    public void updateIsScopeChangedByFormIdIn(@Param("isScopeChanged") String isScopeChanged,
            @Param("formIds") List<String> formIds);

}
