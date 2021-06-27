package com.ebizprise.winw.project.base.controller;

import org.apache.commons.codec.Charsets;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;

/**
 * WebService/RestFul API的基礎類別
 * @author adam.yeh
 */
public class BaseWsController extends BaseController {
    
    protected CryptoUtil base64Util = new CryptoUtil(new Base64Util());
    
    /**
     * 將輸出訊息加密成base64字串
     * @param propCode i18n裡面的代號
     * @return
     * @author adam.yeh
     */
    protected String getBase64Msg (String propCode) {
        return base64Util.encrypt(getMessage(propCode), Charsets.UTF_8.displayName());
    }

    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

}
