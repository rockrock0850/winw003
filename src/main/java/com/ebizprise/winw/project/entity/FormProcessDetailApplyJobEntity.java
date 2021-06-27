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
@Table(name = "FORM_PROCESS_DETAIL_APPLY_JOB")
public class FormProcessDetailApplyJobEntity extends BaseEntity {
    
	private long id;
	private String detailId;
	private String processId;
	private int processOrder;
	private String groupId;
	private Integer nextLevel;
	private Integer backLevel;
    private String workProject;// 工作項目
    private String isWorkLevel;// 作業關卡
    private String isCreateJobCIssue;// 是否產生工作會辦單
    private String isCreateSpJobIssue;// 是否新增系統工作單
    private String isWaitForSubIssueFinish;// 是否等待衍生單完成
    private String isCreateCompareList;// 是否產生程式清單
    private String isModifyColumnData;
    private String isApprover;
    private String parallels;
    private String isParallel;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
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
	@Column(name = "IsWaitForSubIssueFinish")
	public String getIsWaitForSubIssueFinish() {
		return isWaitForSubIssueFinish;
	}

	public void setIsWaitForSubIssueFinish(String isWaitForSubIssueFinish) {
		this.isWaitForSubIssueFinish = isWaitForSubIssueFinish;
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
    @Column(name = "IsCreateJobCIssue")
    public String getIsCreateJobCIssue() {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue(String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
    }

    @Basic
    @Column(name = "IsCreateCompareList")
    public String getIsCreateCompareList() {
        return isCreateCompareList;
    }

    public void setIsCreateCompareList(String isCreateCompareList) {
        this.isCreateCompareList = isCreateCompareList;
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
    @Column(name = "IsCreateSPJobIssue")
    public String getIsCreateSpJobIssue() {
        return isCreateSpJobIssue;
    }

    public void setIsCreateSpJobIssue(String isCreateSpJobIssue) {
        this.isCreateSpJobIssue = isCreateSpJobIssue;
    }

    @Basic
    @Column(name = "IsApprover")
    public String getIsApprover() {
        return isApprover;
    }

    public void setIsApprover(String isApprover) {
        this.isApprover = isApprover;
    }

    @Basic
    @Column(name = "Parallels")
    public String getParallels () {
        return parallels;
    }

    public void setParallels (String parallels) {
        this.parallels = parallels;
    }

    @Basic
    @Column(name = "IsParallel")
    public String getIsParallel () {
        return isParallel;
    }

    public void setIsParallel (String isParallel) {
        this.isParallel = isParallel;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FormProcessDetailApplyJobEntity that = (FormProcessDetailApplyJobEntity) o;
		return id == that.id && processOrder == that.processOrder && Objects.equals(processId, that.processId)
				&& Objects.equals(groupId, that.groupId) && Objects.equals(nextLevel, that.nextLevel)
				&& Objects.equals(detailId, that.detailId)
				&& Objects.equals(backLevel, that.backLevel)
				&& Objects.equals(isWorkLevel, that.isWorkLevel)
				&& Objects.equals(isCreateJobCIssue, that.isCreateJobCIssue)
				&& Objects.equals(isCreateSpJobIssue, that.isCreateSpJobIssue)
				&& Objects.equals(isWaitForSubIssueFinish, that.isWaitForSubIssueFinish)
				&& Objects.equals(isCreateCompareList, that.isCreateCompareList)
				&& Objects.equals(isModifyColumnData, that.isModifyColumnData)
				&& Objects.equals(workProject, that.workProject)
				&& Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
				&& Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
				&& Objects.equals(this.getCreatedBy(), this.getCreatedBy())
				&& Objects.equals(this.getCreatedAt(), this.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, detailId, processId, processOrder, groupId, nextLevel, backLevel, workProject,
				isWorkLevel, isWaitForSubIssueFinish, isCreateJobCIssue,isCreateCompareList,isModifyColumnData,isCreateSpJobIssue, this.getUpdatedBy(),
				this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt());
	}

}
