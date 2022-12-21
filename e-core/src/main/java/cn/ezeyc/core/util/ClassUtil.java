package cn.ezeyc.core.util;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 这段代码千万不要去写
 * 写了没意义
 * 这个我大体说一下原理
 * 原理是什么样的呢
 *
 *
 * @author wz
 *
 */
public class ClassUtil {
    public static byte[] readStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            return outSteam.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  static Field  getField(Class clazz,String name)  {
        Field  field = null;
        try {
            if(clazz!=Object.class&&clazz!=null){
                field = clazz.getDeclaredField(name);
            }
        } catch (NoSuchFieldException e) {
            if(field==null){
                field=getField(clazz.getSuperclass(),name);
            }
        }
        return  field;
    }
    /**
     * 获取全部字段（包括父类）
     * @param clazz
     * @return
     */
    public static Field[] getAllFields(Class clazz,Class<? extends java.lang.annotation.Annotation> ...an){
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            if(an.length>0){
                final Field[] declaredFields = clazz.getDeclaredFields();
                for(Field field:declaredFields){
                    for(Class c : an){
                        if (field.isAnnotationPresent(c)){
                            fieldList.add(field);
                        }
                    }

                }
            }
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
    /**
     * 获取全部字段（包括父类）
     * @param clazz
     * @return
     */
    public static Field[] getAllFields(Class clazz,Class<? extends java.lang.annotation.Annotation> an){
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
                final Field[] declaredFields = clazz.getDeclaredFields();
                for(Field field:declaredFields){
                        if (field.isAnnotationPresent(an)){
                            fieldList.add(field);
                        }
                }
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
    public static Field[] getAllFields(Class clazz){
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            final Field[] declaredFields = clazz.getDeclaredFields();
            for(Field field:declaredFields){
                    fieldList.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 取得某一类所在包的所有类名 不含迭代
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        // 将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        int packageLength = packagePathSplit.length;
        for (int i = 0; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }

}
