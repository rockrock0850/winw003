package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.SystemManagementVO;

/*
 * 系統名稱管理介面
 */
public interface ISystemManagementService {

    /**
     * 系統名稱管理 查詢
     */
    public List<SystemManagementVO> getSystemManagmentByCondition(SystemManagementVO vo);

    /**
     * 系統名稱管理 新增
     */
    public SystemManagementVO createData(SystemManagementVO vo);

    /**
     * 系統名稱管理 編輯
     */
    public SystemManagementVO update(SystemManagementVO vo);

}
