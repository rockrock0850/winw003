package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 表單附件VO
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年9月19日
 */
public class FormFileVO extends BaseVO {
    private String formId;
    private String name;
    private String description;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
