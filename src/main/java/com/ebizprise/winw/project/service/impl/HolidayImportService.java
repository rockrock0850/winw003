package com.ebizprise.winw.project.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.SysHolidayEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.repository.ISysHolidayRepository;
import com.ebizprise.winw.project.vo.HolidayVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;
import com.google.gson.JsonSyntaxException;

/**
 * 匯入假日資料 服務類別
 * @author adam.yeh
 */
@Service
@Transactional
public class HolidayImportService extends BaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(HolidayImportService.class);
    
    @Autowired
    private ISysHolidayRepository holidayRepo;

    /**
     * 上傳行事曆
     * @param year
     * @param file
     * @throws Exception
     * @author adam.yeh
     */
    public void importFile (String year, MultipartFile file) throws Exception {
        String temp = "";
        Date today = new Date();
        String yes = getMessage("yes");
        List<HolidayVO> holidays = null;
        List<SysHolidayEntity> entitys = new ArrayList<>();
        SysUserVO loginUser = fetchLoginUser();
        String fileName = file.getOriginalFilename();
        String content = new String (file.getBytes(), StandardCharsets.UTF_8.name());
        content = CommonStringUtil.unicodeToUtf8(content);
        
        try {
            holidays = BeanUtil.fromJsonToList(content, HolidayVO.class);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("輸入檔案格式不符, 請確認是否為JSON標準格式。");
        }
        
        for (HolidayVO holiday : holidays) {
            if (StringUtils.contains(holiday.getDate(), year)) {
                temp = holiday.getName();
                
                if (StringUtils.isBlank(temp)) {
                    holiday.setName(holiday.getHolidayCategory());
                }
                
                if (yes.equals(holiday.getIsHoliday())) {
                    holiday.setIsHoliday(StringConstant.SHORT_YES);
                } else {
                    holiday.setIsHoliday(StringConstant.SHORT_NO);
                }
                
                SysHolidayEntity entity = new SysHolidayEntity();
                BeanUtil.copyProperties(holiday, entity);
                entity.setYear(year);
                entity.setCreatedAt(today);
                entity.setUpdatedAt(today);
                entity.setFileName(fileName);
                entity.setCreatedBy(loginUser.getUserId());
                entity.setUpdatedBy(loginUser.getUserId());
                entity.setDate(DateUtils.fromString(
                        holiday.getDate(), DateUtils._PATTERN_YYYYMMDD_SLASH));
                entitys.add(entity);
            }
        }
        
        if (CollectionUtils.isNotEmpty(entitys)) {
            holidayRepo.deleteByYear(year);
            holidayRepo.saveAll(entitys);
        } else {
            logger.warn("放假日清單是空白的。");
        }
    }

    /**
     * 查詢指定年份行事曆清單或首頁清單
     * @param vo
     * @return
     * @author adam.yeh
     */
    public List<HolidayVO> getYearHolidays (HolidayVO vo) {
        boolean isView = vo.getIsView();
        List<HolidayVO> holidays = new ArrayList<>();
        
        if (isView) {
            List<SysHolidayEntity> entityList = holidayRepo.findByYearOrderById(vo.getYear());
            if (CollectionUtils.isNotEmpty(entityList)) {
                for (SysHolidayEntity e : entityList) {
                    HolidayVO holiday = new HolidayVO();
                    holiday.setName(e.getName());
                    holiday.setDate(DateUtils.toString(e.getDate(), DateUtils._PATTERN_YYYYMMDD_SLASH));
                    holidays.add(holiday);
                }
            } else {
                logger.warn("該年度沒有放假日資料。");
            }
        } else {
            ResourceEnum resource = ResourceEnum.SQL_HOLIDAY_IMPORT.getResource("FIND_HOLIDAY_YEAR_LIST");
            holidays = jdbcRepository.queryForList(resource, HolidayVO.class);
        }
        
        return holidays;
    }
    
    /**
     * 是否為例假日
     * @param time
     * @return
     * @throws Exception
     * @author jacky.fu
     */
    public boolean isHolidayByDate (Date time) throws Exception {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int count = holidayRepo.countByDateAndIsHoliday(calendar.getTime(), "Y");
        return (count > 0);
        
    }

}
