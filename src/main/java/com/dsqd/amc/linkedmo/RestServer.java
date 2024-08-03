package com.dsqd.amc.linkedmo;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.controller.DataController;
import com.dsqd.amc.linkedmo.controller.LoginController;
import com.dsqd.amc.linkedmo.controller.SchedulerController;
import com.dsqd.amc.linkedmo.controller.SubscribeController;
import com.dsqd.amc.linkedmo.util.AES256Util;
import com.dsqd.amc.linkedmo.util.ApiExcludeList;
import com.dsqd.amc.linkedmo.util.JwtUtil;
import com.dsqd.amc.linkedmo.util.SchedulerModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import spark.Request;
import spark.Response;

public class RestServer {
	
    private static final Logger logger = LoggerFactory.getLogger(RestServer.class);

	public static void main(String [] args) {
		
		String env = "local";
        if (args.length != 1) {
            System.err.println("Usage: java -jar RestServer <env>");
            System.err.println("   Running with local");
            //System.exit(1);
        } else {
        	env = args[0];
        }

        MyBatisConfig.init(env);
        
        logger.info("Starting the REST server... [" + env + "]");

//		staticFiles.externalLocation("/Users/eunjun/Documents/dsqf/AMCProject/public2"); // Static files
		staticFiles.externalLocation("C:\\Users\\silve\\git\\safelink\\public"); // Static files
		
		int port = 5000;
		port(port);
		
		before((request, response) -> {
            String path = request.pathInfo();
            String clientIP = request.ip();
            logger.info("ip:{} - path:{}", clientIP, path);
            
            if ("127.0.0.1".equals(clientIP) || "0:0:0:0:0:0:0:1".equals(clientIP) || ApiExcludeList.isExcluded(path)) {
                return; // 특정 API와 파일확장자는 JWT 검증을 제외
            }

            String token = request.headers("Authorization");
            if (token == null || token.isEmpty()) {
                //response.redirect("/login.html");
                //halt();
                halt(401, "You are not authorized");
            }
            
            // TEST용 *********************************** 
            System.out.println(token);
            if (token.equals("Bearer admin")) return;
            // *****************************************
            
            try {
                DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
                //String clientIP = request.ip();
                String tokenClientIP = JwtUtil.getClientIP(decodedJWT);
                String tokenUsername = JwtUtil.getUsername(decodedJWT);

                if (!clientIP.equals(tokenClientIP)) {
                    halt(401, "Token IP mismatch");
                }

                // Token is valid, generate new token
                String newToken = JwtUtil.createToken(clientIP, tokenUsername);
                response.header("Authorization", "Bearer " + newToken);
            } catch (JWTVerificationException e) {
                halt(401, "Token is not valid or IP mismatch");

            } catch (Exception e) {
                halt(401, "Unexpected situation");
            }
        });
		
		get("/hello", (req, res) -> "Hello World");
		post("/hello/:USERID", (req, res) -> getUser(req, res));

		//post("/api/v1.0/subscribe", (req, res) -> subscribe(req, res));
		get("/exit", (req, res) -> exitTest() );
		
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
		
		SchedulerModule.startScheduler();
		
		// root is 'src/main/resources', so put files in 'src/main/resources/public'
		// :DAPAID", (req, res) -> serviceSSO(req, res)
		
	}
	
	private static String exitTest() {
		stop();
		return "Exit";	
	}
	
	private static String subscribe(Request req, Response res) {
		JSONObject json = new JSONObject();
    	JSONParser parser = new JSONParser();
    	try {
			json = (JSONObject) parser.parse(req.body());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(json.toJSONString());
    	
    	Map map = setMap(req);
    	
    	JSONObject retObj = new JSONObject();
    	retObj.put("code", 200);
    	retObj.put("message", "OK");

    	return retObj.toJSONString();
	}
	
	private static String getUser(Request req, Response res) {
    	String userid = req.params(":USERID");
    	System.out.println("Get Body" + req.body());
    	JSONObject json = new JSONObject();
    	JSONParser parser = new JSONParser();
    	try {
			json = (JSONObject) parser.parse(req.body());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(json.toJSONString());
    	
    	Map map = setMap(req);
    	
    	return "{\"data\":\"success - " + userid + "\"}";
	}
	
	private static Map setMap(Request req) {
    	Map map = new HashMap();
    	Iterator itor = req.params().keySet().iterator();
    	while (itor.hasNext()) {
    		String key = (String) itor.next();
    		String value = req.params(key);
    		map.put(key, value);
    		System.out.println("set params**************" + key + ":" + value);
    	}
    	
    	Iterator itor2 = req.queryParams().iterator();
    	while (itor2.hasNext()) {
    		String key = (String) itor2.next();
    		String value = req.queryParams(key);
    		map.put(key, value);
    		System.out.println("set queryParams ********" + key + ":" + value);
    	}
    	return map;
	}
	
}
