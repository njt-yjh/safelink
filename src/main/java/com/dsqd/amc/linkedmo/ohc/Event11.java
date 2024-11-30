package com.dsqd.amc.linkedmo.ohc;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.model.Evententry;
import com.dsqd.amc.linkedmo.service.EvententryService;
import com.dsqd.amc.linkedmo.util.HashUtil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class Event11 {
    private static final Logger logger = LoggerFactory.getLogger(Event11.class);

	// 전송전문 조립
	private String sendRestAPI(String url, String jsonParam, String method) {
		String resp = "";
		HttpUtil http = new HttpUtil(url, jsonParam, method);
		try {
			resp = http.httpConnection();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HttpConnection ERROR : {}", e.getMessage());
		}
		
		return resp;
	}
	
	// 이벤트 진입처리
	public JSONObject ohcSafelink(String mobileno, String ohvalue, String ip, String m_id) {
		String eventcd = "linksafe";
		String mappingno = HashUtil.SHA1toHex(mobileno);
		String keycode = "173l1ryYffihacZJqi22YFUuhzLcnlNaJhTPscqgCJlcU9A1P6Iq42pOcQu4LNE4";
		String keyvalue = HashUtil.MD5toHex(ohvalue + "|" + keycode); // 
		String url = "http://map.ohpoint.co.kr/rest/commit/"+eventcd;

		String resp = "";
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("joinInfo", mappingno);
		jsonParam.put("ohvalue", ohvalue);
		jsonParam.put("keyvalue", keyvalue.toLowerCase());
		
		logger.info("EVENT[{}] SEND {} - PARAM : {}", url, "OHC", jsonParam.toJSONString());
		
		resp = sendRestAPI(url, jsonParam.toJSONString(), "GET");
		logger.debug("EVENT[{}] RETURN JSON : {}", "OHC", resp);
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp = (JSONObject) JSONValue.parse(resp);
			String resCode = jsonResp.getAsString("resCode");

			// 가입 subscribe id를 가져온다.
			EvententryService service = new EvententryService();
			int subscribeid = service.getSubcribeIDByMobileno(mobileno);

			Evententry data = Evententry.builder()
					.eventcd(eventcd+"|"+m_id)
					.ucode(ohvalue)
					.status("20")
					.entryip(ip)
					.mobileno(mobileno)
					.mappingno(mappingno)
					.offercode("11") // ohpoint 이벤트 
					.completedt(new Date())
					.subscribeid(subscribeid+"")
					.json(resp)
					.build();
			
			if ("E00".equals(resCode)) { // 정상처리
				service.insertEvententry(data);
				logger.info("EventEntry insert : {}", resp);
			} else {
				data.setStatus("90");
				service.insertEvententry(data);
				logger.warn("EventEntry insert : {}", resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("JSON Parsing ERROR : {}", e.getMessage());
			jsonResp.put("resCode", "E99");
			jsonResp.put("resMsg", "JSONParsing ERROR");
		}
		return jsonResp;
	}
	
	// 이벤트 가입처리
	
	// 이벤트 취소처리 
	
	public static void main(String [] args) {
		// http://map.ohpoint.co.kr/rest/commit/{eId}?joinInfo=실적정보&ohvalue=전달ohvalue&keyvalue=MD5해시값
		// keyvalue=MD5해시값 : ohvalue + “|” + keycode 조합의 MD5 해시
		// param 은 json 형태로 전달함
		String eventcd = "linksafe";
		String mobileno = "01062235635";
		String mappingno = HashUtil.SHA1toHex(mobileno);
		String ohvalue = "20241111231220_01329757";
		String keycode = "173l1ryYffihacZJqi22YFUuhzLcnlNaJhTPscqgCJlcU9A1P6Iq42pOcQu4LNE4";
		String keyvalue = HashUtil.MD5toHex(ohvalue + "|" + keycode); // 
		String url = "http://map.ohpoint.co.kr/rest/commit/"+eventcd;
		
		String [] mobilenos = {
				"01062785382",
				"01073855844",
				"01088245740",
				"01085502927",
				"01085534196",
				"01090087862",
				"01090456034",
				"01043225862",
				"01046429742",
				"01092033676",
				"01063936545",
				"01036996545",
				"01021716545",
				"01037236866",
				"01041506359",
				"01052615816",
				"01050096297",
				"01064618029",
				"01099611929",
				"01036182862",
				"01064009829",
				"01091924867",
				"01094555718",
				"01057022351",
				"01052687058",
				"01054317059",
				"01049235936",
				"01057428579",
				"01064744973",
				"01071601373",
				"01089087085",
				"01089587085",
				"01047699956",
				"01086433006",
				"01064312043",
				"01040359785",
				"01048113354",
				"01093355836",
				"01041154874",
				"01062451695",
				"01038227025",
				"01027730288",
				"01093531433"};
		
		String [] ohvalues = {
				"20241118215155_02074334",
				"20241118214737_02073970",
				"20241118214131_02073435",
				"20241118205842_02069619",
				"20241118204512_02068450",
				"20241118202902_02067205",
				"20241118200034_02064829",
				"20241118200217_02064946",
				"20241118195841_02064683",
				"20241118195540_02064432",
				"20241118195041_02064057",
				"20241118194722_02063775",
				"20241118194722_02063776",
				"20241118194540_02063645",
				"20241118192902_02062411",
				"20241118190725_02060813",
				"20241118190004_02060285",
				"20241118183313_02058480",
				"20241118181407_02057099",
				"20241118180527_02056529",
				"20241118180015_02056157",
				"20241118173638_02054568",
				"20241118171337_02053038",
				"20241118170458_02052526",
				"20241118165019_02051650",
				"20241118163119_02050436",
				"20241118160728_02048931",
				"20241118141124_02041582",
				"20241118134940_02040283",
				"20241118131256_02037892",
				"20241118125811_02036834",
				"20241118124942_02036212",
				"20241118123848_02035506",
				"20241118123401_02035173",
				"20241118121223_02033811",
				"20241118113302_02031479",
				"20241118112804_02031180",
				"20241118112640_02031085",
				"20241118111135_02030064",
				"20241118105701_02028995",
				"20241118105625_02028956",
				"20241118105333_02028736",
				"20241118104313_02028115",

		};
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("joinInfo", mappingno);
		jsonParam.put("ohvalue", ohvalue);
		jsonParam.put("keyvalue", keyvalue.toLowerCase());
		
		System.out.println(jsonParam.toJSONString());
		
		for (String mo:mobilenos) {
			System.out.println(HashUtil.SHA1toHex(mo));
		}
		System.out.println();
		for (String ov:ohvalues) {
			String kv = HashUtil.MD5toHex(ov + "|" + keycode);
			System.out.println(kv.toLowerCase());
		}
	}
	
}
