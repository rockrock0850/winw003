package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.JobManagementVO;

/*
 * 工作要項管理介面
 */
public interface IJobItemManagementService {

    /*
     * 工作要項管理 查詢
     */
    public List<JobManagementVO> getJobItemManagementByCondition(JobManagementVO vo);

    /*
     * 工作要項管理 新增
     */
    public JobManagementVO createData(JobManagementVO vo);

    /*
     * 工作要項管理 編輯
     */
    public JobManagementVO update(JobManagementVO vo);

}
