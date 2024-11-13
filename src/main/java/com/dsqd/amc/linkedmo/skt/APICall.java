package com.dsqd.amc.linkedmo.skt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class APICall {

	private static final Logger logger = LoggerFactory.getLogger(APICall.class);
	
//	private static final String basic = "https://apih.sktelecom.com/api"; // 운영
//	private static final String apiKey = "00w2gjs4rkelbe5h";
//	private static final String decKey = "l6sh5mk6fb40wq9e";


	private static String basic = "https://apihs.sktelecom.com/api"; // 스테이징
	private static String apiKey = "3clj7ilxoudchmd2";
	private static String decKey = "k8j11dyvwj5saang";
	
	private static String env = "local"; // 기본로컬
	
	public APICall(String env) {
		this.env = env;
		Properties prop = MyBatisConfig.getApplicationProperties();
		this.basic = prop.getProperty("rest.url");
		this.apiKey = prop.getProperty("rest.apiKey");
		this.decKey = prop.getProperty("rest.decKey");
	}
	
	public APICall() {
		this.env = System.getProperty("argEnv");
		Properties prop = MyBatisConfig.getApplicationProperties();
		this.basic = prop.getProperty("rest.url");
		this.apiKey = prop.getProperty("rest.apiKey");
		this.decKey = prop.getProperty("rest.decKey");
	}
	
	
    private String getOpDtm() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter); 
    }
    
    
    // ISSWG00047  부가서비스 가입/해지
    
    public String ISSWG00047(Map param) {
    	return run_ISSWG00047(param);
    }
    
    private String run_ISSWG00047(Map param) {
		String path = "/is/ISSWG00047/";
		String action = (String) param.get("action");
		String SCRB_TERM_CL_CD = "";
		String CAN_YN = "N";
		
		if ("subscribe".equals(action)) {
			SCRB_TERM_CL_CD="01";
			
		} else if ("cancel".equals(action)) {
			SCRB_TERM_CL_CD="03";
			
		} else { // action code가 잘못 들어온 경우
			logger.error("Wrong Param : {} - {}", "Not Include available ACTION", param);
		}
		
		// request parameter
		JSONObject requestParam = new JSONObject();
		try {
            requestParam.put("SVC_NUM", (String) param.get("mobileno"));
            requestParam.put("CHNL_CL_CD", "ECG");
            requestParam.put("CHG_CD", "I2");
            requestParam.put("CHG_RSN_CD", "00");
            requestParam.put("OP_DTM", getOpDtm());
            requestParam.put("CAN_YN",CAN_YN);
            requestParam.put("PROD_ID", "NC00000200");
            requestParam.put("SCRB_TERM_CL_CD", SCRB_TERM_CL_CD);  // "01":가입 , "03":해지
            requestParam.put("ADD_INFO_EXIST_YN", "N");
            requestParam.put("AUDIT_DTM", getOpDtm());
            requestParam.put("CHANNEL_ID", "API0000019");
            
            if ("03".equals(SCRB_TERM_CL_CD)) { // 해지시 취소기준일시 추가요청 2024-10-21
            	requestParam.put("CAN_BASE_DTM", getOpDtm());
            }
            
      		logger.info("Request Param : {}", requestParam.toJSONString());            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
        }
		
		JSONObject retObj = callREST(path, requestParam.toJSONString());
		logger.info("SKT RETURNS : {}", retObj.toJSONString());
		/**
		 * JSON : {"RESPONSE":
		 * 			{"HEADER":
		 * 				{"RESULT_CODE":"00",
		 * 				"RESULT":"S",
		 * 				"RESULT_MESSAGE":"서비스가 정상적으로 처리되었습니다."},
		 * 			"BODY":"암호화된 String 내용"}
		 * 		  }
		 */
		
		return retObj.toJSONString();
    }
    
    // ISICS00021 서비스정보

    public String ISICS00021(String mobileno) {
    	return run_ISICS00021(mobileno);
    }
    
    private String run_ISICS00021(String mobileno) {
		String path = "/is/ISICS00021/";
		
		// request parameter
		JSONObject requestParam = new JSONObject();
		try {
            requestParam.put("SVC_NUM", mobileno);
//            requestParam.put("CHANNEL_ID", "API0000019");

      		logger.info("Request Param : {}", requestParam.toJSONString());            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
        }
		
		JSONObject retObj = callREST(path, requestParam.toJSONString());
		logger.info("SKT RETURNS : {}", retObj.toJSONString());
		/**
		 * JSON : {"RESPONSE":
		 * 			{"HEADER":
		 * 				{"RESULT_CODE":"00",
		 * 				"RESULT":"S",
		 * 				"RESULT_MESSAGE":"서비스가 정상적으로 처리되었습니다."},
		 * 			"BODY":"암호화된 String 내용"}
		 * 		  }
		 */
		
		return retObj.toJSONString();
	}
    
    // ISICS00022 부가서비스 가입여부

    public String ISICS00022(String mobileno) {
    	return run_ISICS00022(mobileno);
    }
    
    private String run_ISICS00022(String mobileno) {
		String path = "/is/ISICS00022/";
		
		// request parameter
		JSONObject requestParam = new JSONObject();
		try {
            requestParam.put("SVC_NUM", mobileno);
            requestParam.put("PROD_ID", "NC00000200");
//            requestParam.put("CHANNEL_ID", "API0000019");

      		logger.info("Request Param : {}", requestParam.toJSONString());            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
        }
		
		JSONObject retObj = callREST(path, requestParam.toJSONString());
		logger.info("SKT RETURNS : {}", retObj.toJSONString());
		/**
		 * JSON : {"RESPONSE":
		 * 			{"HEADER":
		 * 				{"RESULT_CODE":"00",
		 * 				"RESULT":"S",
		 * 				"RESULT_MESSAGE":"서비스가 정상적으로 처리되었습니다."},
		 * 			"BODY":"암호화된 String 내용"}
		 * 		  }
		 */
		
		return retObj.toJSONString();
	}
	
	private JSONObject callREST(String path, String param) {
				
		// 주소
		String url = basic + path + apiKey;
		logger.info("[{}]:Request URL: {}", env, url);
		
		// local이면 보내지 않음
		if ("local".equals(env)) {
			logger.warn("[{}] {}", env, "DO NOT SEND TO SKT SERVER");
			return new JSONObject();
		} else if ("dev".equals(env) || "develpoment".equals(env)) {
			//logger.warn("[{}] {}", env, "server send");
			//return new JSONObject();
		} else if ("prod".equals(env)) {
			//logger.warn("[{}] {}", env, "server send");
			//return new JSONObject();
		}
		
		StringBuilder sb = new StringBuilder();

		HttpUtil http = new HttpUtil(url, param);
		String resp = null;
        JSONObject data = null;

		try {
			resp = http.httpConnection();
			/**
			 * JSON : {"RESPONSE":{"HEADER":{"RESULT_CODE":"00","RESULT":"S","RESULT_MESSAGE":"서비스가 정상적으로 처리되었습니다."},"BODY":"암호화된 String 내용"}}
			 */
			logger.debug("Response : {}", resp);
			
			if( resp != null ) {
				JSONObject dataMap = null;
                JSONObject header = null;
                JSONObject body = null;

                // JSON 파싱에 json-smart 사용
                dataMap = (JSONObject) JSONValue.parse(resp);
                data = (JSONObject) dataMap.get("RESPONSE");
				
                if (data != null) {
                    header = (JSONObject) data.get("HEADER");

                    String result = header.getAsString("RESULT");
        			logger.debug("Message : {}", header.getAsString("RESULT_MESSAGE"));
        			logger.debug("Result : {}", result);

                    if ("S".equals(result)) {

                        // Body 추출
                        String encBody = data.getAsString("BODY");
                        String strBody = decrypt(encBody);
                        logger.debug("BODY : {}", strBody);
                        
                        // 암호화된 body 부분을 json-smart로 파싱
                        body = (JSONObject) JSONValue.parse(strBody);
                        data.put("BODY", body);

                    } 
               }
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		return data;
	}
	
	private static String decrypt(String data) throws Exception {
		byte[] decodeBase64 = Base64.decodeBase64(data);
		return CryptoUtil.decrypt(new String(decodeBase64, "UTF-8"), decKey);
	}
	
}
