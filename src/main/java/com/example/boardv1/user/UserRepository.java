package com.example.boardv1.user;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    // DI
    private final EntityManager em;

    // 회원가입할때 insert
    public User save(User user) {
        em.persist(user);
        return user;
    }

    // 로그인할때 username으로 조회해서 password 검증
    public User findByUsername(String username) {
        Query query = em.createQuery("select u from User u where u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            User findUser = (User) query.getSingleResult();
            return findUser;
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(int id) {
        return em.find(User.class, id);
    }
}
