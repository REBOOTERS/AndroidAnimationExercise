package home.smart.fly.animations.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Rookie on 2017/8/8.
 */


public class SendEmail {
    private Properties properties;
    private Session session;
    private Message message;
    private MimeMultipart multipart;

    public SendEmail() {
        super();
        this.properties = new Properties();
    }

    public void setProperties(String host, String post) {
        //地址
        this.properties.put("mail.smtp.host", host);
        //端口号
        this.properties.put("mail.smtp.post", post);
        //是否验证
        this.properties.put("mail.smtp.auth", true);
        this.session = Session.getInstance(properties);
        this.message = new MimeMessage(session);
        this.multipart = new MimeMultipart("mixed");
    }

    /**
     * 设置收件人
     *
     * @param receiver
     * @throws MessagingException
     */
    public void setReceiver(String[] receiver) throws MessagingException {
        Address[] address = new InternetAddress[receiver.length];
        for (int i = 0; i < receiver.length; i++) {
            address[i] = new InternetAddress(receiver[i]);
        }
        this.message.setRecipients(Message.RecipientType.TO, address);
    }

    /**
     * 设置邮件
     *
     * @param from    来源
     * @param title   标题
     * @param content 内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void setMessage(String from, String title, String content) throws AddressException, MessagingException {
        this.message.setFrom(new InternetAddress(from));
        this.message.setSubject(title);
        //纯文本的话用setText()就行，不过有附件就显示不出来内容了
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(content, "text/html;charset=gbk");
        this.multipart.addBodyPart(textBody);
    }

    /**
     * 添加附件
     *
     * @param filePath 文件路径
     * @throws MessagingException
     */
    public void addAttachment(String filePath) throws MessagingException {
        FileDataSource fileDataSource = new FileDataSource(new File(filePath));
        DataHandler dataHandler = new DataHandler(fileDataSource);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDataHandler(dataHandler);
        mimeBodyPart.setFileName(fileDataSource.getName());
        this.multipart.addBodyPart(mimeBodyPart);
    }

    /**
     * 发送邮件
     *
     * @param host    地址
     * @param account 账户名
     * @param pwd     密码
     * @throws MessagingException
     */
    public void sendEmail(String host, String account, String pwd) throws MessagingException {
        //发送时间
        this.message.setSentDate(new Date());
        //发送的内容，文本和附件
        this.message.setContent(this.multipart);
        this.message.saveChanges();
        //创建邮件发送对象，并指定其使用SMTP协议发送邮件
        Transport transport = session.getTransport("smtp");
        //登录邮箱
        transport.connect(host, account, pwd);
        //发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
    }
}