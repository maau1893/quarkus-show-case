package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TodoRepository : PanacheRepository<Todo> {

    fun findById(id: String): Uni<Todo> {
        return this.find("id", id).singleResult().onFailure().recoverWithNull()
    }

    fun deleteById(id: String): Uni<String> {
        return findById(id).onItem().transform { entity -> this.delete(entity) }.map { id }
    }

    fun updateAndFlush(id: String, todo: Todo): Uni<Todo> {
        return findById(id).onItem().transform { entity ->
            entity.description = todo.description
            entity.isDone = todo.isDone
            return@transform entity
        }.onItem().call { entity ->
            persistAndFlush(entity)
        }
    }
}
