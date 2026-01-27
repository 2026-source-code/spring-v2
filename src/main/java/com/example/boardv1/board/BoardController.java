package com.example.boardv1.board;

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

    // body : title=하하하&content=호호호호
    @PostMapping("/boards/{id}/update")
    public String update(@PathVariable("id") int id, String title, String content) {
        boardService.게시글수정(id, title, content);
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

    @GetMapping("/boards/{id}")
    public String detail(@PathVariable("id") int id, HttpServletRequest req) {
        Board board = boardService.상세보기(id);
        req.setAttribute("model", board);
        return "board/detail";
    }
}
