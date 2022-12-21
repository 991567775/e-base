package cn.ezeyc.core.pojo.mybatis;



import com.baomidou.mybatisplus.solon.plugins.pagination.Page;
import com.baomidou.mybatisplus.solon.service.IService;

import java.util.List;

/**
 * BaseDao：
 *
 * @author wz
 * @date  2020年11月23日, 0023 14:59:10
 */
public interface ServiceBase<T>  extends IService<T> {
    /**
     * 查询分页列表
     * @param o 实体
     * @return 返回
     */
    Page<T> list(T o);

    /**
     * 查询列表
     * @param o 实体
     * @return 返回
     */
    List<T> listAll(T o);

    /**
     * 查询单个
     * @param id 主键
     * @return 返回
     */
    T getById( Long id);


    /**
     * 根据id删除
     * @param id id
     * @return 返回
     */
    Integer delById( String id);

    /**
     * 保存
     * @param t 对象
     * @return 返回
     */
    T saveObj(T t);
}
