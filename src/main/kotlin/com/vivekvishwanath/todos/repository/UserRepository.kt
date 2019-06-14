package com.vivekvishwanath.todos.repository

import com.vivekvishwanath.todos.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User
}
