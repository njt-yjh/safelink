package com.dsqd.amc.linkedmo.util;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.controller.DataController;

/**
 * 서버에 요청을 보내는 클래스를 분리하여 작성.
 */
public class RequestSender {

	private static final Logger logger = LoggerFactory.getLogger(RequestSender.class);

    private final InterfaceManager interfaceManager;
    private final ConcurrentHashMap<String, AtomicInteger> serverLoadMap;
    private final ConcurrentHashMap<String, Integer> serverSuccessMap;
    private final ConcurrentHashMap<String, Integer> serverFailureMap;

    public RequestSender(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
        this.serverLoadMap = interfaceManager.getServerLoadMap();
        this.serverSuccessMap = interfaceManager.getServerSuccessMap();
        this.serverFailureMap = interfaceManager.getServerFailureMap();
    }

    /**
     * 서버에 요청을 보내는 메서드.
     *
     * @param method    HTTP 메서드 (GET, POST, PUT 등)
     * @param uri       요청 URI
     * @param parameter 요청 파라미터
     * @return 서버 응답
     * @throws Exception 요청 중 발생한 예외
     */
    public String sendRequest(String method, String uri, String parameter) throws Exception {
        List<String> activeServerList = interfaceManager.getActiveServerList();
        if (activeServerList.isEmpty()) {
        	logger.error("{}", "모든 서버가 비활성 상태입니다. 소켓이 연결되지 않습니다.");
            throw new Exception("모든 서버가 비활성 상태입니다. 소켓이 연결되지 않습니다.");
        }

        String response = null;
        String server = interfaceManager.getLeastLoadedServer();
        long startTime = System.currentTimeMillis();
        logger.info("서버 요청 정보: 서버=" + server + "[" + serverLoadMap.getOrDefault(server, new AtomicInteger(0)).get() + "], 메서드=" + method + ", URI=" + uri + ", 파라미터=" + parameter);
        try {
            // 서버의 현재 로드 증가
            serverLoadMap.get(server).incrementAndGet();

            URL url = new URL(server + uri);
            logger.debug("URL : {}", url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty ("Authorization", "Bearer "+interfaceManager.getNaruJWT());

            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                connection.setDoOutput(true);
                connection.getOutputStream().write(parameter.getBytes("UTF-8"));
            } else if ("GET".equalsIgnoreCase(method)) {
                JSONObject json = (JSONObject) JSONValue.parse(parameter);
                StringBuilder params = new StringBuilder();
                for (String key : json.keySet()) {
                    if (params.length() != 0) params.append('&');
                    params.append(key).append('=').append(json.getAsString(key));
                }
                connection = (HttpURLConnection) new URL(server + uri + "?" + params.toString()).openConnection();
            }

            int responseCode = connection.getResponseCode();
            long endTime = System.currentTimeMillis();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                response = content.toString();
                logger.info("서버 응답 정보: 서버=" + server + ", 응답시간: " + (endTime - startTime) + "ms, 응답내용: " + response);
                serverSuccessMap.put(server, serverSuccessMap.getOrDefault(server, 0) + 1);
            } else {
                serverFailureMap.put(server, serverFailureMap.getOrDefault(server, 0) + 1);
                logger.error("서버가 비정상 상태입니다. 응답 코드: {}", responseCode);
                throw new IOException("서버가 비정상 상태입니다. 응답 코드: " + responseCode);
            }
        } catch (IOException e) {
        	logger.error("서버 요청 중 IOException 발생: " + e.getMessage());
            System.err.println("서버 요청 중 IOException 발생: " + e.getMessage());
            activeServerList.remove(server);
            serverFailureMap.put(server, serverFailureMap.getOrDefault(server, 0) + 1);
            throw e;
        } catch (Exception e) {
        	logger.error("서버 요청 중 오류 발생: " + e.getMessage());
            System.err.println("서버 요청 중 오류 발생: " + e.getMessage());
            activeServerList.remove(server);
            serverFailureMap.put(server, serverFailureMap.getOrDefault(server, 0) + 1);
            throw e;
        } finally {
            // 서버의 현재 로드 감소
            serverLoadMap.get(server).decrementAndGet();
        }
        return response;
    }
}
