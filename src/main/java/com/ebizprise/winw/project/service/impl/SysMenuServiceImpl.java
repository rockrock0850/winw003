package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysMenuEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.SysMenuJDBC;
import com.ebizprise.winw.project.repository.ISysMenuRepository;
import com.ebizprise.winw.project.service.ISysMenuService;
import com.ebizprise.winw.project.vo.MenuVO;

@Transactional
@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseService implements ISysMenuService {

	@Autowired
	private ISysMenuRepository sysMenuEntityRepository;

	@Autowired
	private SysMenuJDBC sysMenuJDBC;

    @Override
    public List<MenuVO> findActivitedMenus () {
        List<SysMenuEntity> sysMenuEntityList = sysMenuEntityRepository.findActivited();
        
        String parentId;
        List<MenuVO> menus = new ArrayList<>();

        // 尋找選單的第一層
        for (SysMenuEntity entity : sysMenuEntityList) {
            parentId = entity.getParentId();
            
            if (parentId.equals("root")) {
                MenuVO root = new MenuVO();
                BeanUtils.copyProperties(entity, root);
                menus.add(root);
            }
        }
        
        // 遞回尋找子選單
        for (MenuVO parent : menus) {
            findSubMenus(parent, sysMenuEntityList);// 遞回開始
        }

        return menus;
    }

	@Override
	public List<Map<String, Object>> findSysMenuByTreeLevel(int treeLevel){
		return sysMenuJDBC.findByLevel(treeLevel);
	}

    @Override
    public List<MenuVO> findGroupPermissionMenusByGroupID(String groupId) {
        ResourceEnum resource = ResourceEnum.SQL_GROUP_PERMISSION.getResource("FIND_GROUP_PERMISSION_BY_GROUP_ID");

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<SysMenuEntity> permissionData = sysMenuJDBC.queryForList(resource, params, SysMenuEntity.class);

        String parentId;
        List<MenuVO> menus = new ArrayList<>();

        // 尋找選單的第一層
        for (SysMenuEntity entity : permissionData) {
            parentId = entity.getParentId();

            if (parentId.equals("root")) {
                MenuVO root = new MenuVO();
                BeanUtils.copyProperties(entity, root);
                menus.add(root);
            }
        }

        // 遞回尋找子選單
        for (MenuVO parent : menus) {
            findSubMenus(parent, permissionData);// 遞回開始
        }

        return menus;
    }

	// 傳入所有系統菜單依照parentId排列組合成樹狀資料結構
	private void findSubMenus(MenuVO parent, List<SysMenuEntity> sysMenuEntityList) {
        String parentId;
        String currentId = parent.getMenuId();
		List<MenuVO> childList = new ArrayList<>();

		for (SysMenuEntity entity : sysMenuEntityList) {
			parentId = entity.getParentId();

			/*
			 * 搜尋到最底層之後就不會再進行搜尋, 此判斷式將恆等於"否", 所以不會無限迴圈
			 */
            if (currentId.equals(parentId)) {
				MenuVO child = new MenuVO();
				BeanUtils.copyProperties(entity, child);
				childList.add(child);
				findSubMenus(child, sysMenuEntityList);
			}
		}

		parent.setSubMenus(childList);
	}

}
