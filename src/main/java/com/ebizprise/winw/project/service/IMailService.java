package com.ebizprise.winw.project.service;

import javax.mail.MessagingException;

import com.ebizprise.winw.project.payload.Mail;

public interface IMailService {

    /**
     * 初始化郵件發送數據
     */
	public void setInitData() throws MessagingException;

	public void sendEmail(Mail mail);

    /**
     * 發送一般信件
     */
	public void simpleMailSend(Mail mail);

    /**
     * 發送附件,支持多附件 //使用JavaMail的MimeMessage，支付更加複雜的郵件格式和內容
     * MimeMessages為複雜郵件模板，支持文本、附件、html、圖片等。
     */
	public void attachedSend(Mail mail) throws MessagingException;

    /**
     * 發送html文件，支持多圖片
     */
	public void richContentSend(Mail mail) throws MessagingException;

    /**
     * 群發多人，且多附件
     */
	public void sendBatchMailWithFile(Mail mail) throws Exception;
	
}
