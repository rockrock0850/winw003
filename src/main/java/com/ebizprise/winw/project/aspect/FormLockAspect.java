package com.ebizprise.winw.project.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.service.startup.FormLockHelper;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單編輯權限鎖, 實現多人審核/編輯當下之資料正確性議題
 * 
 * @author adam.yeh
 */
@Aspect
@Controller
public class FormLockAspect extends BaseAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(FormLockAspect.class);

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private FormLockHelper helper;
    @Autowired
    private IFormRepository formRepo;
    @Autowired
    private IFormVerifyLogRepository verifyRepo;
    
    @Around("execution(* com.ebizprise.winw.project.controller.form.operation..*.*(..)) && @annotation(com.ebizprise.winw.project.annotation.FormLock)")
    public Object around (ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        Object[] argList = joinPoint.getArgs();
        
        if(!(argList[0] instanceof BaseFormVO)){
            logger.info("FormLock檢核錯誤 : {}沒有繼承BaseFormVO。", argList[0]);
            return joinPoint.proceed();
        }

        SysUserVO info = (SysUserVO) httpSession.getAttribute(UserInfoUtil.loginUserId());

        ClassLoader loader = argList[0].getClass().getClassLoader();
        Class<?> clazz = loader.loadClass(argList[0].getClass().getName());
        Object formVO = clazz.getConstructor().newInstance();
        BeanUtil.copyProperties(argList[0], formVO);
        
        Method getFormId = clazz.getMethod("getFormId");
        Method getUserId = clazz.getMethod("getUserId");
        Method getUserName = clazz.getMethod("getUserName");
        Method getVerifyLevel = clazz.getMethod("getVerifyLevel");
        Method getVerifyType = clazz.getMethod("getVerifyType");
        Method getUpdatedAt = clazz.getMethod("getUpdatedAt");
        Method getFormStatus = clazz.getMethod("getFormStatus");
        Method setValidateLogicError = clazz.getMethod("setValidateLogicError", String.class);

        String key = getFormId.invoke(formVO) + "_" + info.getUserId();
        
        synchronized (this) {
            if (helper.isModifying(key)) {
                String msg = "%s(%s) 編輯表單中...請稍候！";
                return out(
                        msg, helper.get((String) getFormId.invoke(formVO)),
                        setValidateLogicError, getUserId, getUserName);
            }

            locked(clazz, formVO, info, key);
        }
        
        if (!isProposeOrClosedOrDeprecated((String) getFormStatus.invoke(formVO)) && isUpdatedOrVerifyed(
                (String) getFormId.invoke(formVO),
                (String) getVerifyLevel.invoke(formVO),
                (String) getVerifyType.invoke(formVO),
                (Date) getUpdatedAt.invoke(formVO))) {
            Object form = helper.get((String) getFormId.invoke(formVO));
            helper.unlock(key);
            String msg = "表單已被更新, 請重新確認表單資訊再進行審核。";
            return out(msg, form, setValidateLogicError, getUserId, getUserName);
        }

        proceed = joinPoint.proceed();
        helper.unlock(key);

        return proceed;
    }

    private void locked (Class<?> clazz, Object vo, SysUserVO info, String key) throws Throwable {
        Method setUserId = clazz.getMethod("setUserId", String.class);
        Method setUserName = clazz.getMethod("setUserName", String.class);
        
        setUserId.invoke(vo, info.getName());
        setUserName.invoke(vo, info.getUserId());
        helper.lock(key, vo);
    }
    
    @Transactional
    private boolean isUpdatedOrVerifyed (String formId, String verifyLevel, String verifyType, Date updatedAt) {
        if (StringUtils.isBlank(formId)) {
            return false;
        }
        
        FormEntity formPojo = formRepo.findByFormId(formId);
        int count = verifyRepo.countByFormIdAndVerifyLevelAndVerifyTypeAndCompleteTimeIsNull(formId, verifyLevel, verifyType);
        boolean isProposing = FormEnum.PROPOSING.name().equals(formPojo.getFormStatus());
        boolean isVerifyed = !isProposing && !(count > 0);
        boolean isUpdated = !isProposing && formPojo.getUpdatedAt().compareTo(updatedAt) > 0;
        
        return isUpdated || isVerifyed;
    }

    private boolean isProposeOrClosedOrDeprecated (String formStatus) {
        return FormEnum.CLOSED.name().equals(formStatus) ||
                FormEnum.DEPRECATED.name().equals(formStatus) ||
                StringUtils.isBlank(formStatus);
    }

    private Object out (
            String msg,
            Object formVO,
            Method setValidateLogicError,
            Method getUserId, Method getUserName) throws Throwable {
        setValidateLogicError.invoke(
                formVO, String.format(msg, getUserId.invoke(formVO), getUserName.invoke(formVO)));
        return formVO;
    }
    
}
