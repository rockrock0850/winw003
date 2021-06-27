/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service;
import java.util.List;

import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.SysOptionRoleVO;
/**
 * 處理前端共用元件的資料操作 服務類別
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年7月5日
 */
public interface IHtmlService {
    
    /**
     * 取得系統下拉選單清單
     * @param html
     * @return
     */
    public List<HtmlVO> getDropdownList(HtmlVO html, boolean release);
    
    /**
     * 取得系統下拉選單清單
     * 
     * @param optionId
     * @return
     */
    public List<HtmlVO> getDropdownList(String optionId);

    /**
     * 取得系統下拉選單子清單
     * 
     * @param html
     * @return
     */
    public List<HtmlVO> getSubDropdownList(HtmlVO html);

    /**
     * 取得系統下拉選單子清單
     * 
     * @param parentId
     * @return
     */
    public List<HtmlVO> getSubDropdownList(String parentId);

    /**
     * 取得系統名稱模組內的清單
     * 
     * @param vo
     * @return
     */
    public List<HtmlVO> getSystemList (HtmlVO vo);

    /**
     * 取得指定表單號的表單資訊頁簽-會辦科區塊已記錄的會辦科清單
     * 
     * @param formId
     * @return
     */
    public HtmlVO getCListSelecteds (String formId, String formClass);
    
    /**
     * 取得工作要項清單
     * 
     * @param vo
     * @return
     * @author emily.lai
     */
    public List<HtmlVO> getWorkingItemList(HtmlVO vo);

    /**
     * 取得指定表單號的表單資訊頁簽-工作要項區塊的清單
     * 
     * @param formId
     * @return
     * @author adam.yeh
     */
    public List<HtmlVO> getWorkingItems (String formId);

    /**
     * 查詢組態元件清單
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    public List<HtmlVO> getCComponentList (HtmlVO vo);
    
    /**
     * 透過值,抓出所有對應的下拉選單資訊
     * 
     * @param values
     * @return List
     */
    public List<HtmlVO> getDropdownListByValue(List<String> values);
    
    
    
    /**
     * 透過HtmlVo 取得對應的SYS_OPTION_ROLE資料
     * 
     * @param htmlVO
     * @return SysOptionRoleVO
     */
    public SysOptionRoleVO getSysOptionRole(HtmlVO htmlVO);
    
    /**
     * 透過OptionId以及value,取得對應的SYS_OPTION_ROLE資料
     * 
     * @param optionId
     * @param value
     * @return SysOptionRoleVO
     */
    public SysOptionRoleVO getSysOptionRole(String optionId,String value);
    
    /**
     * 找平行會辦的工作群組清單
     * @return
     * @author adam.yeh
     */
    public List<HtmlVO> getFormCParallels (HtmlVO vo);
    
    /**
     * 取得內會流程清單
     * @param formId
     * @return
     * @author justin.lin
     */
    public List<HtmlVO> getInternalProcessList(String formId);
    
    /**
     * 取得內會各流程狀態
     * @param formId
     * @return
     * @author justin.lin
     */
    public List<HtmlVO> getInternalProcessStatus(String formId);
    
    /**
     * 取得未完成的內會流程清單
     * @param formId
     * @return
     * @author justin.lin
     */
    public List<String> getUnfinishedInternalProcess(String formId);
    
    /**
     * 取得串會副科長名單
     * @return
     * @author justin.lin
     */
    public List<HtmlVO> getSplitProcessViceList();
}
