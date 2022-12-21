package cn.ezeyc.core.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @author wz
 */
public class IdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        //返回生成的id值即可.
        return SnowflakeIdGenerator.nextId();
    }

}

