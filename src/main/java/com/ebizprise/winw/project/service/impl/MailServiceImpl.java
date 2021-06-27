package com.ebizprise.winw.project.service.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.enums.SysParametersEnum;
import com.ebizprise.winw.project.payload.Mail;
import com.ebizprise.winw.project.service.IMailService;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.vo.SysParameterVO;

@Service("mailService")
public class MailServiceImpl implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private Environment env;
    @Autowired
    private ISystemConfigService systemConfigService;

    private JavaMailSenderImpl mailSender;
    
    @Override
    public void setInitData() throws MessagingException {
        //創建郵件發送服務器
        mailSender = new JavaMailSenderImpl();
        mailSender.setProtocol(env.getProperty("mail.transport.protocol"));
        String mailPassword;
        List<SysParameterVO> sysParameterVOList = systemConfigService.selectParametersByKey("mail");
        Properties javaMailProperties = new Properties();
        if (CollectionUtils.isNotEmpty(sysParameterVOList)) {
            for (SysParameterVO sysParameterVO : sysParameterVOList) {
                if (SysParametersEnum.isKeyExists(sysParameterVO.getParamKey())) {
                    switch (sysParameterVO.getParamKey()) {
                        case "MAIL_DEBUG":
                            javaMailProperties.put("mail.debug", sysParameterVO.getParamValue());
                            continue;
                        case "MAIL_SERVER_ACCOUNT":
                            if (StringUtils.isNotBlank(sysParameterVO.getParamValue())) {
                                mailSender.setUsername(sysParameterVO.getParamValue());
                            }
                            continue;
                        case "MAIL_SERVER_PASSWORD":
                            if (StringUtils.isNotBlank(sysParameterVO.getParamValue())) {
                                mailSender.setPassword(sysParameterVO.getParamValue());
                            }
                            continue;
                        case "MAIL_SERVER_HOST":
                            mailSender.setHost(sysParameterVO.getParamValue());
                            continue;
                        case "MAIL_SERVER_PORT":
                            mailSender.setPort(Integer.valueOf(sysParameterVO.getParamValue()));
                            continue;
                        case "MAIL_SMTP_AUTH":
                            javaMailProperties.put("mail.smtp.auth", sysParameterVO.getParamValue().equals(StringConstant.SHORT_YES) ? StringConstant.TRUE : sysParameterVO.getParamValue());
                    }
                }
            }
        } else {
            javaMailProperties.put("mail.debug", env.getProperty("mail.debug"));
            javaMailProperties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
            mailSender.setUsername(env.getProperty("mail.username"));
            mailPassword = env.getProperty("mail.pa55word");
            mailSender.setHost(env.getProperty("mail.host"));
            mailSender.setPort(Integer.valueOf(env.getProperty("mail.port")));

            // 如設定檔將編碼加密開啟，需解碼
            String encrypt = env.getProperty("encrypt.enabled");
            if (StringUtils.isNotBlank(encrypt) && encrypt.equalsIgnoreCase(StringConstant.TRUE) && StringUtils.isNotBlank(mailPassword)) {
                CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
                mailPassword = cryptoUtil.decode(mailPassword);
            }
            mailSender.setPassword(mailPassword);
        }

        // 加入認證機制
        javaMailProperties.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // Timeout 機制
        javaMailProperties.put("mail.smtp.connectiontimeout", 5000);
        javaMailProperties.put("mail.smtp.timeout", 3000);
        javaMailProperties.put("mail.smtp.writetimeout", 5000);
        mailSender.setJavaMailProperties(javaMailProperties);
        mailSender.testConnection();
        logger.info("初始化郵件服務完成");
    }

    @Override
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setCc(mail.getMailCc());
            mimeMessageHelper.setText(mail.getMailContent(), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void simpleMailSend(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail.getMailFrom());
        message.setTo(mail.getMailTo());
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailContent());
        mailSender.send(message);
        logger.info("發送成功");
    }

    @Override
    public void attachedSend(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 創建MimeMessageHelper對象，處理MimeMessage的輔助類
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        // 使用辅助类MimeMessage设定参数
        mimeMessageHelper.setFrom(mail.getMailFrom());
        mimeMessageHelper.setTo(mail.getMailTo());
        mimeMessageHelper.setCc(mail.getMailCc());
        mimeMessageHelper.setSubject(mail.getMailSubject());
        mimeMessageHelper.setText(mail.getMailContent());

        if (null != mail.getAttachments()) {
            for (File file : mail.getAttachments()) {
                mimeMessageHelper.addAttachment(file.getName(), file);
            }
        }

        mailSender.send(mimeMessage);
        logger.info("發送成功");
    }

    @Override
    public void richContentSend(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        
        try {
            mimeHelper.setTo(mail.getMailTo());
            mimeHelper.setFrom(mail.getMailFrom());
            if (!Objects.isNull(mail.getMailCc()) && mail.getMailCc().length > 0) {
                mimeHelper.setCc(mail.getMailCc());
            }
            mimeHelper.setSubject(mail.getMailSubject());
            // 第二個參數true，表示text的內容為html，然後注意<img/>標籤，src='cid:file'，'cid'是contentId的縮寫，'file'是一個標記，
            // 需要在後面的代碼中調用MimeMessageHelper的addInline方法替代成文件
            mimeHelper.setText(mail.getMailContent(), true);
            // 文件地址相對應src目錄
            // ClassPathResource file = new ClassPathResource("logo.png");

            if (null != mail.getAttachments()) {
                for (File file : mail.getAttachments()) {
                    mimeHelper.addAttachment(file.getName(), file);
                }
            }

            mailSender.send(mimeMessage);
            
        } catch (Exception e) {
            throw e;
        }
        logger.info("發送成功");
    }

    @Override
    public void sendBatchMailWithFile(Mail mail) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        mimeHelper.setSubject(mail.getMailSubject());
        mimeHelper.setFrom(new InternetAddress(MimeUtility.encodeText(mail.getMailFrom())));
        
        if (CollectionUtils.isEmpty(mail.getAttachments())) {
            mimeHelper.setText(mail.getMailContent(), true);
            mimeMessage = mimeHelper.getMimeMessage();
        } else {
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(mail.getMailContent(), "text/html;charset=UTF-8");
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart);
            
            // 逐個加入附件
            String fileName;
            MimeBodyPart mimeBodyPart;
            FileDataSource dataSource;
            for (int i = 0; i < mail.getAttachments().size(); i++) {
                dataSource = new FileDataSource(mail.getAttachments().get(i));
                fileName = dataSource.getName();
                mimeBodyPart = new MimeBodyPart();
                
                try {
                    mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                    mimeBodyPart.setFileName(MimeUtility.encodeText(fileName));
                } catch (Exception e) {
                    logger.error("附件「" + fileName + "」附加至郵件失敗。", e);
                }
                
                multiPart.addBodyPart(mimeBodyPart);
            }
            
            mimeMessage.setContent(multiPart);
        }

        mimeMessage.setRecipients(Message.RecipientType.TO, wrapRecipients(mail.getMailTo()));

        mailSender.send(mimeMessage);
        logger.info("發送成功");
    }

    // 將String[]轉成InternetAddress[]
    private InternetAddress[] wrapRecipients (String[] mailAry) {
        String mailTo;
        // 多型參數內不可使用String類別, 因為會只發送給1個郵件地址
        List<InternetAddress> addresses = new ArrayList<InternetAddress>();
        
        for (int i = 0; i < mailAry.length; i++) {
            mailTo = mailAry[i];
            
            try {// 若後面有效能問題再把catch改成方法層級
                addresses.add(new InternetAddress(mailTo));
            } catch (AddressException e) {
                logger.error("郵件地址「" + mailTo + "」轉型「InternetAddress」失敗。", e);
            }
        }
        
        return addresses.toArray(new InternetAddress[addresses.size()]);
    }

}
