package com.dsqd.amc.linkedmo.model;

import net.minidev.json.JSONObject;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Attachment {
    private int id;
    private int boardId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;

    public void parse(JSONObject jsonObject) {
        this.fileName = (String) jsonObject.get("fileName");
        this.filePath = (String) jsonObject.get("filePath");
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("boardId", boardId);
        jsonObject.put("fileName", fileName);
        jsonObject.put("filePath", filePath);
        jsonObject.put("uploadedAt", uploadedAt.toString());
        return jsonObject.toJSONString();
    }
}
