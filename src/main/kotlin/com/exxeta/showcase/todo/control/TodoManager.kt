package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.TodoCreateRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoUpdateRequestDto
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.unchecked.Unchecked
import org.slf4j.Logger
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.InternalServerErrorException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class TodoManager(
    private val todoRepository: TodoRepository,
    private val todoMapper: TodoMapper,
    private val logger: Logger,
) {

    fun getAll(): Uni<List<TodoResponseDto>> {
        logger.info("Requesting all Todos")
        return todoRepository.findAll().stream()
            .map(todoMapper::toResponseDto)
            .collect().asList().invoke { result -> logger.info("Found ${result.size} results") }
    }

    fun getTodoById(id: UUID): Uni<TodoResponseDto> {
        logger.info("Requesting Todo with id $id")
        return todoRepository.findById(id).map(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("Todo for id $id successfully found") }
            .onFailure().transform {
                logger.error("Failed to fetch Todo with id $id", it)
                NotFoundException("Todo with id $id does not exist")
            }
    }

    fun deleteTodoById(id: UUID): Uni<UUID> {
        logger.info("Attempting to delete Todo with id $id")
        return todoRepository.deleteById(id)
            .onItem()
            .invoke(Unchecked.consumer { result ->
                if (!result) {
                    logger.error("Failed to delete Todo with id $id")
                    throw NotFoundException("Todo with id $id does not exist")
                }
            })
            .onItem()
            .transform { id }
            .invoke { _ -> logger.info("Todo with id $id successfully deleted") }
    }

    fun createTodo(dto: TodoCreateRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to create Todo")
        return todoRepository.persistAndFlush(todoMapper.toEntity(dto)).map(todoMapper::toResponseDto)
            .invoke { todo -> logger.info("Todo successfully created with id ${todo.id}") }
            .onFailure().transform {
                logger.error("Failed to create Todo", it)
                InternalServerErrorException("Failed to create Todo")
            }
    }

    fun updateTodo(id: UUID, dto: TodoUpdateRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to update Todo with id $id")
        return todoRepository.updateAndFlush(id, todoMapper.toEntity(dto)).map(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("Todo with id $id successfully updated") }
            .onFailure().transform {
                logger.error("Failed to update Todo with id $id", it)
                InternalServerErrorException("Failed to update Todo with id $id")
            }
    }
}
