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
@Table(name = "FORM_PROCESS_DETAIL_REVIEW_JOB")
public class FormProcessDetailReviewJobEntity extends BaseEntity {
    
	private long id;
	private String detailId;
	private String processId;
	private int processOrder;
    private String workProject;// 工作項目
    private String isCreateJobCIssue;// 可否產生工作會辦單
	private String groupId;
	private Integer nextLevel;
	private Integer backLevel;
	private String isWorkLevel;// 作業關卡
    private String isWaitForSubIssueFinish;// 是否等待衍生單完成
	private String isModifyColumnData;
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
    @Column(name = "IsModifyColumnData")
    public String getIsModifyColumnData() {
        return isModifyColumnData;
    }

    public void setIsModifyColumnData(String isModifyColumnData) {
        this.isModifyColumnData = isModifyColumnData;
    }

    @Basic
    @Column(name = "WorkProject")
	public String getWorkProject() {
        return workProject;
    }

    public void setWorkProject(String workProject) {
        this.workProject = workProject;
    }

    @Basic
    @Column(name = "IsWorkLevel")
    public String getIsWorkLevel() {
        return isWorkLevel;
    }

    public void setIsWorkLevel(String isWorkLevel) {
        this.isWorkLevel = isWorkLevel;
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
    @Column(name = "IsCreateJobCIssue")
    public String getIsCreateJobCIssue() {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue(String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
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
		FormProcessDetailReviewJobEntity that = (FormProcessDetailReviewJobEntity) o;
		return id == that.id && processOrder == that.processOrder && Objects.equals(processId, that.processId)
				&& Objects.equals(groupId, that.groupId) && Objects.equals(nextLevel, that.nextLevel) && Objects.equals(detailId, that.detailId)
				&& Objects.equals(backLevel, that.backLevel) 
				&& Objects.equals(isModifyColumnData, that.isModifyColumnData)
				&& Objects.equals(workProject, that.workProject)
				&& Objects.equals(isWorkLevel, that.isWorkLevel)
				&& Objects.equals(isCloseForm, that.isCloseForm)
				&& Objects.equals(isCreateJobCIssue, that.isCreateJobCIssue)
				&& Objects.equals(isWaitForSubIssueFinish, that.isWaitForSubIssueFinish)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, detailId, processId, processOrder, groupId, nextLevel, backLevel,isModifyColumnData,isCreateJobCIssue, workProject,isWorkLevel,isCloseForm,isWaitForSubIssueFinish,this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}

}
