package com.ebizprise.winw.project.vo;

/**
 * 工作單的工作關卡頁簽的資料物件
 * 
 * @author adam.yeh
 */
public class CommonCheckPersonVO {

    private Long id;        // 流水號
    private String formId;  // 表單號碼
    private String order;   // 流程順序
    private String groupId; // 流程群組
    private String level;   // 工作項目
    private String userId;  // 作業人員

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getOrder () {
        return order;
    }

    public void setOrder (String order) {
        this.order = order;
    }

    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public String getLevel () {
        return level;
    }

    public void setLevel (String level) {
        this.level = level;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

}
