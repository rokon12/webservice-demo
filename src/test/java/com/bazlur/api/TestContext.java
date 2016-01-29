package com.bazlur.api;

import com.bazlur.api.service.TodoService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
@Configuration
public class TestContext {

    @Bean
    public TodoService todoService() {

        return Mockito.mock(TodoService.class);
    }
}
