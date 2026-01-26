package com.example.boardv1.board;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class BoardNativeRepository {

    private final EntityManager em;

    public Board findById(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        query.setParameter("id", id);

        Board board = (Board) query.getSingleResult();
        return board;
    }

    public List<Board> findAll() {
        Query query = em.createNativeQuery("select * from board_tb order by id desc", Board.class);
        List<Board> list = query.getResultList();
        return list;
    }

    public int save(String title, String content) {
        Query query = em
                .createNativeQuery("insert into board_tb(title, content, created_at) values(:title, :content, now())");
        query.setParameter("content", content);
        query.setParameter("title", title);
        return query.executeUpdate();

    }

    public int deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public int updateById(int id, String title, String content) {
        Query query = em.createNativeQuery("update board_tb set title = :title, content = :content where id = :id");
        query.setParameter("id", id);
        query.setParameter("title", title);
        query.setParameter("content", content);
        return query.executeUpdate();
    }

}
