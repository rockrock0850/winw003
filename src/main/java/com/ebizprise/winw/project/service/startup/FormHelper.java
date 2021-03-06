package com.ebizprise.winw.project.service.startup;

import java.util.EnumMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.config.ApplicationContextHelper;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.validation.form.impl.APCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.APValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.BAValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.CHGValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.INCCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.INCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.KLValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.QCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.QValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.SPCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.SPValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.SRCValidateColHelper;
import com.ebizprise.winw.project.validation.form.impl.SRValidateColHelper;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentFormVo;
import com.ebizprise.winw.project.vo.CountersignedFormVO;

/**
 * ???????????????????????????????????????????????????????????????<br>
 * P.S. ?????????????????????????????????SWITCH CASE???????????????????????????
 *
 * @author adam.yeh
 *
 */
@Service("formHelper")
public class FormHelper extends BaseService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private QValidateColHelper qvHelper;
    @Autowired
    private SRValidateColHelper srvHelper;
    @Autowired
    private INCValidateColHelper incvHelper;
    @Autowired
    private CHGValidateColHelper chgvHelper;
    @Autowired
    private QCValidateColHelper qcvHelper;
    @Autowired
    private SRCValidateColHelper srcvHelper;
    @Autowired
    private INCCValidateColHelper inccvHelper;
    @Autowired
    private SPValidateColHelper spvHelper;
    @Autowired
    private APValidateColHelper apvHelper;
    @Autowired
    private SPCValidateColHelper spcvHelper;
    @Autowired
    private APCValidateColHelper apcvHelper;
    @Autowired
    private BAValidateColHelper bavHelper;
    @Autowired
    private KLValidateColHelper klvHelper;

    private EnumMap<FormEnum, String> formService = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formClassSQLMap = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formParallelMap = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> applyInfoResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> reviewInfoResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> applyGroupResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> reviewGroupResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> newerProcessResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formParallelNewestMap = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formInfoDetailService = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formProcessMaxResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> applyLimitListResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> reviewLimitListResource = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> formProcessDetailService = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, String> countersignedMailService = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, ResourceEnum> formResourceMapping = new EnumMap<>(FormEnum.class);
    private EnumMap<FormEnum, FormColumnValidate> formColumnValidateMap = new EnumMap<>(FormEnum.class);

    @PostConstruct
    public void startup () {
        setupFormParallelMap();
        setupFormColumnValidateMap();
        setupFormProcessMaxResource();
        setupNewerProcessResource();
        setupFormResourceMapping();
        setupLimitListResource();
        setupLevelInfoResource();
        setupGroupResource();
        setupCountersignedService();
        setupFormProcessDetailService();
        setupFormInfoDetailService();
        setupFormService();
        setupFormClassSQLMap();
    }

    /**
     * ?????????????????????????????????
     * @return
     * @author adam.yeh
     */
    public ResourceEnum getFormParallels (String formId, FormEnum clazz) {
        ResourceEnum resource;
        ResourceEnum mapping = formResourceMapping.get(clazz);
        
        if (StringUtils.isBlank(formId)) {
            mapping = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT;
            resource = mapping.getResource(formParallelNewestMap.get(clazz));
        } else {
            resource = mapping.getResource(formParallelMap.get(clazz));
        }
        
        return resource;
    }

    /**
     * ??????????????????????????????
     * @param clazz
     * @return
     * @author adam.yeh
     */
    public FormColumnValidate getFormColumnValidateHelper (FormEnum clazz) {
        return formColumnValidateMap.get(clazz);
    }

    /**
     * ?????????????????????????????????????????????
     * @return
     */
    public ResourceEnum getFormProcessMaxResource (FormEnum clazz, FormEnum verifyType) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(formProcessMaxResource.get(verifyType));
    }

    /**
     * ?????????????????????SQL?????????
     * @return
     */
    public ResourceEnum getNewerProcessResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(newerProcessResource.get(clazz));
    }

    /**
     * ????????????????????????????????????SQL?????????
     * @return
     */
    public ResourceEnum getApplyLimitResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(applyLimitListResource.get(clazz));
    }

    /**
     * ????????????????????????????????????SQL?????????
     * @return
     */
    public ResourceEnum getReviewLimitResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(reviewLimitListResource.get(clazz));
    }

    /**
     * ??????????????????????????????SQL?????????
     * @param clazz
     * @param verifyType
     * @return
     */
    public ResourceEnum getLimitResource (FormEnum clazz, FormEnum verifyType) {
        ResourceEnum resource;

        switch (verifyType) {
            case APPLY:
                resource = getApplyLimitResource(clazz);
                break;

            case REVIEW:
                resource = getReviewLimitResource(clazz);
                break;

            default:
                resource = null;
                break;
        }

        return resource;
    }

    /**
     * ?????????????????????????????????SQL?????????
     * @param clazz
     * @return
     */
    public ResourceEnum getApplyGroupResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(applyGroupResource.get(clazz));
    }

    /**
     * ?????????????????????????????????SQL?????????
     * @param clazz
     * @return
     */
    public ResourceEnum getReviewGroupResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(reviewGroupResource.get(clazz));
    }

    /**
     * ??????????????????????????????????????????SQL?????????
     * @param clazz
     * @param verifyType
     * @return
     */
    public ResourceEnum getLevelInfoResource (FormEnum clazz, FormEnum verifyType) {
        ResourceEnum resource = null;

        switch (verifyType) {
            case APPLY:
                resource = getApplyGroupResource(clazz);
                break;

            case REVIEW:
                resource = getReviewGroupResource(clazz);
                break;

            default:
                break;
        }

        return resource;
    }

    /**
     * ???????????????????????????SQL?????????
     * @param clazz
     * @return
     */
    public ResourceEnum getApplyInfoResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(applyInfoResource.get(clazz));
    }

    /**
     * ???????????????????????????SQL?????????
     * @param clazz
     * @return
     */
    public ResourceEnum getReviewInfoResource (FormEnum clazz) {
        ResourceEnum resource = formResourceMapping.get(clazz);
        return resource.getResource(reviewInfoResource.get(clazz));
    }

    /**
     * ????????????????????????????????????
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public IBaseCountersignedFormService<CountersignedFormVO> getCountersignedMailService (FormEnum clazz){
        return (IBaseCountersignedFormService<CountersignedFormVO>)
                ApplicationContextHelper.getBean(countersignedMailService.get(clazz));
    }

    /**
     * ?????????????????????????????????
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public IFormProcessManagmentService<BaseFormProcessManagmentFormVo> getFormProcessDetailService(FormEnum clazz) {
        return (IFormProcessManagmentService<BaseFormProcessManagmentFormVo>) ApplicationContextHelper.getBean(formProcessDetailService.get(clazz));
    }

    /**
     * ???????????????????????????????????????
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public BaseFormService<BaseFormVO> getFormInfoDetailService(FormEnum clazz) {
        return (BaseFormService<BaseFormVO>) ApplicationContextHelper.getBean(formInfoDetailService.get(clazz));
    }

    /**
     * ???????????????????????????
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public IBaseFormService<BaseFormVO> getFormService (FormEnum clazz) {
        return (IBaseFormService<BaseFormVO>) ApplicationContextHelper.getBean(formService.get(clazz));
    }

    /**
     * ????????????????????????-?????????????????????SQL??????
     * @param clazz
     * @return
     */
    public String getFormClassSQLMap(FormEnum clazz) {
        return formClassSQLMap.get(clazz);
    }

    // ????????????????????????
    private void setupFormParallelMap () {
        formParallelMap.put(FormEnum.Q_C, "FIND_Q_PARALLEL_LIST");
        formParallelMap.put(FormEnum.SR_C, "FIND_SR_PARALLEL_LIST");
        formParallelMap.put(FormEnum.JOB_SP_C, "FIND_JOB_PARALLEL_LIST");
        formParallelNewestMap.put(FormEnum.Q_C, "FIND_FORM_PARALLEL_Q_NEWST");
        formParallelNewestMap.put(FormEnum.SR_C, "FIND_FORM_PARALLEL_SR_NEWST");
        formParallelNewestMap.put(FormEnum.JOB_SP_C, "FIND_FORM_PARALLEL_JOB_NEWST");
    }

    // ??????????????????????????????????????????????????????
    private void setupFormColumnValidateMap () {
        formColumnValidateMap.put(FormEnum.Q, qvHelper);
        formColumnValidateMap.put(FormEnum.SR, srvHelper);
        formColumnValidateMap.put(FormEnum.INC, incvHelper);
        formColumnValidateMap.put(FormEnum.CHG, chgvHelper);
        formColumnValidateMap.put(FormEnum.Q_C, qcvHelper);
        formColumnValidateMap.put(FormEnum.SR_C, srcvHelper);
        formColumnValidateMap.put(FormEnum.INC_C, inccvHelper);
        formColumnValidateMap.put(FormEnum.JOB_SP, spvHelper);
        formColumnValidateMap.put(FormEnum.JOB_AP, apvHelper);
        formColumnValidateMap.put(FormEnum.JOB_SP_C, spcvHelper);
        formColumnValidateMap.put(FormEnum.JOB_AP_C, apcvHelper);
        formColumnValidateMap.put(FormEnum.BA, bavHelper);
        formColumnValidateMap.put(FormEnum.KL, klvHelper);
    }

    private void setupFormProcessMaxResource () {
        formProcessMaxResource.put(FormEnum.APPLY, "FIND_JOB_APPLY_PROCESS_MAX_ORDER");
        formProcessMaxResource.put(FormEnum.REVIEW, "FIND_JOB_REVIEW_PROCESS_MAX_ORDER");
    }

    private void setupLevelInfoResource () {
        applyInfoResource.put(FormEnum.Q, "FIND_APPLY_Q_INFO");
        applyInfoResource.put(FormEnum.Q_C, "FIND_APPLY_Q_INFO");
        applyInfoResource.put(FormEnum.SR, "FIND_APPLY_SR_INFO");
        applyInfoResource.put(FormEnum.SR_C, "FIND_APPLY_SR_INFO");
        applyInfoResource.put(FormEnum.INC, "FIND_APPLY_INC_INFO");
        applyInfoResource.put(FormEnum.INC_C, "FIND_APPLY_INC_INFO");
        applyInfoResource.put(FormEnum.CHG, "FIND_APPLY_CHG_INFO");
        applyInfoResource.put(FormEnum.JOB_AP, "FIND_APPLY_JOB_INFO");
        applyInfoResource.put(FormEnum.JOB_SP, "FIND_APPLY_JOB_INFO");
        applyInfoResource.put(FormEnum.JOB_AP_C, "FIND_APPLY_JOB_INFO");
        applyInfoResource.put(FormEnum.JOB_SP_C, "FIND_APPLY_JOB_INFO");
        applyInfoResource.put(FormEnum.BA, "FIND_APPLY_BA_INFO");
        
        reviewInfoResource.put(FormEnum.Q, "FIND_REVIEW_Q_INFO");
        reviewInfoResource.put(FormEnum.Q_C, "FIND_REVIEW_Q_INFO");
        reviewInfoResource.put(FormEnum.SR, "FIND_REVIEW_SR_INFO");
        reviewInfoResource.put(FormEnum.SR_C, "FIND_REVIEW_SR_INFO");
        reviewInfoResource.put(FormEnum.INC, "FIND_REVIEW_INC_INFO");
        reviewInfoResource.put(FormEnum.INC_C, "FIND_REVIEW_INC_INFO");
        reviewInfoResource.put(FormEnum.CHG, "FIND_REVIEW_CHG_INFO");
        reviewInfoResource.put(FormEnum.JOB_AP, "FIND_REVIEW_JOB_INFO");
        reviewInfoResource.put(FormEnum.JOB_SP, "FIND_REVIEW_JOB_INFO");
        reviewInfoResource.put(FormEnum.JOB_AP_C, "FIND_REVIEW_JOB_INFO");
        reviewInfoResource.put(FormEnum.JOB_SP_C, "FIND_REVIEW_JOB_INFO");
        reviewInfoResource.put(FormEnum.BA, "FIND_REVIEW_BA_INFO");
    }

    private void setupLimitListResource () {
        applyLimitListResource.put(FormEnum.Q, "FIND_APPLY_Q_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.Q_C, "FIND_APPLY_Q_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.SR, "FIND_APPLY_SR_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.SR_C, "FIND_APPLY_SR_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.INC, "FIND_APPLY_INC_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.INC_C, "FIND_APPLY_INC_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.CHG, "FIND_APPLY_CHG_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.JOB_AP, "FIND_APPLY_JOB_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.JOB_SP, "FIND_APPLY_JOB_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.JOB_AP_C, "FIND_APPLY_JOB_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.JOB_SP_C, "FIND_APPLY_JOB_LIMIT_LIST");
        applyLimitListResource.put(FormEnum.BA, "FIND_APPLY_BA_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.Q, "FIND_REVIEW_Q_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.Q_C, "FIND_REVIEW_Q_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.SR, "FIND_REVIEW_SR_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.SR_C, "FIND_REVIEW_SR_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.INC, "FIND_REVIEW_INC_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.INC_C, "FIND_REVIEW_INC_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.CHG, "FIND_REVIEW_CHG_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.JOB_AP, "FIND_REVIEW_JOB_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.JOB_SP, "FIND_REVIEW_JOB_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.JOB_AP_C, "FIND_REVIEW_JOB_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.JOB_SP_C, "FIND_REVIEW_JOB_LIMIT_LIST");
        reviewLimitListResource.put(FormEnum.BA, "FIND_REVIEW_BA_LIMIT_LIST");
    }

    private void setupGroupResource () {
        applyGroupResource.put(FormEnum.Q, "FIND_Q_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.SR, "FIND_SR_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.INC, "FIND_INC_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.Q_C, "FIND_Q_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.SR_C, "FIND_SR_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.INC_C, "FIND_INC_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.CHG, "FIND_CHG_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.JOB_AP, "FIND_JOB_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.JOB_SP, "FIND_JOB_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.JOB_AP_C, "FIND_JOB_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.JOB_SP_C, "FIND_JOB_ACCEPTABLE_APPLY_GROUP");
        applyGroupResource.put(FormEnum.BA, "FIND_BA_ACCEPTABLE_APPLY_GROUP");
        reviewGroupResource.put(FormEnum.Q, "FIND_Q_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.SR, "FIND_SR_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.INC, "FIND_INC_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.Q_C, "FIND_Q_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.SR_C, "FIND_SR_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.INC_C, "FIND_INC_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.CHG, "FIND_CHG_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.JOB_AP, "FIND_JOB_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.JOB_SP, "FIND_JOB_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.JOB_AP_C, "FIND_JOB_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.JOB_SP_C, "FIND_JOB_ACCEPTABLE_REVIEW_GROUP");
        reviewGroupResource.put(FormEnum.BA, "FIND_BA_ACCEPTABLE_REVIEW_GROUP");
    }

    // ???????????????????????????SQL????????????
    private void setupFormResourceMapping () {
        formResourceMapping.put(FormEnum.Q, ResourceEnum.SQL_FORM_OPERATION_Q);
        formResourceMapping.put(FormEnum.Q_C, ResourceEnum.SQL_FORM_OPERATION_Q);
        formResourceMapping.put(FormEnum.SR, ResourceEnum.SQL_FORM_OPERATION_SR);
        formResourceMapping.put(FormEnum.SR_C, ResourceEnum.SQL_FORM_OPERATION_SR);
        formResourceMapping.put(FormEnum.INC, ResourceEnum.SQL_FORM_OPERATION_INC);
        formResourceMapping.put(FormEnum.CHG, ResourceEnum.SQL_FORM_OPERATION_CHG);
        formResourceMapping.put(FormEnum.INC_C, ResourceEnum.SQL_FORM_OPERATION_INC);
        formResourceMapping.put(FormEnum.JOB_AP, ResourceEnum.SQL_FORM_OPERATION_JOB);
        formResourceMapping.put(FormEnum.JOB_SP, ResourceEnum.SQL_FORM_OPERATION_JOB);
        formResourceMapping.put(FormEnum.JOB_AP_C, ResourceEnum.SQL_FORM_OPERATION_JOB);
        formResourceMapping.put(FormEnum.JOB_SP_C, ResourceEnum.SQL_FORM_OPERATION_JOB);
        formResourceMapping.put(FormEnum.BA, ResourceEnum.SQL_FORM_OPERATION_BA);
    }

    // ??????????????????
    private void setupNewerProcessResource () {
        newerProcessResource.put(FormEnum.Q, "FIND_NEWEST_Q_PROCESS");
        newerProcessResource.put(FormEnum.SR, "FIND_NEWEST_SR_PROCESS");
        newerProcessResource.put(FormEnum.INC, "FIND_NEWEST_INC_PROCESS");
        newerProcessResource.put(FormEnum.CHG, "FIND_NEWEST_CHG_PROCESS");
        newerProcessResource.put(FormEnum.Q_C, "FIND_NEWEST_Q_PROCESS");
        newerProcessResource.put(FormEnum.SR_C, "FIND_NEWEST_SR_PROCESS");
        newerProcessResource.put(FormEnum.INC_C, "FIND_NEWEST_INC_PROCESS");
        newerProcessResource.put(FormEnum.JOB_AP, "FIND_NEWEST_JOB_PROCESS");
        newerProcessResource.put(FormEnum.JOB_SP, "FIND_NEWEST_JOB_PROCESS");
        newerProcessResource.put(FormEnum.JOB_AP_C, "FIND_NEWEST_JOB_PROCESS");
        newerProcessResource.put(FormEnum.JOB_SP_C, "FIND_NEWEST_JOB_PROCESS");
        newerProcessResource.put(FormEnum.BA, "FIND_NEWEST_BA_PROCESS");
    }

    // ??????????????????Service(?????????)
    private void setupCountersignedService () {
        countersignedMailService.put(FormEnum.SR_C, "requirementCFormService");
        countersignedMailService.put(FormEnum.Q_C, "questionCFormService");
        countersignedMailService.put(FormEnum.INC_C, "eventCFormService");
        countersignedMailService.put(FormEnum.JOB_AP_C, "jobAPCFormService");
        countersignedMailService.put(FormEnum.JOB_SP_C, "jobSPCFormService");

    }

    // ?????????????????????Service
    private void setupFormProcessDetailService () {
        formProcessDetailService.put(FormEnum.SR, "formProcessSrService");
        formProcessDetailService.put(FormEnum.Q, "formProcessQuestionService");
        formProcessDetailService.put(FormEnum.INC, "formProcessIncService");
        formProcessDetailService.put(FormEnum.CHG, "formProcessChgService");
        formProcessDetailService.put(FormEnum.SR_C, "formProcessSrService");
        formProcessDetailService.put(FormEnum.Q_C, "formProcessQuestionService");
        formProcessDetailService.put(FormEnum.INC_C, "formProcessIncService");
        formProcessDetailService.put(FormEnum.JOB_AP, "formProcessJobService");
        formProcessDetailService.put(FormEnum.JOB_AP_C, "formProcessJobService");
        formProcessDetailService.put(FormEnum.JOB_SP, "formProcessJobService");
        formProcessDetailService.put(FormEnum.JOB_SP_C, "formProcessJobService");
    }

    private void setupFormService () {
        formService.put(FormEnum.INC, "eventFormService");
        formService.put(FormEnum.Q, "questionFormService");
        formService.put(FormEnum.CHG, "changeFormService");
        formService.put(FormEnum.JOB_AP, "apJobFormService");
        formService.put(FormEnum.JOB_SP, "spJobFormService");
        formService.put(FormEnum.SR, "requirementFormService");
    }

    private void setupFormInfoDetailService () {
        formInfoDetailService.put(FormEnum.SR, "requirementFormService");
        formInfoDetailService.put(FormEnum.Q, "questionFormService");
        formInfoDetailService.put(FormEnum.INC, "eventFormService");
        formInfoDetailService.put(FormEnum.CHG, "changeFormService");
    }

    private void setupFormClassSQLMap() {
        formClassSQLMap.put(FormEnum.Q, "FIND_FORM_Q_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.QA, "FIND_FORM_QA_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.SR, "FIND_FORM_SR_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.INC, "FIND_FORM_INC_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.CHG, "FIND_FORM_CHG_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.Q_C, "FIND_FORM_C_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.SR_C, "FIND_FORM_C_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.INC_C, "FIND_FORM_C_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.JOB_AP, "FIND_FORM_JOB_AP_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.JOB_SP, "FIND_FORM_JOB_SP_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.JOB_AP_C, "FIND_FORM_JOB_AP_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.JOB_SP_C, "FIND_FORM_JOB_SP_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.BA, "FIND_FORM_BA_DETAIL_BY_CONDITION");
        formClassSQLMap.put(FormEnum.KL, "FIND_FORM_KL_DETAIL_BY_CONDITION");
    }

}