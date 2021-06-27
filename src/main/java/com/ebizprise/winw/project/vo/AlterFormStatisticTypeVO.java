package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class AlterFormStatisticTypeVO extends BaseVO {

    private String dept;
    private String normalUrgent;
    private String normalNonUrgent;
    private String standardUrgent;
    private String standardNonUrgent;

    public String getNormalUrgent () {
        return normalUrgent;
    }

    public void setNormalUrgent (String normalUrgent) {
        this.normalUrgent = normalUrgent;
    }

    public String getNormalNonUrgent () {
        return normalNonUrgent;
    }

    public void setNormalNonUrgent (String normalNonUrgent) {
        this.normalNonUrgent = normalNonUrgent;
    }

    public String getStandardUrgent () {
        return standardUrgent;
    }

    public void setStandardUrgent (String standardUrgent) {
        this.standardUrgent = standardUrgent;
    }

    public String getStandardNonUrgent () {
        return standardNonUrgent;
    }

    public void setStandardNonUrgent (String standardNonUrgent) {
        this.standardNonUrgent = standardNonUrgent;
    }

    public String getDept () {
        return dept;
    }

    public void setDept (String dept) {
        this.dept = dept;
    }

}
