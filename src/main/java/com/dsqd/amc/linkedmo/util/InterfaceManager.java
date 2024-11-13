package com.dsqd.amc.linkedmo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.controller.LoginController;

/**
 * 인터페이스 관리자 클래스. 서버의 헬스체크, 부하 분산, 재시도 등을 관리한다.
 */
public class InterfaceManager {

    private static final InterfaceManager instance = new InterfaceManager();
    private final Logger logger = LoggerFactory.getLogger(InterfaceManager.class);

    private final List<String> initialServerList = new CopyOnWriteArrayList<>();
    private final List<String> activeServerList = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, AtomicInteger> serverLoadMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> serverSuccessMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> serverFailureMap = new ConcurrentHashMap<>();
    private final AtomicBoolean healthCheckInProgress = new AtomicBoolean(false);
    private InterfaceHealthChecker healthChecker;
    private final RequestSender requestSender;
    private int maxLoadThreshold;
    
    private boolean checkTCP;
    private boolean checkHEAD;
    
    private String naruBToken;
    private String testMobileno = "01062235635";
    
    public void setTestMobileno(String mno) {
    	this.testMobileno = mno;
    }
    
    public String getTestMobileno() {
    	return this.testMobileno;
    }

    private InterfaceManager() {
        loadConfiguration();
        for (String server : initialServerList) {
            serverLoadMap.put(server, new AtomicInteger(0));
            serverSuccessMap.put(server, 0);
            serverFailureMap.put(server, 0);
        }
        this.requestSender = new RequestSender(this);

        // 초기 헬스체크
        startHealthCheckThread();
        while (isHealthCheckInProgress()) {
            System.out.println("=>초기 헬스 체크가 완료될 때까지 대기 중...");
            try {
                Thread.sleep(100); // 0.1초 대기 후 다시 확인
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static InterfaceManager getInstance() {
        return instance;
    }

    private void loadConfiguration() {
        Properties properties = new Properties();
        try {
            properties.load(Resources.getResourceAsStream("application.properties"));
            checkTCP = "1".equals(properties.getProperty("checkTCP"))?true:false;
            checkHEAD = "1".equals(properties.getProperty("checkHEAD"))?true:false;
            naruBToken = properties.getProperty("naru.jwt");
            
            logger.info("checkTCP:[{}] / checkHEAD:[{}]", checkTCP, checkHEAD);
            logger.info("NARU JWT : {}", naruBToken);
            
            String servers = properties.getProperty("initialServers");
            if (servers != null && !servers.isEmpty()) {
                for (String server : servers.split(",")) {
                    initialServerList.add(server.trim());
                    activeServerList.add(server.trim());
                }
            }
            logger.info("REST SERVER LIST : {}", servers);
            maxLoadThreshold = Integer.parseInt(properties.getProperty("maxLoadThreshold", "5"));
        } catch (IOException e) {
        	logger.error("설정 파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }

    public List<String> getInitialServerList() {
        return initialServerList;
    }

    public List<String> getActiveServerList() {
        return activeServerList;
    }

    public ConcurrentHashMap<String, AtomicInteger> getServerLoadMap() {
        return serverLoadMap;
    }

    public ConcurrentHashMap<String, Integer> getServerSuccessMap() {
        return serverSuccessMap;
    }

    public ConcurrentHashMap<String, Integer> getServerFailureMap() {
        return serverFailureMap;
    }

    public void addServer(String server) {
        initialServerList.add(server);
        activeServerList.add(server);
        serverLoadMap.put(server, new AtomicInteger(0));
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
        healthChecker = new InterfaceHealthChecker(this, checkTCP, checkHEAD);
        setHealthCheckInProgress(true);
        healthChecker.start();
    }

    public void setMaxLoadThreshold(int maxLoadThreshold) {
        this.maxLoadThreshold = maxLoadThreshold;
    }

    public int getMaxLoadThreshold() {
        return maxLoadThreshold;
    }
    
    public String getNaruJWT() {
        return this.naruBToken;
    }

    /**
     * 가장 적은 업무를 수행 중인 서버를 선택하는 메서드.
     *
     * @return 가장 적은 업무를 수행 중인 서버 URL
     */
    public String getLeastLoadedServer() {
        String leastLoadedServer = null;
        int leastLoad = Integer.MAX_VALUE;
        int totalLoad = 0;

        for (String server : activeServerList) {
            int load = serverLoadMap.get(server).get();
            totalLoad += load;
            if (load < leastLoad) {
                leastLoad = load;
                leastLoadedServer = server;
            }
        }

        if (totalLoad / activeServerList.size() >= maxLoadThreshold) {
            return leastLoadedServer;
        } else {
            // 라운드 로빈 방식으로 서버 선택
            int serverIndex = (int) (System.currentTimeMillis() % activeServerList.size());
            return activeServerList.get(serverIndex);
        }
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
        return requestSender.sendRequest(method, uri, parameter);
    }
}
