/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.FormInfoDateEntity;
import com.ebizprise.winw.project.entity.FormJobInfoDateEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysOptionEntity;
import com.ebizprise.winw.project.entity.SysParameterEntity;
import com.ebizprise.winw.project.enums.DashBoardEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IFormJobInfoDateRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.ISysOptionRepository;
import com.ebizprise.winw.project.repository.ISysParameterRepository;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.IDashBoardService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.DashBoardVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 首頁相關資訊
 * 
 * @author suho.yeh, adam.yeh
 * @version 1.0, Created at 2019年7月15日
 */
@Transactional
@Service("dashBoardService")
public class DashBoardServiceImpl extends BaseService implements IDashBoardService {
    
    private static final String NEXT_EXPIRATION = "FIND_FORM_INFO_DATE_BY_FORMID";
    private static final String NOT_SENT = "FIND_FORM_JOIN_FORM_INFO_BY_USERCREATED";
    private static final String UNFINISHED ="FIND_FORM_JOIN_DETAIL_JOIN_LOG_BY_USERCREATED";
    private static final String KPI_TARGET = "FIND_FORM_ALL";
    private static final String CHECK_PENDING = "checkPending";
    private static final String CHECK_PENDING_APPROVER = "checkPendingApprover";
    private static final String [] HTML_ID = {"kpi",
                                              "checkPending",
                                              "notSent",
                                              "unfinished"};
    private static final String [] CHECK_PENDING_SQL = {"FIND_FORM_PROCESS_DETAIL_APPLY_CHG_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_APPLY_INC_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_APPLY_JOB_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_APPLY_Q_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_APPLY_SR_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_APPLY_BA_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_CHG_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_INC_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_JOB_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_Q_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_SR_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_REVIEW_BA_BY_FORMID",
                                                    "FIND_FORM_PROCESS_DETAIL_C_BY_FORMID",
                                                    };
    
    @Autowired
    private ISysGroupRepository sysGroupRepository;
    @Autowired
    private ISysParameterRepository sysParameterRepository;
    @Autowired
    private ISysOptionRepository sysOptionRepository;
    @Autowired
    private ICommonFormService commonFormService;
    @Autowired
    private IFormJobInfoDateRepository jobDateRepo;
    
    @Override
    public List<DashBoardVO> getDashBoardDataList(SysUserVO user) {
        boolean isNumber;
        int verifyLevel = 0;
        boolean isPic = commonFormService.isPic();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("groupId", user.getGroupId());
        params.put("cGroupid", user.getGroupId());
        List<Map<String,Object>> kpiTraget = getDataList(KPI_TARGET,new Conditions());
        SysGroupEntity sysGroup = sysGroupRepository.findByGroupId(user.getGroupId());
        List<Map<String,Object>> checkPending = getPendingList(user);
        List<Map<String,Object>> notSent = getDataList(NOT_SENT, new Conditions().and().equal("result.UserCreated",user.getUserId()));
        List<Map<String,Object>> unfinished = getDataList(UNFINISHED, new Conditions().and().equal("r.UserCreated",user.getUserId()));
        SysParameterEntity kpiDemand = sysParameterRepository.findByParamKey(SysParametersEnum.KPI_DEMAND.name());
        SysParameterEntity kpiIssue = sysParameterRepository.findByParamKey(SysParametersEnum.KPI_ISSUE.name());
        SysParameterEntity kpiEvent = sysParameterRepository.findByParamKey(SysParametersEnum.KPI_EVENT.name());
        Map<String, Object> exp = getData(NEXT_EXPIRATION, params);
        EnumMap<FormEnum, ArrayList<Map<String,Object>>> kpiMapping = new EnumMap<>(FormEnum.class);

        List<DashBoardVO> htmlTableList = null;
        Map<String,List<Map<String,Object>>> kpiMappingList = null;
        List<DashBoardVO> voList = new ArrayList<DashBoardVO>();
        
        for(int i = 0; i<HTML_ID.length;i++) {
            if(isKPIShow(HTML_ID[i], sysGroup)) {
                continue;
            }
            
            DashBoardVO dashboardVO = new DashBoardVO();
            dashboardVO.setIsAllowBatchReview(StringConstant.SHORT_YES.equals(sysGroup.getAllowBatchReview()));
            dashboardVO.setIsDisplayKpi(sysGroup.getDisplayKpi());
            dashboardVO.setAuthType(sysGroup.getAuthType());
            dashboardVO.setNextExpiration(MapUtils.getString(exp, "ECT", ""));
            
            //若登入人員不是經辦,且為當前的項目為checkPending的話,則調整名稱的顯示方式
            boolean isCheckPendingApprover = !isPic && CHECK_PENDING.equals(HTML_ID[i]);
            if (isCheckPendingApprover) {
                dashboardVO.setHtmlId(CHECK_PENDING_APPROVER);
            } else {
                dashboardVO.setHtmlId(HTML_ID[i]);
            }
            
            htmlTableList = new ArrayList<DashBoardVO>();
            if(HTML_ID[i].equals(HTML_ID[0])) {// KPI
                dashboardVO.setKpiDemand(isKPIEmpty(kpiDemand) ? 0 : Integer.valueOf(kpiDemand.getParamValue()));
                dashboardVO.setKpiIssue(isKPIEmpty(kpiIssue) ? 0 : Integer.valueOf(kpiIssue.getParamValue()));
                dashboardVO.setKpiEvent(isKPIEmpty(kpiEvent) ? 0 : Integer.valueOf(kpiEvent.getParamValue()));

                kpiMapping.put(FormEnum.Q, new ArrayList<Map<String,Object>>());
                kpiMapping.put(FormEnum.SR, new ArrayList<Map<String,Object>>());
                kpiMapping.put(FormEnum.INC, new ArrayList<Map<String,Object>>());
                
                for(Map<String,Object>m:kpiTraget) {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("orderNumber", MapUtils.getString(m, "FormId"));
                    map.put("sender", MapUtils.getString(m, "Name"));
                    map.put("orderStatus", getOrderStatus(m, user.getGroupName()));
                    map.put("orderType", getMessage(mappingStatus(FormEnum.valueOf(MapUtils.getString(m, "FormClass")))));
                    map.put("processStatus", processStatusByGroupId(m));
                    map.put("billingTime", nullSafeBySubmitTime(m));
                    
                    FormEnum type = FormEnum.valueOf(MapUtils.getString(m, "FormClass"));
                    kpiMapping.get(type).add(map);
                }
                
                dashboardVO.setActualDemand(kpiMapping.get(FormEnum.SR).size());
                dashboardVO.setActualIssue(kpiMapping.get(FormEnum.Q).size());
                dashboardVO.setActualEvent(kpiMapping.get(FormEnum.INC).size());

                kpiMappingList = new HashMap<String,List<Map<String,Object>>>();
                kpiMappingList.put("2issue", kpiMapping.get(FormEnum.Q));
                kpiMappingList.put("1demand", kpiMapping.get(FormEnum.SR));
                kpiMappingList.put("3event", kpiMapping.get(FormEnum.INC));
                dashboardVO.setKpiList(kpiMappingList);
            }
           
           if(HTML_ID[i].equals(HTML_ID[1])) {// checkPending
                for(Map<String,Object> map : checkPending) {
                    isNumber = NumberUtils.isParsable(MapUtils.getString(map, "verifyLevel"));
                    verifyLevel = Integer.valueOf(isNumber ? MapUtils.getString(map, "verifyLevel") : "0");
                    
                    DashBoardVO vo = new DashBoardVO();
                    vo.setId(MapUtils.getLong(map, "Id"));
                    vo.setSender(MapUtils.getString(map, "Name"));
                    vo.setOrderNumber(MapUtils.getString(map, "FormId"));
                    vo.setOrderType(getMessage(mappingStatus(FormEnum.valueOf(MapUtils.getString(map, "FormClass")))));
                    vo.setOrderStatus(getOrderStatus(map, user.getGroupName()));
                    vo.setArrivedTime(nullSafeBySubmitTime(map));
                    vo.setFormId(MapUtils.getString(map, "FormId"));
                    vo.setDetailId(MapUtils.getString(map, "DetailId"));
                    vo.setFormClass(MapUtils.getString(map, "formClass"));
                    vo.setFormStatus(MapUtils.getString(map, "formStatus"));
                    vo.setVerifyType(MapUtils.getString(map, "verifyType"));
                    vo.setVerifyLevel(String.valueOf(verifyLevel));
                    vo.setJumpLevel(String.valueOf(verifyLevel + 1));
                    vo.setVerifyResult(FormEnum.AGREED.name());
                    vo.setIsNextLevel(true);
                    vo.setTct(nullSafeByTctDate(map));
                    vo.setFinishDate(nullSafeByFinishDate(map));
                    vo.setUserSolving(MapUtils.getString(map, "userSolving"));
                    vo.setUserId(MapUtils.getString(map, "UserSolvingId"));
                    vo.setGroupSolving(MapUtils.getString(map, "groupSolving"));
                    vo.setSummary(subSummary(MapUtils.getString(map, "Summary", "")));
                    vo.setUpdatedAt((Date) MapUtils.getObject(map, "UpdatedAt", null));
                    vo.setDivisionSolving(MapUtils.getString(map, "divisionSolving"));
                    vo.setActualCompDate(nullSafeByActualCompDate(map));
                    vo.setObservation(nullSafeByObservation(map));
                    htmlTableList.add(vo);
                 }
                //若為會辦單,則在透過條件,取得特定的時間欄位,作為預計完成時間的資料
                finishDateProvider(htmlTableList);
            }
           
            if(HTML_ID[i].equals(HTML_ID[2])) {// notSent
                for(Map<String,Object> m:notSent) {
                    DashBoardVO vo = new DashBoardVO();
                    vo.setOrderNumber(MapUtils.getString(m, "FormId"));
                    vo.setSummary(subSummary(MapUtils.getString(m, "Summary", "")));
                    vo.setOrderType(getMessage(mappingStatus(FormEnum.valueOf(MapUtils.getString(m, "FormClass")))));
                    vo.setOrderStatus(getOrderStatus(m, user.getGroupName()));
                    vo.setBillingTime(nullSafeByCreateAt(m));
                    htmlTableList.add(vo);
                }
            }
            
            if(HTML_ID[i].equals(HTML_ID[3])) {// unfinished
                for(Map<String,Object> m:unfinished) {
                    DashBoardVO vo = new DashBoardVO();
                    vo.setOrderStatus(getOrderStatus(m, user.getGroupName()));
                    vo.setSender(MapUtils.getString(m, "Name"));
                    vo.setOrderNumber(MapUtils.getString(m, "FormId"));
                    vo.setOrderType(getMessage(mappingStatus(FormEnum.valueOf(MapUtils.getString(m, "FormClass")))));
                    vo.setArrivedTime(nullSafeByCreateAt(m));
                    vo.setSummary(subSummary(MapUtils.getString(m, "Summary", "")));
                    htmlTableList.add(vo);
                }
            }
            if (isCheckPendingApprover) {
            	String formClass;
            	for (DashBoardVO vo : htmlTableList) {
            		formClass = vo.getFormClass();
            		if (StringUtils.equalsAny(formClass, "JOB_AP", "JOB_AP_C")) {
            			vo.setTct(vo.getFinishDate());	//FIXME SQL中TCT欄位修正後刪除此行
            			vo.setFinishDate("");
            		}
            	}
            }
            dashboardVO.setFormList(htmlTableList);
            dashboardVO.setCount(htmlTableList.size());
            voList.add(dashboardVO);
        }
        
        return voList;
    }

    @Override
    public String getFormStatus(String groupId,String status, String verifyLevel,int item) {
        int num =  0;
        if(item!=0) {
            num = 1;
        }
        if(StringUtils.isBlank(groupId)) {
            if(StringUtils.isBlank(verifyLevel) || isNumeric(verifyLevel)) {
                return formStatus().containsKey(status)?getMessage(
                        String.format(formStatus().get(status), 0)):StringUtils.SPACE;
            }
            return verifyLevel;
        }else {
            if(StringUtils.isBlank(status) || (!formStatus().containsKey(status))) {
                return StringUtils.SPACE;
            }
            if(!getSysGroup().containsKey(groupId)) {
                return  getMessage(
                            String.format(formStatus().get(status), 0));
            }
            return
               getMessage(String.format(formStatus().get(status), num), getSysGroup().get(groupId));
        }
    }
    
    private String subSummary (String summary) {
        if (summary.length() > 60) {
            summary = summary.substring(0, 60) + ".....";
        }
        
        summary = summary.replaceAll("\\s+", " ");
        summary = summary.replaceAll("\\\\","\\\\\\\\");
        // \" -> \\\"
        // 反斜線+雙引號會導致前端JSON解析失敗, 所以在這邊處理成跳脫字元
        summary = summary.replaceAll("\\\"","\\\\\\\"");
        
        return getTextFromHtml(summary);
    }
    
    private String getSysOptionDisplay (String workProject) {
        List<SysOptionEntity> workLevels =
                sysOptionRepository.findByOptionIdOrderBySortAsc("WORK_LEVEL");
        
        if (StringUtils.equals(workProject, FormJobEnum.CSPERSON.name()) ||
                StringUtils.equals(workProject, FormJobEnum.SPERSON.name())) {
            workProject = "經辦";
        } else {
            for (SysOptionEntity workLevel : workLevels) {
                if (workLevel.getValue().equals(workProject)) {
                    workProject = workLevel.getDisplay();
                }
            }
        }
        
        workProject = workProject.replace("組", "人員");
        workProject = StringUtils.
                contains(workProject, "_") ? workProject : "ISWP_" + workProject;
        
        return workProject;
    }
    
    private String delHtmlTags(String htmlStr) {
        String scriptRegex="<script[^>]*?>[\\s\\S]*?<\\/script>";
        String styleRegex="<style[^>]*?>[\\s\\S]*?<\\/style>";
        String htmlRegex="<[^>]+>";
        String spaceRegex = "\\s*|\t|\r|\n";
 
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        htmlStr = htmlStr.replaceAll(spaceRegex, "");
        return htmlStr.trim();
    }
    
    private String getTextFromHtml(String htmlStr){
        htmlStr = delHtmlTags(htmlStr);
        htmlStr = htmlStr.replaceAll(" ","");
        return htmlStr;
    }

    private String getOrderStatus (Map<String, Object> map, String userGroupName) {
        String groupName = MapUtils.getString(map, "GroupName", "");
        String formStatus = MapUtils.getString(map, "FormStatus", "");
        String orderStatus = mappingStatus(FormEnum.valueOf(formStatus));
        
        if (FormEnum.PROPOSING.name().equalsIgnoreCase(formStatus)) {
            orderStatus = FormEnum.PROPOSING.formStatus(userGroupName);
        } else if (FormEnum.WATCHING.name().equalsIgnoreCase(formStatus)) {
            orderStatus = FormEnum.WATCHING.formStatus(groupName);
        } else if (FormEnum.ASSIGNING.name().equalsIgnoreCase(formStatus)) {
            orderStatus = FormEnum.ASSIGNING.formStatus(groupName);
        } else {
            String formClass = MapUtils.getString(map, "FormClass", "");
            String workProject = MapUtils.getString(map, "WorkProject", "");
            
            if (isPic(groupName)) {
                if (isJobFrom(formClass) && StringUtils.isNotBlank(workProject)) {
                    orderStatus = FormEnum.CHARGING.formStatus(
                            getSysOptionDisplay(MapUtils.getString(map, "WorkProject", "")));
                } else {
                    orderStatus = FormEnum.CHARGING.formStatus(groupName);
                }
            } else {
                orderStatus = FormEnum.APPROVING.formStatus(groupName);
            }
        }
        
        return orderStatus;
    }
    
    private boolean isPic (String groupName) {
        return !StringUtils.contains(groupName, UserEnum.DIVISION_CHIEF.wording()) &&
                !StringUtils.contains(groupName, UserEnum.DEPUTY_MANAGER.wording()) &&
                !StringUtils.contains(groupName, UserEnum.ASSISTANT_MANAGER.wording()) &&
                !StringUtils.contains(groupName, UserEnum.VICE_DIVISION_CHIEF.wording());
    }

    private boolean isJobFrom (String formClass) {
        return StringUtils.contains(formClass, FormEnum.JOB.name());
    }

    private String nullSafeByCreateAt (Map<String, Object> m) {
        return m.get("CreatedAt") == null ? "" : DateUtils.toString((Date)m.get("CreatedAt"), DateUtils.pattern12);
    }
    
    private String nullSafeByFinishDate (Map<String, Object> m) {
        return m.get("finishDate") == null ? "" : DateUtils.toString((Date)m.get("finishDate"), DateUtils.pattern12);
    }
    
    private String nullSafeByTctDate (Map<String, Object> m) {
        return m.get("TCT") == null ? "" : DateUtils.toString((Date)m.get("TCT"), DateUtils.pattern12);
    }

    private String nullSafeBySubmitTime (Map<String, Object> m) {
        return m.get("SubmitTime") == null ?
                StringUtils.EMPTY : DateUtils.toString((Date)m.get("SubmitTime"), DateUtils.pattern12);
    }
    
    private String nullSafeByActualCompDate (Map<String, Object> m) {
        return m.get("actualCompDate") == null ? "" : DateUtils.toString((Date)m.get("actualCompDate"), DateUtils.pattern12);
    }
    
    private String nullSafeByObservation (Map<String, Object> m) {
        return m.get("observation") == null ? "" : DateUtils.toString((Date)m.get("observation"), DateUtils.pattern12);
    }

    private String processStatusByGroupId (Map<String, Object> m) {
        String processStatus = "";
        
        if(m.get("GroupId") == null) {
            processStatus = MapUtils.getString(m, "VerifyLevel");
        } else {
            processStatus = MapUtils.getString(m, "GroupName", "") + " " + mappingStatus(FormEnum.valueOf(MapUtils.getString(m, "ProcessStatus")));
        }
        
        return processStatus;
    }

    private List<Map<String,Object>> getPendingList (SysUserVO user){
        String sqlFile;
        Conditions conditions;
        Map<String, Object> params;
        boolean isC, isJob, isReview;
        String groupId = user.getGroupId();
        String code = UserInfoUtil.getUserTitleCode(user);
        boolean isPic = UserEnum.PIC.name().equalsIgnoreCase(code);// 經辦
        boolean isDirect1 = UserEnum.DIRECT1.name().equalsIgnoreCase(code);// 副理
        List<Map<String,Object>> pendingList = new ArrayList<Map<String,Object>>();

        for (int i = 0; i < CHECK_PENDING_SQL.length; i++) {
            params = new HashMap<>();
            conditions = new Conditions();
            sqlFile = CHECK_PENDING_SQL[i];
            isC = StringUtils.contains(sqlFile, "_C_");// 一般會辦單
            isJob = StringUtils.contains(sqlFile, "_JOB_");// 工作單/工作會辦單
            isReview = StringUtils.contains(sqlFile, "_REVIEW_");// 審核流程才有副理專屬的SQL
            sqlFile = wrapDirectSqlFile(sqlFile, isDirect1);
                    
            if (isPic) {
                if (isC) {
                    conditions.and().leftPT();
                    conditions.equal("f.UserSolving", user.getUserId());
                    conditions.or().equal("fvl.UserId", user.getUserId());
                    conditions.RightPT();
                    if (StringUtils.equals(groupId, "ISWP-SP")) {
                    	conditions.or().leftPT();
                    	conditions.equal("PD.groupid", "ISWP-DC");
                    	conditions.and().equal("f.UserSolving", user.getUserId());
                    	conditions.RightPT();
                    }
                } else if (isJob) {
                    conditions.and().leftPT();
                    conditions.equal("temp.UserId", user.getUserId());
                    conditions.or().equal("temp.UserSolving", user.getUserId());
                    conditions.RightPT();
                    params.put("userId", user.getUserId());
                } else {
                    conditions.and().equal("temp.UserSolving", user.getUserId());
                }
                params.put("groupId", groupId);
            } else if (isDirect1 && isReview) {
                params.put("userId", user.getUserId());
                conditions.and().equal("PD.GroupId", groupId);
            } else {
                params.put("groupId", groupId);
                params.put("userId", user.getUserId());
            }

            List<Map<String,Object>> dataList =
                    getDataList(sqlFile, conditions, params);
            
            if (CollectionUtils.isNotEmpty(dataList)) {
                pendingList.addAll(dataList);
            }

            appendApJobCsPersonForms(isJob, isReview, user, params, pendingList);
        }
        
        return pendingList;
    }

    /**
     * AP工作單的會辦人員關卡只給查看不給審核<br>
     * AP工作單的流程新增「會辦處理人員關卡」並且該關卡之審核人員只能審閱表單不可審核。
     * @param isJob
     * @param isReview
     * @param user
     * @param params
     * @param dataList
     * @author adam.yeh
     */
    private void appendApJobCsPersonForms (
            boolean isJob,
            boolean isReview,
            SysUserVO user,
            Map<String, Object> params,
            List<Map<String, Object>> dataList) {
        if (isJob) {
            params.clear();
            params.put("userId", user.getUserId());
            
            if (isReview) {
                dataList.addAll(getDataList(
                        "FIND_FORM_PROCESS_DETAIL_REVIEW_JOB_FOR_CSPERSON", params));
            } else {
                dataList.addAll(getDataList(
                        "FIND_FORM_PROCESS_DETAIL_APPLY_JOB_FOR_CSPERSON", params));
            }
            dataList = dataList.stream().distinct().collect(Collectors.toList());
        }
    }

    // 如果是審核階段且審核者若是副理則使用副理專用的SQL撈資料
    private String wrapDirectSqlFile (String sqlFile, boolean isDirect1) {
        if (StringUtils.contains(sqlFile, "REVIEW")) {
            String sqlDirect = StringUtils.replace(sqlFile, "FORMID", "DIRECT");
            sqlFile = isDirect1 ? sqlDirect : sqlFile;
        }
        
        return sqlFile;
    }

    private List<Map<String, Object>> getDataList (String name, Conditions conditions){
        ResourceEnum sqlResource = ResourceEnum.SQL_DASH_BOARD_DATA.getResource(name);
        return jdbcRepository.queryForList(sqlResource, conditions);
    }
    
    private List<Map<String, Object>> getDataList (String name, Map<String,Object> params){
        ResourceEnum sqlResource = ResourceEnum.SQL_DASH_BOARD_DATA.getResource(name);
        return jdbcRepository.queryForList(sqlResource, params);
    }
    
    private List<Map<String, Object>> getDataList (String name, Conditions conditions, Map<String,Object> params){
        ResourceEnum sqlResource = ResourceEnum.SQL_DASH_BOARD_DATA.getResource(name);
        return jdbcRepository.queryForList(sqlResource, conditions, params);
    }

    private Map<String, Object> getData (String name, Map<String,Object> params){
        ResourceEnum sqlResource = ResourceEnum.SQL_DASH_BOARD_DATA.getResource(name);
        return jdbcRepository.queryForMap(sqlResource, params);
    }
    
    private boolean isKPIEmpty (SysParameterEntity kpi) {
        return StringUtils.isBlank(kpi.getParamValue());
    }
    
    private Map<String,String> getSysGroup (){
        Map<String,String> map = new HashMap<String,String>();
        List<SysGroupEntity> groups = sysGroupRepository.findAll();
        
        for(SysGroupEntity s : groups) {
            if(map.containsKey(s.getGroupId())) {
                continue;
            }

            map.put(s.getGroupId(), s.getGroupName());
        }
        
        return map;
    }
    
    private Map<String, String> formStatus(){
        Map<String,String> map = new HashMap<String,String>();
        map.put(DashBoardEnum.TYPE_PROPOSING.getName(), DashBoardEnum.TYPE_PROPOSING.getI18nKey());
        map.put(DashBoardEnum.TYPE_APPROVING.getName(), DashBoardEnum.TYPE_APPROVING.getI18nKey());
        map.put(DashBoardEnum.TYPE_CHARGING.getName(), DashBoardEnum.TYPE_CHARGING.getI18nKey());
        map.put(DashBoardEnum.TYPE_CLOSED.getName(), DashBoardEnum.TYPE_CLOSED.getI18nKey());
        map.put(DashBoardEnum.TYPE_DEPRECATED.getName(), DashBoardEnum.TYPE_DEPRECATED.getI18nKey());
        map.put(DashBoardEnum.TYPE_ASSIGNING.getName(), DashBoardEnum.TYPE_ASSIGNING.getI18nKey());
        map.put(DashBoardEnum.TYPE_SELFSOLVE.getName(), DashBoardEnum.TYPE_SELFSOLVE.getI18nKey());
        map.put(DashBoardEnum.TYPE_WATCHING.getName(), DashBoardEnum.TYPE_WATCHING.getI18nKey());
        return map;
    }
    
    private boolean isNumeric (String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        
        return !isNum.matches();
    }
    
    private boolean isKPIShow (String htmlId, SysGroupEntity sysGroup) {
        return sysGroup.getDisplayKpi().equals(StringConstant.SHORT_NO) && htmlId.equals(HTML_ID[0]);
    }

    // 取得表單狀態或表單流程的相對應名稱
    private String mappingStatus (FormEnum status) {
        String result = "";
        
        switch (status) {
            case PROPOSING:
                result =  FormEnum.PROPOSING.status();
                break;
            case APPROVING:
                result =  FormEnum.APPROVING.status();
                break;
            case CHARGING:
                result =  FormEnum.CHARGING.status();
                break;
            case CLOSED:
                result =  FormEnum.CLOSED.status();
                break;
            case DEPRECATED:
                result =  FormEnum.DEPRECATED.status();
                break;
            case ASSIGNING:
                result =  FormEnum.ASSIGNING.status();
                break;
            case SELFSOLVE:
                result =  FormEnum.SELFSOLVE.status();
                break;
            case WATCHING:
                result =  FormEnum.WATCHING.status();
                break;
            case SENT:
                result =  FormEnum.SENT.verifyType();
                break;
            case AGREED:
                result =  FormEnum.AGREED.verifyType();
                break;
            case PENDING:
                result =  FormEnum.PENDING.verifyType();
                break;
            case DISAGREED:
                result =  FormEnum.DISAGREED.verifyType();
                break;
            case JUMP:
                result =  FormEnum.JUMP.verifyType();
                break;
            case SR:
                result =  FormEnum.SR.wording();
                break;
            case SR_C:
                result =  FormEnum.SR_C.wording();
                break;
            case Q:
                result =  FormEnum.Q.wording();
                break;
            case Q_C:
                result =  FormEnum.Q_C.wording();
                break;
            case INC:
                result =  FormEnum.INC.wording();
                break;
            case INC_C:
                result =  FormEnum.INC_C.wording();
                break;
            case CHG:
                result =  FormEnum.CHG.wording();
                break;
            case JOB:
            case JOB_AP:
            case JOB_SP:
                result =  FormEnum.JOB.wording();
                break;
            case JOB_AP_C:
                result =  FormEnum.JOB_AP_C.wording();
                break;
            case JOB_SP_C:
                result =  FormEnum.JOB_SP_C.wording();
                break;
            case BA:
                result =  FormEnum.BA.wording();
                break;
            default:
                break;
        }
        
        return result;
    }
    
    /**
     * 預計完成時間欄位會因會辦單類型的不同而有所改變,若為特定會辦單,則再從DB去撈取正確的資料,避免在SQL加上龐大且複雜的邏輯影響後續維護
     * 
     * @param vo
     */
    private void finishDateProvider(List<DashBoardVO> dashBoardLs) {
        //目前只有AP工作會辦單跟事件單的欄位較為特殊,額外取得
        List<String> jobApCFormIdLs = new ArrayList<>();
        List<String> eventCFormIdLs = new ArrayList<>();
        List<FormJobInfoDateEntity> apJobCFormDate = new ArrayList<>();
        List<FormInfoDateEntity> eventCFormDate = new ArrayList<>();
        
        for(DashBoardVO target : dashBoardLs) {
            if(StringUtils.isNotBlank(target.getFormClass())) {
                switch (FormEnum.valueOf(target.getFormClass())) {
                    case JOB_AP_C:
                        jobApCFormIdLs.add(target.getFormId());
                        break;
                    case INC_C:
                        eventCFormIdLs.add(target.getFormId());
                        break;
                    default:
                        break;
                }
            }
        }
        
        if(CollectionUtils.isNotEmpty(jobApCFormIdLs)) {
            apJobCFormDate = jobDateRepo.findByFormIdIn(jobApCFormIdLs);
        }
        
        if(CollectionUtils.isNotEmpty(eventCFormIdLs)) {
            Conditions condition = new Conditions();
            condition.and().in("F.FormId", eventCFormIdLs);
            eventCFormDate = jdbcRepository.queryForList(ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_SOURCE_DATE"),condition,FormInfoDateEntity.class);
        }
        
        //DB撈回來的日期進行對應,若相符,則替換掉dashBoardVo的finishDate
        for(FormJobInfoDateEntity target : apJobCFormDate) {
            for(DashBoardVO dashBoardVo : dashBoardLs) {
                if(FormEnum.JOB_AP_C.name().equals(dashBoardVo.getFormClass())
                        && dashBoardVo.getFormId().equals(target.getFormId())) {
                    dashBoardVo.setFinishDate(Objects.isNull(target.getSct()) ? "" : DateUtils.toString(target.getSct(), DateUtils.pattern12));
                    
                    continue;
                }
            }
        }
        
        for(FormInfoDateEntity target : eventCFormDate) {
            for(DashBoardVO dashBoardVo : dashBoardLs) {
                if(FormEnum.INC_C.name().equals(dashBoardVo.getFormClass())
                        && dashBoardVo.getFormId().equals(target.getFormId())) {
                    dashBoardVo.setFinishDate(Objects.isNull(target.getEct()) ? "" : DateUtils.toString(target.getEct(), DateUtils.pattern12));
                    
                    continue;
                }
            }
        }
    }

}
