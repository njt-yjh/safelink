package com.dsqd.amc.linkedmo.util;

public class LoadBalancerApp {
    public static void main(String[] args) {
        ServerManager serverManager = ServerManager.getInstance();

        // 초기 헬스 체크 시작
        serverManager.startHealthCheckThread();

        // 초기 헬스 체크 완료 대기
        while (serverManager.isHealthCheckInProgress()) {
            System.out.println("초기 헬스 체크가 완료될 때까지 대기 중...");
            try {
                Thread.sleep(100); // 0.1초 대기 후 다시 확인
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 다섯 개의 워커 스레드를 생성하여 요청 처리
        for (int i = 0; i < 15; i++) {
            final int threadNum = i;
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    String method = "GET";
                    String uri = "/hello";
                    String parameter = "{\"key\":\"value\"}";
                    try {
                        // 0~2초 사이의 랜덤 지연 추가
                        Thread.sleep((long) (Math.random() * 2000));
                        System.out.println("Thread-" + threadNum + "에서 " + (j + 1) + "번째 요청을 보냅니다.");
                        serverManager.submitRequest(method, uri, parameter);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        // 오류 발생 시 즉시 헬스 체크 수행
                        serverManager.startHealthCheckThread();
                    }
                }
            }).start();
        }
    }
}
