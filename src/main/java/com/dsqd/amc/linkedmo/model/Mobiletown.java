package com.dsqd.amc.linkedmo.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.JSONObject;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Mobiletown {
	private int txid;
	private String purpose; 	// 사용목적, S(subscribe), C(cancel), E(기타)
	private String spuserid; 	// 통신사에서 받은 고객의 고유값 
	private String rcverkey; 	// key
	private String rcverphone; 	// 수신자전화번호
	private Date sendtime; 		// 전송시간
	private String subject; 	// 전송메시지-제목(LMS일 경우)
	private String content; 	// 전송메시지 (최대 2000byte)
	private String result; 		// 전송결과 응답 JSON
	private String rnumber; 	// 인증번호 (숫자6자리)
	private Date checktime; 	// 점검시도시간 
	private String checkcode; 	//점검결과, T - 대기, S - 성공, F - 실패 
	private int errcnt; 		// 점검오류회수 

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txid", txid);
        jsonObject.put("purpose", purpose);
        jsonObject.put("spuserid", spuserid);
        jsonObject.put("rcverkey", rcverkey);
        jsonObject.put("rcverphone", rcverphone);
        jsonObject.put("sendtime", sendtime);
        jsonObject.put("subject", subject);
        jsonObject.put("content", content);
        jsonObject.put("result", result);
        jsonObject.put("rnumber", rnumber);
        jsonObject.put("checktime", checktime);
        jsonObject.put("checkcode", checkcode);
        jsonObject.put("errcnt", errcnt);
        return jsonObject.toJSONString();
    }
}
