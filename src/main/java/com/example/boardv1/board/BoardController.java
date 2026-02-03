package com.example.boardv1.board;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // body : title=title7&content=content7 (x-www-form)
    @PostMapping("/boards/save")
    public String save(BoardRequest.SaveOrUpdateDTO reqDTO) throws IOException {
        boardService.게시글쓰기(reqDTO.getTitle(), reqDTO.getContent());
        return "redirect:/";
    }

    // body : title=하하하&content=호호호호
    @PostMapping("/boards/{id}/update")
    public String update(@PathVariable("id") int id, BoardRequest.SaveOrUpdateDTO reqDTO) {
        boardService.게시글수정(id, reqDTO.getTitle(), reqDTO.getContent());
        return "redirect:/boards/" + id;
    }

    @GetMapping("/")
    public String index(HttpServletRequest req) {
        List<Board> list = boardService.게시글목록();
        req.setAttribute("models", list);
        return "index";
    }

    @GetMapping("/boards/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    @GetMapping("/boards/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest req) {
        Board board = boardService.상세보기(id);
        req.setAttribute("model", board);
        return "board/update-form";
    }

    // /boards?content=사과
    // select * from board_tb where content like '%사과%';

    @GetMapping("/boards/{id}")
    public String detail(@PathVariable("id") int id, HttpServletRequest req) {
        Board board = boardService.상세보기(id);
        req.setAttribute("model", board);
        return "board/detail";
    }

    @PostMapping("/boards/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        boardService.게시글삭제(id);
        return "redirect:/";
    }
}
