package com.example.boardv1.board;

import java.util.List;

import com.example.boardv1.reply.ReplyResponse;

import lombok.Data;

public class BoardResponse {

    @Data
    public static class DTO {
        private int id;
        private String title;
        private String content;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }

    }

    @Data
    public static class DetailDTO {
        // 화면에 보이지 않는것
        private int id;
        private int userId;

        // 화면에 보이는것
        private String title;
        private String content;
        private String username;

        // 연산해서 만들어야 되는것
        private boolean isOwner; // 게시글의 주인인가?

        private List<ReplyResponse.DTO> replies;

        public DetailDTO(Board board, Integer sessionUserId) {
            this.id = board.getId();
            this.userId = board.getUser().getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.username = board.getUser().getUsername();
            this.isOwner = board.getUser().getId() == sessionUserId;
            this.replies = board.getReplies().stream()
                    .map(reply -> new ReplyResponse.DTO(reply, sessionUserId))
                    .toList();
        }

    }
}
