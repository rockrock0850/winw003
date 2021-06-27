package com.ebizprise.winw.project.interceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ebizprise.winw.project.service.ISysMenuService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.MenuVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 群組權限選單 欄截器
 * 
 * @author adam.yeh, willy.peng
 */
@Service
public class GroupPermissionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GroupPermissionInterceptor.class);

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private ISysMenuService sysMenuService;

    @Override
    public boolean preHandle (
            HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Object userObject = httpSession.getAttribute(UserInfoUtil.loginUserId());
        
        if (!isUserVO(userObject)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }

        boolean passUri = true;
        SysUserVO userInfo = (SysUserVO) httpSession.getAttribute(UserInfoUtil.loginUserId());
        String checkPath = needCheckPermission(request.getRequestURI()); // URI是否要檢核授權,空值:不須檢核

        // 判斷URI是否要檢核授權
        if (StringUtils.isNotBlank(checkPath) && Objects.nonNull(userInfo)) {
            // 取得此群組授權之子選單的路徑
            // example:/formSearch
            List<MenuVO> menuList = userInfo.getMenuList();
            Set<String> permissionPaths = new HashSet<>();
            for (MenuVO menu : menuList) {
                Set<String> datas = menu.getSubMenus().stream().map(x -> x.getPath()).collect(Collectors.toSet());
                permissionPaths.addAll(datas);
            }

            // checkPath不在群組授權之路徑=>該群組未授權此選單功能
            if (!permissionPaths.contains(checkPath)) {
                passUri = false;
            }
        }

        if (!passUri) {
            logger.info("GroupID:{} can't access uri:{} ", Objects.nonNull(userInfo) ? userInfo.getGroupId() : null, request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/dashboard");// 無法進入的網頁直接導回首頁
        }
        
        return passUri;
    }

    @Override
    public void postHandle (
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion (
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    @Override
    public void afterConcurrentHandlingStarted (
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    }

    /**
     * 檢查URI是否需要檢核,回傳空值表示此URI不須檢核
     * 
     * @param uri
     * @return
     */
    private String needCheckPermission(String uri) {
        String checkPath = "";

        // 先拿出所有選單的路徑
        List<MenuVO> menus = sysMenuService.findActivitedMenus();
        Set<String> menuPaths = new HashSet<>();
        for (MenuVO menu : menus) {
            Set<String> datas = menu.getSubMenus().stream().map(x -> x.getPath()).collect(Collectors.toSet());
            menuPaths.addAll(datas);
        }

        // 若URI跟選單路徑有match=>表示此URI需要檢核是否授權
        for (String path : menuPaths) {
            if (StringUtils.isNotBlank(path) && uri.contains(path)) {
                checkPath = path;
                break;
            }
        }

        return checkPath;
    }

    private boolean isUserVO (Object userObject) {
        return userObject != null && (userObject instanceof SysUserVO);
    }

}
