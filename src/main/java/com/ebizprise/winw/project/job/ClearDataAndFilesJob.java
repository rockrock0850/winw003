package com.ebizprise.winw.project.job;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.service.IClearDataAndFilesService;

/**
 * 清除過期Log資料_排程
 */
public class ClearDataAndFilesJob extends QuartzJobFactory {

    @Autowired
    public IClearDataAndFilesService clearDataAndFilesService;

    @Override
    public String executeJob(JobExecutionContext jobCtx) throws Exception {
        clearDataAndFilesService.clearProcess();
        return "";
    }
    
}
