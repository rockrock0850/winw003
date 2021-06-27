package com.ebizprise.winw.project.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.ebizprise.project.utility.bean.BeanUtil;

/**
 * @author gary.tsai 2019/9/5, adam.yeh
 */
public abstract class BaseAspect {

    private static final Logger logger = LoggerFactory.getLogger(BaseAspect.class);
    
    protected Map<String, String> getLogInfo (ProceedingJoinPoint pjp) {
        String methodName = pjp.getSignature().getName();
        String targetName = pjp.getTarget().getClass().getSimpleName();
        
        Map<String, String> info = new HashMap<String, String>();
        info.put("className", targetName);
        info.put("methodName", methodName);
        
        return info;
    }
    
    protected String toJson (Object[] args, int limitation) {
        for (int i = 0; i < args.length; i++) {
            // 過濾掉不想Log出來的物件
            if(args[i] == null || 
                    args[i] instanceof BindingResult || 
                    args[i] instanceof HttpServletRequest ||
                    args[i] instanceof HttpServletResponse) {
                args[i] = args[i].getClass().getSimpleName();
                continue;
            }
            
            args[i] = argsLimitation(args[i]);
        }
        
        return BeanUtil.toJson(args);
    }
    
    private Object argsLimitation (Object object) {
        if(object instanceof List){
            try {
                List<?> list = (List<?>) object;
                
                if(list == null || list.isEmpty()){
                    return "";
                }
                
                // List 最多10筆
                object = list.subList(0, list.size() > 10 ? 10 : list.size());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        
        return object;
    }
    
}