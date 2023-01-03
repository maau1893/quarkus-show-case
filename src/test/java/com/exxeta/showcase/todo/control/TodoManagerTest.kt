package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkiverse.test.junit.mockk.InjectSpy
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@QuarkusTest
@Suppress("ReactiveStreamsUnusedPublisher")
internal class TodoManagerTest {
    
    @Inject
    private lateinit var todoManager: TodoManager

    @InjectMock
    private lateinit var todoRepository: TodoRepository

    @InjectSpy
    private lateinit var todoMapper: TodoMapper

    private val todo: Todo = Todo().apply {
        this.id = UUID.randomUUID()
        this.createdAt = LocalDateTime.now()
        this.updatedAt = LocalDateTime.now()
        this.isDone = true
        this.description = "Test description"
    }

    @Test
    fun getAll() {
        /*val expected = listOf(todoMapper.toResponseDto(todo))

        every { todoRepository.findAll() } returns mockk()

        val subscriber = todoManager.getAll().subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        verify { todoMapper.toResponseDto(todo) }

        Assertions.assertEquals(expected, subscriber.item)*/
    }

    @Test
    fun getTodoById() {
    }

    @Test
    fun deleteTodoById() {
    }

    @Test
    fun createTodo() {
    }

    @Test
    fun updateTodo() {
    }
}
