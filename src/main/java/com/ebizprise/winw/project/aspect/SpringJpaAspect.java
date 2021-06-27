package com.ebizprise.winw.project.aspect;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 記錄Spring data JPA 每個SQL執行時間。
 */
@Aspect
@Component
public class SpringJpaAspect extends BaseSqlAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringJpaAspect.class);

	@Pointcut("execution(public * org.springframework.data.repository.Repository+.*(..)) && @annotation(com.ebizprise.winw.project.annotation.TraceLog)")
	public void monitor() {}

//	@Around("monitor()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        Map<String, String> logInfo = getLogInfo(pjp);
        
        StringBuilder builder = new StringBuilder()
                .append(MapUtils.getString(logInfo, "className", ""))
                .append(".")
                .append(MapUtils.getString(logInfo, "methodName", ""))
                .append("() JVM memory in use = " + getMemoryInUse());
        logger.info(builder.toString());

        long begin = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long ended = System.currentTimeMillis() - begin;

        if (isRunOverLimitTime(ended)) {
            logger.warn(createWarnMessage(logInfo, ended, pjp.getArgs()));
        }
        
		return obj;
	}
	
}