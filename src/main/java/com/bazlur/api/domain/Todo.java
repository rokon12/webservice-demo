package com.bazlur.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
@Entity
@Table(name = "todos")
public class Todo extends BaseEntity {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

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


    public static class TodoBuilder {
        private String description;
        private String title;
        private Long id;

        private TodoBuilder() {
        }

        public static TodoBuilder aTodo() {
            return new TodoBuilder();
        }

        public TodoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TodoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TodoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TodoBuilder but() {
            return aTodo().withDescription(description).withTitle(title);
        }

        public Todo build() {
            Todo todo = new Todo();
            todo.setDescription(description);
            todo.setTitle(title);
            todo.setId(id);

            return todo;
        }
    }
}
