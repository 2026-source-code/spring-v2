package com.example.boardv1.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;

@Import(BoardRepository.class)
@DataJpaTest // EntityManger가 ioc에 등록됨
public class BoardRepositoryTest {

    @Autowired // 어노테이션 DI 기법
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void save_test() {
        // given
        Board board = new Board();
        board.setTitle("title7");
        board.setContent("content7");
        System.out.println("===before persist");
        System.out.println(board);

        // when
        boardRepository.save(board);

        // eye (board객체가 DB데이터와 동기화 되었음.)
        System.out.println("===after persist");
        System.out.println(board);
    }

    @Test
    public void findById_test() {
        // given
        int id = 1;

        // when
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요"));
        // boardRepository.findById(1);

        // eye
        System.out.println(board);
    }

    @Test
    public void findAll_test() {
        // given

        // when
        List<Board> list = boardRepository.findAll();

        // eye
        for (Board board : list) {
            System.out.println(board);
        }
    }

    @Test
    public void delete_test() {
        // given
        int id = 1;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요"));

        // when
        boardRepository.delete(board);

        // eye
        em.flush();
    }

    @Test
    public void update_test() {
        // given
        int id = 1;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요요"));

        // when
        board.setTitle("title1-update");

        // eye
        em.flush();

        List<Board> list = boardRepository.findAll();

        // eye
        for (Board b : list) {
            System.out.println(b);
        }
    }

    @Test
    public void findByIdV2_test() {
        // given
        int id = 1;

        // when
        boardRepository.findById(id);
        em.clear();
        boardRepository.findById(id);
    }

    @Test
    public void orm_test() {
        int id = 1;

        Board board = boardRepository.findById(id).get();
        System.out.println("board->user->id : " + board.getUser().getId());
        System.out.println("------------------------------------------");
        System.out.println("board->user->username : " + board.getUser().getUsername());
    }

    @Test
    public void orm_v2_test() {
        int id = 1;

        Board board = boardRepository.findById(id).get();
        System.out.println(board.toString());
    }

    // ========== Part 2 테스트: User 관계 + N+1 문제 ==========

    @Test
    public void part2_findById_lazy_n_plus_1_test() {
        System.out.println("===== [Part 2] findById 호출 - Lazy Loading =====");
        Board board = boardRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요"));
        System.out.println("===== board 조회 완료 =====");
        System.out.println("board.id: " + board.getId());
        System.out.println("board.title: " + board.getTitle());

        // User 접근 시 추가 쿼리 발생 (N+1)
        System.out.println("===== user.username 접근 시작 =====");
        String username = board.getUser().getUsername();
        System.out.println("===== user.username 접근 완료: " + username + " =====");
    }

    @Test
    public void part2_findByIdJoinUser_no_n_plus_1_test() {
        System.out.println("===== [Part 2] findByIdJoinUser 호출 - JOIN FETCH =====");
        Board board = boardRepository.findByIdJoinUser(1)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요"));
        System.out.println("===== JOIN FETCH로 board + user 조회 완료 =====");

        // User 접근 시 추가 쿼리 없음
        System.out.println("===== user.username 접근 시작 =====");
        String username = board.getUser().getUsername();
        System.out.println("===== user.username 접근 완료: " + username + " (추가 쿼리 없음) =====");
    }

    @Test
    public void part2_findAll_n_plus_1_test() {
        System.out.println("===== [Part 2] findAll 호출 - N+1 문제 =====");
        List<Board> boards = boardRepository.findAll();
        System.out.println("===== " + boards.size() + "개 게시글 조회 완료 (1번 쿼리) =====");

        int userQueryCount = 0;
        for (Board board : boards) {
            String username = board.getUser().getUsername();
            userQueryCount++;
            System.out.println("Board " + board.getId() + " - User: " + username + " (N+1 쿼리 #" + userQueryCount + ")");
        }

        System.out.println("===== 총 User 쿼리 발생 횟수: " + userQueryCount + "번 (N+1 문제!) =====");
    }

    // ========== Part 3 테스트: Reply 관계 + Massive N+1 문제 ==========

    @Test
    public void part3_findAll_with_replies_massive_n_plus_1_test() {
        System.out.println("===== [Part 3] findAll + Replies 접근 - Massive N+1 문제 =====");
        List<Board> boards = boardRepository.findAll();
        System.out.println("===== " + boards.size() + "개 게시글 조회 완료 (1번 쿼리) =====");

        int boardUserQueryCount = 0;
        int replyQueryCount = 0;
        int replyUserQueryCount = 0;

        for (Board board : boards) {
            // Board의 User 접근
            String boardUsername = board.getUser().getUsername();
            boardUserQueryCount++;
            System.out.println("Board " + board.getId() + " - User: " + boardUsername + " (Board User 쿼리 #" + boardUserQueryCount + ")");

            // Board의 Replies 접근
            int replyCount = board.getReplies().size();
            replyQueryCount++;
            System.out.println("  ㄴ Replies 조회: " + replyCount + "개 (Reply 쿼리 #" + replyQueryCount + ")");

            // 각 Reply의 User 접근
            for (int i = 0; i < Math.min(3, replyCount); i++) {
                String replyUsername = board.getReplies().get(i).getUser().getUsername();
                replyUserQueryCount++;
                System.out.println("    ㄴ Reply User: " + replyUsername + " (Reply User 쿼리 #" + replyUserQueryCount + ")");
            }
        }

        System.out.println("===== 총 쿼리 발생 =====");
        System.out.println("Board 조회: 1번");
        System.out.println("Board User 조회: " + boardUserQueryCount + "번");
        System.out.println("Reply 조회: " + replyQueryCount + "번");
        System.out.println("Reply User 조회: " + replyUserQueryCount + "번");
        System.out.println("총 쿼리: " + (1 + boardUserQueryCount + replyQueryCount + replyUserQueryCount) + "번 (Massive N+1 문제!)");
    }

    @Test
    public void part3_batch_fetch_size_test() {
        System.out.println("===== [Part 3] Batch Fetch Size 테스트 =====");
        System.out.println("application.properties에 default_batch_fetch_size=10 설정됨");

        List<Board> boards = boardRepository.findAll();
        System.out.println("===== " + boards.size() + "개 게시글 조회 완료 =====");

        // User 일괄 조회 (IN 쿼리)
        System.out.println("===== Board User 일괄 접근 시작 =====");
        for (Board board : boards) {
            board.getUser().getUsername();
        }
        System.out.println("===== Board User IN 쿼리로 일괄 조회 완료 =====");

        // Reply + Reply User 일괄 조회
        System.out.println("===== Replies + Reply User 일괄 접근 시작 =====");
        for (Board board : boards) {
            for (int i = 0; i < Math.min(3, board.getReplies().size()); i++) {
                board.getReplies().get(i).getUser().getUsername();
            }
        }
        System.out.println("===== Replies + Reply User IN 쿼리로 일괄 조회 완료 =====");
        System.out.println("총 4개 쿼리 (Board 1번 + User IN 1번 + Reply IN 1번 + Reply User IN 1번)");
    }

}
