package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseProcessService;
import com.ebizprise.winw.project.entity.FormProcessEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.WorkingItemEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.approve.FormApproveApplyEnum;
import com.ebizprise.winw.project.enums.form.approve.FormApproveReviewEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormProcessRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.IWorkingItemRepository;
import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.service.startup.FormProcessHelper;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentBaseVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentWordingVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單流程管理 共用服務
 * 
 * The <code>FormProcessServiceImpl</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月28日
 */
@Transactional
@Service("formProcessBaseService")
public class FormProcessBaseServiceImpl extends BaseProcessService implements IFormProcessBaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(FormProcessBaseServiceImpl.class);
    
    @Autowired
    private ISysGroupRepository sysGroupRepository;
    
    @Autowired
    private IFormProcessRepository formProcessRepository;
    
    @Autowired
    private ISysGroupRepository sysGroupRepo;
    
    @Autowired
    private IWorkingItemRepository workingItemRepository;
    
    @Autowired
    private FormHelper formHelper;
    
    @Autowired
    private FormProcessHelper processHelper;

    @Override
    public List<FormProcessManagmentBaseVO> getFormProcessManagmentByCondition(FormProcessManagmentResultVO vo) {
        Conditions conditions = new Conditions();
        String formType = vo.getFormType();
        String processName = vo.getProcessName();
        String departmentIdAndDivision = vo.getDivision();//前端的Division的value是DepartmentId+科別組合而成的
        
        if(StringUtils.isNoneBlank(vo.getFormType())) {
            conditions.and().equal("T1.FormType", formType);
        }
        
        if(StringUtils.isNoneBlank(departmentIdAndDivision)) {
            conditions.and().equal("T1.DepartmentId", departmentIdAndDivision.split("-")[0]);
            conditions.and().equal("T1.Division", departmentIdAndDivision.split("-")[1]);
        }
        
        if(StringUtils.isNoneBlank(processName)) {
            conditions.and().like("T1.ProcessName", processName);
        }
        
        List<FormProcessManagmentBaseVO> rtnLs =  jdbcRepository.queryForList(ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_FORM_PROCESS_LIST_BY_CONDITION"),conditions, FormProcessManagmentBaseVO.class);

        if(CollectionUtils.isEmpty(rtnLs)) {
            logger.error("無法根據條件,查詢到FormProcessManagment 資訊");
        }
        
        return rtnLs;
    }

    @Override
    public boolean updateFormProcessStatusById(List<FormProcessManagmentBaseVO> voLs) {
        if(CollectionUtils.isNotEmpty(voLs)) {
            List<Map<String,Object>> sqlParams = new ArrayList<>();

            //然後再把本次需要更新的資料,進行更新
            for(FormProcessManagmentBaseVO target : voLs) {
                Map<String,Object> params = new HashMap<>();
                params.put("id", target.getId());
                params.put("formType", target.getFormType());
                params.put("departmentId",target.getDepartmentId());
                params.put("division",target.getDivision());
                params.put("isEnable", target.getIsEnable());
                
                sqlParams.add(params);
            }
            jdbcRepository.updateBatch(ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("UPDATE_FORM_PROCESS_STATUS_BY_CONDITION"), sqlParams);
        }
        
        return true;
    }

    @Override
    public List<HtmlVO> getSysGroupSelector() {
        List<HtmlVO> rtnLs = new ArrayList<>();
        Set<String> distinctSet = new HashSet<>();
        String manager = UserEnum.MANAGER.symbol();
        String division, departmentId, deparmentName;
        List<SysGroupEntity> dataLs = sysGroupRepository.findAll();
        
        for (SysGroupEntity entity : dataLs) {
            division = entity.getDivision();
            departmentId = entity.getDepartmentId();
            deparmentName = entity.getDepartmentName();
            
            if (StringUtils.isNotBlank(departmentId) && StringUtils.isNotBlank(division)) {
                if (manager.equals(division)) {
                    continue;
                }
                
                HtmlVO vo = new HtmlVO();
                String key = deparmentName + "-" + division;
                vo.setWording(key);
                vo.setValue(departmentId + "-" + division);
                
                if(!distinctSet.contains(key)) {
                    rtnLs.add(vo);
                    distinctSet.add(key);
                }
            }
        }
        
        return rtnLs;
    }
    
    @Override
    public List<HtmlVO> getSpGroupSelector() {
        List<HtmlVO> rtnLs = new ArrayList<>();
        Set<String> distinctSet = new HashSet<>();
        String manager = UserEnum.MANAGER.symbol();
        String spGroup;
        List<WorkingItemEntity> dataLs = workingItemRepository.findAll();
        
        for (WorkingItemEntity entity : dataLs) {
            spGroup = entity.getSpGroup();
            
            if (StringUtils.isNotBlank(spGroup)) {
                if (manager.equals(spGroup)) {
                    continue;
                }
                
                HtmlVO vo = new HtmlVO();
                String key = spGroup;
                vo.setWording(key);
                vo.setValue(key);
                
                if(!distinctSet.contains(key)) {
                    rtnLs.add(vo);
                    distinctSet.add(key);
                }
            }
        }
        
        return rtnLs;
    }

    @Override
    public List<HtmlVO> getSysGroupIdByDeptIdAndDivision(FormProcessManagmentBaseVO baseVo) {
        List<SysGroupEntity> dataLs = sysGroupRepository.findByDepartmentIdAndDivision(baseVo.getDepartmentId(),baseVo.getDivision());
        List<HtmlVO> rtnLs = new ArrayList<>();
        
        for(SysGroupEntity target : dataLs) {
            String groupId = target.getGroupId();
            String groupName = target.getGroupName();
            
            HtmlVO vo = new HtmlVO();
            vo.setValue(groupId);
            vo.setWording(groupName);
            
            rtnLs.add(vo);
        }
        
        return rtnLs;
    }
    
    @Override
    public List<HtmlVO> getSysGroupIdByDeptIdAndDivisionWithManagment(FormProcessManagmentBaseVO baseVo) {
        List<SysGroupEntity> dataLs = null;

        int formType = baseVo.getFormType();
        String departmentId = baseVo.getDepartmentId();
        String division = baseVo.getDivision();
        Integer[] allowFormType = {1,3,5,7,8};//1,3,5,7,8代表需求單,問題單,事件單,變更單以及工作單的formType,只有這些表單才需要撈取經理的群組
        
        //防呆,防止不為上述formType的資料跑建立撈經理的群組資訊
        if(Arrays.asList(allowFormType).indexOf(formType) != -1) {
            dataLs = sysGroupRepository.findByDepartmentIdAndDivisionWithManagment(departmentId, division, "Manager");
        } else {
            dataLs = sysGroupRepository.findByDepartmentIdAndDivision(departmentId ,division);
        }
        
        List<HtmlVO> rtnLs = new ArrayList<>();
        
        for(SysGroupEntity target : dataLs) {
            String groupId = target.getGroupId();
            String groupName = target.getGroupName();
            
            HtmlVO vo = new HtmlVO();
            vo.setValue(groupId);
            vo.setWording(groupName);
            
            rtnLs.add(vo);
        }
        
        return rtnLs;
    }

    @Override
    public FormProcessManagmentBaseVO getFormProcessById(Long id) {
        Optional<FormProcessEntity> entity = formProcessRepository.findById(id);
        FormProcessManagmentBaseVO rtnVo = new FormProcessManagmentBaseVO();

        BeanUtil.copyProperties(entity.get(),rtnVo);
        
        return rtnVo;
    }

    @Override
    public List<HtmlVO> getFormTypeSelector() {
        List<HtmlVO> selector = new ArrayList<HtmlVO>();
        HtmlVO vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.SR.formType()));
        vo.setWording(getMessage("requirement.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.SR_C.formType()));
        vo.setWording(getMessage("requirement.countersigned.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.Q.formType()));
        vo.setWording(getMessage("question.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.Q_C.formType()));
        vo.setWording(getMessage("question.countersigned.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.INC.formType()));
        vo.setWording(getMessage("event.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.INC_C.formType()));
        vo.setWording(getMessage("event.countersigned.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.CHG.formType()));
        vo.setWording(getMessage("change.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.JOB.formType()));
        vo.setWording(getMessage("work.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.JOB_C.formType()));
        vo.setWording(getMessage("work.countersigned.form"));
        selector.add(vo);
        
        vo = new HtmlVO();
        vo.setValue(String.valueOf(FormEnum.BA.formType()));
        vo.setWording(getMessage("batchinterrupt.form"));
        selector.add(vo);
        
        return selector;
    }

    @Override
    public List<BaseFormProcessManagmentDetailVO> getApplySigningList (BaseFormProcessManagmentDetailVO vo) {
        ResourceEnum resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
        Map<String, Object> currentLevel = getLevelInfo(vo, resource);
        return getSigningList(vo, currentLevel);
    }

    @Override
    public List<BaseFormProcessManagmentDetailVO> getReviewSigningList (BaseFormProcessManagmentDetailVO vo) {
        ResourceEnum resource = formHelper.getReviewInfoResource(FormEnum.valueOf(vo.getFormClass()));
        Map<String, Object> currentLevel = getLevelInfo(vo, resource);
        return getSigningList(vo, currentLevel);
    }

    @Override
    public FormProcessManagmentBaseVO getFormProcessByCondition(FormProcessManagmentBaseVO vo) {
        FormProcessEntity entiey = formProcessRepository.findTop1ByFormTypeAndDivisionAndDepartmentIdAndIsEnable(vo.getFormType(), vo.getDivision(), vo.getDepartmentId(), StringConstant.SHORT_YES);
        if(entiey != null) {
            FormProcessManagmentBaseVO rtnVo = new FormProcessManagmentBaseVO();
            BeanUtil.copyProperties(entiey, rtnVo);
            
            return rtnVo;
        } else {
            return null;
        }
    }
    

    @Override
    public List<BaseFormProcessManagmentDetailVO> getCurrentSigningList(BaseFormProcessManagmentDetailVO vo) {
        String groupId = "";
        String processOrder = "";
        ResourceEnum resource = null;
        Map<String, Object> currentLevel = new HashMap<>();
        
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
                break;

            case REVIEW:
                resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
                break;

            default:
                break;
        }
        
        currentLevel = getLevelInfo(vo, resource);
        groupId = MapUtils.getString(currentLevel, "GroupId");
        processOrder = MapUtils.getString(currentLevel, "ProcessOrder");
        SysGroupEntity sysGroupEntity = sysGroupRepo.findByGroupId(groupId);
        
        BaseFormProcessManagmentDetailVO rtnVo = new BaseFormProcessManagmentDetailVO();
        rtnVo.setGroupId(groupId);
        rtnVo.setGroupName(sysGroupEntity.getGroupName());
        rtnVo.setProcessOrder(Integer.parseInt(processOrder));
        
        List<BaseFormProcessManagmentDetailVO> rtnLs = new ArrayList<BaseFormProcessManagmentDetailVO>();
        rtnLs.add(rtnVo);
        
        return rtnLs;
    }
    
    @Override
    public List<BaseFormProcessManagmentDetailVO> getLastLevelSigningList(String userTitleCode,BaseFormProcessManagmentDetailVO vo) {
        String groupName = "";
        String processOrder = "";
        ResourceEnum resource = null;
        Map<String, Object> currentLevel = new HashMap<>();
        
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                groupName = "無法退關";
                resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
                break;

            case REVIEW:
                groupName = getCloseFormDesc(userTitleCode, vo);
                resource = formHelper.getApplyInfoResource(FormEnum.valueOf(vo.getFormClass()));
                break;

            default:
                break;
        }
        
        currentLevel = getLevelInfo(vo, resource);
        processOrder = MapUtils.getString(currentLevel, "ProcessOrder");
        
        BaseFormProcessManagmentDetailVO rtnVo = new BaseFormProcessManagmentDetailVO();
        rtnVo.setGroupId("EMPTY");
        rtnVo.setGroupName(groupName);
        rtnVo.setProcessOrder(StringUtils.isNotBlank(processOrder) ? Integer.parseInt(processOrder) : 0);
        
        List<BaseFormProcessManagmentDetailVO> rtnLs = new ArrayList<BaseFormProcessManagmentDetailVO>();
        rtnLs.add(rtnVo);
        
        return rtnLs;
    }
    

    @Override
    public List<BaseFormProcessManagmentDetailVO> overwriteSigningListGroupName (
            List<BaseFormProcessManagmentDetailVO> signingList,
            String usertitleCode,
            BaseFormProcessManagmentDetailVO detailVO) {
        //若為退關,則在後面拼上_BACK,可取得退關的對應描述
        if(!detailVO.getIsNextLevel()) {
            usertitleCode = usertitleCode + "_BACK";
        }
        
        changeVerifyType(detailVO);
        
        if(FormEnum.APPLY.name().equals(detailVO.getVerifyType())) {//若為申請流程
            FormApproveApplyEnum approveEnum = FormApproveApplyEnum.valueOf(usertitleCode.toUpperCase());
            for(BaseFormProcessManagmentDetailVO target : signingList) {
                String groupId = target.getGroupId();
                String userTitleCode = getUserTitileCode(groupId);
                String groupName = "";
                switch(UserEnum.valueOf(userTitleCode)) {
                    case PIC:
                        groupName = approveEnum.getPic();
                        break;
                    case VSC:
                        groupName = approveEnum.getVsc();
                        break;
                    case SC:
                        groupName = approveEnum.getSc();
                        break;
                    default:
                        break;
                }
                
                target.setGroupName(groupName);
            }
        } else if(FormEnum.REVIEW.name().equals(detailVO.getVerifyType())){//若為審核流程
            FormApproveReviewEnum approveEnum = FormApproveReviewEnum.valueOf(usertitleCode.toUpperCase());
            for(BaseFormProcessManagmentDetailVO target : signingList) {
                String groupId = target.getGroupId();
                String userTitleCode = getUserTitileCode(groupId);
                String groupName = "";
                
                switch(UserEnum.valueOf(userTitleCode)) {
                    case PIC:
                        groupName = approveEnum.getPic();
                        break;
                    case VSC:
                        groupName = approveEnum.getVsc();
                        break;
                    case SC:
                        groupName = approveEnum.getSc();
                        break;
                    case DIRECT1:
                        groupName = approveEnum.getDirect1();
                        break;
                    case DIRECT2:
                        groupName = approveEnum.getDirect2();
                        break;
                    default:
                        break;
                }
                
                target.setGroupName(groupName);
            }
        }
        
        return signingList;
    }
    
    @Override
    public String getUserTitileCode (String groupId) {
        SysUserVO vo = new SysUserVO();
        Map<String, String> mapping = processHelper.getGIdmDivision();
        String division = MapUtils.getString(mapping, groupId);
        
        vo.setGroupId(groupId);
        vo.setDivision(division);
        
        return UserInfoUtil.getUserTitleCode(vo);
    }

    @Override
    public void getProcessWording (List<BaseFormProcessManagmentDetailVO> signingList, FormEnum type,BaseFormProcessManagmentDetailVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_FROM_PROCESS_WORDING");
        try {
            for(BaseFormProcessManagmentDetailVO voo : signingList) {
                Conditions condi = new Conditions();
                condi.and().equal("wording.DetailId",voo.getDetailId());
                condi.and().equal("wording.[Type]",type.toString());
                condi.and().equal("wording.ProcessOrder",vo.getVerifyLevel());
                condi.and().equal("wording.WordingLevel",String.valueOf(voo.getProcessOrder()));
                FormProcessManagmentWordingVO bean = jdbcRepository.queryForBean(resource, condi, FormProcessManagmentWordingVO.class);
                voo.setWording(bean.getWording());
            }
        } catch (Exception e) {
            logger.error("關卡文字錯誤不處理只印LOG");
        }
    }
    
    private Map<String, Object> getLevelInfo (BaseFormProcessManagmentDetailVO vo, ResourceEnum resource) {
        int order = vo.getProcessOrder();
        boolean isChangeVerifyType = vo.getProcessOrder() == 0;
        
        Map<String, Object> params = new HashMap<>();
        params.put("detailId", vo.getDetailId());
        
        /*
         * 若開始轉換流程則審核關卡會壓成0的狀態進行之後的邏輯控制。
         * 但流程中沒有第0關, 所以最少要取得第1關的資訊。
         */
        params.put("processOrder", isChangeVerifyType ? 1 : order);
        
        return jdbcRepository.queryForMap(resource, params);
    }

    private List<BaseFormProcessManagmentDetailVO> getSigningList (
            BaseFormProcessManagmentDetailVO vo, Map<String, Object> currentLevel) {
        List<Integer> limits = new ArrayList<>();
        boolean isNextLevel = vo.getIsNextLevel();
        List<BaseFormProcessManagmentDetailVO>  signingList = null;
        
        if (isNextLevel) {
            limits = calculateLimitNumber(vo, MapUtils.getInteger(currentLevel, "NextLevel"));
            signingList = getJumpingList(vo, limits, MapUtils.getInteger(currentLevel, "NextLevel"));
        } else {
            limits.add(MapUtils.getInteger(currentLevel, "BackLevel"));
            signingList = getBackingList(vo, limits);
        }
        
        return signingList;
    }

    private List<BaseFormProcessManagmentDetailVO> getJumpingList (
            BaseFormProcessManagmentDetailVO vo, List<Integer> limits, Integer jumping) {
        Conditions conditions = null;
        boolean isJobForm = isJobForm(vo);
        boolean isApJobForm = FormEnum.JOB_AP.name().equals(vo.getFormClass());
        boolean isChangeVerifyType = vo.getProcessOrder() == 0;
        List<BaseFormProcessManagmentDetailVO> jumpingList = null;
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        int maxOrder = getJobProcessMaxOrder(vo, formClass, verifyType, isJobForm, isChangeVerifyType);
        
        ResourceEnum limitList = formHelper.getLimitResource(formClass, verifyType);
        
        if (isJobForm) {
            conditions = new Conditions()
                    .and().equal("F.FormId", vo.getFormId())
                    .and().equal("PROCESS.DetailId", vo.getDetailId())
                    .and().gt("PROCESS.ProcessOrder", nullSafeByVerifyLevel(vo));
            
            if (isApJobForm) {
                conditions.and().unEqual("PERSON.Level", FormJobEnum.CSPERSON.name());
            }
            
            if (isChangeVerifyType) {
                conditions.and().ltEqual("PROCESS.ProcessOrder", String.valueOf(maxOrder));
            }
            
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            params.put("detailId", vo.getDetailId());
            params.put("verifyLevel", nullSafeByVerifyLevel(vo));
            params.put("processOrder", maxOrder);
            jumpingList = jdbcRepository.queryForList(limitList, conditions, params, BaseFormProcessManagmentDetailVO.class);
            subJumpingList(jumpingList, jumping);
        } else {
            conditions = new Conditions()
                    .and().equal("PROCESS.DetailId", vo.getDetailId())
                    .and().leftPT().gt("PROCESS.ProcessOrder", String.valueOf(limits.get(0)))
                    .and().ltEqual("PROCESS.ProcessOrder", String.valueOf(limits.get(1))).RightPT();
            jumpingList = jdbcRepository.queryForList(limitList, conditions, BaseFormProcessManagmentDetailVO.class);
        }
        
        return jumpingList;
    }

    private List<BaseFormProcessManagmentDetailVO> getBackingList (BaseFormProcessManagmentDetailVO vo, List<Integer> limits) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_BACK_LIMIT_LIST");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        params.put("verifyType", vo.getVerifyType());
        params.put("verifyLevel",
                vo.getVerifyLevel() == null ?
                null : Integer.valueOf(vo.getVerifyLevel()));
        //篩掉含有中文字的verifyLevel欄位資料，直接改.sql會出現亂碼
        Conditions conditions = new Conditions().notIn("VerifyLevel", stringLevels());
        List<BaseFormProcessManagmentDetailVO> pojoList =
                jdbcRepository.queryForList(resource, conditions, params, BaseFormProcessManagmentDetailVO.class);
        
        List<BaseFormProcessManagmentDetailVO> backList = new ArrayList<>();

        int size = 0;
        boolean isChangeType = vo.isChangeVerifyType();
        
        if(isChangeType) {
            size = 1;
        } else {
            size = pojoList.size() > limits.get(0) ? limits.get(0) : pojoList.size();
        }

        BaseFormProcessManagmentDetailVO backLevel = null;
        FormEnum formClass = FormEnum.valueOf(vo.getFormClass());
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        ResourceEnum limitList = formHelper.getLimitResource(formClass, verifyType);
        for (int i = 0; i < size; i++) {
            if (CommonStringUtil.isInteger(pojoList.get(i).getVerifyLevel())) {
                conditions = new Conditions()
                        .and().equal("PROCESS.DetailId", pojoList.get(i).getDetailId())
                        .and().equal("PROCESS.ProcessOrder", pojoList.get(i).getVerifyLevel());

                if (isJobForm(vo)) {
                    params = new HashMap<>();
                    params.put("formId", pojoList.get(i).getFormId());
                    params.put("detailId", pojoList.get(i).getDetailId());
                    params.put("verifyLevel", Integer.valueOf(pojoList.get(i).getVerifyLevel())-1);
                    params.put("processOrder", pojoList.get(i).getVerifyLevel());
                    backLevel = jdbcRepository.queryForBean(limitList, conditions, params, BaseFormProcessManagmentDetailVO.class);
                } else {
                    backLevel = jdbcRepository.queryForBean(limitList, conditions, BaseFormProcessManagmentDetailVO.class);
                }
                
                if (backLevel != null) {
                    backList.add(backLevel);
                }
            }
        }
        
        // 若為審核流程退回申請一定為true且只會有一筆資料,
        // 接著把processOrder壓成0, 直接利用現有邏輯退回去。
        if(isChangeType) {
            backList.get(0).setProcessOrder(0);
        }
        
        Collections.reverse(backList);
        
        return backList;
    }

    private String nullSafeByVerifyLevel (BaseFormProcessManagmentDetailVO vo) {
        return StringUtils.isBlank(vo.getVerifyLevel()) ?
                String.valueOf(vo.getProcessOrder()) : vo.getVerifyLevel();
    }
    
    // 去除可跳關卡之外的關卡
    private void subJumpingList (List<BaseFormProcessManagmentDetailVO> jumpingList, Integer jumping) {
        int size = jumpingList.size();
        
        if (!CollectionUtils.isEmpty(jumpingList)) {
            jumpingList.subList(jumping < size ? jumping : size, size).clear();
        }
    }

    private int getJobProcessMaxOrder (BaseFormProcessManagmentDetailVO vo, FormEnum formClass, FormEnum verifyType, boolean isJobForm, boolean isChangeVerifyType) {
        int maxOrder = 1;
        
        if (isJobForm && !isChangeVerifyType) {
            ResourceEnum resource = formHelper.getFormProcessMaxResource(formClass, verifyType);
            Map<String, Object> params = new HashMap<>();
            params.put("formId", vo.getFormId());
            params.put("detailId", vo.getDetailId());
            BaseFormProcessManagmentDetailVO max = jdbcRepository.queryForBean(resource, params, BaseFormProcessManagmentDetailVO.class);
            maxOrder = max.getProcessOrder();
        }
        
        return maxOrder;
    }

    private boolean isJobForm (BaseFormProcessManagmentDetailVO vo) {
        return (vo.getFormClass().indexOf("JOB") != -1);
    }

    @Deprecated
    @Override
    protected int fetchApplyProcessDetailProcessOrder(String detailId, String groupId) {
        return 0;
    }

    @Deprecated
    @Override
    protected int fetchReviewProcessDetailProcessOrder(String detailId, String groupId) {
        return 0;
    }
    
    //------------------------------PRIVATE METHOD-----------------------------
    /**
     * 若isChangeVerifyType為true,代表是需要從申請進關到審核或審核退關到申請,直接調整detailVO VerifyType的值
     * 
     * @param detailVO
     */
    private void changeVerifyType(BaseFormProcessManagmentDetailVO detailVO) {
        if(detailVO.isChangeVerifyType()) {
            if(FormEnum.APPLY.name().equals(detailVO.getVerifyType())) {
                detailVO.setVerifyType(FormEnum.REVIEW.name());
            } else if(FormEnum.REVIEW.name().equals(detailVO.getVerifyType())) {
                detailVO.setVerifyType(FormEnum.APPLY.name());
            }
        }
    }
    
    /**
     * 以登入者職位,取得對應表單結案文字描述
     * 
     * @param userTitleCode
     * @param vo 
     * @return String
     */
    private String getCloseFormDesc(String userTitleCode, BaseFormProcessManagmentDetailVO vo) {
        String result = "";
        boolean isBaForm = FormEnum.BA.name().equals(vo.getFormClass());
        
        switch (UserEnum.valueOf(userTitleCode)) {
            case PIC:
                result = UserEnum.PIC.symbol();
                break;
                
            case VSC:
                if (isBaForm) {
                    result = getMessage("form.common.is.close.form.vice1");
                } else {
                    result = UserEnum.VSC.symbol();
                }
                
                break;
                
            case SC:
                result = UserEnum.SC.symbol();
                break;
                
            case DIRECT1:
                result = UserEnum.DIRECT1.symbol();
                break;
                
            case DIRECT2:
                result = UserEnum.DIRECT2.symbol();
                break;

            default:
                break;
        }
        
        return result;
    }
    
    /**
     * 將verifyLevel會出現的中文字整理到list中
     * 
     * @param
     * @return List<String>
     */
    private List<String> stringLevels () {
        List<String> list = new ArrayList<String>();
        list.add("經辦處理");
        list.add("副科處理");
        list.add("副科長處理");
        return list;
    }
    
}
