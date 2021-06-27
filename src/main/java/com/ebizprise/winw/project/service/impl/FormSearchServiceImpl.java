package com.ebizprise.winw.project.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.doc.excel.ExcelUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.ModLog;
import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.Form;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.fields.FormFields;
import com.ebizprise.winw.project.enums.form.fields.FormFieldsEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.jdbc.criteria.SQL;
import com.ebizprise.winw.project.repository.IFormFieldsRepository;
import com.ebizprise.winw.project.service.IFormSearchService;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.vo.FormSearchVO;
import com.ebizprise.winw.project.vo.VersionFormVO;

/**
 * 表單搜尋
 * 
 * @author adam.yeh
 */
@Transactional
@Service("formSearchService")
public class FormSearchServiceImpl extends BaseFormService<FormSearchVO> implements IFormSearchService {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseFormService.class);

    @Autowired
    private JdbcRepositoy jdbcRepositoy;

    @Autowired
    private IFormFieldsRepository formFieldsRepo;

    @Autowired
    private FormHelper formHelper;

    @Override
    public List<VersionFormVO> getJobInfoList (FormSearchVO search) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_SEARCH.getResource("FIND_JOB_INFO_LIST");
        Conditions conditions = new Conditions();
        
        if (StringUtils.isNotBlank(search.getUserCreated())) {
            conditions.and().equal("F.UserCreated", search.getUserCreated());
        }
        
        if (StringUtils.isNotBlank(search.getFormId())) {
            conditions.and().equal("F.FormId", search.getFormId());
        }
        
        return jdbcRepository.queryForList(resource, conditions, VersionFormVO.class);
    }

    @ModLog
    @Override
    public void getFormInfo (FormSearchVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION.getResource("FIND_FORM_SEARCH_INFO_BY_FORM_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("formId", vo.getFormId());
        
        // 設定狀態外顯文字
        Map<String, Object> formInfo = jdbcRepository.queryForMap(resource, params);
        String formClass = MapUtils.getString(formInfo, "formClass");
        String formStatus = MapUtils.getString(formInfo, "formStatus");
        String groupName = MapUtils.getString(formInfo, "groupName");
        String processStatus = MapUtils.getString(formInfo, "processStatus");
        
        formInfo.put("formWording", getMessage(FormEnum.valueOf(formClass).wording()));
        formInfo.put("formType", FormEnum.valueOf(formClass).formType());
        formInfo.put("statusWording", FormEnum.valueOf(formStatus).status());
        formInfo.put("formStatusWording", FormEnum.valueOf(formStatus).formStatus(groupName));
        formInfo.put("processStatusWording", String.format(FormEnum.valueOf(processStatus).processStatus(), groupName));
        
        vo.setFormInfo(formInfo);
    }

    @Override
    public List<FormSearchVO> findFormFieldsByFormClass(String formClass) {
        Form formEnum = null;
        
        // 取得表單類型的欄位,ex:1,2,3,4,...
        String[] fields = formFieldsRepo
                .findByFormClass(formClass).getFormFields().split(",");
        
        // 用field去抓對應的欄位資訊
        if (FormEnum.isContaining(formClass)) {
            formEnum = FormEnum.valueOf(formClass);
        } else if (FormJobEnum.isContaining(formClass)) {
            formEnum = FormJobEnum.valueOf(formClass);
        }
        
        String fieldsJson = FormFieldsEnum.advance(Arrays.asList(fields), formEnum);
        List<FormSearchVO> formFieldList = BeanUtil.fromJsonToList(fieldsJson, FormSearchVO.class);
        
        // 設定I18n
        for (FormSearchVO vo : formFieldList) {
            vo.setName(getMessage(vo.getName()));
        }
        
        return formFieldList;
    }

    @Override
    public List<FormSearchVO> findBySearch (FormSearchVO formSearchVo) {
        return findFormByCondition(formSearchVo, false);
    }

    @Override
    public List<FormSearchVO> findByExport (FormSearchVO formSearchVo) {
        return findFormByCondition(formSearchVo, true);
    }

    @Override
    public List<FormSearchVO> findFormByCondition(FormSearchVO vo, boolean isExport) {
        String formClass = vo.getFormClass();
        String sqlFile = "FIND_FORM_BY_CONDITION";// 預設為查詢表單編號
        boolean isJobAp = FormEnum.JOB_AP.name().equals(formClass); 
        boolean isJobApc = FormEnum.JOB_AP_C.name().equals(formClass); 

        if (StringUtils.isNotBlank(formClass)) {
            sqlFile = formHelper.getFormClassSQLMap(FormEnum.valueOf(formClass));
        }
        
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_FORM_SEARCH.getResource(sqlFile);

        // 表單類別
        if (StringUtils.isNotBlank(formClass)) {
            conditions.and().equal("f.FormClass", formClass);
        }

        // 知識庫前次變更時間-起日
        if (StringUtils.isNotBlank(vo.getUpdatedAtStart())) {
            conditions.and().gtEqual("CONVERT(date, FIDTL.UpdatedAt, 111)", vo.getUpdatedAtStart());
        }
        // 知識庫前次變更時間-迄日
        if (StringUtils.isNotBlank(vo.getUpdatedAtEnd())) {
            conditions.and().ltEqual("CONVERT(date, FIDTL.UpdatedAt, 111)", vo.getUpdatedAtEnd());
        }
        
        // 知識庫根因
        if (StringUtils.isNotBlank(vo.getKnowledges())) {
            conditions.and().like("FIDTL.Knowledges", vo.getKnowledges());
        }

        // 知識庫相關解決方案
        if (StringUtils.isNotBlank(vo.getSolutions())) {
            conditions.and().like("FIDTL.Solutions", vo.getSolutions());
        }

        if (StringUtils.isNotBlank(vo.getFormUserRecord())) {
            conditions.and().like("FUR.Summary", vo.getFormUserRecord());
        }
        
        if (StringUtils.isNotBlank(vo.getIsAlterDone())) {
            conditions.and().equal("f.IsAlterDone", vo.getIsAlterDone());
        }

        if (StringUtils.isNotBlank(vo.getFormFile())) {
            conditions.and().like("FF.Name", vo.getFormFile());
        }
        
        // 表單編號
        if (StringUtils.isNotBlank(vo.getFormId())) {
            conditions.and().like("f.FormId", vo.getFormId());
        }
        // 表單狀態
        if (StringUtils.isNotBlank(vo.getFormStatus())) {
            conditions.and().equal("f.FormStatus", vo.getFormStatus());
        }
        // 來源表單
        if (StringUtils.isNotBlank(vo.getSourceId())) {
            conditions.and().like("f.SourceId", vo.getSourceId());
        }
        // 開單時間-起日
        if (StringUtils.isNotBlank(vo.getCreateTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, f.CreateTime, 111)", vo.getCreateTimeStart());
        }
        // 開單時間-迄日
        if (StringUtils.isNotBlank(vo.getCreateTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, f.CreateTime, 111)", vo.getCreateTimeEnd());
        }
        // 開單科別
        if (StringUtils.isNotBlank(vo.getDivisionCreated())) {
            conditions.and().like("f.DivisionCreated", vo.getDivisionCreated());
        }
        // 開單人員
        if (StringUtils.isNotBlank(vo.getUserCreated())) {
            conditions.and().like("lUser.Name", vo.getUserCreated());
        }
        // 處理科別
        if (StringUtils.isNotBlank(vo.getDivisionSolving())) {
            conditions.and().like("f.DivisionSolving", vo.getDivisionSolving());
        }
        // 處理人員
        if (StringUtils.isNotBlank(vo.getUserSolving())) {
            conditions.and().like("lUS.Name", vo.getUserSolving());
        }
        // 提出單位分類
        if (StringUtils.isNotBlank(vo.getUnitCategory())) {
            conditions.and().equal("u.UnitCategory", vo.getUnitCategory());
        }
        // 提出人員單位
        if (StringUtils.isNotBlank(vo.getUnitId())) {
            conditions.and().like("u.UnitId", vo.getUnitId());
        }
        // 提出人員姓名
        if (StringUtils.isNotBlank(vo.getUserName())) {
            conditions.and().like("u.UserName", vo.getUserName());
        }
        // (需求、問題、事件、變更)摘要
        if (StringUtils.isNotBlank(vo.getSummary())) {
            if (FormEnum.JOB_SP.name().equals(formClass)) {
                conditions.and().like("ap.Summary", vo.getSummary());
            } else {
                conditions.and().like("FIDTL.Summary", vo.getSummary());
            }
        }
        // (需求、問題、事件、變更、執行)內容
        if (StringUtils.isNotBlank(vo.getContent())) {
            boolean isJob = formClass.indexOf(FormEnum.JOB.name()) != -1;
            
            if (isJob) {
                conditions.and().like("ap.Content", vo.getContent());
            } else {
                conditions.and().like("Content", vo.getContent());
            }
        }
        // 問題來源
        if (StringUtils.isNotBlank(vo.getQuestionId())) {
            conditions.and().equal("u.QuestionId", vo.getQuestionId());
        }
        // 問題優先順序
        if (StringUtils.isNotBlank(vo.getQuestionPriority())) {
            conditions.and().equal("QuestionPriority", vo.getQuestionPriority());
        }
        // 事件主類別
        if (StringUtils.isNotBlank(vo.getEventClass())) {
            conditions.and().equal("EventClass", vo.getEventClass());
        }
        // 事件類型
        if (StringUtils.isNotBlank(vo.getEventType())) {
            conditions.and().like("EventType", vo.getEventType());
        }
        // 資安事件
        if (StringUtils.isNotBlank(vo.getEventSecurity())) {
            conditions.and().like("EventSecurity", vo.getEventSecurity());
        }
        // 事件優先順序
        if (StringUtils.isNotBlank(vo.getEventPriority())) {
            conditions.and().equal("EventPriority", vo.getEventPriority());
        }
        // 設定為主要事件單
        if (StringUtils.isNotBlank(vo.getIsMainEvent())) {
            conditions.and().equal("IsMainEvent", vo.getIsMainEvent());
        }
        // 併入主要事件單
        if (StringUtils.isNotBlank(vo.getMainEvent())) {
            conditions.and().like("d.MainEvent", vo.getMainEvent());
        }
        // 全部功能服務中斷
        if (StringUtils.isNotBlank(vo.getIsInterrupt())) {
            conditions.and().equal("IsInterrupt", vo.getIsInterrupt());
        }
        // 變更影響系統
        if (StringUtils.isNotBlank(vo.getEffectSystem())) {
            conditions.and().like("EffectSystem", vo.getEffectSystem());
        }
        // 是新系統
        if (StringUtils.isNotBlank(vo.getIsNewSystem())) {
            conditions.and().equal("IsNewSystem", vo.getIsNewSystem());
        }
        // 是新服務暨重大服務
        if (StringUtils.isNotBlank(vo.getIsNewService())) {
            conditions.and().equal("IsNewService", vo.getIsNewService());
        }
        // 緊急變更
        if (StringUtils.isNotBlank(vo.getIsUrgent())) {
            conditions.and().equal("IsUrgent", vo.getIsUrgent());
        }
        // 標準變更作業
        if (StringUtils.isNotBlank(vo.getStandard())) {
            conditions.and().like("Standard", vo.getStandard());
        }
        // 變更類型
        if (StringUtils.isNotBlank(vo.getChangeType())) {
            conditions.and().equal("ChangeType", vo.getChangeType());
        }
        // 變更等級
        if (StringUtils.isNotBlank(vo.getChangeRank())) {
            conditions.and().equal("ChangeRank", vo.getChangeRank());
        }
        // 有新增異動欄位影響到資料倉儲系統產出資料
        if (StringUtils.isNotBlank(vo.getIsEffectField())) {
            conditions.and().equal("IsEffectField", vo.getIsEffectField());
        }
        // 有新增異動會計科目影響到資料倉儲系統產出資料
        if (StringUtils.isNotBlank(vo.getIsEffectAccountant())) {
            conditions.and().equal("IsEffectAccountant", vo.getIsEffectAccountant());
        }
        // 未有修改程式
        if (StringUtils.isNotBlank(vo.getIsModifyProgram())) {
            conditions.and().equal("IsModifyProgram", vo.getIsModifyProgram());
        }
        // 起日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
        if (StringUtils.isNotBlank(vo.getInfoDateCreateTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, d.CreatedAt, 111)", vo.getInfoDateCreateTimeStart());
        }
        // 迄日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
        if (StringUtils.isNotBlank(vo.getInfoDateCreateTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.CreatedAt, 111)", vo.getInfoDateCreateTimeEnd());
        }
        // 預計完成時間(目標解決時間)-起日
        if (StringUtils.isNotBlank(vo.getEctStart())) {
            conditions.and().gtEqual("CONVERT(date, d.ECT, 111)", vo.getEctStart());
        }
        // 預計完成時間(目標解決時間)-迄日
        if (StringUtils.isNotBlank(vo.getEctEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.ECT, 111)", vo.getEctEnd());
        }
        // 實際開始時間-起日
        if (StringUtils.isNotBlank(vo.getAstStart())) {
            conditions.and().gtEqual("CONVERT(date, d.AST, 111)", vo.getAstStart());
        }
        // 實際開始時間-迄日
        if (StringUtils.isNotBlank(vo.getAstEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.AST, 111)", vo.getAstEnd());
        }
        // 實際完成時間(事件完成時間)-起日
        if (StringUtils.isNotBlank(vo.getActStart())) {
            conditions.and().gtEqual("CONVERT(date, d.ACT, 111)", vo.getActStart());
        }
        // 實際完成時間(事件完成時間)-迄日
        if (StringUtils.isNotBlank(vo.getActEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.ACT, 111)", vo.getActEnd());
        }
        // 與業務單位確認預計上線時間-起日
        if (StringUtils.isNotBlank(vo.getEotStart())) {
            conditions.and().gtEqual("CONVERT(date, d.EOT, 111)", vo.getEotStart());
        }
        // 與業務單位確認預計上線時間-迄日
        if (StringUtils.isNotBlank(vo.getEotEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.EOT, 111)", vo.getEotEnd());
        }
        // 變更申請時間-起日
        if (StringUtils.isNotBlank(vo.getCatStart())) {
            conditions.and().gtEqual("CONVERT(date, d.CAT, 111)", vo.getCatStart());
        }
        // 變更申請時間-迄日
        if (StringUtils.isNotBlank(vo.getCatEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.CAT, 111)", vo.getCatEnd());
        }
        // 預計變更結束時間-起日
        if (StringUtils.isNotBlank(vo.getCctStart())) {
            conditions.and().gtEqual("CONVERT(date, d.CCT, 111)", vo.getCctStart());
        }
        // 預計變更結束時間-迄日
        if (StringUtils.isNotBlank(vo.getCctEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.CCT, 111)", vo.getCctEnd());
        }
        // 主單預計完成時間-起日
        if (StringUtils.isNotBlank(vo.getMectStart())) {
            conditions.and().gtEqual("CONVERT(date, d.MECT, 111)", vo.getMectStart());
        }
        // 主單預計完成時間-迄日
        if (StringUtils.isNotBlank(vo.getMectEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.MECT, 111)", vo.getMectEnd());
        }
        // 特殊結案
        if (StringUtils.isNotBlank(vo.getIsSpecial())) {
            conditions.and().equal("d.IsSpecial", vo.getIsSpecial());
        }
        // 特殊結案狀態
        if (StringUtils.isNotBlank(vo.getSpecialEndCaseType())) {
            conditions.and().equal("d.SpecialEndCaseType", vo.getSpecialEndCaseType());
        }
        // 報告時間-起日
        if (StringUtils.isNotBlank(vo.getReportTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, d.ReportTime, 111)", vo.getReportTimeStart());
        }
        // 報告時間-迄日
        if (StringUtils.isNotBlank(vo.getReportTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.ReportTime, 111)", vo.getReportTimeEnd());
        }
        // 問題單觀察期-起日
        if (StringUtils.isNotBlank(vo.getObservationStart())) {
            conditions.and().gtEqual("CONVERT(date, d.Observation, 111)", vo.getObservationStart());
        }
        // 問題單觀察期-迄日
        if (StringUtils.isNotBlank(vo.getObservationEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.Observation, 111)", vo.getObservationEnd());
        }
        // 事件(當下)排除時間-起日
        if (StringUtils.isNotBlank(vo.getExcludeTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, d.ExcludeTime, 111)", vo.getExcludeTimeStart());
        }
        // 事件(當下)排除時間-迄日
        if (StringUtils.isNotBlank(vo.getExcludeTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.ExcludeTime, 111)", vo.getExcludeTimeEnd());
        }
        // 同一事件兩日內復發
        if (StringUtils.isNotBlank(vo.getIsSameInc())) {
            conditions.and().equal("IsSameInc", vo.getIsSameInc());
        }
        // 上線失敗
        if (StringUtils.isNotBlank(vo.getIsOnlineFail())) {
            conditions.and().equal("IsOnlineFail", vo.getIsOnlineFail());
        }
        // 上線時間
        if (StringUtils.isNotBlank(vo.getOnlineTime())) {
            conditions.and().gtEqual("CONVERT(date, d.OnlineTime, 111)", vo.getOnlineTime());
        }
        // (上線失敗的)工作單單號
        if (StringUtils.isNotBlank(vo.getOnlineJobFormId())) {
            conditions.and().like("OnlineJobFormId", vo.getOnlineJobFormId());
        }
        // 服務類別
        if (StringUtils.isNotBlank(vo.getsClass())) {
            conditions.and().equal("SClass", vo.getsClass());
        }
        // 服務子類別
        if (StringUtils.isNotBlank(vo.getsSubClass())) {
            conditions.and().equal("SSubClass", vo.getsSubClass());
        }
        // 系統名稱
        if (StringUtils.isNotBlank(vo.getSystem())) {
            conditions.and().like("System", vo.getSystem());
        }
        // 資訊資產群組
        if (StringUtils.isNotBlank(vo.getAssetGroup())) {
            conditions.and().like("AssetGroup", vo.getAssetGroup());
        }
        // 組態分類
        if (StringUtils.isNotBlank(vo.getcCategory())) {
            conditions.and().equal("CCategory", vo.getcCategory());
        }
        // 組態元件類別
        if (StringUtils.isNotBlank(vo.getcClass())) {
            conditions.and().equal("CClass", vo.getcClass());
        }
        // 組態元件
        if (StringUtils.isNotBlank(vo.getcComponent())) {
            conditions.and().equal("CComponent", vo.getcComponent());
        }
        // 影響範圍
        if (StringUtils.isNotBlank(vo.getEffectScope())) {
            conditions.and().like("EffectScope", vo.getEffectScope());
        }
        // 緊急程度
        if (StringUtils.isNotBlank(vo.getUrgentLevel())) {
            conditions.and().like("UrgentLevel", vo.getUrgentLevel());
        }
        // 需求等級
        if (StringUtils.isNotBlank(vo.getRequireRank())) {
            conditions.and().like("RequireRank", vo.getRequireRank());
        }
        // 負責科
        if (StringUtils.isNotBlank(vo.getDivision())) {
            conditions.and().like("Division", vo.getDivision());
        }
        // 會辦科
        if (StringUtils.isNotBlank(vo.getCountersigneds())) {
            conditions.and().like("Countersigneds", vo.getCountersigneds());
        }
        // 工作會辦處理情形
        if (StringUtils.isNotBlank(vo.getcCountersigneds())) {
            conditions.and().like("Countersigneds", vo.getcCountersigneds());
        }
        // 事件來源為IVR
        if (StringUtils.isNotBlank(vo.getIsIVR())) {
            conditions.and().equal("d.IsIVR", vo.getIsIVR());
        }
        // 處理方案
        if (StringUtils.isNotBlank(vo.getProcessProgram())) {
            if (FormEnum.KL.name().equals(formClass)) {
                conditions.and().like("FIDTL.ProcessProgram", vo.getProcessProgram());
            } else {
                conditions.and().like("fp.ProcessProgram", vo.getProcessProgram());
            }
        }
        // 徵兆
        if (StringUtils.isNotBlank(vo.getIndication())) {
            if (FormEnum.KL.name().equals(formClass)) {
                conditions.and().like("FIDTL.Indication", vo.getIndication());
            } else {
                conditions.and().like("fp.Indication", vo.getIndication());
            }
        }
        // 原因
        if (StringUtils.isNotBlank(vo.getReason())) {
            if (FormEnum.KL.name().equals(formClass)) {
                conditions.and().like("FIDTL.Reason", vo.getReason());
            } else {
                conditions.and().like("fp.Reason", vo.getReason());
            }
        }
        // 是否建議加入處理方案
        if (StringUtils.isNotBlank(vo.getIsSuggestCase())) {
            conditions.and().equal("fp.IsSuggestCase", vo.getIsSuggestCase());
        }
        
        // 暫時性解決方案，且無法於事件目標解決時間內根本解決者?
        if (FormEnum.INC.name().equals(formClass) && 
                StringUtils.isNotBlank(vo.getIsForward())) {
            conditions.and().equal("u.IsForward", vo.getIsForward());
        }
        
        // 衝擊分析欄位-----------start-----------
        // 影響評估
        if (StringUtils.isNotBlank(vo.getEvaluation())) {
            conditions.and().like("fiaView.Evaluation", vo.getEvaluation());
        }
        // 因應措施
        if (StringUtils.isNotBlank(vo.getSolution())) {
            conditions.and().like("fiaView.Solution", vo.getSolution());
        }
        // 分數
        if (StringUtils.isNotBlank(vo.getTotal())) {
            conditions.and().equal("fiaView.Total", vo.getTotal());
        }
        // 衝擊分析欄位------------end------------
        // 工作單欄位-----------start-----------
        if (StringUtils.isNotBlank(vo.getaStatus())) {
            conditions.and().like("ap.AStatus", vo.getaStatus());
        }
        // PRODUCTION
        if (StringUtils.isNotBlank(vo.getIsProduction())) {
            conditions.and().equal("ap.IsProduction", vo.getIsProduction());
        }
        // TEST
        if (StringUtils.isNotBlank(vo.getIsTest())) {
            conditions.and().equal("ap.IsTest", vo.getIsTest());
        }
        // 全部功能中斷
        if (StringUtils.isNotBlank(vo.getIsInterrupt())) {
            conditions.and().equal("ap.IsInterrupt", vo.getIsInterrupt());
        }
        // 回復原廠設定
        if (StringUtils.isNotBlank(vo.getIsReset())) {
            conditions.and().equal("ap.IsReset", vo.getIsReset());
        }
        // 工作組別
        if (StringUtils.isNotBlank(vo.getWorking())) {
            conditions.and().equal("ap.Working", vo.getWorking());
        }
        // 先處理後呈閱
        if (StringUtils.isNotBlank(vo.getIsHandleFirst())) {
            conditions.and().equal("ap.IsHandleFirst", vo.getIsHandleFirst());
        }
        // 上線修正
        if (StringUtils.isNotBlank(vo.getIsCorrect())) {
            conditions.and().equal("ap.IsCorrect", vo.getIsCorrect());
        }
        // 新增系統功能
        if (StringUtils.isNotBlank(vo.getIsAddFuntion())) {
            conditions.and().equal("ap.IsAddFuntion", vo.getIsAddFuntion());
        }
        // 新增報表
        if (StringUtils.isNotBlank(vo.getIsAddReport())) {
            conditions.and().equal("ap.IsAddReport", vo.getIsAddReport());
        }
        // 新增檔案
        if (StringUtils.isNotBlank(vo.getIsAddFile())) {
            conditions.and().equal("ap.IsAddFile", vo.getIsAddFile());
        }
        // 程式上線
        if (StringUtils.isNotBlank(vo.getIsProgramOnline())) {
            conditions.and().equal("ap.IsProgramOnline", vo.getIsProgramOnline());
        }
        // 作業目的
        if (StringUtils.isNotBlank(vo.getPurpose())) {
            conditions.and().like("ap.Purpose", vo.getPurpose());
        }
        
        // 送交組態人員
        if (StringUtils.isNotBlank(vo.getIsForward()) && 
                (FormEnum.JOB_AP.name().equals(formClass) || FormEnum.JOB_AP_C.name().equals(formClass))) {
            conditions.and().equal("ap.IsForward", vo.getIsForward());
        }
        
        // 送交監督人員
        if (StringUtils.isNotBlank(vo.getIsWatching())) {
            conditions.and().equal("ap.IsWatching", vo.getIsWatching());
        }
        // 會造成服務中斷或需要停機，屬於計畫性系統維護
        if (StringUtils.isNotBlank(vo.getIsPlaning())) {
            conditions.and().equal("d.IsPlaning", vo.getIsPlaning());
        }
        // 會造成服務中斷或需要停機，屬於非計畫性系統維護
        if (StringUtils.isNotBlank(vo.getIsUnPlaning())) {
            conditions.and().equal("d.IsUnPlaning", vo.getIsUnPlaning());
        }
        // 公告停機時間-起日
        if (StringUtils.isNotBlank(vo.getOffLineTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, d.OffLineTime, 111)", vo.getOffLineTimeStart());
        }
        // 公告停機時間-迄日
        if (StringUtils.isNotBlank(vo.getOffLineTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.OffLineTime, 111)", vo.getOffLineTimeEnd());
        }
        // 中斷起始時間-起日
        if (StringUtils.isNotBlank(vo.getIstStart())) {
            conditions.and().gtEqual("CONVERT(date, d.IST, 111)", vo.getIstStart());
        }
        // 中斷起始時間-迄日
        if (StringUtils.isNotBlank(vo.getIstEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.IST, 111)", vo.getIstEnd());
        }
        // 中斷結束時間-起日
        if (StringUtils.isNotBlank(vo.getIctStart())) {
            conditions.and().gtEqual("CONVERT(date, d.ICT, 111)", vo.getIctStart());
        }
        // 中斷結束時間-迄日
        if (StringUtils.isNotBlank(vo.getIctEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.ICT, 111)", vo.getIctEnd());
        }
        // 測試系統完成時間 -起日
        if (StringUtils.isNotBlank(vo.getTctStart())) {
            conditions.and().gtEqual("CONVERT(date, d.TCT, 111)", vo.getTctStart());
        }
        // 測試系統完成時間 -迄日
        if (StringUtils.isNotBlank(vo.getTctEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.TCT, 111)", vo.getTctEnd());
        }
        // 連線系統完成時間 -起日
        if (StringUtils.isNotBlank(vo.getSctStart())) {
            conditions.and().gtEqual("CONVERT(date, d.SCT, 111)", vo.getSctStart());
        }
        // 連線系統完成時間 -迄日
        if (StringUtils.isNotBlank(vo.getSctEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.SCT, 111)", vo.getSctEnd());
        }
        // 連線系統實施時間 -起日
        if (StringUtils.isNotBlank(vo.getSitStart())) {
            conditions.and().gtEqual("CONVERT(date, d.SIT, 111)", vo.getSitStart());
        }
        // 連線系統實施時間 -迄日
        if (StringUtils.isNotBlank(vo.getSitEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.SIT, 111)", vo.getSitEnd());
        }
        // 工作單欄位------------end------------
        // 工作單會辦欄位-----------start-----------
        if (StringUtils.isNotBlank(vo.getcType())) {// DC科
            FormJobEnum jobEnum = FormJobEnum.valueOf(vo.getcType());
            
            if (FormJobEnum.DB == jobEnum) {
                conditions.and().equal("FJW.Type", vo.getcType());

            	if (isJobApc) {
            		conditions
            			.and()
            			.isNull("FJB.FormId")
            			.and()
            			.isNull("FJC.DataType");
            	}
            } else if (FormJobEnum.BATCH == jobEnum) {
            	conditions.and().notNull("FJB.FormId");
            } else {// DC123頁簽
            	conditions.and().equal("FJC.Division", vo.getcType());

            	if (isJobApc) {
            		conditions
            			.and()
            			.isNull("FJW.Type")
            			.and()
            			.isNull("FJB.FormId");
            	}
            }
        } else if ((isJobAp || isJobApc) && 
                FormJobEnum.DC.name().equals(vo.getDivision())) {// DC科全部
            conditions
            .and()
            .leftPT()
            .notNull("FJW.Type")
            .or()
            .notNull("FJC.DataType")
            .or()
            .leftPT()
            .isNull("FJW.Type")
            .and()
            .isNull("FJC.DataType")
            .and()
            .notNull("FJB.FormId")
            .RightPT()
            .RightPT();
        } else if (StringUtils.isNotBlank(vo.getcDivision()) && 
        		!FormJobEnum.DC.name().equals(vo.getcDivision())) {// 其他科
            conditions.and().equal("FJC.Division", vo.getcDivision());
        }
        
        if (StringUtils.isNotBlank(vo.getcDataType())) {
            conditions.and().equal("FJC.DataType", vo.getcDataType());
        }
        if (StringUtils.isNotBlank(vo.getcBookNumber())) {
            conditions.and().equal("FJC.BookNumber", vo.getcBookNumber());
        }
        if (StringUtils.isNotBlank(vo.getcOnlyNumber())) {
            conditions.and().equal("FJC.OnlyNumber", vo.getcOnlyNumber());
        }
        if (StringUtils.isNotBlank(vo.getcOnlyCode())) {
            conditions.and().equal("FJC.OnlyCode", vo.getcOnlyCode());
        }
        if (StringUtils.isNotBlank(vo.getcLinkNumber())) {
            conditions.and().equal("FJC.LinkNumber", vo.getcLinkNumber());
        }
        if (StringUtils.isNotBlank(vo.getcLinkCode())) {
            conditions.and().equal("FJC.LinkCode", vo.getcLinkCode());
        }
        if (StringUtils.isNotBlank(vo.getcLinkOnly())) {
            conditions.and().equal("FJC.LinkOnly", vo.getcLinkOnly());
        }
        if (StringUtils.isNotBlank(vo.getcLinkOnlyNumber())) {
            conditions.and().equal("FJC.LinkOnlyNumber", vo.getcLinkOnlyNumber());
        }
        if (StringUtils.isNotBlank(vo.getcRollbackDesc())) {
            conditions.and().like("FJC.RollbackDesc", vo.getcRollbackDesc());
        }
        if (StringUtils.isNotBlank(vo.getcBook())) {
            conditions.and().like("FJC.Book", vo.getcBook());
        }
        if (StringUtils.isNotBlank(vo.getcOnly())) {
            conditions.and().like("FJC.Only", vo.getcOnly());
        }
        if (StringUtils.isNotBlank(vo.getcLink())) {
            conditions.and().like("FJC.Link", vo.getcLink());
        }
        if (StringUtils.isNotBlank(vo.getcDbName())) {
            conditions.and().like("FJW.DBName", vo.getcDbName());
        }
        if (StringUtils.isNotBlank(vo.getcPsbName())) {
            conditions.and().like("FJW.PSBName", vo.getcPsbName());
        }
        if (StringUtils.isNotBlank(vo.getcAlterWay())) {
            conditions.and().like("FJW.AlterWay", vo.getcAlterWay());
        }
        if (StringUtils.isNotBlank(vo.getCbUserId())) {
            conditions.and().like("FJB.UserId", vo.getCbUserId());
        }
        if (StringUtils.isNotBlank(vo.getcOther())) {
            conditions.and().like("FJC.Other", vo.getcOther());
        }
        if (StringUtils.isNotBlank(vo.getCbOther())) {
            conditions.and().like("FJB.Other", vo.getCbOther());
        }
        if (StringUtils.isNotBlank(vo.getcRemark())) {
            conditions.and().like("FJW.Remark", vo.getcRemark());
        }
        if (StringUtils.isNotBlank(vo.getCbRemark())) {
            conditions.and().like("FJB.Remark", vo.getCbRemark());
        }
        if (StringUtils.isNotBlank(vo.getcPsb())) {
            conditions.and().like("FJB.PSB", vo.getcPsb());
        }
        if (StringUtils.isNotBlank(vo.getcProgramName())) {
            conditions.and().like("FJB.ProgramName", vo.getcProgramName());
        }
        if (StringUtils.isNotBlank(vo.getcProgramNumber())) {
            conditions.and().like("FJB.ProgramNumber", vo.getcProgramNumber());
        }
        if (StringUtils.isNotBlank(vo.getcJcl())) {
            conditions.and().like("FJB.JCL", vo.getcJcl());
        }
        if (StringUtils.isNotBlank(vo.getcCljcl())) {
            conditions.and().like("FJB.CLJCL", vo.getcCljcl());
        }
        if (StringUtils.isNotBlank(vo.getcReason())) {
            conditions.and().like("FJB.Reason", vo.getcReason());
        }
        if (StringUtils.isNotBlank(vo.getcIsRollback())) {
            conditions.and().equal("FJC.IsRollback", vo.getcIsRollback());
        }
        if (StringUtils.isNotBlank(vo.getCbIsRollback())) {
            conditions.and().equal("FJB.IsRollback", vo.getCbIsRollback());
        }
        if (StringUtils.isNotBlank(vo.getcIsHelp())) {
            conditions.and().equal("FJB.IsHelp", vo.getcIsHelp());
        }
        if (StringUtils.isNotBlank(vo.getcIsChange())) {
            conditions.and().equal("FJB.IsCange", vo.getcIsChange());
        }
        if (StringUtils.isNotBlank(vo.getcIsAllow())) {
            conditions.and().equal("FJB.IsAllow", vo.getcIsAllow());
        }
        if (StringUtils.isNotBlank(vo.getcIsOther())) {
            conditions.and().equal("FJB.IsOther", vo.getcIsOther());
        }
        if (StringUtils.isNotBlank(vo.getcIsHelpCl())) {
            conditions.and().equal("FJB.IsHelpCL", vo.getcIsHelpCl());
        }
        if (StringUtils.isNotBlank(vo.getcIsOtherDesc())) {
            conditions.and().equal("FJB.IsOtherDesc", vo.getcIsOtherDesc());
        }
        // 程式CL及工作執行日期-起日
        if (StringUtils.isNotBlank(vo.getcItStart())) {
            conditions.and().gtEqual("CONVERT(date, FJB.IT, 111)", vo.getcItStart());
        }
        // 程式CL及工作執行日期-迄日
        if (StringUtils.isNotBlank(vo.getcItEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJB.IT, 111)", vo.getcItEnd());
        }
        // 預計實施日期-起日
        if (StringUtils.isNotBlank(vo.getcEitStart())) {
            conditions.and().gtEqual("CONVERT(date, FJW.EIT, 111)", vo.getcEitStart());
        }
        // 預計實施日期-迄日
        if (StringUtils.isNotBlank(vo.getcEitEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJW.EIT, 111)", vo.getcEitEnd());
        }
        // 實際實施日期-起日
        if (StringUtils.isNotBlank(vo.getcAstStart())) {
            conditions.and().gtEqual("CONVERT(date, FJW.AST, 111)", vo.getcAstStart());
        }
        // 實際實施日期-迄日
        if (StringUtils.isNotBlank(vo.getcAstEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJW.AST, 111)", vo.getcAstEnd());
        }
        if (StringUtils.isNotBlank(vo.getcUserId())) {
            if (FormEnum.JOB_SP_C.name().equals(vo.getFormClass())) {
                conditions.and().like("ap.CUserId", vo.getcUserId());
            } else {
                conditions.and().like("FJC.UserId", vo.getcUserId());
            }
        }
        // 測試系統完成日期-起日
        if (StringUtils.isNotBlank(vo.getcTctStart())) {
            conditions.and().gtEqual("CONVERT(date, FJC.TCT, 111)", vo.getcTctStart());
        }
        // 測試系統完成日期-迄日
        if (StringUtils.isNotBlank(vo.getcTctEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJC.TCT, 111)", vo.getcTctEnd());
        }
        // 連線系統完成日期-起日
        if (StringUtils.isNotBlank(vo.getcSctStart())) {
            conditions.and().gtEqual("CONVERT(date, FJC.SCT, 111)", vo.getcSctStart());
        }
        // 連線系統完成日期-迄日
        if (StringUtils.isNotBlank(vo.getcSctEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJC.SCT, 111)", vo.getcSctEnd());
        }
        // 連線系統實施日期-起日
        if (StringUtils.isNotBlank(vo.getcSitStart())) {
            conditions.and().gtEqual("CONVERT(date, FJC.SIT, 111)", vo.getcSitStart());
        }
        // 連線系統實施日期-迄日
        if (StringUtils.isNotBlank(vo.getcSitEnd())) {
            conditions.and().ltEqual("CONVERT(date, FJC.SIT, 111)", vo.getcSitEnd());
        }
        if (StringUtils.isNotBlank(vo.getcDescription())) {
            conditions.and().like("FJC.Description", vo.getcDescription());
        }
        if (StringUtils.isNotBlank(vo.getcIsTest())) {
            conditions.and().equal("FJC.IsTest", vo.getcIsTest());
        }
        if (StringUtils.isNotBlank(vo.getcIsProduction())) {
            conditions.and().equal("FJC.IsProduction", vo.getcIsProduction());
        }
        // 工作單會辦欄位------------end------------
        
        if (StringUtils.isNotBlank(vo.getBatchName())) {
            conditions.and().like("FIDTL.BatchName", vo.getBatchName());
        }
        if (StringUtils.isNotBlank(vo.getExecuteTime())) {
            conditions.and().equal("FIDTL.ExecuteTime", vo.getExecuteTime());
        }
        if (StringUtils.isNotBlank(vo.getDbInUse())) {
            conditions.and().equal("FIDTL.DbInUse", vo.getDbInUse());
        }
        if (StringUtils.isNotBlank(vo.getEffectDateStart())) {
            conditions.and().gtEqual("CONVERT(date, FIDTL.EffectDate, 111)", vo.getEffectDateStart());
        }
        if (StringUtils.isNotBlank(vo.getEffectDateEnd())) {
            conditions.and().ltEqual("CONVERT(date, FIDTL.EffectDate, 111)", vo.getEffectDateEnd());
        }
        
        List<FormSearchVO> formRecords = new ArrayList<>(); 
                
        if (isExport) {
            conditions.orderBy("f.CreateTime", SQL.DESC);
            formRecords.addAll(jdbcRepositoy.queryForList(resource, conditions, FormSearchVO.class));
        } else {
            formRecords.addAll(jdbcRepositoy.queryForList(resource, conditions, vo, FormSearchVO.class));   
        }

        // 查詢其他頁簽欄位(日誌、關聯表單、處理方案等)
        connectOtherPage(formRecords, vo, isExport);
        
        // 資料處理
        for (FormSearchVO form : formRecords) {
            form.setFormClass(getMessage(FormEnum.valueOf(form.getFormClass()).wording()));
            form.setFormStatus(FormEnum.valueOf(form.getFormStatus()).status());
        }

        return formRecords;
    }

    @ModLog
    @Override
    public void exportExcel(FormSearchVO vo, List<FormSearchVO> records, HttpServletResponse response) throws IOException {
        try {
            Collection<Object> dataSet = new ArrayList<Object>();
            
            FormEnum clazz = FormEnum.valueOf(vo.getFormClass());
            String fileName = clazz + "_" + DateUtils.
                    toString(new Date(), DateUtils._PATTERN_YYYYMMDD_TIME);
            setHttpServletResponse(response, fileName);
            setDataSet(dataSet, vo, records);// 寫入Excel檔 資料
            
            ExcelUtil.exportExcel(dataSet, response.getOutputStream());
        } catch (IOException e) {
            logger.debug("表單匯出發生未知錯誤。", e);
            throw e;
        }
    }

    @Override
    public List<BaseFormVO> getIncForms(FormSearchVO vo) {
        String formId = vo.getFormId();
        String summary = vo.getSummary();
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_INC.getResource("FIND_INC_FORMS");

        Map<String, Object> params = new HashMap<>();
        
        if (StringUtils.isBlank(formId)) {
            formId = "";
        }
        
        params.put("formId", formId);
        
        if (StringUtils.isBlank(summary)) {
            summary = "";
        }
        
        params.put("summary", summary);
        
        String status;
        List<BaseFormVO> resultList = jdbcRepository.queryForList(resource, params, BaseFormVO.class);
        
        for (BaseFormVO incs : resultList) {
            status = incs.getFormStatus();
            incs.setFormStatus(FormEnum.valueOf(status).status());
        }

        return resultList;
    }
    
    @Override
    public List<FormSearchVO> getIncFormsByCondition(FormSearchVO vo) {
        
        ResourceEnum resource = ResourceEnum.SQL_FORM_OPERATION_INC.getResource("FIND_INC_FORMS_BY_CONDITION");
        
        Conditions conditions = new Conditions();        
        // 起日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
        if (StringUtils.isNotBlank(vo.getInfoDateCreateTimeStart())) {
            conditions.and().gtEqual("CONVERT(date, d.CreatedAt, 111)", vo.getInfoDateCreateTimeStart());
        }
        // 迄日-開單時間、建立日期、事件發生時間、變更申請時間、表單分派時間
        if (StringUtils.isNotBlank(vo.getInfoDateCreateTimeEnd())) {
            conditions.and().ltEqual("CONVERT(date, d.CreatedAt, 111)", vo.getInfoDateCreateTimeEnd());
        }
        
        List<FormSearchVO> resultList = jdbcRepositoy.queryForList(resource,conditions,FormSearchVO.class);
        return resultList;
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setDataSet (Collection<Object> dataSet, FormSearchVO vo, List<FormSearchVO> records) {
        List<String> header = vo.getColumnOrder();
        List<String> typeRange = new ArrayList<>();
        List<String> columnName = new ArrayList<>();
        List<JSONObject> contentList = new ArrayList<>();

        for (String voName : header) {
            Map<String, String> columnInfo = FormFieldsEnum.
                    getColumnInfo(voName, FormEnum.valueOf(vo.getFormClass()));
            columnName.add(this.getMessage(columnInfo.get("i18nKey")));

            if (FormFields.DATE_RANGE.equals(columnInfo.get("type"))) {
                typeRange.add(voName);
            }
        }
        
        for (FormSearchVO record : records) {
            contentList.add(new JSONObject(BeanUtil.toJson(record)));
        }

        // 標題
        dataSet.add(new ExcelModel((Object[]) columnName.toArray(new String[columnName.size()])));
        
        // 內容
        String dataStr;
        Map<String, String> knowledge;
        List<Map<String, String>> knowledges;
        Object cType, cDataType, cbFormId, kObj;
        
        for (JSONObject content : contentList) {
            List<String> data = new ArrayList<>();
            for (String voName : header) {
                dataStr = "";
                
                if (voName.equalsIgnoreCase("createTime") || typeRange.contains(voName)) {
                    data.add(jsonValueToDateStr(content.get(voName)));
                } else {
                	cType = content.get(voName);
                	
                	if ("cType".equals(voName)) {// DC科
                		cbFormId = content.get("cbFormId");
                		cDataType = content.get("cDataType");
                		
						if (StringUtils.isBlank(vo.getcType())) {// 全部
                            if (JSONObject.NULL != cType &&
                                    StringUtils.isNotBlank((String) cType)) {
                                dataStr += "/" + (String) cType;
                            }
                            
                            if (JSONObject.NULL != cDataType &&
                                    StringUtils.isNotBlank((String) cDataType)) {
                                dataStr += "/" + (String) cDataType;
                            }
                            
                            if (JSONObject.NULL != cbFormId &&
                                    StringUtils.isNotBlank((String) cbFormId)) {
                                dataStr += "/" + getMessage("form.title.tabBatch");
                            }
                            
                            data.add(dataStr.substring(1, dataStr.length()));
						} else {
						    if (JSONObject.NULL != cbFormId &&
	                                StringUtils.isNotBlank((String) cbFormId) &&
	                                FormJobEnum.BATCH.name().equals(vo.getcType())) {
	                            data.add(getMessage("form.title.tabBatch"));
	                        } else if (JSONObject.NULL != cType &&
                                    StringUtils.isNotBlank((String) cType) &&
                                    FormJobEnum.DB.name().equals(vo.getcType())) {
                                data.add((String) cType);   
	                        } else if (JSONObject.NULL != cDataType &&
                                    StringUtils.isNotBlank((String) cDataType)) {
                                data.add((String) cDataType);    
	                        }
						}
             		} else if ("knowledges".equals(voName)) {
         		        kObj = content.get(voName);
             		   
                        if (kObj != null) {
                            knowledges = BeanUtil.fromJsonToList((String) kObj, Map.class);
                            
                            for (int i = 0; i < knowledges.size(); i++) {
                                knowledge = knowledges.get(i);
                                dataStr += MapUtils.getString(knowledge, "knowledge1Display", "") + " : " + MapUtils.getString(knowledge, "knowledge2Display", "");
                                if (i < knowledges.size()-1) dataStr += "\n";
                            }
                        }
                        
                        data.add(dataStr);
             		} else {
             			data.add(content.get(voName).toString());
             		}
                }
            }

            dataSet.add(new ExcelModel(
                    (Object[]) data.toArray(new String[data.size()])));
        }
    }

    private String jsonValueToDateStr(Object time) {
        String dateStr = "";

        if (StringUtils.isNotBlank(time.toString()) && 
                !"null".equalsIgnoreCase(time.toString())) {
            dateStr = DateUtils.toString(new Date((Long) time), DateUtils.pattern12);
        }

        return dateStr;
    }

    private void connectOtherPage (List<FormSearchVO> records, FormSearchVO vo, boolean isExport) {
        for (FormSearchVO record : records) {
            record.setFormFile(splitSignToBreakLine(record.getFormFile(), isExport));
            
            if (isExport) {
                record.setAssociationForm(splitSignToBreakLine(record.getAssociationForm(), isExport));
            }
            
            record.setFormUserRecord(splitSignToBreakLine(record.getFormUserRecord(), isExport));
        }
    }

    private String splitSignToBreakLine (String dataStr, boolean isExport) {
        if (StringUtils.isBlank(dataStr)) {
            dataStr = "";
        } else {
            dataStr = dataStr.replace("[-]", isExport ? "\r\n" : "<br>");
        }
        
        return dataStr;
    }

    private void setHttpServletResponse(HttpServletResponse response, String fileName) {
        response.setContentType(StringConstant.SET_CONTENT_TYPE);
        response.setHeader(
                StringConstant.SET_HEADER_COOKIE, 
                StringConstant.SET_HEADER_FILEDOWNLOAD_PATH);
        response.setHeader(
                StringConstant.SET_HEADER_CONTENT_DISPOSITION,
                StringConstant.SET_HEADER_ATTACHMENT_FILENAME + fileName + StringConstant.DOT + StringConstant.EXCEL_SEXTENSION);
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(FormSearchVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(FormSearchVO vo) {
        return null;
    }

    @Override
    @Deprecated
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        return null;
    }

    @Override
    @Deprecated
    public FormSearchVO getFormDetailInfo(String formId) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String getReviewLastLevel (String detailId) {
        return null;
    }

}
