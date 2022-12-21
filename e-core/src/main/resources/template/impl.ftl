package ${package}.service.impl;
import com.baomidou.mybatisplus.solon.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.solon.plugins.pagination.Page;
import org.noear.solon.aspect.annotation.Service;
import cn.ezeyc.core.pojo.mybatis.ServiceBaseImpl;
import java.util.List;
import org.noear.solon.annotation.Inject;
import ${package}.model.${tableName};
import ${package}.mapper.${tableName}Mapper;
import ${package}.service.${tableName}Service;
import java.util.Arrays;
/**
* 描述：${remark} 服务实现层
* @author ${author}
* @date ${date}
*/
@Service
public class ${tableName}ServiceImpl extends ServiceBaseImpl<${tableName}Mapper, ${tableName}>  implements ${tableName}Service {
    @Inject
    private ${tableName}Mapper mapper;

    @Override
    public Page<${tableName}> list(${tableName} o) {
        return new LambdaQueryChainWrapper<>(mapper)
            .page(new Page<>(o.getPageNo(),o.getPageSize()));
    }

    @Override
    public List<${tableName}> listAll(${tableName} o) {
        return new LambdaQueryChainWrapper<>(mapper)
        .list();
    }

    @Override
    public ${tableName} getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public ${tableName} saveObj(${tableName} o) {
        if(this.saveOrUpdate(o)){
            return o;
        }
        return null;
    }

    @Override
    public Integer delById(String id) {
        if(id!=null){
            return mapper.deleteBatchIds(Arrays.asList(id.split(",")));
        }else  {
            return null;
        }
    }
}