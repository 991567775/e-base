package cn.ezeyc.core.config;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import java.util.Properties;

@Component
@Setter
@Getter
public class MailConfig {
    @Inject(value = "${e.mail.host}",required = false)
    private String host;
    @Inject(value = "${e.mail.username}",required = false)
    private String username;
    @Inject(value = "${e.mail.nickname}",required = false)
    private String nickname;
    @Inject(value = "${e.mail.password}",required = false)
    private String password;
    @Inject(value = "${e.mail.encoding}",required = false)
    private String encoding="utf-8";
    @Inject(value = "${e.mail.auth}",required = false)
    private String auth="true";
    @Inject(value = "${e.mail.protocol}",required = false)
    private String protocol="smtp";


    private  static Transport ts=null;
    public  MimeMessage init(){
        final Properties props = new Properties();
        //允许邮箱授权认证（这里和申请授权码的邮箱协议一定要一致）
        props.put("smtp.auth", auth);
        //设置邮件主机
        props.put("smtp.host", host);
        //设置邮件发送协议
        props.put("smtp.transport.protocol", protocol);
        // 使用环境属性和授权信息，创建邮件会话
        Session session =  Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        //获取服务器连接
        try {
            ts = session.getTransport();
            ts.connect(host,username,password);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //连接至服务器
      return  new MimeMessage(session);
    }

    public  void  send(Message message){
        try {
            ts.sendMessage(message,message.getAllRecipients());//new Address[]{new InternetAddress(USER)}
            ts.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
