package com.ebizprise.winw.project.repository;

import java.util.List;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebizprise.winw.project.entity.SysGroupEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("sysGroupRepository")
public interface ISysGroupRepository extends JpaRepository<SysGroupEntity, Long> {

    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<SysGroupEntity> findAll();

    @Query("FROM SysGroupEntity sg WHERE sg.groupName LIKE %?1%")
    public List<SysGroupEntity> findByGroupNameLike(String groupName);

    public SysGroupEntity findBySysGroupId(Long sysGroupId);

    /**
     * 透過科別查詢群組資訊,不含經理等級的職位
     *
     * @param departmentId
     * @param division
     * @return List
     */
    @Query("FROM SysGroupEntity sg WHERE sg.departmentId = ?1 AND sg.division = ?2")
    public List<SysGroupEntity> findByDepartmentIdAndDivision(String departmentId, String division);

    /**
     * 透過科別查詢群組資訊,包含經理等級的職位,即division為空值的資料
     *
     * @param departmentId
     * @param division
     * @return List
     */
    @Query("FROM SysGroupEntity sg WHERE sg.departmentId = ?1 AND (sg.division = ?2 OR sg.division = ?3)")
    public List<SysGroupEntity> findByDepartmentIdAndDivisionWithManagment(String departmentId, String division, String manager);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public SysGroupEntity findByGroupId(String groupId);
    
    public SysGroupEntity findGroupNameBySysGroupId (Long sysGroupId);

    public List<SysGroupEntity> findByDepartmentIdAndDivisionAndGroupIdContaining(String departmentId, String division, String groupId);

    @Query("FROM SysGroupEntity group WHERE group.departmentId = :departmentId AND group.division = :division AND group.groupName LIKE %:groupName")
    public SysGroupEntity findByDivisionAndGroupNameLike(@Param("departmentId") String departmentId, @Param("division") String division, @Param("groupName") String groupName);

    @Query(nativeQuery = true,
            value = "SELECT sg.GroupName FROM FORM f LEFT JOIN SYS_GROUP sg ON f.GroupSolving=sg.GroupId WHERE f.FormId=:formId")
    public String findGroupNameJoinForm(@Param("formId") String formId);

    public List<SysGroupEntity> findSysGroupByDivision (String division);
}
