package com.dsqd.amc.linkedmo.ohc;

import com.dsqd.amc.linkedmo.util.HashUtil;

import net.minidev.json.JSONObject;

public class Event {
	// 전송전문 조립
	
	// 이벤트 진입처리
	
	// 이벤트 가입처리
	
	// 이벤트 취소처리 
	
	public static void main(String [] args) {
		// http://map.ohpoint.co.kr/rest/commit/{eId}?joinInfo=실적정보&ohvalue=전달ohvalue&keyvalue=MD5해시값
		// keyvalue=MD5해시값 : ohvalue + “|” + keycode 조합의 MD5 해시
		// param 은 json 형태로 전달함
		String eventcd = "linksafe";
		String mobileno = "01062235635";
		String mappingno = HashUtil.SHA1toHex(mobileno);
		String ohvalue = "20241111231220_01329757";
		String keycode = "173l1ryYffihacZJqi22YFUuhzLcnlNaJhTPscqgCJlcU9A1P6Iq42pOcQu4LNE4";
		String keyvalue = HashUtil.MD5toHex(ohvalue + "|" + keycode); // 
		String url = "http://map.ohpoint.co.kr/rest/commit/"+eventcd;
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("joinInfo", mappingno);
		jsonParam.put("ohvalue", ohvalue);
		jsonParam.put("keyvalue", keyvalue.toLowerCase());
		
		System.out.println(jsonParam.toJSONString());
	}
	
}
