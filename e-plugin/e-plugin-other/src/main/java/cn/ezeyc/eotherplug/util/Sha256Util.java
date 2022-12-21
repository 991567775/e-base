package cn.ezeyc.eotherplug.util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Sha256Util {
    public static String getSHA256(String str)  {
        String encodeStr = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
            return encodeStr;
        } catch (Exception var4) {
            throw new RuntimeException("SHAException:SHA加密过程中出现异常"+ var4);
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;

        for(int i = 0; i < bytes.length; ++i) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                stringBuilder.append("0");
            }

            stringBuilder.append(temp);
        }

        return stringBuilder.toString();
    }
}
