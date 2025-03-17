package cn.edu.tust.beauty_back.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    /**
     * 获取字符串的MD5加密值（32位小写）
     * @param input 原始字符串
     * @return 加密后的MD5字符串
     */
    public static String getMd5String(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // 转为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 校验输入字符串与加密后的MD5是否匹配
     * @param rawInput 原始字符串
     * @param encrypted 加密后的MD5
     * @return 是否匹配
     */
    public static boolean verify(String rawInput, String encrypted) {
        return getMd5String(rawInput).equalsIgnoreCase(encrypted);
    }

}
