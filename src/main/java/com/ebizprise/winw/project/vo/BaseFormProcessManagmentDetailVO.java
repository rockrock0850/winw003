package com.ebizprise.winw.project.vo;

import java.util.List;

public class BaseFormProcessManagmentDetailVO {

    private String formId;
    private String detailId;
    private String processId;
    private int processOrder;
    private String groupId;
    private List<String> groupIdList;
    private String formClass;
    private String groupName;
    private Integer nextLevel;
    private Integer backLevel;
    private int formType;
    private String verifyLevel;
    private String verifyType;
    private String verifyResult;
    private boolean isNextLevel; // 是否為向下跳關
    private String isApprover;//是否為審核者
    private String subGroup;// 會辦系統科群組
    private boolean lastLevel;//是否為最後一關
    private boolean changeVerifyType;//是否需要改變流程狀態,例如:申請換審核 OR 審核換申請
    private String levelWordings; //關卡文字 JSON
    private String wording;

    public boolean isLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(boolean lastLevel) {
        this.lastLevel = lastLevel;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public int getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(int processOrder) {
        this.processOrder = processOrder;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    public Integer getBackLevel() {
        return backLevel;
    }

    public void setBackLevel(Integer backLevel) {
        this.backLevel = backLevel;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public int getFormType () {
        return formType;
    }

    public void setFormType (int formType) {
        this.formType = formType;
    }

    public String getVerifyType () {
        return verifyType;
    }

    public void setVerifyType (String verifyType) {
        this.verifyType = verifyType;
    }

    public boolean getIsNextLevel () {
        return isNextLevel;
    }

    public void setIsNextLevel (boolean isNextLevel) {
        this.isNextLevel = isNextLevel;
    }

    public String getVerifyResult () {
        return verifyResult;
    }

    public void setVerifyResult (String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getVerifyLevel () {
        return verifyLevel;
    }

    public void setVerifyLevel (String verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public String getFormClass () {
        return formClass;
    }

    public void setFormClass (String formClass) {
        this.formClass = formClass;
    }

    /**
     * @return the isApprover
     */
    public String getIsApprover() {
        return isApprover;
    }

    /**
     * @param isApprover the isApprover to set
     */
    public void setIsApprover(String isApprover) {
        this.isApprover = isApprover;
    }

    public List<String> getGroupIdList () {
        return groupIdList;
    }

    public void setGroupIdList (List<String> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public String getSubGroup () {
        return this.subGroup;
    }

    public void setSubGroup (String subGroup) {
        this.subGroup = subGroup;
    }

    /**
     * @return the changeVerifyType
     */
    public boolean isChangeVerifyType() {
        return changeVerifyType;
    }

    /**
     * @param changeVerifyType the changeVerifyType to set
     */
    public void setChangeVerifyType(boolean changeVerifyType) {
        this.changeVerifyType = changeVerifyType;
    }

    public String getLevelWordings () {
        return levelWordings;
    }

    public void setLevelWordings (String levelWordings) {
        this.levelWordings = levelWordings;
    }

    public String getWording () {
        return wording;
    }

    public void setWording (String wording) {
        this.wording = wording;
    }

}
