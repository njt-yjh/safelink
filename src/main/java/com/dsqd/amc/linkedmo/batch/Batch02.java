package com.dsqd.amc.linkedmo.batch;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Batchlog;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.naru.SubscribeNaru;
import com.dsqd.amc.linkedmo.service.AdminService;
import com.dsqd.amc.linkedmo.service.BatchService;
import com.dsqd.amc.linkedmo.service.BatchlogService;
import com.dsqd.amc.linkedmo.service.BoardService;
import com.dsqd.amc.linkedmo.service.SubscribeService;
import com.dsqd.amc.linkedmo.skt.APICall;
import com.dsqd.amc.linkedmo.skt.SubscribeSK;
import com.dsqd.amc.linkedmo.util.Task;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class Batch02 implements Task {
	// PING Query 수행
	
	private static final Logger logger = LoggerFactory.getLogger(Batch02.class);
	
	
	public static void main(String [] args) {
		Batch02 bat = new Batch02();
	}
	
	@Override
    public void executeTask(Map<String, Object> params, int triggerId) {
		Properties prop = MyBatisConfig.getApplicationProperties();
		String env = System.getProperty("argEnv"); // local, dev, prod
		String batch_name = "Batch02-pingQuery";
		
		AdminService service = new AdminService();
		
        // 여기에 실제 작업 로직 작성
		logger.info("Executing {} with params: {} and triggerId: {}",batch_name, params, triggerId);
        int data = service.pingQuery();
    	logger.info("{} executeTask END", batch_name);
	}
}
