package com.example.boardv1.reply;

import java.util.List;

import lombok.Data;

public class ReplyResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String comment;
        private Integer replyUserId;
        private String replyUsername;
        private boolean isReplyOwner; // 로그인한 유저가 댓글을 작성한 유저인지

        public DTO(Reply reply, Integer sessionUserId) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.replyUserId = reply.getUser().getId();
            this.replyUsername = reply.getUser().getUsername();
            this.isReplyOwner = reply.getUser().getId() == sessionUserId;
        }
    }
}
