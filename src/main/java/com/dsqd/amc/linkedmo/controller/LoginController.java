package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.model.Manager;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.service.LoginService;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

import static spark.Spark.*;

public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private LoginService loginService = new LoginService();

	public LoginController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
		path("/api", () -> {
			path("/v1.0", () -> {
				path("/login", () -> {

					// login
					post("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();

						Manager data = JSONValue.parse(req.body(), Manager.class);
						Manager logined = loginService.login(data);
						logger.info("Login try : {} => {}", data, logined);
						res.status(200);
						
						if (logined !=null && data.getUsername().equals(logined.getUsername())) {
							code = 200;
							msg = "LOGIN SUCCESS";
						} else {
							code = 402;
							msg = "NOT MATCHED LOGIN ID and PASSWORD";
						}
						// 문제가 없다면 정상코드 제공
						responseJSON.put("code", code);
						responseJSON.put("msg", msg);

						return responseJSON.toJSONString();
					});

					// Update existing data
					put("", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();
						
						Manager data = JSONValue.parse(req.body(), Manager.class);						
						Manager changed = loginService.changePassword(data);
						
						if (changed != null && data.getPassword().equals(changed.getPassword())) {
							logger.info("Password updated for username: {}", data.getUsername());
							res.status(200);
							code = 200;
							msg = "PASSWORD CHANGE SUCCESS";
						} else {
							code = 406;
							msg = "FAILED CHANGE PASSWORD";
						}
						
						return responseJSON.toJSONString();
					});

					// Delete data by ID
					delete("/:loginid", (req, res) -> {
						int code = 999;
						String msg = "";
						JSONObject responseJSON = new JSONObject();
						
						String loginid = req.params(":loginid");
						loginService.deleteManager(loginid);
						logger.info("Manager deleted for username: {}", loginid);
						res.status(204);
						
						code = 200;
						msg = "DELETED MANAGER";
						
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
