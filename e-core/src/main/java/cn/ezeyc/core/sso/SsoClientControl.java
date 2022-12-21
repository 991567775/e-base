package cn.ezeyc.core.sso;

import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoManager;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.ezeyc.core.enums.ResultEnum;
import cn.ezeyc.core.pojo.ResultBody;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.Context;

/**
 * Sa-Token-SSO Client端
 * @author kong
 *
 *
 */
@Controller
public class SsoClientControl {

    @Mapping("/sso/*")
    public ResultBody ssoRequest() {
        return ResultBody.success(SaSsoHandle.clientRequest());
    }
    // 当前是否登录
    @Mapping("/sso/isLogin")
    public ResultBody isLogin() {
        return ResultBody.success(StpUtil.isLogin());
    }

    // 返回SSO认证中心登录地址
    @Mapping("/sso/getSsoAuthUrl")
    public ResultBody getSsoAuthUrl( @Param("clientLoginUrl") String clientLoginUrl, @Param("backUrl") String backUrl) {
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(clientLoginUrl, backUrl);
       if(serverAuthUrl.startsWith("/sso/auth")){
           return ResultBody.failed(ResultEnum.ssoError);
       }else {
           return ResultBody.success(serverAuthUrl);
       }
    }

    // 根据ticket进行登录
    @Mapping("/sso/doLoginByTicket")
    public ResultBody doLoginByTicket(@Param("ticket") String ticket) {
        Object loginId = SaSsoHandle.checkTicket(ticket, "/sso/doLoginByTicket");
        if(loginId != null) {
            StpUtil.login(loginId);
            return ResultBody.success(StpUtil.getTokenValue());
        }
        return ResultBody.failed("无效ticket：" + ticket);
    }
    // 查询我的账号信息
    @Mapping("/sso/myInfo")
    public Object myInfo(Context ctx) {
        //组装url
        String url= SaSsoUtil.addSignParams(SaSsoManager.getConfig().splicingUserinfoUrl(), StpUtil.getLoginId());
        url+="&token="+ctx.header("token");
        return SaSsoManager.getConfig().getSendHttp().apply(url);
    }



 }
