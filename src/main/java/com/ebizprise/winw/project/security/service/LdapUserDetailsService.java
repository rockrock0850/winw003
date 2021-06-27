package com.ebizprise.winw.project.security.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.net.LdapUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.LdapUserEntity;

@Service("ldapUserDetailsService")
public class LdapUserDetailsService extends CommonLoginProcess {

    static final Logger logger = LoggerFactory.getLogger(LdapUserDetailsService.class);

    @Autowired
    private Environment env;

    @Autowired
    MessageSource messageSource;

    /**
     * 一般使用者認證程序
     *
     * @param loginId
     * @param password
     * @return
     */
    @Override
    public synchronized UsernamePasswordAuthenticationToken verifyNormalUser (String loginId, String password) {
        LdapUtil ldapUtil = null;
        String dn = env.getProperty("ldap.dn");
        String port = env.getProperty("ldap.port");
        String domain = env.getProperty("ldap.domain");
        String fullLoginId = loginId.split(StringConstant.AT).length > 1 ? 
                loginId : loginId + StringConstant.AT + domain; // 帳號如無域名自動加入
        
        try {
            ldapUtil = new LdapUtil(fullLoginId, password, domain, port, dn);
            ldapUtil.getLdapContext();
            ldapUtil.loginLdap();
        } catch (Exception e) {
            logger.warn(
                    messageSource.getMessage("login.ldap.error.messages", 
                    new String[]{loginId, e.getMessage()}, LocaleContextHolder.getLocale()));
            throw new BadCredentialsException(e.getMessage());
        } finally {
            ldapUtil.closeConnection();
        }
        
        logger.info(
                messageSource.getMessage("login.ldap.success.messages", 
                new String[]{loginId}, LocaleContextHolder.getLocale()));
        logger.info(
                messageSource.getMessage("login.user.update", 
                new String[]{loginId}, LocaleContextHolder.getLocale()));
        
        // 如使用 AD 帳號登入時需要域名需將域名移除
        String[] userNameSplit = fullLoginId.split(StringConstant.AT);
        LdapUserEntity ldapUser = sysUserService.findUserByUserId(userNameSplit[0]);
        
        if (Objects.isNull(ldapUser)) {
            throw new UsernameNotFoundException(userNameSplit[0]);
        } else if (StringConstant.SHORT_NO.equals(ldapUser.getIsEnabled())) {
            throw new DisabledException(loginId);
        } else {
            ldapUser.setPassword(passwordEncoder.encode(password));
            try {
                sysUserService.saveUser(ldapUser);
            } catch (Exception e) {
                throw e;
            }
        }
        
        return new UsernamePasswordAuthenticationToken(userNameSplit[0], password, null);
    }
}
