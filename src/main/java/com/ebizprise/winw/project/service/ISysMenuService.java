package com.ebizprise.winw.project.service;

import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.vo.MenuVO;

public interface ISysMenuService {
    
    /**
     * 找所有可顯示的選單
     * 
     * @return
     * @author adam.yeh
     */
    public List<MenuVO> findActivitedMenus();

    public List<Map<String, Object>> findSysMenuByTreeLevel(int treeLevel);
    
    /**
     * 找群組的選單權限
     * 
     * @param groupId
     * @return
     */
    public List<MenuVO> findGroupPermissionMenusByGroupID(String groupId);

}
