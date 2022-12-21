package cn.ezeyc.etxplug.config;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * @author wz
 */
@Getter
@Setter
public class Msg  {

    private String secretId;

    private String secretKey;


    private  List<MsgApp> sdkApp;


}
