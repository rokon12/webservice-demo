package com.bazlur.api.repository;

import com.bazlur.api.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
