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
import com.dsqd.amc.linkedmo.util.InterfaceManager;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class SubscribeController {
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	private SubscribeService service = new SubscribeService();
	private InterfaceManager itfMgr = InterfaceManager.getInstance();
	
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
						
						//오늘 가입한 적이 있는 휴대전화번호인지 확인함
						List <Subscribe> todayMobileno = service.getTodaySubscribeByMobileno(data);
						
						if (checkDup.size() > 0) {
							logger.warn("Already Subscribed Mobile: {}", data.getMobileno());
							code = 901;
							msg = "이미 가입된 전화번호입니다.[901]";
							
						} else if (todayMobileno.size() > 0) { // 오늘 가입한적이 있음 
							code = 902;
							msg = "입력하신 전화번호는 해지관련 전산처리 중으로 내일 가입이 가능합니다.[902]";
							
						} else { // 정상 가입 
							
							// 통신사로 부가서비스 가입요청
							
							// 부가서비스 제공사로 가입요청
							String method = "POST";
							String uri = "/api/v1.0/linksafe/subscribe";
							JSONObject jsonParam = new JSONObject();
							jsonParam.put("spcode", data.getSpcode());
							jsonParam.put("mobileno", data.getMobileno());
							
							JSONObject itfJSON = (JSONObject) JSONValue.parse(itfMgr.sendRequest(method, uri, jsonParam.toJSONString()));
							String resultCode = itfJSON.getAsString("code");
							String resultMsg = itfJSON.getAsString("msg");
							// 아무 문제가 없으면 DB에 저장함 
							
							if ("200".equals(resultCode)) {
								try {
									service.insertSubscribe(data);
									logger.info("Subscribe inserted: {}", jsonParam.toJSONString());
									res.status(201);
									code = 200;
									msg = "정상 가입";
									
								} catch (Exception e) {
									e.printStackTrace();
									code = 999;
									msg = "가입 중 오류가 발생하였습니다.";
								}
							} else {
								code = Integer.parseInt(resultCode);
								msg = resultMsg;
							}
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);
						
						logger.info("response JSON : {}", responseJSON.toJSONString());
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
							
							// 통신사로 부가서비스 취소 요청
							
							// 부가서비스 제공사로 해지 요청
							String method = "POST";
							String uri = "/api/v1.0/linksafe/cancel";
							JSONObject jsonParam = new JSONObject();
							jsonParam.put("spcode", data.getSpcode());
							jsonParam.put("mobileno", data.getMobileno());
							jsonParam.put("status", "C");
							
							JSONObject itfJSON = (JSONObject) JSONValue.parse(itfMgr.sendRequest(method, uri, jsonParam.toJSONString()));
							String resultCode = itfJSON.getAsString("code");
							String resultMsg = itfJSON.getAsString("msg");
							// 아무 문제가 없으면 DB에 저장함 
							
							if ("200".equals(resultCode)) {
								try {
									int dcnt = 0;
									for (Subscribe d: targets) {
										service.deleteSubscribe(d.getId());
										dcnt++;
										logger.info("[{}] Cancel Service - ID:{}", dcnt, d.getId());
									}
									code = 200;
									msg = "정상 해지";
									
								} catch (Exception e) {
									e.printStackTrace();
									code = 999;
									msg = "해지 중 오류가 발생하였습니다.";
								}
							} else {
								code = Integer.parseInt(resultCode);
								msg = resultMsg;
							}
							
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
