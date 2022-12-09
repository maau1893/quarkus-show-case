package com.exxeta.showcase.todo.api

import com.exxeta.showcase.common.ApiDescriptions
import com.exxeta.showcase.todo.control.TodoManager
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoCreateRequestDto
import com.exxeta.showcase.todo.model.TodoUpdateRequestDto
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
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
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "204")
        ]
    )
    fun getAll(): Uni<List<TodoResponseDto>> {
        return todoManager.getAll()
    }

    @GET
    @Path("/{id}")
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.GET_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404")
        ]
    )
    fun getTodoById(@PathParam("id") id: String): Uni<TodoResponseDto> {
        return todoManager.getTodoById(id)
    }

    @DELETE
    @Path("/{id}")
    @ReactiveTransactional
    @Operation(description = ApiDescriptions.DELETE_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404"),
            APIResponse(responseCode = "500")
        ]
    )
    fun deleteTodoById(@PathParam("id") id: String): Uni<String> {
        return todoManager.deleteTodoById(id)
    }

    @PUT
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.PUT_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "500")
        ]
    )
    fun createTodo(dto: TodoCreateRequestDto): Uni<TodoResponseDto> {
        return todoManager.createTodo(dto)
    }

    @POST
    @Path("/{id}")
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.POST_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404"),
            APIResponse(responseCode = "500")
        ]
    )
    fun updateTodo(@PathParam("id") id: String, dto: TodoUpdateRequestDto): Uni<TodoResponseDto> {
        return todoManager.updateTodo(id, dto)
    }
}
