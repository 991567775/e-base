package cn.ezeyc.core.core;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;

/**
 * @author by wz
 * @date 2022/11/12.
 */
@Component
@Slf4j
public class ErrorListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable e) {
        //或者记录到日志系统，或者别的处理
        log.error(e.getMessage());
    }
}
