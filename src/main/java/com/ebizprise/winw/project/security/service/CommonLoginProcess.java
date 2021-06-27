package com.ebizprise.winw.project.security.service;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * @author gary.tsai 2019/8/26
 */
abstract class CommonLoginProcess implements AuthenticationProvider {

    @Autowired
    protected ISysUserService sysUserService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    abstract UsernamePasswordAuthenticationToken verifyNormalUser(String loginId, String password);

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (StringUtils.isBlank(loginId)) {
            throw new UsernameNotFoundException("");
        }

        if (UserEnum.ADMIN.name().equalsIgnoreCase(authentication.getName())) {
            return verifySpecialUser(loginId, password);
        } else {
            return verifyNormalUser(loginId, password);
        }
    }

    /**
     * 特殊使用者認證程序，如:系統管理者
     *
     * @param userName
     * @param password
     * @return
     */
    public UsernamePasswordAuthenticationToken verifySpecialUser(String userName, String password) {
        // 以當前日期為密碼
        String dynamicPassword = DateUtils.toString(new Date(), DateUtils._PATTERN_YYYYMMDD);
        if (!dynamicPassword.equalsIgnoreCase(password)) {
            throw new BadCredentialsException("");
        }
        return new UsernamePasswordAuthenticationToken(userName, password, null);
    }
}
