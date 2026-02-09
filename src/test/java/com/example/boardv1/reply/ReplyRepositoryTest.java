package com.example.boardv1.reply;

import com.example.boardv1.board.Board;
import com.example.boardv1.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(ReplyRepository.class)
@DataJpaTest
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager em;

    // ========== Part 3 테스트: getReference()로 Reply 저장 ==========

    @Test
    public void part3_save_with_getReference_test() {
        System.out.println("===== [Part 3] getReference로 Reply 저장 =====");

        // Given: 프록시로 Board, User 참조 (SELECT 없이)
        System.out.println("===== Board, User getReference 호출 =====");
        Board boardProxy = em.getReference(Board.class, 1);
        User userProxy = em.getReference(User.class, 1);
        System.out.println("===== 프록시 생성 완료 (아직 SELECT 없음) =====");

        Reply reply = new Reply();
        reply.setComment("getReference 테스트 댓글");
        reply.setBoard(boardProxy);
        reply.setUser(userProxy);

        // When
        System.out.println("===== Reply 저장 시작 =====");
        replyRepository.save(reply);
        em.flush();
        System.out.println("===== Reply 저장 완료 (INSERT만 발생, SELECT 없음!) =====");

        // Eye
        System.out.println("Reply ID: " + reply.getId());
    }

    @Test
    public void part3_save_without_getReference_test() {
        System.out.println("===== [Part 3] find()로 Reply 저장 (비교용) =====");

        // Given: find()로 Board, User 조회 (SELECT 발생)
        System.out.println("===== Board, User find 호출 =====");
        Board board = em.find(Board.class, 1);
        User user = em.find(User.class, 1);
        System.out.println("===== Board, User SELECT 쿼리 2번 발생 =====");

        Reply reply = new Reply();
        reply.setComment("find 테스트 댓글");
        reply.setBoard(board);
        reply.setUser(user);

        // When
        System.out.println("===== Reply 저장 시작 =====");
        replyRepository.save(reply);
        em.flush();
        System.out.println("===== Reply 저장 완료 (총 3개 쿼리: SELECT 2번 + INSERT 1번) =====");

        // Eye
        System.out.println("Reply ID: " + reply.getId());
    }
}
