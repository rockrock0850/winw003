package com.ebizprise.winw.project.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 表單編輯權限鎖, 實現多人審核/編輯當下之資料正確性議題
 * 
 * @author adam.yeh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FormLock {}