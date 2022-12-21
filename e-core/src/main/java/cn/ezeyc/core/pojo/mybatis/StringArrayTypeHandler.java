package cn.ezeyc.core.pojo.mybatis;

import cn.ezeyc.core.config.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.*;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wz
 * 用以mysql中varchar格式的字段，进行转换的自定义转换器，转换为实体类的JSONArray属性
 */
@Slf4j
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({String[].class})
public class StringArrayTypeHandler extends BaseTypeHandler<String []> {



    /**
     * java转数据库类型
     * @param preparedStatement 参数
     * @param i 参数
     * @param objects 参数
     * @param jdbcType 参数
     * @throws SQLException 异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String[] objects, JdbcType jdbcType) throws SQLException {
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
    public String[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String v = resultSet.getString(s);
        if(v!=null&&!"".equals(v)){
            return  v.split(Const.dou);
        }
       return null;
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String v = resultSet.getString(i);
        if(v!=null){
            return  v.split(Const.dou);
        }
        return null;
    }

    @Override
    public String[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String v = callableStatement.getString(i);
        if(v!=null){
            return  v.split(Const.dou);
        }
        return null;
    }


}