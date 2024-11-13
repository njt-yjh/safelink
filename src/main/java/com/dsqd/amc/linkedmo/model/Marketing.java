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

public class Marketing {
	private int id;
	private String eventcd; // : linksafe 
	private String keycode; // : 마케팅이벤트용 key
	private String eventcp; // : ohc  (회사명)
	private Date startdate; // : 시작일자 
	private Date enddate; // : 종료일자
	private String remark; // : 기타 사항
	
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("eventcd", eventcd);
        jsonObject.put("keycode", keycode);
        jsonObject.put("startdate", startdate);
        jsonObject.put("enddate", enddate);
        jsonObject.put("remark", remark);

        return jsonObject.toJSONString();
    }
}
