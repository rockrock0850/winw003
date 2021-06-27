package com.ebizprise.winw.project.enums.form;

/**
 * 紀錄知識庫相關常數
 * @author adam.yeh
 */
public enum KnowledgeEnum {

    Q("form.question.process.knowledge.8");

    private String flowName;

    private KnowledgeEnum (String flowName) {
        this.flowName = flowName;
    }
    
    public String flowName () {
        return flowName;
    }
    
}
