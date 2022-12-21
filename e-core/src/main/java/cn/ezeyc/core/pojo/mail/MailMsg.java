package cn.ezeyc.core.pojo.mail;


import lombok.Getter;
import lombok.Setter;

/**
 * 邮件
 * @author wz
 */
@Getter
@Setter
public class MailMsg {
    /**
     * 邮件名称
     */
    private String title;
    /**
     * 内容
     */
    private String context;
    /**
     *邮箱地址
     */
    private String email;
    /**
     *附件地址
     */
    private  String filePath;


}
