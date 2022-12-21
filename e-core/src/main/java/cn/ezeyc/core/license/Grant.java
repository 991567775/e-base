package cn.ezeyc.core.license;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.ezeyc.core.config.Const;



/**
 * 获取授权以及验证
 * @author wz
 */
public class Grant {
    /**
     * 加密授权 获取授权码
     * @return 返回
     */
    public static  String grant(String ip){
        String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdmTgv/9ndbQji1b2qsYm6aU0SJyhFOk++txAlCSq6LAoyDX6D0NtwoV1Pv6UyoDiAY3oHkcM7P5nXmWTvie0YK2qd0+EDnyr9XZ0Y2itAqrixO94VnJ2faufnc51pfLJKVJYM8xj4D2+8tP1ZCEfMZTtDFtC5JO+Bm4qt0iO08wIDAQAB";
        //验证全部
        //return SaSecureUtil.rsaEncryptByPublic(publicKey,ONode.stringify(getServer()));
        //验证ip
        if(ip!=null&&!"".equals(ip)){
            return SaSecureUtil.rsaEncryptByPublic(publicKey, ip);

        }else{
            return SaSecureUtil.rsaEncryptByPublic(publicKey, getServer().getIpAddress().get(0));

        }

    }
    /**
     * 解密授权,对比ip,序列号。等
     */
    public static  boolean verity(String s){
        String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ2ZOC//2d1tCOLVvaqxibppTRInKEU6T763ECUJKrosCjINfoPQ23ChXU+/pTKgOIBjegeRwzs/mdeZZO+J7Rgrap3T4QOfKv1dnRjaK0CquLE73hWcnZ9q5+dznWl8skpUlgzzGPgPb7y0/VkIR8xlO0MW0Lkk74Gbiq3SI7TzAgMBAAECgYBED5qraBQfw2n2BJbfs8KmRbmqIE0M8Kzl0Utgoz/XnRNeIGPF65oJXzbcLQjb/0PHdq6JvFKTMNlwuedUZ72DaBVgmzg0lKpYnGcOgVyDv/xKPYJOdwNVKsw8Fu+P0r34AK+SEHoz5ZnXCV0LWEl/8W4SVrpPbuGmXW4RjoGW8QJBAMNQabM8xR6htsOECniDYtI4nBHLQwybDZj5BRNWEh5aDMxfjW6ZMyULkn6grTTMl9fL678dNlFM/j+AG4qBjqkCQQDOkNlyOp5jmh1zFwNJ3gSSX87i/j7T+c/8KRb4mWiWYVU3B24o6pGkMhFrEPxHNpHltGklGQV4epF6X81m0bQ7AkBVhShDKyDEwNpd0bsj+aOTmOX9hJrfMiVKDf+C7/UPBS8FbfFns/0JA4ApogE1VUBHZSrci9X001mgRF/Kzd/BAkAkNk54cQBNU7ov7+vaqjpA8tzQ/GNFQ/kLKqTlRfeIzV1j/UB5mc2YbFKTY3HmhhGDpGH+Z+mGmm4gduUKTl77AkAKDSitatz+5xj/5+sKY+2n8sE79ulcdLNfnuQobSmhr6erdtveEKO0LjC+wG6Hq1H/3usn5cEhnX2F18LXQzyi";

        ServerModel server = getServer();
       //验证ip
        String ip = SaSecureUtil.rsaDecryptByPrivate(privateKey, s);
        if(server.getIpAddress()!=null&&server.getIpAddress().contains(ip)){
           return true;
       }else {
            return false;
        }
       //验证全部
//        ServerModel deserialize = ONode.deserialize(SaSecureUtil.rsaDecryptByPrivate(privateKey, s), ServerModel.class);
//        if(deserialize==null){
//            return false;
//        }
//        if(deserialize.getCpuSerial().equals(server.getCpuSerial())&&
//                deserialize.getMainBoardSerial().equals(server.getMainBoardSerial())&&
//                deserialize.getMacAddress().containsAll(server.getMacAddress())&&
//                deserialize.getIpAddress().containsAll(server.getIpAddress())){
//            return true;
//        }else{
//            return false;
//        }
    }

    private static final String OS="os.name";
    /**
     * 获取授权码
     * @return 返回
     */
    private static  ServerModel getServer(){
        AbstractServerInfos abstractServerInfos;
        if(System.getProperty(OS).toLowerCase().startsWith(Const.win)){
            abstractServerInfos=new WindowsServerInfos();
        }else {
            abstractServerInfos = new LinuxServerInfos();
        }
        if(abstractServerInfos!=null){
            try {
                return abstractServerInfos.getServerInfos();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return  new ServerModel();
    }



}
