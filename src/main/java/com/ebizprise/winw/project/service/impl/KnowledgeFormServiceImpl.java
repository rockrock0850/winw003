package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormInfoKlDetailEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.form.KnowledgeEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormInfoDateRepository;
import com.ebizprise.winw.project.repository.IFormInfoKlDetailRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.singleton.FormIdHandler;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.KnowledgeFormVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 知識庫 新增/修改等作業
 * @author adam.yeh
 */
@Transactional
@Service("knowledgeFormService")
public class KnowledgeFormServiceImpl extends BaseFormService<KnowledgeFormVO> implements IBaseFormService<KnowledgeFormVO> {

    @Autowired
    private JdbcRepositoy jdbcRepo;
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormInfoDateRepository formDateRepo;
    @Autowired
    private IFormInfoKlDetailRepository formDetailRepo;

    /**
     * 
     * @param vo
     * @author adam.yeh
     * @return 
     */
    public List<KnowledgeFormVO> list (KnowledgeFormVO vo) {
        List<KnowledgeFormVO> voList = new ArrayList<>();

        if (vo.getIsDetails()) {
            List<String> solutions = Arrays.asList(StringUtils.split(vo.getSolutions(), ","));
            
            for (String formId : solutions) {
                KnowledgeFormVO v = new KnowledgeFormVO();
                BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), v);
                voList.add(v);
            }
        } else if (StringUtils.isBlank(vo.getSolutions())) {
            Conditions conditions = new Conditions();
            ResourceEnum resource = ResourceEnum.SQL_FORM_SEARCH.getResource("FIND_FORM_KL_DETAIL_BY_CONDITION");
            
            // 表單類別
            if (StringUtils.isNotBlank(vo.getFormClass())) {
                conditions.and().equal("f.FormClass", vo.getFormClass());
            }
            
            // 摘要
            if (StringUtils.isNotBlank(vo.getSummary())) {
                conditions.and().like("FIDTL.Summary", vo.getSummary());
            }
            
            // 徵兆
            if (StringUtils.isNotBlank(vo.getIndication())) {
                conditions.and().like("FIDTL.Indication", vo.getIndication());
            }
            
            // 原因
            if (StringUtils.isNotBlank(vo.getReason())) {
                conditions.and().like("FIDTL.Reason", vo.getReason());
            }
            
            // 處理方案
            if (StringUtils.isNotBlank(vo.getProcessProgram())) {
                conditions.and().like("FIDTL.ProcessProgram", vo.getProcessProgram());
            }

            // 啟用狀態
            if (StringUtils.isNotBlank(vo.getProcessProgram())) {
                conditions.and().like("FIDTL.IsEnabled", vo.getIsEnabled());
            }
            
            voList = jdbcRepo.queryForList(resource, conditions, KnowledgeFormVO.class);  
        }
        
        return voList;
    }

    @Override
    public void create (KnowledgeFormVO vo) {
        String formId = vo.getFormId();
        String flowName = getMessage(KnowledgeEnum.valueOf(vo.getFormClass()).flowName());
        List<FormEntity> knowledges = formRepo.findBySourceIdAndFormClass(formId, FormEnum.KL.name());

        
        if (knowledges.size() == 0) {
            vo.setFormId(null);
        } else {// 一張單只能開一張知識庫, 否則都直接更新內容即可。
            vo.setFormId(knowledges.get(0).getFormId());
            vo.setSolutions(formDetailRepo.findByFormId(knowledges.get(0).getFormId()).getSolutions());
        }
        
        vo.setSourceId(formId);
        vo.setFlowNameDisplay(flowName);
        vo.setFlowName(vo.getFormClass());
        vo.setFormClass(FormEnum.KL.name());
        vo.setIsEnabled(vo.getIsSuggestCase());
        vo.setFormStatus(FormEnum.CLOSED.name());

        vo.setId(null);
        vo.setStatusWording(null);
        vo.setFormStatusWording(null);
        vo.setProcessStatusWording(null);
        vo.setEct(null);
        vo.setAct(null);
        vo.setIsSpecial(null);
        vo.setReportTime(null);
        vo.setObservation(null);
        vo.setSpecialEndCaseType(null);
        
        mergeFormInfo(vo);
    }

    @ModLog
    @Override
    public void getFormInfo (KnowledgeFormVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_INFO_BY_CONDITIONS");
        Conditions conditions = new Conditions();
        conditions.and().equal("F.FormId", vo.getFormId());
        
        if (!StringUtils.isBlank(vo.getVerifyType()) && !isClosed(vo.getFormId())) {
            conditions.and().isNull("FVL.CompleteTime");
            conditions.and().equal("FVL.VerifyType", vo.getVerifyType());
        }
        
        KnowledgeFormVO formInfo = jdbcRepository.queryForBean(resource, conditions, KnowledgeFormVO.class);

        vo.setFormWording(getMessage(FormEnum.KL.wording()));
        
        // 初始化表單基本資料
        if (formInfo == null) {
            Date today = new Date();
            resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INIT_FORM_INFO_BY_USER_ID");
            Map<String, Object> params = new HashMap<>();
            params.put("userId", UserInfoUtil.loginUserId());
            formInfo = jdbcRepository.queryForBean(resource, params, KnowledgeFormVO.class);
            
            BeanUtil.copyProperties(formInfo, vo);
            vo.setProcessStatus(FormEnum.PROPOSING.name());
            vo.setStatusWording(FormEnum.PROPOSING.status());
            vo.setFormStatusWording(FormEnum.PROPOSING.formStatus(formInfo.getGroupName()));
            vo.setProcessStatusWording(
                    String.format(FormEnum.PROPOSING.processStatus(), formInfo.getGroupName()));
            vo.setFormClass(FormEnum.KL.name());
            vo.setFormType(FormEnum.KL.formType());
            vo.setCreatedAt(today);
            vo.setCreatedBy(UserInfoUtil.loginUserId());
            vo.setUpdatedAt(today);
            vo.setUpdatedBy(UserInfoUtil.loginUserId());
            
            return;
        }
        
        BeanUtil.copyProperties(formInfo, vo);
        BeanUtil.copyProperties(getFormDetailInfo(vo.getFormId()), vo);
        
        FormEnum formStatus = FormEnum.valueOf(vo.getFormStatus());
        FormEnum processStatus = FormEnum.valueOf(vo.getProcessStatus());
        
        vo.setIsVerifyAcceptable(true);
        vo.setFormType(formStatus.formType());
        vo.setStatusWording(processStatus.status());
        vo.setFlowNameDisplay(getMessage(KnowledgeEnum.valueOf(vo.getFlowName()).flowName()));
    }

    @Override
    public void mergeFormInfo (KnowledgeFormVO vo) {
        Date today = new Date();
        String formId = vo.getFormId();
        String userId = UserInfoUtil.loginUserId();
        
        vo.setUpdatedBy(userId);
        vo.setUpdatedAt(today);
        
        if (StringUtils.isEmpty(formId)) {// 新增
            vo.setCreateTime(today);
            vo.setFormId(FormIdHandler.getInstance().next(FormEnum.KL));
            vo.setCreatedBy(userId);
            vo.setCreatedAt(today);
            
            FormEntity formPojo = new FormEntity();
            FormInfoDateEntity formDatePojo = new FormInfoDateEntity();
            FormInfoKlDetailEntity formDetailPojo = new FormInfoKlDetailEntity();

            BeanUtil.copyProperties(vo, formPojo);
            BeanUtil.copyProperties(vo, formDatePojo);
            BeanUtil.copyProperties(vo, formDetailPojo);

            formRepo.save(formPojo);
            formDateRepo.save(formDatePojo);
            formDetailRepo.save(formDetailPojo);
        } else {
            ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_KL.getResource("UPDATE_KL_FORM");
            Map<String, Object> params = BeanUtil.toMap(vo);
            jdbcRepository.update(resource, params);
        }
    }

    @Override
    public KnowledgeFormVO getFormDetailInfo (String formId) {
        KnowledgeFormVO vo = new KnowledgeFormVO();
        BeanUtil.copyProperties(formDetailRepo.findByFormId(formId), vo, new String[] {"updatedAt"});
        return vo;
    }

    @Override
    public void lockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_YES, formId);
    }

    @Override
    public void unlockFormFileStatus(String formId) {
        updateFormFileStatus(StringConstant.SHORT_NO, formId);
    }
    
    /**
     * 表單是否符合「加入知識庫」的條件
     * @param vo
     * @return
     */
    public boolean isEligible(KnowledgeFormVO vo) {
        return isSendFormToDirect1(vo);
    }
    
    @Override
    @Deprecated
    public String checkAttachmentExists(KnowledgeFormVO vo) {
        return null;
    }
    
    @Override
    @Deprecated
    public void ectExtended (KnowledgeFormVO vo) {
    }

    @Override
    @Deprecated
    public void sendToVerification (KnowledgeFormVO vo) {
    }
    
    @Override
    @Deprecated
    public void prepareVerifying(KnowledgeFormVO vo) {
    }

    @Override
    @Deprecated
    public void verifying (KnowledgeFormVO vo) {
    }
    
    @Override
    @Deprecated
    public void createVerifyCommentByVice(KnowledgeFormVO vo) {
    }

    @Override
    @Deprecated
    public boolean verifyStretchForm (KnowledgeFormVO vo, FormVerifyType type) {
        return false;
    }

    @Override
    @Deprecated
    public void deleteForm (KnowledgeFormVO vo) {
    }

    @Override
    @Deprecated
    public boolean isVerifyAcceptable (KnowledgeFormVO vo, SysUserVO userInfo) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFormClosed (KnowledgeFormVO vo) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isNewerDetailExist (KnowledgeFormVO vo) {
        return false;
    }

    @Override
    @Deprecated
    public void notifyProcessMail (KnowledgeFormVO vo) {
    }

    /**
     * 儲存處理方案的資料
     * 
     * @param vo
     */
    @Override
    @Deprecated
    public void saveProgram (KnowledgeFormVO vo) {
    }

    /**
     * 取得處理方案的資料
     * 
     * @param formId
     */
    @Override
    @Deprecated
    public KnowledgeFormVO getProgram (String formId) {
        return null;
    }
    
    /**
     * 副科/副理 表單直接結案
     * 
     * @param vo
     */
    @Override
    @Deprecated
    public void immediateClose (KnowledgeFormVO vo) {
    }

    @Override
    @Deprecated
    public KnowledgeFormVO newerProcessDetail (String division, FormEnum e) {
        return BeanUtil.fromJson(
                getRecentlyDetail(division, e), KnowledgeFormVO.class);
    }

    @Override
    @Deprecated
    public boolean isAllStretchDied (KnowledgeFormVO vo) {
        return false;
    }
    
    @Override
    @Deprecated
    public List<String> getFormCountsignList(String formId) {
        return null;
    }

    @Override
    @Deprecated
    protected String getReviewLastLevel (String detailId) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(KnowledgeFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(KnowledgeFormVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

}
