package com.example.todo.todoapi.controller;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")

// CORS 허용 설정
@CrossOrigin
public class TodoApiController {

    private final TodoService todoService;

    // 할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(
           @Validated @RequestBody TodoCreateRequestDTO requestDTO,
           @AuthenticationPrincipal String userId,
           BindingResult result
    ){
        if (result.hasErrors()){
            log.warn("DTO 검증 에러 발생 : {}", result.getFieldErrors());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldErrors());
        }
        try {
            TodoListResponseDTO responseDTO = todoService.create(requestDTO, userId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    // 할 일 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(
            @PathVariable("id") String todoId,
            @AuthenticationPrincipal String userId
    ){
        log.info("/api/todos/{} DELETE request!", todoId);

        if(todoId == null || todoId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해주세요"));
        }

        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId, userId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }

    }
    // 할 일 목록요청 (GET)
    @GetMapping
    public ResponseEntity<?> getTodoList(
            // 인증 완료처리시 등록했던 값을 넣어줌
            @AuthenticationPrincipal String userId
        ){
        try {
            TodoListResponseDTO list = todoService.retrieve(userId);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoDetail(@PathVariable("id")String todoId){
        if(todoId == null || todoId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해주세요"));
        }
        try {
            TodoDetailResponseDTO list = todoService.detail(todoId);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }
    }
    // 할 일 수정요청 (PUT, PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchTodo(
            @PathVariable("id") String todoId,
            @Validated @RequestBody TodoModifyRequestDTO todoModifyRequestDTO,
            @AuthenticationPrincipal String userId,
            BindingResult result
    ){
        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생 : {}", result.getFieldErrors());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldErrors());
        }
            try {
                TodoListResponseDTO modify = todoService.modify(todoId, todoModifyRequestDTO, userId);
                return ResponseEntity.ok().body(modify);
            } catch (Exception e) {
                log.error(e.getMessage());
                return ResponseEntity
                        .badRequest()
                        .body(TodoListResponseDTO.builder().error(e.getMessage()).build());
            }
    }

}