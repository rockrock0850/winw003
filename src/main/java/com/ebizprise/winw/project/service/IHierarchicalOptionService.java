package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 階層式系統參數共用介面<br>
 * 只支援到二階層
 */
public interface IHierarchicalOptionService {

    /**
     * 查詢
     */
    public List<SystemOptionVO> getHierachicalList (SystemOptionVO vo);

    /**
     * 新增
     */
    public SystemOptionVO create (SystemOptionVO vo);

    /**
     * 編輯
     */
    public SystemOptionVO update (SystemOptionVO vo);

}
