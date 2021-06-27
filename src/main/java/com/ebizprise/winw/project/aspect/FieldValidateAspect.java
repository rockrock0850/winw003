package com.ebizprise.winw.project.aspect;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.annotation.FailMessages;
import com.ebizprise.winw.project.vo.FieldValidationVO;

/**
 * 統一驗證VO內需要檢核的欄位<br>
 * PS 使用前須配合以下條件<br>
 *    1. Custom Annotation FailMessages<br>
 *    2. Hibernate Annotation Valid<br>
 *    3. Spring API BindingResult<br>
 *    4. 在message.properties裡面新增[vo的class名稱( 全小寫 )]+[vo需要檢核的欄位名稱]<br>
 * 
 * @author adam.yeh
 */
@Aspect
@Controller
public class FieldValidateAspect {
    
    @Autowired
    private MessageSource source;
    
    private List<String> properties;

    @Before("execution(* com.ebizprise.winw.project.controller..*.*(..))")
    public void before (JoinPoint joinPoint) throws Throwable {
        BindingResult result = null;
        Object[] argList = joinPoint.getArgs();
        
        for (Object arg : argList) {
            if(arg instanceof BindingResult){
                result = (BindingResult) arg;
                break;
            }
        }
        
        if (result == null || !result.hasErrors()) {
            return;
        }
        
        sendFailedResults(FetchMessageFromFields(result));
    }

    /*
     * 1. 根據FailMessages註釋所設定的i18n參數找到對應的語系值
     * 2. 根據檢核VO名稱+檢核欄位找到對應的欄位語系名稱
     */
    private Map<String, List<FieldValidationVO>> FetchMessageFromFields (BindingResult result) {
        FailMessages validateFail;
        Object vo = result.getTarget();
        String className = vo.getClass().getSimpleName() + ".";
        String fieldName;
        String errorName;
        String errorMessage;
        List<FieldValidationVO> failList = new ArrayList<>();
        Map<String, List<FieldValidationVO>> map = new HashMap<>();
        
        for (Field field : vo.getClass().getDeclaredFields()) {
            fieldName = field.getName();
            for (FieldError error : result.getFieldErrors()) {
                errorName = error.getField();
                if (StringUtils.equals(fieldName, errorName)) {
                    validateFail = field.getAnnotation(FailMessages.class);
                    errorMessage = validateFail == null ? "global.unknow.message" : getProperty(validateFail.value());
                    errorMessage = getMessage(errorMessage);
                    
                    FieldValidationVO fieldVO = new FieldValidationVO();
                    fieldVO.setFieldName(getMessage(
                            StringUtils.lowerCase(className) + errorName));
                    fieldVO.setErrorMessage(errorMessage);
                    failList.add(fieldVO);
                }
            }
        }
        map.put("validateFieldErrors", failList);
        
        return map;
    }
    
    private void sendFailedResults (Map<String, List<FieldValidationVO>> map) throws Exception {
        ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = res.getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        
        OutputStream output = response.getOutputStream();
        output.write(BeanUtil.toJson(map).getBytes(StandardCharsets.UTF_8.name()));
        output.close();
    }
    
    private String getProperty (String[] value) {
        if (properties == null || properties.size() == 0) {
            properties = new ArrayList<String>(Arrays.asList(value));
        }
        
        String property = properties.get(0);
        properties.remove(0);
        
        return property;
    }
    
    private String getMessage (String key, String... args) {
        return source.getMessage(key, args, LocaleContextHolder.getLocale());
    }
    
}
