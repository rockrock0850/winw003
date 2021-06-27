package com.ebizprise.winw.project.vo;
import com.ebizprise.winw.project.base.vo.BaseVO;

/**
 * 匯入假日資料的資料物件
 * @author adam.yeh
 */
public class HolidayVO extends BaseVO {

    private String year;            // 年份
    private String date;            // 日期
    private String name;            // 節日名稱
    private String isHoliday;       // 是否為放假日或補上班日(Y=放假日/N=補班日)
    private String holidayCategory; // 放假日種類
    private String description;     // 放假日備註
    private String fileName;            // 檔案名稱
    private boolean isView;         // 是否要查詢指定年份行事曆清單

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getIsHoliday () {
        return isHoliday;
    }

    public void setIsHoliday (String isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getHolidayCategory () {
        return holidayCategory;
    }

    public void setHolidayCategory (String holidayCategory) {
        this.holidayCategory = holidayCategory;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getYear () {
        return year;
    }

    public void setYear (String year) {
        this.year = year;
    }

    public boolean getIsView () {
        return isView;
    }

    public void setIsView (boolean isView) {
        this.isView = isView;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

}
