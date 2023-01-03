package cn.ezeyc.core.config;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.sso.SaSsoManager;
import cn.ezeyc.core.config.mybatis.IdGenerator;
import cn.ezeyc.core.config.mybatis.Interceptor;
import cn.ezeyc.core.pojo.mybatis.StringArrayTypeHandler;
import cn.ezeyc.core.sso.MySaJsonTemplateImpl;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.solon.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.solon.plugins.inner.PaginationInnerInterceptor;
import com.ejlchina.okhttps.OkHttps;
import com.zaxxer.hikari.HikariDataSource;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.metrics.Metrics;
import io.jaegertracing.internal.metrics.NoopMetricsFactory;
import io.jaegertracing.internal.reporters.CompositeReporter;
import io.jaegertracing.internal.reporters.LoggingReporter;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Sender;
import io.jaegertracing.thrift.internal.senders.UdpSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.solon.annotation.Db;
import org.apache.thrift.transport.TTransportException;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.cache.jedis.RedisCacheService;
import org.noear.solon.cloud.CloudProps;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.vault.annotation.VaultInject;

import javax.sql.DataSource;
import java.net.URI;

/**
 * 数据源、redis缓存、mybatis plus 、链路追踪 、sa-token-redis 配置
 * @author wz
 */
@Slf4j
@Configuration
public class Config {

    @Inject(value = "${mybatis.db.showSql}",required = false)
    private boolean showSql=true;


    /**
     * 导入数据源
     * @param ds 数据源
     * @return 返回
     */
    @Bean(value = "db")
    public DataSource db(@VaultInject("${e.db}") HikariDataSource ds) {
        ds.setMaxLifetime(120000);
        return ds;
    }



    /**
     *  拦截器
     * @param cfg  配置
     * @param config 配置
     * @return 返回
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Db("db") MybatisConfiguration cfg,@Db("db") GlobalConfig config) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 自定义拦截器，先添加先执行。
        interceptor.addInnerInterceptor(new Interceptor(showSql));
        // 自带分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //自定义转换器
        cfg.getTypeHandlerRegistry().register(StringArrayTypeHandler.class.getPackage().getName());
        //枚举类型
         cfg.setDefaultEnumTypeHandler(null);
        //设置自定义id生成器
        config.setIdentifierGenerator(new IdGenerator());
        config.setBanner(false);
        return interceptor;
    }



    /**
     * 配置缓存
     * @param  cache 配置
     * @return 返回
     * typed 表示可类型注入 //即默认
     */
    @Bean(name = "cache", typed = true) //typed 表示可类型注入 //即默认
    public CacheService cache(@Inject("${e.cache}") RedisCacheService cache){
        return cache;
    }


    // 配置SSO相关参数
    @Bean
    public void configSso(@Inject("${sa-token.sso}") SaSsoConfig sso) {
        // 配置Http请求处理器
        sso.setSendHttp(url -> {
            System.out.println("发起请求：" + url);
            return OkHttps.sync(url).get().getBody().toString();
        });
        //必须设置
        SaSsoManager.setConfig(sso);
        //设置json转换器
        SaManager.setSaJsonTemplate(new MySaJsonTemplateImpl());
    }

    /**
     * 链路追踪
     * @return 返回
     * @throws TTransportException  异常
     */
//    @Bean
//    public JaegerTracer tracer() throws TTransportException {
//        CloudProps cloudProps = OpentracingProps.instance;

        //为了可自由配置，这行代码重要
        //为了可自由配置，这行代码重要
//        if(cloudProps.getTraceEnable() == false
//                || Utils.isEmpty(cloudProps.getServer())){
//            return null;
//        }
//
//        URI serverUri = URI.create(cloudProps.getServer());
//        Sender sender = new UdpSender(serverUri.getHost(), serverUri.getPort(), 0);
//
//        final CompositeReporter compositeReporter = new CompositeReporter(
//                new RemoteReporter.Builder().withSender(sender).build(),
//                new LoggingReporter()
//        );
//
//        final Metrics metrics = new Metrics(new NoopMetricsFactory());

//        return new JaegerTracer.Builder(Solon.cfg().appName())
//                .withReporter(compositeReporter)
//                .withMetrics(metrics)
//                .withExpandExceptionLogs()
//

//    }
}