package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.buzzvil.Event12;
import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.ohc.Event11;
import com.dsqd.amc.linkedmo.service.AdminService;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;


public class EventController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	private final AdminService adminService = new AdminService();

	public EventController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
        path("/api", () -> {
            path("/v1.0", () -> {
                path("/event", () -> {
                    // push OHPoint event, with MD5
                    post("/ohc", this::pushOhcSafelink);
                    post("/ohcPG", this::pushOhcSafelinkPG);
                    post("/buzzvil", this::pushBuzzSafelink);
                    post("/buzzvilHtml", this::pushBuzzSafelinkHTML);
                    post("/buzzvilHtmlPG", this::pushBuzzSafelinkHTMLPG);
                });
            });
        });
    }
	
    private String pushOhcSafelink(Request req, Response res) {
    	JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
    	logger.info("OHC EVENT BODY : {}", req.body());
    	
        JSONObject json = new JSONObject();
        String mobileno = jsonObject.getAsString("mobileno");
        String ohvalue = jsonObject.getAsString("ohvalue");
        String m_id = jsonObject.getAsString("m_id");
        
        if (m_id != null && !"".equals(m_id)) {
	        Event11 event = new Event11();
	        json = event.ohcSafelink(mobileno, ohvalue, req.ip(), m_id);
        } else {
        	json.put("resCode", "E98");
        	json.put("resMsg", "m_id가 빈값임"); 
        	
        }
        logger.info("OHC EVENT RESULT : {}", json.toJSONString());
        return json.toJSONString();
    }
    
    private String pushBuzzSafelink(Request req, Response res) {
    	JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
    	logger.info("BUZZ EVENT BODY : {}", req.body());
    	
        JSONObject json = new JSONObject();
        String mobileno = jsonObject.getAsString("mobileno");
        String bz_tracking_id = jsonObject.getAsString("bz_tracking_id");
        String recordType = jsonObject.getAsString("recordType");
        String eventcd = "119577650586064";

        if (bz_tracking_id != null && !"".equals(bz_tracking_id)) {
	        Event12 event = new Event12();
	        json = event.buzzSafelink(mobileno, bz_tracking_id, req.ip(), eventcd, recordType);
        } else {
        	json.put("resCode", "E98");
        	json.put("resMsg", "bz_tracking_id가 빈값임"); 
        	
        }
        logger.info("BUZZ EVENT RESULT : {}", json.toJSONString());
        return json.toJSONString();
    }
    
    private String pushBuzzSafelinkHTML(Request req, Response res) {
        String mobileno = req.queryParams("bz_mobileno");
        String bz_tracking_id = req.queryParams("bz_tracking_id");
        String recordType = req.queryParams("bz_recordType");
        String checkcode = req.queryParams("bz_checkcode");
        String eventcd = "119577650586064";

        String ret = "";
        
        Event12 event = new Event12();
        ret = event.buzzSafelinkHTML(mobileno, bz_tracking_id, req.ip(), eventcd, recordType, checkcode);

        return ret;
    }

    private String pushBuzzSafelinkHTMLPG(Request req, Response res) {

        JSONObject jsonObject = new JSONObject();
        String[] params = req.body().split("&");
        for(String param : params) {
            // =로 분리하여 key와 value를 추출
            String[] keyValue = param.split("=");
            if(keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "EUC-KR");
                    String value = URLDecoder.decode(keyValue[1], "EUC-KR");
                    jsonObject.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        String mobileno = jsonObject.getAsString("No");
        String bz_tracking_id = null;
        String recordType = "javascript";
        String checkcode = null;
        String eventcd = "119577650586064";

        for(String item : jsonObject.getAsString("MSTR").split("\\|")) {
            String [] keyValue = item.split("=");

            switch (keyValue[0]) {
                case "bz_tracking_id" :
                    bz_tracking_id = keyValue[1];
                    break;
                case "bz_checkcode" :
                    checkcode = keyValue[1];
                    break;
            }
        }

        logger.info("bz_checkcode : {}", checkcode);

        String ret = "";

        Event12 event = new Event12();
        ret = event.buzzSafelinkHTML(mobileno, bz_tracking_id, req.ip(), eventcd, recordType, checkcode);

        return ret;
    }

    private String pushOhcSafelinkPG(Request req, Response res) {
        JSONObject jsonObject = new JSONObject();
        String[] params = req.body().split("&");
        for(String param : params) {
            // =로 분리하여 key와 value를 추출
            String[] keyValue = param.split("=");
            if(keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "EUC-KR");
                    String value = URLDecoder.decode(keyValue[1], "EUC-KR");
                    jsonObject.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("OHC EVENT BODY : {}", req.body());

        JSONObject json = new JSONObject();
        String mobileno = jsonObject.getAsString("No");
        String ohvalue = null;
        String m_id = null;

        for(String item : jsonObject.getAsString("MSTR").split("\\|")) {
            String [] keyValue = item.split("=");

            switch (keyValue[0]) {
                case "ohvalue" :
                    ohvalue = keyValue[1];
                    break;
                case "m_id" :
                    m_id = keyValue[1];
                    break;
            }
        }

        if (m_id != null && !"".equals(m_id)) {
            Event11 event = new Event11();
            json = event.ohcSafelink(mobileno, ohvalue, req.ip(), m_id);
        } else {
            json.put("resCode", "E98");
            json.put("resMsg", "m_id가 빈값임");

        }
        logger.info("OHC EVENT RESULT : {}", json.toJSONString());

        Properties properties = new Properties();
        try {
            properties.load(Resources.getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.redirect(properties.getProperty("mobilians.paycomplete.url"));

        return null;
    }
}
