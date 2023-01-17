package com.exxeta.showcase.todo.api

import com.exxeta.showcase.common.model.ApiDescriptions
import com.exxeta.showcase.todo.control.TodoService
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.CreateTodoRequestDto
import com.exxeta.showcase.todo.model.UpdateTodoRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import java.util.UUID
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class TodoResource(private val todoService: TodoService) {

    @GET
    @Operation(description = ApiDescriptions.GET_TODOS)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "204")
        ]
    )
    fun getAll(): Uni<List<TodoResponseDto>> {
        return todoService.getAll()
    }

    @GET
    @Path("{id}")
    @Operation(description = ApiDescriptions.GET_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404")
        ]
    )
    fun getTodoById(@PathParam("id") id: UUID): Uni<TodoResponseDto> {
        return todoService.getTodoById(id)
    }

    @DELETE
    @Path("{id}")
    @Operation(description = ApiDescriptions.DELETE_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404"),
            APIResponse(responseCode = "500")
        ]
    )
    fun deleteTodoById(@PathParam("id") id: UUID): Uni<UUID> {
        return todoService.deleteTodoById(id)
    }

    @POST
    @Operation(description = ApiDescriptions.PUT_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "500")
        ]
    )
    fun createTodo(dto: @Valid CreateTodoRequestDto): Uni<TodoResponseDto> {
        return todoService.createTodo(dto)
    }

    @PUT
    @Path("{id}")
    @Operation(description = ApiDescriptions.POST_TODO)
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "404"),
            APIResponse(responseCode = "500")
        ]
    )
    fun updateTodo(@PathParam("id") id: UUID, @Valid dto: UpdateTodoRequestDto): Uni<TodoResponseDto> {
        return todoService.updateTodo(id, dto)
    }
}
