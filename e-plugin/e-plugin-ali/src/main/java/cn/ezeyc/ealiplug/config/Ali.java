package cn.ezeyc.ealiplug.config;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信相关配置
 */
@Getter
@Setter
public class Ali   {
    /**
     * 小程序相关配置
     */
    public List<AliMp> mp =new ArrayList<>();

}
