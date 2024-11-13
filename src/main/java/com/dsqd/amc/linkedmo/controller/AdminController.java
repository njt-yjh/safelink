package com.dsqd.amc.linkedmo.controller;

import static spark.Spark.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.service.AdminService;

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
            });
        });
    }
	
    private String getBatchAll(Request req, Response res) {
        List<Batch> batchs = adminService.getBatchAll();
        return batchs.stream()
                     .map(Batch::toJSONString)
                     .reduce("[", (acc, json) -> acc + json + ",")
                     .replaceAll(",$", "]");  // JSON Array 형식으로 반환
    }
}
