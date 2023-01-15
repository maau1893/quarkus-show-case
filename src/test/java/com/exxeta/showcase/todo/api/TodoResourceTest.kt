package com.exxeta.showcase.todo.api

import com.exxeta.showcase.todo.control.TodoManager
import com.exxeta.showcase.todo.model.TodoCreateRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoUpdateRequestDto
import io.mockk.every
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.smallrye.mutiny.Uni
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@QuarkusTest
@TestHTTPEndpoint(TodoResource::class)
@Suppress("ReactiveStreamsUnusedPublisher")
internal class TodoResourceTest {

    private val todoResponseDto: TodoResponseDto = TodoResponseDto(
        id = UUID.randomUUID(),
        description = "Test description",
        isDone = false,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )

    @InjectMock
    private lateinit var todoManager: TodoManager


    @Test
    fun getAll() {
        val expected = listOf(todoResponseDto)

        every { todoManager.getAll() } returns Uni.createFrom().item(expected)

        val result = When {
            get()
        } Then {
            statusCode(Response.Status.OK.statusCode)
            contentType(MediaType.APPLICATION_JSON)
        } Extract {
            jsonPath().getList(".", TodoResponseDto::class.java)
        }

        verify { todoManager.getAll() }

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun getTodoById() {
        val id = todoResponseDto.id

        val response = Uni.createFrom().item(todoResponseDto)

        every { todoManager.getTodoById(id) } returns response

        val result = When {
            get(id.toString())
        } Then {
            statusCode(Response.Status.OK.statusCode)
            contentType(MediaType.APPLICATION_JSON)
        } Extract {
            `as`(TodoResponseDto::class.java)
        }

        verify { todoManager.getTodoById(id) }

        Assertions.assertEquals(todoResponseDto, result)
    }

    @Test
    fun deleteTodoById() {
        val id = UUID.randomUUID()

        every { todoManager.deleteTodoById(id) } returns Uni.createFrom().item(id)

        val result = When {
            delete("{id}", id)
        } Then {
            statusCode(Response.Status.OK.statusCode)
            contentType(MediaType.APPLICATION_JSON)
        } Extract {
            `as`(UUID::class.java)
        }

        verify { todoManager.deleteTodoById(id) }

        Assertions.assertEquals(id, result)
    }

    @Test
    fun createTodo() {
        val requestDto = TodoCreateRequestDto(description = "Test description")

        every { todoManager.createTodo(requestDto) } returns Uni.createFrom().item(todoResponseDto)

        val result = Given {
            body(requestDto)
            contentType(MediaType.APPLICATION_JSON)
        } When {
            post()
        } Then {
            statusCode(Response.Status.OK.statusCode)
            contentType(MediaType.APPLICATION_JSON)
        } Extract {
            `as`(TodoResponseDto::class.java)
        }

        verify { todoManager.createTodo(requestDto) }

        Assertions.assertEquals(todoResponseDto, result)
    }

    @Test
    fun updateTodo() {
        val id = todoResponseDto.id

        val requestDto = TodoUpdateRequestDto(description = "Test description 2", isDone = true)

        val responseDto = TodoResponseDto(
            id = todoResponseDto.id,
            description = requestDto.description,
            isDone = requestDto.isDone,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { todoManager.updateTodo(id, requestDto) } returns Uni.createFrom().item(responseDto)

        val result = Given {
            body(requestDto)
            contentType(MediaType.APPLICATION_JSON)
        } When {
            put("{id}", id)
        } Then {
            statusCode(Response.Status.OK.statusCode)
            contentType(MediaType.APPLICATION_JSON)
        } Extract {
            `as`(TodoResponseDto::class.java)
        }

        verify { todoManager.updateTodo(id, requestDto) }

        Assertions.assertEquals(responseDto, result)
    }
}
