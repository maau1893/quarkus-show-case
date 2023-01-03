package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import com.exxeta.showcase.todo.model.TodoCreateRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.TodoUpdateRequestDto
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@QuarkusTest
internal class TodoMapperTest {

    @Inject
    private lateinit var todoMapper: TodoMapper

    @Test
    fun toResponseDto() {
        val id = UUID.randomUUID()
        val createdAt = LocalDateTime.now()
        val updatedAt = LocalDateTime.now()
        val description = "Test description"
        val expected = TodoResponseDto(
            id = id,
            isDone = true,
            createdAt = createdAt,
            updatedAt = updatedAt,
            description = description
        )
        val entity = Todo().apply {
            this.id = id
            this.isDone = true
            this.createdAt = createdAt
            this.updatedAt = updatedAt
            this.description = "Test description"
        }
        val result = todoMapper.toResponseDto(entity)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `toEntity from TodoCreateRequestDto`() {
        val requestDto = TodoCreateRequestDto("Test description")

        val result = todoMapper.toEntity(requestDto)

        Assertions.assertEquals(requestDto.description, result.description)
    }

    @Test
    fun `toEntity from TodoUpdateRequestDto`() {
        val updateDto = TodoUpdateRequestDto("Test description update", isDone = true)

        val result = todoMapper.toEntity(updateDto)

        Assertions.assertEquals(updateDto.description, result.description)
        Assertions.assertEquals(updateDto.isDone, result.isDone)
    }
}
