package com.vivekvishwanath.todos

import com.vivekvishwanath.todos.model.Role
import com.vivekvishwanath.todos.model.Todo
import com.vivekvishwanath.todos.model.User
import com.vivekvishwanath.todos.model.UserRoles
import com.vivekvishwanath.todos.repository.RoleRepository
import com.vivekvishwanath.todos.repository.ToDoRepository
import com.vivekvishwanath.todos.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import java.util.ArrayList
import java.util.Date

@Transactional
@Component
class SeedData(internal var rolerepos: RoleRepository, internal var userrepos: UserRepository, internal var todorepos: ToDoRepository) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(args: Array<String>) {
        val r1 = Role("admin")
        val r2 = Role("user")

        val admins = ArrayList<UserRoles>()
        admins.add(UserRoles(User(), r1))
        admins.add(UserRoles(User(), r2))

        val users = ArrayList<UserRoles>()
        users.add(UserRoles(User(), r2))

        rolerepos.save(r1)
        rolerepos.save(r2)

        val u1 = User("barnbarn", "password", users)
        val u2 = User("admin", "password", admins)

        // the date and time string should get coverted to a datetime Java data type. This is done in the constructor!
        u1.todos.add(Todo("Finish java-orders-swagger", Date(), u1))
        u1.todos.add(Todo("Feed the turtles", Date(), u1))
        u1.todos.add(Todo("Complete the sprint challenge", Date(), u1))

        u2.todos.add(Todo("Walk the dogs", Date(), u2))
        u2.todos.add(Todo("provide feedback to my instructor", Date(), u2))

        userrepos.save(u1)
        userrepos.save(u2)
    }
}
