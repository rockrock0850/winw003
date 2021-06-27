package com.ebizprise.winw.project.singleton;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormIdEnum;

/**
 * 實現在執行緒安全的前提下, 取得各種表單類型的下1組編號。
 * 
 * @author gary.tsai, adam.yeh 2019/7/24
 */
public class FormIdHandler {

    private static final Logger log = LoggerFactory.getLogger(FormIdHandler.class);

    private static FormIdHandler instance = null;

    private Map<FormEnum, String> idMap = new HashMap<>();

    private FormIdHandler() {
    }

    public static FormIdHandler getInstance () {
        if (instance == null) {
            instance = new FormIdHandler();
        }
        
        return instance;
    }

    /**
     * 取得編號
     * 
     * @param clazz
     * @return
     */
    public synchronized String next (FormEnum clazz) {
        String formId = MapUtils.getString(idMap, clazz);
        
        if (StringUtils.isBlank(formId)) {
            formId = "0";
            log.info("The form id is empty when form class is {}", clazz);
        }
        
        formId = addOneFromId(formId);

        idMap.put(clazz, CommonStringUtil.exceptString(formId));
        
        return putPrefix(formId, clazz);
    }

    public void initIds (Map<FormEnum, String> idMap) {
        this.idMap = idMap;
    }
    
    private String putPrefix (String formId, FormEnum clazz) {
        FormIdEnum id = FormIdEnum.valueOf(clazz.name());
        formId = StringUtils.leftPad(formId, id.format(), "0");
        
        return clazz.prefix() + formId;
    }

    private String addOneFromId (String formId) {
        int id = 0;
        
        if (StringUtils.isBlank(formId)) {
            formId = "";
        } else {
            id = Integer.valueOf(formId);
            formId = String.valueOf(id += 1);
        }
        
        return formId;
    }

}
