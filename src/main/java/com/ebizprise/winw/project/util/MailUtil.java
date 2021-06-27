package com.ebizprise.winw.project.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailUtil {

    private JavaMailSenderImpl mailSender;

    /**
     * 初始化郵件發送數據
     *
     * @param host     服務器
     * @param username 發送人
     * @param passwd   發送人密碼
     */
    public void setInitData(String host, int port, String username, String passwd) {
        //創建郵件發送服務器
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(passwd);

        //加入認證機制
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        javaMailProperties.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.debug", true);
//        javaMailProperties.put("mail.smtp.timeout", 50000);
        mailSender.setJavaMailProperties(javaMailProperties);
        System.out.println("初始化郵件發送信息完成");
    }

    /**
     * 發送普通文本
     *
     * @param email   對方郵箱地址
     * @param subject 主題
     * @param text    郵件內容
     */
    public void simpleMailSend(String email, String subject, String text) {
        //創建郵件內容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("uscc.sys@ebizprise.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        //發送郵件
        mailSender.send(message);
        System.out.println("發送成功");
    }

    /**
     * 發送附件,支持多附件
     * //使用JavaMail的MimeMessage，支付更加複雜的郵件格式和內容
     * //MimeMessages為複雜郵件模板，支持文本、附件、html、圖片等。
     *
     * @param email   對方郵箱
     * @param subject 主題
     * @param text    內容
     * @param paths   附件路徑，和文件名
     * @throws MessagingException
     */
    public void attachedSend(String email, String subject, String text, Map<String, String> paths) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        //創建MimeMessageHelper對象，處理MimeMessage的輔助類
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //使用輔助類MimeMessage設定參數
        helper.setFrom(mailSender.getUsername());
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);

        if (paths != null) {
            paths.forEach((k, v) -> {
                //加載文件資源，作為附件
                FileSystemResource file = new FileSystemResource(v);
                try {
                    //添加附件
                    helper.addAttachment(k, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        //發送郵件
        mailSender.send(message);
        System.out.println("發送成功");
    }

    /**
     * 發送html文件，支持多圖片
     *
     * @param email   對方郵箱
     * @param subject 主題
     * @param text    內容
     * @param paths   富文本中添加用到的路徑，一般是圖片，或者css,js文件
     * @throws MessagingException
     */
    public void richContentSend(String email, String subject, String text, Map<String, String> paths) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(mailSender.getUsername());
        helper.setTo(email);
        helper.setSubject(subject);
        //第二個參數true，表示text的內容為html，然後注意<img/>標籤，src='cid:file'，'cid'是contentId的縮寫，'file'是一個標記，
        //需要在後面的代碼中調用MimeMessageHelper的addInline方法替代成文件
        helper.setText(text, true);
        //文件地址相對應src目錄
        // ClassPathResource file = new ClassPathResource("logo.png");

        if (paths != null) {
            paths.forEach((k, v) -> {
                //文件地址對應系統目錄
                FileSystemResource file = new FileSystemResource(v);
                try {
                    helper.addInline(k, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        mailSender.send(message);
        System.out.println("發送成功");
    }

    /**
     * 群發多人，且多附件
     *
     * @param emails   多人郵件地址
     * @param subject  主題
     * @param text     內容
     * @param filePath 文件路徑
     * @throws Exception
     */
    public void sendBatchMailWithFile(String[] emails, String subject, String text, String[] filePath) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(new InternetAddress(MimeUtility.encodeText(mailSender.getUsername())));
        messageHelper.setSubject(subject);
        if (filePath != null) {
            BodyPart mdp = new MimeBodyPart();// 新建一個存放信件內容的BodyPart對象
            mdp.setContent(text, "text/html;charset=UTF-8");// 給BodyPart對象設置內容和格式/編碼方式
            Multipart mm = new MimeMultipart();// 新建一個MimeMultipart對像用來存放BodyPart對象
            mm.addBodyPart(mdp);// 將BodyPart加入到MimeMultipart對像中(可以加入多個BodyPart)
            // 把mm作為消息對象的內容
            MimeBodyPart filePart;
            FileDataSource filedatasource;
            // 逐個加入附件
            for (int j = 0; j < filePath.length; j++) {
                filePart = new MimeBodyPart();
                filedatasource = new FileDataSource(filePath[j]);
                filePart.setDataHandler(new DataHandler(filedatasource));
                try {
                    filePart.setFileName(MimeUtility.encodeText(filedatasource.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mm.addBodyPart(filePart);
            }
            mimeMessage.setContent(mm);
        } else {
            messageHelper.setText(text, true);
        }

        List<InternetAddress> list = new ArrayList<InternetAddress>();// 不能使用string類型的類型，這樣只能發送一個收件人
        for (int i = 0; i < emails.length; i++) {
            list.add(new InternetAddress(emails[i]));
        }
        InternetAddress[] address = list.toArray(new InternetAddress[list.size()]);

        mimeMessage.setRecipients(Message.RecipientType.TO, address);
        mimeMessage = messageHelper.getMimeMessage();

        mailSender.send(mimeMessage);
        System.out.println("發送成功");
    }


    public static void main(String[] args) throws Exception {
        MailUtil test = new MailUtil();
        //測試發送普通文本
        //  test.setInitData("smtp.qq.com","706548532@qq.com","123456");
        test.setInitData("eptw00exch00.ebizprise.com", 25, "uscc.sys", "eBiz12673852");
        test.simpleMailSend("zipe.daden@gmail.com", "测试", "测试能不能发邮件！！！");

        //测试发送附件
//        test.setInitData("smtp.163.com", "1234@163.com", "1234");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("test12.xls", "D:\\tomcat8\\apache-tomcat-8.0.29\\test12.xls");
//        map.put("wsdl.rar", "D:\\wsdl.rar");
//        test.attachedSend("706548532@qq.com", "Hello Attachment", "This is a mail with attachment", map);

        //测试发送富文本（html文件）
      /*  test.setInitData("smtp.163.com","1234@163.com","1234");
        String text = "<body><p style='color:red;'>Hello Html Email</p><img src='cid:file'/></body>";
        Map<String,String> map = new HashMap<String, String>();
        map.put("file", "E:\\1f7827.jpg");
        test.richContentSend("706548532@qq.com","邮件标题",text,map);*/

        //测试群发多人多附件
//        test.setInitData("smtp.163.com", "1234@163.com", "1234");
//        String[] address = {"706548532@qq.com", "1326624701@qq.com"};
//        String[] filePath = {"D:\\tomcat8\\apache-tomcat-8.0.29\\test12.xls", "D:\\wsdl.rar"};
//        test.sendBatchMailWithFile(address, "群发多文件", "实时", filePath);
    }

}