package com.dsqd.amc.linkedmo.model;

import net.minidev.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class Board {
    private int id;
    private String title;
    private String content;
    private String userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String filePath;
    private List<Comment> comments;
    private List<Reply> replies;

    private Board(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.filePath = builder.filePath;
        this.comments = builder.comments;
        this.replies = builder.replies;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String title;
        private String content;
        private String userId;
        private String userName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String filePath;
        private List<Comment> comments;
        private List<Reply> replies;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Builder replies(List<Reply> replies) {
            this.replies = replies;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }

    public void parse(JSONObject jsonObject) {
        this.title = (String) jsonObject.get("title");
        this.content = (String) jsonObject.get("content");
        this.userId = (String) jsonObject.get("userId");
        this.userName = (String) jsonObject.get("userName");
        this.filePath = (String) jsonObject.get("filePath");
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("userId", userId);
        jsonObject.put("userName", userName);
        jsonObject.put("createdAt", createdAt.toString());
        jsonObject.put("updatedAt", updatedAt != null ? updatedAt.toString() : null);
        jsonObject.put("filePath", filePath);
        return jsonObject.toJSONString();
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}


}
