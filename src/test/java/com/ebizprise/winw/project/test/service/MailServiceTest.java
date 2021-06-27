package com.ebizprise.winw.project.test.service;

import com.ebizprise.winw.project.payload.Mail;
import com.ebizprise.winw.project.service.IMailService;
import com.ebizprise.winw.project.test.base.TestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MailServiceTest extends TestBase {

	@Autowired
	private IMailService mailService;

	private String[] mailto = { "gary.tsai@ebizprise.com" };
	private String[] mailcc = { "gary.tsai@ebizprise.com" };

	// @Ignore
	@Test
	public void testSendSimpleMail() throws MessagingException {
		mailService.setInitData();
		Mail mail = new Mail();
		mail.setMailFrom("uscc.sys@ebizprise.com");
		mail.setMailTo(mailto);
		mail.setMailSubject("測試");
		mail.setMailCc(mailcc);
		mail.setMailContent("testSendSimpleMail");
		mailService.sendEmail(mail);
	}

	@Ignore
	@Test
	public void testSendAttachedMail() {
		Mail mail = new Mail();
		mail.setMailFrom("Gary");
		mail.setMailTo(mailto);
		mail.setMailSubject("測試");
		mail.setMailCc(mailcc);
		mail.setMailContent("testSendAttachedMail");
		List<File> attachmentsList = new ArrayList<File>();
		attachmentsList.add(new File("/home/zipe/tmp/JdbcClient.java"));
		attachmentsList.add(new File("/home/zipe/tmp/invoice.jpeg"));
		mail.setAttachments(attachmentsList);
//		try {
//			mailService.attachedSend(mail);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
	}

	@Ignore
	@Test
	public void testSendRichContentMail() {
		Mail mail = new Mail();
		mail.setMailFrom("Gary");
		mail.setMailTo(mailto);
		mail.setMailSubject("測試");
		mail.setMailCc(mailcc);
		mail.setMailContent("testSendRichContentMail");
		List<File> attachmentsList = new ArrayList<File>();
		attachmentsList.add(new File("/home/zipe/tmp/SpringMVCAnt.log"));
		attachmentsList.add(new File("/home/zipe/tmp/startbootstrap-sb-admin-2-gh-pages/dist/css/sb-admin-2.css"));
		mail.setAttachments(attachmentsList);
//		try {
//			mailService.attachedSend(mail);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
	}

	@Ignore
	@Test
	public void testSendBatchMailWithFileMail() {
		Mail mail = new Mail();
		mail.setMailFrom("Gary");
		mail.setMailTo(mailto);
		mail.setMailSubject("測試");
		mail.setMailCc(mailcc);
		mail.setMailContent("testSendBatchMailWithFileMail");
		List<File> attachmentsList = new ArrayList<File>();
		attachmentsList.add(new File("/home/zipe/tmp/SpringMVCAnt.log"));
		// attachmentsList.add(new File("/home/zipe/tmp/quartz-2.3.1.jar"));
		attachmentsList.add(new File("/home/zipe/tmp/invoice.jpeg"));
		attachmentsList.add(new File("/home/zipe/tmp/startbootstrap-sb-admin-2-gh-pages/dist/css/sb-admin-2.css"));
		mail.setAttachments(attachmentsList);
//		try {
//			mailService.attachedSend(mail);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
	}
}
