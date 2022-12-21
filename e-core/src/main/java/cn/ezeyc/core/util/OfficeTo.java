package cn.ezeyc.core.util;

import cn.ezeyc.core.config.Const;
import cn.ezeyc.core.enums.OfficeType;
import com.aspose.cells.*;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;

import java.io.*;

/**
 * 使用aspose破解
 * @author by wz
 * @date 2022/12/5.
 */
public class OfficeTo {

    /**
     * 文档转换
     * @param file
     * @param type
     * @return
     */
    public static File DocTo (File file, OfficeType type) {
        String hz = file.getName().substring(file.getName().lastIndexOf(Const.dot)+1);
        File newFile = new File(file.getPath().replace(hz, type.getValue()));
        if(newFile.exists()){
            return  newFile;
        }
        FileOutputStream os=null;
        try {
            // Address是将要被转化的word文档
            Document doc = new Document(file.getPath());
            //转换文件
            os= new FileOutputStream(newFile);
            //判断
            if(OfficeType.PDF==type){
                doc.save(os, SaveFormat.PDF);
            }else {
                doc.save(os, SaveFormat.HTML);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return  newFile;
    }
    /**
     * 文档转换
     * @param in
     * @param type
     * @return
     */
    public static void DocTo (InputStream in, OfficeType type) {
        try {
            // Address是将要被转化的word文档
            Document doc = new Document(in);
            //转换文件
            //判断
            if(OfficeType.PDF==type){
                doc.save(Context.current().outputStream(), SaveFormat.PDF);
            }else {
                doc.save(Context.current().outputStream(), SaveFormat.HTML);
            }
            Context.current().flush();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 表格转换
     * @param file
     * @param type
     * @return
     */
    public static File xlxTo (File file, OfficeType type) {
        String hz = file.getName().substring(file.getName().lastIndexOf(Const.dot)+1);
        File newFile = new File(file.getPath().replace(hz, type.getValue()));
        if(newFile.exists()){
            return  newFile;
        }
        FileOutputStream os = null;
        try {
            // Address是将要被转化的word文档
            Workbook workbook = new Workbook(file.getPath());
            //转换文件
           os=new FileOutputStream(newFile);
            //判断
            if(OfficeType.PDF==type){
                PdfSaveOptions opts = new PdfSaveOptions();
                //所有列缩放到一个页面
                opts.setAllColumnsInOnePagePerSheet(true);
                workbook.save(os, opts);
            }else {
                HtmlSaveOptions opts = new HtmlSaveOptions();
                opts.setExportComments(true);
                workbook.save(os,opts);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return  newFile;
    }

    /**
     * 表格转换
     * @param in
     * @param type
     * @return
     */
    public static void xlxTo (InputStream in, OfficeType type) {
        try {
            // Address是将要被转化的word文档
            Workbook workbook = new Workbook(in);
            //转换文件
            //判断
            if(OfficeType.PDF==type){
                PdfSaveOptions opts = new PdfSaveOptions();
                //所有列缩放到一个页面
                opts.setAllColumnsInOnePagePerSheet(true);
                workbook.save(Context.current().outputStream(), opts);
            }else {
                HtmlSaveOptions opts = new HtmlSaveOptions();
                opts.setExportComments(true);
                workbook.save(Context.current().outputStream(),opts);
            }
            Context.current().flush();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
