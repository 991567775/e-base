package cn.ezeyc.core.util;

import cn.ezeyc.core.config.UploadConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.handle.Context;
import java.io.*;
import java.util.Map;

/**
 * 根据模板导出文档
 */
@Slf4j
public class CommonExport {

    /**
     *
     * @param map
     * @param c
     * @param name
     * @param ftl
     */
    public static void export(UploadConfig config,Map map, Class c, String name, String ftl) {

        Template template = null;
        Configuration  configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        // 输入文档路径及名称
        // 从本地路径读取存放ftl的文件夹
        try {
            configuration.setClassForTemplateLoading(c, "/");
            // 被读取的文件
            template = configuration.getTemplate(ftl);
            template.setEncoding("utf-8");
            File outFile=new File(config.getUploadPath()+name);
            // 输出文档路径及名称
            Writer out = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
            template.process(map, out);
            out.close();
            Context.current().outputAsFile(outFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
