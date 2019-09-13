package com.vivekvishwanath.todos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*


 @Entity
@Table(name = "todos")
class Todo: Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var totoid: Long? = 0

    @Column(nullable = false)
    var description: String? = ""

    var datecreated: String? = ""
     get() {
         return SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date())
     }

    var completed: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties("todos")
    var user: User? = null

    constructor()

    constructor(description: String, date: Date, user: User) {
        this.description = description
        this.datecreated = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(date)
        this.user = user
    }
}