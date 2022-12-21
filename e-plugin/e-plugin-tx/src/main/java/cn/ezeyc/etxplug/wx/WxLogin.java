package cn.ezeyc.etxplug.wx;


import com.alibaba.fastjson2.JSON;
import cn.ezeyc.etxplug.config.wxMp;
import cn.ezeyc.etxplug.config.wxPub;
import org.noear.solon.cloud.utils.http.HttpUtils;


import java.io.IOException;
import java.util.Map;

/**
 * 微信相关内容登录
 */
public class WxLogin {

    /**
     * 获取小程序 用户openid
     * @param wxMp
     * @return
     */
    public  static Map<String,String> loginMp(wxMp wxMp) throws IOException {
        Map<String,String> map = null;
        if(wxMp !=null&& !"".equals(wxMp.getAppId()) &&wxMp.getAppId()!=null && !"".equals(wxMp.getAppSecret())&&wxMp.getAppSecret()!=null){
            String result = HttpUtils.http("https://api.weixin.qq.com/sns/jscode2session",
                    "appid=" + wxMp.getAppId() + "&secret=" + wxMp.getAppSecret() + "&js_code=" + wxMp.getCode() + "&grant_type=authorization_code").get();
            map = JSON.parseObject(result, Map.class);
            if (map.get("openid") == null) {
                throw new RuntimeException(map.get("errMsg"));
            }
        }else {
            throw new RuntimeException("获取微信小程序配置信息失败");
        }

        return  map;
    }
    /**
     * 获取微信公众号用户openid
     * @param wxPub
     * @return
     */
    public  static Map<String,String> loginPub(wxPub wxPub) throws IOException {
        Map<String,String> map = null;
        if(wxPub !=null&& !"".equals(wxPub.getAppId()) &&wxPub.getAppId()!=null && !"".equals(wxPub.getAppSecret()) &&wxPub.getAppSecret()!=null){
            String result = HttpUtils.http("https://api.weixin.qq.com/sns/jscode2session",
                    "appid=" + wxPub.getAppId() + "&secret=" + wxPub.getAppSecret() + "&js_code=" + wxPub.getCode() + "&grant_type=authorization_code").get();
            map = JSON.parseObject(result, Map.class);
            if (map.get("openid") == null) {
                throw new RuntimeException(map.get("errMsg"));
            }
        }else {
            throw new RuntimeException("获取微信公众号/服务号配置信息失败");
        }

        return  map;
    }
}
