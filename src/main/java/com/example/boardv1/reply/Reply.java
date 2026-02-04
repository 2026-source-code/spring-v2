package com.example.boardv1.reply;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.boardv1.board.Board;
import com.example.boardv1.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글1 댓글N
 * 유저1 댓글N
 */

@NoArgsConstructor
@Data
@Entity
@Table(name = "reply_tb")
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String comment;

    @ManyToOne
    private Board board; // board_id

    @ManyToOne
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
