package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.entity.SysParameterLogEntity;
import com.ebizprise.winw.project.enums.SysCommonEnum;
import com.ebizprise.winw.project.enums.SysParamEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.repository.ISysParameterLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.SysParameterVO;

@Transactional
@Service("systemConfigService")
public class SystemConfigServiceImpl extends BaseService implements ISystemConfigService {

    private static final Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    @Autowired
    private ISysParameterRepository sysParamRepo;

    @Autowired
    private ISysParameterLogRepository sysParameterLogRepository;

    /**
     * 同步系統參數值
     */
    @Override
    public void syncParameterList() {
        logger.debug("into syncParameterList");

        List<SysParameterVO> allParams = selectAllParameters();
        logger.debug("selectAllParameters size:{}", allParams.size());
        // 先將舊的參數值刪除
        for (SysParameterVO p : allParams) {
            if (SysParametersEnum.isKeyExists(p.getParamKey())) {
                sysParamRepo.deleteById(p.getId());
            }
        }

        // 設定 param id 從 0 開始
        int i = 0;
        for (SysParametersEnum p : SysParametersEnum.values()) {
            SysParameterEntity sysParameterEntity = sysParamRepo.findByParamKey(p.name());
            if (Objects.isNull(sysParameterEntity)) {
                logger.debug("save param name:{}", p.name());

                // SysParameter
                sysParameterEntity = new SysParameterEntity();
                sysParameterEntity.setParamId(i);
                sysParameterEntity.setParamKey(p.name());
                sysParameterEntity.setParamValue(p.value);
                sysParameterEntity.setIsPassword(p.encryption);
                sysParameterEntity.setDescription(p.desc);
                sysParameterEntity.setCreatedAt(new Date());
                sysParameterEntity.setCreatedBy(UserInfoUtil.loginUserId());
                sysParamRepo.save(sysParameterEntity);

                // 紀錄使用者修改記錄
                SysParameterLogEntity sysParameterLogEntity = new SysParameterLogEntity();
                BeanUtil.copyProperties(sysParameterEntity, sysParameterLogEntity);
                sysParameterLogEntity.setTime(new Date());
                sysParameterLogEntity.setUserId(UserInfoUtil.loginUserId());
                sysParameterLogEntity.setAction(SysParamEnum.INSERT.action());
                sysParameterLogRepository.save(sysParameterLogEntity);
                i++;
            }
        }
    }

    @Override
    public List<SysParameterVO> selectAllParameters() {

        List<SysParameterVO> sysParameterVOList = new ArrayList<>();
        List<SysParameterEntity> datas = sysParamRepo.findAllByOrderByIdAsc();

        if (CollectionUtils.isNotEmpty(datas)) {
            return syncEntityToVO(datas);
        }
        return sysParameterVOList;
    }

    @Override
    public List<SysParameterVO> selectParametersByKey(String paramKey) {

        List<SysParameterVO> vos = new ArrayList<>();
        List<SysParameterEntity> datas = sysParamRepo.findByParamKeyLike(paramKey);

        if (CollectionUtils.isNotEmpty(datas)) {
            return syncEntityToVO(datas);
        }
        return vos;
    }

    @Override
    public void updateParameter(SysParameterVO vo) {
        logger.debug("into updateParameter id:{}", vo.getParamId());
        // 取出資料庫中的 parameter 參數
        SysParameterEntity old = sysParamRepo.findTop1ByParamId(Math.toIntExact(vo.getParamId()));
        if (StringUtils.isBlank(vo.getParamValue())) {
            old.setParamValue(null);
        } else {
            // 確認參數值是否需要解碼
            if (SysCommonEnum.YES.symbol().equalsIgnoreCase(vo.getIsPassword())) {
                CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
                String v = cryptoUtil.encrypt(vo.getParamValue());
                old.setParamValue(v);
                old.setIsPassword(StringConstant.SHORT_YES);
            } else {
                old.setParamValue(vo.getParamValue());
                old.setIsPassword(StringConstant.SHORT_NO);
            }
        }

        old.setDescription(StringUtils.isBlank(vo.getDescription()) ? null : vo.getDescription());
        old.setUpdatedAt(new Date());
        old.setUpdatedBy(UserInfoUtil.loginUserId());

        // 紀錄至 SYS_PARAMETER_LOG 中
        SysParameterLogEntity sysParameterLogEntity = new SysParameterLogEntity();
        BeanUtil.copyProperties(old, sysParameterLogEntity);
        sysParameterLogEntity.setId(null);
        sysParameterLogEntity.setTime(new Date());
        sysParameterLogEntity.setUserId(UserInfoUtil.loginUserId());
        sysParameterLogEntity.setAction(SysParamEnum.UPDATE.action());
        sysParameterLogRepository.save(sysParameterLogEntity);
    }

    /**
     * 將資料庫資料轉為畫面所使用的實體
     *
     * @param sysParameterEntityList
     * @return
     */
    private List<SysParameterVO> syncEntityToVO(List<SysParameterEntity> sysParameterEntityList) {
        CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
        List<SysParameterVO> vos = new ArrayList<>();

        for (SysParameterEntity d : sysParameterEntityList) {
            SysParameterVO sysParameterVO = new SysParameterVO();

            BeanUtil.copyProperties(d, sysParameterVO);
            sysParameterVO.setUpdateBy(d.getUpdatedBy());
            if (null != d.getUpdatedAt()) {
                sysParameterVO.setUpdateAt(DateUtils.toString(d.getUpdatedAt(), DateUtils.pattern12));
            }
            if (StringUtils.isNotBlank(d.getIsPassword()) && StringConstant.SHORT_YES.equals(d.getIsPassword())) {
                sysParameterVO.setParamValue(cryptoUtil.decode(d.getParamValue()));
            }
            vos.add(sysParameterVO);
        }
        return vos;
    }

    @Override
    public SysParameterVO getFileExtension () {
        SysParameterVO vo = new SysParameterVO();
        SysParameterEntity extendsion = sysParamRepo.findByParamKey(SysParametersEnum.FILE_EXTENSION.name());
        BeanUtil.copyProperties(extendsion, vo);
        
        return vo;
    }

    @Override
    public SysParameterVO getFileSize () {
        SysParameterVO vo = new SysParameterVO();
        SysParameterEntity size = sysParamRepo.findByParamKey(SysParametersEnum.FILE_SIZE.name());
        BeanUtil.copyProperties(size, vo);
        
        return vo;
    }

    @Override
    public SysParameterVO getFraction () {
        SysParameterVO vo = new SysParameterVO();
        SysParameterEntity size = sysParamRepo.findByParamKey(SysParametersEnum.IMPACT_ANALYSIS_VALIDATE_FRACTION.name());
        BeanUtil.copyProperties(size, vo);
        
        return vo;
    }

    @Override
    public SysParameterVO getMail () {
        SysParameterVO vo = new SysParameterVO();
        SysParameterEntity size = sysParamRepo.findByParamKey(SysParametersEnum.MAIL_SERVER_EMAIL.name());
        BeanUtil.copyProperties(size, vo);
        
        if (StringUtils.isBlank(vo.getParamValue())) {
            vo.setParamValue(env.getProperty("mail.sender"));
        }
        
        return vo;
    }
    
}
