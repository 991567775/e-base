package cn.ezeyc.core.plugin;

import cn.ezeyc.core.core.Init;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * 核心插件
 * @author wz
 */
@Slf4j
public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        Init.init();
    }
    @Override
    public void stop(){
    }
}