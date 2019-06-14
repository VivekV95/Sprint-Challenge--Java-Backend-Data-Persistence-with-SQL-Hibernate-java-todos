package com.lambdaschool.starthere.repository

import com.sun.tools.javac.comp.Todo
import org.springframework.data.repository.CrudRepository

interface ToDoRepository: CrudRepository<Todo, Long>