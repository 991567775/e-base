package cn.ezeyc.core.config;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

/**
 * 附件路径配置
 * @author wz
 */
@Component
public class UploadConfig {
    @Inject(value = "${uploadPath}",required = false)
    private String uploadPath="/root/base/file/";
    @Inject(value = "${uploadWinPath}",required = false)
    private String uploadWinPath="C:\\data\\upload\\";


    public String getUploadPath() {
        //获得系统属性集
        String props = System.getProperties().getProperty("os.name");
        if(props.toLowerCase().contains(Const.windows)){
            return uploadWinPath;
        }else {
            return   uploadPath;
        }
    }
}
