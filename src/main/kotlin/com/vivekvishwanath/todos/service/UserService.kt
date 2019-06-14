package com.vivekvishwanath.todos.service

import com.vivekvishwanath.todos.model.Todo
import com.vivekvishwanath.todos.model.User

interface UserService {

    fun findAll(): List<User>

    fun findUserById(id: Long): User

    fun delete(id: Long)

    fun save(user: User): User

    fun findByUsername(username: String): User

    fun update(user: User, id: Long): User

    fun addTodoToUser(todo: Todo, id: Long): User

    fun updateTodoById(updatedTodo: Todo, id: Long): User
}