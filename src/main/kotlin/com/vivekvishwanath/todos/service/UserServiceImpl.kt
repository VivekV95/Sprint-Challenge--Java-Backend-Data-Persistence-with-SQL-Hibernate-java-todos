package com.vivekvishwanath.todos.service

import com.vivekvishwanath.todos.model.User
import com.vivekvishwanath.todos.model.UserRoles
import com.vivekvishwanath.todos.repository.RoleRepository
import com.vivekvishwanath.todos.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityNotFoundException
import java.util.ArrayList


@Service(value = "userService")
class UserServiceImpl : UserDetailsService, UserService {

    @Autowired
    lateinit var userrepos: UserRepository

    @Autowired
    lateinit var rolerepos: RoleRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userrepos.findByUsername(username)
                ?: throw UsernameNotFoundException("Invalid username or password.")
        return org.springframework.security.core.userdetails.User(user.username!!, user.getPassword()!!, user.authority)
    }

    @Throws(EntityNotFoundException::class)
    override fun findUserById(id: Long): User {
        return userrepos.findById(id)
                .orElseThrow { EntityNotFoundException(java.lang.Long.toString(id)) }
    }

    override fun findAll(): List<User> {
        val list = ArrayList<User>()
        userrepos.findAll().iterator().forEachRemaining{ list.add(it) }
        return list
    }

    override fun delete(id: Long) {
        if (userrepos.findById(id).isPresent) {
            userrepos.deleteById(id)
        } else {
            throw EntityNotFoundException(java.lang.Long.toString(id))
        }
    }

    @Transactional
    override fun save(user: User): User {
        val newUser = User()
        newUser.username = user.username
        user.getPassword()?.let { newUser.setPasswordNoEncrypt(it) }

        val newRoles = ArrayList<UserRoles>()
        for (ur in user.userRoles) {
            ur.role?.let { UserRoles(newUser, it) }?.let { newRoles.add(it) }
        }
        newUser.userRoles = newRoles

        for (todo in user.todos) {
            newUser.todos.add(todo)
            todo.user = newUser
        }
        return userrepos.save(newUser)
    }

    @Transactional
    override fun update(user: User, id: Long): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userrepos!!.findByUsername(authentication.name)

        if (currentUser != null) {
            if (id == currentUser.userid) {
                if (user.username != null) {
                    currentUser.username = user.username
                }

                if (user.getPassword() != null) {
                    currentUser.setPasswordNoEncrypt(user.getPassword()!!)
                }

                if (user.userRoles.size > 0) {
                    // with so many relationships happening, I decided to go
                    // with old school queries
                    // delete the old ones
                    rolerepos.deleteUserRolesByUserId(currentUser.userid)

                    // add the new ones
                    for (ur in user.userRoles) {
                        rolerepos.insertUserRoles(id, ur.role!!.roleid)
                    }
                }

                return userrepos.save(currentUser)
            } else {
                throw EntityNotFoundException(java.lang.Long.toString(id) + " Not current user")
            }
        } else {
            throw EntityNotFoundException(authentication.name)
        }

    }

    override fun findByUsername(username: String): User {
        return userrepos.findByUsername(username)
    }
}
