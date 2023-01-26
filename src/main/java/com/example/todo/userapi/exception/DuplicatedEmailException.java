package com.example.todo.userapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedEmailException extends RuntimeException {

    // 기본 생성자

    // 에러 메세지를 처리하는 생성자
    public DuplicatedEmailException(String message){
        super(message);
    }
}
