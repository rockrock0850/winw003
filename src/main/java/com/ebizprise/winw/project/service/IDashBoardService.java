/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.DashBoardVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 公佈欄 首頁 共用服務介面	
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年7月15日
 */
public interface IDashBoardService {
    /**
     * 取得公佈欄 首頁 各項 資訊(含分派的工作,待審核表單,申請未送出,申請未結案)
     * 
     * @return List
     */
    List<DashBoardVO> getDashBoardDataList(SysUserVO user);   
    
    /**
     * 取得 表單狀態名稱
     * 
     * @return String
     */     
    String getFormStatus(String groupId,String status, String verifyLevel,int item);
}
