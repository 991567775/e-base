package cn.ezeyc.core.service;

import cn.ezeyc.core.config.Const;
import cn.ezeyc.core.config.UploadConfig;
import cn.ezeyc.core.enums.OfficeType;
import cn.ezeyc.core.util.ClassUtil;
import cn.ezeyc.core.util.OfficeTo;
import cn.ezeyc.core.util.Util;
import cn.ezeyc.core.util.ToHtml;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Media;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.handle.UploadedFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 附件服务
 * @author wz
 */
@Service
public class FileService {

    @Inject
    private UploadConfig config;

    @Inject(value = "${uploadType}",required = false)
    private String type= Const.qiniu;

    public String upload(UploadedFile file) throws IOException {
        if(type.equals(Const.qiniu)){
            String date= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

            String name =date+ Const.slanting+ CloudClient.id().generate();
            //后缀
            if(file.name.contains(Const.dot)){
                name+=Util.getFileType(file.name);
            }
            Result put = CloudClient.file().put(name, new Media(file.content));
            // 把它转为本地文件
            return name;
        } else if (type.equals(Const.local)) {
            //设置日期目录
            String date= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String name = CloudClient.id().generate()+Util.getFileType(file.name);
            File newFile = new File(config.getUploadPath() + date);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            // 把它转为本地文件
            file.transferTo(new File(newFile.getPath()+ Const.slanting +name));
            return date+ Const.slanting +name;
        }
        return  "上传类型未确定";
    }

    public void down(Context ctx, String path) {
        if(type.equals(Const.qiniu)){
            byte[] bytes = CloudClient.file().get(path).bodyAsByts();
            try {
                ctx.outputStream().write(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if (type.equals(Const.local)){
            File file = new File(config.getUploadPath(), path);
            try {
                ctx.outputAsFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Get
    public void view(Context ctx, String path, String officeType) {
        InputStream fis=null;
        try {
            if(path.endsWith(Const.PDF)||path.endsWith(Const.pdf)){
                if(type.equals(Const.qiniu)){
                    fis=CloudClient.file().get(path).body();
                }else if (type.equals(Const.local)){
                    fis= new FileInputStream(new File(config.getUploadPath(), path));
                }
                ctx.contentType("application/pdf");
                ctx.output(ClassUtil.readStream(fis));
            }else if(path.endsWith(Const.doc)||path.endsWith(Const.DOC)
                    ||path.endsWith(Const.docx)||path.endsWith(Const.DOCX)
                    ||path.endsWith(Const.xls)||path.endsWith(Const.XLS)
                    ||path.endsWith(Const.xlsx)||path.endsWith(Const.XLSX)){
                if(officeType!=null&&!"".equals(officeType)){
                    ctx.contentType("application/"+officeType);
                }else{
                    ctx.contentType("application/pdf");
                }
                File newFile=null;
                if(path.endsWith(Const.doc)||path.endsWith(Const.DOC)
                        ||path.endsWith(Const.docx)||path.endsWith(Const.DOCX)){
                    if(type.equals(Const.qiniu)){
                        fis=CloudClient.file().get(path).body();
                    }else if (type.equals(Const.local)){
                        fis=new FileInputStream(new File(config.getUploadPath(), path));
                    }
                    if(officeType!=null&&"html".equals(officeType)){
                        OfficeTo.DocTo(fis,OfficeType.HTML);
                        ctx.flush();
                    }else{
                        OfficeTo.DocTo(fis, OfficeType.PDF);
                        ctx.flush();
                    }
                }else{
                    if(type.equals(Const.qiniu)){
                        fis=CloudClient.file().get(path).body();
                    }else if (type.equals(Const.local)){
                        fis=new FileInputStream(new File(config.getUploadPath(), path));
                    }
                    if(officeType!=null&&"html".equals(officeType)){
                        OfficeTo.xlxTo(fis,OfficeType.HTML);
                    }else{
                        OfficeTo.xlxTo(fis, OfficeType.PDF);
                    }
                }
            }else{
                if(type.equals(Const.qiniu)){
                    fis=CloudClient.file().get(path).body();
                }else if (type.equals(Const.local)){
                    fis=new FileInputStream(new File(config.getUploadPath(), path));
                }
                ctx.output(ClassUtil.readStream(fis));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void del(String path) {
        if(type.equals(Const.qiniu)){
            CloudClient.file().delete(path);
        }else if(type.equals(Const.local)){
            File file=new File(config.getUploadPath()+path);
            if(file.exists()){
                file.delete();
            }
        }
    }

    public void del(String[] path) {
        for(String s: path){
            if(type.equals(Const.qiniu)){
                CloudClient.file().delete(s);
            }else if(type.equals(Const.local)){
                File file=new File(config.getUploadPath()+s);
                if(file.exists()){
                    file.delete();
                }
            }
        }

    }
}
