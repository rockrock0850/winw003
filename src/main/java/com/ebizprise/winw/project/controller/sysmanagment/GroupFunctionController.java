package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.ISysGroupService;
import com.ebizprise.winw.project.service.ISysMenuService;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.MenuVO;
import com.ebizprise.winw.project.vo.SysGroupPermissionVO;

/**
 * 
 * 群組功能管理
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年6月13日
 */
@RestController
@RequestMapping("/groupFunction")
public class GroupFunctionController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GroupFunctionController.class);

    @Autowired
    private ISysGroupService sysGroupService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.GROUP_FUNCTION.initPage());
    }
    
    /**
     * 編輯群組資料
     * 
     * @param queryVo
     * @return
     */
    @GetMapping(path = "/edit/{sysGroupId}")
    public ModelAndView editPage(GroupFunctionVO queryVo) {
        logger.debug("sysGroupId={}", queryVo.getSysGroupId());
        queryVo = sysGroupService.findBySysGroupId(queryVo.getSysGroupId());

        List<MenuVO> menus = sysMenuService.findActivitedMenus();
        queryVo.setMenus(menus);

        // 取得群組選單權限
        List<SysGroupPermissionVO> groupPermissions = sysGroupService
                .findGroupMenuPermissionBySysGroupId(queryVo.getSysGroupId());
        queryVo.setGroupPermissions(groupPermissions);

        List<LdapUserVO> ldapUsers = sysUserService.getGroupUsers(queryVo.getSysGroupId());
        queryVo.setLdapUsers(ldapUsers);

        return new ModelAndView(DispatcherEnum.GROUP_FUNCTION.editPage(), "groupData", queryVo);
    }

    /**
     * 查詢群組資料
     * 
     * @param queryVo
     * @return
     */
    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<GroupFunctionVO>> search(@RequestBody GroupFunctionVO queryVo) {
        List<GroupFunctionVO> groupLists = new ArrayList<>();

        if (StringUtils.isBlank(queryVo.getGroupName())) {
            groupLists = sysGroupService.getAllSysGroupVOs();
        } else {
            groupLists = sysGroupService.findSysGroupByName(queryVo);
        }

        return ResponseEntity.ok(groupLists);
    }

    /**
     * 更新群組功能資料
     * 
     * @param groupVo
     * @return
     */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GroupFunctionVO> save(@RequestBody GroupFunctionVO groupVo) {
        sysGroupService.saveGroupFunction(groupVo);

        return ResponseEntity.ok(groupVo);
    }
}
