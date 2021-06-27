package com.ebizprise.winw.project.base.controller;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.UserStatusEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 基礎控制類別
 */
public abstract class BaseController {

    @Autowired
    protected Environment env;
    
    @Autowired
    protected AuthenticationTrustResolver authenticationTrustResolver;
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Locale currentLocale;
    protected String defaultMsg;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ISysUserService sysUserService;
    
    public abstract ModelAndView initPage ();

    @ModelAttribute
    public void myModel(HttpServletRequest request, HttpServletResponse response, Model model) {
        SysUserVO sysUserVO = getUserInfo();

        if (!isCurrentAuthenticationAnonymous()) {
            this.request = request;
            this.response = response;
            this.currentLocale = LocaleContextHolder.getLocale();
            this.defaultMsg = "Message resource not found.";
        }

        model.addAttribute("sysUserVO", sysUserVO);
    }

    /**
     * This method returns true if users is already authenticated [logged-in], else
     * false.
     */
    protected boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    /**
     * 取得登入者資訊
     * 
     * @return
     */
    protected SysUserVO getUserInfo () {
        SysUserVO vo = sysUserService.getLoginUserInfo(UserInfoUtil.loginUserId());
        
        if (vo == null) {
            vo = new SysUserVO();
        }

        SysUserLogEntity sysUserLogEntity = sysUserService.findUserLogLastRecord(vo.getUserId(), UserStatusEnum.LOGIN.desc);

        if (Objects.isNull(sysUserLogEntity)) {
            vo.setLoginTime(new Date());
        } else {
            vo.setLoginTime(sysUserLogEntity.getTime());
        }
        
        return vo;
    }

    protected String getMessage(String key, String... args) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        
        return messageSource.getMessage(key, args, currentLocale);
    }
    
    /**
     * 取得當前登入人員的職位代碼
     * 
     * @return String
     */
    protected String getCurrentUserTitleCode() {
        return UserInfoUtil.getUserTitleCode(getUserInfo());
    }
    
    /**
     * 系統內有的欄位是以布林作為邏輯判斷, 但又是以字串型態定義。
     * @param boolen
     * @return
     * @author adam.yeh
     */
    protected boolean valueOfBoolean (String boolen) {
        boolean result;
        
        if (StringUtils.isBlank(boolen)) {
            result = false;
        } else if (StringConstant.SHORT_YES.equals(boolen) || 
                StringConstant.SHORT_NO.equals(boolen)) {
            result = StringConstant.SHORT_YES.equals(boolen);
        } else {
            result = Boolean.valueOf(boolen);
        }
        
        return result;
    }

}
