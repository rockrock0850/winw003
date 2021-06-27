package com.ebizprise.winw.project.exception;

/**
 * 處理表單審核/更新等等操作的過程中發生的錯誤
 * @author adam.yeh
 */
public class FormException extends Exception {

    private static final long serialVersionUID = 1L;

    public FormException (String msg) {
        super(msg);
    }

    public FormException (String msg, Throwable t) {
		super(msg, t);
	}
	
}
