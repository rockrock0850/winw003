package com.ebizprise.winw.project.security.service;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("customAuthenticationProvider")
public class CustomAuthenticationProvider extends CommonLoginProcess {
	static final Logger logger = LoggerFactory.getLogger(LdapUserDetailsService.class);

    /**
     * 一般使用者認證程序
     *
     * @param loginId
     * @param password
     * @return
     */
    @Override
    public UsernamePasswordAuthenticationToken verifyNormalUser (String loginId, String password) {
        LdapUserEntity ldapUser = sysUserService.findUserByUserId(loginId);

        if (Objects.isNull(ldapUser)) {
            throw new UsernameNotFoundException(loginId);
        } else if (StringConstant.SHORT_NO.equals(ldapUser.getIsEnabled())) {
            throw new DisabledException(loginId);
        }

        if (!passwordEncoder.matches(password, ldapUser.getPassword())) {
            throw new BadCredentialsException("");
        }

        return new UsernamePasswordAuthenticationToken(loginId, password, null);
    }
}
