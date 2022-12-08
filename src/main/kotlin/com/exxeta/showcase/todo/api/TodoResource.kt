package com.exxeta.showcase.todo.api

import com.exxeta.showcase.common.ApiDescriptions
import com.exxeta.showcase.todo.control.TodoManager
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoRequestDto
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/todos")
class TodoResource @Inject constructor(private val todoManager: TodoManager) {

    @GET
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.GET_TODOS)
    fun getAll(): Uni<List<TodoResponseDto>> {
        return todoManager.getAll()
    }

    @GET
    @Path("/{id}")
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.GET_TODO)
    fun getTodoById(@PathParam("id") id: String): Uni<TodoResponseDto> {
        return todoManager.getTodoById(id)
    }

    @DELETE
    @Path("/{id}")
    @ReactiveTransactional
    @Operation(description = ApiDescriptions.DELETE_TODO)
    fun deleteTodoById(@PathParam("id") id: String): Uni<String> {
        return todoManager.deleteTodoById(id)
    }

    @PUT
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.PUT_TODO)
    fun createTodo(dto: TodoRequestDto): Uni<TodoResponseDto> {
        return todoManager.createTodo(dto)
    }

    @POST
    @Path("/{id}")
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.POST_TODO)
    fun updateTodo(@PathParam("id") id: String, dto: TodoRequestDto): Uni<TodoResponseDto> {
        return todoManager.updateTodo(id, dto)
    }
}
