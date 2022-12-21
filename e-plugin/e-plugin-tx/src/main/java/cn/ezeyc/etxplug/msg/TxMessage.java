package cn.ezeyc.etxplug.msg;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import cn.ezeyc.etxplug.config.Msg;
import cn.ezeyc.etxplug.config.MsgApp;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import java.util.List;

/**
 * 腾讯短信
 * @author zewang
 * @date 2021.09.15
 */
@Component
public class TxMessage {

    @Inject("${tx.msg}")
    private Msg msg;

    private SmsClient client;
    private SendSmsRequest req = new SendSmsRequest();

    private MsgApp app;
    public void  init(){
        Credential cred = new Credential(msg.getSecretId(), msg.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
         client = new SmsClient(cred, "ap-nanjing",clientProfile);

    }

    public  void send(MsgTypeEnum type,String [] phones,String [] params) throws TencentCloudSDKException {
        List<MsgApp> sdkApp = msg.getSdkApp();
        if(sdkApp!=null&&sdkApp.size()>0){
            for(MsgApp a:sdkApp){
                if(type.getType()==a.getType()){
                    app=a;
                }
            }
            if(app==null){
                throw new RuntimeException("sdkApp中id配置错误");
            }
            //设置应用id
            req.setSmsSdkAppId(app.getSdkAppId());
            //设置签名
            req.setSignName(app.getSignName());
            //设置模版id
            req.setTemplateId(app.getTemplateId());
            //发送手机号
            req.setPhoneNumberSet(phones);
            //模版参数【按照顺序】
            req.setTemplateParamSet(params);
            SendSmsResponse res = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));
        }else {
            throw new RuntimeException("sdkApp未配置");
        }

    }
}
