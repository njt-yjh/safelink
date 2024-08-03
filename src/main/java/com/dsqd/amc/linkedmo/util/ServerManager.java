package com.dsqd.amc.linkedmo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 서버 상태 확인, 부하 분산, 재시도, 요청 전송 등을 관리하는 클래스
 */
public class ServerManager {

    private static final int MAX_RETRIES = 3;

    protected final List<String> serverList = new CopyOnWriteArrayList<>();
    private final java.util.Map<String, Integer> serverRetries = new java.util.HashMap<>();

    /**
     * 서버 관리자 인스턴스 생성자
     * 여러 서버의 URL을 초기화한다.
     */
    public ServerManager() {
        serverList.add("https://example.com:80/server1");
        serverList.add("https://example.com:80/server2");
        // Add more servers as needed
    }

    /**
     * 랜덤으로 서버 선택
     *
     * @return 랜덤으로 선택된 서버 URL
     */
    public String getRandomServer() {
        int randomIndex = ThreadLocalRandom.current().nextInt(serverList.size());
        return serverList.get(randomIndex);
    }

    /**
     * 서버에 GET 요청을 보내고, 응답을 처리하는 메서드.
     * 서버 응답이 없거나 오류가 발생하면 재시도를 수행하며,
     * 최대 재시도 횟수를 초과하면 해당 서버를 제외한다.
     */
    public void sendRequest() {
        String selectedServer = getRandomServer();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(selectedServer).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("서버 응답 (" + selectedServer + "): " + response.toString());
                serverRetries.put(selectedServer, 0);
            } else {
                System.out.println("서버 응답 코드 (" + selectedServer + "): " + responseCode);
                System.out.println("서버 응답 없음 또는 오류 발생");

                int retries = serverRetries.getOrDefault(selectedServer, 0);
                if (retries < MAX_RETRIES) {
                    System.out.println("재시도 중 (" + retries + "회)");
                    serverRetries.put(selectedServer, retries + 1);
                    sendRequest();
                } else {
                    System.out.println("최대 재시도 횟수를 초과하여 서버를 제외합니다: " + selectedServer);
                    serverList.remove(selectedServer);
                    serverRetries.remove(selectedServer);
                }
            }
        } catch (IOException e) {
            System.out.println("서버와의 연결 중 오류가 발생했습니다. 서버가 동작하지 않을 수 있습니다.");
        }
    }
}
