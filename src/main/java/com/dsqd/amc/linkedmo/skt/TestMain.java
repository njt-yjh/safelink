package com.dsqd.amc.linkedmo.skt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestMain {

	/**
	 * 해당 소스는 Sample 입니다.( 꼭 이렇게만 사용해야한다는 것이 아닙니다. )
	 * HttpUtil은 가장 기본적인 java로만 구성되어 있으므로, 개발하시는 사이트에 맞게 사용하시면 됩니다.
	 */
	
	private static ObjectMapper mapper = new ObjectMapper();	// lib 경로의 jackson-databind, jackson-core, jackson-annotations jar파일
	
	//private static final String basic = "https://apih.sktelecom.com/api"; // 운영
	private static final String basic = "https://apihs.sktelecom.com/api"; // 스테이징
	private static String path = "/is/ISICS00021/"; // or /im/제공API서비스ID/ (mashup) ex) IMASH00001 ...
	private static final String apiKey = "3clj7ilxoudchmd2";
	private static final String decKey = "k8j11dyvwj5saang";
	
	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) {
		test();
	}
	
	public String ISICS00021() {
		path = "/is/ISICS00021/";
		return test();
	}
	
	private static String test() {
		//System.setProperty("javax.net.debug", "ssl");
		
		/**
		 * JAVA 가 구버젼 일 경우 ssl연결 시 인증서 관련 에러가 발생 할 수 있으며, 해당 사항의 경우는 installCert.java 파일을 이용하는 방법 등을 이용하여
		 * 인증서를 내부 서버에 업데이트 해야합니다. ( InstallCert.java 파일 상단에  java의 인증서가 설치되는 경로를 확인해 주세요. )
		 * javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: 
		 * 	sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
		 * 실행 방법 : java InstallCert apihub.sktelecom.com
		 */
		
		System.err.println("///////////////////  API HUB TEST - START  ///////////////////");
		
		// 주소
		String url = basic + path + apiKey;
		System.err.println("Request URL : "+url);
		
		// request parameter
		String param = "";
		try {
			Map<String, Object> requestParam = new HashMap<String, Object>();
			requestParam.put("SVC_NUM", "01043002305");
			//requestParam.put("KEY02", "DATA02");
			
			param = mapper.writeValueAsString(requestParam);
			System.err.println("Request Param : "+param);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();

		
		HttpUtil http = new HttpUtil(url, param);
		String resp = null;
		try {
			resp = http.httpConnection();
			/**
			 * JSON : {"RESPONSE":{"HEADER":{"RESULT_CODE":"00","RESULT":"S","RESULT_MESSAGE":"서비스가 정상적으로 처리되었습니다."},"BODY":"암호화된 String 내용"}}
			 */
			System.err.println("Response : "+resp);
			
			if( resp != null ) {
				Map<String, Object> dataMap = null;
				Map<String, Object> data = null;
				Map<String, Object> header = null;
				Map<String, Object> body = null;
				dataMap = mapper.readValue(resp, Map.class);
				data = (Map<String, Object>) dataMap.get("RESPONSE");
				
				if( data != null) {
					header = (Map<String, Object>) data.get("HEADER");
					
					String result = header.get("RESULT").toString();
					if( "S".equals(result) ) {
						System.err.println("Result : Success.");
						System.err.println("Message : " + header.get("RESULT_MESSAGE"));
						
						// Body 추출
						String encBody = data.get("BODY").toString();
						String strBody = decrypt(encBody);
						body = mapper.readValue(strBody, Map.class);
						
						for (String key: body.keySet()) {
							sb.append(key).append(" : ").append(body.get(key)).append("<br/>");
							System.out.println(key + " : " + body.get(key));
						}
						
					} else {
						System.err.println("Result : Failed.");
						System.err.println("Message : " + header.get("RESULT_MESSAGE"));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.err.println("///////////////////  API HUB TEST - END  ///////////////////");
		return sb.toString();
	}
	
	private static String decrypt(String data) throws Exception {
		byte[] decodeBase64 = Base64.decodeBase64(data);
		return CryptoUtil.decrypt(new String(decodeBase64, "UTF-8"), decKey);
	}
}
