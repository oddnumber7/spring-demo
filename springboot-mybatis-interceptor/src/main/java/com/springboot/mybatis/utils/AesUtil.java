package com.springboot.mybatis.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName AesUtil
 * @Create 2023/4/11 17:59
 * @Description AES工具类
 */
@Component
public class AesUtil {

    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param key  密钥
     * @param data 待加密数据
     * @return Base64编码的加密结果
     * @throws Exception 异常
     */
    public String encrypt(String key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     *
     * @param key    密钥
     * @param base64 Base64编码的待解密数据
     * @return 解密后的数据
     * @throws Exception 异常
     */
    public String decrypt(String key, String base64) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytes = Base64.getDecoder().decode(base64);
        byte[] decryptedBytes = cipher.doFinal(bytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}