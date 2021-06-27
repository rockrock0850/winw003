package com.ebizprise.winw.project.enums.form.editable;

/**
 * 按鈕權限對應表<br><br>
 * 
 * 欄位名稱與HTML的欄位屬性id對應<br>
 * 參考文件 : commonButtons.jsp
 * 
 * @author adam.yeh
 */
public enum ButtonsEnum {

    Q("button#clickQButton"),
    ALTER("button#clickAlterButton"),
    JOB("button#clickJobCFormButton"),
    SAVE("button#formInfoSaveButton"),
    SEND("button#formInfoSendButton"),
    C("button#clickCountersignedButton"),
    WORKING("button#clickWorkingButton"),
    DELETE("button#formInfoDeleteButton"),
    SIGNING("button#signingButton"),
    DEPRECATED("button#checkLogDeleteButton"),
    CLOSE("button#checkLogCloseForm"),
    PROGRAM("button#programSaveButton"),
    LOGNEW("button#logNewButton"),
    LOGDEL("button#logDeleteButton"),
    LOGSAVE("button#logSaveButton"),
    FILENEW("button#fileListNewButton"),
    FILEDEL("button#fileListDeleteButton"),
    PERSON("button#checkPersonSaveButton"),
    IMPACT("button#formImpactAnalysisSaveButton"),
    SOLVE("button#userSolvingButton"),
	INTERNAL_PROCESS("button#internalProcessButton"),
	SPLIT_PROCESS("button#splitProcessViceButton");
    
    private String id;
    
    private ButtonsEnum (String id) {
        this.id = id;
    }
    
    public String id () {
        return this.id;
    }
    
}
