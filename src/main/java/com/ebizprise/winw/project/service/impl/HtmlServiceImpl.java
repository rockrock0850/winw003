/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.FormInternalProcessStatusEntity;
import com.ebizprise.winw.project.entity.FormJobDivisionMappingEntity;
import com.ebizprise.winw.project.entity.FormJobInfoWorkingItemsEntity;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.entity.WorkingItemEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysCommonEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormInfoCDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoChgDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoIncDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoQDetailRepository;
import com.ebizprise.winw.project.repository.IFormInfoSrDetailRepository;
import com.ebizprise.winw.project.repository.IFormInternalProcessStatusRepository;
import com.ebizprise.winw.project.repository.IFormJobDivisionMappingRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoApDetailRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoSysDetailRepository;
import com.ebizprise.winw.project.repository.IFormJobInfoWorkingItemsRepository;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.repository.ISysOptionRoleRepository;
import com.ebizprise.winw.project.repository.IWorkingItemRepository;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.vo.FormInternalProcessStatusVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.SysOptionRoleVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 處理前端共用元件的資料操作 服務類別
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年7月5日
 */
@Transactional
@Service("htmlServiceImpl")
public class HtmlServiceImpl extends BaseService implements IHtmlService {

    @Autowired
    private ISysOptionRepository sysOptionRepository;
    @Autowired
    private IFormInfoSrDetailRepository srRepository;
    @Autowired
    private IFormInfoQDetailRepository qRepository;
    @Autowired
    private IFormInfoIncDetailRepository incRepository;
    @Autowired
    private IFormInfoChgDetailRepository chgRepository;
    @Autowired
    private IFormInfoCDetailRepository cRepository;
    @Autowired
    private IFormJobInfoApDetailRepository jobApRepository;
    @Autowired
    private IFormJobInfoSysDetailRepository jobSpRepository;
    @Autowired
    private IWorkingItemRepository workingItemRepository;
    @Autowired
    private IFormJobInfoWorkingItemsRepository formItemRepo;
    @Autowired
    private ISysOptionRoleRepository sysOptionRoleRepo;
    @Autowired
    private IFormJobDivisionMappingRepository formJobDivisionMappingRepository;
    @Autowired
    private IFormRepository formRepository;
    @Autowired
    private IFormInternalProcessStatusRepository formInternalProcessStatusRepository;
    @Autowired
    private FormHelper formHelper;

    @Override
    public List<HtmlVO> getFormCParallels (HtmlVO vo) {
        boolean isAll = vo.getIsAll();
        String formId = vo.getFormId();
        String formClazz = vo.getFormClass();
        ResourceEnum resource = null;
        Map<String, Object> dataMap = null;
        List<HtmlVO> resultList = new ArrayList<>();
        
        if (isAll) {
            resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_PARALLEL_GROUPS");
            resultList = jdbcRepository.queryForList(resource, HtmlVO.class);
        } else if (StringUtils.isBlank(formId)) {// 取最新的流程所設定的平行會辦組別清單
            resource = formHelper.getFormParallels(formId, FormEnum.valueOf(formClazz));
            dataMap = jdbcRepository.queryForMap(resource);
        } else {// 取表單所對應的流程內設定的平行會辦組別清單
            resource = formHelper.getFormParallels(formId, FormEnum.valueOf(formClazz));
            Map<String, Object> params = new HashMap<>();
            params.put("formId", formId);
            dataMap = jdbcRepository.queryForMap(resource, params);
        }
        
        String parallelStr = MapUtils.getString(dataMap, "Parallels");
        
        if (StringUtils.isNotBlank(parallelStr)) {
            String[] parallels = parallelStr.split(",");
            resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_PARALLEL_GROUPS");
            List<HtmlVO> all = jdbcRepository.queryForList(resource, HtmlVO.class);
            
            for (HtmlVO html : all) {
                for (String parallel : parallels) {
                    if (parallel.equals(html.getValue())) {
                        resultList.add(html);
                    }
                }
            }
        }
        
        return resultList;
    }

    @Override
    public List<HtmlVO> getDropdownList (HtmlVO html, boolean release) {
        List<HtmlVO> htmlList = new ArrayList<>();
        
        if (release) {
            htmlList = renderSysOptionToVO(
                    sysOptionRepository.findByOptionIdOrderBySortAsc(html.getOptionId()));
        } else {
            htmlList = getDropdownList(html);
        }
        
        return htmlList;
    }

    @Override
    public List<HtmlVO> getSubDropdownList (HtmlVO html) {
        List<SysOptionEntity> sysOptionList = sysOptionRepository.findByParentIdOrderBySortAsc(html.getValue());
        return renderSysOptionToVO(sysOptionList);
    }

    @Override
    public List<HtmlVO> getDropdownList (String optionId) {
        HtmlVO html = new HtmlVO();
        html.setOptionId(optionId);
        return getDropdownList(html);
    }

    @Override
    public List<HtmlVO> getSubDropdownList (String parentId) {
        HtmlVO html = new HtmlVO();
        html.setValue(parentId);
        return getSubDropdownList(html);
    }

    @Override
    public List<HtmlVO> getSystemList (HtmlVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_COMMON.getResource("FIND_SYSTEM_LIST");
        String department = StringUtils.isBlank(
                vo.getDepartment()) ? fetchLoginUser().getDivision() : vo.getDepartment();
        Conditions conditions = new Conditions();
        
        // 副理、協理等職位沒有division和department欄位值
        if (!StringUtils.isBlank(department)) {
            conditions.and().like("Department", department);
        }
        
        //系統編號
        if (!StringUtils.isBlank(vo.getSystemId())) {
            conditions.and().like("SystemId", vo.getSystemId());
        }
        
        //系統描述
        if (!StringUtils.isBlank(vo.getDescription())) {
            conditions.and().like("Description", vo.getDescription());
        }
        
        //系統中文說明
        if (!StringUtils.isBlank(vo.getSystemName())) {
            conditions.and().like("SystemName", vo.getSystemName());
        }
        
        //資訊資產群組
        if (!StringUtils.isBlank(vo.getMark())) {
            conditions.and().like("Mark", vo.getMark());
        }
        
        //Opinc
        if (!StringUtils.isBlank(vo.getOpinc())) {
            conditions.and().equal("Opinc", vo.getOpinc());
        }
        
        //Apinc
        if (!StringUtils.isBlank(vo.getApinc())) {
            conditions.and().equal("Apinc", vo.getApinc());
        }
        
        //極限值
        if (!StringUtils.isBlank(vo.getDescription())) {
            conditions.and().equal("Limit", vo.getLimit());
        }
        
        conditions.and().equal("Active", "Y"); //是否啟用
        conditions.and().equal("MboName", SysCommonEnum.Q.symbol());
        
        return jdbcRepository.queryForList(resource, conditions, vo, HtmlVO.class);
    }

    @Override
    public List<HtmlVO> getCComponentList (HtmlVO vo) {
        String wording = vo.getWording();
        String optionId = vo.getOptionId();
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_C_COMPONENT");
        
        if (!StringUtils.isBlank(wording)) {
            conditions.and()
                .leftPT()
                .like("Value", wording)
                .or()
                .like("Display", wording)
                .RightPT();
        }
        conditions.and().equal("ParentId", optionId);
        
        return jdbcRepository.queryForList(resource, conditions, vo, HtmlVO.class);
    }

    @Override
    public HtmlVO getCListSelecteds (String formId, String formClass) {
        HtmlVO vo = new HtmlVO();
        
        switch (FormEnum.valueOf(formClass)) {
            case SR:
                BeanUtil.copyProperties(srRepository.findByFormId(formId), vo);
                break;
                
            case Q:
                BeanUtil.copyProperties(qRepository.findByFormId(formId), vo);
                break;
                
            case INC:
                BeanUtil.copyProperties(incRepository.findByFormId(formId), vo);
                break;
                
            case CHG:
                BeanUtil.copyProperties(chgRepository.findByFormId(formId), vo);
                break;

            case Q_C:
            case SR_C:
            case INC_C:
                BeanUtil.copyProperties(cRepository.findByFormId(formId), vo);
                break;
                
            case JOB_AP:
            case JOB_AP_C:
                BeanUtil.copyProperties(jobApRepository.findByFormId(formId), vo);
                break;
                
            case JOB_SP:
            case JOB_SP_C:
                BeanUtil.copyProperties(jobSpRepository.findByFormId(formId), vo);
                break;

            default:
                break;
        }
        
        return vo;
    }
    
    @Override
    public List<HtmlVO> getWorkingItemList(HtmlVO vo) {
        List<WorkingItemEntity> resultList = null;
        
        if (StringUtils.isBlank(vo.getKeyword())) {
            resultList = workingItemRepository.findByActive(StringConstant.SHORT_YES);
        } else {
            resultList = workingItemRepository.findByWorkingItemNameLikeAndSpGroupLike(vo.getKeyword());
        }
        
        return BeanUtil.copyList(resultList, HtmlVO.class);
    }

    @Override
    public List<HtmlVO> getWorkingItems (String formId) {
        List<FormJobInfoWorkingItemsEntity> pojoList = formItemRepo.findByFormId(formId);
        List<HtmlVO> voList = new ArrayList<>();
        
        for (FormJobInfoWorkingItemsEntity pojo : pojoList) {
            HtmlVO vo = BeanUtil.fromJson(pojo.getItem(), HtmlVO.class);
            BeanUtil.copyProperties(pojo, vo);
            voList.add(vo);
        }
        
        return CollectionUtils.isEmpty(voList) ? null : voList;
    }

    @Override
    public List<HtmlVO> getDropdownListByValue(List<String> values) {
    	List<String> resetValues = new ArrayList<>();
    	Set<String> tempSet = new HashSet<>();
    	for (String value : values) {
			if (StringUtils.isNotBlank(value) && value.split("-").length > 2) {
				String[] valueSplit = value.split("-");
				value = valueSplit[0] + "-" + valueSplit[1];
			}
			tempSet.add(value);
    	}
    	resetValues.addAll(tempSet);
    	
        List<HtmlVO> rtnLs = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(values)) {
            List<SysOptionEntity> dataLs = sysOptionRepository.findByValueIn(resetValues);
            for(SysOptionEntity target : dataLs) {
                HtmlVO vo = new HtmlVO();
                vo.setWording((target.getDisplay()));
                vo.setValue(target.getValue());
                
                rtnLs.add(vo);
            }
        }
        
        return rtnLs;
    }

    @Override
    public SysOptionRoleVO getSysOptionRole(HtmlVO htmlVO) {
        SysOptionRoleVO rtnVO = new SysOptionRoleVO();
        SysOptionEntity option = sysOptionRepository.findByDisplayAndValue(htmlVO.getWording(), htmlVO.getValue());
        
        if (option != null) {
            String value = option.getValue();
            String optionId = option.getOptionId();
            BeanUtil.copyProperties(sysOptionRoleRepo.findByOptionIdAndValue(optionId, value), rtnVO);
        }
        
        return rtnVO;
    }

    @Override
    public SysOptionRoleVO getSysOptionRole(String optionId, String value) {
        SysOptionRoleVO rtnVO = new SysOptionRoleVO();
        
        if (StringUtils.isNoneBlank(optionId,value)) {
            BeanUtil.copyProperties(sysOptionRoleRepo.findByOptionIdAndValue(optionId, value), rtnVO);
        }
        
        return rtnVO;
    }
    
	@Override
	public List<HtmlVO> getInternalProcessList(String formId) {
		String division = FormJobEnum.DC.name();
		
		List<FormInternalProcessStatusEntity> internalProcessList = formInternalProcessStatusRepository.findByFormId(formId);
		Set<String> internalProcessSet = new HashSet<>();
		for (FormInternalProcessStatusEntity internalProcess : internalProcessList) {
			internalProcessSet.add(internalProcess.getDivision());
		}
		
		List<FormJobDivisionMappingEntity> entities = formJobDivisionMappingRepository.findByDivisionLike(division+"-%");
		List<HtmlVO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entities)) {
			HtmlVO vo;
			for (FormJobDivisionMappingEntity entity : entities) {
				vo = new HtmlVO();
				vo.setOptionId(entity.getJobTabName());
				vo.setValue(entity.getDivision());
				vo.setWording(division + StringConstant.DASH + entity.getJobTabName());
				vo.setDisplay(internalProcessSet.contains(entity.getDivision()) ? StringConstant.SHORT_YES : StringConstant.SHORT_NO);
				result.add(vo);
			}
		}
		return result;
	}

	@Override
	public List<HtmlVO> getInternalProcessStatus(String formId) {
		
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INTERNAL_PROCESS_STATUS");
        
        Conditions conditions = new Conditions();
        conditions.and().equal("fips.FormId", formId);
        
        List<FormInternalProcessStatusVO> entities = jdbcRepository.queryForList(resource, conditions, FormInternalProcessStatusVO.class);
		List<HtmlVO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entities)) {
			HtmlVO vo;
			for (FormInternalProcessStatusVO e : entities) {
				vo = new HtmlVO();
				vo.setOptionId(e.getDivision());
				vo.setValue(e.getDivision());
				vo.setWording(e.getJobTabName());
				vo.setDisplay(e.getIsProcessDone());
				result.add(vo);
			}
		}
		return result;
	}
    
	@Override
	public List<String> getUnfinishedInternalProcess(String formId) {
		ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_INTERNAL_PROCESS_STATUS");
        Conditions conditions = new Conditions();
        conditions.and().equal("fips.FormId", formId);
        conditions.and().equal("fips.IsProcessDone", StringConstant.SHORT_NO);
		List<FormInternalProcessStatusVO> unfinishedList = jdbcRepository.queryForList(resource, conditions, FormInternalProcessStatusVO.class);
		
		List<String> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(unfinishedList)) {
			unfinishedList.forEach(e -> result.add(e.getJobTabName()));
		}
		return result;
	}
	
	@Override
	public List<HtmlVO> getSplitProcessViceList() {
		SysUserVO user = super.fetchLoginUser();
		String userId = user.getUserId();
		String groupId = user.getGroupId();
		
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_NAME_MANAGEMENT.getResource("FIND_USERS_BY_SYSGROUP");
        Conditions conditions = new Conditions();
        conditions.and().unEqual("LU.UserId", userId);
        conditions.and().equal("SG.GroupId", groupId);
        List<LdapUserVO> entities = jdbcRepository.queryForList(resource, conditions, LdapUserVO.class);
        
		List<HtmlVO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entities)) {
			HtmlVO vo;
			for (LdapUserVO e : entities) {
				vo = new HtmlVO();
				vo.setValue(e.getUserId());
				vo.setWording(e.getName());
				result.add(vo);
			}
		}
		return result;
	}
	
    private List<HtmlVO> getDropdownList (HtmlVO html) {
        String optionId = html.getOptionId();
        SysUserVO userInfo = fetchLoginUser();
        String departmentId = userInfo.getDepartmentId();
        List<SysOptionEntity> newList = new ArrayList<>();
        List<SysOptionEntity> sysOptionList = sysOptionRepository.findByOptionIdAndActiveOrderBySortAsc(optionId, StringConstant.SHORT_YES);
        
        if ("C_LIST".equals(optionId)) {// 表單資訊 會辦科 模組
        	boolean isShowMainDC = !StringUtils.equalsAny(html.getFormClass(), FormEnum.JOB.name(), FormEnum.JOB_AP.name(), FormEnum.JOB_AP_C.name(), FormEnum.JOB_SP.name());
        	boolean isDC, isMainDC;
            for (SysOptionEntity e : sysOptionList) {// 過濾資訊部/資安部
                if (StringUtils.contains(e.getValue(), departmentId)) {
                	isDC = StringUtils.contains(e.getValue(), FormJobEnum.DC.name());
                	isMainDC = isDC && !StringUtils.contains(e.getValue(), FormJobEnum.DC.name()+StringConstant.DASH);
                	if (isDC) {
                		if (isShowMainDC == isMainDC) {
                			newList.add(e);
                		}
                	} else {
                		newList.add(e);
                	}
                }
            }
        } else {
            newList = sysOptionList;
        }
            
        return renderSysOptionToVO(newList);
    }
    
    private List<HtmlVO> renderSysOptionToVO (List<SysOptionEntity> eList) {
        List<HtmlVO> selectors = new ArrayList<>();
        
        for(SysOptionEntity e : eList) {
            HtmlVO vo = new HtmlVO();
            vo.setValue(e.getValue());
            vo.setWording(e.getDisplay());
            vo.setIsKnowledge(e.getIsKnowledge());
            selectors.add(vo);
        }
        
        return selectors;
    }

}
