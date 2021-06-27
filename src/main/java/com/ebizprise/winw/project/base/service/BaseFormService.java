package com.ebizprise.winw.project.base.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.doc.velocity.VelocityUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.DashboardDirectAuthEntity;
import com.ebizprise.winw.project.entity.FormContentModifyLogEntity;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.entity.FormFileLogEntity;
import com.ebizprise.winw.project.entity.FormInfoCDetailEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInternalProcessStatusEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobInfoSysDetailEntity;
import com.ebizprise.winw.project.entity.FormUserRecordEntity;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysMailLogEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.enums.MailTemplate;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.payload.Mail;
import com.ebizprise.winw.project.repository.IDashBoardDirectAuthRepository;
import com.ebizprise.winw.project.repository.IFormContentModifyLogRepository;
import com.ebizprise.winw.project.repository.IFormFileLogRepository;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.repository.IFormInfoCDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoChgDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInternalProcessStatusRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoSysDetailRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormUserRecordRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.ISysMailLogRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.ICommonJobFormService;
import com.ebizprise.winw.project.service.IMailService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.thread.AsynchronousService;
import com.ebizprise.winw.project.thread.ProcessMail;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.CountersignedFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobApplyVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單資料服務 基礎類別
 *
 * @author gary.tsai 2019/6/25, adam.yeh
 */
public abstract class BaseFormService<formVO extends BaseFormVO> extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseFormService.class);

    @Autowired
    private IFormRepository formRepo;

    @Autowired
    private IFormFileRepository formFileRepository;
    
    @Autowired
    private IFormFileLogRepository formFileLogRepository;

    @Autowired
    private IFormUserRecordRepository formUserRecordRepository;

    @Autowired
    private IFormVerifyLogRepository verifyLogRepository;

    @Autowired
    private IFormInfoDateRepository formDateRepo;

    @Autowired
    private IFormJobInfoDateRepository formJobDateRepo;

    @Autowired
    private IFormJobInfoSysDetailRepository formSysDetailRepo;
    
    @Autowired
    private IFormInfoCDetailRepository formDetailRepo;
    
    @Autowired
    private IMailService mailService;

    @Autowired
    private VelocityUtil velocityUtil;

    @Autowired
    protected ISysGroupRepository sysGroupRepository;

    @Autowired
    protected ILdapUserRepository ldapUserRepository;

    @Autowired
    private ISysParameterRepository sysParameterRepository;

    @Autowired
    private IFormVerifyLogRepository verifyRepo;

    @Autowired
    private ISysMailLogRepository mailLogRepo;

    @Autowired
    private IFormInfoCDetailRepository formInfoCDetailRepository;

    @Autowired
    private FormHelper formHelper;

    @Autowired
    private ICommonJobFormService commonJobFormService;
    
    @Autowired
    private IDashBoardDirectAuthRepository directRepo;
    
    @Autowired
    private SysUserServiceImpl sysUserService;
    
    @Autowired
    private ICommonFormService commonFormService;
    
    @Autowired
    private AsynchronousService asyc;

    @Autowired
    private IFormInfoChgDetailRepository formChgDetailRepo;
    
    @Autowired
    private IFormInternalProcessStatusRepository internalProcessRepo;
    
    @Autowired
    private IFormContentModifyLogRepository formContentModifyLogRepository;
    
    /**
     * 取得各表單詳細資訊
     * @param formId
     * @return
     */
    public abstract formVO getFormDetailInfo(String formId);

    /**
     * 取得申請流程中的表單群組資訊
     * @param vo
     * @return
     */
    protected abstract String getFormApplyGroupInfo(formVO vo);

    /**
     * 取得申請流程中的表單群組資訊
     * @param detailId
     * @param processOrder
     * @return
     */
    protected abstract String getFormApplyGroupInfo(String detailId, String processOrder);

    /**
     * 取得審核流程中的表單群組資訊
     * @param vo
     * @return
     */
    protected abstract String getFormReviewGroupInfo(formVO vo);

    /**
     * 取得審核流程中的表單群組資訊
     * @param detailId
     * @param processOrder
     * @return
     */
    protected abstract String getFormReviewGroupInfo(String detailId, String processOrder);

    /**
     * 檢查是否為申請流程最後一關(申請流程最後一關可檢查衝擊分析是否可直接跳至最後一關)
     *
     * @param vo
     * @return String
     */
    protected abstract String isApplyLastLevel (String detailId, String verifyType, String verifyLevel);

    /**
     * 檢查是否為申請流程最後一關(申請流程最後一關可檢查衝擊分析是否可直接跳至最後一關)
     *
     * @param vo
     * @return String
     */
    protected abstract String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel);
    
    /**
     * 取得各種表單的審核流程最後一關的關數
     * @return
     * @author adam.yeh
     */
    protected abstract String getReviewLastLevel (String detailId);

    /**
     * 母單「實際完成日期」符合下列任一條件，直接送單；反之，顯示系統訊息 (仍可送單)<br>
     * 1. 母單「實際完成日期」 >= AP工作單「連線系統完成日期」(且已結案)<br>
     * 2. 母單「實際完成日期」 >= SP工作單「實際完成日期」 (且已結案)<br>
     * 3. 若子單(變更單)無衍生工作單，則直接送單 (待合庫確認)<br>
     * ※提示訊息：本單的實際完成日期，小於AP工作單「連線系統完成日期」，或SP工作單「實際完成日期」
     * 
     * @param vo
     * @return
     */
    public boolean isAlertJobSCTWarning (formVO vo) {
        boolean isAlert = true;
        String formId = vo.getFormId();
        List<BaseFormVO> childList = getChildFormList(formId);

        List<String> apFormIds = childList.stream()
                                          .filter(it -> it.getFormClass().equals(FormEnum.JOB_AP.name()) &&
                                                  FormEnum.CLOSED.name().equals(it.getFormStatus()))
                                          .map(it -> it.getFormId())
                                          .collect(Collectors.toList());
        List<String> spFormIds = childList.stream()
                                          .filter(it -> it.getFormClass().equals(FormEnum.JOB_SP.name()) &&
                                                  FormEnum.CLOSED.name().equals(it.getFormStatus()))
                                          .map(it -> it.getFormId())
                                          .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(apFormIds) && CollectionUtils.isEmpty(spFormIds)) {
            isAlert = false;
        } else if (CollectionUtils.isNotEmpty(apFormIds)) {
            isAlert = calJobSCTWarning(apFormIds, vo, FormEnum.JOB_AP.name());
        } else if (CollectionUtils.isNotEmpty(spFormIds)) {
            isAlert = calJobSCTWarning(spFormIds, vo, FormEnum.JOB_SP.name());
        }

        return isAlert;
    }

    /**
     * 超級副科修改作業館卡人員，平行會辦人員，處理人員後更新對應的審核處理人員
     * @param vo
     * @author jacky.fu
     */
   public void updateVerifyLog (formVO vo) {
       //更新處理人員(限定經辦)
       if(StringUtils.isNoneBlank(vo.getUserSolving())) {
           ResourceEnum resource2 = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_USERSOLVING");
           Map<String, Object> params2 = new HashMap<String, Object>();
           params2.put("formId", vo.getFormId());
           params2.put("userSolving", vo.getUserSolving());
           jdbcRepository.update(resource2, params2);
       }
       
       //更新BY作業關卡人員
       ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_CHECKPERSON");
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("formId", vo.getFormId());
       jdbcRepository.update(resource, params);
       
       //更新BY平行會辦關卡人員
       FormJobInfoSysDetailEntity detail = formSysDetailRepo.findByFormId(vo.getFormId());
       if(detail != null) {
           List<HtmlVO> spcGroups = BeanUtil.fromJsonToList(detail.getSpcGroups(), HtmlVO.class);
           if(CollectionUtils.isNotEmpty(spcGroups)) {
               for (HtmlVO spc : spcGroups) {
                   if(StringUtils.isNoneBlank(spc.getUserId())) {
                       ResourceEnum resource3 = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_PARALLELC");
                       Map<String, Object> params3 = new HashMap<String, Object>();
                       params3.put("userId", spc.getUserId());
                       params3.put("prallel", spc.getValue());
                       params3.put("formId", vo.getFormId());
                       jdbcRepository.update(resource3, params3);
                   }
               }
           }
       }
       
       FormInfoCDetailEntity detail2 = formDetailRepo.findByFormId(vo.getFormId());
       if(detail2 != null) {
           List<HtmlVO> spcGroups2 = BeanUtil.fromJsonToList(detail2.getSpcGroups(), HtmlVO.class);
           if(CollectionUtils.isNotEmpty(spcGroups2)) {
               for (HtmlVO spc : spcGroups2) {
                   ResourceEnum resource3 = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_PARALLELC");
                   Map<String, Object> params3 = new HashMap<String, Object>();
                   params3.put("userId", spc.getUserId());
                   params3.put("prallel", spc.getValue());
                   params3.put("formId", vo.getFormId());
                   jdbcRepository.update(resource3, params3);
               }
           }
       }
   }
    
    /**
     * 申請最後一關自動帶入最後一張工作單「實際完成日」，並與表單「實際完成日」比較日期，取最新日期 。
     * @param formId
     * @author jacky.fu, adam.yeh
     */
    protected void setLastJobAct (formVO vo) {
        String formId = vo.getFormId();
        
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_CHILD_FORM_JOIN_JOBDATE_LIST");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formId", formId);
        List<BaseFormVO> allList = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        
        if (CollectionUtils.isEmpty(allList)) {
            return;
        }
        
        List<Date> resList = new ArrayList<Date>();
        
        List<Date> dateActList = allList.stream()
                .filter(it -> FormEnum.JOB_AP.name().equals(it.getFormClass()) || FormEnum.JOB_SP.name().equals(it.getFormClass()))
                .filter(it -> Objects.nonNull(it.getAct()))
                .map(u -> u.getAct())
                .collect(Collectors.toList());

        List<Date> dateSctList = allList.stream()
                .filter(it -> FormEnum.JOB_AP.name().equals(it.getFormClass()) || FormEnum.JOB_SP.name().equals(it.getFormClass()))
                .filter(it -> Objects.nonNull(it.getSct()))
                .map(u -> u.getSct())
                .collect(Collectors.toList());
        
        resList.addAll(dateActList);
        resList.addAll(dateSctList);

        Date lastAct = resList.stream().max(Comparator.nullsLast(Comparator.naturalOrder())).orElse(null);
        
        if (lastAct != null &&
                StringConstant.SHORT_YES.equals(vo.getIsApplyLastLevel()) &&
                (vo.getAct() == null || DateUtils.isBefore(vo.getAct(), lastAct))) {
            vo.setAct(lastAct);
        }
    }
    
    /**
     * 表單變更成功、失敗的標註機制
     * @param form
     * @author adam.yeh
     */
    protected void saveFormAlterResult (String formId, boolean isC) {
        FormEntity form = formRepo.findByFormId(formId);
        FormInfoDateEntity formDate = formDateRepo.findByFormId(formId);
        
        // 副科或系統管理者會在尚未填寫實際完成時間的時候修改表單內容, 但同時又需要享有變更成功失敗的邏輯
        // 所以加上判斷formDate.getAct() != null才做事, 否則讓IsAlterDone欄位為空值(不進行統計)
        if (hasChangeForm(formId) && formDate.getAct() != null) {
            Date ect = null;
            boolean isAlterDone = false;
            
            if (isC) {
                ect = formDate.getMect() == null ?
                        formDate.getCreateTime() : formDate.getMect();
            } else {
                ect = formDate.getEct();
            }
            
            if (ect != null) {
                isAlterDone = formDate.getAct().compareTo(ect) < 1;
            }
            
            form.setIsAlterDone(isAlterDone ? StringConstant.SHORT_YES : StringConstant.SHORT_NO);
        } else {
            form.setIsAlterDone(null);
        }
        
        formRepo.save(form);
    }

    /**
     * 判斷延伸單裡面有沒有開過變更單
     * @param formId
     * @return
     * @author adam.yeh
     */
    protected boolean hasChangeForm (String formId) {
        boolean hasChangeForm = false;
        List<BaseFormVO> childs = getChildFormList(formId);
        
        for (BaseFormVO child : childs) {
            hasChangeForm = StringUtils.contains(child.getFormId(), FormEnum.CHG.name());
            if (hasChangeForm) break;
        }
        
        return hasChangeForm;
    }
    
    /**
     * 表單一般化共用寄信函式
     * @param subject 主旨
     * @param mailList 收件者清單
     * @param params :<br>
     * -ccList 副本清單<br>
     * -formName 表單名稱<br>
     * -template 模板名稱<br>
     * -verifyType 表單審核狀態<br>
     * -信件模板需要用到的變數
     * @author adam.yeh
     */
    @SuppressWarnings("unchecked")
    protected void sendMail (String subject, List<String> mailList, Map<String, Object> params) {
        String mailFrom = "";
        List<String> ccTemp = new ArrayList<>();
        String formName = MapUtils.getString(params, "formName", "");
        String verifyType = MapUtils.getString(params, "verifyType", "");
        String content = velocityUtil.generateContect(MapUtils.getString(params, "template", ""), params);
        List<String> ccList = (List<String>) MapUtils.getObject(params, "ccList", new ArrayList<String>());
        SysParameterEntity props = sysParameterRepository.findByParamKey(SysParametersEnum.MAIL_SERVER_EMAIL.name);
        
        ccTemp.addAll(ccList);
        String[] cc = new String[ccTemp.size()];
        String[] mailTo = new String[mailList.size()];
        
        if (props != null) {
            mailFrom = props.getParamValue();
        } else {
            mailFrom = env.getProperty("mail.sender");
        }
        
        try {
            mailService.setInitData();
            Mail mail = new Mail();
            mail.setMailCc(cc);
            mail.setMailFrom(mailFrom);
            mail.setMailSubject(subject);
            mail.setMailContent(content);
            mail.setMailTo(mailList.toArray(mailTo));
            mailService.richContentSend(mail);
            logger.info(getMessage("form.mail.send.start.log", new String[]{formName, verifyType}));
        } catch (Exception e) {
            logger.error("發送郵件發生錯誤。", e);
        }
    }
    
    /**
     * 非同步發送關卡流程通知信
     * @param vo
     * @author adam.yeh
     */
    protected synchronized void asyncMailLauncher (formVO vo) {
        SysUserVO loginUser = fetchLoginUser();
        String formURL = getFormUrl(vo.getFormId());
        
        ProcessMail mail = new ProcessMail() {

            @Override
            public Map<String, Object> getParams () {
                Map<String, Object> params = new HashMap<>();
                params.put("formURL", formURL);
                params.put("loginUser", loginUser);
                
                return params;
            }
            
            @Override
            public void run () {
                try {
                    sendFormProcessMail(vo, getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        };

        asyc.execProcessMail(mail);
    }
    
    /**
     * 需要跳到審核流程哪一關<br>
     * P.S. 不指定jumpLevel=直接跳審核最後一關
     * @param vo
     * @author adam.yeh
     */
    protected void jumpToReview (formVO vo) {
        vo.setVerifyResult(FormEnum.AGREED.name());
        updateCurrentLevel(vo);
        vo.setVerifyType(FormEnum.REVIEW.name());
        String reviewLevel = vo.getJumpLevel();
        
        if (StringUtils.isBlank(reviewLevel)) {
            reviewLevel = getReviewLastLevel(vo.getDetailId());
        }

        vo.setVerifyLevel(reviewLevel);
        vo.setGroupId(MapUtils.getString(getProcessLevel(
                vo, FormEnum.valueOf(vo.getFormClass())), "GroupId"));
        saveLevel(vo, reviewLevel, FormEnum.REVIEW, FormEnum.PENDING);
    }
    
    /**
     * 檢查延伸單是否全部已作廢或結案
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isAllStretchDied (formVO vo) {
        int count = 0;
        String formId = vo.getFormId();
        String status = vo.getFormStatus();
        
        if (FormEnum.APPROVING.name().equals(status)) {
            ResourceEnum resource = ResourceEnum.
                    SQL_FORM_OPERATION.getResource("COUNT_CHILD_FORM");
            Map<String, Object> params = new HashMap<>();
            params.put("formId", formId);
            Map<String, Object> dataMap = jdbcRepository.queryForMap(resource, params);
            count = MapUtils.getIntValue(dataMap, "COUNT");
        }
        
        return count == 0;
    }

    /**
     * 取得當前關卡資訊
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected BaseFormVO getCurrentLevelInfo (formVO vo) {
        String clazz = vo.getFormClass();
        String verifyType = vo.getVerifyType();
        BaseFormVO currentLevel = new BaseFormVO();
        
        if (StringUtils.isNoneBlank(clazz) &&
                StringUtils.isNoneBlank(verifyType)) {
            FormEnum clazzEnum = FormEnum.valueOf(clazz);
            FormEnum typeEnum = FormEnum.valueOf(verifyType);
            
            ResourceEnum resource = formHelper.getLevelInfoResource(clazzEnum, typeEnum);
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            params.put("verifyLevel", vo.getVerifyLevel());
            
            currentLevel = jdbcRepository.queryForBean(resource, params, BaseFormVO.class);
        }

        return currentLevel;
    }
    
    /**
     * 如果是平行會辦期間執行退關, 將會作廢該期間所開的延伸單
     * @param vo
     * @author adam.yeh
     * @param isParallel
     */
    protected void deprecatedParallelChildFroms (formVO vo, boolean isParallel) {
        if (isParallel) {
            ResourceEnum resource = ResourceEnum.
                    SQL_FORM_OPERATION_JOB.getResource("FIND_PARALLEL_CHILD_FORM_LIST");
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            List<BaseFormVO> childList =
                    jdbcRepository.queryForList(resource, params, BaseFormVO.class);
            
            for (BaseFormVO child : childList) {
                FormEntity form = formRepo.findByFormId(child.getFormId());
                form.setFormStatus(FormEnum.DEPRECATED.name());
                formRepo.save(form);
            }
        }
    }

    /**
     * 是否具有平行會辦審核資格
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isParallelApprover (
            formVO vo, FormVerifyLogEntity entity, SysUserVO userInfo) {
        boolean result = true;
        String parallel = entity.getParallel();
        String subGroup = userInfo.getSubGroup();
        
        if (StringUtils.isNotBlank(parallel)) {// 是否還在平行會辦關卡
            result = StringUtils.equals(parallel, subGroup);
        }
        
        return result;
    }

    /**
     * 取得當前關卡的平行會辦群組
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected String fetchParallel (formVO vo) {
        String parallel = "";
        String formId = vo.getFormId();
        SysUserVO loginUser = fetchLoginUser();
        String verifyLevel = vo.getVerifyLevel();
        String subGroup = loginUser.getSubGroup();
        boolean isParallel = StringConstant.SHORT_YES.equals(vo.getIsParallel());
        
        if (isParallel && StringUtils.isNotBlank(subGroup)) {
            FormVerifyLogEntity vLog = verifyRepo.
                    findTop1ByFormIdAndVerifyLevelAndParallelAndCompleteTimeIsNull(formId, verifyLevel, subGroup);
            parallel= vLog == null ? "" : vLog.getParallel();
        }
        
        return parallel;
    }

    /**
     * 是否在平行會辦期間
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected String isParalleling (formVO vo) {
        String formId = vo.getFormId();
        String isParallel = vo.getIsParallel();
        String verifyLevel = vo.getVerifyLevel();
        int count = verifyRepo.countByFormIdAndVerifyLevelAndCompleteTimeIsNull(formId, verifyLevel);

        if (StringConstant.SHORT_YES.equals(isParallel)) {
            isParallel = count == 1 ?
                    StringConstant.SHORT_NO : StringConstant.SHORT_YES;
        }
        
        return isParallel;
    }

    /**
     * 逆向遞迴找來源表單清單
     * @param formId
     * @return
     * @author adam.yeh
     */
    protected List<BaseFormVO> getSourceFormList (String formId) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_SOURCE_FORM_LIST");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formId", formId);

        return jdbcRepository.queryForList(resource, params, BaseFormVO.class);
    }

    /**
     * 正向遞迴找子表單清單
     * @param formId
     * @return
     * @author adam.yeh
     */
    protected List<BaseFormVO> getChildFormList (String formId) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_CHILD_FORM_LIST");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formId", formId);

        return jdbcRepository.queryForList(resource, params, BaseFormVO.class);
    }

    /**
     * 1. 判斷是否需要等待延伸單全部結案</br>
     * 2. 判斷延伸單是否已全部結案
     * @param vo
     * @return
     */
    protected boolean isStretchFormClosed (formVO vo) {
        String isWait = "N";
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());

        // 是否需要等待延伸單完成
        ResourceEnum resource = formHelper.getLevelInfoResource(formClass, verifyType);
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());

        isWait = jdbcRepository.queryForBean(resource, params, BaseFormVO.class).getIsWaitForSubIssueFinish();

        if (StringConstant.SHORT_NO.equals(isWait)) {
            return false;
        }

        int stretchs = getStretchs(vo.getFormId());

        return !(stretchs == 0);
    }
    
    protected boolean isStretchFormClosed (formVO vo,Boolean isINC) {
        String isWait = "N";
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());

        // 是否需要等待延伸單完成
        ResourceEnum resource = formHelper.getLevelInfoResource(formClass, verifyType);
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());

        isWait = jdbcRepository.queryForBean(resource, params, BaseFormVO.class).getIsWaitForSubIssueFinish();

        if (StringConstant.SHORT_NO.equals(isWait)) {
            return false;
        }

        List<String> formStatusNoInList = new ArrayList<>();
        formStatusNoInList.add(FormEnum.CLOSED.name());
        formStatusNoInList.add(FormEnum.DEPRECATED.name());

        List<String> formClassNoInList = new ArrayList<>();
        formClassNoInList.add(FormEnum.Q.toString());
        
        int stretchs = formRepo.countBySourceIdAndFormStatusNotInAndFormClassNotIn(vo.getFormId(),
                formStatusNoInList, formClassNoInList);

        return !(stretchs == 0);
    }

    /**
     * 取得延伸單數量
     * @param vo
     * @return
     */
    protected int getStretchs (String formId) {
        List<String> formStatusNoInList = new ArrayList<>();
        formStatusNoInList.add(FormEnum.CLOSED.name());
        formStatusNoInList.add(FormEnum.DEPRECATED.name());

        return formRepo.countBySourceIdAndFormStatusNotIn(formId, formStatusNoInList);
    }
    
    /**
     * 母單於申請流程最後一關_經辦關卡送出時，系統檢核。<br>
     * 母單衍伸之變更單，取最大的預計變更結束時間，不可大於母單的預計完成時間<br><br>
     * 母單範圍:需求單、問題單、事件單、需求會辦單、問題會辦單、事件會辦單
     * 
     * @param vo
     * @return
     */
    public boolean isAlertCCTWarning (formVO vo) {
        boolean isAlert = false;
        Date ect = vo.getEct();
        FormInfoDateEntity formDate = formDateRepo.findByFormId(vo.getFormId());
        Date mect = formDate.getMect();
        
        List<BaseFormVO> dataLs = commonFormService.getFormRelationship(vo.getFormId());
        
        //母單衍伸之變更單，取最大的預計變更結束時間
        List<Date> resList = new ArrayList<Date>();
        List<Date> dateCctList = dataLs.stream()
                .filter(it -> FormEnum.CHG.name().equals(it.getFormClass()))
                .filter(it -> Objects.nonNull(it.getCct()))
                .map(u -> u.getCct())
                .collect(Collectors.toList());
        resList.addAll(dateCctList);
        
        Date maxCct = resList.stream().max(Comparator.nullsLast(Comparator.naturalOrder())).orElse(null);
        
        if (!dateCctList.isEmpty()) {
            if (vo.getFormClass().equals(FormEnum.SR.name()) || vo.getFormClass().equals(FormEnum.Q.name()) ||
                    vo.getFormClass().equals(FormEnum.INC.name())) {
                if (maxCct.after(ect)) {
                    isAlert = true;
                }
            } else if (vo.getFormClass().equals(FormEnum.SR_C.name()) || vo.getFormClass().equals(FormEnum.Q_C.name()) ||
                    vo.getFormClass().equals(FormEnum.INC_C.name())) {
                if (maxCct.after(mect)) {
                    isAlert = true;
                }
            }
        }
        
        return isAlert;
    }

    /**
     * 用於共用表單中的附件頁簽 新增附件時使用
     *
     * @param formId
     * @param desc
     * @param file
     * @throws IOException
     */
    protected void saveFormFile(
            String type,
            String formId,
            String description,
            String alterContent,
            String layoutDataset,
            MultipartFile file) throws IOException {
        FormFileEntity entity = new FormFileEntity();
        entity.setType(type);
        entity.setFormId(formId);
        entity.setData(file.getBytes());
        entity.setDescription(description);
        entity.setAlterContent(alterContent);
        entity.setLayoutDataset(layoutDataset);
        entity.setName(file.getOriginalFilename());
        entity.setCreatedAt(new Date());
        entity.setCreatedBy(UserInfoUtil.loginUserId());
        entity.setIslocked(StringConstant.SHORT_NO);
        formFileRepository.save(entity);
        
        FormFileLogEntity log = new FormFileLogEntity();
        log.setActionType(FormEnum.ADD.toString());
        log.setFormId(formId);
        log.setUpdatedAt(new Date());
        log.setUpdatedBy(UserInfoUtil.loginUserId());
        log.setCreatedAt(new Date());
        log.setCreatedBy(UserInfoUtil.loginUserId());
        formFileLogRepository.save(log);
        
    }

    /**
     * 取得最近的流程編號
     *
     * @param division
     * @param e
     * @return
     * @author adam.yeh
     */
    protected String getRecentlyDetail (String division, FormEnum e) {
        String[] divisions = division.split("-");
        ResourceEnum resource = formHelper.getNewerProcessResource(e);

        Map<String, Object> params = new HashMap<>();
        params.put("formType", e.formType());
        params.put("isEnable", StringConstant.SHORT_YES);
        params.put("departmentId", divisions[0]);
        params.put("division", divisions[1]);
        params = jdbcRepository.queryForMap(resource, params);
        params = BeanUtil.fromJson(BeanUtil.toJson(params));// 將欄位名稱UPPER CAMAL CASE轉LOWER CAMEL CASE

        return BeanUtil.toJson(params);
    }

    /**
     * 用於共用表單中的附件頁簽 根據id和formId刪除資料
     *
     * @param id
     * @param formId
     */
    protected void delFormFile(Long id, String formId) {
        logger.debug(String.format("表單編號:%d, 檔案名稱:%s, 刪除!!", id, formId));
        formFileRepository.deleteByIdAndFormId(id, formId);
        
        FormFileLogEntity log = new FormFileLogEntity();
        log.setActionType(FormEnum.DELETE.toString());
        log.setFormId(formId);
        log.setUpdatedAt(new Date());
        log.setUpdatedBy(UserInfoUtil.loginUserId());
        log.setCreatedAt(new Date());
        log.setCreatedBy(UserInfoUtil.loginUserId());
        formFileLogRepository.save(log);
        
    }

    /**
     * 更改表單檔案狀態
     *
     * @param islocked Y OR N
     * @param formId
     */
    protected void updateFormFileStatus(String islocked,String formId) {
        formFileRepository.updateFormFileStatusByFormId(islocked, formId);
    }

    /**
     * 用於共用表單中的附件頁簽 點選頁面檔案連結下載特定檔案 會自動備份在"C:/Temp/download/當天日期"
     *
     * @param id
     * @param formId
     * @return
     * @throws IOException
     */
    protected File downloadFile(Long id, String formId) throws IOException {
        logger.debug(String.format("表單編號:%d, 檔案名稱:%s, 下載!!", id, formId));

        FormFileEntity formFileEntity = formFileRepository.findByIdAndFormId(id, formId);
        File file = new File(genFilePath(formFileEntity.getName()));

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        file = FileUtil.getFileFromByte(formFileEntity.getData(), file.getAbsolutePath());

        return file;
    }

    /**
     * 刪除日誌紀錄
     * @param formUserRecordEntity
     */
    protected void delFormUserRecord(FormUserRecordEntity formUserRecordEntity){
        if (formUserRecordEntity != null) {
            formUserRecordRepository.deleteById(formUserRecordEntity.getId());
        }
    }

    /**
     * 儲存和更新日誌紀錄
     * @param tempUser
     */
    protected void saveOrUpdateFormUserRecord(FormUserRecordEntity tempUser) {
        if (Objects.nonNull(tempUser)) {
            if (null == tempUser.getId()) {
                saveNewFormUserRecord(tempUser);
            } else {
                FormUserRecordEntity existUser = formUserRecordRepository.findById(tempUser.getId()).orElse(new FormUserRecordEntity());
                existUser.setUpdatedAt(new Date());
                existUser.setFormId(tempUser.getFormId());
                existUser.setSummary(tempUser.getSummary());
                existUser.setUpdatedBy(tempUser.getUpdatedBy());
            }
        }
    }

    /**
     * 根據登入的員工編號，新增一筆簽核紀錄
     * (不影響流程)
     *
     * @param vo
     */
    protected void saveLevel (formVO vo) {
        Date today = new Date();
        FormVerifyLogEntity verifyPojo = new FormVerifyLogEntity();
        
        verifyPojo.setFormId(vo.getFormId());
        verifyPojo.setUserId(UserInfoUtil.loginUserId());
        verifyPojo.setCompleteTime(today);
        verifyPojo.setSubmitTime(today);
        verifyPojo.setVerifyLevel(vo.getVerifyLevel());
        verifyPojo.setVerifyType(vo.getVerifyType());
        verifyPojo.setCreatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setCreatedAt(today);
        verifyPojo.setUpdatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setUpdatedAt(today);
        verifyPojo.setVerifyResult(vo.getVerifyResult());
        verifyPojo.setVerifyComment(vo.getModifyComment());
        verifyPojo.setParallel(vo.getParallel());
        verifyPojo.setGroupId(fetchLoginUser().getGroupId());

        verifyLogRepository.save(verifyPojo);
    }
    
    /**
     * 新增關卡進FormVerifyLogRepository
     *
     * @param vo
     * @param level Integer
     * @param verifyResult
     * @author adam.yeh
     */
    protected void saveLevel (formVO vo, Integer level, FormEnum verifyType, FormEnum verifyResult) {
        saveLevel(vo, String.valueOf(level), verifyType, verifyResult);
    }

    /**
     * 新增關卡進FormVerifyLogRepository
     *
     * @param vo
     * @param level String
     * @param verifyResult
     * @author adam.yeh
     */
    protected void saveLevel (formVO vo, String level, FormEnum verifyType, FormEnum verifyResult) {
        Date today = new Date();

        FormVerifyLogEntity verifyPojo = new FormVerifyLogEntity();
        verifyPojo.setFormId(vo.getFormId());
        verifyPojo.setUserId(vo.getUserId());
        verifyPojo.setCompleteTime(vo.getCompleteTime());
        verifyPojo.setVerifyLevel(level);
        verifyPojo.setVerifyType(verifyType.name());
        verifyPojo.setSubmitTime(today);
        verifyPojo.setCreatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setCreatedAt(today);
        verifyPojo.setUpdatedBy(UserInfoUtil.loginUserId());
        verifyPojo.setUpdatedAt(today);
        verifyPojo.setVerifyResult(verifyResult.name());
        verifyPojo.setParallel(vo.getParallel());
        verifyPojo.setGroupId(vo.getGroupId());

        verifyLogRepository.save(verifyPojo);
    }

    /**
     * 更新當前審核的關卡資料
     *
     * @param vo
     * @author adam.yeh
     */
    protected void updateCurrentLevel (formVO vo) {
        Date today = new Date();
        boolean isParallel = Boolean.valueOf(vo.getIsParallel());
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("APPROVAL_LEVEL");
        
        Conditions conditions = new Conditions();
        if (isParallel) {
            conditions.and().equal("FVL.Parallel", vo.getParallel());
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("verifyResult", vo.getVerifyResult());
        params.put("completeTime", today);
        params.put("userId", UserInfoUtil.loginUserId());
        params.put("verifyComment", vo.getVerifyComment());
        params.put("updatedBy", UserInfoUtil.loginUserId());
        params.put("updatedAt", today);
        params.put("formId", vo.getFormId());
        params.put("detailId", vo.getDetailId());
        params.put("verfiyLevel", vo.getVerifyLevel());
        params.put("verfiyType", vo.getVerifyType());
        
        jdbcRepository.update(resource, conditions, params);
    }
    
    /**
     * 計算審核同意/退回時跳幾關的區間</br>
     * 1. 第0個=起始位置</br>
     * 2. 第1個=結束位置
     *
     * @param vo isNextLevel 向下跳關/向上跳關
     * @return
     * @author adam.yeh
     */
    protected List<Integer> calculateLimitNumber (BaseFormVO vo) {
        Integer start, ended;
        List<Integer> limits = new ArrayList<>();
        boolean isNextLevel = vo.getIsNextLevel();

        if (isNextLevel) {
            start = Integer.valueOf(vo.getVerifyLevel());
            ended = Integer.valueOf(vo.getJumpLevel());

            limits.add(start);
            limits.add(ended);
        } else {
            start = Integer.valueOf(vo.getJumpLevel());
            ended = Integer.valueOf(vo.getVerifyLevel());

            limits.add(start - 1);
            limits.add(ended - 1);
        }
        
        logger.info("起始關卡 : " + start);
        logger.info("結束關卡 : " + ended);
        logger.info("抓哪幾關 : " + limits);
        logger.info("是否進關 : " + isNextLevel);

        return limits;
    }

    /**
     * 判斷當前登入者是否有審核的權限
     *
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isAcceptable (formVO vo, FormVerifyLogEntity verifyLog, ResourceEnum resource, SysUserVO userInfo) {
        boolean result = false;

        if(verifyLog != null) {
            // 有沒有押審核人員
            if (StringUtils.isBlank(verifyLog.getUserId())) {
                Map<String, Object> params = new HashMap<>();
                params.put("formId", vo.getFormId());
                params.put("verifyLevel", verifyLog.getVerifyLevel());
                BaseFormVO acceptable = jdbcRepository.queryForBean(resource, params, BaseFormVO.class);
                
                if (acceptable != null) {
                    if (sysUserService.isPic(acceptable.getGroupId())) {// 是否在經辦關卡，並檢查此經辦是否為處理人員
                        result = StringUtils.equals(userInfo.getUserId(),vo.getUserSolving());
                    } else if (StringUtils.isNotBlank(userInfo.getGroupId())) {// 綁定群組
                        result = userInfo.getGroupId().equalsIgnoreCase(acceptable.getGroupId());
                    }
                }
            } else if (StringUtils.isNotBlank(userInfo.getUserId())) {// 綁定使用者編號
                result = userInfo.getUserId().equalsIgnoreCase(verifyLog.getUserId());
            }
        }

        return result;
    }

    /**
     * 檢查表單是否已棄用或結案
     *
     * @return
     * @author adam.yeh
     */
    protected boolean isClosed (String formId) {
        FormEntity formPojo = formRepo.findByFormId(formId);

        if (formPojo == null) {
            return false;
        }

        return (FormEnum.CLOSED.equals(FormEnum.valueOf(formPojo.getFormStatus())) ||
                FormEnum.DEPRECATED.equals(FormEnum.valueOf(formPojo.getFormStatus())));
    }

    /**
     * 判斷當前登入者是否可開變更單或會辦單
     *
     * @param isSubCreation
     * @param groupId
     * @return
     * @author adam.yeh
     */
    protected String isSubCreation (String isSubCreation, String groupId) {
        boolean result = false;
        SysUserVO userInfo = fetchLoginUser();

        result = StringUtils.equals(groupId, userInfo.getGroupId()) &&
                    StringUtils.equals(StringConstant.SHORT_YES, isSubCreation);

        return result ? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
    }

    /**
     *
     * @param isModifyColumn
     * @param groupId
     * @return
     * @author adam.yeh
     */
    protected String isModifyInfo (String isModifyColumn, String groupId) {
        boolean result = false;
        SysUserVO userInfo = fetchLoginUser();

        result = StringUtils.equals(groupId, userInfo.getGroupId()) &&
                    StringUtils.equals(StringConstant.SHORT_YES, isModifyColumn);

        return result ? StringConstant.SHORT_YES : StringConstant.SHORT_NO;
    }

    /**
     * 判斷是否為審核關卡第一關往回退
     *
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isBackToApplyProcess (formVO vo) {
        return !vo.getIsNextLevel() &&
                (FormEnum.REVIEW.name().equals(vo.getVerifyType()) && "0".equals(vo.getJumpLevel()));
    }

    /**
     * 判斷是否為回到申請的第一關
     *
     * @param vo
     * @return
     * @author bernard.yu
     */
    protected boolean isBackToApplyLevel1(formVO vo) {
        return StringUtils.equals(vo.getJumpLevel(), "1")
                && (!vo.getIsNextLevel() && FormEnum.APPLY.name().equals(vo.getVerifyType()));

    }

    /**
     * 審核流程往回跳申請流程最後一關
     *
     * @param vo
     * @author adam.yeh
     */
    protected void backToApply (formVO vo) {
        FormVerifyLogEntity applyLast = verifyLogRepository.findTop1ByFormIdAndVerifyTypeOrderByUpdatedAtDesc(vo.getFormId(), FormEnum.APPLY.name());
        if(!Objects.isNull(applyLast)) {
            int start = Integer.valueOf(applyLast.getVerifyLevel()) - 1;
            int end = start + 1;

            vo.setVerifyLevel(String.valueOf(start));
            vo.setJumpLevel(String.valueOf(end));
            vo.setIsNextLevel(true);
            vo.setIsBackToApply(true);
            vo.setVerifyType(FormEnum.APPLY.name());
        } else {
            //若是直接從擬案狀態直接跳到審核,則不會有申請流程的資料,故在退回的時候,統一退回到申請第一關
            vo.setVerifyLevel(String.valueOf(0));
            vo.setJumpLevel(String.valueOf(1));
            vo.setIsNextLevel(true);
            vo.setIsBackToApply(true);
            vo.setVerifyType(FormEnum.APPLY.name());
        }

        FormEntity form = formRepo.findByFormId(vo.getFormId());
        FormInfoDateEntity date = formDateRepo.findByFormId(vo.getFormId());
        FormJobInfoDateEntity jobDate = formJobDateRepo.findByFormId(vo.getFormId());
        
        form.setIsAlterDone(null);
        formRepo.save(form);
        
        if (date != null) {
            date.setAct(null);
            formDateRepo.save(date);
        }

        if (jobDate != null) {
            jobDate.setAct(null);
            formJobDateRepo.save(jobDate);
        }
    }

    /**
     * 表單結案
     * @param vo
     */
    protected void closeForm (FormEntity form) {
        form.setFormStatus(FormEnum.CLOSED.name());
        form.setProcessStatus(FormEnum.CLOSED.name());
        formRepo.save(form);
    }

    /**
     * 表單直接結案
     * @param vo
     */
    protected void closeFormForImmdiation (formVO vo) {
       closeFormForImmdiation(vo, FormEnum.CLOSE_FORM);
    }

    /**
     * 根據不同的狀態情景, 將表單壓成結案
     * @param vo
     */
    protected void closeFormForImmdiation (formVO vo, FormEnum formEnum) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();

        FormVerifyLogEntity log = verifyRepo.
                findByFormIdAndVerifyLevelAndCompleteTimeIsNull(formId, vo.getVerifyLevel());
        log.setUserId(userId);
        log.setCompleteTime(today);
        log.setVerifyResult(formEnum.name());
        log.setVerifyComment(vo.getVerifyComment());
        log.setUpdatedAt(today);
        log.setUpdatedBy(log.getUpdatedBy());
        verifyRepo.save(log);
        
        formRepo.updateStatusByFormId(formId, FormEnum.CLOSED.name(), userId, today);
    }

    /**
     * 是否可以直接結案
     * @return String
     */
    protected String verifyIsFormClose(formVO vo) {
        if(FormEnum.REVIEW.name().equals(vo.getVerifyType())) {//是否為審核狀態
            SysUserVO userVo = this.fetchLoginUser();
            //檢查登入者的群組ID,與表單流程的群組ID是否一致,並且是否允許可直接結案
            if(userVo.getGroupId().equals(vo.getGroupId()) && StringConstant.SHORT_YES.equals(vo.getIsCloseForm())) {
                return StringConstant.SHORT_YES;
            }
        }
        return StringConstant.SHORT_NO;
    }

    /**
     * 取得各類會辦單的詳細資訊
     * 並放回會辦單VO中
     * @param formId
     * @return
     */
    protected CountersignedFormVO getVariousCFormDetailInfo(String formId){
        CountersignedFormVO vo = new CountersignedFormVO();
        FormInfoCDetailEntity formInfoCDetailEntity = formInfoCDetailRepository.findByFormId(formId);
        BeanUtil.copyProperties(formInfoCDetailEntity, vo, new String[] {"updatedAt"});
        vo.setUnitId(formInfoCDetailEntity.getUnit());
        return vo;
    }

    /**
     * 帶入 Form id 取得表單超連結
     * @param formId
     * @return
     */
    protected String getFormUrl(String formId){
        // 取得 request 中的 URL 並使用 slash 分割重組
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        URI uri = builder.build().toUri();
        StringBuilder hyperLink = new StringBuilder();
        String[] urlSplit = uri.toString().split(StringConstant.SLASH);
        // 取得 Url prefix
        // Example : http://localhost:8080/ISWP
        for (int i = 0; i != 4; i++) {
            hyperLink.append(urlSplit[i] + StringConstant.SLASH);
        }
        // 取得 URL : http://localhost:8080/ISWP/formSearch/search/{FormId}
        hyperLink.append("formSearch/search/" + formId);

        return hyperLink.toString();
    }

    /**
     * 檢查是否需要將表單壓成觀察中的狀態
     *
     * @param vo
     * @return boolean
     */
    protected boolean isWatchingFormStatus(formVO vo) {
        FormInfoDateEntity dateEntity = formDateRepo.findByFormId(vo.getFormId());
        if(dateEntity == null) {
            return false;
        } else if(dateEntity.getObservation() != null && dateEntity.getObservation().after(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 依據VerifyType, FormId, VerifyLevel拿流程關卡資料
     * @param vo
     * @param clazz
     * @return
     * @author adam.yeh
     */
    protected Map<String, Object> getProcessLevel (formVO vo, FormEnum clazz) {
        ResourceEnum resource = formHelper.getLevelInfoResource(clazz, FormEnum.valueOf(vo.getVerifyType()));

        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyLevel", vo.getVerifyLevel());

        return jdbcRepository.queryForMap(resource, params);
    }

    /**
     * 設定當前關卡的審核結果文字供前端顯示<br>
     * P.S. 若是棄用表單的話, 當前關卡的審核結果會動態組成[審核群組]棄用
     *
     * @param formLog
     * @author adam.yeh
     */
    protected void setVerifyResultWording (BaseFormVO formLog, SysUserVO userInfo) {
        FormEnum verifyEnum = FormEnum.valueOf(formLog.getVerifyResult());
        String wording = verifyEnum.verifyType();
        String closeForm = FormEnum.CLOSE_FORM.name();
        String groupSolving = formLog.getGroupSolving();
        String verifyResult = formLog.getVerifyResult();
        
        // 將「直接結案」改為「代科長審核」or「代協理審核」
        if (verifyEnum.toString().equals(closeForm)) {
            if (UserEnum.DEPUTY_MANAGER.wording().equals(groupSolving)) {
                wording = getMessage("form.common.is.close.form.direct2.substitute");
            } else if (UserEnum.VICE_DIVISION_CHIEF.wording().equals(groupSolving.split("_")[1])) {
                wording = getMessage("form.common.is.close.form.vice.substitute");
            }
        }
        
        // 若審核結果為副科修改，關卡群組wording應為其副科群組
        if (FormEnum.VSC_MODIFY.name().equals(verifyResult) && groupSolving.equals("經辦處理")) {
            formLog.setGroupSolving(userInfo.getGroupName());
        }
        
        if (FormEnum.DEPRECATED.equals(verifyEnum)) {
            wording = String.format(verifyEnum.processStatus(), formLog.getGroupSolving());
        }
        
        formLog.setVerifyResultWording(wording);
    }

    /**
     * 檢查是否為作業關卡的指定人員
     *
     * @param vo
     * @return
     */
    protected String isOwner (formVO vo) {
        if(FormEnum.APPLY.name().equals(vo.getVerifyType())) {
            List<FormProcessManagmentJobApplyVO> dataLs = commonJobFormService.getJobWorkItems(vo, false);

            String processOrder;
            for(FormProcessManagmentJobApplyVO target : dataLs) {
                processOrder = String.valueOf(target.getProcessOrder());
                if(isOwnerLevel(vo, processOrder, target)) {
                    return StringConstant.SHORT_YES;
                }
            }
        }

        return StringConstant.SHORT_NO;
    }


    /**
     * 設定退關mail寄送人員
     *
     * @param vo
     * @param back
     */
    protected void setBackLevelMailUserId(formVO vo,FormVerifyLogEntity back) {
        // 設定待審核關卡的人員編號( 為了之後寄信用的邏輯 )
        String userId = "";

        if(!Objects.isNull(back)) {
            userId = back.getUserId();
        } else {
            //若無back資訊,則取表單的createBy人員
            FormEntity formEntity = formRepo.findByFormId(vo.getFormId());
            if(!Objects.isNull(formEntity)) {
                userId = formEntity.getCreatedBy();
            } else {
                //如果連formEntity都取不到資料,就直接拿登入者的id
                userId = fetchLoginUser().getUserId();
            }
        }
        vo.setUserId(userId);
    }

    /**
     * 是否可開問題單
     *
     * @param vo
     * @return boolean
     */
    protected String isCreateQuestionIssue(formVO vo) {
        if(StringConstant.SHORT_YES.equalsIgnoreCase(vo.getIsAddQuestionIssue())) {
            //若該流程可開問題單,則在檢查是否有編輯權限,若有才可以
            FormVerifyLogEntity verifyPojo = verifyRepo.findByFormIdAndVerifyLevelAndCompleteTimeIsNull(vo.getFormId(), vo.getVerifyLevel());
            ResourceEnum resource = formHelper.getLevelInfoResource(FormEnum.valueOf(vo.getFormClass()), FormEnum.valueOf(vo.getVerifyType()));

            boolean result = isAcceptable(vo, verifyPojo, resource, fetchLoginUser());

            if(result) {
                return StringConstant.SHORT_YES;
            }
        }

        return StringConstant.SHORT_NO;
    }

    /**
     * 根據表單流程的選項,檢查該關卡是否需要檢核審核者
     *
     * @param vo
     * @return String
     */
    protected String isApprover(formVO vo) {
        if(StringConstant.SHORT_YES.equalsIgnoreCase(vo.getIsApprover())) {
            String userSolving = vo.getUserSolving();
            String currentUserId = fetchLoginUser().getUserId();

            //若當前關卡=審核者，且當前人員=處理人員時，不顯示【同意】【退回】按鈕
            if(userSolving.equalsIgnoreCase(currentUserId)) {
                return StringConstant.SHORT_YES;
            }
        }

        return StringConstant.SHORT_NO;
    }

    /**
     * 透過formId,移除所有表單資料表下的對應資料表的內容
     *
     * @param formId
     */
    protected void deleteFormTables(String formId) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("DELETE_FORM_TABLES_BY_ID");
        Map<String,Object> params = new HashMap<>();
        params.put("formId", formId);

        jdbcRepository.update(resource,params);
    }
    
    /**
     * 表單進關才要做以下的判斷<br>
     * 當副科跳過科長或副理關卡，審核結果為「代科長審核」或「代副理審核」<br>
     * 若無跳關行為，則為「通過」
     * 
     * @param vo
     */
    protected void prepareVerifyWording(formVO vo) {
        if (vo.getIsNextLevel()) {
            String verifyResult = FormEnum.AGREED.name();
            int jumpLevel = Integer.valueOf(vo.getJumpLevel());
            List<Map<String, Object>> processList = getAllProcessList(vo);
            boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
            
            String groupId;
            int scLevel = 0;
            int d1Level = 0;
            int vscLevel = 0;
            boolean isVsc = false;
            boolean isChief = false;
            boolean isDirect1 = false;
            
            for (Map<String, Object> map : processList) {
                groupId = MapUtils.getString(map, "GroupId", "");
                isVsc = groupId.equals(vo.getGroupSolving() + UserEnum.VSC.name());
                isChief = groupId.equals(vo.getGroupSolving() + UserEnum.SC.name());
                isDirect1 = groupId.indexOf(UserEnum.DEPUTY_MANAGER.symbol()) != -1;
                
                if (isChief) {
                    scLevel = (int) map.get("ProcessOrder");
                } else if (isDirect1) {
                    d1Level = (int) map.get("ProcessOrder");
                } else if (isVsc) {
                    vscLevel = (int) map.get("ProcessOrder");
                }
            }
            
            if (isVice) {
                if (jumpLevel > d1Level && d1Level > vscLevel) {
                    verifyResult = FormEnum.VSC_PROXY2.name();
                } else if (jumpLevel > scLevel && scLevel > vscLevel) {
                    verifyResult = FormEnum.VSC_PROXY1.name();
                }
            }
            
            vo.setVerifyResult(verifyResult);
        }
    }

    /**
     * 更新FormInternalProcessStatus
     * @param formId
     * @param countersigneds_
     */
    protected boolean mergeFormInternalProcessStatus(String formId, String countersigneds_) {
        boolean result = false;
        if (StringUtils.isNotBlank(countersigneds_)) {
            internalProcessRepo.deleteByFormIdAndIsProcessDone(formId, StringConstant.SHORT_NO);
            
            String[] countersigneds = StringUtils.split(countersigneds_, StringConstant.COMMA);
            if (countersigneds != null && countersigneds.length > 0) {
                String division;
                String[] divisionCut;
                FormInternalProcessStatusEntity entity;
                List<FormInternalProcessStatusEntity> insertList = new ArrayList<>();
                Date today = new Date();
                for (String countersigned : countersigneds) {
                    if (StringUtils.contains(countersigned, FormJobEnum.DC + StringConstant.DASH)) {
                        divisionCut = countersigned.split(StringConstant.DASH);
                        if (divisionCut.length > 2) {
                            division = divisionCut[1] + StringConstant.DASH + divisionCut[2];
                        } else {
                            division = countersigned;
                        }
                        
                        entity = new FormInternalProcessStatusEntity();
                        entity.setDivision(division);
                        entity.setFormId(formId);
                        entity.setIsProcessDone(StringConstant.SHORT_NO);
                        entity.setUpdatedBy(UserInfoUtil.loginUserId());
                        entity.setUpdatedAt(today);
                        entity.setCreatedBy(UserInfoUtil.loginUserId());
                        entity.setCreatedAt(today);
                        insertList.add(entity);
                    }
                }
                if (!insertList.isEmpty()) {
                    internalProcessRepo.saveAll(insertList);
                    result = true;
                }
            }
        }
        return result;
    }
    
    protected void saveLog(BaseFormVO baseFormVO) {
        if (StringUtils.isBlank(baseFormVO.getUpdatedBy()) || Objects.isNull(baseFormVO.getUpdatedAt())) {
            logger.warn("Form_id : %s, 並無更新時間或使用者!!", baseFormVO.getFormId());
            return;
        }
        FormContentModifyLogEntity formContentModifyLogEntity = new FormContentModifyLogEntity();
        formContentModifyLogEntity.setFormId(baseFormVO.getFormId());
        formContentModifyLogEntity.setContents(baseFormVO.toString());
        formContentModifyLogEntity.setUpdatedAt(baseFormVO.getUpdatedAt());
        formContentModifyLogEntity.setUpdatedBy(baseFormVO.getUpdatedBy());
        // 因希望紀錄 log 的程式不要影響正常流程資料，所以發生 Exception 時只會在 log 檔中顯示
        try {
            formContentModifyLogRepository.save(formContentModifyLogEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 副科長編輯「預計完成時間」：<br>
     * (1)發信通知其所有會辦科「經辦、副科長、科長」 (會辦科經辦:只通知處理人員)<br>
     * (2)紀錄發信log
     * @param vo
     * @param countersignedIds
     * @author jacky.fu
     */
    protected void ectExtendedSendMail (formVO vo, List<String> countersignedIds) {
        SysUserVO loginUser = sysUserService.getLoginUserInfo(UserInfoUtil.loginUserId());
        String needToReplaceformURL = getFormUrl("XXXXX");
        Map<String,List<LdapUserVO>> needSendUserMap = new HashMap<String,List<LdapUserVO>>();
        for (String formId : countersignedIds) {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
            Conditions conditions = new Conditions();
            conditions
                .and().equal("F.FormId",formId)
                .and().leftPT()
                .equal("F.FormStatus", FormEnum.APPROVING.name()).or().equal("F.FormStatus", FormEnum.WATCHING.name())
                .RightPT();
            CountersignedFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, CountersignedFormVO.class);
            
            if (formInfo == null) continue;
            
            LdapUserEntity userSolving = ldapUserRepository.findByUserIdAndIsEnabled(formInfo.getUserSolving(), StringConstant.SHORT_YES);
            
            ResourceEnum resource2 = ResourceEnum.SQL_SYSTEM_NAME_MANAGEMENT.getResource("FIND_USERS_BY_SYSGROUP");
            Conditions conditions2 = new Conditions();
            conditions2.and().equal("SG.GroupId", formInfo.getGroupSolving() + UserEnum.VICE_DIVISION_CHIEF.symbol());
            conditions2.or().equal("SG.GroupId", formInfo.getGroupSolving() + UserEnum.DIVISION_CHIEF.symbol());
            conditions2.or().equal("LU.UserId", userSolving.getUserId());
            List<LdapUserVO> list = jdbcRepository.queryForList(resource2, conditions2, LdapUserVO.class);
            needSendUserMap.put(formId, list);
        }

        if (MapUtils.isEmpty(needSendUserMap)) {
            return;
        }
        
        ProcessMail mail = new ProcessMail() {

            @Override
            public Map<String, Object> getParams () {
                Map<String, Object> mailParams = new HashMap<>();
                mailParams.put("template", "scopeChangeContent.vm");
                mailParams.put("formClass", vo.getFormClass());
                mailParams.put("summary", vo.getSummary());
                mailParams.put("modifyUserName", loginUser.getName());
                mailParams.put("sysTime", DateUtils.toString(vo.getEct(), DateUtils.pattern12));
                mailParams.put("verifyComment",
                        StringUtils.isBlank(vo.getVerifyComment()) ? vo.getModifyComment()
                                : vo.getVerifyComment());
                return mailParams;
            }
            
            @Override
            public void run () {
                try {
                    for (String formId : needSendUserMap.keySet()) {
                        for (LdapUserVO userVo : needSendUserMap.get(formId)) {
                            Map<String, Object> params = getParams();
                            params.put("formId", formId);
                            params.put("recipient", userVo.getName());
                            String link = needToReplaceformURL.replace("XXXXX",formId);
                            params.put("link", link);
                            List<String> mailList = new ArrayList<String>();
                            mailList.add(userVo.getEmail());
                            sendMail("【ISWP通知】" + formId + " 會辦單之預計完成日異動", mailList, params);
                            recordMailLog(formId, loginUser, BeanUtil.toJson(mailList));
                            logger.info("已寄送郵件清單 : " + BeanUtil.toJson(mailList));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        asyc.execProcessMail(mail);
    }

    
    /**
     * ADMIN，副科長編輯「預計完成時間」後:<br>
     * (1)連動其所有子單(會辦單、變更單)主單預計完成時間<br>
     * (2)主單(需求單、問題單)，編輯「預計完成時間」，連動更新AP工作單：變更結束時間<br>
     * (3)主單(事件單)，編輯「事件優先順序」，連動更新AP工作單：變更結束時間<br>
     * (4)彈跳視窗:若使用者選擇「此單之變更範圍，與原來不同」，isScopeChanged欄位更新=Y，否則為預設值N<br>
     *    備註:不論使用者選擇變更範圍相同或不同，皆一併連動子單的時間，該彈跳視窗只為提醒用。
     */
    protected void ectExtendedForStretchs (formVO vo) {
        String formId = vo.getFormId();
        String formClass = vo.getFormClass();
        String isDifferentScope = vo.getIsDifferentScope();
        boolean isAdmin = sysUserService.isAdmin();
        boolean isInc = FormEnum.INC.name().equals(formClass);
        boolean isVice = sysUserService.isVice(fetchLoginUser().getGroupId());
        
        if (!(isVice || isAdmin || isInc)) return;
        
        List<BaseFormVO> allList = getChildFormList(formId);
        
        //變更單(含會辦延伸變更單)
        List<String> chgIds = allList.stream()
                .filter(it -> FormEnum.CHG.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        //會辦單
        List<String> countersignedIds = allList.stream()
                .filter(it ->
                    FormEnum.Q_C.name().equals(it.getFormClass()) ||
                    FormEnum.SR_C.name().equals(it.getFormClass()) ||
                    FormEnum.INC_C.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        
        //AP工作單
        List<String> apJobIds = allList.stream()
                .filter(it -> FormEnum.JOB_AP.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        
        Date ect = vo.getEct();
        
        //連動其所有子單(會辦單、變更單)主單預計完成時間
        if (CollectionUtils.isNotEmpty(countersignedIds)){
            formDateRepo.updateMectByFormIdIn(ect, countersignedIds);
        }
        
        if (CollectionUtils.isNotEmpty(chgIds)){
            formDateRepo.updateCctByFormIdIn(ect, chgIds);
            
            //超級副科改單:前端彈跳視窗-->有勾取「此單之變更範圍，與原來不同」，更新IsScopeChanged=Y
            if (StringConstant.SHORT_YES.equals(isDifferentScope)) { 
                formChgDetailRepo.updateIsScopeChangedByFormIdIn(StringConstant.SHORT_YES, chgIds);
            } else {
                formChgDetailRepo.updateIsScopeChangedByFormIdIn(StringConstant.SHORT_NO, chgIds);
            }
        }
        
        //連動其所有子單(AP工作單)變更結束時間
        if (CollectionUtils.isNotEmpty(apJobIds)){
            formJobDateRepo.updateCctByFormIdIn(ect, apJobIds);
        }
        
        if (isVice) {
            ectExtendedSendMail(vo, countersignedIds);
        }

        // 紀錄原始預計完成時間
        if (vo.getOect() == null) {
            List<String> csForms = new ArrayList<>();
            csForms.add(FormEnum.Q_C.name());
            csForms.add(FormEnum.SR_C.name());
            csForms.add(FormEnum.INC_C.name());

            FormInfoDateEntity formDate = formDateRepo.findByFormId(formId);
            Date oect = formDate.getEct();
            
            if (csForms.contains(formClass)) {
                oect = formDate.getMect();
            }
            
            formDate.setOect(oect);
            formDateRepo.save(formDate);

            // 更新Form: isExtended=Y
            formRepo.updateIsExtendedByFormId(StringConstant.SHORT_YES, formId);
        }
    }
    
    /**
     * 是否送簽至副理關卡
     * @param vo
     * @return
     */
    protected boolean isSendFormToDirect1 (formVO vo) {
        boolean isSendToD1 = false;
        if (vo.getIsNextLevel()) {
            int jumpLevel = Integer.valueOf(vo.getJumpLevel());
            List<Map<String, Object>> processList = getAllProcessList(vo);

            String groupId;
            int scLevel = 0;
            int vscLevel = 0;
            boolean isVsc = false;
            boolean isChief = false;
            
            for (Map<String, Object> map : processList) {
                groupId = MapUtils.getString(map, "GroupId", "");
                isVsc = groupId.equals(vo.getGroupSolving() + UserEnum.VSC.name());
                isChief = groupId.equals(vo.getGroupSolving() + UserEnum.SC.name());

                if (isChief) {
                    scLevel = (int) map.get("ProcessOrder");
                } else if (isVsc) {
                    vscLevel = (int) map.get("ProcessOrder");
                }
            }
            
            if (jumpLevel > scLevel && scLevel > vscLevel) {
                isSendToD1 = true;
            }
        }
        
        return isSendToD1;
    }

    /*
     * 寄送表單各種流程送出後所對應的 mail 內容
     * 如:同意，退回，作廢，結案
     * @param vo
     * @param tParams 執行續需要另外用到的參數
     */
    private synchronized void sendFormProcessMail (formVO vo, Map<String, Object> tParams) throws Exception {
        String formURL = MapUtils.getString(tParams, "formURL");
        SysUserVO loginUser = (SysUserVO) MapUtils.getObject(tParams, "loginUser");
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        String verifyType = FormEnum.valueOf(vo.getVerifyResult()).verifyType();
        String formName = getMessage(FormEnum.valueOf(vo.getFormClass()).wording());
        logger.info(this.getMessage("form.mail.send.start.log", new String[]{formName, verifyType}));

        // 從 FORM_VERIFY_LOG 取得該表單最新的紀錄
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_VERIFY_LOGS_LATEST_FORM_RECORD");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        List<BaseFormVO> nextLevels = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        String pendingGroupId = getPendingGroupId(vo, nextLevels, form, loginUser);

        // 排除作廢和副科修改之表單
        if (StringUtils.isBlank(pendingGroupId) &&
                !FormEnum.DEPRECATED.name().equals(vo.getVerifyResult()) &&
                !FormEnum.VSC_MODIFY.name().equals(vo.getVerifyResult())) {
            if (CollectionUtils.isNotEmpty(nextLevels)) {
                recordMailLog(vo.getFormId(),
                        loginUser, nextLevels.get(0).getDetailId());
                logger.error("找不到可通知之群組。");
            } else {
                recordMailLog(vo.getFormId(), loginUser,
                        "FIND_VERIFY_LOGS_LATEST_FORM_RECORD is empty.");
                logger.error("nextLevels is null.");
            }
            
            return;
        }

        String userSolving = null;
        String userCreated = null;
        LdapUserEntity ldapUser = null;
        SysGroupEntity pendingGroup = null;
        String yes = StringConstant.SHORT_YES;
        String i18n, sysGroupId, mailSubject, mailTemplate;
        String division = subToDivision(vo.getGroupSolving());
        boolean isPic = sysUserService.isPic(pendingGroupId);
        boolean isDirect1 = sysUserService.isDirect1(pendingGroupId);
        boolean isDirect2 = sysUserService.isDirect2(pendingGroupId);
        boolean isC = StringUtils.contains(vo.getFormClass(), "_C");     // 會辦單
        boolean isJob = StringUtils.contains(vo.getFormClass(), "JOB_"); // 工作單/工作會辦單
        boolean isParallel = yes.equals(vo.getIsParallel());             // 平行會辦
        List<LdapUserEntity> mailTo = new ArrayList<>();
        
        // 副科修改一定要寄信
        if (!FormEnum.VSC_MODIFY.name().equals(vo.getVerifyResult())) {
            if ((isDirect1 || isDirect2) &&
                    !FormEnum.CLOSED.name().equals(vo.getVerifyResult())) {
                return; // TODO 客戶表示, 暫時不寄信給協/副理
            }
        }

        // 依表單各狀態給予不同的 mail 內容及 mail 清單
        switch (FormEnum.valueOf(vo.getVerifyResult())) {
            case SENT:
            case VSC_PROXY1:
            case AGREED:
                pendingGroup = sysGroupRepository.findByGroupId(pendingGroupId);
                sysGroupId = String.valueOf(pendingGroup.getSysGroupId());
                mailTemplate = isPic ? MailTemplate.AGREED_PIC.src() : MailTemplate.AGREED.src();
                i18n = isPic ? "form.mail.handle.group.subject" : "form.mail.reviewer.group.subject";// 審核者/經辦要用的主旨
                mailSubject = getMessage(i18n, new String[] {pendingGroup.getGroupName(), vo.getFormId(), formName});
    
                if (isPic) {
                    if ((isC || isJob || isParallel) && CollectionUtils.isNotEmpty(nextLevels)) {
                        for (BaseFormVO nextLevel : nextLevels) {
                            mailTo.add(ldapUserRepository.
                                    findByUserIdAndIsEnabled(nextLevel.getUserId(), yes));
                        }
                    } else {
                        mailTo.add(ldapUserRepository.
                                findByUserIdAndIsEnabled(vo.getUserSolving(), yes));
                    }
                } else if (isDirect1) {
                    String like = "%" + division + "%";
                    DashboardDirectAuthEntity auth = directRepo.findByDivisionLike(like);
                    if (auth != null) {
                        mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(auth.getUserId(), yes));
                    }
                } else {// 預設寄群組
                    mailTo = ldapUserRepository.findBySysGroupIdAndIsEnabled(sysGroupId, yes);
                }
                
                break;
                
            case DISAGREED:
                pendingGroup = sysGroupRepository.findByGroupId(pendingGroupId);
                mailTemplate = MailTemplate.DISAGREED.src();
                mailSubject = getMessage("form.mail.reject.user.subject", new String[]{pendingGroup.getGroupName(), vo.getFormId(), formName});

                if (isParallel) {
                    for (String userId : vo.getRecipients()) {
                        mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(userId, yes));
                    }
                } else {
                    mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(vo.getUserId(), yes));
                }
                
                break;
                
            case DEPRECATED:
                mailTemplate = MailTemplate.DEPRECATED.src();
                mailSubject = getMessage("form.mail.deprecated.subject", new String[]{vo.getFormId(), formName});

                ldapUser = ldapUserRepository.findByUserIdAndIsEnabled(vo.getUserCreated(), yes);
                if (ldapUser != null) {
                    mailTo.add(ldapUser);
                    userSolving = CollectionUtils.isNotEmpty(mailTo) ? mailTo.get(0).getName() : "";
                }
                
                break;
                
            case CLOSED:
                mailTemplate = MailTemplate.CLOSED.src();
                mailSubject = getMessage("form.mail.closed.subject", new String[]{vo.getFormId(), formName});
                userSolving = vo.getUserSolving();
                userCreated = formRepo.findByFormId(vo.getFormId()).getUserCreated();
                
                if (isC) {
                    mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(userSolving, yes));
                    
                    if (!StringUtils.equals(userSolving, userCreated)) {
                        mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(userCreated, yes));
                    }
                } else {
                    userSolving = StringUtils.isBlank(userSolving) ? userCreated : userSolving;
                    mailTo.add(ldapUserRepository.findByUserIdAndIsEnabled(userSolving, yes));
                }
                
                break;
            
            case VSC_MODIFY:
                mailTemplate = MailTemplate.VICE_MODIFY.src();
                pendingGroup = sysGroupRepository.findByGroupId(pendingGroupId);
                mailSubject = getMessage("form.mail.modify.by.vice.subject", new String[]{vo.getFormId(), formName});

                if (StringUtils.isBlank(vo.getUserCreated()) ||
                        StringUtils.isBlank(vo.getDivisionCreated())) {
                    vo.setCreateTime(form.getCreateTime());
                    vo.setUserCreated(form.getUserCreated());
                    vo.setDivisionCreated(form.getDivisionCreated());
                }
                
                Map<String, Object> mails = getViceModifyMails(vo);
                String scMailStr = MapUtils.getString(mails, "scMails", "");
                String vscMailStr = MapUtils.getString(mails, "vscMails", "");
                String picMailStr = MapUtils.getString(mails, "picMails", "");
                List<String> scMails = Arrays.asList(StringUtils.split(scMailStr, ","));
                List<String> vscMails = Arrays.asList(StringUtils.split(vscMailStr, ","));
                List<String> picMails = Arrays.asList(StringUtils.split(picMailStr, ","));
                
                for (String mail : picMails) {
                    LdapUserEntity e = new LdapUserEntity();
                    e.setEmail(mail);
                    mailTo.add(e);
                }
                
                pendingGroup.setGroupName(MapUtils.getString(mails, "picName", ""));
                sendMail(vo, formURL, formName, verifyType, userSolving, mailSubject, mailTemplate, loginUser, pendingGroup, mailTo);
                
                mailTo.clear();
                
                for (String mail : vscMails) {
                    LdapUserEntity e = new LdapUserEntity();
                    e.setEmail(mail);
                    mailTo.add(e);
                }
                
                pendingGroup.setGroupName(MapUtils.getString(mails, "vscName", ""));
                sendMail(vo, formURL, formName, verifyType, userSolving, mailSubject, mailTemplate, loginUser, pendingGroup, mailTo);
                
                mailTo.clear();
                
                for (String mail : scMails) {
                    LdapUserEntity e = new LdapUserEntity();
                    e.setEmail(mail);
                    mailTo.add(e);
                }
                
                pendingGroup.setGroupName(MapUtils.getString(mails, "scName", ""));
                sendMail(vo, formURL, formName, verifyType, userSolving, mailSubject, mailTemplate, loginUser, pendingGroup, mailTo);
                
                //發信給會辦科
                sendViceModifyToCountersigned(vo, mailSubject, formName, tParams);
                
                return;
                
            default:
                if (FormEnum.VSC_PROXY2.name().equals(vo.getVerifyResult())) {
                    logger.warn("副科長代副理審核");
                } else {
                    logger.warn("無法取得表單審核結果");
                }
                
                return;
        }
        
        // 整批審核會有開單資訊為空值問題，所以需從資料庫搜尋
        if (StringUtils.isBlank(vo.getUserCreated()) ||
                StringUtils.isBlank(vo.getDivisionCreated())) {
            vo.setCreateTime(form.getCreateTime());
            vo.setUserCreated(form.getUserCreated());
            vo.setDivisionCreated(form.getDivisionCreated());
        }

        sendMail(vo, formURL, formName, verifyType, userSolving, mailSubject, mailTemplate, loginUser, pendingGroup, mailTo);
    }
    
    /**
     * 副科修改表單後，需寄信通知的群組<br>
     * 1. 處理科別的副科群組<br>
     * 2. 處理科別的科長群組<br>
     * 3. 僅處理人員
     * @param vo
     * @return mailList
     * @throws Exception
     */
    private Map<String, Object> getViceModifyMails (formVO vo) throws Exception {
        String userSolving = vo.getUserSolving();
        String[] sp = vo.getDivisionSolving().split("-");
        ResourceEnum mailResource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_PIC_AND_VSC_AND_SC_EMAIL");
        Map<String, Object> param = new HashMap<>();
        param.put("division", sp[1]);
        param.put("departmentId", sp[0]);
        param.put("sc", UserEnum.SC.name());
        param.put("vsc", UserEnum.VSC.name());
        
        //取得經辦、副科、科長的mail
        List<SysGroupEntity> es = sysGroupRepository.findByDepartmentIdAndDivisionAndGroupIdContaining(sp[0], sp[1], vo.getGroupSolving());
        //取得表頭的「處理人員」資訊
        LdapUserEntity handler = ldapUserRepository.findByUserIdAndIsEnabled(userSolving, StringConstant.SHORT_YES);

        Map<String, Object> mails = jdbcRepository.queryForMap(mailResource, param);
        mails.put("picMails", handler.getEmail()); //只需寄給「處理人員」
        
        for (SysGroupEntity e : es) {
            if (e.getGroupId().endsWith(UserEnum.VSC.name())) {
                mails.put("vscName", e.getGroupName());
            } else if (e.getGroupId().endsWith(UserEnum.SC.name()) &&
                        !e.getGroupId().endsWith(UserEnum.VSC.name())) {
                mails.put("scName", e.getGroupName());
            } else if (e.getGroupId().equals(vo.getGroupSolving()) &&
                    userSolving.equals(handler.getUserId())) {
                mails.put("picName", e.getGroupName());
            }
        }
        
        return mails;
    }
    
    /**
     * 副科修改後，需寄信給會辦單之處理人員、副科、科長
     * @param vo
     * @param mailSubject
     * @throws Exception
     */
    protected void sendViceModifyToCountersigned (formVO vo, String mailSubject, String formName, Map<String, Object> tParams) throws Exception {
        String formURL = MapUtils.getString(tParams, "formURL");
        SysUserVO loginUser = (SysUserVO) MapUtils.getObject(tParams, "loginUser");
        
        //取得所有子單(會辦單)
        List<BaseFormVO> allList = getChildFormList(vo.getFormId());
        List<String> countersignedIds = allList.stream()
                .filter(it ->
                    FormEnum.SR_C.name().equals(it.getFormClass()) ||
                    FormEnum.Q_C.name().equals(it.getFormClass()) ||
                    FormEnum.INC_C.name().equals(it.getFormClass()) ||
                    FormEnum.JOB_AP_C.name().equals(it.getFormClass()) ||
                    FormEnum.JOB_SP_C.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        
        Map<String,List<LdapUserVO>> needSendUserMap = new HashMap<String,List<LdapUserVO>>();
        for (String formId : countersignedIds) {
            ResourceEnum formInfo = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFOS");
            Conditions conditions = new Conditions();
            conditions.and().equal("F.FormId", formId);
            CountersignedFormVO countersignedForm = jdbcRepository.queryForBean(formInfo, conditions, CountersignedFormVO.class);
            
            //取得會辦單的「處理人員」
            LdapUserEntity handler = ldapUserRepository.findByUserIdAndIsEnabled(countersignedForm.getUserSolving(), StringConstant.SHORT_YES);
            
            ResourceEnum findUser = ResourceEnum.SQL_SYSTEM_NAME_MANAGEMENT.getResource("FIND_USERS_BY_SYSGROUP");
            Conditions sysGroup = new Conditions();
            sysGroup.and().equal("SG.GroupId", countersignedForm.getGroupSolving() + UserEnum.VICE_DIVISION_CHIEF.symbol());
            sysGroup.or().equal("SG.GroupId", countersignedForm.getGroupSolving() + UserEnum.DIVISION_CHIEF.symbol());
            sysGroup.or().equal("LU.UserId", handler.getUserId());
            
            List<LdapUserVO> list = jdbcRepository.queryForList(findUser, sysGroup, LdapUserVO.class);
            needSendUserMap.put(formId, list);
        }
        
        if (MapUtils.isEmpty(needSendUserMap)) {
            return;
        }
        
        ProcessMail mail = new ProcessMail() {
            
            @Override
            public Map<String, Object> getParams () {
                Map<String, Object> mailParams = new HashMap<>();
                mailParams.put("template", "formModifyByViceContent.vm");
                mailParams.put("link", formURL);
                mailParams.put("formClass", formName);
                mailParams.put("formId", vo.getFormId());
                mailParams.put("userName", loginUser.getName());
                mailParams.put("modifyComment", vo.getModifyComment());
                mailParams.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));
                mailParams.put("updatedAt", DateUtils.toString(vo.getUpdatedAt(), DateUtils.pattern12));
                
                return mailParams;
            }
            
            @Override
            public void run () {
                try {
                    for (String formId : needSendUserMap.keySet()) {
                        for (LdapUserVO userVo : needSendUserMap.get(formId)) {
                            String sysGroupId = userVo.getSysGroupId();
                            long id = Long.parseLong(sysGroupId);
                            SysGroupEntity entity = sysGroupRepository.findGroupNameBySysGroupId(id);
                            
                            Map<String, Object> params = getParams();
                            params.put("group", entity.getGroupName());
                            
                            List<String> mailList = new ArrayList<String>();
                            mailList.add(userVo.getEmail());
                            sendMail(mailSubject, mailList, params);
                            recordMailLog(formId, loginUser, BeanUtil.toJson(mailList));
                            logger.info("已寄送會辦科郵件清單 : " + BeanUtil.toJson(mailList));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        asyc.execProcessMail(mail);
    }

    private void sendMail (
            formVO vo,
            String formURL,
            String formName,
            String verifyType,
            String userCreated,
            String mailSubject,
            String mailTemplate,
            SysUserVO loginUser,
            SysGroupEntity pendingGroup,
            List<LdapUserEntity> ldapUsers) {
        Map<String, Object> params;
        List<String> mailTo = new ArrayList<>();
        List<String> sentList = new ArrayList<>();
        ldapUsers.removeAll(Collections.singletonList(null));// 移除集合內為空的值
        
        if (CollectionUtils.isNotEmpty(ldapUsers)) {
            String warn = "「%s」emill not found.";
            
            for (LdapUserEntity user : ldapUsers) {
                if (StringUtils.isBlank(user.getEmail())) {
                    logger.warn(String.format(warn, user.getUserId()));
                    sentList.add(String.format(warn, user.getUserId()));
                    continue;
                }

                mailTo.clear();
                mailTo.add(user.getEmail());
                params = getTemplateParams(vo, formURL, formName, userCreated, loginUser, user, pendingGroup);
                params.put("formNmae", formName);
                params.put("verifyType", verifyType);
                params.put("template", mailTemplate);
                params.put("verifyComment", 
                        StringUtils.isBlank(vo.getVerifyComment()) ? "" : vo.getVerifyComment());
                sendMail(mailSubject, mailTo, params);
                sentList.addAll(mailTo);
            }

            String sentJson = BeanUtil.toJson(sentList);
            recordMailLog(vo.getFormId(), loginUser, sentJson);
            logger.info("已寄送郵件清單 : " + sentJson);
        }
    }

    // Template 檔案所需要的變數名稱
    private Map<String, Object> getTemplateParams (
            formVO vo,
            String formURL,
            String formName,
            String userCreated,
            SysUserVO loginUser,
            LdapUserEntity user,
            SysGroupEntity sysGroup) {
        LdapUserEntity ldapUserEntity = null;
        String yes = StringConstant.SHORT_YES;
        Map<String, Object> params = new HashMap<>();

        params.put("formClass", formName);                                                     // 表單類型
        params.put("user", user.getName());                                                    // 收信者名稱
        params.put("formId", vo.getFormId());                                                  // 表單編號
        params.put("link", formURL);                                                           // 表單超鏈結
        params.put("verifyComment", vo.getVerifyComment());                                    // 審核意見
        params.put("modifyComment", vo.getModifyComment());                                    // 修改意見
        params.put("userName", loginUser.getName());                                           // 當前登入使用者資訊
        params.put("divisionCreated", vo.getDivisionCreated());                                // 開單科別
        params.put("summary", getFormDetailInfo(vo.getFormId()).getSummary());                 // 摘要
        params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // 系統時間
        params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // 建立日期
        params.put("updatedAt", DateUtils.toString(vo.getUpdatedAt(), DateUtils.pattern12));   // 更新時間

        // 審核群組
        if (sysGroup != null) {
            params.put("group", sysGroup.getGroupName());
        }
        
        // 開單人員
        if (FormEnum.DEPRECATED.name().equals(vo.getVerifyResult())) {
            params.put("userCreated", StringUtils.isNotBlank(userCreated) ? userCreated : vo.getUserCreated());
        } else {
            ldapUserEntity = ldapUserRepository.findByUserIdAndIsEnabled(vo.getUserCreated(), yes);
            params.put("userCreated", Objects.isNull(ldapUserEntity) ? vo.getUserCreated() : ldapUserEntity.getName());
        }
        
        logger.info("Mail params : " + params);
        
        return params;
    }

    private String subToDivision (String groupSolving) {
        int dash = 0;
        
        if (StringUtils.isNotBlank(groupSolving)) {
            dash = groupSolving.indexOf("-") + 1;
        }
        
        return groupSolving.substring(dash);
    }

    // 紀錄表單通知信到資料庫
    private void recordMailLog (
            String formId, SysUserVO loginUser, String sentJson) {
        Date today = new Date();
        String userId = loginUser.getUserId();
        
        SysMailLogEntity record = new SysMailLogEntity();
        record.setCreatedAt(today);
        record.setUpdatedAt(today);
        record.setCreatedBy(userId);
        record.setUpdatedBy(userId);
        record.setRecognize(formId);
        record.setAddresses(sentJson);
        mailLogRepo.save(record);
    }

    // 取得同意/退回之後審核關卡的群組編號
    private String getPendingGroupId (formVO vo, List<BaseFormVO> nextLevels, FormEntity form, SysUserVO loginUser) {
        String nextGroup = null;
        
        if (CollectionUtils.isNotEmpty(nextLevels)) {// 準備結案
            String formStatus = form.getFormStatus();
            BaseFormVO nextLevel = nextLevels.get(0);
            String detailId = nextLevel.getDetailId();
            String verifyType = nextLevel.getVerifyType();
            String verifyLevel = nextLevel.getVerifyLevel();
            boolean isC = StringUtils.contains(vo.getFormClass(), "_C");// 會辦單
            boolean isReview = FormEnum.REVIEW.name().equals(verifyType);
            boolean isClosed = FormEnum.CLOSED.name().equals(formStatus);

            if (isC) {
                IBaseCountersignedFormService<CountersignedFormVO> cMailService =
                        formHelper.getCountersignedMailService(FormEnum.valueOf(vo.getFormClass()));
                nextGroup = cMailService.getGroupIdFromFormProcess(detailId, Integer.valueOf(verifyLevel), verifyType);
            } else {
                if (isReview) {
                    nextGroup = getFormReviewGroupInfo(detailId, verifyLevel);
                } else {
                    nextGroup = getFormApplyGroupInfo(detailId, verifyLevel);
                }

                // 若表單已結案需補上結案關鍵字讓後面的邏輯可以拿到正確的template
                if (isClosed) {
                    vo.setVerifyResult(formStatus);
                }
            }
        } else {
            nextGroup = loginUser.getGroupId();
        }

        return nextGroup;
    }

    private boolean isOwnerLevel (formVO vo, String processOrder, FormProcessManagmentJobApplyVO target) {
        return (vo.getVerifyLevel().equals(processOrder) &&
                    UserInfoUtil.loginUserId().equalsIgnoreCase(target.getUserId()));
    }

    // 儲存日誌紀錄
    private void saveNewFormUserRecord(FormUserRecordEntity newUser){
        newUser.setCreatedBy(UserInfoUtil.loginUserId());
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedBy(UserInfoUtil.loginUserId());
        newUser.setUpdatedAt(new Date());
        formUserRecordRepository.save(newUser);
    }

    private String genFilePath (String name) {
        return env.getProperty("form.file.download.dir") + File.separatorChar
        + DateUtils.getCurrentDate(DateUtils._PATTERN_YYYYMMDD) + File.separatorChar + name;
    }
    
    /**
     * 取得表單全部的流程關卡
     * 
     */
    protected List<Map<String, Object>> getAllProcessList (formVO vo) {
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        
        //取得申請或審核流程，關卡清單的SQL檔
        ResourceEnum limitList = formHelper.getLimitResource(formClass, verifyType);
        
        List<Map<String, Object>> processList = null;
        Conditions conditions = null;
        String processOrder = null;
        boolean isApJobForm = FormEnum.JOB_AP.name().equals(vo.getFormClass());
        boolean isSpJobForm = FormEnum.JOB_SP.name().equals(vo.getFormClass());
        boolean isApCJobForm = FormEnum.JOB_AP_C.name().equals(vo.getFormClass());
        boolean isSpCJobForm = FormEnum.JOB_SP_C.name().equals(vo.getFormClass());
        
        List<FormProcessManagmentJobApplyVO> dataLs = commonJobFormService.getJobWorkItems(vo, false);
        for (FormProcessManagmentJobApplyVO target : dataLs) {
            processOrder = String.valueOf(target.getProcessOrder());
        }
        
        if (isApJobForm || isSpJobForm || isApCJobForm || isSpCJobForm) {
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            params.put("detailId", vo.getDetailId());
            params.put("verifyLevel", vo.getVerifyLevel());
            params.put("processOrder", processOrder);
            
            conditions = new Conditions().and().equal("PROCESS.DetailId", vo.getDetailId());
            
            if (isApJobForm) {
                conditions.and().unEqual("PERSON.Level", FormJobEnum.CSPERSON.name());
            }
            processList = jdbcRepository.queryForList(limitList, conditions, params);
        } else {
            conditions = new Conditions().and().equal("PROCESS.DetailId", vo.getDetailId());
            processList = jdbcRepository.queryForList(limitList, conditions);
        }
        
        return processList;
    }
    
    /**
     * 主單：
     * 情境一：經辦 送結案，編輯「實際完成時日」小於其工作單最新「連線系統完成日期」，系統提示+不儲存+不允許送出
     * 情境二：副科 在REVIEW副科關卡，編輯「實際完成時日」小於其工作單最新「連線系統完成日期」，系統提示+不儲存+不允許送出
     * 情境三：副科 非REVIEW副科關卡，編輯「實際完成時日」小於其工作單最新「連線系統完成日期」，系統提示+不儲存
     * 
     * 比對結果為下列狀態時，系統提示訊息：
     *  ①主單「實際完成日期」 < AP工作單「連線系統完成日期」or 「測試系統完成日期」(且已結案)
     *  ②主單「實際完成日期」 < SP工作單「實際完成日期」 (且已結案)
     * @param formIds
     * @param vo
     * @param formClass
     * @return
     * @author adam.yeh
     */
    private boolean calJobSCTWarning (List<String> formIds, formVO vo, String formClass) {
        boolean isAlert = true;
        Date mainFormACT = vo.getAct();

        if (CollectionUtils.isNotEmpty(formIds) && mainFormACT != null) {
            long tempTime = 0L;
            long lastTime = 0L;// 最新的那張工作單的完成日期
            List<FormJobInfoDateEntity> dateList = formJobDateRepo.findByFormIdIn(formIds);
            
            for (FormJobInfoDateEntity date : dateList) {
                if (FormEnum.JOB_AP.name().equals(formClass)) {
                    tempTime = date.getSct() == null ? 0L : date.getSct().getTime();
                    lastTime = date.getTct() == null ? 0L : date.getTct().getTime();
                    tempTime = tempTime > lastTime ? tempTime : lastTime;
                } else if (FormEnum.JOB_SP.name().equals(formClass)) {
                    tempTime = date.getAct() == null ? 0L : date.getAct().getTime();
                }
                
                lastTime = tempTime > lastTime ? tempTime : lastTime;
            }
            
            isAlert = !(lastTime != 0 && mainFormACT.getTime() >= lastTime);
        }

        return isAlert;
    }

}
