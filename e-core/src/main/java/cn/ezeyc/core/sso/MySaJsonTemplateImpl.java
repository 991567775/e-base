package cn.ezeyc.core.sso;

import cn.dev33.satoken.json.SaJsonTemplate;
import com.alibaba.fastjson2.JSON;

import java.util.Map;

/**
 * sa-token sso json处理器
 */
public class MySaJsonTemplateImpl implements SaJsonTemplate {
    @Override
    public String toJsonString(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public Map<String, Object> parseJsonToMap(String s) {
        return  JSON.parseObject(s,Map.class);
    }
}
