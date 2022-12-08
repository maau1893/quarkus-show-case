package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import com.exxeta.showcase.todo.model.TodoRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface TodoMapper {

    fun toResponseDto(entity: Todo): TodoResponseDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "description", target = "description")
    fun toEntity(requestDto: TodoRequestDto): Todo
}
