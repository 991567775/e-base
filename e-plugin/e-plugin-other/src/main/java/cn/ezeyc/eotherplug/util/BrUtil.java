package cn.ezeyc.eotherplug.util;


import cn.ezeyc.eotherplug.config.BrApp;
import com.alibaba.fastjson2.JSONObject;
import cn.ezeyc.eotherplug.pojo.BrParam;
import org.noear.solon.cloud.utils.http.HttpUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * 百融
 */
public class BrUtil {

    public  static String brSearch(BrApp brApp, BrParam param){
        String appkey=brApp.getAppKey();
        if("md5".equals(brApp.getEncryType())){
            appkey=MD5Util.genMd5(brApp.getAppKey());
        }else {
            appkey= Sha256Util.getSHA256(brApp.getAppKey());
        }
        JSONObject reqData = new JSONObject();
        reqData.put("strategy_id", brApp.getStrategyId());
        reqData.put("id", MD5Util.genMd5(param.getCard()));
        reqData.put("cell", MD5Util.genMd5(param.getPhone()));
        reqData.put("name", param.getName());
        String checkCode = getCheckCode(brApp,reqData.toJSONString());

        try {
            String encryptJsonData= AESUtil.encrypt(URLEncoder.encode(reqData.toJSONString(), StandardCharsets.UTF_8.name()), brApp.getAppKey());
            JSONObject map=new JSONObject();
            map.put("appKey",appkey);
            map.put("apiCode",brApp.getApiCode());
            map.put("jsonData",encryptJsonData);
            map.put("checkCode",checkCode);
            System.out.println(map.toJSONString());
            String s =  HttpUtils.http(brApp.getHost() + brApp.getStrategyUrl()).data(map).post();
            return s;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private  static String getCheckCode(BrApp brApp,String data){
        String checkCode = "";
        if ("md5".equals(brApp.getEncryType())) {
            checkCode = MD5Util.genMd5(data + brApp.getApiCode() + brApp.getAppKey());
        } else {
            checkCode = Sha256Util.getSHA256(data + brApp.getApiCode() + brApp.getAppKey());
        }

        return checkCode;
    }
}
