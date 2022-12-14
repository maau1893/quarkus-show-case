package com.exxeta.showcase.todo.model

import io.quarkus.runtime.annotations.RegisterForReflection
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todo")
@RegisterForReflection
class Todo {

    @Id
    @GeneratedValue
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "description")
    var description: String = ""

    @Column(name = "done")
    var isDone: Boolean = false

    @CreationTimestamp
    @Column(name = "created_at")
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(name = "updated_at")
    lateinit var updatedAt: LocalDateTime
}
