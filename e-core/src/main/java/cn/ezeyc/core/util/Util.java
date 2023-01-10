package cn.ezeyc.core.util;

import cn.ezeyc.core.config.Const;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具包
 * @author wz
 */
public class Util {

    /**
     * 去除横杠
     * @param str 参数
     * @return 返回
     */
    public static String  rmLine(String str){
        return  str.replaceAll("-","");
    }

    /**
     * 首字母转小写
     * @param s 参数
     * @return 返回
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     *  下划线转驼峰
     * @param str 参数
     * @return 返回
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,效率比上面高
     * @param str 参数
     * @return 返回
     */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    /**
     * 首字母转大写
     * @param s 参数
     * @return 返回
     */
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    /**
     * 获取文件类型后缀
     * @param file 参数
     * @return 返回
     */
    public static String getFileType(String file) {
        if(file.contains(Const.dot)){
            return file.substring(file.indexOf(Const.dot),file.length());
        }else{
            return "";
        }

    }

}
