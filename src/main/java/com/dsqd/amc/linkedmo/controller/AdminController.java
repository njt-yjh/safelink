package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.*;

import java.security.Provider.Service;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.model.Evententry;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.service.AdminService;
import com.dsqd.amc.linkedmo.util.JSONHelper;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;


public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	private final AdminService adminService = new AdminService();

	public AdminController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
        path("/api", () -> {
            path("/v1.0", () -> {
                path("/admin/batch", () -> {
                    // Get all boards
                    get("", this::getBatchAll);
                });
                
                path("/admin/subscribe/status", () -> {
                    // Get all boards
                    post("", this::setAdminStatus);
                });
                
                path("/admin/evententry", () -> {
                    // Get all envententry
                    get("", this::getAllEvententry);
                });
                
                path("/admin/evententry2", () -> {
                    // Get all envententry
                    get("", this::getAllEvententry2);
                });
                
                path("/admin/summary1", () -> {
                    // 통계::가입자순증
                    post("", this::adminSummaryNet);
                });
                
                path("/admin/summary", () -> {
                    // 통계::가입통계
                    post("/offer", this::adminSummaryOffer);
                });

                path("/admin/dashboard", () -> {
                	// 당일현황
                	get("/today", this::adminDashbdToday);
                });
            });
        });
    }
	
	private String adminSummaryNet(Request req, Response res) {
		AdminService adminService = new AdminService();
		JSONObject param = new JSONObject();
		List<Map<String, Object>> lst = adminService.summaryNet(param);
		JSONArray retArry = new JSONArray();
		for (Map<String, Object> m: lst) {
			JSONObject obj = new JSONObject();
			for (Map.Entry<String, Object> entry : m.entrySet()) {
				String key = entry.getKey(); 
				Object value = entry.getValue(); 
				String valueAsString = value.toString();
				if ("summarydate".equals(key)) {
					obj.put(key, valueAsString);
				} else {
					obj.put(key, value);
				}
				// System.out.println("key : " + key + "   value : " + valueAsString);
			}
			retArry.add(obj);
		}
		return retArry.toJSONString();
	}
	
	private String adminSummaryOffer(Request req, Response res) {
		AdminService adminService = new AdminService();
		JSONObject param = new JSONObject();
		List<Map<String, Object>> lst = adminService.summaryOffer();
		JSONArray retArry = new JSONArray();
		for (Map<String, Object> m: lst) {
			JSONObject obj = new JSONObject();
			for (Map.Entry<String, Object> entry : m.entrySet()) {
				String key = entry.getKey(); 
				Object value = entry.getValue(); 
				String valueAsString = value.toString();
				if ("summarydate".equals(key)) {
					obj.put(key, valueAsString);
				} else {
					obj.put(key, value);
				}
				// System.out.println("key : " + key + "   value : " + valueAsString);
			}
			retArry.add(obj);
		}
		return retArry.toJSONString();
	}

	
	private String adminDashbdToday(Request req, Response res) {
		AdminService adminService = new AdminService();
		JSONObject retObj = new JSONObject();
		Map<String, Object> map = adminService.dashbdToday();
		retObj.put("payedcnt", map.get("payedcnt"));
		retObj.put("todaysubcnt", map.get("todaysubcnt"));
		retObj.put("todaycnlcnt", map.get("todaycnlcnt"));
		return retObj.toJSONString();
	}
	
	private String setAdminStatus(Request req, Response res) {
		/* 데이터를 받아와야 함
		UPDATE subscribe
        SET spcode=#{spcode},
            mobileno=#{mobileno},
			status=#{status},
            updated_at=NOW(),
            cancelreason='관리자가 상태 변경'
        WHERE id=#{id}
		 */
		JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
		int code = 0;
		String msg = "";
		
		try {
			int id = Integer.parseInt(jsonObject.getAsString("id"));
			String mobileno = jsonObject.getAsString("mobileno");
			String status = jsonObject.getAsString("status");
			String before_status = jsonObject.getAsString("bstatus");
			Subscribe data = Subscribe.builder()
					.id(id)
					.mobileno(mobileno)
					.status(status)
					.build();
			adminService.updateSubscribeStatus(data);
			code = 200;
			msg = "Status Changer SUCCESS - " + before_status + "->" + status;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DB UPDATE ERROR [{}]", e.getMessage());
			code = 901;
			msg = "Status Changer FAILED - " + e.getMessage();
		}
		return JSONHelper.assembleResponse(code, msg).toJSONString();
	}
	
    private String getJsonData(Request req, Response res) {
    	JSONObject jsonData = new JSONObject();
    	JSONArray lstJson = new JSONArray();
    	
    	List<Map<String, Object>> result = adminService.getJsonData(jsonData); 
    	for (Map<String, Object> row : result) { 
    		JSONObject json = new JSONObject(row); 
    		lstJson.add(json); 
    	}        
    	return lstJson.toJSONString();  // JSON Array 형식으로 반환
    }
	
    private String getBatchAll(Request req, Response res) {
        List<Batch> json_list = adminService.getBatchAll();
        return json_list.stream()
                     .map(Batch::toJSONString)
                     .reduce("[", (acc, json) -> acc + json + ",")
                     .replaceAll(",$", "]");  // JSON Array 형식으로 반환
    }
    
    private String getAllEvententry(Request req, Response res) {
    	List<Evententry> json_list = adminService.getAllEvententry();
        return json_list.stream()
                     .map(Evententry::toJSONString)
                     .reduce("[", (acc, json) -> acc + json + ",")
                     .replaceAll(",$", "]");  // JSON Array 형식으로 반환
    }
    
    private String getAllEvententry2(Request req, Response res) {
    	List<Evententry> json_list = adminService.getAllEvententry2();
        return json_list.stream()
                     .map(Evententry::toJSONString)
                     .reduce("[", (acc, json) -> acc + json + ",")
                     .replaceAll(",$", "]");  // JSON Array 형식으로 반환
    }
}
