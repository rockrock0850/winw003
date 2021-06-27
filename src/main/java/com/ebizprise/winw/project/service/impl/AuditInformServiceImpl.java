package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.doc.velocity.VelocityUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.AuditNotifyParamsEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysMailLogEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.payload.Mail;
import com.ebizprise.winw.project.repository.IAuditNotifyParamsRepository;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.ISysMailLogRepository;
import com.ebizprise.winw.project.service.IAuditInformService;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.service.IMailService;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.vo.AuditInformVO;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentFormVo;
import com.ebizprise.winw.project.vo.SysParameterVO;

/**
 * 稽核通知排程共用 服務類別
 * @author Bernard.Yu 2020/09/04, adam.yeh
 */
@Service("auditInformServiceImpl")
public class AuditInformServiceImpl extends BaseService implements IAuditInformService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditInformServiceImpl.class);
    
    @Autowired
    private IMailService mailService;
    @Autowired
    private VelocityUtil velocityUtil;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private ISysGroupRepository sysGroupRepo;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private IAuditNotifyParamsRepository auditParamsRepo;
    @Autowired
    private ISysMailLogRepository mailLogRepo;
    @Autowired
    private HolidayImportService holidayImportService;
    @Autowired
    protected ILdapUserRepository ldapUserRepo;
    
    @Override
    public List<AuditInformVO> checkList (List<AuditInformVO> dataList, List<AuditNotifyParamsEntity> params) throws Exception {
        String limitTime = "";
        Date now = new Date();
        List<AuditInformVO> checkedList = new ArrayList<AuditInformVO>();
        AuditNotifyParamsEntity expiredParam = new AuditNotifyParamsEntity();
        AuditNotifyParamsEntity expiredSoonParam = new AuditNotifyParamsEntity();
        
        for (AuditNotifyParamsEntity param : params) {
            if (StringUtils.equals(param.getNotifyType(), FormVerifyType.EXPIRED.name())) {
                expiredParam = param;
            } else if (StringUtils.equals(param.getNotifyType(), FormVerifyType.EXPIRE_SOON.name())) {
                expiredSoonParam = param;
                limitTime = param.getTime();
            }
        }
        
        for (AuditInformVO vo : dataList) {
            //審核時限
            Long timeGap = new Long(0);
            
            if(vo.getEct() == null) {
                logger.warn("表單 : " + vo.getFormId() + " 預計完成時間為空，不做判斷。");
                continue;
            }
            
            //最小單位只取時(or天)
            if (StringUtils.equals(vo.getEventPriority(), "1") || StringUtils.equals(vo.getEventPriority(), "2")) {
                timeGap = (DateUtils.fromString(DateUtils.toString(vo.getEct(), "yyyy/MM/dd HH"),"yyyy/MM/dd HH").getTime() -
                        DateUtils.fromString(DateUtils.toString(now, "yyyy/MM/dd HH"),"yyyy/MM/dd HH").getTime())/(1000*60*60);
                vo.setLimit(limitTime + "小時");
            } else {
                timeGap = (DateUtils.fromString(DateUtils.toString(vo.getEct(), "yyyy/MM/dd"),"yyyy/MM/dd").getTime() -
                        DateUtils.fromString(DateUtils.toString(now, "yyyy/MM/dd"),"yyyy/MM/dd").getTime())/(1000*60*60*24);
                vo.setLimit(limitTime + "天");
            }
            
            boolean isTimeout = DateUtils.isBefore(vo.getEct(), now);
            
            if (timeGap < 0 || isTimeout) { //已經逾期
                vo.setOverdue(true);
                vo.setAnParam(expiredParam);
            } else if (timeGap >= 0 &&
                Integer.parseInt(limitTime) >= timeGap) { //即將逾期
                vo.setOverdue(false);
                vo.setAnParam(expiredSoonParam);
            } else {
                logger.warn("表單 : " + vo.getFormId() + " 尚未到達通知的時間範圍內。");
                continue;
            }
            
            //審核進度，科長以下
            String[] divisionSplit = vo.getDivisionSolving().split(StringConstant.DASH);
            vo.setDivision(divisionSplit[1]);
            vo.setDepartmentId(divisionSplit[0]);
            
            if (divisionSplit.length <= 1) {
                logger.warn("表單 : " + vo.getFormId() + " 缺少處理科別資訊。");
                continue;
            }
            
            //針對問題單,審核階段不寄信
            if (StringUtils.equals(vo.getFormClass(), FormEnum.Q.name()) &&
                    StringUtils.equals(vo.getVerifyType(), FormEnum.REVIEW.name())) {
                logger.warn("表單 : " + vo.getFormId() + " 問題單於審核階段，不寄信通知。");
                continue;
            }
            
            //審核進度為申請中，免判斷直接寄信
            if (StringUtils.equals(vo.getVerifyType(), FormEnum.APPLY.name())) {
                checkedList.add(vo);
                continue;
            }
            
            String verifyLevel = vo.getVerifyLevel();
            SysGroupEntity sysGroup = sysGroupRepo.findByDivisionAndGroupNameLike(vo.getDepartmentId(), vo.getDivision(), "[_]" + UserEnum.DIVISION_CHIEF.wording());
            IFormProcessManagmentService<BaseFormProcessManagmentFormVo> processService = formHelper.getFormProcessDetailService(FormEnum.valueOf(vo.getFormClass()));
            int scLevel = processService.getFormProcessOrder(vo.getDetailId(), sysGroup.getGroupId(), FormEnum.REVIEW.name());
            
            if (StringUtils.isNumeric(verifyLevel) &&
                    Integer.valueOf(verifyLevel) <= scLevel) {
                checkedList.add(vo);
            } else {
                logger.warn("表單 : " + vo.getFormId() + " 關卡 : " + vo.getVerifyLevel() + ", 超過送信通知範疇遂忽略之。");
            }
        }
        
        //遇假日逾期不寄信
        List<AuditInformVO> expireSoonList = new ArrayList<AuditInformVO>();
        boolean isHoliday = holidayImportService.isHolidayByDate(new Date());
        if (isHoliday) {
            for (AuditInformVO vo : checkedList) {
                //遇假日即將逾期僅通知「處理人員」
                if (StringUtils.equals(vo.getAnParam().getNotifyType(), FormVerifyType.EXPIRE_SOON.name())) {
                    vo.setDivision("");
                    vo.setDepartmentId("");
                    vo.getAnParam().setNotifyMails("");
                    expireSoonList.add(vo);
                }
            }
            checkedList = expireSoonList;
        }
        
        return checkedList;
    }
    
    @Override
    public String sendMail (List<AuditInformVO> checkedList, String template, String jobName) throws Exception {
        if (CollectionUtils.isEmpty(checkedList)) {
            logger.info("沒有需要稽催的表單");
            return "沒有需要稽催的表單";
        }

        Mail mail = new Mail();
        String subject, formClass, notifyType;
        Map<String, Object> mailParams = new HashMap<>();
        List<String> sendMailReports = new ArrayList<String>();
        List<String> sendMails = new ArrayList<String>();
        SysParameterVO mailFrom = systemConfigService.getMail();
        String hyperLink = String.format(
                "https://%s/ISWP/formSearch/search/", env.getProperty("mail.ap.domain"));

        mailService.setInitData();
        
        for (AuditInformVO vo : checkedList) {
            try {
                formClass = getMessage(FormEnum.valueOf(vo.getFormClass()).wording());
                
                //Template 檔案所需要的變數名稱
                mailParams.clear();
                mailParams.put("pic", StringUtils.isNotEmpty(vo.getSolvingName()) ? vo.getSolvingName() : vo.getCreateName());
                mailParams.put("limit", vo.getLimit());
                mailParams.put("formClass", formClass);
                mailParams.put("formId", vo.getFormId());
                mailParams.put("summary", vo.getSummary());
                mailParams.put("content", vo.getContent());
                mailParams.put("formIdTitle", vo.getFormId());
                mailParams.put("priority", vo.getEventPriority());
                mailParams.put("url", hyperLink + vo.getFormId());
                mailParams.put("ect", DateUtils.toString(vo.getEct(), DateUtils.pattern3));
                mailParams.put("creatTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern3));
                
                //稽催通知內文格式:已逾期、即將逾期
                if (FormVerifyType.EXPIRED.name().equals(vo.getAnParam().getNotifyType())) {
                    mailParams.put("limit", StringUtils.EMPTY);
                    mailParams.put("notifyType", FormVerifyType.EXPIRED.desc());
                    notifyType = FormVerifyType.EXPIRED.desc();
                } else {
                    mailParams.put("limit", "(將在" + vo.getLimit() + "前開始通知)");
                    mailParams.put("notifyType", FormVerifyType.EXPIRE_SOON.desc());
                    notifyType = FormVerifyType.EXPIRE_SOON.desc();
                }
                
                if (StringUtils.isEmpty(vo.getEventPriority())) {
                    if (vo.isOverdue()) {
                        //已逾期格式
                        subject = getMessage("schedule.audit.mail.subject.expired",
                                new String[] { formClass, vo.getFormId(), notifyType, StringUtils.EMPTY });
                    } else {
                        //即將逾期格式
                        subject = getMessage("schedule.audit.mail.subject.expired.soon",
                                new String[] {formClass, vo.getFormId(), notifyType, vo.getLimit()});
                    }
                } else { //事件單
                    if (vo.isOverdue()) {
                        subject = getMessage("schedule.audit.mail.subject.inc.expired",
                                new String[] {formClass, vo.getFormId(), vo.getEventPriority(), notifyType, StringUtils.EMPTY});
                    } else {
                        subject = getMessage("schedule.audit.mail.subject.inc.expired.soon",
                                new String[] {formClass, vo.getFormId(), vo.getEventPriority(), notifyType, vo.getLimit()});
                    }
                }
                
                List<String> mailList = getMailGroup(vo);
                String[] mailArray =  new String[mailList.size()];
                mail.setMailTo(mailList.toArray(mailArray));
                mail.setMailFrom(mailFrom.getParamValue());
                mail.setMailSubject(subject);
                mail.setMailContent(velocityUtil.generateContect(template, mailParams));
                mailService.richContentSend(mail);
                
                sendMailReports.add(vo.getFormId());
                sendMails.addAll(mailList);
                
                logger.info("表單編號 : " + vo.getFormId() + " 已進入寄信清單。");
            } catch (Exception e) {
                logger.error("表單編號 : " + vo.getFormId() + " 寄信發生錯誤。", e);
            }
        }
        
        saveMailLog(sendMails, jobName);
        
        return BeanUtil.toJson(sendMailReports);
    }
    
    @Override
    public List<AuditNotifyParamsEntity> getANParams(String formType) throws Exception {
        return BeanUtil.copyList(auditParamsRepo.findByFormType(formType), AuditNotifyParamsEntity.class);
    }
    
    /**
     * 取寄信的對象群組人員的mail
     *
     * @param vo
     * @return mailList
     * @throws Exception
     */
    private List<String> getMailGroup(AuditInformVO vo) throws Exception {
        //取所有相關人員的mail
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_EMAIL_FOR_AUDIT_JOB");
        Map<String, Object> params = new HashMap<>();
        params.put("division", vo.getDivision());
        params.put("sc", vo.getDivision() + "SC");
        params.put("departmentId", vo.getDepartmentId());
        params.put("pic",
                StringUtils.isNotEmpty(vo.getUserSolving()) ? vo.getUserSolving() : vo.getUserCreated());
        Map<String, Object> mailMap = jdbcRepository.queryForMap(resource, params);

        String[] mails;
        String[] paramMail = StringUtils.split(vo.getAnParam().getNotifyMails(), ";"); //負責人
        //Array轉List後, 需要重建一個新的List才能新增刪除。
        List<String> mailTemp = (paramMail.length != 0) ? Arrays.asList(paramMail) : new ArrayList<String>();
        List<String> mailList = new ArrayList<String>(mailTemp);
        
        if (StringUtils.equals(vo.getAnParam().getIsPic(), StringConstant.SHORT_YES)) {
            mails = StringUtils.split(MapUtils.getString(mailMap, "picMails", ""), ",");
            mailList.addAll(Arrays.asList(mails));
        }

        if (StringUtils.equals(vo.getAnParam().getIsVsc(), StringConstant.SHORT_YES)) {
            mails = StringUtils.split(MapUtils.getString(mailMap, "vscMails", ""), ",");
            mailList.addAll(Arrays.asList(mails));
        }

        if (StringUtils.equals(vo.getAnParam().getIsSc(), StringConstant.SHORT_YES)) {
            mails = StringUtils.split(MapUtils.getString(mailMap, "scMails", ""), ",");
            mailList.addAll(Arrays.asList(mails));
        }

        if (StringUtils.equals(vo.getAnParam().getIsD1(), StringConstant.SHORT_YES)) {
            mails = StringUtils.split(MapUtils.getString(mailMap, "dc1Mails", ""), ",");
            mailList.addAll(Arrays.asList(mails));
        }

        if (StringUtils.equals(vo.getAnParam().getIsD2(), StringConstant.SHORT_YES)) {
            mails = StringUtils.split(MapUtils.getString(mailMap, "dc2Mails", ""), ",");
            mailList.addAll(Arrays.asList(mails));
        }
        
        //即將逾期:僅寄給「處理人員」
        if (FormVerifyType.EXPIRE_SOON.name().equals(vo.getAnParam().getNotifyType())) {
            mailList.clear();
            //取得表頭的「處理人員」資訊
            LdapUserEntity handler = ldapUserRepo.findByUserIdAndIsEnabled(vo.getUserSolving(), StringConstant.SHORT_YES);
            mailList.add(handler.getEmail());
        }

        return mailList;
    } 
    
    /**
     * 系統信件發送紀錄(SYS_MAIL_LOG)
     * @param dataList
     * @param jobName
     * @throws Exception
     */
    private void saveMailLog (List<String> mailList, String jobName) throws Exception {
        if (mailList.size() > 0) {
            Date today = new Date();
            String sentJson = BeanUtil.toJson(mailList);
            
            SysMailLogEntity record = new SysMailLogEntity();
            record.setCreatedAt(today);
            record.setUpdatedAt(today);
            record.setCreatedBy("SCHEDULE");
            record.setUpdatedBy("SCHEDULE");
            record.setRecognize(jobName);
            record.setAddresses(sentJson);
            mailLogRepo.save(record);
        }
    }
    
}
