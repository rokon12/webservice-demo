package com.bazlur.api.controller;

import com.bazlur.api.domain.Todo;
import com.bazlur.api.dto.TodoDto;
import com.bazlur.api.exception.TodoNotFoundException;
import com.bazlur.api.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoDto> findAll() {
        LOGGER.debug("Finding all todo entries.");

        List<Todo> models = todoService.findAll();
        LOGGER.debug("Found {} to-do entries.", models.size());

        return models.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public TodoDto add(@Valid @RequestBody TodoDto todo) {
        LOGGER.debug("Adding a new to-do entry with information: {}", todo);

        Todo added = todoService.add(todo);
        LOGGER.debug("Added a to-do entry with information: {}", added);

        return convertToDto(added);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TodoDto findById(@PathVariable("id") Long id) throws TodoNotFoundException {
        LOGGER.debug("Finding to-do entry with id: {}", id);

        Todo found = todoService.findById(id);
        LOGGER.debug("Found to-do entry with information: {}", found);

        return convertToDto(found);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TodoDto update(@Valid @RequestBody TodoDto dto, @PathVariable("id") Long todoId) throws TodoNotFoundException {
        LOGGER.debug("Updating a to-do entry with information: {}", dto);

        Todo updated = todoService.update(dto);
        LOGGER.debug("Updated the information of a to-entry to: {}", updated);

        return convertToDto(updated);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable("id") Long id) throws TodoNotFoundException {
        LOGGER.debug("Deleting a to-do entry with id: {}", id);
        todoService.deleteById(id);

        LOGGER.debug("Deleted a to-do entry with id: {}", id);
    }

    private TodoDto convertToDto(Todo model) {
        TodoDto todoDto = new TodoDto();

        todoDto.setId(model.getId());
        todoDto.setDescription(model.getDescription());
        todoDto.setTitle(model.getTitle());

        return todoDto;
    }
}
