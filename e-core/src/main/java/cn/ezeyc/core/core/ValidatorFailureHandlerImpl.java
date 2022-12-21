package cn.ezeyc.core.core;

import cn.ezeyc.core.enums.ResultEnum;
import cn.ezeyc.core.pojo.ResultBody;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.ValidatorFailureHandler;

import java.lang.annotation.Annotation;

/**
 * 框架参数验证显示
 */
@Component
public class ValidatorFailureHandlerImpl implements ValidatorFailureHandler {

    @Override
    public boolean onFailure(Context ctx, Annotation anno, Result rst, String msg) throws Throwable {
        ctx.setHandled(true);

        if (rst.getCode() > 400 && rst.getCode() < 500) {
            ctx.status(rst.getCode());
        } else {
            ctx.status(400);
        }

        if (ctx.getRendered() == false) {
            if (Utils.isEmpty(msg)) {
                if (Utils.isEmpty(rst.getDescription())) {
                    msg = new StringBuilder(100)
                            .append("@")
                            .append(anno.annotationType().getSimpleName())
                            .append(" 参数验证失败")
                            .toString();
                } else {
                    msg = new StringBuilder(100)
                            .append("@")
                            .append(anno.annotationType().getSimpleName())
                            .append("参数验证失败: ")
                            .append(rst.getDescription())
                            .toString();
                }
            }
            ctx.render(ResultBody.failed(ResultEnum.verify).setMessage(msg));

        }

        return true;
    }
}