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

public class Evententry {
	private int txid;
	private String eventcd; // : 이벤트코드 (FK : maketings.eventcd)
	private String ucode; // : 개별이벤트 참가를 위해 만든 것 (이벤트사에서 데이터 제공) - ohvalue 
	private String status; // : 상태코드 (00 : Entry 기록, 10 : 가입완료, E : 20 : 이벤트참여완료)
	private String entryip; // : 참여아이피
	private String mobileno; // : 전화번호
	private String mappingno; // : 전화번호 매핑코드
	private String offercode; // : offercode 
	private String subscribeid; // : 가입번호 (FK : subscribe.id)
	private Date entrydt; // : 참여시작일시
	private Date completedt; // : 이벤트종료일시 (이벤트사에 전문발송 기준)
	private String json; // : 관련내용 JSON 저장 (hash값 저장)
	private String nowstatus; // 현재상태
	
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txid", txid);
        jsonObject.put("eventcd", eventcd);
        jsonObject.put("ucode", ucode);
        jsonObject.put("status", status);
        jsonObject.put("entryip", entryip);
        jsonObject.put("mobileno", mobileno);
        jsonObject.put("mappingno", mappingno);
        jsonObject.put("offercode", offercode);
        jsonObject.put("subscribeid", subscribeid);
        jsonObject.put("entrydt", entrydt);
        jsonObject.put("completedt", completedt);
        jsonObject.put("json", json);
        jsonObject.put("nowstatus", nowstatus);

        return jsonObject.toJSONString();
    }
}
