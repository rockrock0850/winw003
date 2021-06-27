package com.ebizprise.winw.project.base.vo;

import java.util.Date;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.jdbc.criteria.Paging;

public abstract class BaseVO extends Paging {

    private Long id;                     // 唯一識別碼
    private Date updatedAt;              // 變更時間
    private Date createdAt;              // 建立時間
    private String updatedBy;            // 變更人員
    private String createdBy;            // 建立人員
    private String validateLogicError;   // 檢核結果訊息或其他需要傳遞至前端的訊息

    public Date getUpdatedAt () {
        return updatedAt;
    }

    public void setUpdatedAt (Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy () {
        return updatedBy;
    }

    public void setUpdatedBy (String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy () {
        return createdBy;
    }

    public void setCreatedBy (String createdBy) {
        this.createdBy = createdBy;
    }

    public String getValidateLogicError () {
        return validateLogicError;
    }

    public void setValidateLogicError (String validateLogicError) {
        this.validateLogicError = validateLogicError;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }
    
    @Override
    public String toString () {
        return BeanUtil.toJson(this);
    }

}
