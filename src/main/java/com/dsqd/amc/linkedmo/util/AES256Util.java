package com.dsqd.amc.linkedmo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Util {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 32;

    public static String encrypt(String value, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(padKey(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedValue, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(padKey(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return new String(decrypted);
    }

    private static byte[] padKey(String key) {
        byte[] keyBytes = key.getBytes();
        if (keyBytes.length > KEY_SIZE) {
            throw new IllegalArgumentException("Key length should be 32 bytes");
        }
        byte[] paddedKey = new byte[KEY_SIZE];
        System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
        return paddedKey;
    }
}
