package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.mobiletown.SubscribeMobiletown;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.naru.SubscribeNaru;
import com.dsqd.amc.linkedmo.service.SubscribeService;
import com.dsqd.amc.linkedmo.skt.SubscribeSK;
import com.dsqd.amc.linkedmo.util.AES256Util;
import com.dsqd.amc.linkedmo.util.InterfaceManager;
import com.dsqd.amc.linkedmo.util.JSONHelper;
import com.dsqd.amc.linkedmo.util.TestMobileno;

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
				// 콜센터 등 관리자용
				path("/admin", () -> {
					// 전체 가입자 정보 조회 
					get("/subscribe", (req, res) -> {
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
					
					// Get data by ID
					get("/subscribe/:id", (req, res) -> {
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
					
					// Update existing data
					put("/subscribe/:id", (req, res) -> {
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
					delete("/subscribe/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						service.deleteSubscribe(id);
						logger.info("Subscribe deleted for ID: {}", id);
						res.status(204);
						return "";
					});
					
					// 해지자목록 조회  
					get("/cancel", (req, res) -> {
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
					
				});
				
				// 사용자 홈페이지
				path("/subscribe", () -> {
					// 가입자 등록
					post("", (req, res) -> {
						int code = 999;
						String msg = "";
						String sp_uid = "";
						
						JSONObject responseJSON = new JSONObject();

						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						logger.info(jsonObject.toJSONString());
						Subscribe data = JSONValue.parse(req.body(), Subscribe.class);
						
						// checkcode의 전화번호와 요청한 전화번호가 같은지 확인
						String encCheckcode = data.getCheckcode();
						if (encCheckcode != null && !"".equals(encCheckcode)) {
							try {
								String checkcode = AES256Util.decrypt(encCheckcode);
								//logger.info("checkcode:{}", checkcode);
								String[] codes = checkcode.split("\\|");
								if (codes.length > 2) {
									String cmobileno = codes[0];
									// 개발서버에서는 체크하지 말것
									if ((System.getProperty("argEnv")).equals("dev")) cmobileno = data.getMobileno();
									if (!cmobileno.equals(data.getMobileno())) {
										logger.warn("cmobileno:{} - data.getMobileno():{}", cmobileno, data.getMobileno());
										return JSONHelper.assembleResponse(951, "정상적인 방법으로 인증번호를 입력하고 가입하여 주세요.[951]");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("CHECK CODE 검증시 오류 : [{}]", e.getMessage());
								return JSONHelper.assembleResponse(952, "정상적인 방법으로 인증번호를 입력하고 가입하여 주세요.[952]");
							}
						} else {
							return JSONHelper.assembleResponse(952, "정상적인 방법으로 인증번호를 입력하고 가입하여 주세요.[953]");
						}
						
						//기존에 동일한 휴대전화번호가 있는지 확인함 
						List <Subscribe> checkDup = service.getSubscribeByMobileno(data);
						
						//오늘 가입한 적이 있는 휴대전화번호인지 확인함
						List <Subscribe> todayMobileno = service.getTodaySubscribeByMobileno(data);
						
						// TEST 전용폰 확인하여 당일해지자 가입방지 로직 Skeip
						TestMobileno tm = new TestMobileno();
						
						if (checkDup.size() > 0) {
							return JSONHelper.assembleResponse(901, "이미 가입된 전화번호입니다.[901]");
							
						} else if (todayMobileno.size() > 0 && !tm.isTestphone(data.getMobileno())) { // 오늘 가입한적이 있음 
							return JSONHelper.assembleResponse(902, "입력하신 전화번호는 해지관련 전산처리 중으로 내일 가입이 가능합니다.[902]");
							
						} else { // 정상 가입 프로세스 시작 
							
							// ========================================================
							// 통신사로 부가서비스 가입요청 -- START
							// ========================================================
							
							if ("SKT".equals(data.getSpcode())) { // 통신사 코드로 분기처리
								SubscribeSK skt = new SubscribeSK();
								responseJSON = skt.user(data); // 사용자가 있는지 확인
								if ((int) responseJSON.get("code") != 200) 
									return responseJSON;
								else 
									data.setSpuserid(responseJSON.getAsString("SVC_MGMT_NUM"));
								
								responseJSON = skt.subscribe(data);
								
							} else if ("KT".equals(data.getSpcode())) {
								
							} else if ("LGU".equals(data.getSpcode())) {
								
							} else { // 통신사 코드에 이상것이 들어왔을 때 
								return JSONHelper.assembleResponse(997, "통신사를 선택하여야 합니다.[997]");
							}
							// 정상완료가 되었다면 code=200 이어야 함
							if ((int) responseJSON.get("code") != 200) 
								return responseJSON;
							
							// ========================================================
							// 통신사로 부가서비스 가입요청 -- END
							// ========================================================
							
							
							
							// ========================================================
							// 부가서비스 제공사로 가입요청 -- START
							// ========================================================
							SubscribeNaru naru = new SubscribeNaru();
							responseJSON = naru.subscribe(data);
							if ((int) responseJSON.get("code") != 200) 
								return responseJSON;
							// ========================================================
							// 부가서비스 제공사로 가입요청 -- END
							// ========================================================

							
							// ========================================================
							// 자체 DB 저장 -- START
							// ========================================================
							
							if ((int) responseJSON.get("code") == 200) {
								try {
									service.insertSubscribe(data);
									logger.info("Subscribe inserted: {}", data.toString());
									res.status(201);
									code = 200;
									msg = "정상 가입";
									
									// SVC_MGMT_NUM 업데이트 =>> 시간이 걸려서 바로 처리는 안됨
//									Thread.sleep(3000);
//									SubscribeSK skt = new SubscribeSK();
//									responseJSON = skt.confirm(data);
									
								} catch (Exception e) {
									e.printStackTrace();
									code = 999;
									msg = "정상적으로 가입이 처리되지 못했습니다.[999]";
								}
							} else {
								code = (int) responseJSON.get("code");
								msg = responseJSON.getAsString("msg");
							}
							
							// ========================================================
							// 자체 DB 저장 -- END
							// ========================================================
						}
						
						return JSONHelper.assembleResponse(code, msg);
					});

					path("/mobiletown", ()-> {
						// 가입시 SMS 문자발송 
						post("/sendsms", (req, res) -> {
							int code = 999;
							String msg = "";
							
							JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
							logger.info(jsonObject.toJSONString());
							
							String mobileno = jsonObject.getAsString("mobileno");
							Subscribe data = Subscribe.builder().mobileno(mobileno).build();
							SubscribeSK skt = new SubscribeSK();
							JSONObject responseJSON = skt.user(data); // 사용자가 있는지 확인
							if ((int) responseJSON.get("code") != 200) 
								return responseJSON;
							else 
								data.setSpuserid(responseJSON.getAsString("SVC_MGMT_NUM"));
							
							SubscribeMobiletown smt = new SubscribeMobiletown();
							JSONObject json = new JSONObject();
							TestMobileno tm = new TestMobileno();

							if (tm.isTestphone(mobileno)) {
								// 특정 핸드폰의 경우에는 SMS를 발송하지 않고 저장함
								json = smt.subscribeMobiletownPseudo(mobileno, data.getSpuserid());
								
							} else {
								// 개발서버일 경우에는 SMS를 다른 폰으로 쏜다. ===================
								if ((System.getProperty("argEnv")).equals("dev"))  mobileno = itfMgr.getTestMobileno();
								// ===============================================================
								json = smt.subscribeMobiletown(mobileno, data.getSpuserid());
							}
							
							if (200 == (int) json.get("code")) {
								code = 200;
								msg = "";
							} else {
								code = (int) json.get("code");
								msg = json.getAsString("msg");
							}
							return JSONHelper.assembleResponse(code, msg);
						});
						
						//
						post("/checkotp", (req, res) -> {
							int code = 999;
							String msg = "";
							
							JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
							logger.info(jsonObject.toJSONString());
							
							String mobileno = jsonObject.getAsString("mobileno");
							String rnumber = jsonObject.getAsString("rnumber");
							
							SubscribeMobiletown smt = new SubscribeMobiletown();
							TestMobileno tm = new TestMobileno();

							if (tm.isTestphone(mobileno)) {
								
							} else {
								// 개발서버일 경우에는 SMS를 다른 폰으로 쏜다. ===================
								if ((System.getProperty("argEnv")).equals("dev"))  mobileno = itfMgr.getTestMobileno();
								// ===============================================================
							}
							
							JSONObject json = smt.subscribeMobiletownOtp(mobileno, rnumber);
							if (200 == (int) json.get("code")) {
								json.put("code", 200);
								json.put("msg", "");
							} else {
								json.put("code", (int) json.get("code"));
								json.put("msg", json.getAsString("msg"));
							}
							
							return json;
						});
					});

				});
				
				path("/cancel", ()-> {
					// 사용자의 해지요청 처리
					post("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();

						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						logger.info(jsonObject.toJSONString());
						Subscribe data = JSONValue.parse(req.body(), Subscribe.class);
						logger.info(data.toString());
						
						List<Subscribe> targets = service.getSubscribeByMobileno(data);
						if (targets != null && targets.size()>0) {
							
							// ========================================================
							// 통신사로 부가서비스 해지요청 -- START
							// ========================================================
							
							if ("SKT".equals(data.getSpcode())) { // 통신사 코드로 분기처리
								SubscribeSK skt = new SubscribeSK();
								responseJSON = skt.cancel(data);
								
							} else if ("KT".equals(data.getSpcode())) {
								
							} else if ("LGU".equals(data.getSpcode())) {
								
							} else { // 통신사 코드에 이상것이 들어왔을 때 
								return JSONHelper.assembleResponse(997, "통신사를 선택하여야 합니다.[997]");
							}
							// 정상완료가 되었다면 code=200 이어야 함
							if ((int) responseJSON.get("code") != 200) 
								return responseJSON;
							
							// ========================================================
							// 통신사로 부가서비스 해지요청 -- END
							// ========================================================
							
							// ========================================================
							// 부가서비스 제공사로 가입요청 -- START
							// ========================================================
							SubscribeNaru naru = new SubscribeNaru();
							responseJSON = naru.cancel(data);
							if ((int) responseJSON.get("code") != 200) 
								return responseJSON;
							// ========================================================
							// 부가서비스 제공사로 가입요청 -- END
							// ========================================================
							

							// ========================================================
							// 자체 DB 저장 -- START
							// ========================================================
							
							if ((int) responseJSON.get("code") == 200) {
								try {
									int dcnt = 0;
									for (Subscribe d: targets) {
										service.deleteSubscribe(d.getId()); dcnt++;
										logger.info("[{}] Cancel Service - ID:{} | MOBILENO:{}", dcnt, d.getId(), d.getMobileno());
									}
									res.status(201);
									code = 200;
									msg = "정상 해지";
									
								} catch (Exception e) {
									e.printStackTrace();
									code = 999;
									msg = "해지 중 오류가 발생하였습니다. [999]";
								}
							} else {
								code = (int) responseJSON.get("code");
								msg = responseJSON.getAsString("msg");
							}
							
							// ========================================================
							// 자체 DB 저장 -- END
							// ========================================================
							
						} else {
							code = 912;
							msg = "가입정보를 찾을 수 없습니다.[912]";
						}

						return JSONHelper.assembleResponse(code, msg);
					});
					
					path("/mobiletown", ()-> {
						// 해지시 SMS 문자발송 
						post("/sendsms", (req, res) -> {
							int code = 999;
							String msg = "";
							
							JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
							logger.info(jsonObject.toJSONString());
							
							String mobileno = jsonObject.getAsString("mobileno");
							Subscribe data = Subscribe.builder().mobileno(mobileno).build();
							
							List<Subscribe> targets = service.getSubscribeByMobileno(data);
							if (targets != null && targets.size()>0) {
								SubscribeMobiletown smt = new SubscribeMobiletown();
								JSONObject json = new JSONObject();
								TestMobileno tm = new TestMobileno();

								if (tm.isTestphone(mobileno)) {
									// 특정 핸드폰의 경우에는 SMS를 발송하지 않고 저장함
									json = smt.cancelMobiletownPseudo(mobileno, data.getSpuserid());
									
								} else {
									// 개발서버일 경우에는 SMS를 다른 폰으로 쏜다. ===================
									if ((System.getProperty("argEnv")).equals("dev"))  mobileno = itfMgr.getTestMobileno();
									// ===============================================================
									json = smt.cancelMobiletown(mobileno, data.getSpuserid());
								}
								if (200 == (int) json.get("code")) {
									code = 200;
									msg = "";
								} else {
									code = (int) json.get("code");
									msg = json.getAsString("msg");
								}
							} else {
								code = 912;
								msg = "가입정보를 찾을 수 없습니다.[912]";
							}
							return JSONHelper.assembleResponse(code, msg);
						});
						
						//
						post("/checkotp", (req, res) -> {
							int code = 999;
							String msg = "";
							
							JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
							logger.info(jsonObject.toJSONString());
							
							String mobileno = jsonObject.getAsString("mobileno");
							String rnumber = jsonObject.getAsString("rnumber");
							
							SubscribeMobiletown smt = new SubscribeMobiletown();
							JSONObject json = new JSONObject();
							TestMobileno tm = new TestMobileno();

							if (tm.isTestphone(mobileno)) {
								// 특정 핸드폰의 경우에는 SMS를 발송하지 않고 저장함
								
							} else {
								// 개발서버일 경우에는 SMS를 다른 폰으로 쏜다. ===================
								if ((System.getProperty("argEnv")).equals("dev"))  mobileno = itfMgr.getTestMobileno();
								// ===============================================================
							}
							json = smt.cancelMobiletownOtp(mobileno, rnumber);

							if (200 == (int) json.get("code")) {
								json.put("code", 200);
								json.put("msg", "");
							} else {
								json.put("code", (int) json.get("code"));
								json.put("msg", json.getAsString("msg"));
							}
							
							return json;
						});
					});
				});
				
				
			});
		});
	}
}
