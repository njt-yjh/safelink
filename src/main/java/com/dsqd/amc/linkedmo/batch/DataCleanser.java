package com.dsqd.amc.linkedmo.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.controller.DataController;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.service.SubscribeService;
import com.dsqd.amc.linkedmo.util.InterfaceManager;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class DataCleanser {
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	private SubscribeService service = new SubscribeService();
	private InterfaceManager itfMgr = InterfaceManager.getInstance();
	
	public DataCleanser() {
		
	}
	
	public void batch() {
		SubscribeService service = new SubscribeService();
		InterfaceManager itfMgr = InterfaceManager.getInstance();
		
		// 전체 사용자 정보를 조회함
		List <Subscribe> data = service.getSubscribeAll();
		
		for (Subscribe item: data) {
		// 부가서비스 제공사(Naru)와 사용자 정보를 비교함
			String method = "POST";
			String uri = "/api/v1.0/linksafe/getdata";
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("spcode", item.getSpcode());
			jsonParam.put("mobileno", item.getMobileno());
			
			JSONObject itfJSON;
			try {
				itfJSON = (JSONObject) JSONValue.parse(itfMgr.sendRequest(method, uri, jsonParam.toJSONString()));
				System.out.println(itfJSON.toJSONString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}
}
