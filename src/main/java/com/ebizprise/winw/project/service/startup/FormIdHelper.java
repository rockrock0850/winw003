package com.ebizprise.winw.project.service.startup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.singleton.FormIdHandler;

/**
 * 系統初始化之後, 記錄資料庫內最新1筆的表單編號。
 */
@Service("FormIdHelper")
public class FormIdHelper extends BaseService {
    
    /**
     * 紀錄當前資料庫內最新的表單編號
     */
    @PostConstruct
    public void startup () {
        Map<FormEnum, String> idMap = new HashMap<>();
        
        idMap.put(FormEnum.Q, getLastestId(FormEnum.Q));
        idMap.put(FormEnum.SR, getLastestId(FormEnum.SR));
        idMap.put(FormEnum.INC, getLastestId(FormEnum.INC));
        idMap.put(FormEnum.CHG, getLastestId(FormEnum.CHG));
        idMap.put(FormEnum.JOB, getLastestId(FormEnum.JOB));
        idMap.put(FormEnum.Q_C, getLastestId(FormEnum.Q_C));
        idMap.put(FormEnum.SR_C, getLastestId(FormEnum.SR_C));
        idMap.put(FormEnum.INC_C, getLastestId(FormEnum.INC_C));
        idMap.put(FormEnum.JOB_C, getLastestId(FormEnum.JOB_C));
        idMap.put(FormEnum.BA, getLastestId(FormEnum.BA));
        idMap.put(FormEnum.KL, getLastestId(FormEnum.KL));

        FormIdHandler.getInstance().initIds(idMap);
    }
    
    private String getLastestId (FormEnum clazz) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_LATEST_ID");
        Conditions conditions = new Conditions().and();
        if (clazz == FormEnum.JOB) { // AP/SP工作單共用一組流水號
            conditions.in("FormClass", Arrays.asList(FormEnum.JOB_AP.name(), FormEnum.JOB_SP.name()));
        } else if (clazz == FormEnum.JOB_C) { // AP/SP工作會辦單共用一組流水號
            conditions.in("FormClass", Arrays.asList(FormEnum.JOB_AP_C.name(), FormEnum.JOB_SP_C.name()));
        } else {
            conditions.equal("FormClass", clazz.name());
        }
        
        Map<String, Object> dataMap = jdbcRepository.queryForMap(resource, conditions);
        String latest = MapUtils.getString(dataMap, "FormId");
        
        return CommonStringUtil.exceptString(latest);
    }
    
}