package com.bazlur.api.service;

import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.exception.TodoNotFoundException;

import java.util.List;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public interface TodoService {
    Todo add(TodoDto added);

    void deleteById(Long id) throws TodoNotFoundException;

    List<Todo> findAll();

    Todo findById(Long id) throws TodoNotFoundException;

    Todo update(TodoDto updated) throws TodoNotFoundException;
}
