package com.ebizprise.winw.project.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Service層若要實作切面Log功能, <br>
 * 只需要在想實作的方法上面新增這個Annotation
 * @author adam.yeh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModLog {}
