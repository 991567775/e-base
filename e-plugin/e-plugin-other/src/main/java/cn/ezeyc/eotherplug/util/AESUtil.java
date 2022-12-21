package cn.ezeyc.eotherplug.util;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 *    SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 *    -- 解密
 *    SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * @author 836508
 *
 */
public class AESUtil {
    private static final String ENCODE = System.getProperty("file.encoding");

    public static String encrypt(String content, String pwdKey) {
        if (!"".equals(content)&&content!=null && !"".equals(pwdKey)&& pwdKey!=null) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(pwdKey.getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                byte[] byteContent = content.getBytes(ENCODE);
                cipher.init(1, key);
                byte[] result = cipher.doFinal(byteContent);
                return Base64.encodeBase64URLSafeString(result);
            } catch (Exception var10) {
                throw new RuntimeException("AESException:对参数进行AES加解密过程中异常"+var10);
            }
        } else {
            throw new IllegalArgumentException("参数不合法");
        }
    }


}