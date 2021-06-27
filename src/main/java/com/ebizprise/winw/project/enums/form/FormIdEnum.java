package com.ebizprise.winw.project.enums.form;

/**
 * 定義表單編號格式
 * 
 * @author adam.yeh
 *
 */
public enum FormIdEnum {

    Q(8),
    SR(8),
    INC(8),
    CHG(10),
    Q_C(8),
    SR_C(8),
    INC_C(8),
    JOB(12),
    JOB_C(8),
    BA(8),
    KL(8);
    
    private int format;
    
    private FormIdEnum (int format) {
        this.format = format;
    }
    
    public int format () {
        return this.format;
    }
    
}
