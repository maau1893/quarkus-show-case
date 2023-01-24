package com.exxeta.showcase.todo.control

import com.exxeta.showcase.common.control.logAndFailWith
import com.exxeta.showcase.todo.model.CreateTodoRequestDto
import com.exxeta.showcase.todo.model.Todo
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.UpdateTodoRequestDto
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.unchecked.Unchecked
import org.slf4j.Logger
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.NotFoundException

@ApplicationScoped
class TodoService(
    private val todoRepository: TodoRepository,
    private val todoMapper: TodoMapper,
    private val logger: Logger,
) {

    fun getAll(): Uni<List<TodoResponseDto>> {
        logger.info("Requesting all ${Todo.tag} entities")
        return todoRepository.findAll().stream()
            .map(todoMapper::toResponseDto)
            .collect().asList().invoke { result -> logger.info("Found ${result.size} results") }
    }

    fun getTodoById(id: UUID): Uni<TodoResponseDto> {
        logger.info("Requesting ${Todo.tag} with id $id")
        return todoRepository.findById(id).onItem().ifNull()
            .logAndFailWith(logger, NotFoundException("${Todo.tag} with id $id does not exist"))
            .onItem().ifNotNull().transform(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("${Todo.tag} for id $id successfully found") }
    }

    @ReactiveTransactional
    fun deleteTodoById(id: UUID): Uni<UUID> {
        logger.info("Attempting to delete ${Todo.tag} with id $id")
        return todoRepository.deleteById(id)
            .onItem()
            .invoke(Unchecked.consumer { result ->
                if (!result) {
                    val message = "${Todo.tag} with id $id does not exist"
                    logger.error(message)
                    throw NotFoundException(message)
                }
            })
            .onItem()
            .transform { id }
            .invoke { _ -> logger.info("${Todo.tag} with id $id successfully deleted") }
    }

    @ReactiveTransactional
    fun createTodo(dto: CreateTodoRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to create ${Todo.tag}")
        return todoRepository.persistAndFlush(todoMapper.toEntity(dto)).map(todoMapper::toResponseDto)
            .invoke { todo -> logger.info("${Todo.tag} successfully created with id ${todo.id}") }
    }

    @ReactiveTransactional
    fun updateTodo(id: UUID, dto: UpdateTodoRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to update ${Todo.tag} with id $id")
        return todoRepository.findById(id).onItem().ifNull()
            .logAndFailWith(logger, NotFoundException("${Todo.tag} with id $id not found"))
            .onItem().ifNotNull()
            .transform { entity -> entity.copy(todoMapper.toEntity(dto)) }
            .map(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("${Todo.tag} with id $id successfully updated") }
    }
}
