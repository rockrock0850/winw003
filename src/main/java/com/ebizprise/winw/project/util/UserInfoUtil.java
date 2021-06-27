package com.ebizprise.winw.project.util;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 取得登入者資訊
 * @author gary.tsai, adam.yeh 2019/7/5
 */
public class UserInfoUtil {
    
    /**
     * 取得登入者ID
     *
     * @return
     */
    public static String loginUserId() {
        String userId;
        Object principal = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            principal = auth.getPrincipal();
        }

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        return userId;
    }
    
    /**
     * 取得人員的職位代碼
     * @return String
     */
    public static String getUserTitleCode (SysUserVO user) {
        String result = "";
        String groupId = user.getGroupId();
        String division = user.getDivision();
        boolean isDirect = 
                StringUtils.containsIgnoreCase(groupId, UserEnum.DIRECT1.name()) || 
                StringUtils.containsIgnoreCase(groupId, UserEnum.DIRECT2.name());
        
        if(StringUtils.isNoneBlank(groupId)) {
            String target = groupId.split("-")[1];//如果沒有 - 的話,直接拋Exception,可快速釐清錯誤問題
            
            if(!isDirect && StringUtils.isNotBlank(division)) {
                result = target.replace(division, "");
                result = StringUtils.isNotBlank(result)? result : UserEnum.PIC.name();//如果是空字串,則代表為經辦,塞入PIC字串
            } else {
                target = target.toUpperCase(Locale.TAIWAN);//轉大寫
                if(target.indexOf(UserEnum.DIRECT1.name()) != -1) {//副理
                    result = UserEnum.DIRECT1.name();
                } else if(target.indexOf(UserEnum.DIRECT2.name()) != -1) {//協理
                    result = UserEnum.DIRECT2.name();
                }
            }
        }
        
        user = null;
        
        return result.toUpperCase();
    }
    
}
