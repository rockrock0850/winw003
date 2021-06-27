package com.ebizprise.winw.project.security.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.ebizprise.winw.project.config.ApplicationContextHelper;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;

public class SessionListener extends HttpSessionEventPublisher {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(60 * 15);// Seconds
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long lastMove = TimeUnit.MILLISECONDS.toSeconds(session.getLastAccessedTime());
        int timeout= session.getMaxInactiveInterval();
        boolean isTimeOut = (currentTime-lastMove) > timeout;

        if (isTimeOut) {
            SessionRegistry sessionRegistry = getSessionRegistry();
            // 取得正確的 session
            SessionInformation sessionInfo = (sessionRegistry != null ? sessionRegistry
                    .getSessionInformation(event.getSession().getId()) : null);
            String userId = "";
            if (sessionInfo != null) {
                userId = (String) sessionInfo.getPrincipal();
            }
            if (StringUtils.isNotBlank(userId)) {
//                ISysUserService sysUserService = (SysUserServiceImpl) ApplicationContextHelper.getBean("sysUserService");
                // 記錄使用者逾時時間
                SysUserLogEntity sysUserLogEntity = new SysUserLogEntity();
                sysUserLogEntity.setUserId(userId);
                sysUserLogEntity.setStatus(UserStatusEnum.TIMEOUT.desc);
                sysUserLogEntity.setTime(new Date());
                try {
                    /*
                     * 若使用者登入逾時會在SYS_USER_LOG裡面新增一筆"TIMEOUT"狀態的紀錄
                     * 根據客戶需求視SA協調的時間將此功能補進正式環境
                     * 
                     * 1. 解開sysUserService.saveUserLog(sysUserLogEntity);的註解
                     * 2. 移除SysUserServiceImpl.findUserLogsByCondition()裡面的
                     *    conditions.and().unEqual("SLOG.Status", "TIMEOUT");
                     *    這行程式
                     */
                    // sysUserService.saveUserLog(sysUserLogEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        super.sessionDestroyed(event);
    }

    private SessionRegistry getSessionRegistry() {
        return (SessionRegistry) ApplicationContextHelper.getBean("sessionRegistry");
    }
}