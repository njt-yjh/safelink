package com.dsqd.amc.linkedmo.controller;

import com.dsqd.amc.linkedmo.model.Board;
import com.dsqd.amc.linkedmo.service.BoardService;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.List;

import static spark.Spark.*;

public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService = new BoardService();

    public BoardController() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        path("/api", () -> {
            path("/v1.0", () -> {
                path("/boards", () -> {
                    // Get all boards
                    get("", this::getAllBoards);

                    // Get board by ID
                    get("/:id", (req, res) -> {
                        int id = Integer.parseInt(req.params(":id"));
                        Board board = boardService.getBoardById(id);
                        if (board != null) {
                            logger.info("Board retrieved for ID: {}", id);
                            res.status(200);
                            return board.toJSONString();
                        } else {
                            logger.warn("Board not found for ID: {}", id);
                            res.status(404);
                            return "Board not found";
                        }
                    });

                    // Insert new board
                    post("", (req, res) -> {
                    	System.out.println(req.body());
                    	JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
                        logger.info("Received board data for insertion: {}", jsonObject.toJSONString());

                        Board board = Board.builder()
                                           .title(jsonObject.getAsString("title"))
                                           .content(jsonObject.getAsString("content"))
                                           .userId(jsonObject.getAsString("userId"))
                                           .userName(jsonObject.getAsString("userName"))
                                           .createdAt(LocalDateTime.now())
                                           .updatedAt(LocalDateTime.now())
                                           .build();

                        boardService.insertBoard(board);
                        logger.info("Board inserted: {}", board);
                        res.status(201);
                        return board.toJSONString();
                    });

                    // Update existing board
                    put("/:id", (req, res) -> {
                        int id = Integer.parseInt(req.params(":id"));
                        JSONObject jsonObject = (JSONObject) JSONValue.parse(req.body());
                        logger.info("Received board data for update: {}", jsonObject.toJSONString());

                        Board board = Board.builder()
                                           .id(id)
                                           .title(jsonObject.getAsString("title"))
                                           .content(jsonObject.getAsString("content"))
                                           .userId(jsonObject.getAsString("userId"))
                                           .userName(jsonObject.getAsString("userName"))
                                           .updatedAt(LocalDateTime.now())
                                           .build();

                        boardService.updateBoard(board);
                        logger.info("Board updated for ID: {}", id);
                        res.status(200);
                        return board.toJSONString();
                    });

                    // Delete board by ID
                    delete("/:id", (req, res) -> {
                        int id = Integer.parseInt(req.params(":id"));
                        boardService.deleteBoard(id);
                        logger.info("Board deleted for ID: {}", id);
                        res.status(204);
                        return "";
                    });
                });
            });
        });
    }

    private String getAllBoards(Request req, Response res) {
        List<Board> boards = boardService.getAllBoards();
        return boards.stream()
                     .map(Board::toJSONString)
                     .reduce("[", (acc, json) -> acc + json + ",")
                     .replaceAll(",$", "]");  // JSON Array 형식으로 반환
    }
}
