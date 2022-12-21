package cn.ezeyc.eotherplug.config;


import lombok.Getter;
import lombok.Setter;

/**
 * 百融api
 * @author wz
 */
@Setter
@Getter
public class BrApp   {

    /**
     * 百融请求域名
     */
    private  String host;
    /**
     * 贷前策略API
     */
    private String strategyUrl;
    /**
     * 信息验证API
     */
    private String verifyUrl;

    /**
     * 加密方式
     */
    private String encryType="md5";
    /**
     * 应用名称
     */
    private String apiName;
    /**
     * 策略编号
     */
    private String strategyId;
    /**
     * ApiCode账号
     */
    private String apiCode;
    /**
     * ApiCode密钥
     */
    private String appKey;


}
