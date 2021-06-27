package com.ebizprise.winw.project.base.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.ebizprise.project.utility.bean.BeanUtil;

/**
 * @author gary.tsai 2019/5/31
 */
@MappedSuperclass
public class BaseEntity {
    
    private String updatedBy;
    private Date updatedAt;
    private String createdBy;
    private Date createdAt;

    @Basic
    @Column(name = "UpdatedBy")
    public String getUpdatedBy () {
        return updatedBy;
    }

    public void setUpdatedBy (String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Basic
    @Column(name = "UpdatedAt")
    public Date getUpdatedAt () {
        return updatedAt;
    }

    public void setUpdatedAt (Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Basic
    @Column(name = "CreatedBy")
    public String getCreatedBy () {
        return createdBy;
    }

    public void setCreatedBy (String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CreatedAt")
    public Date getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString () {
        return BeanUtil.toJson(this);
    }

}
