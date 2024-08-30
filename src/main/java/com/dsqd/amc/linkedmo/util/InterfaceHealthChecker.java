package com.dsqd.amc.linkedmo.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 * 서버 헬스체크를 수행하는 스레드 클래스.
 */
public class InterfaceHealthChecker extends Thread {

    private final InterfaceManager interfaceManager;
    private final boolean checkTCP;
    private final boolean checkHEAD;

    public InterfaceHealthChecker(InterfaceManager interfaceManager, boolean checkTCP, boolean checkHEAD) {
        this.interfaceManager = interfaceManager;
        this.checkTCP = checkTCP;
        this.checkHEAD = checkHEAD;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            boolean allServersDown = true;
            for (String server : interfaceManager.getInitialServerList()) {
                boolean isServerUp = false;
                boolean isMethodUp = false;
                
                try {
                    // TCP 포트 체크
                	String host = server.split(":")[1].replaceAll("/", "");
                	int port;

                	if (server.split(":").length == 3) {
                	    // 명시된 포트가 있는 경우
                	    port = Integer.parseInt(server.split(":")[2]);
                	} else {
                	    // 명시된 포트가 없는 경우
                	    if (server.startsWith("https://")) {
                	        port = 443;
                	    } else if (server.startsWith("http://")) {
                	        port = 80;
                	    } else {
                	        throw new IllegalArgumentException("잘못된 URL 형식입니다: " + server);
                	    }
                	}
                	
                	if (checkTCP) {
	                    try (Socket socket = new Socket()) {
	                        socket.connect(new InetSocketAddress(host, port), 2000); // 2초 타임아웃
	                        isServerUp = true;
	                        
	                    } catch (IOException e) {
	                        System.err.println("TCP 포트 체크 실패(" + host +":" + port + "): " + e.getMessage());
	                    }
                	}
                	// Method 체크를 안하는 경우 여기서 ActiveServer 정리
                	if (!checkHEAD) {
                		if (isServerUp) {
                            if (!interfaceManager.getActiveServerList().contains(server)) {
                                interfaceManager.getActiveServerList().add(server);
                            }
                            allServersDown = false;
                        } else {
                            interfaceManager.getActiveServerList().remove(server);
                        }
                	}
                	
                    // HTTP HEAD 요청 체크
                	if (checkHEAD) {
	                    if (isServerUp) {
	                        HttpURLConnection connection = (HttpURLConnection) new URL(server + "/health").openConnection();
	                        connection.setRequestMethod("HEAD");
	                        int responseCode = connection.getResponseCode();
	
	                        if (responseCode == HttpURLConnection.HTTP_OK) {
	                            if (!interfaceManager.getActiveServerList().contains(server)) {
	                                interfaceManager.getActiveServerList().add(server);
	                            }
	                            allServersDown = false;
	                        } else {
	                            interfaceManager.getActiveServerList().remove(server);
	                        }
	                    } else {
	                        interfaceManager.getActiveServerList().remove(server);
	                    }
                	}
                } catch (IOException e) {
                    interfaceManager.getActiveServerList().remove(server);
                }
            }

            interfaceManager.setHealthCheckInProgress(false);

            if (allServersDown) {
                try {
                    System.out.println("모든 서버가 비활성 상태입니다. 10초 후 다시 헬스체크를 시도합니다.");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                try {
                    System.out.println("헬스 체크 완료. 5분 후 다시 헬스체크를 시도합니다.");
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }
}
