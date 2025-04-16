package com.dsqd.amc.linkedmo.batch;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.service.MobiliansService;
import com.dsqd.amc.linkedmo.util.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

public class Batch04 implements Task {
	// KG모빌리언스 자동결제 배치

	private static final Logger logger = LoggerFactory.getLogger(Batch04.class);
	
	
	public static void main(String [] args) {
		Batch04 bat = new Batch04();
	}
	
	@Override
    public void executeTask(Map<String, Object> params, int triggerId) {
		String batch_name = "Batch04-autoPayBatch";

		MobiliansService mobiliansService = new MobiliansService();
		
        // 여기에 실제 작업 로직 작성
		logger.info("Executing {} with params: {} and triggerId: {}",batch_name, params, triggerId);
		mobiliansService.autoPayBatch();
    	logger.info("{} executeTask END", batch_name);
	}
}
