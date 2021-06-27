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
 * @author gary.tsai 2019/6/14
 */
@Entity
@Table(name = "QUESTION_MAINTAIN")
public class QuestionMaintainEntity extends BaseEntity {

	private Long id;
	private String content;
	private String fraction;
	private String description;
	private String isValidateFraction;
    private String isAddUp; // 是否加總
    private String isEnable; // 是否啟用

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
	@Column(name = "Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Basic
	@Column(name = "Fraction")
	public String getFraction() {
		return fraction;
	}

	public void setFraction(String fraction) {
		this.fraction = fraction;
	}

	@Basic
	@Column(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Basic
	@Column(name = "IsValidateFraction")
	public String getIsValidateFraction() {
	    return isValidateFraction;
	}
	
	public void setIsValidateFraction(String isValidateFraction) {
	    this.isValidateFraction = isValidateFraction;
	}

    @Basic
    @Column(name = "IsAddUp")
    public String getIsAddUp() {
        return isAddUp;
    }

    public void setIsAddUp(String isAddUp) {
        this.isAddUp = isAddUp;
    }

    @Basic
    @Column(name = "IsEnable")
    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		QuestionMaintainEntity that = (QuestionMaintainEntity) o;
		return id == that.id && Objects.equals(content, that.content) && Objects.equals(fraction, that.fraction)
				&& Objects.equals(description, that.description)
				&& Objects.equals(isValidateFraction, that.isValidateFraction)
				&& Objects.equals(isAddUp, that.isAddUp)
                && Objects.equals(isEnable, that.isEnable)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
        return Objects.hash(id, content, fraction, description, isValidateFraction, isAddUp, isEnable,
                this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}
}
