package com.bazlur.api.controller;

import com.bazlur.api.TestContext;
import com.bazlur.api.WebServiceDemoApplication;
import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.exception.TodoNotFoundException;
import com.bazlur.api.service.TodoService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestContext.class, WebServiceDemoApplication.class})
@WebAppConfiguration
public class TodoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TodoService todoServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(todoServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void add_EmptyTodo_ShouldReturnValidationErrorForTitle() throws Exception {
        TodoDto dto = new TodoDto();

        mockMvc.perform(post("/api/todo/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].path", is("title")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("The title cannot be empty.")));

        verifyZeroInteractions(todoServiceMock);
    }

    @Test
    public void add_TitleAndDescriptionWithMoreLength_ShouldReturnValidationErrorsForTitleAndDescription() throws Exception {
        String title = RandomStringUtils.random(Todo.MAX_LENGTH_TITLE + 1);
        String description = RandomStringUtils.random(Todo.MAX_LENGTH_DESCRIPTION + 1);

        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withDescription(description)
                .withTitle(title)
                .build();

        mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("title", "description")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "The maximum length of the description is 500 characters.",
                        "The maximum length of the title is 100 characters."
                )));
        verifyZeroInteractions(todoServiceMock);
    }


    @Test
    public void add_NewTodo_ShouldAddTodoAndReturnAddedTodo() throws Exception {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withDescription("description")
                .withTitle("title")
                .build();

        Todo added = Todo.TodoBuilder.aTodo()
                .withId(1L)
                .withDescription("description")
                .withTitle("title")
                .build();

        when(todoServiceMock.add(any(TodoDto.class))).thenReturn(added);

        mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.title", is("title")));

        ArgumentCaptor<TodoDto> dtoCaptor = ArgumentCaptor.forClass(TodoDto.class);
        verify(todoServiceMock, times(1)).add(dtoCaptor.capture());

        verifyNoMoreInteractions(todoServiceMock);

        TodoDto dtoArgument = dtoCaptor.getValue();
        assertNull(dtoArgument.getId());
        assertThat(dtoArgument.getDescription(), is("description"));
        assertThat(dtoArgument.getTitle(), is("title"));
    }


    @Test
    public void findById_TodoEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(todoServiceMock.findById(1L)).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(get("/api/todo/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(todoServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(todoServiceMock);
    }


    @Test
    public void findAll_TodosFound_ShouldReturnFoundTodoEntries() throws Exception {
        Todo first = Todo.TodoBuilder.aTodo()
                .withId(1L)
                .withDescription("Lorem ipsum")
                .withTitle("Foo")
                .build();

        Todo second = Todo.TodoBuilder.aTodo()
                .withId(2L)
                .withDescription("Lorem ipsum")
                .withTitle("Bar")
                .build();

        when(todoServiceMock.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum")))
                .andExpect(jsonPath("$[0].title", is("Foo")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("Lorem ipsum")))
                .andExpect(jsonPath("$[1].title", is("Bar")));

        verify(todoServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(todoServiceMock);
    }


    @Test
    public void findById_TodoFound_ShouldReturnFoundTodo() throws Exception {
        Todo found = Todo.TodoBuilder.aTodo()
                .withId(1L)
                .withDescription("Lorem ipsum")
                .withTitle("Foo")
                .build();

        when(todoServiceMock.findById(1L)).thenReturn(found);

        mockMvc.perform(get("/api/todo/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Lorem ipsum")))
                .andExpect(jsonPath("$.title", is("Foo")));

        verify(todoServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(todoServiceMock);
    }


    @Test
    public void update_EmptyTodo_ShouldReturnValidationErrorForTitle() throws Exception {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withId(1L)
                .build();

        mockMvc.perform(put("/api/todo/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].path", is("title")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("The title cannot be empty.")));

        verifyZeroInteractions(todoServiceMock);
    }

    @Test
    public void update_TitleAndDescriptionWithLongLengthThenExpected_ShouldReturnValidationErrorsForTitleAndDescription() throws Exception {
        String title = RandomStringUtils.random(Todo.MAX_LENGTH_TITLE + 1);
        String description = RandomStringUtils.random(Todo.MAX_LENGTH_DESCRIPTION + 1);

        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withId(1L)
                .withTitle(title)
                .withDescription(description)
                .build();


        mockMvc.perform(put("/api/todo/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("title", "description")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "The maximum length of the description is 500 characters.",
                        "The maximum length of the title is 100 characters."
                )));

        verifyZeroInteractions(todoServiceMock);
    }

    @Test
    public void update_TodoNotFound_ShouldReturnHttpStatusCode404() throws Exception {

        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withId(3L)
                .withTitle("title")
                .withDescription("description")
                .build();

        when(todoServiceMock.update(any(TodoDto.class))).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(put("/api/todo/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isNotFound());

        ArgumentCaptor<TodoDto> dtoCaptor = ArgumentCaptor.forClass(TodoDto.class);
        verify(todoServiceMock, times(1)).update(dtoCaptor.capture());
        verifyNoMoreInteractions(todoServiceMock);

        TodoDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(3L));
        assertThat(dtoArgument.getDescription(), is("description"));
        assertThat(dtoArgument.getTitle(), is("title"));
    }

    @Test
    public void update_TodoFound_ShouldUpdateTodoAndReturnIt() throws Exception {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withId(1L)
                .withTitle("title")
                .withDescription("description")
                .build();

        Todo updated = Todo.TodoBuilder.aTodo()
                .withId(1L)
                .withDescription("description")
                .withTitle("title")
                .build();

        when(todoServiceMock.update(any(TodoDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/todo/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.title", is("title")));

        ArgumentCaptor<TodoDto> dtoCaptor = ArgumentCaptor.forClass(TodoDto.class);
        verify(todoServiceMock, times(1)).update(dtoCaptor.capture());
        verifyNoMoreInteractions(todoServiceMock);

        TodoDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(1L));
        assertThat(dtoArgument.getDescription(), is("description"));
        assertThat(dtoArgument.getTitle(), is("title"));
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsBytes(object);
    }
}