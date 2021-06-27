package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * DB變更、OPEN清單、程式清單頁簽裡面的資料庫變更清單的資料物件
 * @author adam.yeh
 */
public class SegmentVO extends BaseVO {

    private String formId;        // 表單編號
    private String type;          // 表單型態
    private String segment;       // Segment名稱
    private String keyValue;      // Key Value
    private String alterColumns;  // 變更的欄位
    private String alterContent;  // 變更內容
    private String layoutDataset; // Layout Dataset

    public String getSegment () {
        return segment;
    }

    public void setSegment (String segment) {
        this.segment = segment;
    }

    public String getKeyValue () {
        return keyValue;
    }

    public void setKeyValue (String keyValue) {
        this.keyValue = keyValue;
    }

    public String getAlterColumns () {
        return alterColumns;
    }

    public void setAlterColumns (String alterColumns) {
        this.alterColumns = alterColumns;
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

    public String getFormId () {
        return formId;
    }

    public void setFormId (String formId) {
        this.formId = formId;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

}
