package com.ebizprise.winw.project.enums;

/**
 * 定義寄信所需要的格式檔案名稱
 */
public enum MailTemplate {
    
    AGREED("formAgreeContent.vm"),
    AGREED_PIC("formAgreeContentForPic.vm"),
    DISAGREED("formDisagreeContent.vm"),
    DEPRECATED("formDeprecatedContent.vm"),
    CLOSED("formClosedContent.vm"),
    VICE_MODIFY("formModifyByViceContent.vm");

    private String template;

    MailTemplate (String template) {
        this.template = template;
    }

    public String src () {
        return template;
    }
    
}
