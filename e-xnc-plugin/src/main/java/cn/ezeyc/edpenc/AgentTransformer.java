package cn.ezeyc.edpenc;
import cn.ezeyc.edpenc.util.Const;
import cn.ezeyc.edpenc.util.JarUtils;
import cn.ezeyc.edpenc.util.StrUtils;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
/**
 * AgentTransformer
 * jvm加载class时回调
 *
 * @author roseboy
 */
public class AgentTransformer implements ClassFileTransformer {

    /**
     * 密码
     */
    private final char[] pwd;
    /**
     * 构造方法
     *
     * @param pwd 密码
     */
    public AgentTransformer(char[] pwd) {
        this.pwd = pwd;
    }
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain domain, byte[] classBuffer) {

        if (className == null || domain == null || loader == null) {
            return null;
        }
        //获取类所在的项目运行路径
        String projectPath = domain.getCodeSource().getLocation().getPath();
        projectPath = JarUtils.getRootPath(projectPath);
        if (StrUtils.isEmpty(projectPath)) {
            return null;
        }
        className = className.replace("/", ".").replace("\\", ".");
        if(className.startsWith(Const.PACKAGES)){
            byte[] bytes = JarDecryptor.getInstance().doDecrypt(projectPath, className, this.pwd);
            //CAFEBABE,表示解密成功
            if (bytes != null && bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66) {
                System.out.println(className+"--------success");
                return bytes;
            }
        }
        return null;
    }


}
