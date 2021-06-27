package com.ebizprise.winw.project.thread;

import java.util.Map;

/**
 * 寄送表單通知信的時候會使用到的實作介面
 * @author adam.yeh
 */
public interface ProcessMail extends Runnable {

    /**
     * 執行續內需要用到的參數或物件
     * @return
     * @author adam.yeh
     */
    public Map<String, Object> getParams();
    
}