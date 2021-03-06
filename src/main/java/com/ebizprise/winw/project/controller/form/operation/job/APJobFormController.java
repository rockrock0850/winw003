package com.ebizprise.winw.project.controller.form.operation.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.net.HttpUtility;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.FormLock;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.SysCommonEnum;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.enums.WsEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.ICommonFormService;		 
import com.ebizprise.winw.project.service.impl.ApJobFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.ApJobFormVO;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.VersionCodeBaseLineVO;
import com.ebizprise.winw.project.vo.VersionCodeDiffProcVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * AP????????? ??????/??????/??????/???????????????
 * 
 * @author adam.yeh, AndrewLee
 */
@RestController
@RequestMapping("/apJobForm")
public class APJobFormController extends BaseController implements IBaseForm<ApJobFormVO> {
    
    private static final Logger logger = LoggerFactory.getLogger(APJobFormController.class);
    
    @Value("${mock.version.form.switch}")
    private boolean isMock;
    
    @Autowired
    private ApJobFormServiceImpl service;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private ICommonFormService commonFormService;	 
	
    /**
     * ???????????????????????????/BaseLine????????????
     * @return
     */
    @PostMapping(path = "/getVersionCodeList")
    public List<ApJobFormVO> getVersionCodeList (@RequestBody ApJobFormVO vo) {
        return service.getVersionCodes(vo.getFormId(), vo.getRowType());
    }
    
    /**
     * ????????????????????????????????????????????????
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/versionCodeDiffProc")
    public ApJobFormVO versionCodeDiffProc (@RequestBody ApJobFormVO vo) throws IOException {
        VersionCodeDiffProcVO apiVO = new VersionCodeDiffProcVO();
        
        try {
            String mockURL = env.getProperty("mock.version.code.diff.proc.url");
            String realURL = env.getProperty("version.form.version.code.diff.proc");
            String resJson = isMock ? mockURL : realURL;
            
            apiVO.setFn(vo.getFormId());
            resJson = HttpUtility.post(resJson, BeanUtil.toJson(apiVO), true);
            
            Map<String, Object> map = BeanUtil.fromJson(resJson);
            apiVO.setFn(MapUtils.getString(map, WsEnum.fN.name()));
            apiVO.setFileName(MapUtils.getString(
                    map, WsEnum.fileName.name(), getMessage("version.form.error.11")));
            apiVO.setQy_Status(MapUtils.getString(map, WsEnum.qY_Status.name()));
        } catch (Exception e) {
            apiVO.setQy_Status(WsEnum.VCDIFF_4.code());
            apiVO.setMsg(getMessage("version.form.error.4"));
            logger.error(getMessage(
                    "version.form.error.4",
                    new String[] {e.getMessage()}), e);
        }
        
        apiVO.setTime(new Date());
        apiVO.setMsg(getMessage(
                WsEnum.fromVcDiff(apiVO.getQy_Status())));
        BeanUtil.copyProperties(apiVO, vo);
        
        if (WsEnum.VCDIFF_1.code()
                .equals(apiVO.getQy_Status())) {
            downloadDiffFile(vo);
        }

        vo.setQyStatus(apiVO.getQy_Status());
        vo.setRowType(FormJobEnum.LIBRARY.name());
        service.saveCodeDiff(vo);
        
        return vo;
    }
    
    /**
     * ??????????????????????????????BaseLine??????
     * @param vo
     * @return
     */
    @PostMapping(path = "/versionCodeBaseLine")
    public ApJobFormVO versionCodeBaseLine (@RequestBody ApJobFormVO vo) {
        VersionCodeBaseLineVO apiVO = new VersionCodeBaseLineVO();
        
        try {
            String mockURL = env.getProperty("mock.version.code.base.line.url");
            String realURL = env.getProperty("version.form.version.code.baseLine");
            String resJson = isMock ? mockURL : realURL;
            
            apiVO.setFn(vo.getFormId());
            resJson = HttpUtility.post(resJson, BeanUtil.toJson(apiVO), true);
            
            Map<String, Object> map = BeanUtil.fromJson(resJson);
            apiVO.setFn(MapUtils.getString(map, WsEnum.fN.name()));
            apiVO.setBaseLine(MapUtils.getString(
                    map, WsEnum.baseLine.name(), getMessage("version.form.error.11")));
            apiVO.setQy_Status(MapUtils.getString(map, WsEnum.qY_Status.name()));
        } catch (Exception e) {
            apiVO.setQy_Status(WsEnum.VCBASE_3.code());
            apiVO.setMsg(getMessage("version.form.error.4"));
            logger.error(getMessage(
                    "version.form.error.4",
                    new String[] {e.getMessage()}), e);
        }
        
        apiVO.setTime(new Date());
        apiVO.setMsg(getMessage(
                WsEnum.fromVcBase(apiVO.getQy_Status())));
        BeanUtil.copyProperties(apiVO, vo);
        
        vo.setQyStatus(apiVO.getQy_Status());
        vo.setRowType(FormJobEnum.BASELINE.name());
        service.saveCodeDiff(vo);
        
        return vo;
    }
    
    /**
     * ????????????????????????
     * @param vo
     * @throws IOException
     */
    @PostMapping(path = "/downloadDiffFile")
    public void downloadDiffFile (@RequestBody ApJobFormVO vo) throws IOException {
        byte[] file = null;
        
        if (isMock) {
            CommonFormVO commonFormVO = new CommonFormVO();
            commonFormVO.setFormId(vo.getFormId());
            file = HttpUtility.download(
                    env.getProperty("mock.download.diff.file.url"), BeanUtil.toJson(commonFormVO), true);
        } else {
            vo.setFn(vo.getFormId());
            file = HttpUtility.download(
                    env.getProperty("version.form.download.diff.file"), BeanUtil.toJson(vo), true);
        }
        
        vo.setData(file);
        service.saveDiffFile(vo);
    }

    /**
     * ?????????????????????
     * @param vo
     * @author adam.yeh
     * @throws IOException
     */
    @GetMapping(path = "/download/{id}/{formId}")
    public void download (ApJobFormVO vo) throws Exception {
        File file = service.download(vo);
        makeFileOut(response, vo, file);
    }
    
    /**
     * ???????????????
     */
    @PostMapping(path = "/build", produces = "text/plain;charset=UTF-8;")
    public String build (@RequestBody ApJobFormVO vo) {
        vo.setFormClass(FormEnum.JOB_AP.name());
        
        if (service.isNewerDetailExist(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.2"));
            return BeanUtil.toJson(vo);
        }
        
        if (service.isFormClosed(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return BeanUtil.toJson(vo);
        }
        
        service.create(vo);
        
        return BeanUtil.jEscape(BeanUtil.toJson(vo));
    }

    @Override
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView initPage () {
        return new ModelAndView(
                DispatcherEnum.FORM_SEARCH.dispatch(), "info", request.getParameter("formPostData"));
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/info")
    public ApJobFormVO info (@RequestBody ApJobFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @CtlLog
    @Override
    @FormLock
    @PostMapping(path = "/approval")
    public ApJobFormVO approval (@RequestBody ApJobFormVO vo) {
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        if (service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        service.prepareVerifying(vo);
        service.verifying(vo);
        // ????????????????????????????????????,??????????????????
        service.lockFormFileStatus(vo.getFormId());
        // ????????????????????????????????????
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @CtlLog
    @Override
    @FormLock
    @PostMapping(path = "/reject")
    public ApJobFormVO reject (@RequestBody ApJobFormVO vo) {
        SysUserVO loginUser = getUserInfo();
        
        if (!service.isVerifyAcceptable(vo, loginUser)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }

        boolean isPic = sysUserService.isPic(loginUser.getGroupId());
        
        if (isPic && service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        vo.setVerifyResult(FormEnum.DISAGREED.name());
        service.verifying(vo);
        // ????????????????????????????????????,??????????????????
        service.lockFormFileStatus(vo.getFormId());
        // ????????????????????????????????????????????????
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/deprecated")
    public ApJobFormVO deprecated (@RequestBody ApJobFormVO vo) {
        if (!service.isAllStretchDied(vo)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.4"));
            return vo;
        }
        
        vo.setFormStatus(FormEnum.DEPRECATED.name());
        vo.setProcessStatus(FormEnum.DEPRECATED.name());
        vo.setVerifyResult(FormEnum.DEPRECATED.name());
        service.verifying(vo);
        
        // ????????????????????????
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @ModifyRecord
    @PostMapping(path = "/save")
    public ApJobFormVO save (@RequestBody ApJobFormVO vo) throws Exception {
        boolean isAdmin = sysUserService.isAdmin();
        boolean isVice = sysUserService.isVice(getUserInfo().getGroupId());
		
        if (service.isFormClosed(vo) && !isAdmin && !isVice) {
            vo.setValidateLogicError(this.getMessage("form.common.save.warning.1"));
            return vo;
        }
        if (isAdmin) {
        	service.updateVerifyLog(vo);
        }
        
		service.mergeFormInfo(vo);
        
        return vo;
    }
    
    @Override
    @FormLock
    @PostMapping(path = "/modifyColsByVice")
    public ApJobFormVO modifyColsByVice (@RequestBody ApJobFormVO vo) throws Exception {
        service.mergeFormInfo(vo);
        service.updateVerifyLog(vo);
        vo.setVerifyResult(FormEnum.VSC_MODIFY.name());
        
        // ????????????????????????(????????????=????????????)
        service.createVerifyCommentByVice(vo);
        // ?????????????????????????????????????????????????????????
        service.lockFormFileStatus(vo.getFormId());
        // ????????????????????????????????????????????????
        service.notifyProcessMail(vo);
        
        return vo;
    }

    @Override
    @FormLock
    @PostMapping(path = "/send")
    public ApJobFormVO send (@RequestBody ApJobFormVO vo) throws Exception {
        vo.setCreateTime(new Date());
        vo.setFormStatus(FormEnum.APPROVING.name());
        vo.setProcessStatus(FormEnum.APPROVING.name());
        service.mergeFormInfo(vo);
        service.sendToVerification(vo);
        
        // ????????????????????????????????????,??????????????????
        service.lockFormFileStatus(vo.getFormId());
        
        // ????????????????????????????????????
        vo.setVerifyResult(FormEnum.SENT.name());
        service.notifyProcessMail(vo);
        
        return vo;
    }
    
    @Override
    @PostMapping(path = "/delete")
    public void delete (@RequestBody ApJobFormVO vo) {
        service.deleteForm(vo);
        service.getFormInfo(vo);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody ApJobFormVO vo) throws Exception {
        return validateColumnData(vo, false);
    }

    @CtlLog
    @Override
    public Map<String, Object> validateColumnData (@RequestBody ApJobFormVO vo, boolean isJustInfo) throws Exception {
		DataVerifyUtil verifyUtil = new DataVerifyUtil();
        boolean isAdmin = sysUserService.isAdmin();
        boolean isApplyLevelOne = (FormEnum.PROPOSING.toString().equals(vo.getFormStatus()) || StringUtils.isBlank(vo.getFormStatus())) ? true : false;
        
        // ???????????????????????????????????????????????????????????????????????????????????????????????????
        if (isApplyLevelOne && 
                StringConstant.SHORT_YES.equals(vo.getIsProgramOnline())) {
            boolean result = false;
            CommonFormVO commonVo = new CommonFormVO();
            commonVo.setFormId(vo.getFormId());
            commonVo.setType(SysCommonEnum.FILE.name());
            List<CommonFormVO> files = commonFormService.getFiles(commonVo);
            
            for (CommonFormVO file : files) {
                if (file.getName() != null && file.getName().contains(SysParametersEnum.APJOB_TESTING_REPORT_NAME.value)) {
                    result = true;
                    break;
                }
            }
            
            if (StringUtils.isBlank(vo.getFormId()) || !result) {
                verifyUtil.append(this.getMessage("form.job.error.message.1"));
            }
        }
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
		
		verifyUtil.string(vo.getsClass(), this.getMessage("form.question.form.info.service.type")) // ????????????
                .string(vo.getsClass(), this.getMessage("form.question.form.info.service.type"))   // ????????????
                .string(vo.getSystem(), this.getMessage("form.question.form.info.system.name"))    // ????????????
                .string(vo.getPurpose(), this.getMessage("form.search.column.job.ap.purpose"))     // ????????????
                .string(vo.getContent(), this.getMessage("form.question.form.info.content"));      // ??????
		
        if (isJustInfo) {
            return verifyUtil.build();
        }

        /*
         * ????????????????????????????????????????????????????????????,
         * ?????????????????????????????????????????????????????????????????????????????????????????????????????????,
         * ????????????????????????????????????????????????????????????????????????????????????
         */
        if (!vo.getIsSave()) {
            ApJobFormVO fromDB = new ApJobFormVO();
            fromDB.setFormId(vo.getFormId());
            fromDB.setVerifyType(vo.getVerifyType());
            service.getFormInfo(fromDB);
            
            List<String> fields = new ArrayList<>();
            fields.add("getUserCreated");
            fields.add("getUserSolving");
            fields.add("getGroupSolving");
            fields.add("getDivisionSolving");
            fields.add("getDivisionCreated");
            verifyUtil.equals(fromDB, vo, fields, ApJobFormVO.class);
        }
        
        return verifyUtil.build();
    }
    
    /**
     * ????????????
     * 
     * @param vo
     */
    @FormLock
    @PostMapping(path = "/closeForm")
    public ApJobFormVO closeForm (@RequestBody ApJobFormVO vo) {
        if (!service.isVerifyAcceptable(vo, getUserInfo())) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.1"));
            return vo;
        }
        
        if (service.verifyStretchForm(vo, FormVerifyType.STRETCH_FINISHED)) {
            vo.setValidateLogicError(this.getMessage("form.common.approval.warning.2"));
            return vo;
        }
        
        service.immediateClose(vo);
        vo.setVerifyResult(FormEnum.CLOSED.name());
        service.notifyProcessMail(vo);
        
        return vo;
    }
    
    /**
     * ??????????????????
     * @param vo
     */
    @PostMapping(path = "/hasStretchs")
    public boolean hasStretchs (@RequestBody ApJobFormVO vo) {
        return service.verifyStretchForm(vo, FormVerifyType.STRETCH_ZERO);
    }
    
    private void makeFileOut (HttpServletResponse response, ApJobFormVO vo, File file) {
        OutputStream outStream = null;
        FileInputStream inStream = null;
        try {
            String[] fileName = vo.getFileName().split("\\.");
            String name = URLEncoder.encode(fileName[0], StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment; filename=" + name + StringConstant.DOT + fileName[1]);
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
