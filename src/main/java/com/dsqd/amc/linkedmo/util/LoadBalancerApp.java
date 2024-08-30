package com.dsqd.amc.linkedmo.util;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadBalancerApp {

    public static void main(String[] args) {
        InterfaceManager interfaceManager = InterfaceManager.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 15; i++) {
            int threadNum = i;
            executorService.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    String method = "GET";
                    String uri = "/hello";
                    String parameter = "{\"key\":\"value\"}";
                    try {
                        long delay = new Random().nextInt(2000); // 0~2초 랜덤 지연
                        Thread.sleep(delay);
                        System.out.println("Thread-" + threadNum + "에서 " + (j + 1) + "번째 요청 보냄");
                        interfaceManager.sendRequest(method, uri, parameter);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        // 오류 발생 시 즉시 헬스 체크 수행
                        interfaceManager.startHealthCheckThread();
                    }
                }
            });
        }

        executorService.shutdown();
    }
}
