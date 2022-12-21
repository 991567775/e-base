package cn.ezeyc.core.core;

import cn.ezeyc.core.enums.ResultEnum;
import cn.ezeyc.core.pojo.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * 过滤器
 * 过滤器，全局请求的管控（Filter）
 * 全局的请求异常处理
 * 性能记时
 * 响应状态调整
 * 上下文日志记录
 * 链路跟踪等.
 * @author wz
 */
@Slf4j
@Component
public class AppFilter implements Filter {
     String breakerName = "global";
    private  static final  String RUN="/_run";
    private  static final  String GEN="/";

    private  static final  String HEALTH="/healthz";
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        //1.开始计时（用于计算响应时长）
        long start = System.currentTimeMillis();
        try {
            //熔断器
//            if (CloudClient.breaker() == null) {
//                chain.doFilter(ctx);
//            } else {
//                try (AutoCloseable entry = CloudClient.breaker().entry(breakerName)) {
//                    chain.doFilter(ctx);
//                } catch (BreakerException ex) {
//                    ctx.outputAsJson(ResultBody.failed(ResultEnum.notfound).toString());
////                    throw UapiCodes.CODE_4001017;
//                }
//            }
            //监控服务的路径请求性能（后端实现的时候，可以进一步记录超5秒、超1秒的次数；以及典线图）
//            CloudClient.metric().addMeter("path", ctx.path(), 0);
//            //监控服务的路径请求出错次数
//            CloudClient.metric().addCount("path_err", ctx.path(), 1);
//            //监控服务的运行时状态
//            CloudClient.metric().addGauge("service", "runtime", 2);


            //2.执行
            chain.doFilter(ctx);
            //无对应请求时
            //3.未处理设为404状态
            if(! ctx.getHandled()){
                ctx.status(404);
                ctx.setHandled(true);
                ctx.outputAsJson(ResultBody.failed(ResultEnum.notfound).toString());
            }
        } catch (Exception e) {
            //4.异常捕促与控制（并标为500错误）
            ctx.status(500);
            ctx.setHandled(true);
            log.error(e.getMessage());
            ctx.outputAsJson(ResultBody.failed().setCode(500).setMessage(e.getMessage()).toString());
        }
        //5.获得接口响应时长
        long times = System.currentTimeMillis() - start;
        //排除could心跳之类请求
       if(!ctx.pathNew().startsWith(RUN)&&!GEN.equals(ctx.pathNew())&&!HEALTH.equals(ctx.pathNew())) {
           //分布式日志标签
           TagsMDC.tag0("请求");
           TagsMDC.tag1(ctx.pathNew());
           log.info("path:" + ctx.path() + ";  realPath:" + ctx.pathNew() + ";    time:" + times + " ms");
       }

    }

}
