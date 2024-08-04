package com.dsqd.amc.linkedmo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 서버의 상태를 주기적으로 체크하는 스레드 클래스.
 */
public class ServerHealthChecker extends Thread {

    private final ServerManager serverManager;

    public ServerHealthChecker(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        try {
            while (serverManager.isHealthCheckInProgress()) {
                for (String server : serverManager.getInitialServerList()) {
                    try {
                        URL url = new URL(server + "/health");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("HEAD");

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            if (!serverManager.getActiveServerList().contains(server)) {
                                serverManager.getActiveServerList().add(server);
                                System.out.println(server + " 서버가 활성화되었습니다.");
                            }
                        } else if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                            System.out.println(server + " 서버가 비정상 상태이지만 304 또는 404 응답 코드입니다.");
                        } else {
                            System.out.println(server + " 서버가 비정상 상태입니다. 응답 코드: " + responseCode);
                            serverManager.removeActiveServer(server);
                        }
                    } catch (IOException e) {
                        System.out.println(server + " 서버와의 연결에 실패했습니다: " + e.getMessage());
                        serverManager.removeActiveServer(server);
                    }
                }
                
                serverManager.setHealthCheckInProgress(false);
                
                if (serverManager.getActiveServerList().isEmpty()) {
                    System.out.println("모든 서버가 비활성 상태입니다. 10초 후 다시 시도합니다.");
                    Thread.sleep(10000); // 10초 대기 후 다시 헬스 체크
                } else {
                    System.out.println("헬스 체크 완료. 5분 후 다시 시도합니다.");
                    Thread.sleep(300000); // 5분 대기 후 다시 헬스 체크
                }
            }
        } catch (InterruptedException e) {
            System.out.println("헬스 체크가 중단되었습니다: " + e.getMessage());
        } finally {
            serverManager.setHealthCheckInProgress(false);
        }
    }
}
