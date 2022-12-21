package ${package}.control;
import cn.ezeyc.core.annotation.control;
import cn.ezeyc.core.core.MyControl;
import cn.ezeyc.core.pojo.ResultBody;
import org.noear.solon.annotation.Inject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Get;
import ${package}.model.${tableName};
import ${package}.service.${tableName}Service;
import com.baomidou.mybatisplus.solon.plugins.pagination.Page;
import java.util.List;
/**
* ${remark}
* @author ${author}
* @date ${date}
*/
@Slf4j
@control
public class ${tableName}Control extends  MyControl{
    @Inject
    private ${tableName}Service service;
    /**
    * 分页查询
    * @args pageNo 可选   第几页
    * @args pageSize 可选   每页数
    */
    public ResultBody<Page<${tableName}>> list(${tableName} a){
        try {
            return ResultBody.success(service.list(a));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.failed(e.getMessage());
        }
    }
    /**
    * 查询全部
    */
    public ResultBody<List<${tableName}>> listAll(${tableName}  a){
        try {
            return ResultBody.success(service.listAll(a));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.failed(e.getMessage());
        }

    }
    /**
    * 根据id查询对象
    * @args id 必选 主键
    */
    public  ResultBody<${tableName}> getById( Long id){
        try {
            return    ResultBody.success(service.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.failed(e.getMessage());
        }
    }
    /**
    * 删除
    * @args id 必选   主键
    */
    @Get
    public ResultBody<Integer> del( String id){
        try {
            return  ResultBody.success(service.delById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.failed(e.getMessage());
        }

    }
    /**
    * 保存
<#if column?exists>
    <#list column as model>
    * @args ${model.changeColumnName} 可选   ${model.columnComment}
    </#list>
</#if>
    */
    public  ResultBody<${tableName}> save(${tableName} entity){
        try {
            return ResultBody.success(service.saveObj(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.failed(e.getMessage());
        }
    }
}