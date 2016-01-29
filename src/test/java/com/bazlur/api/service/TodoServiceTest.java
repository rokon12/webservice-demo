package com.bazlur.api.service;

import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.exception.TodoNotFoundException;
import com.bazlur.api.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public class TodoServiceTest {

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "description";
    private static final String DESCRIPTION_UPDATED = "updatedDescription";
    private static final String TITLE = "title";
    private static final String TITLE_UPDATED = "updatedTitle";

    private TodoService service;
    private TodoRepository repositoryMock;

    @Before
    public void setUp() {
        repositoryMock = mock(TodoRepository.class);
        service = new TodoServiceImpl(repositoryMock);
    }


    @Test
    public void add_NewTodoEntry_ShouldSaveTodoEntry() {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .build();

        service.add(dto);

        ArgumentCaptor<Todo> toDoArgument = ArgumentCaptor.forClass(Todo.class);
        verify(repositoryMock, times(1)).save(toDoArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Todo model = toDoArgument.getValue();

        assertNull(model.getId());
        assertThat(model.getDescription(), is(dto.getDescription()));
        assertThat(model.getTitle(), is(dto.getTitle()));
    }

    @Test
    public void deleteById_TodoFound_ShouldDeleteTodo() throws TodoNotFoundException {

        service.deleteById(ID);
        verify(repositoryMock, times(1)).delete(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll_ShouldReturnListOfTodos() {
        List<Todo> models = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Todo> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertThat(actual, is(models));
    }

    @Test
    public void findById_TodoFound_ShouldReturnFoundTodo() throws TodoNotFoundException {
        Todo model = Todo.TodoBuilder.aTodo()
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .withId(ID)
                .build();

        when(repositoryMock.findOne(ID)).thenReturn(model);

        Todo actual = service.findById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(repositoryMock);

        assertThat(actual, is(model));
    }

    @Test(expected = TodoNotFoundException.class)
    public void findById_TodoEntryNotFound_ShouldThrowException() throws TodoNotFoundException {
        when(repositoryMock.findOne(ID)).thenReturn(null);

        service.findById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update_TodoFound_ShouldUpdateTodo() throws TodoNotFoundException {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .withId(ID)
                .build();

        Todo model = Todo.TodoBuilder.aTodo()
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .withId(ID)
                .build();

        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Todo actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertThat(model.getId(), is(dto.getId()));
        assertThat(model.getDescription(), is(dto.getDescription()));
        assertThat(model.getTitle(), is(dto.getTitle()));
    }

    @Test(expected = TodoNotFoundException.class)
    public void update_TodoNotFound_ShouldThrowException() throws TodoNotFoundException {
        TodoDto dto = TodoDto.TodoDtoBuilder.aTodoDto()
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .withId(ID)
                .build();

        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }

}