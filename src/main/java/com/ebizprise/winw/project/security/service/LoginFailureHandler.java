package com.ebizprise.winw.project.security.service;

import com.ebizprise.winw.project.entity.ErrorLogEntity;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.naming.CommunicationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

@Service("LoginFailureHandler")
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(LoginFailureHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ISysUserService sysUserService;

    public LoginFailureHandler() {
        setDefaultFailureUrl("/login");
        setUseForward(true);
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String address = request.getRemoteAddr();
        String loginId = request.getParameter("username");
        Locale currentLocale = LocaleContextHolder.getLocale();
        
        log.info("Source ip address : {}", address);
        
        ErrorLogEntity pojo = new ErrorLogEntity();
        
        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) { // 無此使用者
            logger.warn(messageSource.getMessage("login.user.not.exist", new String[]{loginId},  currentLocale));
        } else if (exception.getClass().isAssignableFrom(DisabledException.class)) { // 使用者未啟用
            logger.warn(messageSource.getMessage("login.user.not.enabled", new String[]{loginId}, currentLocale));
        } else if (exception.getClass().isAssignableFrom(AuthenticationException.class)) { // 帳號或密碼錯誤
            logger.warn(messageSource.getMessage("login.password.error.message", new String[]{loginId}, currentLocale));
        } else if (exception.getClass().isAssignableFrom(CommunicationException.class)) { // LDAP 連線愈時
            logger.warn(messageSource.getMessage("login.ldap.connection.timeout.messages", null, currentLocale));
        }
        
        pojo.setMessage(messageSource.getMessage("login.user.verify.fail", null, currentLocale));

        // 記錄使用者登入失敗時間
        SysUserLogEntity userLog = new SysUserLogEntity();
        userLog.setIp(address);
        userLog.setUserId(loginId);
        userLog.setTime(new Date());
        userLog.setStatus(UserStatusEnum.FAIL.desc);
        
        try {
            sysUserService.saveUserLog(userLog);
        } catch (Exception e) {
            throw e;
        }

        request.setAttribute("error", pojo);
        super.onAuthenticationFailure(request, response, exception);
    }

}
