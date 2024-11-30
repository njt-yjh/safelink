package com.dsqd.amc.linkedmo.buzzvil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Evententry;
import com.dsqd.amc.linkedmo.service.EvententryService;
import com.dsqd.amc.linkedmo.util.AES256Util;
import com.dsqd.amc.linkedmo.util.HashUtil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class Event12 {
    private static final Logger logger = LoggerFactory.getLogger(Event12.class);
    
    // html 페이지 생성
    public String buzzSafelinkHTML(String mobileno, String bz_tracking_id, String ip, String eventcd, String type, String encCheckcode) {
    	String checkcode = "";

    	EvententryService service = new EvententryService();
    	// 가입 subscribe id를 가져온다.
    	int subscribeid = service.getSubcribeIDByMobileno(mobileno);
    	
    	Evententry data = Evententry.builder()
    			.eventcd(eventcd)
    			.ucode(bz_tracking_id)
    			.status("21")
    			.entryip(ip)
    			.mobileno(mobileno)
    			.subscribeid(subscribeid+"")
    			.offercode("12") // buzzvil 이벤트 
    			.completedt(new Date())
    			.json("")
    			.build();
    	try {
    		checkcode = AES256Util.decrypt(encCheckcode);
    		
		} catch (Exception e) {
			e.printStackTrace();
			data.setStatus("90");
			data.setJson("CHECK CODE DECRYPT ERROR");
		}

    	logger.debug("MOBILENO : {}", mobileno );
    	logger.debug("BUZZVIL TRACKING ID : {}", bz_tracking_id );
    	logger.debug("EVENT IP : {}", ip );
    	logger.debug("EVENT CODE : {}", eventcd );
    	logger.debug("RECORD TYPE : {}", type );
    	logger.debug("CHECK CODE : {} - {}", checkcode, encCheckcode);
    	
		String[] codes = checkcode.split("\\|");
		
		if (codes.length < 2) {
			data.setStatus("90");
			data.setJson("WRONG CHECK CODE : " + checkcode);
		} else if (!mobileno.equals(codes[0])) {
			data.setStatus("90");
			data.setJson("DISMATCH MOBILE NO : param(" + mobileno + ") / checkcode(" + codes[0] + ")");
		} else {
			// 시간 확인
			String codetime = codes[2];
			long currTime = System.currentTimeMillis();
			long codeTime = Long.parseLong(codetime);
			if ((currTime - codeTime) > 30000) {
				data.setStatus("90");
				data.setJson("OLD CHECK CODE : now(" + currTime + ") / checkcode(" + codeTime + ")");
			}
			data.setJson(checkcode);
		}

		service.insertEvententry(data);
		logger.info("EventEntry insert : {}", data.toJSONString());
		
    	
    	return "<!DOCTYPE html>\r\n"
    			+ "<html lang=\"ko\">\r\n"
    			+ "<head>\r\n"
    			+ "<meta charset=\"UTF-8\">\r\n"
    			+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
    			+ "<title>휴대폰약속번호 가입하기</title>"
    			+ "	<script async src=\"https://buzz-js.buzzvil.com/buzzvil-pixel-sdk/buzzvil-pixel.js\"></script>\r\n"
    			+ "</head>\r\n"
    			+ "<body>\r\n"
    			+ "</body>\r\n"
    			+ "<script	src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>\r\n"
    			+ "<script language=\"javascript\">\r\n"
    			+ "function LoadingWithMask() {\r\n"
    			+ "	    // 화면의 높이와 너비를 구합니다.\r\n"
    			+ "	    var maskHeight = $(document).height();\r\n"
    			+ "	    var maskWidth = $(window).width();\r\n"
    			+ "	    \r\n"
    			+ "	    // 화면에 출력할 마스크를 설정해줍니다.\r\n"
    			+ "	    var mask = \"<div id='mask' style='position:fixed; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>\";\r\n"
    			+ "	    var loadingImg = '';\r\n"
    			+ "	    loadingImg += \"<div id='loadingImg' style='position: fixed; z-index:10000; display: none;'>\";\r\n"
    			+ "	    loadingImg += \" <img src='/images/spinner-2.gif' style='display: block; border-radius: 50%; box-shadow: 0 0 0 2px #EEE; object-fit: cover;'/>\";\r\n"
    			+ "	    loadingImg += \"</div>\";\r\n"
    			+ "\r\n"
    			+ "	    // 화면에 레이어 추가\r\n"
    			+ "	    $('body').append(mask).append(loadingImg);\r\n"
    			+ "\r\n"
    			+ "	    // 마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.\r\n"
    			+ "	    $('#mask').css({\r\n"
    			+ "	        'width': maskWidth,\r\n"
    			+ "	        'height': maskHeight,\r\n"
    			+ "	        'opacity': '0.3'\r\n"
    			+ "	    });\r\n"
    			+ "\r\n"
    			+ "	    // loadingImg 요소를 화면 중앙에 위치시킵니다.\r\n"
    			+ "	    $('#loadingImg').css({\r\n"
    			+ "	        'top': '50%',\r\n"
    			+ "	        'left': '50%',\r\n"
    			+ "	        'transform': 'translate(-50%, -50%)'\r\n"
    			+ "	    });\r\n"
    			+ "\r\n"
    			+ "	    // 마스크와 로딩 이미지 표시\r\n"
    			+ "	    $('#mask').show();\r\n"
    			+ "	    $('#loadingImg').show();\r\n"
    			+ "		setTimeout(function() { alert('서비스가입이 완료되었습니다.'); }, 100);\r\n"
    			+ "		window.bzDataLayer = [];\r\n"
    			+ "		function bzq() {window.bzDataLayer.push(arguments);}\r\n"
    			+ "		bzq(\"init\", \"119577650586064\");\r\n"
    			+ "		window.bzq(\"track\", { action: \"bz_action_complete\", redirect_url: \"/subscribe_done.html\"});\r\n"
    			+ "	}\r\n"
    			+ "$(document).ready(function(){\r\n"
    			+ "		LoadingWithMask();\r\n"
    			+ "});\r\n"
    			+ "</script>\r\n"
    			+ "</html>"
    			;
    }

	// 전송전문 조립
	private String sendRestAPI(String url, String jsonParam, String method) {
		String resp = "";
		HttpUtil http = new HttpUtil(url, jsonParam, method);
		try {
			resp = http.httpConnection();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HttpConnection ERROR : {}", e.getMessage());
		}
		
		return resp;
	}
	
	// 이벤트 진입처리
	// curl -H "Authorization: Bearer {S2S 연동용 토큰을 여기에 넣어주세요}" 
	// https://track.buzzvil.com/action/pb/s2s/\?bz_tracking_id\=10023_71ffbffd-ccf1-4edf-9c4c
	public JSONObject buzzSafelink(String mobileno, String bz_tracking_id, String ip, String eventcd, String type) {
		String token = "es-7b774b8e-3db5-43cb-b588-f524d7302949"; // Bearer Token
		String url = "https://track.buzzvil.com/action/pb/s2s/";
		//String eventcd = "119577650586064";

		String resp = "";
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("bz_tracking_id", bz_tracking_id);
		
		logger.debug("EVENT[{}] SEND URL : {}", "BUZZVIL", url);
		logger.debug("EVENT[{}] SEND PARAM : {}", "BUZZVIL", jsonParam.toJSONString());
		
		if ("javascript".equals(type)) {
			resp = "{'code':'100'}";
			logger.info("EVENT[{}] JAVASCRIPT RUN : {}", "BUZZVIL", resp);
		} else if ("S2S".equals(type)) {
			resp = sendRestAPI(url, jsonParam.toJSONString(), "POST");
			logger.debug("EVENT[{}] RETURN JSON : {}", "BUZZVIL", resp);
			
		}
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp = (JSONObject) JSONValue.parse(resp);
			String resCode = jsonResp.getAsString("code");
			EvententryService service = new EvententryService();
			// 가입 subscribe id를 가져온다.
			int subscribeid = service.getSubcribeIDByMobileno(mobileno);
			
			Evententry data = Evententry.builder()
					.eventcd(eventcd)
					.ucode(bz_tracking_id)
					.entryip(ip)
					.mobileno(mobileno)
					.subscribeid(subscribeid+"")
					.offercode("12") // buzzvil 이벤트 
					.completedt(new Date())
					.json(resp)
					.build();
			
			if ("200".equals(resCode)) { // 정상처리
				data.setStatus("20");
				service.insertEvententry(data);
				logger.info("EventEntry insert : {}", resp);
			} else if ("100".equals(resCode)) { // javascript로 발송
				data.setStatus("21");
				service.insertEvententry(data);
				logger.info("EventEntry insert : {}", resp);
			} else {
				data.setStatus("90");
				service.insertEvententry(data);
				logger.warn("EventEntry insert : {}", resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("JSON Parsing ERROR : {}", e.getMessage());
			jsonResp.put("resCode", "E99");
			jsonResp.put("resMsg", "JSONParsing ERROR");
		}
		return jsonResp;
	}
	
	// 이벤트 가입처리
	
	// 이벤트 취소처리 
	
	public static void main(String [] args) {
		// curl -H "Authorization: Bearer {S2S 연동용 토큰을 여기에 넣어주세요}" 
		// https://track.buzzvil.com/action/pb/s2s/\?bz_tracking_id\=10023_71ffbffd-ccf1-4edf-9c4c
		String eventcd = "119577650586064";
		String token = "es-7b774b8e-3db5-43cb-b588-f524d7302949"; // Bearer Token
		String url = "https://track.buzzvil.com/action/pb/s2s";
		String bz_tracking_id = "10023_71ffbffd-ccf1-4edf-9c4";
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("bz_tracking_id", bz_tracking_id);
		
		System.out.println(jsonParam.toJSONString());
	}
	
}
