package com.ebizprise.winw.project.aspect;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.entity.FormContentModifyLogEntity;
import com.ebizprise.winw.project.repository.IFormContentModifyLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 當 "com.ebizprise.winw.project.controller.form.operation" 中程式方法命名為 save 開頭及使用 ModifyRecord 的註解
 * 而物件為 "CommonFormVO" 型態 就會在正常流程完成後紀錄在 "FORM_CONTENT_MODIFY_LOG" 中
 */
@Component
@Aspect
public class FormContentModifyAspect {

    private static final Logger logger = LoggerFactory.getLogger(FormContentModifyAspect.class);

    @Autowired
    private IFormContentModifyLogRepository formContentModifyLogRepository;

    @Pointcut("execution(* com.ebizprise.winw.project.controller.form.operation..*.*(..)) && @annotation(com.ebizprise.winw.project.annotation.ModifyRecord)")
    public void modifyPointcut() {
    }

    @AfterReturning("modifyPointcut()")
    public void recodingLog(JoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();

        for (Object obj : arguments) {
            if (obj instanceof List) {
                for (Object objDetail : (List<?>) obj) {
                    if (objDetail instanceof BaseFormVO) {
                        saveLog((BaseFormVO) objDetail);
                    } else {
                        break;
                    }
                }
            } else if (obj instanceof BaseFormVO) {
                saveLog((BaseFormVO) obj);
            }
        }
    }

    private void saveLog(BaseFormVO baseFormVO) {
        if (StringUtils.isBlank(baseFormVO.getUpdatedBy()) || Objects.isNull(baseFormVO.getUpdatedAt())) {
            logger.warn("Form_id : %s, 並無更新時間或使用者!!", baseFormVO.getFormId());
            return;
        }
        FormContentModifyLogEntity formContentModifyLogEntity = new FormContentModifyLogEntity();
        formContentModifyLogEntity.setFormId(baseFormVO.getFormId());
        formContentModifyLogEntity.setContents(baseFormVO.toString());
        formContentModifyLogEntity.setUpdatedAt(baseFormVO.getUpdatedAt());
        formContentModifyLogEntity.setUpdatedBy(baseFormVO.getUpdatedBy());
        // 因希望紀錄 log 的程式不要影響正常流程資料，所以發生 Exception 時只會在 log 檔中顯示
        try {
            formContentModifyLogRepository.save(formContentModifyLogEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
