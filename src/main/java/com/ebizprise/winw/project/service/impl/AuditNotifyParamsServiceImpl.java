package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;
import com.ebizprise.winw.project.repository.IAuditNotifyParamsRepository;
import com.ebizprise.winw.project.vo.AuditNotifyParamsVO;

/**
 * 稽催通知參數設定 服務類別
 * @author adam.yeh
 */
@Service
@Transactional
public class AuditNotifyParamsServiceImpl extends BaseService {
    
    @Autowired
    private IAuditNotifyParamsRepository auditParamsRepo;

    /**
     * 新增或更新稽催通知參數
     * @param vo
     * @author adam.yeh
     */
    public void mergeNotifyParams (AuditNotifyParamsVO vo) {
        Date today = new Date();
        List<AuditNotifyParamsVO> voList = vo.getParams();
        String currentUserId = fetchLoginUser().getUserId();
        
        for (AuditNotifyParamsVO paramsVO : voList) {
            if (paramsVO.getId() != null) {
                paramsVO.setUpdatedAt(today);
                paramsVO.setUpdatedBy(currentUserId);
            } else {
                paramsVO.setUpdatedAt(today);
                paramsVO.setCreatedAt(today);
                paramsVO.setUpdatedBy(currentUserId);
                paramsVO.setCreatedBy(currentUserId);
            }
        }

        auditParamsRepo.saveAll(BeanUtil.copyList(voList, AuditNotifyParamsEntity.class));
    }

    /**
     * 獲取最新的稽催通知參數
     * @return
     * @author adam.yeh
     */
    public List<AuditNotifyParamsVO> getNotifyParams () {
        return BeanUtil.copyList(auditParamsRepo.findAll(), AuditNotifyParamsVO.class);
    }
    
}
