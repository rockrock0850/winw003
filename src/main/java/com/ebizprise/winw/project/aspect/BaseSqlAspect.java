package com.ebizprise.winw.project.aspect;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

public abstract class BaseSqlAspect extends BaseAspect {
    
	private final long warnWhenOverTime = 2 * 60 * 1000L;// Minutes
	
	protected long getMemoryInUse () {
	    return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}
	
	protected boolean isRunOverLimitTime (long time) {
	    return time > warnWhenOverTime;
	}

	protected String createWarnMessage (Map<String, String> logInfo, long time, Object[] args) {
        StringBuilder sb = new StringBuilder()
            .append(MapUtils.getString(logInfo, "className", ""))
            .append(".")
            .append(MapUtils.getString(logInfo, "methodName", ""))
            .append("() cost time [")
            .append(time)
            .append("] ms");
        
        return sb.toString();
    }
	
}
