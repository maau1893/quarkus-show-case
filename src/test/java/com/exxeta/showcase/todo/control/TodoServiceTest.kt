package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import com.exxeta.showcase.todo.model.CreateTodoRequestDto
import com.exxeta.showcase.todo.model.UpdateTodoRequestDto
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheQuery
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.UUID
import javax.ws.rs.NotFoundException

@Suppress("ReactiveStreamsUnusedPublisher")
internal class TodoServiceTest {

    private lateinit var todoService: TodoService

    private lateinit var todoRepository: TodoRepository

    private lateinit var todoMapper: TodoMapper

    private val todo: Todo = Todo().apply {
        this.id = UUID.randomUUID()
        this.createdAt = ZonedDateTime.now()
        this.updatedAt = ZonedDateTime.now()
        this.isDone = true
        this.description = "Test description"
    }

    @BeforeEach
    fun beforeEach() {
        todoRepository = mockk()
        todoMapper = TodoMapperImpl()
        todoService = TodoService(todoRepository, todoMapper, LoggerFactory.getLogger(TodoService::class.java))
    }

    @AfterEach
    fun afterEach() {
        clearMocks(todoRepository)
    }

    @Test
    fun getAll() {
        val item = todoMapper.toResponseDto(todo)
        val expected = listOf(item, item)

        val queryMock = mockk<PanacheQuery<Todo>>()

        every { todoRepository.findAll() } returns queryMock
        every { queryMock.stream() } returns Multi.createFrom().items(todo, todo)

        val subscriber = todoService.getAll().subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        Assertions.assertEquals(expected, subscriber.item)
    }

    @Test
    fun getTodoById() {
        val expected = todoMapper.toResponseDto(todo)

        every { todoRepository.findById(todo.id) } returns Uni.createFrom().item(todo)

        val subscriber = todoService.getTodoById(todo.id).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        Assertions.assertEquals(expected, subscriber.item)
    }

    @Test
    fun `deleteTodoById with success`() {
        every { todoRepository.deleteById(todo.id) } returns Uni.createFrom().item(true)

        val subscriber = todoService.deleteTodoById(todo.id).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        Assertions.assertEquals(todo.id, subscriber.item)
    }

    @Test
    fun `deleteTodoById with failure`() {
        every { todoRepository.deleteById(todo.id) } returns Uni.createFrom().item(false)

        val subscriber = todoService.deleteTodoById(todo.id).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertFailedWith(NotFoundException::class.java)
    }

    @Test
    fun `createTodo with success`() {
        val dto = CreateTodoRequestDto(description = "Test description")
        val expected = todoMapper.toResponseDto(todo)

        every { todoRepository.persist(any() as Todo) } returns Uni.createFrom().item(todo)

        val subscriber = todoService.createTodo(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        Assertions.assertEquals(expected, subscriber.item)
    }

    @Test
    fun `updateTodo with success`() {
        val dto = UpdateTodoRequestDto(description = "Test description", isDone = true)
        val expected = todoMapper.toResponseDto(todo)

        every { todoRepository.findById(todo.id) } returns Uni.createFrom().item(todo)

        val subscriber = todoService.updateTodo(todo.id, dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        Assertions.assertEquals(expected, subscriber.item)
    }

    @Test
    fun `updateTodo with failure`() {
        val dto = UpdateTodoRequestDto(description = "Test description", isDone = true)

        every { todoRepository.findById(todo.id) } returns Uni.createFrom().failure(NotFoundException("Entity not found"))

        val subscriber = todoService.updateTodo(todo.id, dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertFailedWith(NotFoundException::class.java)

        Assertions.assertNull(subscriber.item)
    }
}
