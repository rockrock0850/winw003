package com.ebizprise.winw.project.controller.form.operation.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.IHtmlService;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.util.FileVerifyUtil;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.FormImpactAnalysisVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.SysParameterVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單的共用頁簽控制類別
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/commonForm")
public class CommonFormController extends BaseController {

    @Autowired
    private IHtmlService htmlService;
    @Autowired
    private ICommonFormService commonFormService;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    protected ILdapUserRepository ldapUserRepository;

    /**
     * 取得審核歷程記錄
     * @param vo
     */
    @PostMapping(path = "/getVerificationLog")
    public List<BaseFormVO> getVerificationLog (@RequestBody BaseFormVO vo, SysUserVO userInfo) {
        return commonFormService.getFormLogs(vo, userInfo);
    }
    
    /**
     * 根據表單編號取得已選擇的會辦科清單
     * 
     * @param formId
     * @param formClass
     * @return
     * @author adam.yeh
     */
    @GetMapping(path = "/getCListFromFormSelected/{formId}/{formClass}")
    public HtmlVO getCListFromFormSelected (
            @PathVariable String formId,
            @PathVariable String formClass) {
        return htmlService.getCListSelecteds(formId, formClass);
    }
    
    /**
     * 取得日誌清單
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/getLogList")
    public CommonFormVO getLogList(@RequestBody CommonFormVO vo) {
        vo.setName(UserInfoUtil.loginUserId());
        vo.setLogList(commonFormService.getLogs(vo.getFormId()));

        return vo;
    }

    /**
     * 多筆儲存日誌
     * 
     * @param voList
     */
    @PostMapping(path = "/saveLogList")
    public void saveLogList(@RequestBody List<CommonFormVO> voList) {
        commonFormService.saveLogs(voList);
    }

    /**
     * 多筆註銷日誌
     * 
     * @param voList
     */
    @PostMapping(path = "/deleteLogList")
    public void deleteLogList(@RequestBody List<CommonFormVO> voList) {
        commonFormService.deleteLogs(voList);
    }

    /**
     * 取得檔案清單
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/getFileList")
    public List<CommonFormVO> getFileList(@RequestBody CommonFormVO vo) {
        return commonFormService.getFiles(vo);
    }

    /**
     * 刪除多筆檔案
     * 
     * @param voList
     */
    @PostMapping(path = "/deleteFileList")
    public List<CommonFormVO> deleteFileList(@RequestBody List<CommonFormVO> voList) {
        commonFormService.deleteFiles(voList);
        return commonFormService.getFiles(voList.get(0));
    }

    /**
     * 下載單筆檔案
     * 
     * @param vo
     * @throws Exception
     */
    @GetMapping(path = "/download/{id}/{formId}")
    public void download(CommonFormVO vo) throws Exception {
        File file = commonFormService.download(vo);
        makeFileOut(response, file);
    }
    
    /**
     * 取得衝擊分析資訊
     * 
     * @param vo
     * @throws Exception
     */
    @PostMapping(path = "/getFormImpactAnalysis")
    public ResponseEntity<List<FormImpactAnalysisVO>> getFormImpactAnalysis(@RequestBody CommonFormVO vo) throws Exception {
        return ResponseEntity.ok(commonFormService.getFormImpactAnalysis(vo.getFormId()));
    }
    
    /**
     * 保存衝擊分析資訊
     * 
     * @throws Exception
     */
    @PostMapping(path = "/saveFormImpactAnalysisList")
    public void saveFormImpactAnalysisList(@RequestBody FormImpactAnalysisVO vo) throws Exception {
        commonFormService.saveFormImpactAnalysis(vo);
    }
    
    /**
     * 取得衝擊分析的檢核分數
     * 
     * @return String
     */
    @PostMapping(path = "/getValidateFraction")
    public @ResponseBody String getValidateFraction() {
        SysParameterVO fraction = systemConfigService.getFraction();
        return fraction != null ? fraction.getParamValue() : "ERROR-IS-BLANK";
    }
    
    /**
     * 取得檔案上傳限制條件
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/getFileLimitInfo")
    public Map<String, Object> getFileLimitInfo () {
        Map<String, Object> dataMap = new HashMap<>();
        SysParameterVO fileSize = systemConfigService.getFileSize();
        SysParameterVO fileExtension = systemConfigService.getFileExtension();
        dataMap.put("fileSize", fileSize.getParamValue());
        dataMap.put("fileExtension", fileExtension.getParamValue());
        
        return dataMap;
    }

    /**
     * 上傳單筆檔案
     * 
     * @param formId
     * @param description
     * @param file
     * @throws Exception
     */
    @PostMapping(path = "/upload")
    public ResponseEntity<Map<String, Object>> upload (
            @RequestParam("type") String type,
            @RequestParam("formId") String formId,
            @RequestParam("description") String description,
            @RequestParam("alterContent") String alterContent,
            @RequestParam("layoutDataset") String layoutDataset,
            @RequestParam("file") MultipartFile file) throws Exception {
        String returnMsg = "";
        SysParameterVO fileSize = systemConfigService.getFileSize();
        SysParameterVO fileExtension = systemConfigService.getFileExtension();
        Map<String, Object> resMap = new HashMap<>();
        FileVerifyUtil verifyUtil = new FileVerifyUtil();
        verifyUtil
            .exists(file)
            .stringWithErrorMsg(fileExtension.getParamValue(), getMessage("form.common.file.config.fileType"))
            .stringWithErrorMsg(fileSize.getParamValue(), getMessage("form.common.file.config.fileSize"));

        returnMsg = verifyUtil.buildErrorMsg();
        if (StringUtils.isBlank(returnMsg)) {
            // 附檔名轉大寫
            String extensionName = FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase();
            // 允許上傳的附檔名轉大寫
            String settingExtensionStr = fileExtension.getParamValue().toUpperCase();
            // 繼續檢核檔案副檔名以及檔案大小
            verifyUtil.extension(extensionName, settingExtensionStr).fileSize(file, new Long(fileSize.getParamValue()));
            
            returnMsg = verifyUtil.buildErrorMsg();
            if (StringUtils.isBlank(returnMsg)) {
                commonFormService.saveFile(
                        type,
                        formId,
                        description,
                        alterContent,
                        layoutDataset,
                        file);
            }
        }

        resMap.put("returnMsg", returnMsg);

        return ResponseEntity.ok(resMap);
    }
    
    /**
     * 取得表單關聯資訊
     * 
     * @param vo
     * @return ResponseEntity
     */
    @PostMapping(path = "/getFormLinkList")
    public ResponseEntity<List<BaseFormVO>> getFormLinkList(@RequestBody CommonFormVO vo) {
        List<BaseFormVO> rtnLs = new ArrayList<>();
        List<BaseFormVO> dataLs = commonFormService.getFormRelationship(vo.getFormId());
        
        for (BaseFormVO source : dataLs) {
            BaseFormVO target = new BaseFormVO();
            String groupName = StringUtils.isNotBlank(source.getGroupName()) ? source.getGroupName() : "";
            target.setFormClass(this.getMessage(FormEnum.valueOf(source.getFormClass()).wording()));
            target.setFormId(source.getFormId());
            
            if (StringUtils.isNotBlank(source.getSourceId())) {
                target.setSourceId(source.getSourceId());
            } else {
                target.setSourceId("");
            }
            
            LdapUserEntity ldapUser = ldapUserRepository.findByUserIdAndIsEnabled(source.getUserSolving(), StringConstant.SHORT_YES);
            target.setFormStatus(FormEnum.valueOf(source.getFormStatus()).formStatus(groupName));
            target.setCreatedAt(source.getCreatedAt());
            target.setDivisionSolving(source.getDivisionSolving());
            target.setUserSolving(StringUtils.isNotBlank(source.getUserSolving()) ? ldapUser.getName() : source.getUserSolving());
            target.setAct(source.getAct());
            target.setSct(source.getSct());
            target.setCct(source.getCct());
            rtnLs.add(target);
        }
        
        return ResponseEntity.ok(rtnLs);
    }
    
    /**
     * 取得可編輯的HTML欄位
     * 
     * @param vo
     * @return List
     */
    @PostMapping(path = "/getEditableCols")
    public Map<String, Object> getEditableCols (@RequestBody CommonFormVO vo, SysUserVO userInfo) {
        Map<String, Object> elements = new HashMap<>();
        
        // 整理欄位/按鈕
        elements.put("buttons", commonFormService.getEnabledButtons(vo, userInfo));
        elements.put("enabledColumns", commonFormService.getEnabledCols(vo, userInfo));
        elements.put("disabledColumns", commonFormService.getDisabledCols(vo));
        
        return elements;
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage() {
        return null;
    }
    
    /**
     * 查詢該表單該會辦科的會辦單狀態
     * 
     * @param formId
     * @param checkedDivision
     * @return 
     */
    @GetMapping(path = "/getCountersignedForm/{formId}/{checkedDivision}")
    public String getCountersignedForm (@PathVariable String formId,@PathVariable String checkedDivision) {
        boolean proposing = false;
        boolean inProcess = false;
        
        List<BaseFormVO> dataLs = commonFormService.getFormRelationship(formId);
        
        for(BaseFormVO source : dataLs) {
            if (StringUtils.equals(checkedDivision,source.getDivisionSolving()) &&
                    StringUtils.endsWith(source.getFormClass(),"_C")){
                if (StringUtils.equals(FormEnum.PROPOSING.name(), source.getFormStatus())) {
                    proposing = true;
                } else if (!StringUtils.equals(FormEnum.CLOSED.name(), source.getFormStatus()) &&
                        !StringUtils.equals(FormEnum.DEPRECATED.name(), source.getFormStatus())) {
                    inProcess = true;
                }
            }
        }
        
        if (inProcess) {
            return "inProcess";
        } else if (proposing) {
            return "proposing";
        } else {
            return "noCountersigned";
        }
    }
    
    /**
     * 作廢關聯的會辦單
     * 
     * @param formId
     * @param checkedDivision
     * @return 
     */
    @GetMapping(path = "/deprecatedForms/{formId}/{checkedDivision}/{formClass}/{countersigneds}")
    public void deprecatedForms (
            @PathVariable String formId,
            @PathVariable String checkedDivision,
            @PathVariable String formClass,
            @PathVariable String countersigneds
            ) {
        
        //依SR INC Q個別儲存countersigned
        commonFormService.updateCountersigneds(formId, formClass, countersigneds);
        
        List<String> dropFormIdList = new ArrayList<String>();
        List<BaseFormVO> dataLs = commonFormService.getFormRelationship(formId);
        for(BaseFormVO source : dataLs) {
            if (StringUtils.equals(checkedDivision, source.getDivisionSolving()) &&
                    StringUtils.endsWith(source.getFormClass(),"_C") &&
                    StringUtils.equals(FormEnum.PROPOSING.name(), source.getFormStatus())) {
                dropFormIdList.add(source.getFormId());
            }
        }
        commonFormService.deprecatedForms(dropFormIdList);
        
        commonFormService.updateContent(dropFormIdList);
    }
    
    /**
     * 更新主單的會辦科
     * 
     * @param formId
     * @param formClass
     * @param countersigneds
     */
    @GetMapping(path = "/saveCountersignds/{formId}/{formClass}/{countersigneds}")
    public void saveCountersignds (@PathVariable String formId, @PathVariable String formClass, @PathVariable String countersigneds) {
        commonFormService.updateCountersigneds(formId, formClass, countersigneds);
    }
    
    /**
     * 新增簽核紀錄
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/modifyCountersigndByVice")
    public void modifyCountersigndByVice (@RequestBody CommonFormVO vo) {
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // 新增一筆簽核紀錄(簽核意見=修改原因)
        commonFormService.createVerifyCommentByVice(vo);
        // 鎖定該表單所有檔案的狀態，改為不能刪除
        commonFormService.lockFormFileStatus(vo.getFormId());
        // 寄信通知上一關的審核者或處理人員
        commonFormService.notifyProcessMail(vo);
    }
    
    private void makeFileOut(HttpServletResponse response, File file) {
        OutputStream outStream = null;
        FileInputStream inStream = null;
        try {
            String name = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
            name = StringUtils.replace(name, "+", " ");
            response.setHeader("Content-disposition", "attachment; filename=" + name);
            inStream = new FileInputStream(file);
            outStream = response.getOutputStream();
            FileCopyUtils.copy(inStream, outStream);
        } catch (Exception e) {
            closeStream(inStream, outStream);
            e.printStackTrace();
        } finally {
            closeStream(inStream, outStream);
        }
    }
    
    // Fortify Correction
    private void closeStream (InputStream inStream, OutputStream outStream) {
        if (null != outStream) {
            try {
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != inStream) {
            try {
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
