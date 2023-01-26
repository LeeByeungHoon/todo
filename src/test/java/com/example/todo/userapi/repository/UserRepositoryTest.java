package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입에 성공해야 한다.")
    @Transactional
    @Rollback
    void saveTest(){
        // given
        UserEntity user = UserEntity.builder()
                .email("abd")
                .password("123")
                .userName("ㅋㅋ")
                .build();
        // when
        UserEntity savedUser = userRepository.save(user);
        // then
        assertNotNull(savedUser);
    }

    @Test
    @DisplayName("이메일로 회원을 조회해야 한다")
    void findByEmailTest(){
        // given
        String email = "abd";
        // when
        UserEntity findByEmail = userRepository.findByEmail(email);
        // then
        assertEquals("ㅋㅋ", findByEmail.getUserName());
    }

    @Test
    @DisplayName("이메일 중복을 체크해야 한다")
    void existEmailTest(){
        // given
        String email = "altm885@naver.com";
//        String email = "abd";

        // when
        boolean flag = userRepository.existsByEmail(email);

        // then
        assertFalse(flag);
    }
}