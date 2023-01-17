package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepositoryBase
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TodoRepository : PanacheRepositoryBase<Todo, UUID>
