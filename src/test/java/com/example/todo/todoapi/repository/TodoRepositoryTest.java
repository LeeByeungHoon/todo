package com.example.todo.todoapi.repository;

import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.userapi.entity.UserEntity;
import com.example.todo.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Commit // test 실행 후 커밋
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    /*@BeforeEach
    void insertTest(){
        TodoEntity todo1 = TodoEntity.builder()
                .title("저녁 장보기")
                .build();
        TodoEntity todo2 = TodoEntity.builder()
                .title("책 읽기")
                .build();
        TodoEntity todo3 = TodoEntity.builder()
                .title("목욕하기")
                .build();

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
    }*/

    @Test
    @DisplayName("첫번째 일정을 완료")
    void todoTest(){
        String title = "책 읽기";
        TodoEntity todoEntity = todoRepository.findById(title).orElseThrow();
        todoEntity.setDone(true);
        todoRepository.save(todoEntity);
//        assertEquals(3, list.size());
    }
    @Test
    @DisplayName("할일 목록을 조회하면 리스트 사이즈가 3이어야 한다")
    void findAllTest(){
        List<TodoEntity> list = todoRepository.findAll();

        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("회원의 할 일을 등록해야 한다.")
    void saveTodoWithUserTest(){
        String email = "altm885@naver.co";
        UserEntity user = userRepository.findByEmail(email);

        //given
        TodoEntity todo = TodoEntity.builder()
                .title("12123")
                .user(user)
                .build();

        //when
        TodoEntity savedTodo = todoRepository.save(todo);
        System.out.println("savedTodo = " + savedTodo);
        //then
        assertEquals(savedTodo.getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("특정 회원의 할일 목록을 조회해야 한다.")
    @Transactional
    void findByUserTest(){
        // given
        String userId ="402880bf85ebe66b0185ecfd76fd0002";
        //when
        List<TodoEntity> todos = todoRepository.findByUserId(userId);

        todos.forEach(System.out::println);
        assertEquals(2, todos.size());
    }

}