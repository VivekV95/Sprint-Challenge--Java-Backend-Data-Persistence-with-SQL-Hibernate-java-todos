package com.vivekvishwanath.todos.repository

import com.vivekvishwanath.todos.model.Role
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface RoleRepository : CrudRepository<Role, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE from UserRoles where userid = :userid")
    fun deleteUserRolesByUserId(userid: Long)

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO UserRoles(userid, roleid) values (:userid, :roleid)", nativeQuery = true)
    fun insertUserRoles(userid: Long, roleid: Long)
}
