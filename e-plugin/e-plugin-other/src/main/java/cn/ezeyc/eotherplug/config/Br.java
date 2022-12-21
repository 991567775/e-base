package cn.ezeyc.eotherplug.config;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信相关配置
 */
 @Setter
 @Getter
public class Br   {
    /**
     * 小程序相关配置
     */
    public List<BrApp> mp =new ArrayList<>();

}
