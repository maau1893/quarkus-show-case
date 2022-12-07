package com.exxeta.showcase.todo.model

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todo")
@RegisterForReflection
class Todo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String = ""

    var name: String = ""
}