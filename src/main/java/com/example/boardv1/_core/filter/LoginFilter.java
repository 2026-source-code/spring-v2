package com.example.boardv1._core.filter;

import java.io.IOException;

import com.example.boardv1.user.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("------Login Filter-----------");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 예외: GET /boards/{id} 상세보기는 인증 없이 허용
        String uri = req.getRequestURI();
        if ("GET".equals(req.getMethod()) && uri.matches(".*/boards/\\d+$")) {
            chain.doFilter(request, response);
            return;
        }

        // /boards/*, /replies/* 나머지 전부 인증 필요
        if (sessionUser == null) {
            resp.sendRedirect("/login-form");
            return;
        }

        chain.doFilter(request, response);
    }

}
