package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.util.SchedulerModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class SchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    public SchedulerController() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        path("/api", () -> {
            path("/scheduler", () -> {
                post("/start", (req, res) -> {
                    if (!SchedulerModule.isRunning()) {
                        SchedulerModule.startScheduler();
                        logger.info("Scheduler start request received.");
                        res.status(200);
                        return "Scheduler started.";
                    } else {
                        logger.warn("Scheduler start request received, but scheduler is already running.");
                        res.status(409);
                        return "Scheduler is already running.";
                    }
                });

                post("/stop", (req, res) -> {
                    if (SchedulerModule.isRunning()) {
                        SchedulerModule.stopScheduler();
                        logger.info("Scheduler stop request received.");
                        res.status(200);
                        return "Scheduler stopped.";
                    } else {
                        logger.warn("Scheduler stop request received, but scheduler is not running.");
                        res.status(409);
                        return "Scheduler is not running.";
                    }
                });
            });
        });
    }
}
