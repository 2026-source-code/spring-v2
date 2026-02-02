package com.example.boardv1.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(String username, String password, String email) {
        // 1. 유저네임 중복체크
        User findUser = userRepository.findByUsername(username);
        System.out.println("-----ssar1이 없어서 findUser는 null : " + findUser);
        if (findUser != null) {
            throw new RuntimeException("유저네임이 중복되었습니다");
        }

        // 2. 비영속 객체
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // 3. save()호출
        userRepository.save(user);
    }
}
