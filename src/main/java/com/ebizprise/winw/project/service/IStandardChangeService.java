package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 標準變更作業管理 介面
 * 
 * @author momo.liu 2020/08/13
 */
public interface IStandardChangeService {

    /**
     * 標準變更作業管理 查詢
     */
    public List<SystemOptionVO> getSystemOptionByCondition(SystemOptionVO vo);

    /**
     * 標準變更作業管理 新增
     */
    public SystemOptionVO createData(SystemOptionVO vo);

    /**
     * 標準變更作業管理 編輯
     */
    public SystemOptionVO update(SystemOptionVO vo);

}
