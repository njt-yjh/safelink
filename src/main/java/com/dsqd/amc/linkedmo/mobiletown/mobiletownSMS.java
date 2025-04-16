package com.dsqd.amc.linkedmo.mobiletown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Mobiletown;
import com.dsqd.amc.linkedmo.service.MobiletownService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class mobiletownSMS {
	
	private static final Logger logger = LoggerFactory.getLogger(mobiletownSMS.class);
	
	private static String scheme = "https"; 
	private static String hostname = "api.mobiletown.net"; 
	private static int port = 9443;
	private static final String contentType = "json";
	
	private static String id = "amcapi01";
	private static String password = "mobiletown@1";
	private static String callback_number = "15335278";

	private static String subscribe_url = "https://linksafe.kr/subscribe_page.html";
	private static String subscribe_url_bitly_91 = "https://bit.ly/3YVxFzz";
	
	
	
	public mobiletownSMS() {}
	
	public mobiletownSMS(String callback_number) {
		this.callback_number = callback_number;
	}
	
	public mobiletownSMS(String id, String password) {
		this.id = id;
		this.password = password;
	}
	
	public mobiletownSMS(String id, String password, String callback_number) {
		this.id = id;
		this.password = password;
		this.callback_number = callback_number;
	}
	
	// 테스트용 
	public static void main(String[] args) { 
		mobiletownSMS sms = new mobiletownSMS();
		JSONObject json = sms.sendSms("01062235635", sms.setMessage1(genRandoms()));
		System.out.println(json.toJSONString());
		System.out.println(sms.setTrKey("01062235635"));
    }
	
	private String setTrKey(String mobileno) { 
		// 현재 시각을 밀리초로 가져오기 
		long currentMillis = System.currentTimeMillis(); 
		// 밀리초를 문자열로 변환 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		String currentMillisString = sdf.format(new Date(currentMillis)); 
		// 두 문자열 결합 
		return currentMillisString + mobileno; 
	}
	
	public String setMessage1(String random) {
		return "휴대폰약속번호 서비스 인증번호[" + random +"]을(를) 입력해주세요.";
	}
	
	public String setMessage101(String offercode) {
		if ("91".equals(offercode)) {
			return "휴대폰약속번호 신청 하시겠습니까?\n\n"+subscribe_url_bitly_91;
		} else {
			return "휴대폰약속번호 신청 하시겠습니까?\n\nhttps://linksafe.kr/subscribe.html";
		}
	}
	
	public JSONObject sendSms(String receiver_phone, String message) {
		return send("sms", receiver_phone, "", message);
	}
	
	public JSONObject sendLms(String receiver_phone, String subject, String message) {
		return send("lms", receiver_phone, subject, message);
	}
	
	private JSONObject send(String type, String receiver_phone, String subject, String message) {
        String msgType = type; // sms or lms
        JSONObject retObj = new JSONObject(); // code:200, msg:~~~~~
        
        /*
        String jsonData = "{\"callback\":\"15335278\"," +
        		"\"receivers\":[{\"key\":\"1\"," +
        		"\"phone\":\"01062235635\"}]," +
        		"\"subject\":\"LMS 메시지 제목입니다.\"," + 
        		"\"content\":\"메시지 내용입니다.\"}";
        */
        
        JSONArray receivers = new JSONArray();
        JSONObject receiver = new JSONObject();
        String key = setTrKey(receiver_phone);
        receiver.put("key", key); // 200자 -> mobileno + currentmil
        receiver.put("phone", receiver_phone);
        receivers.add(receiver); 
        
        JSONObject obj = new JSONObject();
        obj.put("callback", callback_number);
        obj.put("receivers", receivers);
        obj.put("content", message);
        if ("lms".equals(msgType)) obj.put("subject", subject); // lms인 경우는 subject 추가
        String data = obj.toJSONString();
        
        String url = String.format("%s://%s:%d/send/1.0/%s/%s", scheme, hostname, port, contentType, msgType);
        
        // Basic 인증처리 
    	HttpClientContext context = setAuthentication();
        
        HttpClientBuilder builder = HttpClientBuilder.create();

        try  {
        	SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { trustManager }, null);

            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext);
            builder.setSSLSocketFactory(sslConnectionFactory);
            
        } catch (Exception e) {
        	e.printStackTrace();
        	retObj.put("code", 9001); 
        	retObj.put("msg", e.getMessage()); 
        	return retObj;
        }
        
        CloseableHttpClient client = builder.build();

        try {
        	
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/" + contentType + "; charset=utf-8");
            
            StringEntity se = new StringEntity(data, "UTF-8");
            httpPost.setEntity(se);

            HttpResponse httpResponse = client.execute(httpPost, context);
            System.out.println(httpResponse.getStatusLine().getStatusCode());

            InputStream inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();; // 수신전문 
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                inputStream.close();
                
                JSONObject respObj = (JSONObject) JSONValue.parse(sb.toString());
                JSONArray failed = (JSONArray) respObj.get("failed");
                if (failed != null && failed.size()>0) {
                	for (Object o: failed) {
                		String code = ((JSONObject) o).getAsString("code");
                		retObj.put("code", Integer.parseInt(code));
                	}
	                retObj.put("msg", "sms send failed");
	                retObj.put("request",  obj);
	                retObj.put("response",  (JSONObject) JSONValue.parse(sb.toString()));
	                
                } else {
	                retObj.put("code", 200);
	                retObj.put("msg", "sms send success");
	                retObj.put("key", key);
	                retObj.put("request",  obj);
	                retObj.put("response",  (JSONObject) JSONValue.parse(sb.toString()));
	                
                }
                logger.info("MOBILETOWN SMS : {}", retObj.toJSONString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        	retObj.put("code", 9002); 
        	retObj.put("msg", e.getMessage()); 
        	return retObj;
        } finally {
            try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        return retObj;
	}
	
	private static HttpClientContext setAuthentication() {
		// HTTP Authentication
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope(hostname, port, AuthScope.ANY_REALM),
            new UsernamePasswordCredentials(id, password)
        );
		
        // Create AuthCache instance
        BasicAuthCache authCache = new BasicAuthCache();
        authCache.put(new HttpHost(hostname, port, "https"), new BasicScheme());

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        
        return context;
	}
	
	public static String genRandoms() { 
		SecureRandom secureRandom = new SecureRandom(); 
		int randomNumber = secureRandom.nextInt(1000000); // 0부터 999999 사이의 숫자 생성 
		return String.format("%06d", randomNumber); // 6자리 문자열로 포맷팅 }
	}
	
	private static TrustManager trustManager = new X509TrustManager() {
        
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                String authType) throws CertificateException {
            
        }
        
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                String authType) throws CertificateException {
            
        }
    };
}
