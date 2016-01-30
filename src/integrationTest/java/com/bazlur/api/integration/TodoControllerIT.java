package com.bazlur.api.integration;

import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */

public class TodoControllerIT extends AbstractIntegrationTest {

    public static final String TITLE_1 = "hello world";
    public static final String TITLE_2 = "hello world";
    public static final String DESCRIPTION_1 = "Hello world description";
    public static final String DESCRIPTION_2 = "Hello world description";


    TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository repository;

    @Before
    public void setUp() throws MalformedURLException {
        Todo todo1 = Todo.TodoBuilder.aTodo().withTitle(TITLE_1).withDescription(DESCRIPTION_1).build();
        Todo todo2 = Todo.TodoBuilder.aTodo().withTitle(TITLE_2).withDescription(DESCRIPTION_2).build();
        repository.save(todo1);
        repository.save(todo2);

        restTemplate = new TestRestTemplate();
    }

    @Test
    public void findAllTodos() throws Exception {
        List<TodoDto> list = getList("/api/todo/", TodoDto.class);
        assertEquals(0, list.size());
    }

    @Test
    public void insertNewTodo() {
        TodoDto todo = TodoDto.TodoDtoBuilder.aTodoDto()
                .withTitle("Hello world")
                .withDescription("Hello world2")
                .build();

    }

    @Test
    public void addItemShouldReturnSavedItem() {

    }

}
