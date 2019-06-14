package com.vivekvishwanath.todos.controller


import com.vivekvishwanath.todos.exception.ResourceNotFoundException
import com.vivekvishwanath.todos.model.Todo
import com.vivekvishwanath.todos.model.User
import com.vivekvishwanath.todos.service.UserService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping("/users")
class UserController {

    companion object{
        private val logger = KotlinLogging.logger{}
    }

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/mine"], produces = ["application/json"])
    fun getMyUserAndTodos(request: HttpServletRequest): ResponseEntity<*> {
        logger.info { "users/mine (GET) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        val user = userService.findByUsername(request.userPrincipal.name)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/users"], produces = ["application/json"])
    fun listAllUsers(request: HttpServletRequest): ResponseEntity<*> {
        logger.info { "users/users (GET) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        val myUsers = userService.findAll()
        return ResponseEntity(myUsers, HttpStatus.OK)
    }


    @Throws(ResourceNotFoundException::class)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/user/{userId}"], produces = ["application/json"])
    fun getUser(request: HttpServletRequest, @PathVariable userId: Long?): ResponseEntity<*> {
        logger.info { "users/user/$userId (GET) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        val u = userId?.let { userService.findUserById(it) } ?:
        throw ResourceNotFoundException("User with id $userId not found")
        return ResponseEntity(u, HttpStatus.OK)
    }


    @GetMapping(value = ["/getusername"], produces = ["application/json"])
    @ResponseBody
    fun getCurrentUserName(request: HttpServletRequest, authentication: Authentication): ResponseEntity<*> {
        logger.info { "users/getusername (GET) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        return ResponseEntity(authentication.principal, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = ["/user"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid @RequestBody newuser: User): ResponseEntity<*> {
        logger.info { "users/user (POST) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        var newuser = newuser
        newuser = userService.save(newuser)

        // set the location header for the newly created resource
        val responseHeaders = HttpHeaders()
        val newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newuser.userid)
                .toUri()
        responseHeaders.location = newUserURI

        return ResponseEntity<Any>(null, responseHeaders, HttpStatus.CREATED)
    }


    @PutMapping(value = ["/user/{id}"])
    fun updateUser(request: HttpServletRequest, @RequestBody updateUser: User,
                   @PathVariable id: Long): ResponseEntity<*> {
        logger.info { "users/user/$id (PUT) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        userService.update(updateUser, id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    fun deleteUserById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*> {
        logger.info { "users/user/$id (DELETE) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        userService.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping(value = ["/todo/{userid}"], consumes = ["application/json"], produces = ["application/json"])
    fun addTodoToUser(@PathVariable userid: Long,
                      request: HttpServletRequest,
                      authentication: Authentication,
                      @Valid @RequestBody todo: Todo): ResponseEntity<*> {
        logger.info { "users/todo/$userid (POST) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        return ResponseEntity(userService.addTodoToUser(todo, userid), HttpStatus.OK)
    }

    @PutMapping(value = ["todos/todoid/{todoid}"],
            consumes = ["application/json"], produces = ["application/json"])
    fun updateTodo(@Valid @RequestBody todo: Todo, @PathVariable todoid: Long): ResponseEntity<*> {
        logger.info { "users/todos/todoid/$todoid (PUT) accessed" +
                " on ${SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())}" }
        return ResponseEntity(userService.updateTodoById(todo, todoid), HttpStatus.OK)
    }
}