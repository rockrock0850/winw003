package com.ebizprise.winw.project.security.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.config.ApplicationContextHelper;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.UserInfoUtil;

@Service("logoutSuccessHandler")
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessHandler.class);
    
    @Autowired
    private HttpSession session;

    public LogoutSuccessHandler() {
        setDefaultTargetUrl("/login");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long lastAction = TimeUnit.MILLISECONDS.toSeconds(session.getLastAccessedTime());
        int timeout= session.getMaxInactiveInterval();
        boolean isTimeOut = (now-lastAction) > timeout;
        
        destroyLoginUserInfo();
        
        if (authentication != null && !isTimeOut) {
            String userId = authentication.getPrincipal().toString();

            if (StringUtils.isNotBlank(userId)) {
                ISysUserService sysUserService = (SysUserServiceImpl) ApplicationContextHelper.getBean("sysUserService");
                // 記錄使用者登出時間
                SysUserLogEntity sysUserLogEntity = new SysUserLogEntity();
                sysUserLogEntity.setUserId(userId);
                sysUserLogEntity.setStatus(UserStatusEnum.LOGOUT.desc);
                sysUserLogEntity.setTime(new Date());
                sysUserLogEntity.setIp(request.getRemoteAddr());
                try {
                    sysUserService.saveUserLog(sysUserLogEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            logger.debug("user:" + userId + "logout" + request.getContextPath());
        }
        
        super.onLogoutSuccess(request, response, authentication);
    }

    private void destroyLoginUserInfo () {
        String userId = UserInfoUtil.loginUserId();
        session.removeAttribute(userId);
    }

}
