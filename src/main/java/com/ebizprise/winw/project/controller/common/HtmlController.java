package com.ebizprise.winw.project.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.enums.SysCommonEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.service.IFormSearchService;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.service.ISysGroupService;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.service.impl.ApJobFormServiceImpl;
import com.ebizprise.winw.project.service.impl.FormProcessJobServiceImpl;
import com.ebizprise.winw.project.service.startup.FormHelper;
import com.ebizprise.winw.project.vo.ApJobFormVO;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobApplyVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;
import com.ebizprise.winw.project.vo.FormSearchVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.LogonRecordVO;
import com.ebizprise.winw.project.vo.SysOptionRoleVO;


/**
 * ??????????????????????????????????????????????????????????????????
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/html")
public class HtmlController extends BaseController {
    
    @Autowired
    private ISysGroupService sysGroupService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IFormProcessBaseService processService;
    @Autowired
    private IHtmlService htmlService;
    @Autowired
    private IFormSearchService formSearchService;
    @Autowired
    private FormProcessJobServiceImpl formProcessJobService;
    @Autowired
    private FormHelper formHelper;
    @Autowired
    private ApJobFormServiceImpl apJobFormService;

    /**
     * ????????????????????????????????????
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/getFormCParallels")
    public List<HtmlVO> getFormCParallels (@RequestBody HtmlVO vo) {
        List<HtmlVO> parallels = null;
        boolean isAll = vo.getIsAll();
        String clazz = vo.getFormClass();
        
        if (isAll || 
            FormEnum.Q_C.name().equals(clazz) ||
            FormEnum.SR_C.name().equals(clazz) ||
            FormEnum.JOB_SP_C.name().equals(clazz)) {
            parallels = htmlService.getFormCParallels(vo);
        }
        
        return parallels;
    }
    
    /**
     * ????????????
     * 
     * @author jacky.fu
     */
    @SuppressWarnings("unchecked")
    @PostMapping(path = "/getGroupUserList")
    public Map<String, Object> getGroupUserList (@RequestBody BaseFormProcessManagmentDetailVO vo) {
        Map<String, Object> result = new HashMap<String,Object>();
        
        // ???????????????????????????????????????
        String workProject = "";
        FormProcessManagmentResultVO processResultVO = formProcessJobService.getByFormId(vo.getFormId());
        
        if(CollectionUtils.isNotEmpty(processResultVO.getApplyProcessList())) {
            List<FormProcessManagmentJobApplyVO> applyLs = (List<FormProcessManagmentJobApplyVO>) processResultVO.getApplyProcessList();
            
            for(FormProcessManagmentJobApplyVO target : applyLs) {
                // ???????????????????????????
                if(vo.getProcessOrder() == target.getProcessOrder()) {
                    // ??????SYS_OPTION_ROLE??????
                    SysOptionRoleVO optionRoleVO = htmlService.getSysOptionRole("WORK_LEVEL", target.getWorkProject());
                    if(StringUtils.isNotBlank(optionRoleVO.getCondition())) {
                        workProject = optionRoleVO.getCondition();
                    }
                    break;
                }
            }
        }
        
        if (isPickCheckPerson(vo)) {
            workProject = isApJobForm(vo) ? null : workProject;
            result.put("userList", sysUserService.getEmployeeOfProcess(vo, workProject));  
        } else {
            HtmlVO htmlVO = new HtmlVO();
            htmlVO.setIsAll(true);
            htmlVO.setGroupId(vo.getGroupId());
            htmlVO.setSubGroup(vo.getSubGroup());
            htmlVO.setGroupIdList(vo.getGroupIdList());
            result.put("userList", sysUserService.findUsers(htmlVO)); 
        }
        
        result.put("groupName", vo.getGroupName());

        return result;
    }

    /**
     * ??????????????????
     * 
     * @param formClass
     * @param effect
     * @param urgent
     * @return
     */
    @GetMapping(path = "/getDemandLevel/{formClass}/{effect}/{urgent}")
    public HtmlVO getDemandLevel (
            @PathVariable String formClass,
            @PathVariable Integer effect,
            @PathVariable Integer urgent) {
        Integer level = null;
        
        switch (FormEnum.valueOf(formClass)) {
            case Q:
            case SR:
                level = SysCommonEnum.valueOfOriDemands(effect, urgent);
                break;
                
            case INC:
                level = SysCommonEnum.valueOfIncDemands(effect, urgent);
                break;

            default:
                break;
        }
        
        HtmlVO vo = new HtmlVO();
        vo.setWording(String.valueOf(level));
        
        return vo;
    }
    
    /**
     * ????????????????????????
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/getSigningList")
    public List<HtmlVO> getSigningList (@RequestBody BaseFormProcessManagmentDetailVO vo) {
        List<HtmlVO> selectors = new ArrayList<>();
        String titleCode = getCurrentUserTitleCode();
        List<BaseFormProcessManagmentDetailVO> signingList  = new ArrayList<>();
        
        switch (FormEnum.valueOf(vo.getVerifyType())) {
            case APPLY:
                signingList = processService.getApplySigningList(vo);
                processService.getProcessWording(signingList,FormEnum.APPLY,vo);
                break;
                
            case REVIEW:
                signingList = processService.getReviewSigningList(vo);
                processService.getProcessWording(signingList,FormEnum.REVIEW,vo);
                break;

            default:
                break;
        }
        
        if (vo.isLastLevel()) {
            signingList = processService.getLastLevelSigningList(titleCode, vo);
        } else if (!isApJobForm(vo) && !isSpJobForm(vo)) {// ??????????????????????????????????????????
            signingList = processService.overwriteSigningListGroupName(signingList, titleCode, vo);
        }
        
        if (CollectionUtils.isNotEmpty(signingList)) {
            for (BaseFormProcessManagmentDetailVO detailVO : signingList) {
                HtmlVO html = new HtmlVO();
                BeanUtil.copyProperties(detailVO, html);
                selectors.add(html);
            }
        }
        
        return selectors;
    }
    
    /**
     * ????????????????????????</br>
     * true : </br>
     * value = DepartmentId+Division</br>
     * wording = DepartmentName+Division</br></br>
     * false : </br>
     * value = DepartmentName+Division</br>
     * wording = DepartmentId+Division
     * 
     * @param orientation ???/??????
     * @return
     */
    @GetMapping(path = "/getDivisionSelectors/{orientation}")
    public List<HtmlVO> getDivisionSelectors (@PathVariable boolean orientation) {
        List<HtmlVO> voList;
        
        if (orientation) {
            voList = processService.getSysGroupSelector();
        } else {
            voList = sysGroupService.getSysGroupSelectorReverse();
        }
        
        return voList;
    }
    
    /**
     * ?????????????????????????????????
     * 
     * @param orientation ???/??????
     * @return
     */
    @GetMapping(path = "/getSpGroupSelectors/{orientation}")
    public List<HtmlVO> getSpGroupSelectors (@PathVariable boolean orientation) {
        List<HtmlVO> voList;
        
        if (orientation) {
            voList = processService.getSpGroupSelector();
        } else {
            voList = sysGroupService.getSpGroupSelectorReverse();
        }
        
        return voList;
    }

    
    /**
     * ?????????????????????????????????????????????</br>
     * @param orientation ???/??????
     * @return
     */
    @GetMapping(path = "/getDeptDevisionSelectors")
    public List<HtmlVO> getDeptDevisionSelectors () {
        String departmentId;
        String department = getUserInfo().getDepartmentId();
        List<HtmlVO> voList = processService.getSysGroupSelector();
        List<HtmlVO> newList = new ArrayList<>();
        
        for (HtmlVO html : voList) {
            departmentId = html.getValue().split("-")[0];
            
            if (department.equals(departmentId)) {
                newList.add(html);
            }
        }
        
        return newList;
    }
    
    @GetMapping(path = "/getDivisionSelectorsOnlyUseInc/{isExclude}")
    public List<HtmlVO> getDivisionSelectorsOnlyUseInc (@PathVariable String isExclude) {
        List<HtmlVO> voList = new ArrayList<>();
        voList = sysGroupService.getSysGroupSelectorOnlyUseIncListReport(isExclude);
        return voList;
    }
    
    /**
     * ????????????????????????
     * 
     * @return
     */
    @GetMapping(path = "/getSysGroupSelectors")
    public List<HtmlVO> getSysGroupSelectors () {
        List<HtmlVO> selectors = new ArrayList<>();
        List<GroupFunctionVO> dataList = sysGroupService.getAllSysGroupVOs();
        
        for (GroupFunctionVO vo : dataList) {
            HtmlVO html = new HtmlVO();
            html.setValue(vo.getGroupId());
            html.setWording(vo.getGroupName());
            selectors.add(html);
        }
        
        return selectors;
    }
    
    /**
     * ????????????????????????
     * 
     * @return
     */
    @PostMapping(path = "/getUserSelectors")
    public List<HtmlVO> getUserSelectors (@RequestBody HtmlVO htmlVO) {
        List<HtmlVO> selectors = new ArrayList<>();
        List<LogonRecordVO> voList;
        if (StringUtils.equals(StringConstant.SHORT_YES, htmlVO.getCountersigneds())) {
        	voList = sysUserService.getInternalProcessEmployee();
        } else {
        	voList = sysUserService.findUsers(htmlVO);
        }
        
        for (LogonRecordVO vo : voList) {
            HtmlVO html = new HtmlVO();
            html.setValue(vo.getUserId());
            html.setWording(vo.getUserName());
            selectors.add(html);
        }
        
        return selectors;
    }
    
    /**
     * ????????????????????????
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/getSystemInfoList")
    public List<HtmlVO> getSystemInfoList (@RequestBody HtmlVO vo) {
        return htmlService.getSystemList(vo);
    }
    
    /**
     * ????????????????????????
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/getCComponentList")
    public List<HtmlVO> getCComponentList (@RequestBody HtmlVO vo) {
        return htmlService.getCComponentList(vo);
    }
    
    /**
     * ???????????????????????? (?????????)
     * @return
     */
    @GetMapping(path="/getDropdownList/{optionId}")
    public List<HtmlVO> getDropdownList (HtmlVO htmlVO){
        return htmlService.getDropdownList(htmlVO, false);
    }
    
    /**
     * ???????????????????????? (?????????)
     * @return
     */
    @GetMapping(path="/getDropdownList/{optionId}/{formClass}")
    public List<HtmlVO> getDropdownListWithFormClass (HtmlVO htmlVO){
    	return htmlService.getDropdownList(htmlVO, false);
    }
    
    /**
     * ???????????????????????? (????????????)
     * @return
     */
    @GetMapping(path="/getDropdownList/release/{optionId}")
    public List<HtmlVO> getDropdownList2 (HtmlVO htmlVO){
        return htmlService.getDropdownList(htmlVO, true);
    }

    /**
     * ????????????????????????-??????
     * 
     * @return
     */
    @GetMapping(path="/getSubDropdownList/{value}")
    public List<HtmlVO> getSubDropdownList (HtmlVO htmlVO){
        return htmlService.getSubDropdownList(htmlVO);
    }

    /**
     * ??????????????????????????????
     * 
     * @return
     */
    @GetMapping(path = "/getFormStatusSelectors")
    public List<HtmlVO> getFormStatusSelectors(HtmlVO htmlVO) {
        List<FormEnum> formSelectors = new ArrayList<>();
        formSelectors.add(FormEnum.CLOSED);
        formSelectors.add(FormEnum.WATCHING);
        formSelectors.add(FormEnum.PROPOSING);
        formSelectors.add(FormEnum.APPROVING);
        formSelectors.add(FormEnum.DEPRECATED);

        List<HtmlVO> selectors = new ArrayList<>();
        for (FormEnum form : formSelectors) {
            HtmlVO formInfo = new HtmlVO();
            formInfo.setValue(form.name());
            formInfo.setWording(form.status());
            selectors.add(formInfo);
        }

        return selectors;
    }
    
    /**
     * ????????????????????????
     * 
     * @param vo
     * @return
     * @author emily.lai
     */
    @PostMapping(path = "/getWorkingItemList")
    public List<HtmlVO> getWorkingItemList(@RequestBody HtmlVO vo) {
        return htmlService.getWorkingItemList(vo);
    }

    /**
     * ???????????????????????????????????????????????????
     * 
     * @param vo
     */
    @PostMapping(path = "/getIncFormList")
    public List<BaseFormVO> getIncFormList(@RequestBody FormSearchVO vo) {
        return formSearchService.getIncForms(vo);
    }
    
    /**
     * AP?????????+SP????????????????????????=??????????????????????????????
     * @param vo
     * @return
     * @author jacky.fu
     */
    @PostMapping(path = "/getJobFormList")
    public List<ApJobFormVO> getJobFormList (@RequestBody ApJobFormVO vo) {
        return apJobFormService.getJobForms(vo);
    }
    
    /**
     * ??????????????????????????????
     * 
     * @return List
     */
    @GetMapping(path = "/getCountersignedDivisionList/{formId}/{formClass}")
    public List<HtmlVO> getCountersignedDivisionList(@PathVariable String formId,@PathVariable String formClass) {
        List<String> values = formHelper.getFormService(FormEnum.valueOf(formClass)).getFormCountsignList(formId);
        return htmlService.getDropdownListByValue(values);
    }
    
    /**
     * ??????????????????,???????????????SYS_OPTION_ROLE??????
     * @return SysOptionRoleVO
     */
    @PostMapping(path = "/getSysOptionRole")
    public @ResponseBody SysOptionRoleVO getSysOptionRole(@RequestBody HtmlVO htmlVO) {
        return htmlService.getSysOptionRole(htmlVO);
    }
    
    private boolean isApJobForm (BaseFormProcessManagmentDetailVO vo) {
        String clazz = vo.getFormClass();
        return (FormEnum.JOB_AP.name().equals(clazz) || 
                FormEnum.JOB_AP_C.name().equals(clazz));
    }
    
    private boolean isSpJobForm (BaseFormProcessManagmentDetailVO vo) {
        String clazz = vo.getFormClass();
        return (FormEnum.JOB_SP.name().equals(clazz) || 
                FormEnum.JOB_SP_C.name().equals(clazz));
    }
    
    // ??????????????????????????????
    private boolean isPickCheckPerson (BaseFormProcessManagmentDetailVO vo) {
        return (vo.getProcessOrder() != 0 && StringUtils.isNoneBlank(vo.getFormId()));
    }

    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }
    
    /**
     * ????????????????????????
     * 
     * @return List
     */
    @GetMapping(path = "/getInternalProcessList/{formId}")
    public List<HtmlVO> getInternalProcessList(@PathVariable String formId) {
        return htmlService.getInternalProcessList(formId);
    }
    
    /**
     * ???????????????????????????
     * 
     * @return List
     */
    @GetMapping(path = "/getInternalProcessStatus/{formId}")
    public List<HtmlVO> getInternalProcessStatus(@PathVariable String formId) {
    	return htmlService.getInternalProcessStatus(formId);
    }
    
    /**
     * ????????????????????????????????????
     * 
     * @return List
     */
    @GetMapping(path = "/getUnfinishedInternalProcess/{formId}")
    public List<String> getUnfinishedInternalProcess(@PathVariable String formId) {
    	return htmlService.getUnfinishedInternalProcess(formId);
    }
    
    /**
     * ???????????????????????????
     * 
     * @return List
     */
    @GetMapping(path = "/getSplitProcessViceList")
    public List<HtmlVO> getSplitProcessViceList() {
    	return htmlService.getSplitProcessViceList();
    }
}