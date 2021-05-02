package com.hs.live.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * RSA加密加密类
 *
 * @author zhangqiandong
 */
public class RsaEncryptUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(RsaEncryptUtil.class);

    /**
     * 解密
     *
     * @param cipherText 密文（Base64）
     * @param key        密钥
     * @return 明文 
     * @throws NoSuchPaddingException    获取加密实例出错
     * @throws NoSuchAlgorithmException  获取加密实例出错
     * @throws InvalidKeyException       初始化密钥出错
     * @throws BadPaddingException       加密出错
     * @throws IllegalBlockSizeException 加密出错
     */
    public static String decrypt(String cipherText, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipherText = cipherText.trim();
        byte[] encryptedData = Base64.getDecoder().decode(cipherText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] dBytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            LOGGER.error("公钥解密Out关闭出错：", e);
        }

        return new String(dBytes, StandardCharsets.UTF_8);
    }

    /**
     * 加密
     *
     * @param planText 明文
     * @param key      密钥
     * @return 密文
     * @throws NoSuchPaddingException    获取加密实例出错
     * @throws NoSuchAlgorithmException  获取加密实例出错
     * @throws InvalidKeyException       初始化密钥出错
     * @throws BadPaddingException       加密出错
     * @throws IllegalBlockSizeException 加密出错
     */
    public static String encrypt(String planText, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] data = planText.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }
        byte[] encrypted = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            LOGGER.error("私钥加密Out关闭出错：", e);
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }
}
