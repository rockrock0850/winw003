package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.impl.AuditNotifyParamsServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.AuditNotifyParamsVO;

/**
 * 稽催通知參數設定
 * @author adam.yeh
 */
@RestController
@RequestMapping("/auditNotifyParams")
public class AuditNotifyParamsController extends BaseController {

    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private AuditNotifyParamsServiceImpl service;

    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.AUDIT_PARAMS.initPage(), "auditNotifyParams", service.getNotifyParams());
    }
    
    @PostMapping(path = "/save")
    public List<AuditNotifyParamsVO> save (@RequestBody AuditNotifyParamsVO vo) {
        service.mergeNotifyParams(vo);
        return vo.getParams();
    }

    @CtlLog
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody AuditNotifyParamsVO vo) {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil();
        String prefix, limit, mails, formType, notifyType;
        List<AuditNotifyParamsVO> params = vo.getParams();
        
        for (AuditNotifyParamsVO param : params) {
            limit = param.getTime();
            mails = param.getNotifyMails();
            formType = FormVerifyType.valueOf(param.getFormType()).desc();
            notifyType = FormVerifyType.valueOf(param.getNotifyType()).desc();
            prefix = getMessage("audit.notify.1");
            prefix = String.format(prefix, formType, notifyType);
            
            if (!StringUtils.isNumeric(limit) && 
                    FormVerifyType.EXPIRE_SOON.name().equals(param.getNotifyType())) {
                verifyUtil.append(prefix + getMessage("audit.notify.2"));
            }
            
            if (!StringUtils.contains(mails, ";")) {
                verifyUtil.append(prefix + getMessage("audit.notify.3"));
            } else {
                String[] mailArray = mails.split(";");
                List<String> mailList = Arrays.asList(mailArray);
                
                if (verifyMailSplit(mailList)) {
                    verifyUtil.append(prefix + getMessage("audit.notify.4"));
                }
                
                verifyMailFormat(prefix, verifyUtil, mailList);
            }
        }
    
        return verifyUtil.build();
    }
    
    private void verifyMailFormat (String prefix, DataVerifyUtil verify, List<String> list) {
        for (String mail : list) {
            if (!CommonStringUtil.isEmailFormat(mail)) {
                verify.append(prefix + "「" + mail + getMessage("audit.notify.5"));
            }
        }
    }

    private boolean verifyMailSplit (List<String> list) {
        int count = 0;
        boolean result = true;
        
        for (String mail : list) {
            count = StringUtils.countMatches(mail, "@");
            if (count > 1) {
                result = false;
                break;
            }
        }
        
        return !result;
    }

}
