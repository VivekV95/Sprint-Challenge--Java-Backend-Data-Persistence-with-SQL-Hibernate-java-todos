package com.vivekvishwanath.todos.service

import com.vivekvishwanath.todos.model.Role

interface RoleService {
    fun findAll(): List<Role>

    fun findRoleById(id: Long): Role

    fun delete(id: Long)

    fun save(role: Role): Role
}