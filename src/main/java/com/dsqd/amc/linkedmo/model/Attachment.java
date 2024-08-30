package com.dsqd.amc.linkedmo.model;

import net.minidev.json.JSONObject;

import java.time.LocalDateTime;

public class Attachment {
    private int id;
    private int boardId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;

    private Attachment(Builder builder) {
        this.id = builder.id;
        this.boardId = builder.boardId;
        this.fileName = builder.fileName;
        this.filePath = builder.filePath;
        this.uploadedAt = builder.uploadedAt;
    }

    public static class Builder {
        private int id;
        private int boardId;
        private String fileName;
        private String filePath;
        private LocalDateTime uploadedAt;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder boardId(int boardId) {
            this.boardId = boardId;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder uploadedAt(LocalDateTime uploadedAt) {
            this.uploadedAt = uploadedAt;
            return this;
        }

        public Attachment build() {
            return new Attachment(this);
        }
    }

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

}
