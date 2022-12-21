package cn.ezeyc.etxplug.config;


import lombok.Getter;
import lombok.Setter;

/**
 * @author zewang
 */
@Getter
@Setter
public  class MsgApp  {
    private  int type;
    private  String sdkAppId;

    private  String signName;

    private  String templateId;

}
