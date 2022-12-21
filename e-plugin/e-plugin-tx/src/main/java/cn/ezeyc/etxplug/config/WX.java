package cn.ezeyc.etxplug.config;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信相关配置
 */
@Getter
@Setter
public class WX       {
    /**
     * 小程序相关配置
     */
    public List<wxMp> mp =new ArrayList<>();
    /**
     * 公众号服务号相关配置
     */
    public List<wxPub> pub =new ArrayList<>();

}
