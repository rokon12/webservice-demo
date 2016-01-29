package com.bazlur.api.service;

import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.exception.TodoNotFoundException;
import com.bazlur.api.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
@Service(value = "todoService")
public class TodoServiceImpl implements TodoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoServiceImpl.class);


    private TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    @Override
    public Todo add(TodoDto added) {
        LOGGER.debug("Adding a new to-do entry with information: {}", added);

        Todo todo = Todo.TodoBuilder.aTodo()
                .withTitle(added.getTitle())
                .withDescription(added.getDescription())
                .build();

        return todoRepository.save(todo);
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public void deleteById(Long id) throws TodoNotFoundException {
        LOGGER.debug("Deleting a to-do entry with id: {}", id);

        todoRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Todo> findAll() {
        LOGGER.debug("Finding all to-do entries");

        return todoRepository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {TodoNotFoundException.class})
    @Override
    public Todo findById(Long id) throws TodoNotFoundException {
        LOGGER.debug("Finding a to-do entry with id: {}", id);

        Todo found = todoRepository.findOne(id);
        LOGGER.debug("Found to-do entry: {}", found);

        if (found == null) {
            throw new TodoNotFoundException("No to-entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public Todo update(TodoDto updated) throws TodoNotFoundException {
        LOGGER.debug("Updating contact with information: {}", updated);
        Todo model = findById(updated.getId());
        LOGGER.debug("Found a to-do entry: {}", model);

        model.setDescription(updated.getDescription());
        model.setTitle(updated.getTitle());

        return model;
    }
}
