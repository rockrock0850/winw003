/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.inc;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * AP工作單停機公告報表
 * @author adam.yeh
 */
@Service("apReportServiceImpl")
public class ApReportServiceImpl extends BaseReportOperationService {

    @Override
    public String getUrl() {
        return ReportOperationEnum.INC_AP_REPORT.getPath();
    }
 
    @Override
    public void setReportOperationVO(ReportOperationVO vo) {
    }

    @Override
    public void setDataSet (Collection<Object> dataSet, ReportOperationVO vo) {
    }


}
