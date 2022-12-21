package cn.ezeyc.core.pojo.mybatis;

import cn.ezeyc.core.config.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wz
 * 用以mysql中varchar格式的字段，进行转换的自定义转换器，转换为实体类的JSONArray属性
 */
@Slf4j
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({Boolean[].class})
public class BooleanArrayTypeHandler extends BaseTypeHandler<Boolean []> {



    /**
     * java转数据库类型
     * @param preparedStatement  参数
     * @param i 参数
     * @param objects 参数
     * @param jdbcType 参数
     * @throws SQLException 异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean[] objects, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(Const.dou)));
    }

    /**
     * 数据库转实体
     * @param resultSet 参数
     * @param s 参数
     * @return 返回
     * @throws SQLException 异常
     */
    @Override
    public Boolean[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String v = resultSet.getString(s);
        if(v!=null){
            Boolean[] result = new Boolean[v.length()];
            for (int x=0;x<v.split(Const.dou).length;x++){
                result[x]=Boolean.valueOf(v.split(Const.dou)[x]);
            }
            return result;
        }
        return null;
    }

    @Override
    public Boolean[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String v = resultSet.getString(i);
        if(v!=null&&!"".equals(v)){
            Boolean[] result = new Boolean[v.length()];
            for (int x=0;x<v.split(Const.dou).length;x++){
                result[x]=Boolean.valueOf(v.split(Const.dou)[x]);
            }
            return result;
        }
        return null;
    }

    @Override
    public Boolean[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String v = callableStatement.getString(i);
        if(v!=null){
            Boolean[] result = new Boolean[v.length()];
            for (int x=0;x<v.split(Const.dou).length;x++){
                result[x]=Boolean.valueOf(v.split(Const.dou)[x]);
            }
            return result;
        }
        return null;
    }


}