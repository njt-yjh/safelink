package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.model.Manager;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.service.LoginService;
import com.dsqd.amc.linkedmo.util.JwtUtil;
import com.dsqd.amc.linkedmo.util.QRImageHelper;
import com.google.zxing.WriterException;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

import static spark.Spark.*;

public class QRImageController {
	private static final Logger logger = LoggerFactory.getLogger(QRImageController.class);
	private LoginService loginService = new LoginService();

	public QRImageController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
		get("/qr-code", (req, res) -> {
            String content = req.queryParams("content");
            res.type("image/png");
            QRImageHelper qr = new QRImageHelper();
            
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                qr.generateQRCodeImage(content, baos, 350, 350);
                byte[] imageData = baos.toByteArray();
                res.raw().getOutputStream().write(imageData);
                res.raw().getOutputStream().flush();
            } catch (WriterException we) {
                we.printStackTrace();
                res.status(500);
                return "QR 코드 생성 중 오류가 발생했습니다.";
            }
            return res.raw();
        });
	}
}
