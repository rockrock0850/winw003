package com.ebizprise.winw.project.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.ErrorLogEntity;
import com.ebizprise.winw.project.entity.ScheduleJobLogEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.entity.SysParameterLogEntity;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.repository.IErrorLogRepository;
import com.ebizprise.winw.project.repository.IScheduleJobLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.repository.ISysUserLogRepository;
import com.ebizprise.winw.project.service.IClearDataAndFilesService;

/**
 * 刪除過期資料 排程
 * @author gary.tsai 2019/9/9, adam.yeh
 */
@Transactional
@Service("clearDataAndFilesService")
public class ClearDataAndFilesServiceImpl extends BaseService implements IClearDataAndFilesService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClearDataAndFilesServiceImpl.class);

    @Autowired
    private IErrorLogRepository errorLogRepo;
    @Autowired
    private ISysUserLogRepository sysUserLogRepo;
    @Autowired
    private ISysParameterRepository sysParameterRepo;
    @Autowired
    private IScheduleJobLogRepository scheduleJobLogRepo;
    @Autowired
    private ISysParameterLogRepository sysParameterLogRepo;

    @Override
    public Map<String, Object> clearProcess() throws Exception {
        // 紀錄哪些資料總共刪除多少筆
        Map<String, Object> resultMap = new HashMap<>();
        
        // 取得保留資料的月份數
        SysParameterEntity keepingDataMonths = sysParameterRepo.findByParamKey(SysParametersEnum.KEEPING_DATA_MONTHS.name());
        
        if (Objects.isNull(keepingDataMonths)) {
            throw new Exception("無法從系統參數表取得保留資料月份數。");
        } else if (StringUtils.isBlank(keepingDataMonths.getParamValue())) {
            throw new Exception("保留資料月份數不得為空值或null。");
        }
        
        // 將目前日期跟設定月份相減，取得正確需移除紀錄之日期
        String delLimit = keepingDataMonths.getParamValue();
        Date delDate = DateUtils.getMonthByOffset(new Date(), -Integer.valueOf(delLimit));
        
        // 取得刪除日期並將時間改為指定日期的開始時間
        // 如:2018-01-10 15:29:20 -> 2018-01-10 00:00:00
        delDate = DateUtils.getDayStart(delDate);
        logger.info("刪除日期為 : " + DateUtils.toString(delDate, DateUtils._PATTERN_YYYYMMDD_HYPHEN));

        clearFiles(delDate, resultMap);
        clearData(delDate, delLimit, resultMap);
        
        return resultMap;
    }

    /**
     * 刪除指定日期之前的資料庫資料
     * 如:2019-01-10，則會刪除 2019-01-09 之前的資料(含 2019-01-09)
     *
     * @param delDate
     * @param delLimit 
     * @param resultMap 
     */
    private void clearData(Date delDate, String delLimit, Map<String, Object> resultMap) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("delLimit", delLimit);
            int delSize = jdbcRepository.update(ResourceEnum.SQL_COMMON.getResource("DELETE_EXPIRED_MAIL_LOG"), params);
            logger.info("SYS_MAIL_LOG 欲刪除資料筆數為 : " + delSize);
            
            List<ErrorLogEntity> errorLogEntityList = errorLogRepo.findAllByTimeBefore(delDate);
            logger.info("ERROR_LOG 欲刪除資料筆數為 : " + errorLogEntityList.size());

            List<SysUserLogEntity> sysUserLogEntityList = sysUserLogRepo.findAllByTimeBefore(delDate);
            logger.info("SYS_USER_LOG 欲刪除資料筆數為 : " + sysUserLogEntityList.size());

            List<ScheduleJobLogEntity> scheduleJobLogEntityList = scheduleJobLogRepo.findAllByEndTimeBefore(delDate);
            logger.info("SCHEDULE_JOB_LOG 欲刪除資料筆數為 : " + scheduleJobLogEntityList.size());

            List<SysParameterLogEntity> sysParameterLogEntityList = sysParameterLogRepo.findAllByUpdatedAtBefore(delDate);
            logger.info("SYS_PARAMETER_LOG 欲刪除資料筆數為 : " + sysParameterLogEntityList.size());

            errorLogRepo.deleteAll(errorLogEntityList);
            sysUserLogRepo.deleteAll(sysUserLogEntityList);
            scheduleJobLogRepo.deleteAll(scheduleJobLogEntityList);
            sysParameterLogRepo.deleteAll(sysParameterLogEntityList);
        } catch (Exception e) {
            logger.error("刪除資料庫 Log紀錄失敗。", e);
            throw e;
        }

        logger.info("已完成資料庫紀錄刪除程序");
    }

    /**
     * 刪除指定日期之前的檔案資料
     * 如:2019-01-10，則會刪除 2019-01-09 之前的資料(含 2019-01-09)
     * @param delDate
     * @return 
     */
    private void clearFiles (Date delDate, Map<String, Object> resultMap) {
        File[] logFiles = FileUtil.listFiles(new File(env.getProperty("log.base") + "/archive"));
        File[] tomcatFiles = FileUtil.listFiles(new File(env.getProperty("log.tomcat") + "/logs"));
        File[] formFileFolders = FileUtil.listDirs(new File(env.getProperty("form.file.download.dir")));
        
        logger.info("開始刪除AP的Log。");
        resultMap.put("logFiles", delFile(delDate, logFiles));
        
        logger.info("開始刪除Tomcat的Log。");
        resultMap.put("tomcatFiles", delFile(delDate, tomcatFiles));
        
        logger.info("開始刪除表單的附件。");
        resultMap.put("formFileFolders", delFile(delDate, formFileFolders));
    }

    private int delFile (Date delDate, File[] fList) {
        int count = 0;
        Date modifyDate;
        
        try {
            for (File f : fList) {
                modifyDate = new Date(f.lastModified());
                
                if (delDate.after(modifyDate)) {
                    if (!f.isFile()) {
                        for (File sub : f.listFiles()) {
                            sub.delete();
                        }
                    }
                    
                    f.delete();
                    count++;
                    logger.info(f.getAbsolutePath() + " 已被刪除。");
                }
            }
            
            logger.info("共刪除" + count + "筆。");
        } catch (Exception e) {
            logger.error("刪除紀錄檔案失敗。", e);
            throw e;
        }
        
        return count;
    }
    
}
