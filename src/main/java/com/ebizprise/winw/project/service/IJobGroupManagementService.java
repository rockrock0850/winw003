package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 工作組別管理 介面
 */
public interface IJobGroupManagementService {

    /**
     * 工作組別管理 查詢
     */
    public List<SystemOptionVO> getJopGroupManagementByCondition(SystemOptionVO vo);

    /**
     * 工作組別管理 新增
     */
    public SystemOptionVO createData(SystemOptionVO vo);

    /**
     * 工作組別管理 新增
     */
    public SystemOptionVO update(SystemOptionVO vo);

}
