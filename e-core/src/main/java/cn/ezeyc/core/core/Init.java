package cn.ezeyc.core.core;

import cn.ezeyc.core.pojo.MyRuntimeException;
import cn.ezeyc.core.config.Config;
import cn.ezeyc.core.config.Const;
import cn.ezeyc.core.config.MailConfig;
import cn.ezeyc.core.config.UploadConfig;
import cn.ezeyc.core.util.AsposeRegister;
import cn.ezeyc.core.util.RedisUtil;
import com.xxl.job.core.executor.XxlJobExecutor;
import cn.ezeyc.core.annotation.JsonNotNull;
import cn.ezeyc.core.annotation.control;
import cn.ezeyc.core.license.Grant;
import cn.ezeyc.core.service.FileService;
import cn.ezeyc.core.sso.SsoClientControl;
import org.noear.solon.Solon;

import java.io.File;

/**
 * 插件初始化
 * @author wz
 */
public class Init {

    public static  void init(){
        String key = Solon.cfg().get("key");
        //验证授权
        if(!"991567775".equals(key)){
            if(key==null||"".equals(key)){
                throw new MyRuntimeException("无授权码");
            }
            if(!Grant.verity(key)){
                throw new MyRuntimeException("授权验证错误");
            }
        }
        //配置xxjob log默认路径
        XxlJobExecutor bean = Solon.context().getBean(XxlJobExecutor.class);
        if(bean!=null){
            bean.setLogPath(System.getProperty("user.dir")+ File.separator+"logs"+File.separator);
        }
        //redis工具
        Solon.context().beanMake(RedisUtil.class);
        Solon.context().getBean(RedisUtil.class).init();
        //aspose 注册
        AsposeRegister.registerAll();
        //添加自定义验证
        //ValidatorManager.setNoRepeatSubmitChecker(null);
        //全局异常处理
        Solon.context().beanMake(ErrorListener.class);
        //邮件服务
        Solon.context().beanMake(MailConfig.class);
        //附件上传配置文件
        Solon.context().beanMake(UploadConfig.class);
        //附件服务
        Solon.context().beanMake(FileService.class);
        //数据库、缓存等配置
        Solon.context().beanMake(Config.class);
        //token验证
        Solon.context().beanMake(Auth.class);
        //验证失败自定义
        Solon.context().beanMake(ValidatorFailureHandlerImpl.class);
        //统一请求拦截器
        Solon.context().beanMake(AppFilter.class);
        //sso请求配置
        Solon.context().beanMake(SsoClientControl.class);
        //注册自定义json参数验证
        Solon.context().beanAroundAdd(JsonNotNull.class, new VerifyInterceptor());
        //注册 @control 构建器
        Solon.context().beanBuilderAdd(control.class, (clz, bw, anno) -> {
            //内部实现
            new HandlerLoader(bw,bw.clz().getSimpleName().replace(Const.end_with_control,""),true).load(Solon.app());
        });
    }
}
