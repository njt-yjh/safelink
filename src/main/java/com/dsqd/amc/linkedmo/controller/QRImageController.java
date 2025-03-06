package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.model.Manager;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.naru.SubscribeNaru;
import com.dsqd.amc.linkedmo.service.LoginService;
import com.dsqd.amc.linkedmo.util.JSONHelper;
import com.dsqd.amc.linkedmo.util.JwtUtil;
import com.dsqd.amc.linkedmo.util.QRImageHelper;
import com.google.zxing.WriterException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class QRImageController {
	private static final Logger logger = LoggerFactory.getLogger(QRImageController.class);
	private LoginService loginService = new LoginService();

	public QRImageController() {
		setupEndpoints();
	}

	private void setupEndpoints() {
		get("/api/v1.0/qr-code", (req, res) -> {
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
        post("/api/v1.0/getdata",(req, res) -> {
            JSONObject params = (JSONObject) JSONValue.parse(req.body());
            String phoneNumber = params.getAsString("phoneNumber");
            Subscribe s = Subscribe.builder().spcode("").mobileno(phoneNumber).build();
            SubscribeNaru sn = new SubscribeNaru();
            JSONObject retJson = sn.getData(s);
            int code = (int) retJson.get("code");

            if(code == 200 && "A".equals(retJson.get("nowstatutsut").toString())) {
                JSONArray serviceNumberJson = (JSONArray) retJson.get("servicenumber");
                // servicenumber 배열을 List<String>으로 추출
                List<String> serviceNumbers = new ArrayList<>();
                for (Object obj : serviceNumberJson) {
                    serviceNumbers.add((String) obj);
                }

                // 개발 테스트 코드
                //serviceNumbers = new ArrayList<>();
                //serviceNumbers.add("23752357");
                //serviceNumbers.add("1234");

                String inputPattern = (phoneNumber.length() > 3) ? phoneNumber.substring(3) : "";

                // 패턴 비교 결과를 저장할 리스트
                String nonMatching = null;
                String matching = null;

                // 각 service number에서 앞 3자리를 제외한 나머지와 비교
                for (String data : serviceNumbers) {
                    if (data.length() > 3) {
                        if (!data.equals(inputPattern)) {
                            // 패턴 불일치이면 nonMatching에 추가
                            nonMatching = data;
                            break;
                        } else {
                            matching = data;
                        }
                    }
                }

                // 최종 결과: 불일치 값이 있으면 해당 값들만, 없으면 일치하는 값을 사용
                String servicenumber = (nonMatching != null) ? nonMatching : matching;

                return JSONHelper.assembleResponse(200, servicenumber);
            } else {
                return JSONHelper.assembleResponse(912,"가입정보를 찾을 수 없습니다.[912]");
            }
        });
	}
}
