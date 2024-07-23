package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Data;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.service.DataService;
import com.dsqd.amc.linkedmo.service.SubscribeService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class SubscribeController {
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	private SubscribeService service = new SubscribeService();

	public SubscribeController() {
		setupEndpoints();
	}
	
	private void setupEndpoints() {
		path("/api", () -> {
			path("/v1.0", () -> {
				path("/subscribe", () -> {
					// Get data by ID
					get("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						Subscribe data = service.getSubscribeById(id);
						if (data != null) {
							logger.info("Data retrieved for ID: {}", id);
							res.status(200);
							return JSONValue.toJSONString(data);
						} else {
							logger.warn("Data not found for ID: {}", id);
							res.status(404);
							return "Data not found";
						}
					});
					
					// 전체 가입자 정보 조회 
					get("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();
						List <Subscribe> data = service.getSubscribeAll();
						if (data != null && data.size() > 0) {
							logger.info("All Subscribe Data retrieved: count {}", data.size());
							res.status(200);
							
							code = 200;
							msg = "정상 처리";
							JSONArray arry = new JSONArray();
							for (Subscribe s: data)
								arry.add(s);
							
							responseJSON.put("data", arry);
							
						} else {
							logger.warn("No Subscribe Data");
							res.status(404);
							return "Data not found";
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);

						return responseJSON.toJSONString();
					});

					// Insert new data
					post("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();

						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						logger.info(jsonObject.toJSONString());
						Subscribe data = JSONValue.parse(req.body(), Subscribe.class);
						
						//기존에 동일한 휴대전화번호가 있는지 확인함 
						List <Subscribe> checkDup = service.getSubscribeByMobileno(data);
						if (checkDup.size() > 0) {
							logger.warn("Already Subscribed Mobile: {}", data.getMobileno());
							code = 901;
							msg = "이미 가입된 전화번호입니다.";
						} else {
							
							// 통신사로 부가서비스 가입요청
							
							// 부가서비스 제공사로 가입요청
							
							// 아무 문제가 없으면 DB에 저장함 
							try {
								service.insertSubscribe(data);
								logger.info("Subscribe inserted: {}", data);
								res.status(201);
								code = 200;
								msg = "정상 가입";
								
							} catch (Exception e) {
								e.printStackTrace();
								code = 999;
								msg = "가입 중 오류가 발생하였습니다.";
							}
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);

						return responseJSON.toJSONString();
					});

					// Update existing data
					put("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						Subscribe data = JSONValue.parse(req.body(), Subscribe.class);
	                    data.setId(id);
	                    service.updateSubscribe(data);
	                    logger.info("Data updated for ID: {}", id);
	                    res.status(200);
	                    return JSONValue.toJSONString(data);
					});

					// Delete data by ID
					delete("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						service.deleteSubscribe(id);
						logger.info("Subscribe deleted for ID: {}", id);
						res.status(204);
						return "";
					});
				});
				
				path("/cancel", ()-> {
					// 해지자목록 조회  
					get("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();
						List <Subscribe> data = service.getCancelList();
						if (data != null && data.size() > 0) {
							logger.info("All Cancel Data retrieved: count {}", data.size());
							res.status(200);
							
							code = 200;
							msg = "정상 처리";
							JSONArray arry = new JSONArray();
							for (Subscribe s: data)
								arry.add(s);
							
							responseJSON.put("data", arry);
							
						} else {
							logger.warn("No Cancel Data");
							res.status(404);
							return "Data not found";
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);

						return responseJSON.toJSONString();
					});
					
					// 해지요청 처리
					post("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();

						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						logger.info(jsonObject.toJSONString());
						JSONObject dtocon = new JSONObject();
						dtocon.put("spcode", jsonObject.getAsString("carrier"));
						dtocon.put("mobileno", jsonObject.getAsString("phone"));
						
						Subscribe data = JSONValue.parse(dtocon.toJSONString(), Subscribe.class);
						
						List<Subscribe> targets = service.getSubscribeByMobileno(data);
						if (targets != null && targets.size()>0) {
							int dcnt = 0;
							for (Subscribe d: targets) {
								service.deleteSubscribe(d.getId());
								dcnt++;
								logger.info("[{}] Cancel Service - ID:{}", dcnt, d.getId());
							}
							res.status(200);
							
							code = 200;
							msg = "정상 처리";
							
						} else {
							code = 400;
							msg = "가입정보를 찾을 수 없습니다.";
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);

						return responseJSON.toJSONString();
					});
				});
			});
		});
	}
}
