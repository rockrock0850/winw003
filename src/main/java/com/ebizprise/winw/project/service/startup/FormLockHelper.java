package com.ebizprise.winw.project.service.startup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 表單控制權限鎖 輔助類別
 * 
 * @author adam.yeh
 */
@Service("FormLockHelper")
public class FormLockHelper extends BaseService {
    
    private Map<String, Object> locker;
    
    @PostConstruct
    public void startup () {
        locker = new HashMap<>();
    }
    
    /**
     * 檢查表單是否在編輯中
     * 
     * @param key
     * @return
     */
    public boolean isModifying (String key) {
        String formId1, formId2;
        boolean isFounded = false;
        
        for (String k : getLocks()) {
            formId1 = k.split("_")[0];
            formId2 = key.split("_")[0];
            
            if (formId1.equals(formId2)) {
                isFounded = true;
                break;
            }
        }
        
        return !(locker.containsKey(key) == isFounded);
    }
    
    /**
     * 取得所有表單鎖鍵值
     * 
     * @return
     */
    public Set<String> getLocks () {
        return locker.keySet();
    }
    
    /**
     * 上鎖
     * 
     * @param key
     * @param vo
     */
    public void lock (String key, Object vo) {
        locker.put(key, vo);
    }
    
    /**
     * 解鎖
     * 
     * @param key
     */
    public void unlock (String key) {
        locker.remove(key);
    }

    /**
     * 釋放所有已上鎖表單
     */
    public void release () {
        locker.clear();
    }
    
    /**
     * 模糊查詢上鎖物件( Null Safe )
     * 
     * @param key
     * @return ? extends BaseFormVO
     */
    public Object get (String key) {
        for (String k : getLocks()) {
            if (key.equals(k.split("_")[0])) {
                key = k;
                break;
            }
        }
        
        return locker.get(key) == null ? new BaseFormVO() : locker.get(key);
    }
    
    @Override
    public String toString () {
        return BeanUtil.toJson(locker);
    }
    
}
