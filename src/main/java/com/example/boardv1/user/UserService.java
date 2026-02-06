package com.example.boardv1.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boardv1._core.errors.ex.Exception400;
import com.example.boardv1._core.errors.ex.Exception401;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(String username, String password, String email) {
        // 1. 유저네임 중복체크
        Optional<User> optUser = userRepository.findByUsername(username);

        if (optUser.isPresent()) {
            throw new Exception400("유저네임이 중복되었습니다");
        }

        // 2. 비영속 객체
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // 3. save()호출
        userRepository.save(user);
    }

    public User 로그인(String username, String password) {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception401("username을 찾을 수 없어요"));

        if (!findUser.getPassword().equals(password)) {
            throw new Exception401("패스워드가 일치하지 않아요");
        }
        return findUser;
    }
}
