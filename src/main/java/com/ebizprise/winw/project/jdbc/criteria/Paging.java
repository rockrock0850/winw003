package com.ebizprise.winw.project.jdbc.criteria;

import java.util.List;

import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.enums.ResourceEnum;

/**
 * 實現伺服器端分頁的資料物件<br>
 * P.S. 如果需要使用伺服器端分頁的SQL需要排序, 直接使用Paging.orderBy來排序。
 * @author adam.yeh
 */
public abstract class Paging {

    private static String  pageingTemplate = null;
    
    private int page;                   // 當前頁碼
    private int pagesize;               // 每頁顯示筆數
    private int recordsTotal;           // 總資料筆數
    private String sorting = "";        // 排序方式
    private int recordsFiltered;        // 總資料筆數(顯示於頁角)
    private List<String> orderBy;       // 需要使用哪些欄位排序
    
    static {
        ResourceEnum resource = ResourceEnum.SQL.getResource("PAGING");
        StringBuilder path = new StringBuilder();
        path.append(resource.dir());
        path.append(resource.file());
        path.append(resource.extension());
        pageingTemplate = FileUtil.readFile(path.toString());
    }
    
    public String getPagingSQL () {
        return pageingTemplate;
    }

    public int getPage () {
        return page;
    }

    public void setPage (int page) {
        this.page = page;
    }

    public int getPagesize () {
        return pagesize;
    }

    public void setPagesize (int pagesize) {
        this.pagesize = pagesize;
    }

    public int getRecordsTotal () {
        return recordsTotal;
    }

    public void setRecordsTotal (int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered () {
        return recordsFiltered;
    }

    public void setRecordsFiltered (int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<String> getOrderBy () {
        return orderBy;
    }

    public void setOrderBy (List<String> orderBy) {
        this.orderBy = orderBy;
    }

    public String getSorting () {
        return sorting;
    }

    public void setSorting (String sorting) {
        this.sorting = sorting;
    }

}
