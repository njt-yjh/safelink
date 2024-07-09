package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.model.Data;
import com.dsqd.amc.linkedmo.service.DataService;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

import static spark.Spark.*;

public class DataController {
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	private DataService dataService = new DataService();

	public DataController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
		path("/api", () -> {
			path("/v1.0", () -> {
				path("/data", () -> {
					// Get data by ID
					get("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						Data data = dataService.getDataById(id);
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

					// Insert new data
					post("", (req, res) -> {
						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						logger.info(jsonObject.toJSONString());
						Data data = new Data();
						data.parse(jsonObject);
						dataService.insertData(data);
						logger.info("Data inserted: {}", data);
						res.status(201);
						return data.toJSONString();
					});

					// Update existing data
					put("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
						Data data = new Data();
						data.setId(id);
						data.parse(jsonObject);
						dataService.updateData(data);
						logger.info("Data updated for ID: {}", id);
						res.status(200);
						return data.toJSONString();
					});

					// Delete data by ID
					delete("/:id", (req, res) -> {
						int id = Integer.parseInt(req.params(":id"));
						dataService.deleteData(id);
						logger.info("Data deleted for ID: {}", id);
						res.status(204);
						return "";
					});
				});
			});
		});
	}
}
