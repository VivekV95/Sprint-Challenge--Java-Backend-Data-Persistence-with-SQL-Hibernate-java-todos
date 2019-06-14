package com.vivekvishwanath.todos.repository

import com.vivekvishwanath.todos.model.Todo
import org.springframework.data.repository.CrudRepository

interface ToDoRepository: CrudRepository<Todo, Long>