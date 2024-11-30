package com.dsqd.amc.linkedmo;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.stop;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dsqd.amc.linkedmo.batch.DataCleanser;
import com.dsqd.amc.linkedmo.buzzvil.Event12;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.controller.AdminController;
import com.dsqd.amc.linkedmo.controller.BoardController;
import com.dsqd.amc.linkedmo.controller.DataController;
import com.dsqd.amc.linkedmo.controller.EventController;
import com.dsqd.amc.linkedmo.controller.LoginController;
import com.dsqd.amc.linkedmo.controller.PartnerController;
import com.dsqd.amc.linkedmo.controller.SchedulerController;
import com.dsqd.amc.linkedmo.controller.SubscribeController;
import com.dsqd.amc.linkedmo.mobiletown.mobiletownSMS;
import com.dsqd.amc.linkedmo.model.Mobiletown;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.naru.SubscribeNaru;
import com.dsqd.amc.linkedmo.service.AdminService;
import com.dsqd.amc.linkedmo.service.MobiletownService;
import com.dsqd.amc.linkedmo.skt.APICall;
import com.dsqd.amc.linkedmo.util.AES256Util;
import com.dsqd.amc.linkedmo.util.ApiExcludeList;
import com.dsqd.amc.linkedmo.util.InterfaceManager;
import com.dsqd.amc.linkedmo.util.JSONHelper;
import com.dsqd.amc.linkedmo.util.JwtUtil;
import com.dsqd.amc.linkedmo.util.SchedulerModule;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import spark.Request;
import spark.Response;

public class RestServer {
	
    private static final Logger logger = LoggerFactory.getLogger(RestServer.class);
    private static String env;
    
	public static void main(String [] args) {
		
		env = "local";
        if (args.length != 1) {
            System.err.println("Usage: java -jar RestServer <env>");
            System.err.println("   Running with local");
            System.exit(1);
        } else {
        	env = args[0];
        	System.setProperty("argEnv", env);
        }
        logger.info("");
        logger.info("");
        logger.info("===========================================================");
        logger.info("Starting the REST server... [" + env + "]");
        logger.info("===========================================================");
        logger.info("");

        MyBatisConfig.init(env);
        logger.info("===========================================================");

        if ("local".equals(env)) {
//		staticFiles.externalLocation("/Users/eunjun/Documents/dsqf/AMCProject/public2"); // Static files
        	staticFiles.externalLocation("C:\\Users\\silve\\git\\safelink\\public2"); // Static files
        }
        
        GlobalCache cache = GlobalCache.getInstance();
        Properties properties = new Properties();
        try {
			properties.load(Resources.getResourceAsStream("application.properties"));
			cache.initWithProperties(properties);
			//cache.logCacheContents();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("FAIL Initialize GlobalCache : {}", e.getMessage());
		}
        
		int port = 5000;
		port(port);
		
		before((request, response) -> {
            String path = request.pathInfo();
            String clientIP = request.ip();
            logger.info("ip:{} - path:{}", clientIP, path);
            
//            if ("127.0.0.1".equals(clientIP) || "0:0:0:0:0:0:0:1".equals(clientIP) || ApiExcludeList.isExcluded(path)) {
//                return; // 특정 API와 파일확장자는 JWT 검증을 제외
//            }
            
            if (ApiExcludeList.isExcluded(path)) {
            	logger.debug("excluded path : {}", path);
                return; // 특정 API와 파일확장자는 JWT 검증을 제외
            }
            

            String token = request.headers("Authorization");
            logger.debug("REQEUST AUTHORIZATION : {}", token);
            if (token == null || token.isEmpty()) {
                //response.redirect("/login.html");
                //halt();
                halt(401, "You are not authorized");
            } else {
            	// Bearer Token 추출 (JWT)
            	if (token.length()>10) {
            		token = token.substring(7);
            	} else {
            		logger.error("WRONG TOKEN : []", token);
            	}
            }
            
            try {
                DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
                //String clientIP = request.ip();
                String tokenClientIP = JwtUtil.getClientIP(decodedJWT);
                String tokenUsername = JwtUtil.getUsername(decodedJWT);
                String tokenKorname = JwtUtil.getKorname(decodedJWT);

                if (!clientIP.equals(tokenClientIP)) {
                    halt(401, "Token IP mismatch");
                }

                // Token is valid, generate new token
                String newToken = JwtUtil.createToken(clientIP, tokenUsername, tokenKorname);
                response.header("Authorization", "Bearer " + newToken);
            } catch (JWTVerificationException e) {
                halt(401, "Token is not valid or IP mismatch");

            } catch (Exception e) {
                halt(401, "Unexpected situation");
            }
        });
		
		//get("/api/v1.0/test/summarynet", (req, res) -> adminSummaryNet(req, res));
		
		get("/api/v2.0/test/buzzvil/:mobileno/:bz_tracking_id", (req, res) -> testBuzzvil(req, res));
		
		get("/api/v2.0/test/skt", (req, res) -> "Usage /api/v2.0/test/01012341234");
		
		get("/api/v2.0/test/skt/:mobileno", (req, res) -> ISICS00021(req, res));
		get("/api/v2.0/test/skt/:mobileno/on", (req, res) -> ISSWG00047_on(req, res));
		get("/api/v2.0/test/skt/:mobileno/off", (req, res) -> ISSWG00047_off(req, res));
		get("/api/v2.0/test/skt/:mobileno/status", (req, res) -> ISICS00022(req, res));
		
		get("/api/v2.0/test/naru/:spcode/:mobileno", (req, res) -> adminNaruGetData(req, res));
		get("/api/v2.0/test/naru/:spcode/:mobileno/on", (req, res) -> adminNaruSubscribe(req, res));
		get("/api/v2.0/test/naru/:spcode/:mobileno/off", (req, res) -> adminNaruCancel(req, res));
		
		get("/api/v2.0/test/sms/change/:mobileno", (req, res) -> changeTestmobileno(req, res));
		get("/api/v2.0/test/sms/:mobileno", (req, res) -> mobiletown(req, res));
		get("/api/v2.0/test/sms/:mobileno/:rnumber", (req, res) -> mobiletownOtp(req, res));

		try {
			String encdata = AES256Util.encrypt("safelinkd&07", "12345678901234567890123456789012");
			//System.out.println("AES : " + encdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//DataController.main(args);
		new DataController();
		new SubscribeController();
		new LoginController();
		new SchedulerController();
		new BoardController();
		new AdminController();
		new PartnerController();
		new EventController();
		
		SchedulerModule.startScheduler();
		
	}
	
	
	
	// 테스트용
	
	private static String adminSummaryNet(Request req, Response res) {
		// Admin Controller로 이동되었음 
		return "";
	}
	
	private static String adminNaruGetData(Request req, Response res) {
		String spcode = req.params(":spcode");
		String mobileno = req.params(":mobileno");
		Subscribe s = Subscribe.builder().spcode(spcode).mobileno(mobileno).build();
		SubscribeNaru sn = new SubscribeNaru();
		JSONObject retJson = sn.getData(s);
		return retJson.toJSONString();
	}
	
	private static String adminNaruCancel(Request req, Response res) {
		String spcode = req.params(":spcode");
		String mobileno = req.params(":mobileno");
		Subscribe s = Subscribe.builder().spcode(spcode).mobileno(mobileno).build();
		SubscribeNaru sn = new SubscribeNaru();
		JSONObject retJson = sn.cancel(s);
		// 직접 DB에 내용을 기입해야 함
		return retJson.toJSONString();
	}
	
	private static String adminNaruSubscribe(Request req, Response res) {
		String spcode = req.params(":spcode");
		String mobileno = req.params(":mobileno");
		Subscribe s = Subscribe.builder().spcode(spcode).mobileno(mobileno).build();
		SubscribeNaru sn = new SubscribeNaru();
		JSONObject retJson = sn.subscribe(s);
		// 직접 DB에 내용을 기입해야 함
		return retJson.toJSONString();
	}

	private static String testBuzzvil(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		String bz_tracking_id = req.params(":bz_tracking_id");
		String eventcd = "119577650586064";

		Event12 event = new Event12();
        
		event.buzzSafelink(mobileno, bz_tracking_id, mobileno, eventcd, "S2S");
		
		return "testBuzzvil : " + mobileno;
	}
	
	private static String changeTestmobileno(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		InterfaceManager itfMgr = InterfaceManager.getInstance();
		itfMgr.setTestMobileno(mobileno);
		return "change testmobileno : " + mobileno;
	}
	
	private static String mobiletown(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		mobiletownSMS api = new mobiletownSMS();
		String rnumber = api.genRandoms();
		String message = api.setMessage1(rnumber);
		JSONObject resp = api.sendSms(mobileno, message);
		int code = (int) resp.get("code");
		if (code == 200) {
			MobiletownService service = new MobiletownService();
			Mobiletown mt = Mobiletown.builder().purpose("S")
									  .spuserid("0000000000")
									  .rcverkey("1").rcverphone(mobileno)
									  .content(message).rnumber(rnumber)
									  .result(resp.toJSONString())
									  .checkcode("T")
									  .build();
			service.insertMobiletown(mt); 
		}
		return "SEND SMS by MOBILETOWN <br/>" + resp;
	}
	
	private JSONObject subscribeMobiletown(String mobileno, String spuserid) {
		return mobiletown("S", mobileno, spuserid);
	}
	
	private JSONObject cancelMobiletown(String mobileno, String spuserid) {
		return mobiletown("C", mobileno, spuserid);
	}
	
	private JSONObject mobiletown(String purpose, String mobileno, String spuserid) {
		JSONObject retObj = new JSONObject();
		String msg = "";
		mobiletownSMS api = new mobiletownSMS();
		String rnumber = api.genRandoms();
		String message = api.setMessage1(rnumber);
		JSONObject resp = api.sendSms(mobileno, message);
		int code = (int) resp.get("code");
		if (code == 200) {
			MobiletownService service = new MobiletownService();
			Mobiletown mt = Mobiletown.builder().purpose(purpose)
									  .spuserid(spuserid)
									  .rcverkey("1").rcverphone(mobileno)
									  .content(message).rnumber(rnumber)
									  .result(resp.toJSONString())
									  .checkcode("T")
									  .build();
			service.insertMobiletown(mt); 
		}
		retObj.put("code", code);
		retObj.put("msg", "");
		return retObj;
	}
	
	private JSONObject subscribeMobiletownOtp(String mobileno, String rnumber) {
		return mobiletownOtp("S", mobileno, rnumber);
	}
	
	private JSONObject cancelMobiletownOtp(String mobileno, String rnumber) {
		return mobiletownOtp("C", mobileno, rnumber);
	}
	
	private static JSONObject mobiletownOtp(String purpose, String mobileno, String rnumber) {
		if (rnumber == null || "".equals(rnumber)) {
			return JSONHelper.assembleResponse(3109, "인증번호 입력 후 다시 해주세요.");
		}
		
		MobiletownService service = new MobiletownService();
		int code = 200;
		String msg = "";
		
		List <Mobiletown> lst = service.getMobiletownByMobileid(mobileno);
		if (lst != null && lst.size()>0) {
			Mobiletown mt = lst.get(0); // 무조건 첫번째거만

			// 시간체크
			Date send = mt.getSendtime();
			int errcnt = mt.getErrcnt();
			
			long currentTimeMillis = System.currentTimeMillis();
			long sendTimeMillls = send.getTime();
			long threeMinutesMillis = 3 * 60 * 1000 + 30 * 1000;
			if ("S".equals(mt.getCheckcode())) {
				code = 3102;
				msg= "인증번호 발송 후 검증요청을 해주세요."; 
			} else if (currentTimeMillis - sendTimeMillls > threeMinutesMillis) { 
				code = 3103;
				msg= "인증번호 검증시간은 발송 후 3분입니다. 다시 인증번호를 발송하세요."; 
			} else if (errcnt > 3) {
				code = 3104;
				msg= "인증번호 검증시도 5회 초과입니다. 다시 인증번호를 발송하세요."; 
			} else if (!purpose.equals(mt.getPurpose())) {
				code = 3105;
				msg= "인증번호 발송 후 검증요청을 해주세요.[3105]"; 
			} else {
				String rnumber_send = mt.getRnumber();
				if (rnumber.equals(rnumber_send)) {
					mt.setCheckcode("S");
					service.updateMobiletown(mt);
					code = 200;
					msg= "인증번호를 확인했습니다.["+rnumber+"]"; 
				} else {
					mt.setErrcnt(errcnt++);
					service.updateMobiletown(mt);
					code = 3101;
					msg= "인증번호가 틀립니다.[" + errcnt + "회] 확인 후 다시 입력해보세요."; 
				}
			}
			
		} else {
			code = 3106;
			msg= "인증번호 발송 후 검증요청을 해주세요."; 
		}
				
		return JSONHelper.assembleResponse(code, msg);
	}
	
	
	private static String mobiletownOtp(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		String rnumber = req.params(":rnumber");
		if (rnumber == null || "".equals(rnumber)) {
			return "인증번호를 입력하십시오. /api/1.0/testsms/{전화번호}/{인증번호}";
		}
		
		MobiletownService service = new MobiletownService();
		String ret = "";
		
		List <Mobiletown> lst = service.getMobiletownByMobileid(mobileno);
		if (lst != null && lst.size()>0) {
			Mobiletown mt = lst.get(0); // 무조건 첫번째거만
			
			
			// 시간체크
			Date send = mt.getSendtime();
			int errcnt = mt.getErrcnt();
			
			long currentTimeMillis = System.currentTimeMillis();
			long sendTimeMillls = send.getTime();
			long threeMinutesMillis = 3 * 60 * 1000 + 30 * 1000;
			if ("S".equals(mt.getCheckcode())) {
				ret= "인증번호 발송 후 검증요청을 해주세요."; 
			} else if (currentTimeMillis - sendTimeMillls > threeMinutesMillis) { 
				ret= "인증번호 검증시간은 발송 후 3분입니다. 다시 인증번호를 발송하세요."; 
			} else if (errcnt > 3) {
				ret= "인증번호 검증시도 5회 초과입니다. 다시 인증번호를 발송하세요."; 
			} else {
				String rnumber_send = mt.getRnumber();
				if (rnumber.equals(rnumber_send)) {
					mt.setCheckcode("S");
					service.updateMobiletown(mt);
					ret= "인증번호를 확인했습니다.["+rnumber+"]"; 
				} else {
					mt.setErrcnt(errcnt++);
					service.updateMobiletown(mt);
					ret= "인증번호가 틀립니다.[" + errcnt + "회] 확인 후 다시 입력해보세요."; 
				}
			}
			
		} else {
			ret= "인증번호 발송 후 검증요청을 해주세요."; 
		}
				
		return "CHECK OTP by MOBILETOWN <br/>" + ret;
	}
	
	private static String ISICS00021(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		APICall api = new APICall(env);
		String resp = api.ISICS00021(mobileno);
		return resp;
	}
	
	private static String ISICS00022(Request req, Response res) {
		String mobileno = req.params(":mobileno");
		APICall api = new APICall(env);
		String resp = api.ISICS00022(mobileno);
		return resp;
	}
	
	private static String ISSWG00047(Request req, Response res) {
		APICall api = new APICall(env);
		
		// Map을 복제함 
		Map<String, String> param = new HashMap<String, String>();
		for (String key: req.queryParams()) {
			param.put(key, (String) req.queryParams(key));
		}
		
		// 테스트용 파라미터값 등록 
		String mobileno = req.params(":mobileno");
		if (mobileno != null && !"".equals(mobileno)) {
			param.put("mobileno", mobileno);
		}
		
		// 추가적으로 attribute에 action이 있는지 확인하여 Map에 추가함 
		String action = req.attribute("action");
		if ("subscribe".equals(action) || "cancel".equals(action)) {
			param.put("action", action);
		}
		
		String resp = api.ISSWG00047(param);
		return resp;
	}
	
	private static String ISSWG00047_on(Request req, Response res) {
		req.attribute("action", "subscribe");
		return ISSWG00047(req, res);
	}
	
	private static String ISSWG00047_off(Request req, Response res) {
		req.attribute("action", "cancel");
		return ISSWG00047(req, res);
	}
	
	private static String dataclean(Request req, Response res) {
		DataCleanser dc = new DataCleanser();
		dc.batch();
		return "OK";
	}
	
	
}
