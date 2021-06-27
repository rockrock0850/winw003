package com.ebizprise.winw.project.controller.form.operation.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.service.ICommonJobFormService;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.CommonCheckPersonVO;
import com.ebizprise.winw.project.vo.CommonJobFormVO;
import com.ebizprise.winw.project.vo.HtmlVO;

/**
 * 工作單的共用頁簽控制類別
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/commonJobForm")
public class CommonJobFormController extends BaseController {

    @Autowired
    private IHtmlService htmlService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private ICommonJobFormService commonJobFormService;
    
    /**
     * 根據表單編號工作單的工作要項清單
     * 
     * @param formId
     * @return
     * @author adam.yeh
     */
    @GetMapping(path = "/getWorkingItemsFromJob/{formId}")
    public List<HtmlVO> getWorkingItemsFromJob (@PathVariable String formId) {
        return htmlService.getWorkingItems(formId);
    }
    
    /**
     * 作業關卡人員儲存
     * 
     * @param vo
     * @author jacky.fu
     */
    @PostMapping(path = "/saveCheckPerson")
    public void saveCheckPerson(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCheckPerson(vo);
    }

    /**
     * 檢核作業關卡人員
     * 
     * @param vo
     * @author adam.yeh
     */
    @PostMapping(path = "/validatePerson")
    public Map<String, Object> validatePerson (@RequestBody CommonJobFormVO vo) {
        DataVerifyUtil verifyUtil = new DataVerifyUtil();
        List<CommonCheckPersonVO> persons = vo.getCheckPersonList();
        boolean isPersonTab = persons != null;
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isPersonTab) {
            commonJobFormService.validatePersonList(vo, persons, verifyUtil);
        }
        
        if (!isAdmin) {
            commonJobFormService.validatePersonIsExist(vo.getFormId(), verifyUtil);
        }
        
        return verifyUtil.build();
    }

    /**
     * 作業關卡
     * 
     * @param vo
     * @return
     * @author jacky.fu
     */
    @PostMapping(path = "/getCheckPerson")
    public Map<String,Object> getCheckPerson (@RequestBody CommonJobFormVO vo) {
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("checkPersionList", commonJobFormService.getJobWorkItems(vo, false));
        
        return result;
    }
    
    /**
     * 表單送出前，檢核作業關卡員工是否存在
     * 
     * @param vo
     */
    @PostMapping(path = "/isJobCheckPersonExist")
    public Map<String, Object> isJobCheckPersonExist(@RequestBody CommonJobFormVO vo) {
        return commonJobFormService.checkJobPeasonExistByFormId(vo.getFormId());
    }

    /**
     * 取得 設一/設二/設三/設四 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getApDetail/{formId}/{division}")
    public CommonJobFormVO getApDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 設一/設二/設三/設四 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/saveApDetail")
    public CommonJobFormVO saveApDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 資管科(ONLINE/BATCH/OPEN) 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getDcDetail/{formId}/{division}")
    public CommonJobFormVO getDcDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 資管科(ONLINE/BATCH/OPEN) 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/saveDcDetail")
    public CommonJobFormVO saveDcDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 連管科 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getPtDetail/{formId}/{division}")
    public CommonJobFormVO getPtDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 連管科 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/savePtDetail")
    public CommonJobFormVO savePtDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 資安規劃/資安管理 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getPlanmgmtDetail/{formId}/{division}")
    public CommonJobFormVO getPlanmgmtDetail(@PathVariable String formId,
                                                   @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 資安規劃/資安管理 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/savePlanmgmtDetail")
    public CommonJobFormVO savePlanmgmtDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 電子商務科 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getEaDetail/{formId}/{division}")
    public CommonJobFormVO getEaDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 電子商務科 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/saveEaDetail")
    public CommonJobFormVO saveEaDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 辦公室自動化 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author emily.lai
     */
    @GetMapping(path = "/getOaDetail/{formId}/{division}")
    public CommonJobFormVO getOaDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 辦公室自動化 會辦單明細
     * 
     * @param vo
     * @author emily.lai
     */
    @PostMapping(path = "/saveOaDetail")
    public CommonJobFormVO saveOaDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 系統 會辦單明細
     * 
     * @param formId
     * @param division
     * @return CommonCountersignedVo
     * @author adam.yeh
     */
    @GetMapping(path = "/getSpDetail/{formId}/{division}")
    public CommonJobFormVO getSpDetail(@PathVariable String formId, @PathVariable String division) {
        return commonJobFormService.getCountersignedDetail(formId, division);
    }
    
    /**
     * 儲存 系統 會辦單明細
     * 
     * @param vo
     * @author adam.yeh
     */
    @PostMapping(path = "/saveSpDetail")
    public CommonJobFormVO saveSpDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveCountersignedDetail(vo);
        return vo;
    }
    
    /**
     * 取得 批次 頁簽 明細
     * 
     * @author adam.yeh
     */
    @GetMapping(path = "/getBatchDetail/{formId}/{division}")
    public CommonJobFormVO getBatchDetail(CommonJobFormVO vo) {
        return commonJobFormService.getJobBatchDetail(vo);
    }
    
    /**
     * 儲存 批次 頁簽明細
     * 
     * @param vo
     * @author adam.yeh
     */
    @PostMapping(path = "/saveBatchDetail")
    public CommonJobFormVO saveBatchDetail(@RequestBody CommonJobFormVO vo) {
        commonJobFormService.saveJobBatchDetail(vo);
        return vo;
    }
    
    /**
     * 取得 DB變更 頁簽 明細
     * @author adam.yeh
     */
    @GetMapping(path = "/getDbAlterDetail/{formId}/{type}")
    public CommonJobFormVO getDbAlterDetail (CommonJobFormVO vo) {
        return commonJobFormService.getJobWorkingDetail(vo);
    }
    
    /**
     * 儲存 DB變更 頁簽明細
     * @param vo
     * @author adam.yeh
     */
    @PostMapping(path = "/saveDbAlterDetail")
    public CommonJobFormVO saveDbAlterDetail (@RequestBody CommonJobFormVO vo) {
        commonJobFormService.mergeJobWorkingDetail(vo);
        return vo;
    }
    
    /**
     * 刪除 DB變更 頁簽明細
     * @author adam.yeh
     */
    @PostMapping(path = "/deleteSegments")
    public void deleteSegments (@RequestBody List<Long> ids) {
        commonJobFormService.mergeJobWorkingDetail(ids);
    }
    
    /**
     * 檢核會辦單明細內容
     * 
     * @param vo
     * @return
     * @author emily.lai
     */
    @PostMapping(path = "/validateCountersigned")
    public Map<String, Object> validateCountersigned(@RequestBody CommonJobFormVO vo) {
        DataVerifyUtil verifyUtil = new DataVerifyUtil();
        unNecessary(verifyUtil, vo);
        
        return verifyUtil.build();
    }
    
    /**
     * 透過登入者科別/透過登入者部門, 取得可查看的工作單會辦頁簽
     * @param vo
     * @return CommonJobFormVO
     */
    @PostMapping(path = "/getJobTabs")
    public CommonJobFormVO getJobTabs(@RequestBody CommonJobFormVO vo) {
        List<String> dataLs = new ArrayList<String>();
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin && vo.getIsApTabs()) {
            return vo;
        }
        
        if (vo.getIsApTabs()) {
            vo = commonJobFormService.isSecurityDeptTabs(vo.getDepartmentId());
        } else {
        	if (StringUtils.contains(vo.getDivisionSolving(), FormJobEnum.DC.name())) {
        		String formId = StringUtils.isNotBlank(vo.getFormId()) ? vo.getFormId() : vo.getSourceId();
        		dataLs = commonJobFormService.getInternalProcessJobDivisionTabList(formId);
        	} else {
        		dataLs = commonJobFormService.getJobDivisionTabList(vo.getDivisionSolving());
        	}
            vo.setJobTabName(dataLs);
        }
        
        return vo;
    }
    
    // 非必要輸入欄位檢核
    private void unNecessary (DataVerifyUtil verifyUtil, CommonJobFormVO vo) {
        if (StringUtils.isNotBlank(vo.getUserId())) {
            verifyUtil.length(vo.getUserId(), 10, getMessage("form.countersigned.form.userId"));
        }
        if (StringUtils.isNotBlank(vo.getOnlyCode())) {
            verifyUtil.length(vo.getOnlyCode(), 20, getMessage("form.countersigned.form.onlyCode"));
        }
        if (StringUtils.isNotBlank(vo.getLinkCode())) {
            verifyUtil.length(vo.getLinkCode(), 20, getMessage("form.countersigned.form.linkCode"));
        }
        if (StringUtils.isNotBlank(vo.getRollbackDesc())) {
            verifyUtil.length(vo.getRollbackDesc(), 50,
                getMessage("form.countersigned.form.rollbackDesc.validate"));
        }
        if (StringUtils.isNotBlank(vo.getBookNumber())) {
            verifyUtil.length(vo.getBookNumber(), 6, getMessage("form.countersigned.form.bookNumber"));
            verifyUtil.number(vo.getBookNumber(), getMessage("form.countersigned.form.bookNumber"));
        }
        if (StringUtils.isNotBlank(vo.getOnlyNumber())) {
            verifyUtil.length(vo.getOnlyNumber(), 6, getMessage("form.countersigned.form.onlyNumber"));
            verifyUtil.number(vo.getOnlyNumber(), getMessage("form.countersigned.form.onlyNumber"));
        }
        if (StringUtils.isNotBlank(vo.getLinkNumber())) {
            verifyUtil.length(vo.getLinkNumber(), 6, getMessage("form.countersigned.form.linkNumber"));
            verifyUtil.number(vo.getLinkNumber(), getMessage("form.countersigned.form.linkNumber"));
        }
        if (StringUtils.isNotBlank(vo.getProgramNumber())) {
            verifyUtil.length(vo.getProgramNumber(), 6, "程式支數");
            verifyUtil.number(vo.getProgramNumber(), "程式支數");
        }
        if (StringUtils.isNotBlank(vo.getLinkOnlyNumber())) {
            verifyUtil.length(vo.getLinkOnlyNumber(), 6,
                getMessage("form.countersigned.form.linkOnlyNumber"));
            verifyUtil.number(vo.getLinkOnlyNumber(),
                getMessage("form.countersigned.form.linkOnlyNumber"));
        }
    }

    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }
}
