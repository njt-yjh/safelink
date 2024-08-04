package com.dsqd.amc.linkedmo.util;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 서버 관리자 클래스. 서버의 헬스체크, 부하 분산, 재시도 등을 관리한다.
 */
public class ServerManager {

    private static final ServerManager instance = new ServerManager();

    private final List<String> initialServerList = new CopyOnWriteArrayList<>();
    private final List<String> activeServerList = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, Integer> serverLoadMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> serverSuccessMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> serverFailureMap = new ConcurrentHashMap<>();
    private final AtomicBoolean healthCheckInProgress = new AtomicBoolean(false);
    private ServerHealthChecker healthChecker;

    private final BlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>(100); // 최대 100개의 대기 작업
    private final ExecutorService executor = new ThreadPoolExecutor(
            5, 20, 1, TimeUnit.MINUTES, requestQueue
    );
    private final List<Future<String>> futures = new CopyOnWriteArrayList<>();

    private ServerManager() {
        // 초기 서버 목록을 설정합니다.
        initialServerList.add("http://localhost:5050");
        initialServerList.add("http://127.0.0.1:5050");
        activeServerList.addAll(initialServerList);
        for (String server : initialServerList) {
            serverLoadMap.put(server, 0);
            serverSuccessMap.put(server, 0);
            serverFailureMap.put(server, 0);
        }
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public List<String> getInitialServerList() {
        return initialServerList;
    }

    public List<String> getActiveServerList() {
        return activeServerList;
    }

    public void addServer(String server) {
        initialServerList.add(server);
        activeServerList.add(server);
        serverLoadMap.put(server, 0);
        serverSuccessMap.put(server, 0);
        serverFailureMap.put(server, 0);
    }

    public void removeServer(String server) {
        initialServerList.remove(server);
        activeServerList.remove(server);
        serverLoadMap.remove(server);
        serverSuccessMap.remove(server);
        serverFailureMap.remove(server);
    }

    public void clearActiveServers() {
        activeServerList.clear();
    }

    public boolean isHealthCheckInProgress() {
        return healthCheckInProgress.get();
    }

    public void setHealthCheckInProgress(boolean inProgress) {
        healthCheckInProgress.set(inProgress);
    }

    public void startHealthCheckThread() {
        if (healthChecker != null && healthChecker.isAlive()) {
            healthChecker.interrupt();
        }
        healthChecker = new ServerHealthChecker(this);
        setHealthCheckInProgress(true);
        healthChecker.start();
    }

    public void submitRequest(String method, String uri, String parameter) {
        try {
            Future<String> future = executor.submit(new SendRequestTask(this, method, uri, parameter));
            futures.add(future);
        } catch (RejectedExecutionException e) {
            System.err.println("요청이 거부되었습니다: " + e.getMessage());
            // 재시도 로직 추가 (백오프 전략 적용)
            try {
                Thread.sleep(1000); // 1초 대기 후 재시도
                Future<String> future = executor.submit(new SendRequestTask(this, method, uri, parameter));
                futures.add(future);
            } catch (InterruptedException | RejectedExecutionException ex) {
                System.err.println("재시도 요청이 거부되었습니다: " + ex.getMessage());
            }
        }
    }

    public String getLeastLoadedServer() {
        String leastLoadedServer = null;
        int leastLoad = Integer.MAX_VALUE;

        for (String server : activeServerList) {
            int load = serverLoadMap.getOrDefault(server, 0);
            if (load < leastLoad) {
                leastLoad = load;
                leastLoadedServer = server;
            }
        }
        return leastLoadedServer;
    }

    public void incrementServerLoad(String server) {
        serverLoadMap.put(server, serverLoadMap.getOrDefault(server, 0) + 1);
    }

    public void decrementServerLoad(String server) {
        serverLoadMap.put(server, serverLoadMap.getOrDefault(server, 0) - 1);
    }

    public int getServerLoad(String server) {
        return serverLoadMap.getOrDefault(server, 0);
    }

    public void incrementServerSuccessCount(String server) {
        serverSuccessMap.put(server, serverSuccessMap.getOrDefault(server, 0) + 1);
    }

    public void incrementServerFailureCount(String server) {
        serverFailureMap.put(server, serverFailureMap.getOrDefault(server, 0) + 1);
    }

    public int getServerSuccessCount(String server) {
        return serverSuccessMap.getOrDefault(server, 0);
    }

    public int getServerFailureCount(String server) {
        return serverFailureMap.getOrDefault(server, 0);
    }

    public void removeActiveServer(String server) {
        activeServerList.remove(server);
    }
}
