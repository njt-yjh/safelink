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

public class Batch {
	private int txid;
	private String batchid;
	private Date started;
	private Date ended;
	private String code;
	private String result;

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txid", txid);
        jsonObject.put("batchid", batchid);
        jsonObject.put("started", started);
        jsonObject.put("ended", ended);
        jsonObject.put("code", code);
        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }
}
