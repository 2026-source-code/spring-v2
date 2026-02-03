package com.example.boardv1.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(UserRepository.class)
@DataJpaTest // EntityManger가 ioc에 등록됨
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById_test() {
        int id = 5;

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 아이디로 유저를 찾을 수 없어요"));

        System.out.println("user : " + user);
    }

    @Test
    public void save_fail_test() {
        // given
        User user = new User(); // 비영속 객체
        user.setUsername("cos");
        user.setPassword("1234");
        user.setEmail("cos@nate.com");

        // when
        User findUser = userRepository.save(user); // 영속화됨

        // eye
        System.out.println(findUser);
    }

    @Test
    public void save_test() {
        // given
        User user = new User(); // 비영속 객체
        user.setUsername("love");
        user.setPassword("1234");
        user.setEmail("love@nate.com");

        // when
        User findUser = userRepository.save(user); // 영속화됨

        // eye
        System.out.println(findUser);
    }

    @Test
    public void findByUsername_test() {
        // given
        String username = "good";

        // when (ssar, 1234)
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 user를 찾을 수 없어요"));

        // eye
        System.out.println(findUser);
    }

}
