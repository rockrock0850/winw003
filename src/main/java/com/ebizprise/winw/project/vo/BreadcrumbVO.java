package com.ebizprise.winw.project.vo;

import com.ebizprise.winw.project.base.vo.BaseVO;

public class BreadcrumbVO extends BaseVO {

    private String href;
    private String name;

    public String getHref () {
        return href;
    }

    public void setHref (String href) {
        this.href = href;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

}
