package com.ebizprise.winw.project.entity;

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
@Table(name = "FORM_INFO_CHG_DETAIL")
public class FormInfoChgDetailEntity extends BaseEntity {

	private Long id;
	private String formId;
	private String summary;
	private String content;
	private String effectSystem;
	private String cCategory;
	private String cClass;
	private String cComponent;
	private String standard;
	private String changeType;
	private String changeRank;
	private String isNewSystem;
	private String isNewService;
	private String isUrgent;
	private String isEffectField;
	private String isEffectAccountant;
    private String isScopeChanged;
    private String isModifyProgram;

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
	@Column(name = "Summary")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Basic
	@Column(name = "Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Basic
	@Column(name = "EffectSystem")
	public String getEffectSystem() {
		return effectSystem;
	}

	public void setEffectSystem(String effectSystem) {
		this.effectSystem = effectSystem;
	}

	@Basic
	@Column(name = "CCategory")
	public String getcCategory() {
		return cCategory;
	}

	public void setcCategory(String cCategory) {
		this.cCategory = cCategory;
	}

	@Basic
	@Column(name = "CClass")
	public String getcClass() {
		return cClass;
	}

	public void setcClass(String cClass) {
		this.cClass = cClass;
	}

	@Basic
	@Column(name = "CComponent")
	public String getcComponent() {
		return cComponent;
	}

	public void setcComponent(String cComponent) {
		this.cComponent = cComponent;
	}

	@Basic
	@Column(name = "Standard")
	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	@Basic
	@Column(name = "ChangeType")
	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	@Basic
	@Column(name = "ChangeRank")
	public String getChangeRank() {
		return changeRank;
	}

	public void setChangeRank(String changeRank) {
		this.changeRank = changeRank;
	}

	@Basic
	@Column(name = "IsNewSystem")
	public String getIsNewSystem() {
		return isNewSystem;
	}

	public void setIsNewSystem(String isNewSystem) {
		this.isNewSystem = isNewSystem;
	}

	@Basic
	@Column(name = "IsNewService")
	public String getIsNewService() {
		return isNewService;
	}

	public void setIsNewService(String isNewService) {
		this.isNewService = isNewService;
	}

	@Basic
	@Column(name = "IsUrgent")
	public String getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}

	@Basic
	@Column(name = "IsEffectField")
	public String getIsEffectField() {
		return isEffectField;
	}

	public void setIsEffectField(String isEffectField) {
		this.isEffectField = isEffectField;
	}

	@Basic
	@Column(name = "IsEffectAccountant")
	public String getIsEffectAccountant() {
		return isEffectAccountant;
	}

	public void setIsEffectAccountant(String isEffectAccountant) {
		this.isEffectAccountant = isEffectAccountant;
	}

    @Basic
    @Column(name = "IsScopeChanged")
    public String getIsScopeChanged() {
        return isScopeChanged;
    }

    public void setIsScopeChanged(String isScopeChanged) {
        this.isScopeChanged = isScopeChanged;
    }
    
    @Basic
    @Column(name = "IsModifyProgram")
    public String getIsModifyProgram() {
        return isModifyProgram;
    }

    public void setIsModifyProgram(String isModifyProgram) {
        this.isModifyProgram = isModifyProgram;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormInfoChgDetailEntity that = (FormInfoChgDetailEntity) o;
		return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(summary, that.summary)
				&& Objects.equals(content, that.content) && Objects.equals(effectSystem, that.effectSystem)
				&& Objects.equals(cCategory, that.cCategory) && Objects.equals(cClass, that.cClass)
				&& Objects.equals(cComponent, that.cComponent) && Objects.equals(standard, that.standard)
				&& Objects.equals(changeType, that.changeType) && Objects.equals(changeRank, that.changeRank)
				&& Objects.equals(isNewSystem, that.isNewSystem) && Objects.equals(isNewService, that.isNewService)
				&& Objects.equals(isUrgent, that.isUrgent) && Objects.equals(isEffectField, that.isEffectField)
				&& Objects.equals(isEffectAccountant, that.isEffectAccountant)
                && Objects.equals(isScopeChanged, that.isScopeChanged)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, formId, summary, content, effectSystem, cCategory, cClass, cComponent, standard,
                changeType, changeRank, isNewSystem, isNewService, isUrgent, isEffectField, isEffectAccountant, isScopeChanged,
				this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}

}
