package com.ebizprise.winw.project.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;

import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.service.ISysMenuService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.MenuVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 系統服務層 基礎類別
 */
public abstract class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

	@Autowired
	private MessageSource messageSource;

    @Autowired
    protected JdbcRepositoy jdbcRepository;
	protected Locale currentLocale;
    protected String defaultMsg;

    @Autowired
    private HttpSession session;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    protected Environment env;

    /**
     * 取得登入者資訊
     * 
     * @return
     * @author adam.yeh
     */
    protected SysUserVO fetchLoginUser () {
        String userId = UserInfoUtil.loginUserId();
        
        if ("anonymousUser".equals(userId)) {
            return null;
        }

        SysUserVO userInfo = null;
        Object userObject = session.getAttribute(userId);
        
        if (userObject instanceof SysUserVO) {
            userInfo = (SysUserVO) session.getAttribute(userId);
        } else {
            ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_USER_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            userInfo = jdbcRepository.queryForBean(resource, params, SysUserVO.class);

            logger.info("fetchLoginUser: current userId={}, and userInfo is nullable {}", 
                    params.get("userId"), userInfo == null);

            // 取得該群組的選單權限
            if (userInfo != null) {
                List<MenuVO> menus = sysMenuService.findGroupPermissionMenusByGroupID(userInfo.getGroupId());
                userInfo.setMenuList(menus);
            }
        }
        
        return userInfo;
    }

	protected String getMessage(String key, String... args) {
	    String i18n = "";
	    
		try {
		    currentLocale = LocaleContextHolder.getLocale();
		    i18n = messageSource.getMessage(key, args, currentLocale);
        } catch (NoSuchMessageException e) {
            i18n = messageSource.getMessage("", args, currentLocale);
        }
		
		return i18n;
	}

}
