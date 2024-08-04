package com.dsqd.amc.linkedmo.util;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class SendRequestTask implements Callable<String> {

    private final ServerManager serverManager;
    private final String method;
    private final String uri;
    private final String parameter;

    public SendRequestTask(ServerManager serverManager, String method, String uri, String parameter) {
        this.serverManager = serverManager;
        this.method = method;
        this.uri = uri;
        this.parameter = parameter;
    }

    @Override
    public String call() throws Exception {
        while (serverManager.isHealthCheckInProgress()) {
            System.out.println("초기 헬스 체크가 완료될 때까지 대기 중...");
            Thread.sleep(100); // 0.1초 대기 후 다시 확인
        }

        if (serverManager.getActiveServerList().isEmpty()) {
            throw new Exception("모든 서버가 비활성 상태입니다. 소켓이 연결되지 않습니다.");
        }

        String response = null;
        String server = serverManager.getLeastLoadedServer();
        long startTime = System.currentTimeMillis();
        System.out.println("서버 요청 정보: 서버=" + server + "[" + serverManager.getServerLoad(server) + "], 메서드=" + method + ", URI=" + uri + ", 파라미터=" + parameter);
        try {
            // 서버의 현재 로드 증가
            serverManager.incrementServerLoad(server);

            URL url = new URL(server + uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

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
                System.out.println("서버 응답 정보: 서버=" + server + ", 응답시간: " + (endTime - startTime) + "ms, 응답내용: " + response);
                serverManager.incrementServerSuccessCount(server);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("서버 응답 코드: " + responseCode + ", 서버=" + server);
            } else {
                throw new IOException("서버가 비정상 상태입니다. 응답 코드: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("서버 요청 중 IOException 발생: " + e.getMessage());
            serverManager.incrementServerFailureCount(server);
            serverManager.removeActiveServer(server);
            throw e;
        } catch (Exception e) {
            System.err.println("서버 요청 중 오류 발생: " + e.getMessage());
            serverManager.incrementServerFailureCount(server);
            serverManager.removeActiveServer(server);
            throw e;
        } finally {
            // 서버의 현재 로드 감소
            serverManager.decrementServerLoad(server);
        }
        return response;
    }
}
