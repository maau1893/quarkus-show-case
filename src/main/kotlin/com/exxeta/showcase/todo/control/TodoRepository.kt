package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepositoryBase
import io.smallrye.mutiny.Uni
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TodoRepository : PanacheRepositoryBase<Todo, UUID> {

    fun updateAndFlush(id: UUID, todo: Todo): Uni<Todo> {
        return findById(id).onItem().transform { entity ->
            entity.description = todo.description
            entity.isDone = todo.isDone
            return@transform entity
        }.onItem().call { entity ->
            persistAndFlush(entity)
        }
    }
}
