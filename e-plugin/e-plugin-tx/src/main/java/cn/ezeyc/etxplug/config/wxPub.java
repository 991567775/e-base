package cn.ezeyc.etxplug.config;


import lombok.Getter;
import lombok.Setter;


/**
 * 微信公众号/服务号 开发配置
 * @author wz
 */
@Getter
@Setter
public class wxPub  {

    /**
     * 微信公众号/服务号 appId
     */
    private String appId;
    /**
     * 微信公众号/服务号 秘钥
     */
    private String appSecret;

    private String code;

    private String grantType;


}
