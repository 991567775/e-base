package cn.ezeyc.edpenc.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author wz
 */
public class JarUtils {
    /**
     * 打包时需要删除的文件
     */
    public static final String[] DLE_FILES = {".DS_Store", "Thumbs.db"};

    /**
     * 把目录压缩成jar
     *
     * @param jarDir    需要打包的目录
     * @param targetJar 打包出的jar/war文件路径
     */
    public static void doJar(String jarDir, String targetJar) {
        File jarDirFile = new File(jarDir);
        //枚举jarDir下的所有文件以及目录
        List<File> files = new ArrayList<>();
        IoUtils.listFile(files, jarDirFile);

        ZipOutputStream zos = null;
        OutputStream out = null;

        try {
            File jar = new File(targetJar);
            if (jar.exists()) {
                boolean delete = jar.delete();
                System.out.println("删除"+delete);
            }

            out = new FileOutputStream(jar);
            zos = new ZipOutputStream(out);

            for (File file : files) {
                if (isDel(file)) {
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(jarDirFile.getAbsolutePath().length() + 1);
                fileName = fileName.replace(File.separator, "/");
                //目录，添加一个目录entry
                if (file.isDirectory()) {
                    ZipEntry ze = new ZipEntry(fileName + "/");
                    ze.setTime(System.currentTimeMillis());
                    zos.putNextEntry(ze);
                    zos.closeEntry();
                }
                //jar文件，需要写crc信息
                else if (fileName.endsWith(".jar")) {
                    byte[] bytes = IoUtils.readFileToByte(file);
                    if(bytes!=null){
                        ZipEntry ze = new ZipEntry(fileName);
                        ze.setMethod(ZipEntry.STORED);
                        ze.setSize(bytes.length);
                        ze.setTime(System.currentTimeMillis());
                        ze.setCrc(IoUtils.crc32(bytes));
                        zos.putNextEntry(ze);
                        zos.write(bytes);
                        zos.closeEntry();
                    }

                }
                //其他文件直接写入
                else {
                    ZipEntry ze = new ZipEntry(fileName);
                    ze.setTime(System.currentTimeMillis());
                    zos.putNextEntry(ze);
                    byte[] bytes = IoUtils.readFileToByte(file);
                    if(bytes!=null){
                        zos.write(bytes);
                    }
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(zos, out);
        }
    }


    /**
     * 释放jar内以及子jar的所有文件
     *
     * @param jarPath   jar文件
     * @param targetDir 释放文件夹
     * @return 所有文件的完整路径，包含目录
     */
    public static List<String> unJar(String jarPath, String targetDir) {
        return unJar(jarPath, targetDir, null);
    }

    /**
     * 释放jar内以及子jar的所有文件
     *
     * @param jarPath   jar文件
     * @param targetDir 释放文件夹
     * @return 所有文件的完整路径，包含目录
     */
    public static List<String> unJar(String jarPath, String targetDir, List<String> includeFiles) {
        List<String> list = new ArrayList<>();
        File target = new File(targetDir);
        if (!target.exists()) {
             target.mkdirs();

        }

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(new File(jarPath));
            ZipEntry entry;
            File targetFile;
            //先把文件夹创建出来
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    targetFile = new File(target, entry.getName());
                    if(!targetFile.exists()){
                         targetFile.mkdirs();
                    }
                } else {//有时候entries没有目录,根据文件路径创建目录
                    int lastSeparatorIndex = entry.getName().lastIndexOf("/");
                    if (lastSeparatorIndex > 0) {
                        targetFile = new File(target, entry.getName().substring(0, lastSeparatorIndex));
                        if (!targetFile.exists()) {
                             targetFile.mkdirs();

                        }
                    }
                }
            }

            //再释放文件
            entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                targetFile = new File(target, entry.getName());

                //跳过未包含的文件
                if (includeFiles != null && includeFiles.size() > 0 && !includeFiles.contains(targetFile.getName())) {
                    continue;
                }
                byte[] bytes = IoUtils.toBytes(zipFile.getInputStream(entry));
                IoUtils.writeFile(targetFile, bytes);
                list.add(targetFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(zipFile, null);
        }
        return list;
    }



    /**
     * 在压缩文件中获取一个文件的字节
     *
     * @param zip      压缩文件
     * @param fileName 文件名
     * @return 文件的字节
     */
    public static byte[] getFileFromJar(File zip, String fileName) {
        ZipFile zipFile = null;
        try {
            if (!zip.exists()) {
                return null;
            }
            zipFile = new ZipFile(zip);
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            if (zipEntry == null) {
                return null;
            }
            InputStream is = zipFile.getInputStream(zipEntry);
            return IoUtils.toBytes(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(zipFile);
        }
        return null;
    }

    /**
     * 是否删除这个文件
     *
     * @param file 文件
     * @return 是否需要删除
     */
    public static boolean isDel(File file) {
        for (String f : DLE_FILES) {
            if (file.getAbsolutePath().endsWith(f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取class运行的classes目录或所在的jar包目录
     */

    private static final  String JAR="jar:";
    private static final  String WAR="war:";
    private static final  String FILE="file:";
    private static final  String XIN="*";
    private static final  String WEB_INF="WEB-INF";
    private static final  String GT="!";
    public static String getRootPath(String path) {
        if (path == null) {
            URL resource = JarUtils.class.getResource("");
            if(resource!=null){
                path=resource.getPath();
            }
        }

        if(path!=null){
            try {
                path = java.net.URLDecoder.decode(path, String.valueOf(StandardCharsets.UTF_8));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            if (path.startsWith(JAR) || path.startsWith(WAR)) {
                path = path.substring(4);
            }
            if (path.startsWith(FILE)) {
                path = path.substring(5);
            }
            //没解压的war包
            if (path.contains(XIN)) {
                return path.substring(0, path.indexOf(XIN));
            }
            //war包解压后的WEB-INF
            else if (path.contains(WEB_INF)) {
                return path.substring(0, path.indexOf(WEB_INF));
            }
            //jar
            else if (path.contains(GT)) {
                return path.substring(0, path.indexOf(GT));
            }
            //普通jar/war
            else if (path.endsWith(Const.JAR) ) {
                return path;
            }
            //no
            else if (path.contains(Const.CLASS_G)) {
                return path.substring(0, path.indexOf(Const.CLASS_G) + 9);
            }
        }



        return null;
    }
}
