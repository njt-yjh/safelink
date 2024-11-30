package com.dsqd.amc.linkedmo.buzzvil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {

    private final String charset = "UTF-8"; // UTF-8 이외는 글자가 비정상.
    private final int connectionTimeout = 3000;
    private final int readTimeout = 5000;

    private String contentType = "application/x-www-form-urlencoded"; // 
    private String url;
    private String param;
    private String method; // 요청 방식을 담는 변수 추가
    
    private String token="es-7b774b8e-3db5-43cb-b588-f524d7302949"; // buzzvil 토큰

    public void setToken(String token) {
    	this.token = token;
    }
    
    public void setContentType (String contentType) {
    	this.contentType = contentType;
    }
    
    public HttpUtil(String url, String jsonStringParam, String method) {
        this.url = url;
        this.param = jsonStringParam;
        this.method = method.toUpperCase();
    }

    public String httpConnection() throws Exception {
        if ("GET".equals(this.method)) {
            this.url += "?" + convertJsonToQueryString(this.param);
        } else if ("POST".equals(this.method)) {
        	this.param = convertJsonToQueryString(this.param);
        }

        URL url = new URL(this.url);
        HttpURLConnection conn = null;
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };
            HttpsURLConnection connect = (HttpsURLConnection) url.openConnection();
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new SecureRandom());
            connect.setSSLSocketFactory(context.getSocketFactory());
            conn = connect;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestProperty("Content-Type", this.contentType);
        conn.setRequestProperty("Accept-Charset", this.charset);
        conn.setRequestProperty("Authorization", "Bearer " + this.token);
        conn.setRequestMethod(this.method);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        conn.setConnectTimeout(this.connectionTimeout);
        conn.setReadTimeout(this.readTimeout);

        if ("POST".equals(this.method)) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(this.param.getBytes(this.charset));
            os.flush();
            os.close();
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), this.charset));
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return response.toString();
    }

    private String convertJsonToQueryString(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);
        StringBuilder queryString = new StringBuilder();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (queryString.length() != 0) {
                queryString.append("&");
            }
            queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                       .append("=")
                       .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8.toString()));
        }
 
        return queryString.toString();
    }

    public static void main(String[] args) {
        try {
            String url = "https://example.com";
            String jsonStringParam = "{\"key\":\"value\"}";
            String method = "POST"; // 요청 방식을 설정 (POST 또는 GET)
            HttpUtil httpUtil = new HttpUtil(url, jsonStringParam, method);
            String response = httpUtil.httpConnection();
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
