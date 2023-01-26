package com.example.todo.userapi.controller;


import com.example.todo.userapi.dto.UserSignUpDTO;
import com.example.todo.userapi.dto.UserSignUpResponseDTO;
import com.example.todo.userapi.exception.DuplicatedEmailException;
import com.example.todo.userapi.exception.NoRegisteredArgumentsException;
import com.example.todo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserApiController {
    private final UserService userService;

    // 회원가입 요청 처리
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Validated @RequestBody UserSignUpDTO userSignUpDTO, BindingResult result){
        if(result.hasErrors()){
            log.info("/api/auth/signup POST! - {}", userSignUpDTO);
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.toString());
        }
        try {
            UserSignUpResponseDTO userSignUpResponseDTO = userService.create(userSignUpDTO);
            return ResponseEntity
                    .ok()
                    .body(userSignUpResponseDTO);
        } catch (NoRegisteredArgumentsException e) {
            // 1. dto 가 null 일때
            log.warn("필수 가입 정보를 다시 확인하세요");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }catch (DuplicatedEmailException e){
            // 2. 이메일이 중복될때
            log.warn("중복되었습니다. 다른 이메일을 작성해주세요");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
