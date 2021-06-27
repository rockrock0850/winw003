package com.ebizprise.winw.project.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * @author Bernard.Yu 2020/10/19
 */
@Entity
@Table(name = "FORM_INFO_BA_DETAIL")
public class FormInfoBaDetailEntity extends BaseEntity {
    
    private Long id;
    private String formId;
    private String batchName;
    private String summary;
    private String division;
    private String executeTime;
    private String dbInUse;
    private Date effectDate;
    
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
	@Column(name = "FormId")
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@Basic
	@Column(name = "BatchName")
	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	@Basic
	@Column(name = "Summary")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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
    @Column(name = "ExecuteTime")
    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }
    
    @Basic
    @Column(name = "DbInUse")
    public String getDbInUse() {
        return dbInUse;
    }

    public void setDbInUse(String dbInUse) {
        this.dbInUse = dbInUse;
    }
    
    @Basic
    @Column(name = "EffectDate")
    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormInfoBaDetailEntity that = (FormInfoBaDetailEntity) o;
		return id == that.id && Objects.equals(formId, that.formId)
				&& Objects.equals(batchName, that.batchName) && Objects.equals(summary, that.summary)
				&& Objects.equals(division, that.division) && Objects.equals(executeTime, that.executeTime)
				&& Objects.equals(dbInUse, that.dbInUse) && Objects.equals(effectDate, that.effectDate)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, formId, batchName, summary, division, executeTime, dbInUse, effectDate, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
	
}
