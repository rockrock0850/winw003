/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.controller.report.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.service.IReportOperationSelectService;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 各類報表作業
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月15日
 */
@RestController
@RequestMapping("/reportOperation")
public class ReportOperationController extends BaseController {
    
    @Autowired
    private IReportOperationSelectService reportOperationSelectService;
        
    @RequestMapping(path = "/{operation}/init")
    public ModelAndView initPage(@PathVariable("operation") String operation) {
        return new ModelAndView(reportOperationSelectService.getPageUrl(operation));
    }
    
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ReportOperationVO search(@RequestBody ReportOperationVO reportOperation) {
        reportOperationSelectService.setReportOperationResult(reportOperation);
        return reportOperation;
    } 
    
    @PostMapping(path = "/export")
    public void export() {
        String jsonStr = request.getParameter("formPostData");
        ReportOperationVO reportOperation = BeanUtil.fromJson(jsonStr, ReportOperationVO.class);
        reportOperationSelectService.exportExcel(reportOperation, response);
    }

    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }   
}
