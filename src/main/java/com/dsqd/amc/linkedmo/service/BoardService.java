package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.BoardMapper;
import com.dsqd.amc.linkedmo.model.Attachment;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.model.Comment;
import com.dsqd.amc.linkedmo.model.Reply;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BoardService {
    private SqlSessionFactory sqlSessionFactory;

    public BoardService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
    
    public List<Board> getAllBoards() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            return mapper.getAllBoards();
        }
    }

    // 게시물 삽입
    public void insertBoard(Board board) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.insertBoard(board);
            session.commit();
        }
    }

    // 게시물 수정
    public void updateBoard(Board board) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.updateBoard(board);
            session.commit();
        }
    }

    // 게시물 조회
    public Board getBoardById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            return mapper.getBoardById(id);
        }
    }

    // 게시물 삭제
    public void deleteBoard(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.deleteBoard(id);
            session.commit();
        }
    }

    // 첨부파일 삽입
    public void insertAttachment(Attachment attachment) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.insertAttachment(attachment);
            session.commit();
        }
    }

    // 첨부파일 조회
    public List<Attachment> getAttachmentsByBoardId(int boardId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            return mapper.getAttachmentsByBoardId(boardId);
        }
    }

    // 댓글 삽입
    public void insertComment(Comment comment) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.insertComment(comment);
            session.commit();
        }
    }

    // 댓글 조회
    public List<Comment> getCommentsByBoardId(int boardId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            return mapper.getCommentsByBoardId(boardId);
        }
    }

    // 답글 삽입
    public void insertReply(Reply reply) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            mapper.insertReply(reply);
            session.commit();
        }
    }

    // 답글 조회
    public List<Reply> getRepliesByCommentId(int commentId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            return mapper.getRepliesByCommentId(commentId);
        }
    }
}
