package com.dsqd.amc.linkedmo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minidev.json.JSONObject;

public class JSONHelper {
	private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);

	public static JSONObject assembleResponse(int code, String msg) {
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("code", code);
		responseJSON.put("msg", msg);
		logger.info("response JSON : {}", responseJSON.toJSONString());
		return responseJSON;
	}
	
	public static JSONObject assembleResponse(JSONObject responseJSON, int code, String msg) {
		responseJSON.put("code", code);
		responseJSON.put("msg", msg);
		logger.info("response JSON : {}", responseJSON.toJSONString());
		return responseJSON;
	}
	
	public static JSONObject assembleResponse(int code, JSONObject obj) {
		JSONObject responseJSON = new JSONObject();
		if (obj!= null) {
			for (String key : obj.keySet()) {
				responseJSON.put(key, obj.get(key));
	        }
		}
		responseJSON.put("code", code);
		logger.info("response JSON : {}", responseJSON.toJSONString());
		return responseJSON;
	}
}
