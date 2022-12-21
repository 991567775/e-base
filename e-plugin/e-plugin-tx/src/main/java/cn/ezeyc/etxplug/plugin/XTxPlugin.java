package cn.ezeyc.etxplug.plugin;

import cn.ezeyc.etxplug.msg.TxMessage;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
@Slf4j
public class XTxPlugin implements Plugin {
    @Override
    public void start(AopContext context) {
        //短信
        Solon.context().beanMake(TxMessage.class);
    }
}
