package com.ebizprise.winw.project.entity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author AndrewLee 2019/09/23
 */
@Entity
@Table(name = "FORM_JOB_DIVISION_MAPPING")
public class FormJobDivisionMappingEntity {

    private Long id;
    private String division;
    private String divisionName;
    private String jobTabName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Division")
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Basic
    @Column(name = "DivisionName")
    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    
    @Basic
    @Column(name = "JobTabName")
    public String getJobTabName() {
        return jobTabName;
    }

    public void setJobTabName(String jobTabName) {
        this.jobTabName = jobTabName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormJobDivisionMappingEntity that = (FormJobDivisionMappingEntity) o;
        return id == that.id && Objects.equals(division, that.division) && 
                Objects.equals(divisionName, that.divisionName) &&
                Objects.equals(jobTabName, that.jobTabName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, division, divisionName,jobTabName);
    }
}
