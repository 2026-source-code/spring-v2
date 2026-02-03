package com.example.boardv1.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> 게시글목록() {
        return boardRepository.findAll();
    }

    public Board 상세보기(int id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없어요"));
    }

    @Transactional // update, delete, insert 할때 붙이세요!!
    public void 게시글수정(int id, String title, String content) {
        //
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 게시글을 찾을 수 없어요"));
        board.setTitle(title);
        board.setContent(content);
    }

    // 원자성(모든게 다되면 commit, 하나라도 실패하면 rollback)
    // 트랜잭션 종료시 flush 됨.
    @Transactional
    public void 게시글쓰기(String title, String content) {
        // 1. 비영속 객체
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);

        System.out.println("before persist " + board.getId());

        // 2. persist
        boardRepository.save(board);

        System.out.println("after persist " + board.getId());
    }

    @Transactional
    public void 게시글삭제(int id) {
        // 영속화
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제할 게시글을 찾을 수 없어요"));
        boardRepository.delete(board);
    } // 자동 flush

}
