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
 * 處理需要依照登入者資訊而變動的前端元件內容值
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
     * 找平行會辦的工作群組清單
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
     * 選擇人員
     * 
     * @author jacky.fu
     */
    @SuppressWarnings("unchecked")
    @PostMapping(path = "/getGroupUserList")
    public Map<String, Object> getGroupUserList (@RequestBody BaseFormProcessManagmentDetailVO vo) {
        Map<String, Object> result = new HashMap<String,Object>();
        
        // 先撈取該關卡對應的設定人員
        String workProject = "";
        FormProcessManagmentResultVO processResultVO = formProcessJobService.getByFormId(vo.getFormId());
        
        if(CollectionUtils.isNotEmpty(processResultVO.getApplyProcessList())) {
            List<FormProcessManagmentJobApplyVO> applyLs = (List<FormProcessManagmentJobApplyVO>) processResultVO.getApplyProcessList();
            
            for(FormProcessManagmentJobApplyVO target : applyLs) {
                // 找出當前的關卡順序
                if(vo.getProcessOrder() == target.getProcessOrder()) {
                    // 取得SYS_OPTION_ROLE資訊
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
     * 取得需求等級
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
     * 取得流程關卡清單
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
        } else if (!isApJobForm(vo) && !isSpJobForm(vo)) {// 工作單不需複寫送簽人員的描述
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
     * 取得科別下拉選單</br>
     * true : </br>
     * value = DepartmentId+Division</br>
     * wording = DepartmentName+Division</br></br>
     * false : </br>
     * value = DepartmentName+Division</br>
     * wording = DepartmentId+Division
     * 
     * @param orientation 正/反向
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
     * 取得系統科組別下拉選單
     * 
     * @param orientation 正/反向
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
     * 根據登入者部門取得科別下拉選單</br>
     * @param orientation 正/反向
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
     * 取得群組下拉選單
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
     * 取得人員下拉選單
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
     * 取得系統名稱清單
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
     * 查詢組態元件清單
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
     * 取得系統參數清單 (分部門)
     * @return
     */
    @GetMapping(path="/getDropdownList/{optionId}")
    public List<HtmlVO> getDropdownList (HtmlVO htmlVO){
        return htmlService.getDropdownList(htmlVO, false);
    }
    
    /**
     * 取得系統參數清單 (分部門)
     * @return
     */
    @GetMapping(path="/getDropdownList/{optionId}/{formClass}")
    public List<HtmlVO> getDropdownListWithFormClass (HtmlVO htmlVO){
    	return htmlService.getDropdownList(htmlVO, false);
    }
    
    /**
     * 取得系統參數清單 (不分部門)
     * @return
     */
    @GetMapping(path="/getDropdownList/release/{optionId}")
    public List<HtmlVO> getDropdownList2 (HtmlVO htmlVO){
        return htmlService.getDropdownList(htmlVO, true);
    }

    /**
     * 取得系統參數清單-子類
     * 
     * @return
     */
    @GetMapping(path="/getSubDropdownList/{value}")
    public List<HtmlVO> getSubDropdownList (HtmlVO htmlVO){
        return htmlService.getSubDropdownList(htmlVO);
    }

    /**
     * 取得表單狀態下拉選單
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
     * 取得工作要項清單
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
     * 取得非擬案、非結案、非作廢之事件單
     * 
     * @param vo
     */
    @PostMapping(path = "/getIncFormList")
    public List<BaseFormVO> getIncFormList(@RequestBody FormSearchVO vo) {
        return formSearchService.getIncForms(vo);
    }
    
    /**
     * AP工作單+SP工作單；表單狀態=「審核中」「已結案」
     * @param vo
     * @return
     * @author jacky.fu
     */
    @PostMapping(path = "/getJobFormList")
    public List<ApJobFormVO> getJobFormList (@RequestBody ApJobFormVO vo) {
        return apJobFormService.getJobForms(vo);
    }
    
    /**
     * 取得表單狀態下拉選單
     * 
     * @return List
     */
    @GetMapping(path = "/getCountersignedDivisionList/{formId}/{formClass}")
    public List<HtmlVO> getCountersignedDivisionList(@PathVariable String formId,@PathVariable String formClass) {
        List<String> values = formHelper.getFormService(FormEnum.valueOf(formClass)).getFormCountsignList(formId);
        return htmlService.getDropdownListByValue(values);
    }
    
    /**
     * 透過傳入物件,取得對應的SYS_OPTION_ROLE資訊
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
    
    // 是否選擇作業關卡人員
    private boolean isPickCheckPerson (BaseFormProcessManagmentDetailVO vo) {
        return (vo.getProcessOrder() != 0 && StringUtils.isNoneBlank(vo.getFormId()));
    }

    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }
    
    /**
     * 取得內會流程清單
     * 
     * @return List
     */
    @GetMapping(path = "/getInternalProcessList/{formId}")
    public List<HtmlVO> getInternalProcessList(@PathVariable String formId) {
        return htmlService.getInternalProcessList(formId);
    }
    
    /**
     * 取得內會各流程狀態
     * 
     * @return List
     */
    @GetMapping(path = "/getInternalProcessStatus/{formId}")
    public List<HtmlVO> getInternalProcessStatus(@PathVariable String formId) {
    	return htmlService.getInternalProcessStatus(formId);
    }
    
    /**
     * 取得未完成的內會流程清單
     * 
     * @return List
     */
    @GetMapping(path = "/getUnfinishedInternalProcess/{formId}")
    public List<String> getUnfinishedInternalProcess(@PathVariable String formId) {
    	return htmlService.getUnfinishedInternalProcess(formId);
    }
    
    /**
     * 取得串會副科長名單
     * 
     * @return List
     */
    @GetMapping(path = "/getSplitProcessViceList")
    public List<HtmlVO> getSplitProcessViceList() {
    	return htmlService.getSplitProcessViceList();
    }
}