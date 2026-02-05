package com.example.boardv1.reply;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {

    private final EntityManager em;

    public Optional<Reply> findById(int id) {
        Reply reply = em.find(Reply.class, id);
        return Optional.ofNullable(reply);
    }

    public void save(Reply reply) {
        em.persist(reply);
    }

    public void delete(Reply reply) {
        em.remove(reply);
    }

}
