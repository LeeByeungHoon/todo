package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class TodoServiceTest {
    @Autowired
    TodoService todoService;

    @BeforeEach
    void beforeInsert(){
        TodoCreateRequestDTO todo1 = TodoCreateRequestDTO.builder()
                .title("할일1")
                .build();
        TodoCreateRequestDTO todo2 = TodoCreateRequestDTO.builder()
                .title("할일2")
                .build();
        TodoCreateRequestDTO todo3 = TodoCreateRequestDTO.builder()
                .title("할일3")
                .build();

        todoService.create(todo1);
        todoService.create(todo2);
        todoService.create(todo3);
    }

    @Test
    @DisplayName("새로운 할 일을 등록하면 생성되는 리스트는 할 일이 4개 들어있어야 한다.")
    void createTest(){
        // given
        TodoCreateRequestDTO todo4 = TodoCreateRequestDTO.builder()
                .title("할일4")
                .build();
        // when
        TodoListResponseDTO responseDTO = todoService.create(todo4);

        // then
        List<TodoDetailResponseDTO> todos = responseDTO.getTodos();
        assertEquals(4, todos.size());
        todos.forEach(System.out::println);
    }

    @Test
    @DisplayName("2번째 할일의 제목을 수정수정으로 수정하고 할일 완료 처리를 해야한다.")
    void modifyTest(){
        // given
        String newTitle = "수정수정";
        boolean newDone = true;
        TodoDetailResponseDTO targetTodo = todoService.retrieve().getTodos().get(1);
        TodoModifyRequestDTO modifyRequestDTO = TodoModifyRequestDTO.builder()
                .title(newTitle)
                .done(newDone)
                .build();
        TodoListResponseDTO responseDTO = todoService.modify(targetTodo.getId(), modifyRequestDTO);

        // then
        assertEquals("수정수정", responseDTO.getTodos().get(1).getTitle());
        assertTrue(responseDTO.getTodos().get(1).isDone());

        responseDTO.getTodos().forEach(System.out::println);
    }

    @Test
    @DisplayName("3번째 할일을 삭제해야한다.")
    void deleteTest(){
        TodoListResponseDTO todos = todoService.retrieve();
        String todoId = todos.getTodos().get(2).getId();
        TodoListResponseDTO todo = todoService.delete(todoId);

        assertEquals(2, todo.getTodos().size());
    }
}