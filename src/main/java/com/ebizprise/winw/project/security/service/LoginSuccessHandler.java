package com.ebizprise.winw.project.security.service;

import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.xml.vo.SysUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Service("loginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private HttpSession session;

    public LoginSuccessHandler () {
        setDefaultTargetUrl("/dashboard");
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        saveLoginUserInfo();

        // 記錄使用者登入時間
        SysUserLogEntity sysUserLogEntity = new SysUserLogEntity();
        sysUserLogEntity.setUserId(UserInfoUtil.loginUserId());
        sysUserLogEntity.setStatus(UserStatusEnum.LOGIN.desc);
        sysUserLogEntity.setTime(new Date());
        sysUserLogEntity.setIp(getIpAddress(request));
        sysUserService.saveUserLog(sysUserLogEntity);
        logger.debug("user:" + UserInfoUtil.loginUserId() + "login" + request.getContextPath());
        logger.debug("IP:" + getIpAddress(request));

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private void saveLoginUserInfo () {
        String userId = UserInfoUtil.loginUserId();
        SysUserVO userInfo = sysUserService.getLoginUserInfo(userId);
        session.setAttribute(userId, userInfo);
    }
}
