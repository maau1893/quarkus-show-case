package com.exxeta.showcase.todo.control

import com.exxeta.showcase.common.model.exception.NoContentException
import com.exxeta.showcase.todo.model.TodoCreateRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoUpdateRequestDto
import io.smallrye.mutiny.Uni
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.InternalServerErrorException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class TodoManager @Inject constructor(
    private val todoRepository: TodoRepository,
    private val todoMapper: TodoMapper,
) {
    private val logger: Logger = LoggerFactory.getLogger(TodoManager::class.simpleName)

    fun getAll(): Uni<List<TodoResponseDto>> {
        logger.info("Requesting all Todos")
        return todoRepository.findAll().stream()
            .onCompletion().ifEmpty().fail().map(todoMapper::toResponseDto)
            .collect().asList().invoke { result -> logger.info("Found ${result.size} results") }
            .onFailure().transform {
                logger.error("No items found")
                NoContentException("Not items found")
            }
    }

    fun getTodoById(id: String): Uni<TodoResponseDto> {
        logger.info("Requesting Todo with id $id")
        return todoRepository.findById(id).map(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("Todo for id $id successfully found") }
            .onFailure().transform {
            logger.error("Failed to fetch Todo with id $id")
                NotFoundException("Todo with id $id does not exist")
            }
    }

    fun deleteTodoById(id: String): Uni<String> {
        logger.info("Attempting to delete Todo with id $id")
        return todoRepository.deleteById(id)
            .invoke { _ -> logger.info("Todo with id $id successfully deleted") }
            .onFailure().transform {
                logger.error("Failed to delete Todo with id $id")
                NotFoundException("Todo with id $id does not exist")
            }
    }

    fun createTodo(dto: TodoCreateRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to create Todo")
        return todoRepository.persistAndFlush(todoMapper.toEntity(dto)).map(todoMapper::toResponseDto)
            .invoke { todo -> logger.info("Todo successfully created - Id: ${todo.id}") }
            .onFailure().transform {
                logger.error("Failed to create Todo")
                InternalServerErrorException("Failed to create Todo")
            }
    }

    fun updateTodo(id: String, dto: TodoUpdateRequestDto): Uni<TodoResponseDto> {
        logger.info("Attempting to update Todo with id $id")
        return todoRepository.updateAndFlush(id, todoMapper.toEntity(dto)).map(todoMapper::toResponseDto)
            .invoke { _ -> logger.info("Todo with id $id successfully updated") }
            .onFailure().transform {
                logger.error("Failed to update Todo with id $id")
                InternalServerErrorException("Failed to update Todo with id $id")
            }
    }
}
