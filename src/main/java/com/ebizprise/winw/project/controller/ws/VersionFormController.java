package com.ebizprise.winw.project.controller.ws;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.controller.BaseWsController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.IFormSearchService;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.FormDataListVO;
import com.ebizprise.winw.project.vo.FormSearchVO;
import com.ebizprise.winw.project.vo.JobInfoListVO;
import com.ebizprise.winw.project.vo.JobInfoVO;
import com.ebizprise.winw.project.vo.LogonRecordVO;
import com.ebizprise.winw.project.vo.VersionFormVO;

/**
 * 工作單內的程式庫頁簽需要介接其他系統的版本控制資料<br>
 * 參考文件 : 版本管理系統與電子表單介接  互動API 服務需求
 * @author adam.yeh
 */
@RestController
@RequestMapping("/ws")
public class VersionFormController extends BaseWsController {
    
    private static final Logger logger = LoggerFactory.getLogger(VersionFormController.class);

    @Value("${mock.version.form.switch}")
    private boolean isMock;

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IFormSearchService formSearchService;
    @Autowired
    private ICommonFormService commonFormService;
    
    /**
     * 查詢User開立工作單資訊
     * @param request
     * @return
     */
    @PostMapping(path = "/jobInfoList")
    public JobInfoListVO jobInfoList (@RequestBody VersionFormVO request) {
        String userId = request.getUserId();
        JobInfoListVO response = new JobInfoListVO();
        
        try {
            response.setUserId(userId);
            LogonRecordVO logonRecord = sysUserService.getActiviatedUser(userId);
            
            if (StringUtils.isBlank(logonRecord.getUserId())) {
                response.setQyStatus("0");
                return response;
            }
            
            FormSearchVO search = new FormSearchVO();
            search.setUserCreated(userId);
            List<VersionFormVO> jobInfoList = formSearchService.getJobInfoList(search);
            
            response.setQyStatus("1");
            response.setFormDataList(wrapJobInfoList(jobInfoList));
            response.setFormCounts(jobInfoList.size());
        } catch (Exception e) {
            response.setQyStatus("2");
            logger.error(getMessage("version.form.error.2"), e);
        }
        
        return response;
    }

    /**
     * 查詢工作單狀態
     * @param request
     * @return
     */
    @PostMapping(path = "/jobInfo")
    public JobInfoVO jobInfo (@RequestBody VersionFormVO request) {
        String formId = request.getFn();
        JobInfoVO response = new JobInfoVO();
        
        try {
            response.setFn(formId);
            
            FormSearchVO search = new FormSearchVO();
            search.setFormId(formId);
            List<VersionFormVO> jobInfoList = formSearchService.getJobInfoList(search);
            
            if (jobInfoList == null || jobInfoList.isEmpty()) {
                response.setQyStatus("0");
                return response;
            }
            
            response.setQyStatus("1");
            response.setFnStatus(wrapFnStatus(jobInfoList));
        } catch (Exception e) {
            response.setQyStatus("2");
            logger.error(getMessage("version.form.error.2"), e);
        }
        
        return response;
    }
    
    @PostMapping(path = "/mockVersionCodeBaseLine")
    public Map<String, Object> mockVersionCodeBaseLine (@RequestBody VersionFormVO vo) {
        Map<String, Object> response = new HashMap<>();
        response.put("FN", vo.getFn());
        response.put("QY_Status", "1");
        response.put("BaseLine", "BaseLine:RTM_108SC0004");
        
        return response;
    }
    
    @PostMapping(path = "/mockVersionCodeDiffProc")
    public Map<String, Object> mockVersionCodeDiffProc (@RequestBody VersionFormVO vo) {
        Map<String, Object> response = new HashMap<>();
        response.put("FN", vo.getFn());
        response.put("QY_Status", "3");
//        response.put("FileName", "McokFileName.zip");
        
        return response;
    }
    
    @PostMapping(path = "/mockDownloadDiffFile")
    public void mockDownloadDiffFile (@RequestBody CommonFormVO vo, HttpServletResponse response) {
        OutputStream outStream = null;
        FileInputStream inStream = null;
        
        try {
            List<CommonFormVO> fileList = commonFormService.getFiles(vo);
            File file = commonFormService.download(fileList.get(0));
            String name = URLEncoder.encode(fileList.get(0).getName(), StandardCharsets.UTF_8.name());
            
            response.setHeader("Content-disposition", "attachment; filename=" + name + StringConstant.DOT + vo.getExtension());
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
    
    private List<FormDataListVO> wrapJobInfoList (List<VersionFormVO> jobInfoList) {
        List<FormDataListVO> result = new ArrayList<FormDataListVO>();
        
        for (VersionFormVO versionFormVO : jobInfoList) {
            FormDataListVO vo = new FormDataListVO();
            vo.setFn(versionFormVO.getFn());
            vo.setPublishDate(versionFormVO.getPublishDate());
            vo.setFnStatus(isCreatorStillApply(versionFormVO));
            result.add(vo);
        }
        
        return result;
    }

    // 表單流程是否在開單人員申請
    private String isCreatorStillApply (VersionFormVO versionFormVO) {
        FormEnum formStatus = FormEnum.valueOf(versionFormVO.getFnStatus());
        int isApplyLevelOne = Integer.valueOf(versionFormVO.getFormStatus());
        return (FormEnum.PROPOSING == formStatus || isApplyLevelOne == 1) ? "1" : "0";
    }
    
    private String wrapFnStatus (List<VersionFormVO> jobInfoList) {
        String fnStatus = "0";
        VersionFormVO vo = jobInfoList.get(0);
        FormEnum formStatus = FormEnum.valueOf(vo.getFnStatus());
        boolean isApplyLevelOne = "1".equals(vo.getFormStatus());
        
        if (isApplyLevelOne || 
                FormEnum.DEPRECATED == formStatus) {
            fnStatus = "1";
        }
        
        return fnStatus;
    }

    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }
    
}
