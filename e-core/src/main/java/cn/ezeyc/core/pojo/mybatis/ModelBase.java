package cn.ezeyc.core.pojo.mybatis;



import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * BaseModel：有默认字段
 *
 * @author: Administrator
 * @date: 2020年11月23日, 0023 17:31:44
 */
@Getter
@Setter
public class ModelBase  implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    /**
     *创建人
     */
    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private Long createUser;
    /**
     *创建时间
     */
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    private  LocalDateTime createDate;
    /**
     *更新人
     */
    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     *更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.UPDATE)
    private  LocalDateTime updateDate;
    /**
     *是否删除
     */
    @TableField(value = "remove",fill = FieldFill.INSERT)
    private Boolean remove;
    /**
     *数据级别编码
     */
    @TableField(value = "data_code",typeHandler = StringArrayTypeHandler.class)
    private String [] dataCode;
    /**
     *分页每页数
     */
    @TableField(exist = false)
    private Integer pageSize=10;
    /**
     * 第几页
     */
    @TableField(exist = false)
    private Integer pageNo=1;
    @Override
    public String toString() {
        return "ModelBase{" +
                "id=" + id +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", remove=" + remove +
                ", dataCode=" + Arrays.toString(dataCode) +
                '}';
    }
}
