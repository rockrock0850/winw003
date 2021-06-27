package com.ebizprise.winw.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.SysOptionEntity;

@Repository("sysOptionRepository")
public interface ISysOptionRepository extends JpaRepository<SysOptionEntity, Long> {

    public List<SysOptionEntity> findByParentIdOrderByCreatedAtDesc(String parentId);
    
    public List<SysOptionEntity> findByOptionId(String optionId);

    public List<SysOptionEntity> findByParentIdAndOptionId(String parentId, String optionId);
    
    public List<SysOptionEntity> findByOptionIdOrderBySortAsc(String optionId);

    public List<SysOptionEntity> findByParentIdOrderBySortAsc(String parentId);

    public int countByOptionId(String string);

    public List<SysOptionEntity> findByParentIdAndValueContainingOrDisplayContainingOrderBySortAsc(
            String optionId, String value, String display);

    public List<SysOptionEntity> findByParentIdAndValueContainingAndDisplayContainingOrderBySortAsc(
            String optionId, String wording, String wording2);

    public List<SysOptionEntity> findByValueIn(List<String> values);

    public SysOptionEntity findByValue(String values);

    public SysOptionEntity findByDisplayAndValue(String display, String value);

    public List<SysOptionEntity> findAllByOrderByIdAsc();

    public List<SysOptionEntity> findByOptionIdAndActiveOrderBySortAsc (String optionId, String active);

    public Object findByOptionIdAndValue (String optionId, String value);

}