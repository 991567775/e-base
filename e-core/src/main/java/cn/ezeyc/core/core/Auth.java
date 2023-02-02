package cn.ezeyc.core.core;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.solon.integration.SaTokenInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.ezeyc.core.config.Const;
import cn.ezeyc.core.enums.ResultEnum;
import cn.ezeyc.core.pojo.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 权限验证
 * @author wz
 */
@Slf4j
@Configuration
public class Auth {


    @Inject(value = "${ignore}",required = false)
    private  String  ignore;
    @Inject(value = "${sa-token.way}",required = false)
    private  Boolean  way=true;

    @Inject(value = "${sa-token.sso.enable}",required = false)
    private  Boolean  sso=false;
    /**
     * 注册 [sa-token全局过滤器]
     *
     * @return
     */
    @Bean
    public SaTokenInterceptor tokenPathInterceptor() {
        List<String> strings =new ArrayList<>();
        if(ignore!=null){
            strings = Stream.of(ignore.split(Const.dou)).collect(Collectors.toList());
            strings.add("/favicon.ico");
            strings.add("/sso/**");
            strings.add("/");
            strings.add("/_run/**");
            strings.add("/file/**");
            strings.add("/healthz");
        }
        return new SaTokenInterceptor()
                // 指定 [拦截路由] 与 [放行路由]
                .addInclude("/**")
                .setExcludeList(strings)
                // 认证函数: 每次请求执行
                .setAuth(req -> {
                    //网关隔离服务验证
                    if(way!=null&&way){
                        String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                        SaSameUtil.checkToken(token);
                    }
                    //启用sso时 权限资源提示登录
                    if(sso&&StpUtil.isLogin() == false) {
//                        String back = SaFoxUtil.joinParam(SaHolder.getRequest().getUrl(), Context.current().queryString());;
//                        SaHolder.getResponse().redirect("/sso/login?back=" + SaFoxUtil.encodeUrl(back));
                        SaRouter.back(ResultBody.failed(ResultEnum.noAuth));
                    }
                    //登录验证
                    SaRouter.match("/**", StpUtil::checkLogin);
                })

                // 异常处理函数：每次认证函数发生异常时执行此函数 //包括注解异常
                .setError(e -> {
                    return ResultBody.failed(ResultEnum.error).setMessage(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(req -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff");
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                      });

    }
}
