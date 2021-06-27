package com.ebizprise.winw.project.aspect;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect extends BaseAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.ebizprise.winw.project.controller..*.*(..)) && @annotation(com.ebizprise.winw.project.annotation.CtlLog)")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        Map<String, String> logInfo = getLogInfo(pjp);
        
        StringBuilder builder = new StringBuilder()
                .append(MapUtils.getString(logInfo, "className", ""))
                .append(".")
                .append(MapUtils.getString(logInfo, "methodName", ""))
                .append("() params : ")
                .append(toJson(pjp.getArgs(), 10));
        logger.info(builder.toString());
        
        Object obj = pjp.proceed();
        
        builder = new StringBuilder()
                .append(MapUtils.getString(logInfo, "className", ""))
                .append(".")
                .append(MapUtils.getString(logInfo, "methodName", ""))
                .append("() output : ")
                .append(obj);
        logger.info(builder.toString());
        
        builder = null;
        
        return obj;
    }
    
}
