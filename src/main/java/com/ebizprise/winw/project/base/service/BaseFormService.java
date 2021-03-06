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
 * ?????????????????? ????????????
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
     * ???????????????????????????
     * @param formId
     * @return
     */
    public abstract formVO getFormDetailInfo(String formId);

    /**
     * ??????????????????????????????????????????
     * @param vo
     * @return
     */
    protected abstract String getFormApplyGroupInfo(formVO vo);

    /**
     * ??????????????????????????????????????????
     * @param detailId
     * @param processOrder
     * @return
     */
    protected abstract String getFormApplyGroupInfo(String detailId, String processOrder);

    /**
     * ??????????????????????????????????????????
     * @param vo
     * @return
     */
    protected abstract String getFormReviewGroupInfo(formVO vo);

    /**
     * ??????????????????????????????????????????
     * @param detailId
     * @param processOrder
     * @return
     */
    protected abstract String getFormReviewGroupInfo(String detailId, String processOrder);

    /**
     * ???????????????????????????????????????(??????????????????????????????????????????????????????????????????????????????)
     *
     * @param vo
     * @return String
     */
    protected abstract String isApplyLastLevel (String detailId, String verifyType, String verifyLevel);

    /**
     * ???????????????????????????????????????(??????????????????????????????????????????????????????????????????????????????)
     *
     * @param vo
     * @return String
     */
    protected abstract String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel);
    
    /**
     * ??????????????????????????????????????????????????????
     * @return
     * @author adam.yeh
     */
    protected abstract String getReviewLastLevel (String detailId);

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????? (????????????)<br>
     * 1. ?????????????????????????????? >= AP???????????????????????????????????????(????????????)<br>
     * 2. ?????????????????????????????? >= SP????????????????????????????????? (????????????)<br>
     * 3. ?????????(?????????)???????????????????????????????????? (???????????????)<br>
     * ??????????????????????????????????????????????????????AP?????????????????????????????????????????????SP?????????????????????????????????
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
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param vo
     * @author jacky.fu
     */
   public void updateVerifyLog (formVO vo) {
       //??????????????????(????????????)
       if(StringUtils.isNoneBlank(vo.getUserSolving())) {
           ResourceEnum resource2 = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_USERSOLVING");
           Map<String, Object> params2 = new HashMap<String, Object>();
           params2.put("formId", vo.getFormId());
           params2.put("userSolving", vo.getUserSolving());
           jdbcRepository.update(resource2, params2);
       }
       
       //??????BY??????????????????
       ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_VERIFY_LOG_BY_CHECKPERSON");
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("formId", vo.getFormId());
       jdbcRepository.update(resource, params);
       
       //??????BY????????????????????????
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
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ???
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
     * ??????????????????????????????????????????
     * @param form
     * @author adam.yeh
     */
    protected void saveFormAlterResult (String formId, boolean isC) {
        FormEntity form = formRepo.findByFormId(formId);
        FormInfoDateEntity formDate = formDateRepo.findByFormId(formId);
        
        // ???????????????????????????????????????????????????????????????????????????????????????, ???????????????????????????????????????????????????
        // ??????????????????formDate.getAct() != null?????????, ?????????IsAlterDone???????????????(???????????????)
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
     * ?????????????????????????????????????????????
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
     * ?????????????????????????????????
     * @param subject ??????
     * @param mailList ???????????????
     * @param params :<br>
     * -ccList ????????????<br>
     * -formName ????????????<br>
     * -template ????????????<br>
     * -verifyType ??????????????????<br>
     * -?????????????????????????????????
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
            logger.error("???????????????????????????", e);
        }
    }
    
    /**
     * ????????????????????????????????????
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
     * ?????????????????????????????????<br>
     * P.S. ?????????jumpLevel=???????????????????????????
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
     * ?????????????????????????????????????????????
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
     * ????????????????????????
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
     * ???????????????????????????????????????, ???????????????????????????????????????
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
     * ????????????????????????????????????
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isParallelApprover (
            formVO vo, FormVerifyLogEntity entity, SysUserVO userInfo) {
        boolean result = true;
        String parallel = entity.getParallel();
        String subGroup = userInfo.getSubGroup();
        
        if (StringUtils.isNotBlank(parallel)) {// ??????????????????????????????
            result = StringUtils.equals(parallel, subGroup);
        }
        
        return result;
    }

    /**
     * ???????????????????????????????????????
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
     * ???????????????????????????
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
     * ?????????????????????????????????
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
     * ??????????????????????????????
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
     * 1. ?????????????????????????????????????????????</br>
     * 2. ????????????????????????????????????
     * @param vo
     * @return
     */
    protected boolean isStretchFormClosed (formVO vo) {
        String isWait = "N";
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());

        // ?????????????????????????????????
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

        // ?????????????????????????????????
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
     * ?????????????????????
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
     * ?????????????????????????????????_???????????????????????????????????????<br>
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????<br><br>
     * ????????????:???????????????????????????????????????????????????????????????????????????????????????
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
        
        //???????????????????????????????????????????????????????????????
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
     * ???????????????????????????????????? ?????????????????????
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
     * ???????????????????????????
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
        params = BeanUtil.fromJson(BeanUtil.toJson(params));// ???????????????UPPER CAMAL CASE???LOWER CAMEL CASE

        return BeanUtil.toJson(params);
    }

    /**
     * ???????????????????????????????????? ??????id???formId????????????
     *
     * @param id
     * @param formId
     */
    protected void delFormFile(Long id, String formId) {
        logger.debug(String.format("????????????:%d, ????????????:%s, ??????!!", id, formId));
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
     * ????????????????????????
     *
     * @param islocked Y OR N
     * @param formId
     */
    protected void updateFormFileStatus(String islocked,String formId) {
        formFileRepository.updateFormFileStatusByFormId(islocked, formId);
    }

    /**
     * ???????????????????????????????????? ?????????????????????????????????????????? ??????????????????"C:/Temp/download/????????????"
     *
     * @param id
     * @param formId
     * @return
     * @throws IOException
     */
    protected File downloadFile(Long id, String formId) throws IOException {
        logger.debug(String.format("????????????:%d, ????????????:%s, ??????!!", id, formId));

        FormFileEntity formFileEntity = formFileRepository.findByIdAndFormId(id, formId);
        File file = new File(genFilePath(formFileEntity.getName()));

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        file = FileUtil.getFileFromByte(formFileEntity.getData(), file.getAbsolutePath());

        return file;
    }

    /**
     * ??????????????????
     * @param formUserRecordEntity
     */
    protected void delFormUserRecord(FormUserRecordEntity formUserRecordEntity){
        if (formUserRecordEntity != null) {
            formUserRecordRepository.deleteById(formUserRecordEntity.getId());
        }
    }

    /**
     * ???????????????????????????
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
     * ??????????????????????????????????????????????????????
     * (???????????????)
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
     * ???????????????FormVerifyLogRepository
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
     * ???????????????FormVerifyLogRepository
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
     * ?????????????????????????????????
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
     * ??????????????????/???????????????????????????</br>
     * 1. ???0???=????????????</br>
     * 2. ???1???=????????????
     *
     * @param vo isNextLevel ????????????/????????????
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
        
        logger.info("???????????? : " + start);
        logger.info("???????????? : " + ended);
        logger.info("???????????? : " + limits);
        logger.info("???????????? : " + isNextLevel);

        return limits;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isAcceptable (formVO vo, FormVerifyLogEntity verifyLog, ResourceEnum resource, SysUserVO userInfo) {
        boolean result = false;

        if(verifyLog != null) {
            // ????????????????????????
            if (StringUtils.isBlank(verifyLog.getUserId())) {
                Map<String, Object> params = new HashMap<>();
                params.put("formId", vo.getFormId());
                params.put("verifyLevel", verifyLog.getVerifyLevel());
                BaseFormVO acceptable = jdbcRepository.queryForBean(resource, params, BaseFormVO.class);
                
                if (acceptable != null) {
                    if (sysUserService.isPic(acceptable.getGroupId())) {// ???????????????????????????????????????????????????????????????
                        result = StringUtils.equals(userInfo.getUserId(),vo.getUserSolving());
                    } else if (StringUtils.isNotBlank(userInfo.getGroupId())) {// ????????????
                        result = userInfo.getGroupId().equalsIgnoreCase(acceptable.getGroupId());
                    }
                }
            } else if (StringUtils.isNotBlank(userInfo.getUserId())) {// ?????????????????????
                result = userInfo.getUserId().equalsIgnoreCase(verifyLog.getUserId());
            }
        }

        return result;
    }

    /**
     * ????????????????????????????????????
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
     * ??????????????????????????????????????????????????????
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
     * ?????????????????????????????????????????????
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
     * ???????????????????????????????????????
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
     * ?????????????????????????????????????????????
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
            //?????????????????????????????????????????????,?????????????????????????????????,?????????????????????,??????????????????????????????
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
     * ????????????
     * @param vo
     */
    protected void closeForm (FormEntity form) {
        form.setFormStatus(FormEnum.CLOSED.name());
        form.setProcessStatus(FormEnum.CLOSED.name());
        formRepo.save(form);
    }

    /**
     * ??????????????????
     * @param vo
     */
    protected void closeFormForImmdiation (formVO vo) {
       closeFormForImmdiation(vo, FormEnum.CLOSE_FORM);
    }

    /**
     * ???????????????????????????, ?????????????????????
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
     * ????????????????????????
     * @return String
     */
    protected String verifyIsFormClose(formVO vo) {
        if(FormEnum.REVIEW.name().equals(vo.getVerifyType())) {//?????????????????????
            SysUserVO userVo = this.fetchLoginUser();
            //????????????????????????ID,????????????????????????ID????????????,?????????????????????????????????
            if(userVo.getGroupId().equals(vo.getGroupId()) && StringConstant.SHORT_YES.equals(vo.getIsCloseForm())) {
                return StringConstant.SHORT_YES;
            }
        }
        return StringConstant.SHORT_NO;
    }

    /**
     * ????????????????????????????????????
     * ??????????????????VO???
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
     * ?????? Form id ?????????????????????
     * @param formId
     * @return
     */
    protected String getFormUrl(String formId){
        // ?????? request ?????? URL ????????? slash ????????????
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        URI uri = builder.build().toUri();
        StringBuilder hyperLink = new StringBuilder();
        String[] urlSplit = uri.toString().split(StringConstant.SLASH);
        // ?????? Url prefix
        // Example : http://localhost:8080/ISWP
        for (int i = 0; i != 4; i++) {
            hyperLink.append(urlSplit[i] + StringConstant.SLASH);
        }
        // ?????? URL : http://localhost:8080/ISWP/formSearch/search/{FormId}
        hyperLink.append("formSearch/search/" + formId);

        return hyperLink.toString();
    }

    /**
     * ???????????????????????????????????????????????????
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
     * ??????VerifyType, FormId, VerifyLevel?????????????????????
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
     * ??????????????????????????????????????????????????????<br>
     * P.S. ????????????????????????, ??????????????????????????????????????????[????????????]??????
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
        
        // ????????????????????????????????????????????????or?????????????????????
        if (verifyEnum.toString().equals(closeForm)) {
            if (UserEnum.DEPUTY_MANAGER.wording().equals(groupSolving)) {
                wording = getMessage("form.common.is.close.form.direct2.substitute");
            } else if (UserEnum.VICE_DIVISION_CHIEF.wording().equals(groupSolving.split("_")[1])) {
                wording = getMessage("form.common.is.close.form.vice.substitute");
            }
        }
        
        // ?????????????????????????????????????????????wording?????????????????????
        if (FormEnum.VSC_MODIFY.name().equals(verifyResult) && groupSolving.equals("????????????")) {
            formLog.setGroupSolving(userInfo.getGroupName());
        }
        
        if (FormEnum.DEPRECATED.equals(verifyEnum)) {
            wording = String.format(verifyEnum.processStatus(), formLog.getGroupSolving());
        }
        
        formLog.setVerifyResultWording(wording);
    }

    /**
     * ??????????????????????????????????????????
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
     * ????????????mail????????????
     *
     * @param vo
     * @param back
     */
    protected void setBackLevelMailUserId(formVO vo,FormVerifyLogEntity back) {
        // ????????????????????????????????????( ?????????????????????????????? )
        String userId = "";

        if(!Objects.isNull(back)) {
            userId = back.getUserId();
        } else {
            //??????back??????,???????????????createBy??????
            FormEntity formEntity = formRepo.findByFormId(vo.getFormId());
            if(!Objects.isNull(formEntity)) {
                userId = formEntity.getCreatedBy();
            } else {
                //?????????formEntity??????????????????,????????????????????????id
                userId = fetchLoginUser().getUserId();
            }
        }
        vo.setUserId(userId);
    }

    /**
     * ?????????????????????
     *
     * @param vo
     * @return boolean
     */
    protected String isCreateQuestionIssue(formVO vo) {
        if(StringConstant.SHORT_YES.equalsIgnoreCase(vo.getIsAddQuestionIssue())) {
            //???????????????????????????,?????????????????????????????????,???????????????
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
     * ???????????????????????????,??????????????????????????????????????????
     *
     * @param vo
     * @return String
     */
    protected String isApprover(formVO vo) {
        if(StringConstant.SHORT_YES.equalsIgnoreCase(vo.getIsApprover())) {
            String userSolving = vo.getUserSolving();
            String currentUserId = fetchLoginUser().getUserId();

            //???????????????=???????????????????????????=?????????????????????????????????????????????????????????
            if(userSolving.equalsIgnoreCase(currentUserId)) {
                return StringConstant.SHORT_YES;
            }
        }

        return StringConstant.SHORT_NO;
    }

    /**
     * ??????formId,?????????????????????????????????????????????????????????
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
     * ????????????????????????????????????<br>
     * ???????????????????????????????????????????????????????????????????????????????????????????????????<br>
     * ???????????????????????????????????????
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
     * ??????FormInternalProcessStatus
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
            logger.warn("Form_id : %s, ??????????????????????????????!!", baseFormVO.getFormId());
            return;
        }
        FormContentModifyLogEntity formContentModifyLogEntity = new FormContentModifyLogEntity();
        formContentModifyLogEntity.setFormId(baseFormVO.getFormId());
        formContentModifyLogEntity.setContents(baseFormVO.toString());
        formContentModifyLogEntity.setUpdatedAt(baseFormVO.getUpdatedAt());
        formContentModifyLogEntity.setUpdatedBy(baseFormVO.getUpdatedBy());
        // ??????????????? log ?????????????????????????????????????????????????????? Exception ???????????? log ????????????
        try {
            formContentModifyLogRepository.save(formContentModifyLogEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ??????????????????????????????????????????<br>
     * (1)??????????????????????????????????????????????????????????????? (???????????????:?????????????????????)<br>
     * (2)????????????log
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
                            sendMail("???ISWP?????????" + formId + " ?????????????????????????????????", mailList, params);
                            recordMailLog(formId, loginUser, BeanUtil.toJson(mailList));
                            logger.info("????????????????????? : " + BeanUtil.toJson(mailList));
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
     * ADMIN?????????????????????????????????????????????:<br>
     * (1)?????????????????????(?????????????????????)????????????????????????<br>
     * (2)??????(?????????????????????)????????????????????????????????????????????????AP??????????????????????????????<br>
     * (3)??????(?????????)????????????????????????????????????????????????AP??????????????????????????????<br>
     * (4)????????????:??????????????????????????????????????????????????????????????????isScopeChanged????????????=Y?????????????????????N<br>
     *    ??????:?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
        
        //?????????(????????????????????????)
        List<String> chgIds = allList.stream()
                .filter(it -> FormEnum.CHG.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        //?????????
        List<String> countersignedIds = allList.stream()
                .filter(it ->
                    FormEnum.Q_C.name().equals(it.getFormClass()) ||
                    FormEnum.SR_C.name().equals(it.getFormClass()) ||
                    FormEnum.INC_C.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        
        //AP?????????
        List<String> apJobIds = allList.stream()
                .filter(it -> FormEnum.JOB_AP.name().equals(it.getFormClass()))
                .map(it -> it.getFormId())
                .collect(Collectors.toList());
        
        Date ect = vo.getEct();
        
        //?????????????????????(?????????????????????)????????????????????????
        if (CollectionUtils.isNotEmpty(countersignedIds)){
            formDateRepo.updateMectByFormIdIn(ect, countersignedIds);
        }
        
        if (CollectionUtils.isNotEmpty(chgIds)){
            formDateRepo.updateCctByFormIdIn(ect, chgIds);
            
            //??????????????????:??????????????????-->???????????????????????????????????????????????????????????????IsScopeChanged=Y
            if (StringConstant.SHORT_YES.equals(isDifferentScope)) { 
                formChgDetailRepo.updateIsScopeChangedByFormIdIn(StringConstant.SHORT_YES, chgIds);
            } else {
                formChgDetailRepo.updateIsScopeChangedByFormIdIn(StringConstant.SHORT_NO, chgIds);
            }
        }
        
        //?????????????????????(AP?????????)??????????????????
        if (CollectionUtils.isNotEmpty(apJobIds)){
            formJobDateRepo.updateCctByFormIdIn(ect, apJobIds);
        }
        
        if (isVice) {
            ectExtendedSendMail(vo, countersignedIds);
        }

        // ??????????????????????????????
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

            // ??????Form: isExtended=Y
            formRepo.updateIsExtendedByFormId(StringConstant.SHORT_YES, formId);
        }
    }
    
    /**
     * ???????????????????????????
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
     * ????????????????????????????????????????????? mail ??????
     * ???:?????????????????????????????????
     * @param vo
     * @param tParams ????????????????????????????????????
     */
    private synchronized void sendFormProcessMail (formVO vo, Map<String, Object> tParams) throws Exception {
        String formURL = MapUtils.getString(tParams, "formURL");
        SysUserVO loginUser = (SysUserVO) MapUtils.getObject(tParams, "loginUser");
        FormEntity form = formRepo.findByFormId(vo.getFormId());
        String verifyType = FormEnum.valueOf(vo.getVerifyResult()).verifyType();
        String formName = getMessage(FormEnum.valueOf(vo.getFormClass()).wording());
        logger.info(this.getMessage("form.mail.send.start.log", new String[]{formName, verifyType}));

        // ??? FORM_VERIFY_LOG ??????????????????????????????
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_VERIFY_LOGS_LATEST_FORM_RECORD");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        List<BaseFormVO> nextLevels = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        String pendingGroupId = getPendingGroupId(vo, nextLevels, form, loginUser);

        // ????????????????????????????????????
        if (StringUtils.isBlank(pendingGroupId) &&
                !FormEnum.DEPRECATED.name().equals(vo.getVerifyResult()) &&
                !FormEnum.VSC_MODIFY.name().equals(vo.getVerifyResult())) {
            if (CollectionUtils.isNotEmpty(nextLevels)) {
                recordMailLog(vo.getFormId(),
                        loginUser, nextLevels.get(0).getDetailId());
                logger.error("??????????????????????????????");
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
        boolean isC = StringUtils.contains(vo.getFormClass(), "_C");     // ?????????
        boolean isJob = StringUtils.contains(vo.getFormClass(), "JOB_"); // ?????????/???????????????
        boolean isParallel = yes.equals(vo.getIsParallel());             // ????????????
        List<LdapUserEntity> mailTo = new ArrayList<>();
        
        // ???????????????????????????
        if (!FormEnum.VSC_MODIFY.name().equals(vo.getVerifyResult())) {
            if ((isDirect1 || isDirect2) &&
                    !FormEnum.CLOSED.name().equals(vo.getVerifyResult())) {
                return; // TODO ????????????, ?????????????????????/??????
            }
        }

        // ????????????????????????????????? mail ????????? mail ??????
        switch (FormEnum.valueOf(vo.getVerifyResult())) {
            case SENT:
            case VSC_PROXY1:
            case AGREED:
                pendingGroup = sysGroupRepository.findByGroupId(pendingGroupId);
                sysGroupId = String.valueOf(pendingGroup.getSysGroupId());
                mailTemplate = isPic ? MailTemplate.AGREED_PIC.src() : MailTemplate.AGREED.src();
                i18n = isPic ? "form.mail.handle.group.subject" : "form.mail.reviewer.group.subject";// ?????????/?????????????????????
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
                } else {// ???????????????
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
                
                //??????????????????
                sendViceModifyToCountersigned(vo, mailSubject, formName, tParams);
                
                return;
                
            default:
                if (FormEnum.VSC_PROXY2.name().equals(vo.getVerifyResult())) {
                    logger.warn("????????????????????????");
                } else {
                    logger.warn("??????????????????????????????");
                }
                
                return;
        }
        
        // ???????????????????????????????????????????????????????????????????????????
        if (StringUtils.isBlank(vo.getUserCreated()) ||
                StringUtils.isBlank(vo.getDivisionCreated())) {
            vo.setCreateTime(form.getCreateTime());
            vo.setUserCreated(form.getUserCreated());
            vo.setDivisionCreated(form.getDivisionCreated());
        }

        sendMail(vo, formURL, formName, verifyType, userSolving, mailSubject, mailTemplate, loginUser, pendingGroup, mailTo);
    }
    
    /**
     * ????????????????????????????????????????????????<br>
     * 1. ???????????????????????????<br>
     * 2. ???????????????????????????<br>
     * 3. ???????????????
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
        
        //?????????????????????????????????mail
        List<SysGroupEntity> es = sysGroupRepository.findByDepartmentIdAndDivisionAndGroupIdContaining(sp[0], sp[1], vo.getGroupSolving());
        //???????????????????????????????????????
        LdapUserEntity handler = ldapUserRepository.findByUserIdAndIsEnabled(userSolving, StringConstant.SHORT_YES);

        Map<String, Object> mails = jdbcRepository.queryForMap(mailResource, param);
        mails.put("picMails", handler.getEmail()); //??????????????????????????????
        
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
     * ????????????????????????????????????????????????????????????????????????
     * @param vo
     * @param mailSubject
     * @throws Exception
     */
    protected void sendViceModifyToCountersigned (formVO vo, String mailSubject, String formName, Map<String, Object> tParams) throws Exception {
        String formURL = MapUtils.getString(tParams, "formURL");
        SysUserVO loginUser = (SysUserVO) MapUtils.getObject(tParams, "loginUser");
        
        //??????????????????(?????????)
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
            
            //????????????????????????????????????
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
                            logger.info("?????????????????????????????? : " + BeanUtil.toJson(mailList));
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
        ldapUsers.removeAll(Collections.singletonList(null));// ???????????????????????????
        
        if (CollectionUtils.isNotEmpty(ldapUsers)) {
            String warn = "???%s???emill not found.";
            
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
            logger.info("????????????????????? : " + sentJson);
        }
    }

    // Template ??????????????????????????????
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

        params.put("formClass", formName);                                                     // ????????????
        params.put("user", user.getName());                                                    // ???????????????
        params.put("formId", vo.getFormId());                                                  // ????????????
        params.put("link", formURL);                                                           // ???????????????
        params.put("verifyComment", vo.getVerifyComment());                                    // ????????????
        params.put("modifyComment", vo.getModifyComment());                                    // ????????????
        params.put("userName", loginUser.getName());                                           // ???????????????????????????
        params.put("divisionCreated", vo.getDivisionCreated());                                // ????????????
        params.put("summary", getFormDetailInfo(vo.getFormId()).getSummary());                 // ??????
        params.put("sysTime", DateUtils.toString(new Date(), DateUtils.pattern12));            // ????????????
        params.put("createTime", DateUtils.toString(vo.getCreateTime(), DateUtils.pattern12)); // ????????????
        params.put("updatedAt", DateUtils.toString(vo.getUpdatedAt(), DateUtils.pattern12));   // ????????????

        // ????????????
        if (sysGroup != null) {
            params.put("group", sysGroup.getGroupName());
        }
        
        // ????????????
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

    // ?????????????????????????????????
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

    // ????????????/???????????????????????????????????????
    private String getPendingGroupId (formVO vo, List<BaseFormVO> nextLevels, FormEntity form, SysUserVO loginUser) {
        String nextGroup = null;
        
        if (CollectionUtils.isNotEmpty(nextLevels)) {// ????????????
            String formStatus = form.getFormStatus();
            BaseFormVO nextLevel = nextLevels.get(0);
            String detailId = nextLevel.getDetailId();
            String verifyType = nextLevel.getVerifyType();
            String verifyLevel = nextLevel.getVerifyLevel();
            boolean isC = StringUtils.contains(vo.getFormClass(), "_C");// ?????????
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

                // ?????????????????????????????????????????????????????????????????????????????????template
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

    // ??????????????????
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
     * ?????????????????????????????????
     * 
     */
    protected List<Map<String, Object>> getAllProcessList (formVO vo) {
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        
        //?????????????????????????????????????????????SQL???
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
     * ?????????
     * ?????????????????? ???????????????????????????????????????????????????????????????????????????????????????????????????????????????+?????????+???????????????
     * ?????????????????? ???REVIEW??????????????????????????????????????????????????????????????????????????????????????????????????????????????????+?????????+???????????????
     * ?????????????????? ???REVIEW??????????????????????????????????????????????????????????????????????????????????????????????????????????????????+?????????
     * 
     * ??????????????????????????????????????????????????????
     *  ????????????????????????????????? < AP???????????????????????????????????????or ??????????????????????????????(????????????)
     *  ????????????????????????????????? < SP????????????????????????????????? (????????????)
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
            long lastTime = 0L;// ???????????????????????????????????????
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
