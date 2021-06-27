/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.service.IReportOperationSelectService;
import com.ebizprise.winw.project.service.IReportOperationService;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 集合各報表實作方法	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月17日
 */
@Transactional
@Service("reportOperationSelectService")
public class ReportOperationSelectServiceImpl  extends BaseService implements IReportOperationSelectService {
    @Autowired
    @Qualifier("sRAccuracyService")
    IReportOperationService sRAccuracyService;    
    @Autowired
    @Qualifier("sRSuccessRateService")
    IReportOperationService sRSuccessRateService;    
    @Autowired
    @Qualifier("sRNotFinishBfTargetTimeService")
    IReportOperationService sRNotFinishBfTargetTimeService;    
    
    @Autowired
    @Qualifier("qCloseOnTimeForShirleyService")
    IReportOperationService qCloseOnTimeForShirleyService;
    @Autowired
    @Qualifier("qCloseOnTimeInTargetFinishYMService")
    IReportOperationService qCloseOnTimeInTargetFinishYMService;
    @Autowired
    @Qualifier("qSpecialCloseService")
    IReportOperationService qSpecialCloseService; 
    @Autowired
    @Qualifier("qStatisticCloseService")
    IReportOperationService qStatisticCloseService; 
    @Autowired
    @Qualifier("qStatisticLevelService")
    IReportOperationService qStatisticLevelService; 
    @Autowired
    @Qualifier("qStatisticSourceService")
    IReportOperationService qStatisticSourceService; 
    
    @Autowired
    @Qualifier("iNCListReportService")
    IReportOperationService iNCListReportService; 
    @Autowired
    @Qualifier("iNCCloseRateOnTimeRateReportService")
    IReportOperationService iNCCloseRateOnTimeRateReportService;
    @Autowired
    @Qualifier("iNCInterruptService")
    IReportOperationService iNCInterruptService;
    @Autowired
    @Qualifier("iNCSecurityEventService")
    IReportOperationService iNCSecurityEventService;
    @Autowired
    @Qualifier("iNCStatisticMulitMonthService")
    IReportOperationService iNCStatisticMulitMonthService;
    @Autowired
    @Qualifier("iNCStatisticReportDetailService")
    IReportOperationService iNCStatisticReportDetailService;
    @Autowired
    @Qualifier("iNCStatisticReportService")
    IReportOperationService iNCStatisticReportService;
    @Autowired
    @Qualifier("apReportServiceImpl")
    IReportOperationService apReportServiceImpl;
    @Autowired
    @Qualifier("iNCStatisticReportAPIssueService")
    IReportOperationService iNCStatisticReportAPIssueService;
    @Autowired
    @Qualifier("iNCStatisticReportIDCService")
    IReportOperationService iNCStatisticReportIDCService;
    
    @Autowired
    @Qualifier("cHGStatisticIsNewServiceService")
    IReportOperationService cHGStatisticIsNewServiceService;
    @Autowired
    @Qualifier("cHGStatisticLevelService")
    IReportOperationService cHGStatisticLevelService;
    @Autowired
    @Qualifier("cHGStatisticSourceService")
    IReportOperationService cHGStatisticSourceService;
    @Autowired
    @Qualifier("CHGAlterResultServiceImpl")
    IReportOperationService cHGAlterResultService;
    @Autowired
    @Qualifier("cHGStatisticTypeService")
    IReportOperationService cHGStatisticTypeService;


    @Override
    public String getPageUrl(String operation) {      
        return getOperation().get(operation).getPageUrl();
    }    
    
    @Override
    public void setReportOperationResult(ReportOperationVO reportOperation) {
        getOperation().get(reportOperation.getOperation()).setResultDataList(reportOperation);
    }
    
    @Override
    public void exportExcel(ReportOperationVO reportOperation, HttpServletResponse response) {
        getOperation().get(reportOperation.getOperation()).exportExcel(reportOperation, response);
    }
    
    private Map<String, IReportOperationService> getOperation() {
        Map<String, IReportOperationService> operation = new HashMap <String, IReportOperationService>();
        operation.put(ReportOperationEnum.SR_ACCURACY.getName(), sRAccuracyService);
        operation.put(ReportOperationEnum.SR_SUCCESS_RATE.getName(), sRSuccessRateService);
        operation.put(ReportOperationEnum.SR_NOT_FINISH_BF_TARGET_TIME.getName(), sRNotFinishBfTargetTimeService);
        
        operation.put(ReportOperationEnum.Q_CLOSE_ON_TIME_FOR_SHIRLEY.getName(), qCloseOnTimeForShirleyService);
        operation.put(ReportOperationEnum.Q_CLOSE_ON_TIME_IN_TARGET_FINISH_Y_M.getName(), qCloseOnTimeInTargetFinishYMService);
        operation.put(ReportOperationEnum.Q_SPECIAL_CLOSE.getName(), qSpecialCloseService);
        operation.put(ReportOperationEnum.Q_STATISTIC_CLOSE.getName(), qStatisticCloseService);
        operation.put(ReportOperationEnum.Q_STATISTIC_LEVEL.getName(), qStatisticLevelService);
        operation.put(ReportOperationEnum.Q_STATISTIC_SOURCE.getName(), qStatisticSourceService);
        
        operation.put(ReportOperationEnum.INC_LIST_REPORT.getName(), iNCListReportService);
        operation.put(ReportOperationEnum.INC_CLOSE_RATE_ON_TIME_RATE_REPORT.getName(), iNCCloseRateOnTimeRateReportService);
        operation.put(ReportOperationEnum.INC_INTERRUPT.getName(), iNCInterruptService);
        operation.put(ReportOperationEnum.INC_SECURITY_EVENT.getName(), iNCSecurityEventService);
        operation.put(ReportOperationEnum.INC_STATISTIC_MULIT_MONTH.getName(), iNCStatisticMulitMonthService);
        operation.put(ReportOperationEnum.INC_STATISTIC_REPORT_DETAIL.getName(), iNCStatisticReportDetailService);
        operation.put(ReportOperationEnum.INC_STATISTIC_REPORT.getName(), iNCStatisticReportService);
        operation.put(ReportOperationEnum.INC_AP_REPORT.getName(), apReportServiceImpl);
        operation.put(ReportOperationEnum.INC_STATISTIC_REPORT_A_P_ISSUE.getName(), iNCStatisticReportAPIssueService);
        operation.put(ReportOperationEnum.INC_STATISTIC_REPORT_I_D_C.getName(), iNCStatisticReportIDCService);
        
        operation.put(ReportOperationEnum.CHG_STATISTIC_IS_NEW_SERVICE.getName(), cHGStatisticIsNewServiceService);
        operation.put(ReportOperationEnum.CHG_STATISTIC_LEVEL.getName(), cHGStatisticLevelService);
        operation.put(ReportOperationEnum.CHG_STATISTIC_SOURCE.getName(), cHGStatisticSourceService);
        operation.put(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getName(), cHGAlterResultService);
        operation.put(ReportOperationEnum.CHG_STATISTIC_TYPE.getName(), cHGStatisticTypeService);
        
        return operation;
    }

}
