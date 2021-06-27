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
 * @author gary.tsai 2019/6/27
 */
@Entity
@Table(name = "FORM_INFO_DATE")
public class FormInfoDateEntity extends BaseEntity {

	private Long id;
	private String formId;
	private String formClass;
	private Date createTime;
	private Date ect;
	private Date eot;
	private Date act;
	private Date ast;
	private Date cct;
	private Date cat;
	private Date reportTime;
	private Date excludeTime;
	private Date observation;
	private String mainEvent;
	private String isSpecial;
	private String specialEndCaseType;
	private String isMainEvent;
	private String isSameInc;
	private String isInterrupt;
	private String isIVR;
    private Date mect;
    private Date oect;

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
	@Column(name = "FormClass")
	public String getFormClass() {
		return formClass;
	}

	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}

	@Basic
	@Column(name = "CreateTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Basic
	@Column(name = "ECT")
	public Date getEct() {
		return ect;
	}

	public void setEct(Date ect) {
		this.ect = ect;
	}

	@Basic
	@Column(name = "EOT")
	public Date getEot() {
		return eot;
	}

	public void setEot(Date eot) {
		this.eot = eot;
	}

	@Basic
	@Column(name = "ACT")
	public Date getAct() {
		return act;
	}

	public void setAct(Date act) {
		this.act = act;
	}
	
	@Basic
	@Column(name = "CCT")
	public Date getCct() {
	    return cct;
	}
	
	public void setCct(Date cct) {
	    this.cct = cct;
	}
	
	@Basic
	@Column(name = "CAT")
	public Date getCat() {
	    return cat;
	}
	
	public void setCat(Date cat) {
	    this.cat = cat;
	}

	@Basic
	@Column(name = "AST")
	public Date getAst() {
		return ast;
	}

	public void setAst(Date ast) {
		this.ast = ast;
	}

	@Basic
	@Column(name = "ReportTime")
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	@Basic
	@Column(name = "ExcludeTime")
	public Date getExcludeTime() {
		return excludeTime;
	}

	public void setExcludeTime(Date excludeTime) {
		this.excludeTime = excludeTime;
	}

	@Basic
	@Column(name = "Observation")
	public Date getObservation() {
		return observation;
	}

	public void setObservation(Date observation) {
		this.observation = observation;
	}

	@Basic
	@Column(name = "MainEvent")
	public String getMainEvent() {
		return mainEvent;
	}

	public void setMainEvent(String mainEvent) {
		this.mainEvent = mainEvent;
	}

	@Basic
	@Column(name = "IsSpecial")
	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	@Basic
	@Column(name = "IsMainEvent")
	public String getIsMainEvent() {
		return isMainEvent;
	}

	public void setIsMainEvent(String isMainEvent) {
		this.isMainEvent = isMainEvent;
	}

	@Basic
	@Column(name = "IsInterrupt")
	public String getIsInterrupt() {
		return isInterrupt;
	}

	public void setIsInterrupt(String isInterrupt) {
		this.isInterrupt = isInterrupt;
	}
	
	@Basic
    @Column(name = "SpecialEndCaseType")
	public String getSpecialEndCaseType() {
        return specialEndCaseType;
    }

    public void setSpecialEndCaseType(String specialEndCaseType) {
        this.specialEndCaseType = specialEndCaseType;
    }
    
    @Basic
    @Column(name = "IsIVR")
    public String getIsIVR() {
        return isIVR;
    }

    public void setIsIVR(String isIVR) {
        this.isIVR = isIVR;
    }

    @Basic
    @Column(name = "MECT")
    public Date getMect() {
        return mect;
    }

    public void setMect(Date mect) {
        this.mect = mect;
    }

    @Basic
    @Column(name = "IsSameInc")
    public String getIsSameInc () {
        return isSameInc;
    }

    public void setIsSameInc (String isSameInc) {
        this.isSameInc = isSameInc;
    }
    
    @Basic
    @Column(name = "OECT")
    public Date getOect() {
        return oect;
    }

    public void setOect(Date oect) {
        this.oect = oect;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormInfoDateEntity that = (FormInfoDateEntity) o;
		return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(formClass, that.formClass)
				&& Objects.equals(createTime, that.createTime) && Objects.equals(ect, that.ect)
				&& Objects.equals(eot, that.eot) && Objects.equals(act, that.act) && Objects.equals(ast, that.ast)
				&& Objects.equals(reportTime, that.reportTime) && Objects.equals(excludeTime, that.excludeTime)
				&& Objects.equals(observation, that.observation) && Objects.equals(mainEvent, that.mainEvent)
				&& Objects.equals(isSpecial, that.isSpecial) && Objects.equals(isMainEvent, that.isMainEvent)
				&& Objects.equals(isInterrupt, that.isInterrupt)
				&& Objects.equals(specialEndCaseType, that.specialEndCaseType)
				&& Objects.equals(mect, that.mect)
				&& Objects.equals(isIVR, that.isIVR)
				&& Objects.equals(cat, that.cat)
				&& Objects.equals(cct, that.cct)
                && Objects.equals(oect, that.oect)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, formId, formClass, createTime, ect, eot, act, ast,cat,cct,reportTime, excludeTime, observation,
                mainEvent, isSpecial, isMainEvent, isInterrupt, specialEndCaseType, mect, isIVR, oect,
                this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}

    
}
