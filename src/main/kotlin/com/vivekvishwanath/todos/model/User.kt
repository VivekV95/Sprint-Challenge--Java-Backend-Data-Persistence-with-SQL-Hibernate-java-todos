package com.vivekvishwanath.todos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.persistence.*
import java.util.ArrayList

// User is considered the parent entity

@Entity
@Table(name = "users")
class User : Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var userid: Long = 0

    @Column(nullable = false, unique = true)
    var username: String? = null

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private var password: String? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    @JsonIgnoreProperties("user")
    var userRoles: List<UserRoles> = ArrayList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL],
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user")
    var todos: MutableList<Todo> = mutableListOf()

    val authority: List<SimpleGrantedAuthority>
        get() {
            val rtnList = ArrayList<SimpleGrantedAuthority>()

            for (r in this.userRoles) {

                val myRole = "ROLE_" + r.role?.name?.toUpperCase()
                rtnList.add(SimpleGrantedAuthority(myRole))
            }

            return rtnList
        }

    constructor() {}

    constructor(username: String, password: String, userRoles: List<UserRoles>) {
        this.username = username
        setPassword(password)
        for (ur in userRoles) {
            ur.user = this
        }
        this.userRoles = userRoles
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        val passwordEncoder = BCryptPasswordEncoder()
        this.password = passwordEncoder.encode(password)
    }

    fun setPasswordNoEncrypt(password: String) {
        this.password = password
    }
}
