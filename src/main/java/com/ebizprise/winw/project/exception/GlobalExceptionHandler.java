package com.ebizprise.winw.project.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.ErrorLogEntity;
import com.ebizprise.winw.project.payload.ValidationErrorResponse;
import com.ebizprise.winw.project.service.IErrorLogService;
import com.ebizprise.winw.project.service.startup.FormLockHelper;
import com.microsoft.sqlserver.jdbc.SQLServerException;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private Environment env;
    @Autowired
    private FormLockHelper helper;
    @Autowired
    private IErrorLogService errorLogService;

    @ResponseBody
    @ExceptionHandler(FormException.class)
    public ResponseEntity<ErrorLogEntity> handleFormException (HttpServletRequest request, Exception e){
        helper.release();
        ErrorLogEntity errorLog = handleErrorMessage(request.getServerName(), e);
        errorLog.setMessage(getMessage("global.unknow.error1"));
        
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorLog);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLogEntity> handleException(HttpServletRequest request, Exception e){
        helper.release();
        ErrorLogEntity errorLog = handleErrorMessage(request.getServerName(), e);
        errorLog.setMessage(getMessage("global.unknow.error"));
        
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorLog);
    }

    @ResponseBody
    @ExceptionHandler(SchedulerException.class)
    public ResponseEntity<ErrorLogEntity> handleSchedulerExceptionException(HttpServletRequest request, SchedulerException e) {
        ErrorLogEntity errorLog = handleErrorMessage(request.getServerName(), e);
        errorLog.setMessage(getMessage("job.exception.message.3", e.getMessage()));
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorLog);
    }

    @ResponseBody
    @ExceptionHandler(SQLServerException.class)
    public ResponseEntity<ErrorLogEntity> handleClassNotFoundException(HttpServletRequest request, SQLServerException e) {
        ErrorLogEntity errorLog = handleErrorMessage(request.getServerName(), e);
        errorLog.setMessage(getMessage("global.unknow.error"));
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorLog);
    }

    @ResponseBody
    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<ErrorLogEntity> handleClassNotFoundException(HttpServletRequest request, ClassNotFoundException e) {
        ErrorLogEntity errorLog = handleErrorMessage(request.getServerName(), e);
        errorLog.setMessage(getMessage("global.unknow.error"));
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorLog);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationErrorResponse> handleFiledException(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse> validateErrorList = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validateErrorList
                    .add(new ValidationErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        
        return validateErrorList;
    }

    private ErrorLogEntity handleErrorMessage(String serverName, Exception e) {
        logger.error(e.getMessage(), e);
        
        ErrorLogEntity errorLog = new ErrorLogEntity();
        errorLog.setTime(new Date());
        errorLog.setServerIp(serverName);
        
        if (null != e.getCause()) {
            errorLog.setMessage(e.getCause().getMessage() + "," + e.getMessage());
        } else {
            errorLog.setMessage(e.getMessage());
        }
        
        // 當紀錄 Error log 打開時才會紀錄到 db 中
        if(StringConstant.TRUE.equalsIgnoreCase(env.getProperty("error.log.record.to.db"))){
            errorLogService.saveOrUpdate(errorLog);
        }
        
        return errorLog;
    }
    
}