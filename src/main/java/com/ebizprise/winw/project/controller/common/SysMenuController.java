package com.ebizprise.winw.project.controller.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.service.ISysMenuService;
import com.ebizprise.winw.project.vo.MenuVO;

/**
 * 系統選單控制類別
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

	@Autowired
	private ISysMenuService sysMenuService;

  	@Override
  	public ModelAndView initPage() {
  	  return null;
  	}

    @ResponseBody
    @PostMapping(value = "/getMenuTree")
    public List<MenuVO> getMenus () {
        // String groupId = getUserInfo().getGroupId();
        // List<MenuVO> menus = sysMenuService.findGroupPermissionMenusByGroupID(groupId);
        return getUserInfo().getMenuList();
    }

	@ResponseBody
	@GetMapping(value = "/getMenuTree/{treeLevel}")
	public List<Map<String, Object>> getMenuTree(@PathVariable(value = "treeLevel", required = true) String treeLevel)  {
		return sysMenuService.findSysMenuByTreeLevel(Integer.valueOf(treeLevel));
	}

}
