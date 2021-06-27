package com.ebizprise.winw.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * 工作要項Entity
 * 
 * @author emily.lai
 * @version 1.0, Created at 2019/09/16
 */
@Entity
@Table(name = "WORKING_ITEM")
public class WorkingItemEntity extends BaseEntity {

    private Long id;
    private String workingItemId;       // 工作要項識別碼( WorkingItemName+_+Working+_+IsImpact+_+IsReview )
    private String workingItemName;     // 工作要項名稱
    private String spGroup;             // 系統科組別
    private String isImpact;            // 變更衝擊分析
    private String isReview;            // 變更覆核
    private String active;              // 狀態(啟用:Y/停用:N)


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    @Column(name = "WorkingItemId", unique = true, nullable = false)
    public String getWorkingItemId() {
        return workingItemId;
    }
    
    public void setWorkingItemId(String workingItemId) {
        this.workingItemId = workingItemId;
    }
    
    @Column(name = "WorkingItemName", nullable = false)
    public String getWorkingItemName() {
        return workingItemName;
    }
    
    public void setWorkingItemName(String workingItemName) {
        this.workingItemName = workingItemName;
    }
    
    @Column(name = "SpGroup")
    public String getSpGroup() {
        return spGroup;
    }
    
    public void setSpGroup(String spGroup) {
        this.spGroup = spGroup;
    }
    
    @Column(name = "IsImpact", length = 1)
    public String getIsImpact() {
        return isImpact;
    }
    
    public void setIsImpact(String isImpact) {
        this.isImpact = isImpact;
    }
    
    @Column(name = "IsReview", length = 1)
    public String getIsReview() {
        return isReview;
    }
    
    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }

    @Column(name = "Active", length = 2)
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
