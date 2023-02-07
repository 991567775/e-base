package cn.ezeyc.core.config.mybatis;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * mybatis plus 自定义插入字段
 * @author wz
 */
public class ObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createDate", Timestamp.class,new Timestamp(new Date().getTime()));
        if(StpUtil.isLogin()){
            this.strictInsertFill(metaObject, "createUser", Long.class,Long.valueOf(StpUtil.getLoginId().toString()));
        }
        this.strictInsertFill(metaObject, "remove", Boolean.class, false);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateDate", Timestamp.class, new Timestamp(new Date().getTime()));
        if(StpUtil.isLogin()){
            this.strictUpdateFill(metaObject, "updateUser", Long.class, Long.valueOf(StpUtil.getLoginId().toString()));
        }
    }
}

