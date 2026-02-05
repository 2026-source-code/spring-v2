package com.example.boardv1.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boardv1.board.Board;
import com.example.boardv1.board.BoardRepository;
import com.example.boardv1.user.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final EntityManager em;

    // boardId = 100
    @Transactional
    public void 댓글등록(Integer boardId, String comment, Integer sessionUserId) {
        // join 터지기 싫어!!
        Board board = em.getReference(Board.class, boardId);
        User user = em.getReference(User.class, sessionUserId);
        Reply reply = new Reply();
        reply.setBoard(board);
        reply.setUser(user);
        reply.setComment(comment);

        // insert into reply_tb (user_id, board_id, comment, created_at) values(1, 1,
        // 'comment5', now());
        replyRepository.save(reply);
    }

    @Transactional
    public void 댓글삭제(int id, Integer sessionUserId) {
        // 1. 댓글 찾고
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        // 2. 권한 체크
        if (reply.getUser().getId() != sessionUserId)
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다");

        // 3. 댓글 삭제
        replyRepository.delete(reply);
    }
}
