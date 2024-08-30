package com.dsqd.amc.linkedmo.mapper;

import com.dsqd.amc.linkedmo.model.Attachment;
import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.model.Comment;
import com.dsqd.amc.linkedmo.model.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
	
	List<Board> getAllBoards();

    // 게시물 CRUD
    Board getBoardById(@Param("id") int id);

    void insertBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(@Param("id") int id);

    // 첨부파일 CRUD
    void insertAttachment(Attachment attachment);

    List<Attachment> getAttachmentsByBoardId(@Param("boardId") int boardId);

    // 댓글 CRUD
    void insertComment(Comment comment);

    List<Comment> getCommentsByBoardId(@Param("boardId") int boardId);

    // 답글 CRUD
    void insertReply(Reply reply);

    List<Reply> getRepliesByCommentId(@Param("commentId") int commentId);
}
