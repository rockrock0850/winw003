package com.ebizprise.winw.project.controller.sysmanagment.formprocess;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.vo.FormProcessManagmentBaFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentBaseVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentChgFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentIncFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobApplyVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentQuestionFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentSrFormVO;
import com.ebizprise.winw.project.vo.HtmlVO;

/**
 * ?????????????????? ????????????
 * @author andrew.lee
 * @version 1.0, Created at 2019???6???24???
 */
@Controller
@RequestMapping("/formProcessManagment")
public class FormProcessManagmentBaseController extends BaseController {
    
    private static final String QUERY_CONDITON = "queryCondition";
    private static final Logger logger = LoggerFactory.getLogger(FormProcessManagmentBaseController.class);
    
    @Autowired
    private IFormProcessBaseService formProcessBaseService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentSrFormVO> formProcessSrService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentQuestionFormVO> formProcessQuestionService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentIncFormVO> formProcessIncService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentChgFormVO> formProcessChgService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentBaFormVO> formProcessBaService;
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentJobFormVO> formProcessJobService;
    
    @Autowired
    private IHtmlService htmlService;

    /**
     * ?????????????????? ??????
     * @throws Exception
     * 
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage(HttpSession session) throws Exception {
        List<HtmlVO> formTypeLs =  formProcessBaseService.getFormTypeSelector();
        List<HtmlVO> divisionLs =  formProcessBaseService.getSysGroupSelector();
        
        //????????????????????????????????????????????????
        ModelAndView modelAndView = new ModelAndView(DispatcherEnum.FORM_PROCESS_MANAGMENT.initPage());
        modelAndView.addObject("formTypeLs", formTypeLs);
        modelAndView.addObject("divisionLs", divisionLs);
        
        if(session.getAttribute(QUERY_CONDITON) instanceof FormProcessManagmentBaseVO) {
            modelAndView.addObject(QUERY_CONDITON, BeanUtil.toJson(session.getAttribute(QUERY_CONDITON)));
            session.setAttribute(QUERY_CONDITON, null);
        }
        
        return modelAndView;
    }
    
    /**
     * ??????????????????
     * @return ModelAndView
     */
    @RequestMapping(value = "/addPage" ,method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addPage() {
        List<HtmlVO> formTypeLs =  formProcessBaseService.getFormTypeSelector();
        List<HtmlVO> divisionLs =  formProcessBaseService.getSysGroupSelector();
        ModelAndView modelAndView = new ModelAndView(DispatcherEnum.FORM_PROCESS_MANAGMENT_SR.addPage());
        
        modelAndView.addObject("formTypeLs", formTypeLs);
        modelAndView.addObject("divisionLs", divisionLs);
        modelAndView.addObject("formType", 1);//????????????????????????????????????????????????,???????????????????????????????????????
        modelAndView.addObject("title", this.getMessage("form.process.managment.sr.add.page.title"));
        
        return modelAndView;
    }
    
    /**
     * ??????????????????
     * @param vo
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/editPage/{id}" ,method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView editPage(HttpSession session,FormProcessManagmentBaseVO vo) throws Exception {
        String viewName = "";
        String title = "";
        Long id = vo.getId();
        
        ModelAndView modelAndView = new ModelAndView();
        FormProcessManagmentBaseVO baseVo = formProcessBaseService.getFormProcessById(vo.getId());
        List<HtmlVO> formTypeLs =  formProcessBaseService.getFormTypeSelector();
        List<HtmlVO> divisionLs =  formProcessBaseService.getSysGroupSelector();
        
        FormProcessManagmentResultVO formProcessManagmentFormVo = null;
        
        switch (baseVo.getFormType()) {
            case 1:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_SR.editPage();//?????????
                title = this.getMessage("form.process.managment.sr.edit.page.title");
                
                formProcessManagmentFormVo = formProcessSrService.getFormProcessManagmentFormById(id);
                break;
                
            case 2:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_SR.editPage();//???????????????
                title = this.getMessage("form.process.managment.sr.c.edit.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                
                formProcessManagmentFormVo = formProcessSrService.getFormProcessManagmentFormById(id);
                break;
                
            case 3:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_Q.editPage();//?????????
                title = this.getMessage("form.process.managment.q.edit.page.title");
                
                formProcessManagmentFormVo = formProcessQuestionService.getFormProcessManagmentFormById(id);
                break;
                
            case 4:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_Q.editPage();//???????????????
                title = this.getMessage("form.process.managment.q.c.edit.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                
                formProcessManagmentFormVo = formProcessQuestionService.getFormProcessManagmentFormById(id);
                break;
                
            case 5:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_INC.editPage();//?????????
                title = this.getMessage("form.process.managment.inc.edit.page.title");
                
                formProcessManagmentFormVo = formProcessIncService.getFormProcessManagmentFormById(id);
                break;
                
            case 6:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_INC.editPage();//???????????????
                title = this.getMessage("form.process.managment.inc.c.edit.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                
                formProcessManagmentFormVo = formProcessIncService.getFormProcessManagmentFormById(id);
                break;
                
            case 7:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_CHG.editPage();//?????????
                title = this.getMessage("form.process.managment.chg.edit.page.title");
                
                formProcessManagmentFormVo = formProcessChgService.getFormProcessManagmentFormById(id);
                break;
                
            case 8:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_JOB.editPage();//?????????
                title = this.getMessage("form.process.managment.job.edit.page.title");
                
                formProcessManagmentFormVo = formProcessJobService.getFormProcessManagmentFormById(id);
                break;
                
            case 9:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_JOB.editPage();//???????????????
                title = this.getMessage("form.process.managment.job.c.edit.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                
                formProcessManagmentFormVo = formProcessJobService.getFormProcessManagmentFormById(id);
                List<FormProcessManagmentJobApplyVO> jobApplyVos = (List<FormProcessManagmentJobApplyVO>) formProcessManagmentFormVo
                        .getApplyProcessList();
                String isParallel = StringConstant.SHORT_NO;
                String parallels = StringUtils.EMPTY;
                for (FormProcessManagmentJobApplyVO jobVo : jobApplyVos) {
                    if (StringConstant.SHORT_YES.equals(jobVo.getIsParallel())) {
                        isParallel = StringConstant.SHORT_YES;
                        parallels = jobVo.getParallels();
                        break;
                    }
                }
                modelAndView.addObject("isParallel", isParallel);// ?????????????????????
                modelAndView.addObject("parallels", parallels);// ??????????????????
                break;
            
            case 10:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_BA.editPage();//?????????????????????????????????
                title = this.getMessage("form.process.managment.ba.edit.page.title");
                
                formProcessManagmentFormVo = formProcessBaService.getFormProcessManagmentFormById(id);
                break;

            default:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT.initPage();
                break;
        }
        
        if (formProcessManagmentFormVo != null) {
            String division = formProcessManagmentFormVo.getDivision();
            if (StringUtils.isNotBlank(division) && division.contains(StringConstant.DASH)) {
                String departmentId = division.split(StringConstant.DASH)[0];
                String divisionId = division.split(StringConstant.DASH)[1];

                baseVo.setDepartmentId(departmentId);
                baseVo.setDivision(divisionId);//????????????????????????division??????,?????????????????????????????????????????????

                List<HtmlVO> applySysGroupLs = formProcessBaseService.getSysGroupIdByDeptIdAndDivision(baseVo);
                List<HtmlVO> reviewSysGroupLs = formProcessBaseService.getSysGroupIdByDeptIdAndDivisionWithManagment(baseVo);
                List<HtmlVO> workProjectLs = htmlService.getDropdownList("WORK_LEVEL");
                modelAndView.addObject("applySysGroupLs", applySysGroupLs);
                modelAndView.addObject("reviewSysGroupLs", reviewSysGroupLs);
                modelAndView.addObject("workProjectLs", workProjectLs);
            }
            modelAndView.addObject("formProcessManagmentFormVo", formProcessManagmentFormVo);
            modelAndView.addObject("updateDateText", DateUtils.toString(formProcessManagmentFormVo.getUpdatedAt(), DateUtils.pattern12));
            modelAndView.addObject("formProcessManagmentFormVoJsonStr", BeanUtil.toJson(formProcessManagmentFormVo));
            modelAndView.addObject("formTypeLs", formTypeLs);
            modelAndView.addObject("divisionLs", divisionLs);
            modelAndView.addObject("title", title);
            modelAndView.addObject("isEnable", baseVo.getIsEnable());

            modelAndView.setViewName(viewName);
        }

        return modelAndView;
    }
    
    /**
     * ???????????? ????????????????????????
     * @param vo
     * @return ResponseEntity
     */
    @PostMapping(value = "/verifyProcessStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FormProcessManagmentBaseVO> verifyProcessStatus(HttpSession session,@RequestBody FormProcessManagmentBaseVO vo) {
        return ResponseEntity.ok(formProcessBaseService.getFormProcessById(vo.getId()));
    }
    
    /**
     * ???????????? ????????????????????????
     * @param vo
     * @return ResponseEntity
     */
    @PostMapping(value = "/getFormProcessDetailByConditon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<FormProcessManagmentBaseVO>> getFormProcessDetailByConditon(@RequestBody FormProcessManagmentResultVO vo) {
        return ResponseEntity.ok(formProcessBaseService.getFormProcessManagmentByCondition(vo));
    }
    
    /**
     * ????????????ID????????????,??????????????????
     * @param vo
     * @return ResponseEntity
     */
    @PostMapping(value = "/getGroupInfoByDepartmentIdAndDivision", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<HtmlVO>> getGroupInfoByDepartmentIdAndDivision(@RequestBody FormProcessManagmentBaseVO vo) {
        return ResponseEntity.ok(formProcessBaseService.getSysGroupIdByDeptIdAndDivision(vo));
    }
    
    /**
     * ????????????ID????????????,??????????????????(???????????????)
     * @param vo
     * @return ResponseEntity
     */
    @PostMapping(value = "/getGroupInfoByDepartmentIdAndDivisionWithManagment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<HtmlVO>> getGroupInfoByDepartmentIdAndDivisionWithManagment(@RequestBody FormProcessManagmentBaseVO vo) {
        return ResponseEntity.ok(formProcessBaseService.getSysGroupIdByDeptIdAndDivisionWithManagment(vo));
    }
    
    
    /**
     * ????????????,????????????????????????
     * @param params
     * @return ResponseEntity
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/updateFormProcessStatusById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String,Object>> updateFormProcessStatusById(@RequestBody Map<String,Object> params) {
        List<Map<String,Object>> dataLs = (List<Map<String, Object>>) MapUtils.getObject(params, "changeStatusList");
        List<FormProcessManagmentBaseVO> voLs = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        
        for(Map<String,Object> target : dataLs) {
            FormProcessManagmentBaseVO vo = new FormProcessManagmentBaseVO();
            
            Long id = MapUtils.getLongValue(target, "id");
            int formType = MapUtils.getIntValue(target, "formType");
            String departmentId = MapUtils.getString(target, "departmentId");
            String division = MapUtils.getString(target, "division");
            String isEnable = MapUtils.getString(target, "isEnable");
            String processName = MapUtils.getString(target, "processName");
            
            vo.setId(id);
            vo.setFormType(formType);
            vo.setDepartmentId(departmentId);
            vo.setDivision(division);
            vo.setIsEnable(isEnable);
            vo.setProcessName(processName);
            
            voLs.add(vo);
            
            //?????????????????????????????????,???????????????,????????????,?????????????????????ID,?????????????????????
            if(StringConstant.SHORT_YES.equalsIgnoreCase(isEnable)) {
                FormProcessManagmentBaseVO verifyVo = formProcessBaseService.getFormProcessByCondition(vo);
                if(!Objects.isNull(verifyVo) && !verifyVo.getId().equals(id)) {
                    String duplicateStatusMessage = this.getMessage("form.process.managment.update.status.duplicate.warning");
                    duplicateStatusMessage = MessageFormat.format(duplicateStatusMessage, vo.getProcessName());
                    
                    result.put("result", duplicateStatusMessage);
                    
                    return ResponseEntity.ok(result);
                }
            }
        }
        
        if(CollectionUtils.isNotEmpty(voLs)) {
            formProcessBaseService.updateFormProcessStatusById(voLs);
            result.put("result", this.getMessage("global.update.result"));
        } else {
            logger.info("????????????????????????!");
            result.put("result", this.getMessage("global.no.data.update.result"));
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * ???????????????FormType,????????????????????????
     * @param vo
     * @return ModelAndView
     */
    @RequestMapping(value = "/dispatcherPageByFormType/{formType}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView dispatcherPageByFormType(FormProcessManagmentBaseVO vo) {
        int formType = vo.getFormType();
        String viewName = "";
        String title = "";
        
        ModelAndView modelAndView = new ModelAndView();
        
        switch (formType) {
            case 1:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_SR.addPage();//?????????
                title = this.getMessage("form.process.managment.sr.add.page.title");
                break;
                
            case 2:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_SR.addPage();//???????????????
                title = this.getMessage("form.process.managment.sr.c.add.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                break;
                
            case 3:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_Q.addPage();//?????????
                title = this.getMessage("form.process.managment.q.add.page.title");
                break;
                
            case 4:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_Q.addPage();//???????????????
                title = this.getMessage("form.process.managment.q.c.add.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                break;
                
            case 5:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_INC.addPage();//?????????
                title = this.getMessage("form.process.managment.inc.add.page.title");
                break;
                
            case 6:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_INC.addPage();//???????????????
                title = this.getMessage("form.process.managment.inc.c.add.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                break;
                
            case 7:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_CHG.addPage();//?????????
                title = this.getMessage("form.process.managment.chg.add.page.title");
                break;
                
            case 8:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_JOB.addPage();//?????????
                title = this.getMessage("form.process.managment.job.add.page.title");
                break;
                
            case 9:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_JOB.addPage();//???????????????
                title = this.getMessage("form.process.managment.job.c.add.page.title");
                modelAndView.addObject("isCountersign", StringConstant.SHORT_YES);//??????????????????
                break;
                
            case 10:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT_BA.addPage();//?????????????????????????????????
                title = this.getMessage("form.process.managment.ba.add.page.title");
                break;

            default:
                viewName = DispatcherEnum.FORM_PROCESS_MANAGMENT.initPage();
                break;
        }
        
        List<HtmlVO> formTypeLs =  formProcessBaseService.getFormTypeSelector();
        List<HtmlVO> divisionLs =  formProcessBaseService.getSysGroupSelector();
        
        modelAndView.setViewName(viewName);
        modelAndView.addObject("formTypeLs", formTypeLs);
        modelAndView.addObject("divisionLs", divisionLs);
        modelAndView.addObject("formType", formType);
        modelAndView.addObject("title", title);

        return modelAndView;
    }

    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }
    
}
