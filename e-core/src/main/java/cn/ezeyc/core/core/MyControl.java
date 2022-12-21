package cn.ezeyc.core.core;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Render;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.web.cors.annotation.CrossOrigin;

/**
 * 统一返回处理
 * @author wz
 * 这个注解可继承，用于支持子类的验证
 */
@Valid
@CrossOrigin(origins = "*")
public class MyControl implements Render {
    @Override
    public void render(Object obj, Context ctx)  {
        if (obj == null) {
            return;
        }
        //返回类型为字符串
        if (obj instanceof String) {
            ctx.output((String) obj);
        } else {
            ctx.outputAsJson(ONode.stringify(obj));
        }
    }
}
