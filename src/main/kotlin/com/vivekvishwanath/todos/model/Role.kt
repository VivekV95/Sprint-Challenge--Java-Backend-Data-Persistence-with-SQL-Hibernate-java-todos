package com.vivekvishwanath.todos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.*
import java.util.ArrayList

@Entity
@Table(name = "roles")
class Role : Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var roleid: Long = 0

    @Column(nullable = false, unique = true)
    var name: String? = null

    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL])
    @JsonIgnoreProperties("role")
    var userRoles: List<UserRoles> = ArrayList()

    constructor() {}

    constructor(name: String) {
        this.name = name
    }
}
