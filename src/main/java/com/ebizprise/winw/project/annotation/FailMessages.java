package com.ebizprise.winw.project.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 根據i18n內的參數值找出對應多國語系值<br>
 * PS Annotation需放在最後一位<br>
 * Ex. <br>
 * 
 * NotEmpty()<br>
 * Length(min = 3)<br>
 * FailMessages({"xxx.xxx.xxx", "xxx.xxx.xxx"})<br>
 * private String test;<br>
 * 
 * @author adam.yeh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FailMessages {

    public String[] value ();
    
}
