package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.LoginRequestDTO;
import com.example.todo.userapi.dto.LoginResponseDTO;
import com.example.todo.userapi.dto.UserSignUpDTO;
import com.example.todo.userapi.dto.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("중복된 이메일이 포함된 회원정보로 가입하면 RuntimeException이 발생해야 한다.")
    void validateEmailTest(){
        // given
        UserSignUpDTO userInfo = UserSignUpDTO.builder()
                .email("abd")
                .password("1234")
                .userName("ㅋㅋ")
                .build();

        // when, then
        assertThrows(RuntimeException.class, () -> {
            userService.create(userInfo);
        });
    }

    @Test
    @DisplayName("검증된 회원정보로 가입하면 회원가입에 성공해야 한다.")
    void createTest(){
        // given
        UserSignUpDTO userInfo = UserSignUpDTO.builder()
                .email("abbdc232d")
                .password("1234")
                .userName("ㅋㅋ")
                .build();

        UserSignUpResponseDTO userSignUpResponseDTO = userService.create(userInfo);
        // when, then
        System.out.println("userSignUpResponseDTO = " + userSignUpResponseDTO);
        assertEquals("ㅋㅋ", userSignUpResponseDTO.getUserName());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 시도하면 Exception 이 발생 해야한다")
    void noUserTest(){
        //given
        String email="123@123.com";
        String password = "1234";



        //then
        assertThrows(RuntimeException.class, () -> {
            //when
            userService.getByCredentials(new LoginRequestDTO(email,password));
        });
    }

    @Test
    @DisplayName("정확한 정보로 로그인을 시도하면 회원정보가 반환 돼야한다")
    void userTest(){
        //given
        String email="altm885@naver.com";
        String password = "qudgns6804!";

        //when
        LoginResponseDTO userInfo = userService.getByCredentials(new LoginRequestDTO(email,password));


        //then
        assertEquals("altm885@naver.com",userInfo.getEmail());
        System.out.println(userInfo);
    }
}