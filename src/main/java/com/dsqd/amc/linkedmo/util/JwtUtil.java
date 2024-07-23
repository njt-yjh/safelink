package com.dsqd.amc.linkedmo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "jwt-256-bit-secret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    
    private static final String server_SECRET = "amc_maru_server_$%13*&";
    private static final Algorithm server_algorithm = Algorithm.HMAC256(server_SECRET);
    
    public static String createToken(String clientIP, String username) throws Exception {
        String encryptedIP = AES256Util.encrypt(clientIP);
        String encryptedUsername = AES256Util.encrypt(username);
        return JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800_000)) // 30 minutes
                .withClaim("clientIP", encryptedIP)
                .withClaim("username", encryptedUsername)
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();
        return verifier.verify(token);
    }
    
    public static String getClientIP(DecodedJWT decodedJWT) throws Exception {
        String encryptedIP = decodedJWT.getClaim("clientIP").asString();
        return AES256Util.decrypt(encryptedIP);
    }
    
    public static String getUsername(DecodedJWT decodedJWT) throws Exception {
        String encryptedUsername = decodedJWT.getClaim("username").asString();
        return AES256Util.decrypt(encryptedUsername);
    }
    
    // 서버 토큰용 
    public static String createServerToken(String clientIP, String username, long expire_sec) throws Exception {
        return JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + (expire_sec*1000)))
                .withClaim("clientIP", clientIP)
                .withClaim("username", username)
                .sign(server_algorithm);
    }
    
    public static DecodedJWT verifyServerToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(server_algorithm)
                .withIssuer("auth0")
                .build();
        return verifier.verify(token);
    }
    
    
    public static void main(String [] args) {
    	try {
			String token = createServerToken("103.55.192.229", "amcinc", (60*60*24*30*6l));
			System.out.println(token);
			
			DecodedJWT dtoken = verifyServerToken(token);
			System.out.println("SECRET : [" + server_SECRET + "]");
			System.out.println();
			System.out.println("Verify Token - issuer    : " + dtoken.getIssuer());
			System.out.println("Verify Token - clientIP  : " + dtoken.getClaim("clientIP").asString());
			System.out.println("Verify Token - username  : " + dtoken.getClaim("username").asString());
			System.out.println("Verify Token - expiresAt : " + dtoken.getExpiresAt());


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
