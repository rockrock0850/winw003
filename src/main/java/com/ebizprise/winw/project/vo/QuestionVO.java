package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 衝擊分析題庫維護 VO
 * @author joyce.hsu
 * @version 1.0, Created at 2019年6月4日
 */
public class QuestionVO extends BaseVO {

    private String content;
    private String fraction;
    private String description;
    private String isValidateFraction;
    private String isAddUp;
    private String isEnable;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsValidateFraction() {
        return isValidateFraction;
    }

    public void setIsValidateFraction(String isValidateFraction) {
        this.isValidateFraction = isValidateFraction;
    }

    public String getIsAddUp() {
        return isAddUp;
    }

    public void setIsAddUp(String isAddUp) {
        this.isAddUp = isAddUp;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

}
