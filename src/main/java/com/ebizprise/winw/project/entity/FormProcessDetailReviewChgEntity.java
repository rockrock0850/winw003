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
 * @author gary.tsai 2019/6/26
 */
@Entity
@Table(name = "FORM_PROCESS_DETAIL_REVIEW_CHG")
public class FormProcessDetailReviewChgEntity extends BaseEntity {

	private long id;
	private String detailId;
	private String processId;
	private int processOrder;
	private String groupId;
	private Integer nextLevel;
	private Integer backLevel;
    private String isCreateJobIssue;// 可否產生工作單
    private String isWaitForSubIssueFinish;//等待衍生單完成
    private String isModifyColumnData;//是否修改欄位資料
    private String isCloseForm;
    private String isApprover;
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
    @Column(name = "DetailId")
	public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    @Basic
	@Column(name = "ProcessId")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Basic
	@Column(name = "ProcessOrder")
	public int getProcessOrder() {
		return processOrder;
	}

	public void setProcessOrder(int processOrder) {
		this.processOrder = processOrder;
	}

	@Basic
	@Column(name = "GroupId")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Basic
	@Column(name = "NextLevel")
	public Integer getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Integer nextLevel) {
		this.nextLevel = nextLevel;
	}

	@Basic
	@Column(name = "BackLevel")
	public Integer getBackLevel() {
		return backLevel;
	}

	public void setBackLevel(Integer backLevel) {
		this.backLevel = backLevel;
	}

    @Basic
    @Column(name = "IsCreateJobIssue")
    public String getIsCreateJobIssue() {
        return isCreateJobIssue;
    }

    public void setIsCreateJobIssue(String isCreateJobIssue) {
        this.isCreateJobIssue = isCreateJobIssue;
    }

    @Basic
    @Column(name = "IsModifyColumnData")
    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

    @Basic
    @Column(name = "IsWaitForSubIssueFinish")
    public String getIsWaitForSubIssueFinish() {
        return isWaitForSubIssueFinish;
    }

    public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
        this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
    }
    
    @Basic
    @Column(name = "IsCloseForm")
    public String getIsCloseForm() {
        return isCloseForm;
    }

    public void setIsCloseForm(String isCloseForm) {
        this.isCloseForm = isCloseForm;
    }

    @Basic
    @Column(name = "IsApprover")
    public String getIsApprover() {
        return isApprover;
    }

    public void setIsApprover(String isApprover) {
        this.isApprover = isApprover;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormProcessDetailReviewChgEntity that = (FormProcessDetailReviewChgEntity) o;
		return id == that.id && processOrder == that.processOrder && Objects.equals(processId, that.processId)
				&& Objects.equals(groupId, that.groupId) && Objects.equals(nextLevel, that.nextLevel) && Objects.equals(detailId, that.detailId)
				&& Objects.equals(backLevel, that.backLevel) 
				&& Objects.equals(isCreateJobIssue, that.isCreateJobIssue)
				&& Objects.equals(isWaitForSubIssueFinish, that.isWaitForSubIssueFinish)
				&& Objects.equals(isModifyColumnData, that.isModifyColumnData)
				&& Objects.equals(isCloseForm, that.isCloseForm)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, detailId, processId, processOrder, groupId, nextLevel, backLevel,isCreateJobIssue,isModifyColumnData,isWaitForSubIssueFinish,isCloseForm, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}

}
