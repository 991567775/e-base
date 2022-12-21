package cn.ezeyc.core.config;

import java.io.File;

/**
 * 常量
 * @author wz
 */
public class Const {
    /**
     * 文件后缀dpf
     */
    public final static  String PDF="PDF";
    public final static  String pdf="pdf";
    /**
     * 文件后缀doc
     */
    public final static  String doc="doc";
    public final static  String DOC="DOC";
    public final static  String docx="docx";
    public final static  String DOCX="DOCX";
    /**
     * 文件后缀doc
     */
    public final static  String xls="xls";
    public final static  String XLS="XLS";
    public final static  String xlsx="xlsx";
    public final static  String XLSX="XLSX";
    /**
     * 作者
     */
    public final static  CharSequence AUTHOR="wz";
    /**
     * 逗号
     */
    public  static String dou=",";
    /**
     * 以Mapper结尾的类
     */
    public  static String end_with_Dao="Mapper";

    /**
     *以Service结尾的类
     */
    public  static  String end_with_service="Service";
    /**
     *以Service实现类结尾的类
     */
    public  static  String end_with_service_impl="ServiceImpl";
    /**
     *以Control结尾的类
     */
    public  static  String end_with_control="Control";
    /**
    * 系统*/
    public static String win="windows";

    /**
     * 点
     */
    public  static  String dot=".";
    /**
     *斜杠
     */
    public  static  CharSequence slanting="/";


    /**
     *model包
     */
    public  static  CharSequence package_model="model";
    /**
     *mapper包
     */
    public  static  CharSequence package_dao="mapper";
    /**
     *service
     */
    public  static  CharSequence package_service="service";

    /**
     *serviceimpl
     */
    public  static  CharSequence package_service_impl="service/impl";
    /**
     *service
     */
    public  static  CharSequence package_control="control";

    /**
     * java文件后缀
     */
    public  static  CharSequence suffix_java=".java";
    /**
     * 项目路径
     */
    public final  static CharSequence PROJECT_PATH=System.getProperty("user.dir")+ File.separator;
    /**
     * 判断系统
     */
    public static CharSequence windows="windows";
    /**
     * 附件上传类型 七牛
     */
    public static String qiniu="qiniu";
    /**
     * 附件上传类型 服务部署服务器
     */
    public static String local="local";

    public  static final String outJavaWithRootPath(String root,String modulePath){
        return  root+modulePath+"/src/main/java";
    }

    public  static final  String outJavaPath(String modulePath){
        return  PROJECT_PATH+modulePath+"/src/main/java";
    }

}
