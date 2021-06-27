package com.ebizprise.winw.project.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.doc.velocity.VelocityUtil;
import com.ebizprise.project.utility.net.HttpUtility;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormUserRecordEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysMailLogEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.payload.Mail;
import com.ebizprise.winw.project.repository.IFormUserRecordRepository;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.ISysMailLogRepository;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.service.IMailService;
import com.ebizprise.winw.project.service.IRemindFormUserRecordService;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentFormVo;
import com.ebizprise.winw.project.vo.FormRemindUserRecordVO;
import com.ebizprise.winw.project.vo.SysParameterVO;

/**
 * @author gary.tsai 2019/8/28
 */
@Service("remindFormUserRecordService")
public class RemindFormUserRecordServiceImpl extends BaseService implements IRemindFormUserRecordService {

    private static final Logger logger = LoggerFactory.getLogger(RemindFormUserRecordServiceImpl.class);

    @Autowired
    private IFormUserRecordRepository formUserRecordRepository;

    @Autowired
    private ISysGroupRepository sysGroupRepository;

    @Autowired
    protected ILdapUserRepository ldapUserRepository;

    @Autowired
    private IMailService mailService;

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private VelocityUtil velocityUtil;

    @Autowired
    private FormHelper formHelper;
    
    @Autowired
    private ISysMailLogRepository mailLogRepo;

    private IFormProcessManagmentService<BaseFormProcessManagmentFormVo> formProcessService;

    /**
     * 查詢表單在開單日期到預計完成日期的區間內，需填寫的日誌數，並最後一筆日誌的日期須跟檢查日的前一天日期月份相符
     *
     * @param formRemindUserRecordVOList
     * @param formDataMap
     */
    @Override
    public void checkFormIntervalMonthsOfLog(List<FormRemindUserRecordVO> formRemindUserRecordVOList, Map<String, FormRemindUserRecordVO> formDataMap) {

        Date formCreateDate; // 表單建立時間
        Date formETC; // 表單預計完成時間
        Date datePlusOneMonth;
        boolean isOverETC;
        int count;
        Long dataCount = 0L;
        try {
            for (FormRemindUserRecordVO formRemindUserRecordVO : formRemindUserRecordVOList) {
                if (Objects.nonNull(formDataMap.get(formRemindUserRecordVO.getFormId()))) {
                    continue;
                }
                isOverETC = false;
                count = 1; // 需將開始月份加入，如2019/03/31~2019/05/31，需將3月份算入
                formCreateDate = DateUtils.fromString(formRemindUserRecordVO.getCreateTime(), DateUtils.pattern11);
                formETC = DateUtils.fromString(formRemindUserRecordVO.geteCT(), DateUtils.pattern11);
                // 計算表單建立時間到預計完成時間間隔的月份數，需在今天日期前
                // 如: 20190801~20191121 當前日期為 20191122，中間間隔為3
                //     20191012~20191221 當前日期為 20191202，中間區隔為1
                while (!isOverETC) {
                    datePlusOneMonth = DateUtils.getMonthByOffset(formCreateDate, count);
                    if (DateUtils.isBefore(datePlusOneMonth, formETC) && DateUtils.isBefore(datePlusOneMonth, new Date())) {
                        count++;
                    } else {
                        isOverETC = true;
                    }
                }

                List<FormUserRecordEntity> formUserRecordEntityList = formUserRecordRepository.findAllByFormIdOrderByUpdatedAtDesc(formRemindUserRecordVO.getFormId());
                if (CollectionUtils.isNotEmpty(formUserRecordEntityList)) {
                    if (formUserRecordEntityList.size() >= count) { // 確認日誌數量符合區隔數
                        // 當日誌數量符合需求，最後一筆紀錄需為執行排程的月份
                        Date latestRecordDate = formUserRecordEntityList.get(0).getCreatedAt();
                        Date yesterday = DateUtils.getDateByOffset(new Date(), -1); // 需以排程前一天去判斷
                        boolean isSameMonth = DateUtils.isBeforeMonth(latestRecordDate, yesterday);
                        if (isSameMonth) { //符合數量+一個月內有寫 則不寄
                            continue;
                        }
                    }
                }
                formDataMap.put(formRemindUserRecordVO.getFormId(),formRemindUserRecordVO);
                dataCount++;
            }
        } catch (Exception e) {
            throw e;
        }
        logger.info("超過預計日期完成並未填寫日誌的表單共 : " + dataCount + " 筆");
    }

    /**
     * 查詢表單執行排程日期超過預計完成日期並科長未審核通過
     * 只取出已進入審核流程之各類表單
     *
     * @param formRemindUserRecordVOList
     * @param formDataMap
     * @throws Exception
     */
    @Override
    public void checkFormOverETCOfLog(List<FormRemindUserRecordVO> formRemindUserRecordVOList, Map<String, FormRemindUserRecordVO> formDataMap) throws Exception {

        SysGroupEntity sysGroupEntity;
        Integer verifyLevel;
        String departmentId;
        String division;
        String[] divisionSolvingSplit;
        int formProcessOrder;
        // 1. 先以科別及科長頭銜尋找正確的 GroupId
        // 2. 以 GroupId 和 DetailId 尋找各類表單審核流程中的 ProcessId
        // 3. 比對表單目前在各表單審核流程的停留關卡是否超過科長等級
        // 4. 檢查是否超過表單的預計時間1個月
        // 5. 是否有寫過日誌，沒寫加入，有寫但超過一個月也加入
        try {
            for (FormRemindUserRecordVO formRemindUserRecordVO : formRemindUserRecordVOList) {
                if (Objects.nonNull(formDataMap.get(formRemindUserRecordVO.getFormId()))) {
                    continue;
                }
                // 第1步驟
                divisionSolvingSplit = formRemindUserRecordVO.getDivisionSolving().split(StringConstant.DASH);
                if (divisionSolvingSplit.length <= 1) {
                    logger.warn("表單 : " + formRemindUserRecordVO.getFormId() + ", 缺少處理科別資訊");
                    continue;
                }
                departmentId = divisionSolvingSplit[0];
                division = divisionSolvingSplit[1];
                // 尋找指定部門的科長
                sysGroupEntity = sysGroupRepository.findByDivisionAndGroupNameLike(departmentId, division, "[_]" + UserEnum.DIVISION_CHIEF.wording());
                if (Objects.isNull(sysGroupEntity)) {
                    logger.warn("科別 : " + formRemindUserRecordVO.getDivisionSolving() + ", 找不到科長");
                    continue;
                }
                // 第2步驟
                formProcessService = formHelper.getFormProcessDetailService(FormEnum.valueOf(formRemindUserRecordVO.getFormClass()));
                formProcessOrder = formProcessService.getFormProcessOrder(formRemindUserRecordVO.getDetailId(), sysGroupEntity.getGroupId(), FormEnum.REVIEW.name());
                // 第3步驟
                verifyLevel = Integer.valueOf(formRemindUserRecordVO.getVerifyLevel());
                if (verifyLevel > formProcessOrder) {
                    continue;
                }
                // 第4步驟
                Date yesterday = DateUtils.getDateByOffset(new Date(), -1); // 需以排程前一天去判斷
                Date formECT = DateUtils.fromString(formRemindUserRecordVO.geteCT(), DateUtils.pattern11); //預估時間
                // 與預估時間不超過1個月則不列入清單
                if (DateUtils.getSmartDiff(formECT, yesterday, DateUtils.Type.Month) < 1) {
                    continue;
                }
                // 第5步驟
                List<FormUserRecordEntity> formUserRecordEntityList = formUserRecordRepository.findAllByFormIdOrderByUpdatedAtDesc(formRemindUserRecordVO.getFormId());
                //檢查有無填寫過日誌
                if (CollectionUtils.isNotEmpty(formUserRecordEntityList)) {
                    Date latestRecordDate = formUserRecordEntityList.get(0).getCreatedAt(); // 最後一筆日誌的時間
                    // 與上一筆日誌是差距不到一個月就不寄
                    if(DateUtils.getSmartDiff(latestRecordDate, yesterday, DateUtils.Type.Month) < 1) {
                        continue;
                    };
                }
                // 加入寄送清單
                formDataMap.put(formRemindUserRecordVO.getFormId(), formRemindUserRecordVO);

            }
        } catch (Exception e) {
            throw e;
        }

        logger.info("超過預計完成日並無填寫日誌表單共 : " + formDataMap.size() + " 筆");

    }

    /**
     * 將需提醒的表單寄給開單者
     *
     * @param formDataMap
     */
    @Override
    public void sendMailToCreateUser(Map<String, FormRemindUserRecordVO> formDataMap) throws Exception {
        if (formDataMap.size() > 0) {
            BaseFormService<BaseFormVO> baseFormService;
            BaseFormVO baseFormVO;
            LdapUserEntity ldapUserEntity;
            String mailSubject; // 從 i18n 取得的內容
            String formClassWording;
            String groupName;
            // 因排程為自動執行，無法從 request 取得 local ip，所以使用 java 原生工具取得，如合庫有 domain name
            // 需改為 domain name 解決
            String ip = HttpUtility.getLocalIp();
            String hyperLink = String.format("https://%s/ISWP/formSearch/search/", ip);
            String content;
            Mail mail = new Mail();
            // 郵件服務初始化
            mailService.setInitData();
            SysParameterVO mailSender = systemConfigService.getMail();
            for (Object key : formDataMap.keySet()) {
                FormRemindUserRecordVO formData = formDataMap.get(key);
                ldapUserEntity = ldapUserRepository.findByUserIdAndIsEnabled(formData.getUserCreated(), StringConstant.SHORT_YES);
                String formId = key.toString();
                if (Objects.isNull(ldapUserEntity) || StringUtils.isBlank(ldapUserEntity.getEmail())) {
                    logger.warn("使用者 : " + formDataMap.get(key) + " 可能未有 mail address 或者被停權");
                    continue;
                }
                formClassWording = getMessage(FormEnum.valueOf(formData.getFormClass()).wording());
                groupName = sysGroupRepository.findGroupNameJoinForm(formId);
                mailSubject = getMessage("form.mail.remind.user", new String[]{groupName, formId, formClassWording});
                // 取得表單 Detail 資訊
                baseFormService = formHelper.getFormInfoDetailService(FormEnum.valueOf(formData.getFormClass()));
                baseFormVO = baseFormService.getFormDetailInfo(formId);

                // Template 檔案所需要的變數名稱
                Map<String, Object> map = new HashMap<>();
                map.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));                                            // 系統時間
                map.put("formId", formId);                                                                                  // 表單編號
                map.put("formClass", formClassWording);                                                                             // 表單類型
                map.put("userCreated", ldapUserEntity.getName());                                                                   // 開單人員
                map.put("summary", baseFormVO.getSummary());                                                                        // 摘要
                map.put("link", hyperLink + formId);                                                                        // 表單超鏈結

                try {
                    //根據不同表單的流程套上正確的 mail template file
                    content = velocityUtil.generateContect("formRemindContent.vm", map);

                    mail.setMailSubject(mailSubject);
                    mail.setMailContent(content);
                    mail.setMailFrom(mailSender.getParamValue());
                    mail.setMailTo(new String[]{ldapUserEntity.getEmail()});
                    mail.setMailCc(null);
                    mailService.richContentSend(mail);
                    formData.setCheckSend(true);
                    formData.setLogMessage(ldapUserEntity.getEmail());
                    
                } catch (Exception e) {
                    logger.error("寄送表單程序出錯!!");
                    formData.setCheckSend(false);
                    formData.setLogMessage("寄送失敗 :"+e.getMessage());
                }
            }
            logger.info("提醒日誌填寫 Mail 寄送完成");
        }
    }
    
    /**
     * 紀錄寄送成功/失敗的紀錄新增到DB
     *
     * @author bernard.yu 2020/6/23
     * @param formDataMap
     */
    @Override
    public void recordMailLog(Map<String, FormRemindUserRecordVO> formDataMap) {
        
        if (formDataMap.size() > 0) {
            SysMailLogEntity record = new SysMailLogEntity();
            Map<String,String> saveData = new HashMap<String,String>();
            
            for (Object key : formDataMap.keySet()) {
                saveData.put(key.toString(),formDataMap.get(key).getLogMessage());
            }
           
            String sendJson = BeanUtil.toJson(saveData);
            Date today = new Date();
            record.setCreatedAt(today);
            record.setUpdatedAt(today);
            record.setCreatedBy("SCHEDULE");
            record.setUpdatedBy("SCHEDULE");
            record.setRecognize("RemindFormUserRecordJob");
            record.setAddresses(sendJson);
            mailLogRepo.save(record);
        }
    }
}
