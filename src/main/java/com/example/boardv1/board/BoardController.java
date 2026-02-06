package com.example.boardv1.board;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.boardv1._core.errors.ex.Exception401;
import com.example.boardv1._core.errors.ex.Exception500;
import com.example.boardv1.user.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    // body : title=title7&content=content7 (x-www-form)
    @PostMapping("/boards/save")
    public String save(BoardRequest.SaveOrUpdateDTO reqDTO) throws IOException {

        // 인증(v).권한(x)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null)
            throw new Exception401("인증되지 않았습니다.");

        boardService.게시글쓰기(reqDTO.getTitle(), reqDTO.getContent());
        return "redirect:/";
    }

    // body : title=하하하&content=호호호호
    @PostMapping("/boards/{id}/update")
    public String update(@PathVariable("id") int id, BoardRequest.SaveOrUpdateDTO reqDTO) {

        // 인증(v).권한(v)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null)
            throw new Exception401("인증되지 않았습니다.");

        boardService.게시글수정(id, reqDTO.getTitle(), reqDTO.getContent(), sessionUser.getId());
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

        // 인증(v).권한(x)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null)
            throw new Exception401("인증되지 않았습니다.");
        return "board/save-form";
    }

    @GetMapping("/boards/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest req) {
        // 인증(v).권한(v)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null)
            throw new Exception401("인증되지 않았습니다.");
        Board board = boardService.수정폼게시글정보(id, sessionUser.getId());

        req.setAttribute("model", board);
        return "board/update-form";
    }

    // /boards?content=사과
    // select * from board_tb where content like '%사과%';
    @GetMapping("/boards/{id}")
    public String detail(@PathVariable("id") int id, HttpServletRequest req) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Integer sessionUserId = sessionUser == null ? null : sessionUser.getId();
        BoardResponse.DetailDTO dto = boardService.상세보기(id, sessionUserId);
        req.setAttribute("model", dto);
        return "board/detail";
    }

    @PostMapping("/boards/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        // 인증(v).권한(v)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null)
            throw new Exception401("인증되지 않았습니다.");

        try {
            boardService.게시글삭제(id, sessionUser.getId());
        } catch (Exception e) {
            throw new Exception500("댓글이 있는 게시글을 삭제할 수 없습니다");
        }

        return "redirect:/";
    }

    @GetMapping("/api/boards/{id}")
    public @ResponseBody BoardResponse.DetailDTO apiDetail(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Integer sessionUserId = sessionUser == null ? null : sessionUser.getId();
        BoardResponse.DetailDTO dto = boardService.상세보기(id, sessionUserId);
        return dto;
    }
}
