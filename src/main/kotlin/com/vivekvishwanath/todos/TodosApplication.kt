package com.vivekvishwanath.todos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@EnableJpaAuditing
@SpringBootApplication
class TodosApplication

fun main(args: Array<String>) {
    runApplication<TodosApplication>(*args)
}
