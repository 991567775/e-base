package cn.ezeyc.core.config.mybatis;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import java.time.LocalDateTime;
/**
 * mybatis plus 自定义插入字段
 * @author wz
 */
public class ObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createDate", LocalDateTime.class, LocalDateTime.now());
        if(StpUtil.isLogin()){
            this.strictInsertFill(metaObject, "createUser", Long.class,Long.valueOf(StpUtil.getLoginId().toString()));
        }
        this.strictInsertFill(metaObject, "remove", Boolean.class, false);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateDate", LocalDateTime.class, LocalDateTime.now());
        if(StpUtil.isLogin()){
            this.strictUpdateFill(metaObject, "updateUser", Long.class, Long.valueOf(StpUtil.getLoginId().toString()));
        }
    }
}

