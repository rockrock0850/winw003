package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 流程關卡文字
 * @author jacky.fu
 *
 */
public class FormProcessManagmentWordingVO extends BaseVO {

    private String detailid;
    private int processorder;
    private int wordingLevel;
    private String type;
    private String wording;
    
    public String getDetailid () {
        return detailid;
    }
    public void setDetailid (String detailid) {
        this.detailid = detailid;
    }
    public int getProcessorder () {
        return processorder;
    }
    public void setProcessorder (int processorder) {
        this.processorder = processorder;
    }
    public String getType () {
        return type;
    }
    public void setType (String type) {
        this.type = type;
    }
    public String getWording () {
        return wording;
    }
    public void setWording (String wording) {
        this.wording = wording;
    }
    public int getWordingLevel () {
        return wordingLevel;
    }
    public void setWordingLevel (int wordingLevel) {
        this.wordingLevel = wordingLevel;
    }
    
}
