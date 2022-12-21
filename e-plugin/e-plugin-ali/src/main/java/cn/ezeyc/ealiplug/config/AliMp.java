package cn.ezeyc.ealiplug.config;


import lombok.Getter;
import lombok.Setter;

/**
 * @author wz
 */
@Getter
@Setter
public class AliMp  {

    /**
     * 小程序appid
     */
    private String appId;
    /**
     * 小程序网关
     */
    private String gateWay;
    /**
     * 小程序公钥
     */
    private String publicKey;
    /**
     * 小程序私钥
     */
    private String PrivateKey;
    /**
     * 小程序⽀付宝UID\企业ID
     */
    private String companyId;
    /**
     * 付款回调地址
     */
    private  String payNotifyUrl;
    /**
     * 退款回调地址
     */
    private  String refundNotifyUrl;
    /**
     * 预授权冻结回调地址
     */
    private  String freezeNotify;
    /**
     * 预授权解冻回调地址
     */
    private  String unFreezeNotify;
    /**
     * 预授权转支付回调地址
     */
    private  String FreezePayNotify;
    /**
     * 小程序接口内容加密方式
     */
    private String aes;

    public  String format = "json";
    public  String charSet = "UTF-8";
    public   String singType = "RSA2";


    //证书方式
    private  String AppCertPath;

    private  String PublicCertPath;

    private  String RootCertPath;


}
