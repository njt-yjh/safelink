package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.path;
import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.mobiletown.SubscribeMobiletown;
import com.dsqd.amc.linkedmo.util.JSONHelper;
import com.dsqd.amc.linkedmo.util.JwtUtil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class PartnerController {
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);

	public PartnerController() {
		setupEndpoints();
	}
	private void setupEndpoints() {
		path("/api", () -> {
			path("/v1.0", () -> {
				path("/partner", () -> {
					path("/reqsms", () -> {
						post("", (req, res) -> {
							
							// 헤더에서 Bearer Token 검증 
							String Authorization = req.headers("Authorization");
							String token = Authorization.substring(7);
							//logger.info("token : {}", token);
							String env = System.getProperty("argEnv");
							//String env = "dev";
							boolean ver = JwtUtil.verifyNaruToken(token, "naru-"+env);
							if (!ver) {
								logger.error("JWT Verified ERROR : {}", token);
								return JSONHelper.assembleResponse(951, "토큰 검증시 문제가 발견되어, 발송하지 못하였습니다.");
							}
							
							int code = 999;
							String msg = "";
							
							JSONObject obj = (JSONObject) JSONValue.parse(req.body());
							String spcode = (String) obj.getOrDefault("spcode", "00");
							String mobileno = (String) obj.getOrDefault("mobileno", "");
							String offercode = (String) obj.getOrDefault("offercode", "00");
							String type = (String) obj.getOrDefault("type", "000");

							logger.info("NOTICE FROM {} : {}", offercode,  obj.toJSONString());
							
							SubscribeMobiletown mt = new SubscribeMobiletown();
							JSONObject resp = mt.notiMobiletown(mobileno, offercode, type);
//							JSONObject resp = new JSONObject();
//							resp.put("code", 200);
//							resp.put("msg", "");
							
							return JSONHelper.assembleResponse((int) resp.get("code"), resp.getAsString("msg"));
						});
					});
				});
			});
		});
	}
}
