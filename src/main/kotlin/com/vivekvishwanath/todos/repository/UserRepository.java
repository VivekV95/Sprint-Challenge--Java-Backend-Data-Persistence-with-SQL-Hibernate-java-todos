package com.vivekvishwanath.todos.repository;

import com.vivekvishwanath.todos.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
