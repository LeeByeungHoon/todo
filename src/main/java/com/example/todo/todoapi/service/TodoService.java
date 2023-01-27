package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    // 할 일 목록 조회
    @Transactional
    public TodoListResponseDTO retrieve() {
        List<TodoEntity> entityList = todoRepository.findAll();
        List<TodoDetailResponseDTO> dtoList = entityList.stream()
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());
        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    public TodoDetailResponseDTO detail(final String todoId) {
        TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow();

        return new TodoDetailResponseDTO(todoEntity);
    }

    // 할 일 등록
    public TodoListResponseDTO create(final TodoCreateRequestDTO todoCreateRequestDTO) throws RuntimeException{
        todoRepository.save(todoCreateRequestDTO.toEntity());
        log.info("할 일이 저장되었습니다. 제목 : {}", todoCreateRequestDTO.getTitle());
        return retrieve(); // 완료 시 전체 리스트 출력 실행
    }

    // 할 일 수정 (제목, 할일 완료 여부)
    public TodoListResponseDTO modify(final String id,final TodoModifyRequestDTO todoModifyRequestDTO){
        Optional<TodoEntity> targetEntity = todoRepository.findById(id);
        targetEntity.ifPresent(entity -> {
            entity.setTitle(todoModifyRequestDTO.getTitle());
            entity.setDone(todoModifyRequestDTO.isDone());
            todoRepository.save(entity);
            log.info("할 일이 수정되었습니다. 제목 : {}, 결과 : {}", entity.getTitle(), entity.isDone());
        });
        return retrieve();
    }

    // 할 일 삭제
    public TodoListResponseDTO delete(final String id){
        try {
            todoRepository.deleteById(id);
        } catch (Exception e) {
            log.error("아이디가 존재하지 않아 삭제에 실패했습니다. - ID: {}, err: {}.", id, e.getMessage());
            throw new RuntimeException("아이디가 존재하지 않아 삭제에 실패했습니다.");
        }

        return retrieve();
    }
    // 상세 조회
//    public TodoDetailResponseDTO detail(final String id){
//        Optional<TodoEntity> detail = todoRepository.findById(id);
//        detail.isPresent()
//        return
//    }
}
