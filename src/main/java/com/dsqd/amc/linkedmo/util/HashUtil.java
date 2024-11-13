package com.dsqd.amc.linkedmo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtil {
	
    private static final Logger logger = LoggerFactory.getLogger(HashUtil.class);
   
	public static String SHA1toHex(String input) {
		try {
            return SHA1encodeToHex(input);
        } catch (NoSuchAlgorithmException e) {
        	logger.error("SHA1 ERROR : {}", e.getMessage());
           return input;
        }
	}
	
	public static String MD5toHex(String input) {
		try {
            return MD5encodeToHex(input);
        } catch (NoSuchAlgorithmException e) {
        	logger.error("SHA1 ERROR : {}", e.getMessage());
           return input;
        }
	}
	
    private static String SHA1encodeToHex(String input) throws NoSuchAlgorithmException {
        // SHA-1 메시지 다이제스트 객체 생성
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        // 입력 문자열을 바이트 배열로 변환하고 해시 계산
        byte[] hash = md.digest(input.getBytes());

        // 해시 값을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // 각 바이트를 16진수로 변환
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0'); // 한 자리 16진수 값을 두 자리로 채우기 위해 '0' 추가
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
    
    private static String MD5encodeToHex(String input) throws NoSuchAlgorithmException {
        // MD5 메시지 다이제스트 객체 생성
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 입력 문자열을 바이트 배열로 변환하고 해시 계산
        byte[] hash = md.digest(input.getBytes());

        // 해시 값을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // 각 바이트를 16진수로 변환
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0'); // 한 자리 16진수 값을 두 자리로 채우기 위해 '0' 추가
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase(); // 16진수 문자열을 대문자로 변환
    }

    public static void main(String[] args) {
        try {
            String input = "01062235635";
            System.out.println("Input: " + input);
            System.out.println("MD5 Hex Encoded: " + MD5encodeToHex(input));
            System.out.println("SHA-1 Hex Encoded: " + SHA1encodeToHex(input));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

