package cn.ezeyc.core.codegenerator;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import cn.ezeyc.core.config.Const;
import cn.ezeyc.core.util.Util;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.ResultSet;

/**
 * CodeCreate
 * @author wz
 */
@Getter
@Setter
public class CodeCreate {
    /**
     * 组名
     */
    private String groupId;
    /**
     * 模块id
     */
    private String artifactId;
    /**
     * 模块名称
     */
    private String artifactName;

    /**
     * 包路径名称
     */
    private String author;
    /**
     * 数据库
     */
    private DataSource dataSource;
    /**
     * 数据库名称
     */
    private String tableName;
    /**
     * 生成路径
     */
    private String rootPath;

    /**
     * ui路径
     */
    private String rootUIPath;
    /**
     * page目录名称
     */
    private String pagePath;

    /**
     * 结果集
     */
    private ResultSet resultSet;
    /**
     * 业务说明
     */
    private  String remark;
    /**
     * 加载模板
     */
    private  String template;
    /**
     *所属分层
     */
    private  String packageType;

    public String getRootPath() {
        if(StringUtils.isNotEmpty(rootPath)){
            return  Const.outJavaWithRootPath(rootPath,artifactId.replace(".","/")) + Const.slanting+groupId.replace(".","/")+ Const.slanting+ Util.rmLine(artifactId.replace(".","/"))+ Const.slanting;
        }
        return Const.outJavaPath(artifactId.replace(".","/")) + Const.slanting+groupId.replace(".","/")+ Const.slanting+ Util.rmLine(artifactId.replace(".","/"))+ Const.slanting;
    }
    public String getPackagePath() {
        return groupId+ Const.dot+ Util.rmLine(artifactId);
    }


}
