package com.ebizprise.winw.project.vo;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

public class CommonFormVO extends BaseFormVO {

    private String workProject;          // 作業關卡代號

    // 附件欄位、DB變更頁簽裡面的附件
    private String type;                 // 哪個頁簽的檔案
    private String name;                 // 檔案名稱
    private String extension;            // 副檔名
    private String description;          // 檔案說明
    private String data;                 // 檔案資料大小(MB)
    private String islocked;             // 檔案是否鎖定
    private String alterContent;         // 變更內容
    private String layoutDataset;        // Layout Dataset
              
    // 日誌欄位                          
    private List<CommonFormVO> logList;  // 日誌清單

    // 工作單的作業關卡頁簽
    private String isOwner;              // 是否為工作項目分配人員
    private String isCreateJobIssue;     // 是否可開工作單
    private String isCreateSPJobIssue;   // 是否可開工作單
    private String isCreateJobCIssue;    // 是否開啟工作會辦單

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getExtension () {
        return extension;
    }

    public void setExtension (String extension) {
        this.extension = extension;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public List<CommonFormVO> getLogList () {
        return logList;
    }

    public void setLogList (List<CommonFormVO> logList) {
        this.logList = logList;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public String getIslocked() {
        return islocked;
    }

    public void setIslocked(String islocked) {
        this.islocked = islocked;
    }

    public String getIsOwner () {
        return isOwner;
    }

    public void setIsOwner (String isOwner) {
        this.isOwner = isOwner;
    }

    public String getIsCreateJobCIssue () {
        return isCreateJobCIssue;
    }

    public void setIsCreateJobCIssue (String isCreateJobCIssue) {
        this.isCreateJobCIssue = isCreateJobCIssue;
    }

    public String getIsCreateJobIssue () {
        return isCreateJobIssue;
    }

    public void setIsCreateJobIssue (String isCreateJobIssue) {
        this.isCreateJobIssue = isCreateJobIssue;
    }

    public String getIsCreateSPJobIssue () {
        return isCreateSPJobIssue;
    }

    public void setIsCreateSPJobIssue (String isCreateSPJobIssue) {
        this.isCreateSPJobIssue = isCreateSPJobIssue;
    }

    public String getAlterContent () {
        return alterContent;
    }

    public void setAlterContent (String alterContent) {
        this.alterContent = alterContent;
    }

    public String getLayoutDataset () {
        return layoutDataset;
    }

    public void setLayoutDataset (String layoutDataset) {
        this.layoutDataset = layoutDataset;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getWorkProject () {
        return workProject;
    }

    public void setWorkProject (String workProject) {
        this.workProject = workProject;
    }

}
