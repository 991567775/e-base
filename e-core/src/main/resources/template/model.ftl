package ${package}.model;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.ezeyc.core.pojo.mybatis.ModelBase;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;

/**
* 描述：${remark}
* @author ${author}
* @date ${date}
*/
@Getter
@Setter
@TableName(value ="${table_name}",autoResultMap = true)
public class ${tableName} extends ModelBase {

<#if column?exists>
    <#list column as model>

    <#if (model.columnType = 'varchar'||model.columnType = 'VARCHAR' || model.columnType = 'text'|| model.columnType = 'TEXT')>
    /**
    *${model.columnComment}
    */
    private String ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'decimal'||model.columnType = 'DECIMAL' >
    /**
    *${model.columnComment}
    */
    private BigDecimal ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'timestamp'||model.columnType = 'TIMESTAMP'|| model.columnType = 'datetime'||model.columnType = 'DATETIME' >
    /**
    *${model.columnComment}
     */
    private  LocalDateTime ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'date'||model.columnType = 'DATE'>
    /**
    *${model.columnComment}
    */
    private  LocalDate ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'time'||model.columnType = 'TIME'>
    /**
    *${model.columnComment}
    */
    private  LocalTime ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'bigint'||model.columnType = 'BIGINT' >
    /**
    *${model.columnComment}
    */
    private Long ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'int'||model.columnType = 'INT' >
    /**
    *${model.columnComment}
    */
    private Integer ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'smallint'||model.columnType = 'SMALLINT' >
    /**
    *${model.columnComment}
    */
    private Integer ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'tinyint'||model.columnType = 'TINYINT' >
    /**
    *${model.columnComment}
    */
    private Boolean ${model.changeColumnName};
    </#if>
    <#if model.columnType = 'bit'||model.columnType = 'BIT' >
    /**
    *${model.columnComment}
    */
    private Boolean ${model.changeColumnName};
    </#if>
    </#list>
</#if>

}
