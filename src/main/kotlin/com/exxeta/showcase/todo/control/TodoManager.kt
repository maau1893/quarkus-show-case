package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.TodoRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import io.smallrye.mutiny.Uni
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class TodoManager @Inject constructor(
    private val todoRepository: TodoRepository,
    private val todoMapper: TodoMapper,
) {

    private val logger: Logger = LoggerFactory.getLogger(TodoManager::class.simpleName)

    fun getAll(): Uni<List<TodoResponseDto>> {
        return todoRepository.findAll().stream().map(todoMapper::toResponseDto).collect().asList()
    }

    fun getTodoById(id: String): Uni<TodoResponseDto> {
        return Uni.createFrom().nullItem()
    }

    fun deleteTodoById(id: String): Uni<String> {
        return Uni.createFrom().nullItem()
    }

    fun createTodo(dto: TodoRequestDto): Uni<TodoResponseDto> {
        return Uni.createFrom().nullItem()
    }

    fun updateTodo(id: String, dto: TodoRequestDto): Uni<TodoResponseDto> {
        return Uni.createFrom().nullItem()
    }
}
