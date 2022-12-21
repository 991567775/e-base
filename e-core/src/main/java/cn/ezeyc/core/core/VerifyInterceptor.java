package cn.ezeyc.core.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cn.ezeyc.core.annotation.JsonNotNull;
import cn.ezeyc.core.annotation.verify;
import cn.ezeyc.core.enums.ResultEnum;
import cn.ezeyc.core.pojo.ResultBody;
import cn.ezeyc.core.pojo.mybatis.ModelBase;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;

/**
 * 自定义json参数验证
 */
public class VerifyInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        JsonNotNull annotation = inv.method().getAnnotation(JsonNotNull.class);
        Object[] args = inv.args();
        //验证不为空
        if(annotation.value()!=null&&annotation.value().length>0){
            String[] value = annotation.value();
            String [] val=new String[value.length];
            for(int x=0;x<value.length;x++){
                if(args!=null&&args.length>0 ){
                    for(Object o:args){
                        //实体参数json验证  （其他参数上添加NotNull）
                        if(o!=null&&(o instanceof ModelBase||o.getClass().getAnnotation(verify.class)!=null)){
                            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
                            if(jsonObject.get(value[x])!=null&&!"".equals(jsonObject.get(value[x]))){
                                val[x]=value[x];
                            }
                        }else{
                        }
                    }
                }
            }
            for (int x=0;x<val.length;x++){
                if(val[x]==null){
                    return ResultBody.failed(ResultEnum.verify).setMessage("参数"+value[x]+"验证失败");
                }
            }
        }
        //其他验证 todo

        return inv.invoke();
    }
}
