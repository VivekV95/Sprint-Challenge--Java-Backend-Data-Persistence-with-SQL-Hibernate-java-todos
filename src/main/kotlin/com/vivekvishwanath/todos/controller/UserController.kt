package com.vivekvishwanath.todos.controller


import com.vivekvishwanath.todos.model.Todo
import com.vivekvishwanath.todos.model.User
import com.vivekvishwanath.todos.service.UserService
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
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/mine"], produces = ["application/json"])
    fun getMyUserAndTodos(request: HttpServletRequest): ResponseEntity<*> {
        val user = userService.findByUsername(request.userPrincipal.name)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/users"], produces = ["application/json"])
    fun listAllUsers(request: HttpServletRequest): ResponseEntity<*> {
        val myUsers = userService.findAll()
        return ResponseEntity(myUsers, HttpStatus.OK)
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/user/{userId}"], produces = ["application/json"])
    fun getUser(request: HttpServletRequest, @PathVariable userId: Long?): ResponseEntity<*> {
        val u = userId?.let { userService.findUserById(it) }
        return ResponseEntity(u, HttpStatus.OK)
    }


    @GetMapping(value = ["/getusername"], produces = ["application/json"])
    @ResponseBody
    fun getCurrentUserName(request: HttpServletRequest, authentication: Authentication): ResponseEntity<*> {
        return ResponseEntity(authentication.principal, HttpStatus.OK)
    }

    /* @GetMapping(value = ["/todo/{userid}"], consumes = ["application/json"], produces = ["application/json"])
    fun addTodoToUser(@PathVariable userId: Long,
                      request: HttpServletRequest,
                      authentication: Authentication,
                      @Valid @RequestBody todo: Todo): ResponseEntity<*> {
        val user = userService.findByUsername(request.userPrincipal.name)
        if (user.userid == userId) {
            user.todos.add(todo)
        }
        return ResponseEntity(userService.save(user), HttpStatus.OK)
    } */


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = ["/user"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid @RequestBody newuser: User): ResponseEntity<*> {
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

        userService.update(updateUser, id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    fun deleteUserById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*> {
        userService.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping(value = ["/todo/{userid}"], consumes = ["application/json"], produces = ["application/json"])
    fun addTodoToUser(@PathVariable userid: Long,
                      request: HttpServletRequest,
                      authentication: Authentication,
                      @Valid @RequestBody todo: Todo): ResponseEntity<*> {
        val user = userService.findByUsername(request.userPrincipal.name)
        if (user.userid == userid) {
            user.todos.add(todo)
        } else {
            throw EntityNotFoundException(java.lang.Long.toString(userid) + " Not current user")
        }
        return ResponseEntity(userService.update(user, userid), HttpStatus.OK)
    }
}