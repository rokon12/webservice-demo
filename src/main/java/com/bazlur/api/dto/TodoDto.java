package com.bazlur.api.dto;

import com.bazlur.api.domain.Todo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public class TodoDto {
    private Long id;

    @Length(max = Todo.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Todo.MAX_LENGTH_TITLE)
    private String title;

    public TodoDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public static class TodoDtoBuilder {
        private Long id;
        private String description;
        private String title;

        private TodoDtoBuilder() {
        }

        public static TodoDtoBuilder aTodoDto() {
            return new TodoDtoBuilder();
        }

        public TodoDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TodoDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TodoDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TodoDtoBuilder but() {
            return aTodoDto().withId(id).withDescription(description).withTitle(title);
        }

        public TodoDto build() {
            TodoDto todoDto = new TodoDto();
            todoDto.setId(id);
            todoDto.setDescription(description);
            todoDto.setTitle(title);
            return todoDto;
        }
    }
}
