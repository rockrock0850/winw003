package com.ebizprise.winw.project.service.startup;

import java.util.HashMap;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.repository.ISysGroupRepository;

/**
 * 統一事先準備所有系統內之表單流程需要用到的資料<br>
 * @author adam.yeh
 */
@Service("formProcessHelper")
public class FormProcessHelper extends BaseService {

    private HashMap<String,String> groupIdDivionMap = new HashMap<>();

    @Autowired
    private ISysGroupRepository sysGroupRepository;

    @PostConstruct
    public void startup () {
        setupGroupIdDivionMap();
    }
    
    private void setupGroupIdDivionMap () {
        String groupId, division;
        
        for(SysGroupEntity target : sysGroupRepository.findAll()) {
            groupId = target.getGroupId();
            division = target.getDivision();
            groupIdDivionMap.put(groupId, division);
        }
    }

    /**
     * 取得群組/科別對應表
     * @return
     * @author adam.yeh
     */
    public HashMap<String,String> getGIdmDivision () {
        return this.groupIdDivionMap;
    }

}