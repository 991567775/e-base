package cn.ezeyc.etxplug.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wz
 */
@Getter
@Setter
public class wxMp {

    /**
     * 小程序appid
     */
    private String appId;
    /**
     * 小程序秘钥
     */
    private String appSecret;

    private String code;

    private String grantType;


}
